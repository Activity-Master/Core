package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.*;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications.*;

@SuppressWarnings("Duplicates")
@Log4j2
public class SecurityTokenService
    implements ISecurityTokenService<SecurityTokenService>
{
  @Inject
  private IClassificationService<?> classificationService;

  @Override
  public ISecurityToken<?, ?> get()
  {
    return new SecurityToken();
  }

  //@Transactional()
  @Override
  public Uni<Void> grantAccessToToken(Mutiny.Session session, ISecurityToken<?, ?> fromToken, ISecurityToken<?, ?> toToken,
                                      boolean create, boolean update, boolean delete, boolean read, ISystems<?, ?> system)
  {
    return grantAccessToToken(session, fromToken, toToken, create, update, delete, read, system, null, null, null);
  }

  //@Transactional()
  @Override
  public Uni<Void> grantAccessToToken(Mutiny.Session session, @NotNull ISecurityToken<?, ?> fromToken, @NotNull ISecurityToken<?, ?> toToken,
                                      boolean create, boolean update, boolean delete, boolean read,
                                      ISystems<?, ?> system, String originalId,
                                      Date effectiveFromDate, Date effectiveToDate)
  {
    SecurityTokensSecurityToken sta = new SecurityTokensSecurityToken();
    var enterprise = system.getEnterprise();
    return sta.builder(session)
               .withEnterprise(enterprise)
               .inActiveRange()
               .inDateRange()
               .findBySecurityToken((SecurityToken) fromToken, (SecurityToken) toToken)
               .get()
               .onFailure(NoResultException.class)
               .recoverWithUni(() -> {
                 sta.setSystemID((Systems) system);
                 sta.setOriginalSourceSystemID((Systems) system);
                 sta.setEnterpriseID(enterprise);
                 sta.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                 return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                              sta.setActiveFlagID((ActiveFlag) activeFlag);
                              sta.setSecurityTokenID((SecurityToken) fromToken);
                              sta.setBase((SecurityToken) toToken);
                              sta.setCreateAllowed(create);
                              sta.setUpdateAllowed(update);
                              sta.setDeleteAllowed(delete);
                              sta.setReadAllowed(read);
                              return session.persist(sta)
                                         .replaceWith(Uni.createFrom()
                                                          .item(sta));
                            });
               })
               .chain(result -> Uni.createFrom()
                                    .voidItem());
  }

  //@Transactional()
  @Override
  public Uni<ISecurityToken<?, ?>> create(Mutiny.Session session, String classificationValue, String name, String description, ISystems<?, ?> system)
  {
    return create(session, classificationValue, name, description, system, null);
  }

  //@Transactional()
  @Override
  public Uni<ISecurityToken<?, ?>> create(Mutiny.Session session, String classificationValue, String name, String description, ISystems<?, ?> system, ISecurityToken<?, ?> parent, UUID... identityToken)
  {
    var enterprise = system.getEnterprise();
    log.debug("🔐 Creating security token: '{}' for system: '{}' with session: {}",
        name, system.getName(), session.hashCode());

    return classificationService.find(session, classificationValue, system, identityToken)
               .chain(classification -> {
                 SecurityToken st = new SecurityToken();

                 // First try to find by security token and enterprise
                 return st.builder(session)
                            .withEnterprise(enterprise)
                            .findBySecurityToken(name, enterprise)
                            .inActiveRange()
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .get()
                            .onFailure(NoResultException.class)
                            .recoverWithNull()
                            .chain(existingToken -> {
                              if (existingToken != null)
                              {
                                log.debug("✅ Found existing security token: '{}' with ID: {}", existingToken.getName(), existingToken.getId());
                                return Uni.createFrom()
                                           .item(existingToken);
                              }

                              // Try to find by name
                              return st.builder(session)
                                         .withName(name)
                                         .inActiveRange()
                                         .inDateRange()
                                         .get()
                                         .onFailure(NoResultException.class)
                                         .recoverWithNull()
                                         .chain(existingNameToken -> {
                                           if (existingNameToken != null)
                                           {
                                             log.debug("✅ Found existing token with name: '{}' with ID: {}", existingNameToken.getName(), existingNameToken.getId());
                                             return Uni.createFrom()
                                                        .item(existingNameToken);
                                           }

                                           log.debug("🆕 Creating new security token: '{}'", name);
                                           // Create new token
                                           st.setName(name);
                                           st.setDescription(description);
                                           st.setSystemID(system);
                                           st.setSecurityToken(UUID.randomUUID()
                                                                   .toString());
                                           st.setEnterpriseID(enterprise);
                                           st.setSystemID(((Classification) classification).getSystemID());
                                           st.setOriginalSourceSystemID(((Classification) classification).getSystemID());
                                           st.setSecurityTokenClassificationID((Classification) classification);

                                           IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                           return acService.getActiveFlag(session, enterprise)
                                                      .chain(activeFlag -> {
                                                        st.setActiveFlagID((ActiveFlag) activeFlag);
                                                        return session.persist(st)
                                                                   .replaceWith(Uni.createFrom()
                                                                                    .item(st))
                                                                   .chain(persisted -> {
                                                                     // Start createDefaultSecurity in parallel without waiting for it
                                                                     return persisted.createDefaultSecurity(session, system, identityToken);
                                                                   }).replaceWith(st);
                                                      });
                                         });
                            })
                            .chain(securityToken -> {
                              if (parent == null)
                              {
                                return Uni.createFrom()
                                           .item(securityToken);
                              }

                              return link(session, parent, securityToken, (Classification) classification)
                                         .map(v -> securityToken);
                            });
               });
  }

  //@Transactional()
  @Override
  public Uni<Void> link(Mutiny.Session session, ISecurityToken<?, ?> parent, ISecurityToken<?, ?> child, IClassification<?, ?> classification, String... identifyingToken)
  {
    SecurityTokenXSecurityToken root = new SecurityTokenXSecurityToken();
    var enterprise = child.getEnterprise();
    log.debug("🔗 Linking security tokens: parent '{}' -> child '{}' with session: {}",
        parent.getName(), child.getName(), session.hashCode());

    return root.builder(session)
               .withEnterprise(enterprise)
               .findLink((SecurityToken) parent, (SecurityToken) child, null)
               .withClassification(classification)
               .inActiveRange()
               .inDateRange()
               .get()
               .onFailure(NoResultException.class)
               .recoverWithUni(() -> {
                 log.debug("🆕 Creating new security token link: parent '{}' -> child '{}'", parent.getName(), child.getName());
                 // No existing link found, create a new one
                 root.setParentSecurityTokenID((SecurityToken) parent);
                 root.setChildSecurityTokenID((SecurityToken) child);
                 root.setClassificationID(classification);
                 root.setSystemID(((SecurityToken) parent).getSystemID());
                 root.setOriginalSourceSystemID(((SecurityToken) parent).getSystemID());
                 root.setValue(child.getSecurityToken());
                 root.setEnterpriseID(enterprise);

                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                 return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                              root.setActiveFlagID((ActiveFlag) activeFlag);
                              return session.persist(root)
                                         .replaceWith(Uni.createFrom()
                                                          .item(root))
                                         .invoke(v -> {
                                           updateSecurityHierarchy(child.getId());
                                         });
                            });
               })
               .onItem()
               .invoke(existingLink -> {
                 if (existingLink != null)
                 {
                   log.debug("✅ Found existing security token link: parent '{}' -> child '{}'", parent.getName(), child.getName());
                 }
               })
               .chain(existingLink -> Uni.createFrom()
                                          .voidItem());
  }

  private void updateSecurityHierarchy(UUID securityTokenID)
  {
    //TODO hierarchy updates? i wonder
  }

  //@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
  @Override
  public Uni<ISecurityToken<?, ?>> getEveryoneGroup(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(UserGroup.toString(), system, identityToken)
               .withName(Everyone)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               //   .canRead(enterprise, identityToken)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
  @Override
  public Uni<ISecurityToken<?, ?>> getEverywhereGroup(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(UserGroup.toString(), system, identityToken)
               .withName(Everywhere)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               //      .canRead(enterprise, identityToken)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
  @Override
  public Uni<ISecurityToken<?, ?>> getGuestsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(UserGroup.toString(), system, identityToken)
               .withName(Guests)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               //     .canRead(enterprise,identityToken)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
  @Override
  public Uni<ISecurityToken<?, ?>> getRegisteredGuestsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(UserGroup.toString(), system, identityToken)
               .withName(Registered)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               //    .canRead(enterprise, identityToken)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
  @Override
  public Uni<ISecurityToken<?, ?>> getVisitorsGuestsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(UserGroup.toString(), system, identityToken)
               .withName(Visitors)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               //     .canRead(enterprise, identityToken)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
  @Override
  public Uni<ISecurityToken<?, ?>> getAdministratorsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(UserGroup.toString(), system, identityToken)
               .withName(Administrators)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               //  .canRead(enterprise, identityToken)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
  @Override
  public Uni<ISecurityToken<?, ?>> getSystemsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(UserGroupSecurityTokenClassifications.System.toString(), system, identityToken)
               .withName(System)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               //.canRead(enterprise, identityToken)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
  @Override
  public Uni<ISecurityToken<?, ?>> getPluginsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(Plugin.toString(), system, identityToken)
               .withName(Plugins)
               .inActiveRange()
               //  .canRead(enterprise, identityToken)
               .inDateRange()
               .withEnterprise(enterprise)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
  @Override
  public Uni<ISecurityToken<?, ?>> getApplicationsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityToken st = new SecurityToken();
    var enterprise = system.getEnterprise();
    return st.builder(session)
               .findFolder(Application.toString(), system, identityToken)
               .withName(Applications)
               .inActiveRange()
               //   .canRead(enterprise, identityToken)
               .inDateRange()
               .withEnterprise(enterprise)
               .get()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecurityGetSecurityToken")
  @Override
  public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.Session session, UUID identifyingToken, ISystems<?, ?> system, UUID... identityToken)
  {
    var enterprise = system.getEnterprise();
    return new SecurityToken().builder(session)
               .findBySecurityToken(identifyingToken.toString())
               .withEnterprise(enterprise)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               // .canRead(enterprise, identityToken)
               .get()
               .onFailure(NoResultException.class)
               .recoverWithNull()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }

  //@CacheResult(cacheName = "SecurityGetSecurityTokenNoActiveFlag")
  @Override
  public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.Session session, UUID identifyingToken, boolean overrideActiveFlag, ISystems<?, ?> system, UUID... identityToken)
  {
    SecurityTokenQueryBuilder builder = new SecurityToken().builder(session);
    var enterprise = system.getEnterprise();
    builder = builder.findBySecurityToken(identifyingToken.toString())
                  .withEnterprise(enterprise)
                  .inDateRange();
    if (overrideActiveFlag)
    {
      builder.inActiveRange();
    }

    return builder
               .get()
               .onFailure(NoResultException.class)
               .recoverWithNull()
               .onItem()
               .transform(token -> (ISecurityToken<?, ?>) token);
  }
}

