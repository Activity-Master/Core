package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * <p>
 * [✓] One action per Mutiny.Session at a time
 * - All operations on a session are sequential
 * - No parallel operations on the same session
 * <p>
 * [✓] Pass Mutiny.Session through the chain
 * - All methods accept session as parameter
 * - Session is passed to all dependent operations
 * <p>
 * [✓] No await() usage
 * - Using reactive chains instead of blocking operations
 * <p>
 * [✓] Synchronous execution of reactive chains
 * - All reactive chains execute synchronously
 * - createDefaultSecurity is properly chained with error handling
 * <p>
 * [✓] No parallel operations on a session
 * - Not using Uni.combine().all().unis() with operations that share the same session
 * <p>
 * [✓] No session/transaction creation in libraries
 * - Sessions are passed in from the caller
 * - No sessionFactory.withTransaction() in methods
 * <p>
 * See ReactivityMigrationGuide.md for more details on these rules.
 */

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.InvolvedPartyException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.persistence.criteria.JoinType;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import jakarta.persistence.NoResultException;

import java.util.*;

import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.enumerations.OrderByType.*;
import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;

@SuppressWarnings("unchecked")
@Log4j2
@Singleton
public class InvolvedPartyService implements IInvolvedPartyService<InvolvedPartyService>
{

  @Inject
  private IClassificationService<?> classificationService;

  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private Vertx vertx;

  @Override
  public IInvolvedParty<?, ?> get()
  {
    log.debug("Getting new InvolvedParty instance");
    return new InvolvedParty();
  }

  @Override
  public Uni<IInvolvedParty<?, ?>> findByID(Mutiny.Session session, UUID id)
  {
    log.debug("🔍 Finding InvolvedParty by ID: {} with session: {}", id, session.hashCode());
    return (Uni) new InvolvedParty().builder(session)
                     .find(id)
                     .get();
  }

  @Override
  public Uni<IInvolvedPartyNameType<?, ?>> createNameType(Mutiny.Session session, String name, String description, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Creating InvolvedPartyNameType: name={}, description={}", name, description);
    var enterprise = system.getEnterprise();

    // Find-or-create using NoResultException handling (finders never return null)
    return (Uni) findInvolvedPartyNameType(session, name, system, identityToken)
               .onItem()
               .invoke(found -> log.debug("InvolvedPartyNameType already exists: {}", name))
               .onFailure(NoResultException.class)
               .recoverWithUni(err -> {
                 log.debug("Creating new InvolvedPartyNameType: {} (not found)", name);
                 InvolvedPartyNameType xr = new InvolvedPartyNameType();
                 xr.setName(name);
                 xr.setDescription(description);
                 xr.setSystemID(system);
                 xr.setOriginalSourceSystemID(system.getId());
                 xr.setEnterpriseID(enterprise);

                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                 return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                              xr.setActiveFlagID(activeFlag);
                              return session.persist(xr)
                                         .replaceWith(Uni.createFrom().item(xr));
                            })
                            .chain(persisted -> {
                              // Handle security setup sequentially on the same session/thread
                              return persisted.createDefaultSecurity(session, system, identityToken)
                                         .onItem()
                                         .invoke(result -> log.debug("Security setup completed successfully for name type {}", persisted.getName()))
                                         .onFailure()
                                         .recoverWithItem(error2 -> {
                                           log.warn("Error in createDefaultSecurity for name type", error2);
                                           return null; // Continue chain even if security creation fails
                                         })
                                         .chain(() -> Uni.createFrom().item((IInvolvedPartyNameType<?, ?>) persisted));
                            });
               });
  }

  @Override
  public Uni<IInvolvedPartyIdentificationType<?, ?>> createIdentificationType(Mutiny.Session session, ISystems<?, ?> system, String name, String description, UUID... identityToken)
  {
    log.debug("Creating InvolvedPartyIdentificationType: name={}, description={}", name, description);
    var enterprise = system.getEnterprise();

    // Find-or-create using NoResultException handling (finders never return null)
    return (Uni) findInvolvedPartyIdentificationType(session, name, system, identityToken)
               .onItem()
               .invoke(found -> log.debug("InvolvedPartyIdentificationType already exists: {}", name))
               .onFailure(NoResultException.class)
               .recoverWithUni(err -> {
                 log.debug("Creating new InvolvedPartyIdentificationType: {} (not found)", name);
                 InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
                 xr.setName(name);
                 xr.setDescription(description);
                 xr.setSystemID(system);
                 xr.setOriginalSourceSystemID(system.getId());
                 xr.setEnterpriseID(enterprise);

                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                 return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                              xr.setActiveFlagID(activeFlag);
                              return session.persist(xr)
                                         .replaceWith(Uni.createFrom().item(xr));
                            })
                            .chain(persisted -> {
                              // Handle security setup sequentially on the same session/thread
                              return persisted.createDefaultSecurity(session, system, identityToken)
                                         .onItem()
                                         .invoke(result -> log.debug("Security setup completed successfully for identification type {}", persisted.getName()))
                                         .onFailure()
                                         .recoverWithItem(error2 -> {
                                           log.warn("Error in createDefaultSecurity for identification type", error2);
                                           return null; // Continue chain even if security creation fails
                                         })
                                         .chain(() -> Uni.createFrom().item((IInvolvedPartyIdentificationType<?, ?>) persisted));
                            });
               });
  }

  @Override
  public Uni<IInvolvedPartyType<?, ?>> createType(Mutiny.Session session, ISystems<?, ?> system, String name, String description, UUID... identityToken)
  {
    log.debug("Creating InvolvedPartyType: name={}, description={}", name, description);
    var enterprise = system.getEnterprise();

    // First try to find the entity
    return findType(session, name, system, identityToken)
               .onItem()
               .invoke(found -> log.debug("InvolvedPartyType already exists: {}", name))
               .onFailure(NoResultException.class)
               .recoverWithUni(err -> {
                 // Create new entity if not found
                 log.debug("Creating new InvolvedPartyType: {} (not found)", name);
                 InvolvedPartyType xr = new InvolvedPartyType();
                 xr.setName(name);
                 xr.setDescription(description);
                 xr.setSystemID(system);
                 xr.setOriginalSourceSystemID(system.getId());
                 xr.setEnterpriseID(enterprise);

                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                 return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                              xr.setActiveFlagID(activeFlag);
                              return session.persist(xr)
                                         .replaceWith(Uni.createFrom().item(xr));
                            })
                            .chain(persisted -> {
                              // Get activity master system and handle security setup sequentially
                              return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
                                         .chain(activityMasterSystem -> {
                                           return persisted.createDefaultSecurity(session, activityMasterSystem, identityToken)
                                                      .onItem()
                                                      .invoke(result -> log.debug("Security setup completed successfully for type {}", persisted.getName()))
                                                      .onFailure()
                                                      .recoverWithItem(error2 -> {
                                                        log.warn("Error in createDefaultSecurity for type", error2);
                                                        return null; // Continue the chain even if security creation fails
                                                      })
                                                      .chain(() -> Uni.createFrom().item((IInvolvedPartyType<?, ?>) persisted));
                                         });
                            });
               });
  }

  private Uni<InvolvedPartyOrganicType> createOrganicType(Mutiny.Session session, ISystems<?, ?> system, UUID key, String name, String description, UUID... identityToken)
  {
    log.debug("Creating InvolvedPartyOrganicType: name={}, description={}", name, description);
    var enterprise = system.getEnterprise();
    InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
    xr.setId(key);
    xr.setName(name);
    xr.setDescription(description);
    xr.setSystemID(system);
    xr.setOriginalSourceSystemID(system.getId());
    xr.setEnterpriseID(enterprise);

    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
    return acService.getActiveFlag(session, enterprise)
               .chain(activeFlag -> {
                 xr.setActiveFlagID(activeFlag);
                 return session.persist(xr)
                            .replaceWith(Uni.createFrom()
                                             .item(xr));
               })
               .chain(persisted -> {
                 // Get activity master system and handle security setup sequentially
                 return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
                            .chain(activityMasterSystem -> {
                              return persisted.createDefaultSecurity(session, activityMasterSystem, identityToken)
                                         .onItem()
                                         .invoke(result ->
                                                     log.debug("Security setup completed successfully for organic type {}", persisted.getName())
                                         )
                                         .chain(() -> Uni.createFrom()
                                                          .item(persisted));
                            });
               });

  }

  @Override
  @SuppressWarnings("unchecked")
  public Uni<IInvolvedPartyIdentificationType<?, ?>> findInvolvedPartyIdentificationType(Mutiny.Session session, String idType, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedPartyIdentificationType by name: {}", idType);
    InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
    var enterprise = system.getEnterprise();
    return (Uni) xr.builder(session)
                     .withName(idType)
                     .inActiveRange()
                     .inDateRange()
                     .withEnterprise(enterprise)
                     .get();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Uni<IInvolvedParty<?, ?>> findByResourceItem(Mutiny.Session session, IResourceItem<?, ?> idType, String value, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedParty by ResourceItem: value={}", value);
    return new InvolvedPartyXResourceItem()
               .builder(session)
               .canRead(system, identityToken)
               .inActiveRange()
               .inDateRange()
               .findLink(null, (ResourceItem) idType, value)
               .setReturnFirst(true)
               .get()
               .onItem()
               .transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID());

  }

  @Override
  public Uni<IInvolvedParty<?, ?>> create(Mutiny.Session session, ISystems<?, ?> system, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken)
  {
    return create(session, system, null, idTypes, isOrganic, identityToken);
  }

  @Override
  public Uni<IInvolvedParty<?, ?>> create(Mutiny.Session session, ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken)
  {
    log.trace("Creating InvolvedParty: key={}, idTypes={}, isOrganic={}", key, idTypes, isOrganic);
    final InvolvedParty ip = new InvolvedParty();
    var enterprise = system.getEnterprise();
    ip.setEnterpriseID(enterprise);

    final UUID finalKey = (key == null) ? UUID.randomUUID() : key;
    ip.setId(finalKey);
    ip.setSystemID(system);
    ip.setOriginalSourceSystemID(system.getId());

    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
    return acService.getActiveFlag(session, enterprise)
               .chain(activeFlag -> {
                 ip.setActiveFlagID(activeFlag);
                 return session.persist(ip)
                            .chain(() -> session.flush())
                            .replaceWith(ip);
               })
               .chain(persisted -> {
                 // Handle security setup sequentially
                 return persisted.createDefaultSecurity(session, system, identityToken)
                            .chain(() -> findInvolvedPartyIdentificationType(session, idTypes.getKey(), system, identityToken)
                                             .chain(involvedPartyIdentificationType -> {
                                               // Add identification type to involved party
                                               try
                                               {
                                                 return persisted.addOrUpdateInvolvedPartyIdentificationType(
                                                         session, NoClassification.toString(),
                                                         involvedPartyIdentificationType,
                                                         idTypes.getValue(),
                                                         idTypes.getValue(),
                                                         system,
                                                         identityToken
                                                     )
                                                            .replaceWith(persisted);
                                               }
                                               catch (Exception e)
                                               {
                                                 return Uni.createFrom()
                                                            .failure(e);
                                               }
                                             })
                                             .chain(updatedPersisted -> {
                                               // Setup organic status sequentially
                                               return setupInvolvedPartyOrganicStatus(session, isOrganic, updatedPersisted, system, identityToken)
                                                          .chain(() -> Uni.createFrom()
                                                                           .item((IInvolvedParty<?, ?>) updatedPersisted));
                                             })
                            );
               });
  }

  private Uni<Void> setupInvolvedPartyOrganicStatus(Mutiny.Session session, boolean isOrganic, IInvolvedParty<?, ?> ip, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Setting up InvolvedParty organic status: isOrganic={}, id={}", isOrganic, ip.getId());
    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
    var enterprise = system.getEnterprise();
    if (isOrganic)
    {
      InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
      ipo.setInvolvedParty((InvolvedParty) ip);
      ipo.setId(ip.getId());
      ipo.setEnterpriseID(enterprise);
      ipo.setSystemID(system);
      ipo.setOriginalSourceSystemID(system.getId());

      return acService.getActiveFlag(session, enterprise)
                 .chain(activeFlag -> {
                   ipo.setActiveFlagID(activeFlag);
                   return session.persist(ipo)
                              .chain(() -> session.flush())
                              .replaceWith(ipo);
                 })
                 .chain(persisted -> {
                   // Handle security setup sequentially
                   return persisted.createDefaultSecurity(session, system, identityToken)
                              .onItem()
                              .invoke(result ->
                                          log.debug("Security setup completed successfully for organic involved party {}", persisted.getId())
                              )
                              .onFailure()
                              .recoverWithItem(error -> {
                                log.warn("Error in createDefaultSecurity for organic", error);
                                return null; // Continue the chain even if security creation fails
                              })
                              .chain(() -> Uni.createFrom()
                                               .voidItem());
                 });
    }
    else
    {
      InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
      ipo.setInvolvedParty((InvolvedParty) ip);
      ipo.setId(ip.getId());
      ipo.setEnterpriseID(enterprise);
      ipo.setSystemID(system);
      ipo.setOriginalSourceSystemID(system.getId());

      return acService.getActiveFlag(session, enterprise)
                 .chain(activeFlag -> {
                   ipo.setActiveFlagID(activeFlag);
                   return session.persist(ipo)
                              .replaceWith(ipo);
                 })
                 .chain(persisted -> {
                   // Handle security setup sequentially
                   return persisted.createDefaultSecurity(session, system, identityToken)
                              .onItem()
                              .invoke(result ->
                                          log.debug("Security setup completed successfully for non-organic involved party {}", persisted.getId())
                              )
                              .onFailure()
                              .recoverWithItem(error -> {
                                log.warn("Error in createDefaultSecurity for non-organic", error);
                                return null; // Continue the chain even if security creation fails
                              })
                              .chain(() -> Uni.createFrom()
                                               .voidItem());
                 });
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Uni<IInvolvedPartyType<?, ?>> findType(Mutiny.Session session, String nameType, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedPartyType by name: {}", nameType);
    InvolvedPartyType xr = new InvolvedPartyType();
    var enterprise = system.getEnterprise();
    return (Uni) xr.builder(session)
               .withName(nameType)
               .inActiveRange()
               .withEnterprise(enterprise)
               .inDateRange()
               .get()
        ;

  }

  @Override
  @SuppressWarnings("unchecked")
  public Uni<IInvolvedPartyNameType<?, ?>> findInvolvedPartyNameType(Mutiny.Session session, String nameType, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedPartyNameType by name: {}", nameType);
    var enterprise = system.getEnterprise();
    InvolvedPartyNameType xr = new InvolvedPartyNameType();
    return (Uni) xr.builder(session)
                     .withName(nameType)
                     .inActiveRange()
                     .inDateRange()
                     .withEnterprise(enterprise)
                     .get()
        ;

  }

  @Override
  public Uni<IInvolvedParty<?, ?>> findByToken(Mutiny.Session session, ISecurityToken<?, ?> token, UUID... identityToken)
  {
    log.debug("Finding InvolvedParty by token: {}", token.getSecurityToken());

    return findInvolvedPartyIdentificationType(session, IdentificationTypeUUID.toString(), ((SecurityToken) token).getSystemID(), identityToken)
               .chain(id -> {
                 InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
                 return idType.builder(session)
                            .findLink(null, (InvolvedPartyIdentificationType) id, token.getSecurityToken())
                            .inActiveRange()
                            .inDateRange()
                            //.withEnterprise(enterprise)
                            .canRead(((SecurityToken) token).getSystemID(), identityToken)
                            .get()
                            .onItem()
                            .transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID());
               });
  }

  @Override
  public Uni<IInvolvedParty<?, ?>> find(Mutiny.Session session, UUID uuid)
  {
    log.debug("🔍 Finding InvolvedParty by UUID: {} with session: {}", uuid, session.hashCode());
    return new InvolvedParty().builder(session)
               .find(uuid)
               .get()
               .onItem()
               .invoke(result -> {
                 if (result != null)
                 {
                   log.debug("✅ Found InvolvedParty with UUID: {}", uuid);
                 }
                 else
                 {
                   log.debug("⚠️ InvolvedParty with UUID: {} not found", uuid);
                 }
               })
               .onItem()
               .ifNull()
               .failWith(() -> new InvolvedPartyException("The InvolvedParty does not exist - " + uuid))
               .map(involvedParty -> (IInvolvedParty<?, ?>) involvedParty);
  }

  @Override
  public Uni<IInvolvedPartyType<?, ?>> findType(Mutiny.Session session, UUID uuid)
  {
    log.debug("🔍 Finding InvolvedPartyType by UUID: {} with session: {}", uuid, session.hashCode());
    return new InvolvedPartyType().builder(session)
               .find(uuid)
               .get()
               .onItem()
               .invoke(result -> {
                 if (result != null)
                 {
                   log.debug("✅ Found InvolvedPartyType with UUID: {}", uuid);
                 }
                 else
                 {
                   log.debug("⚠️ InvolvedPartyType with UUID: {} not found", uuid);
                 }
               })
               .onItem()
               .ifNull()
               .failWith(() -> new InvolvedPartyException("The InvolvedPartyType does not exist - " + uuid))
               .map(involvedPartyType -> (IInvolvedPartyType<?, ?>) involvedPartyType);
  }

  @Override
  public Uni<IInvolvedPartyNameType<?, ?>> findNameType(Mutiny.Session session, UUID uuid)
  {
    log.debug("🔍 Finding InvolvedPartyNameType by UUID: {} with session: {}", uuid, session.hashCode());
    return new InvolvedPartyNameType().builder(session)
               .find(uuid)
               .get()
               .onItem()
               .invoke(result -> {
                 if (result != null)
                 {
                   log.debug("✅ Found InvolvedPartyNameType with UUID: {}", uuid);
                 }
                 else
                 {
                   log.debug("⚠️ InvolvedPartyNameType with UUID: {} not found", uuid);
                 }
               })
               .onItem()
               .ifNull()
               .failWith(() -> new InvolvedPartyException("The InvolvedPartyNameType does not exist - " + uuid))
               .map(nameType -> (IInvolvedPartyNameType<?, ?>) nameType);
  }

  @Override
  public Uni<IInvolvedPartyIdentificationType<?, ?>> findIdentificationType(Mutiny.Session session, UUID uuid)
  {
    log.debug("Finding InvolvedPartyIdentificationType by UUID: {}", uuid);
    return (Uni) new InvolvedPartyIdentificationType().builder(session)
                     .find(uuid)
                     .get();

  }

  @Override
  public Uni<IInvolvedParty<?, ?>> findByUUID(Mutiny.Session session, UUID token, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedParty by UUID token: {}", token);
    var enterprise = system.getEnterprise();
    return findInvolvedPartyIdentificationType(session, IdentificationTypeUUID.toString(), system, identityToken)
               .chain(id -> {
                 InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
                 return idType.builder(session)
                            .findLink(null, (InvolvedPartyIdentificationType) id, token.toString())
                            .inActiveRange()
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .canRead(system, identityToken)
                            .get();
               })
               .chain(idxid -> {
                 return session.fetch(idxid.getInvolvedPartyID());
               });

  }

  @Override
  public Uni<List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>> findAllByIdentificationType(Mutiny.Session session, String identificationType, String value)
  {
    log.debug("Finding all InvolvedParties by identification type: {}, value: {}", identificationType, value);
    InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder(session);
    builder.inDateRange()
        .where(InvolvedPartyIdentificationType_.name, Equals, identificationType);

    InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder(session);
    if (value != null)
    {
      ipQb.withValue(value);
    }

    ipQb.inDateRange()
        .orderBy(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, DESC)
        .join(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, builder, JoinType.INNER)
    ;

    return ipQb.getAll()
               .onFailure()
               .invoke(error -> log.error("Error finding involved parties by identification type: {}", error.getMessage(), error))
               .map(list -> (List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>) (List<?>) list);

  }

  @Override
  public Uni<List<IInvolvedParty<?, ?>>> findByRulesClassification(Mutiny.Session session, String classification, String value, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedParties by rules classification: {}, value: {}", classification, value);
    return classificationService.find(session, classification, system, identityToken)
               .chain(classification1 -> {
                 return new InvolvedPartyXRules().builder(session)
                            .withClassification(classification1)
                            .withValue(value)
                            .inActiveRange()
                            .inDateRange()
                            .getAll()
                            .onFailure()
                            .invoke(error -> log.error("Error finding involved parties by rules classification: {}", error.getMessage(), error))
                            .map(list -> {
                              List<IInvolvedParty<?, ?>> result = new ArrayList<>();
                              for (InvolvedPartyXRules item : list)
                              {
                                result.add(item.getInvolvedPartyID());
                              }
                              return result;
                            });
               });
  }

  @Override
  public Uni<IInvolvedParty<?, ?>> findByClassification(Mutiny.Session session, String classification, String value, ISystems<?, ?> system, UUID... identityToken)
  {
    log.debug("Finding InvolvedParty by classification: {}, value: {}", classification, value);
    return classificationService.find(session, classification, system, identityToken)
               .chain(classification1 -> {
                 return new InvolvedPartyXClassification().builder(session)
                            .withClassification(classification1)
                            .withValue(value)
                            .inActiveRange()
                            .inDateRange()
                            .get()
                            .onFailure()
                            .invoke(error -> log.error("Error finding involved party by classification: {}", error.getMessage(), error))
                            .onItem()
                            .ifNotNull()
                            .transform(item -> (IInvolvedParty<?, ?>) item.getPrimary())
                            .onItem()
                            .ifNull()
                            .continueWith(() -> null);
               });

  }
}

