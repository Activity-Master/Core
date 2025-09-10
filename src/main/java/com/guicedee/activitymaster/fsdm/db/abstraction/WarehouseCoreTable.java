package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.SecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.google.inject.Inject;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.guicedee.client.IGuiceContext.*;


/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@SuppressWarnings("unchecked")
@MappedSuperclass()
@Log4j2
public abstract class WarehouseCoreTable<J extends WarehouseCoreTable<J, Q, I, S>,
                                            Q extends QueryBuilderCore<Q, J, I>,
                                            I extends java.util.UUID,
                                            S extends WarehouseSecurityTable<S, ?, ?>
                                            >
    extends WarehouseBaseTable<J, Q, I>
    implements IWarehouseCoreTable<J, Q, I, S>
{
  @Serial
  private static final long serialVersionUID = 1L;

  @Inject
  @Transient
  private SecurityTokenService securityTokenService;

  public WarehouseCoreTable()
  {

  }

  public abstract void configureSecurityEntity(S securityEntity);

  @Override
  public Uni<Void> createDefaultSecurity(Mutiny.Session session, ISystems<?, ?> system, UUID... identity)
  {
    log.trace("🛡️ Creating default security for system: {} with session: {}", system.getName(), session.hashCode());

    // Use the provided session and execute operations sequentially
    log.trace("📋 Starting sequential security operations with session: {}", session.hashCode());
    if (false)
      // Chain all security operations sequentially
      return createDefaultAdministratorSecurityAccess(session, system, identity)
                 .chain(() -> createDefaultEveryoneSecurityAccess(session, system, identity))
                 .chain(() -> createDefaultEverywhereSecurityAccess(session, system, identity))
                 .chain(() -> createDefaultSystemsSecurityAccess(session, system, identity))
                 .chain(() -> createDefaultApplicationsSecurityAccess(session, system, identity))
                 .chain(() -> createDefaultPluginsSecurityAccess(session, system, identity))
                 .chain(() -> createDefaultGuestReadSecurityAccess(session, system, identity))
                 .onItem()
                 .invoke(() -> log.trace("✅ All security operations completed successfully"))
                 .onFailure()
                 .invoke(error -> log.error("❌ Failed to complete security operations: {}", error.getMessage(), error))
                 .replaceWithVoid();
    else
    {
      return Uni.createFrom()
                 .voidItem();
    }
  }

  public Uni<Void> updateSecurity(Mutiny.Session session, J newCoreTable, Systems system)
  {
    log.trace("🔄 Updating security for table with system: {}", system.getName());

    S stAdmin = get(findPersistentSecurityClass());
    @SuppressWarnings("rawtypes")
    QueryBuilderSecurities<?, ?, ?> securities = (QueryBuilderSecurities) stAdmin.builder(session);

    return securities.findLinkedSecurityTokens(this)
               .inDateRange()
               .getAll()
               .chain(result -> {
                 log.debug("📋 Found {} security tokens to update sequentially", result.size());

                 // Start with a completed Uni to begin the chain
                 Uni<Void> sequentialChain = Uni.createFrom()
                                                 .voidItem();

                 // Process each token sequentially by chaining operations
                 for (Object exist : result)
                 {
                   final S existingToken = (S) exist;
                   existingToken.setId(null);
                   configureDefaultsForNewToken(existingToken, system);

                   // Add this operation to the chain
                   sequentialChain = sequentialChain.chain(() -> {
                     log.debug("🔄 Updating security token sequentially");
                     return session.persist(existingToken)
                                .onItem()
                                .invoke(() -> log.debug("✅ Security token updated successfully"))
                                .onFailure()
                                .invoke(error -> log.error("❌ Failed to update security token: {}", error.getMessage(), error))
                                .replaceWithVoid();
                   });
                 }

                 // Return the complete chain
                 return sequentialChain
                            .onItem()
                            .invoke(() -> log.debug("✅ All security tokens updated successfully"))
                            .onFailure()
                            .invoke(error -> log.error("❌ Error updating security tokens: {}", error.getMessage(), error));
               });
  }

  private Uni<S> createDefaultAdministratorSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity)
  {
    log.debug("🔧 Creating default administrator security access");

    S stAdmin = get(findPersistentSecurityClass());
    return securityTokenService.getAdministratorsFolder(session, system, identity)
               .chain(administrators -> {
                 @SuppressWarnings("rawtypes")
                 QueryBuilderSecurities<?, ?, ?> securities = (QueryBuilderSecurities) stAdmin.builder(session);
                 return securities.findLinkedSecurityToken((SecurityToken) administrators, this)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onFailure()
                            .recoverWithUni(() -> {
                              log.debug("🔧 Creating new administrator security token");
                              S stEntity = get(findPersistentSecurityClass());
                              configureDefaultsForNewToken(stEntity, system);
                              stEntity.setSecurityTokenID((SecurityToken) administrators);
                              stEntity.setCreateAllowed(true);
                              stEntity.setUpdateAllowed(true);
                              stEntity.setDeleteAllowed(true);
                              stEntity.setReadAllowed(true);

                              return (Uni) session.persist(stEntity)
                                               .chain(s -> {
                                                 configureSecurityEntity(stEntity);
                                                 return session.merge(stEntity);
                                               });
                            })
                            .chain(result -> {
                              if (result instanceof Uni)
                              {
                                return (Uni<S>) result;
                              }
                              log.debug("✅ Administrator security token already exists");
                              return Uni.createFrom()
                                         .item((S) result);
                            });
               });
  }

  private Uni<S> createDefaultEveryoneSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity)
  {
    log.debug("🔧 Creating default everyone security access with session");

    S stAdmin = get(findPersistentSecurityClass());
    return (Uni) get(SecurityTokenService.class)
                     .getEveryoneGroup(session, system, identity)
                     .chain(everyoneGroup -> {
                       @SuppressWarnings("rawtypes")
                       QueryBuilderSecurities<?, ?, ?> securities = (QueryBuilderSecurities) stAdmin.builder(session);

                       return securities.findLinkedSecurityToken((SecurityToken) everyoneGroup, this)
                                  //.inActiveRange(enterprise)
                                  .inDateRange()
                                  .setReturnFirst(true)
                                  .get()
                                  .onItemOrFailure()
                                  .call((result, throwable) -> {
                                    if (throwable != null)
                                    {
                                      log.debug("🔧 Creating new everyone security token");
                                      S stEntity = get(findPersistentSecurityClass());
                                      configureDefaultsForNewToken(stEntity, system);
                                      stEntity.setSecurityTokenID((SecurityToken) everyoneGroup);
                                      stEntity.setCreateAllowed(false);
                                      stEntity.setUpdateAllowed(false);
                                      stEntity.setDeleteAllowed(false);
                                      stEntity.setReadAllowed(false);

                                      return session.persist(stEntity)
                                                 .chain(s -> {
                                                   configureSecurityEntity(stEntity);
                                                   log.debug("✅ Everyone security token created successfully");
                                                   return Uni.createFrom()
                                                              .item(stEntity);
                                                 });
                                    }
                                    else
                                    {
                                      log.debug("✅ Everyone security token already exists");
                                      return Uni.createFrom()
                                                 .item((S) result);
                                    }
                                  });
                     });

        /*
        Optional<S> exists = (Optional<S>) securities.findLinkedSecurityToken(administrators, this)
                                                   //.inActiveRange(enterprise)
                                                   .inDateRange()
                                                   .setReturnFirst(true)
                                                   .get();
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);
            stAdmin.setCreateAllowed(false);
            stAdmin.setUpdateAllowed(false);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(false);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }
        return stAdmin;*/
  }

  private Uni<S> createDefaultEverywhereSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity)
  {
    log.debug("🔧 Creating default everywhere security access with session");

    S stAdmin = get(findPersistentSecurityClass());
    return get(SecurityTokenService.class)
               .getEverywhereGroup(session, system, identity)
               .chain(everywhereGroup -> {
                 @SuppressWarnings("rawtypes")
                 QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder(session);

                 return securities.findLinkedSecurityToken((SecurityToken) everywhereGroup, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                              if (throwable != null)
                              {
                                log.debug("🔧 Creating new everywhere security token");
                                S stEntity = get(findPersistentSecurityClass());
                                configureDefaultsForNewToken(stEntity, system);
                                stEntity.setSecurityTokenID((SecurityToken) everywhereGroup);
                                stEntity.setCreateAllowed(false);
                                stEntity.setUpdateAllowed(false);
                                stEntity.setDeleteAllowed(false);
                                stEntity.setReadAllowed(true);

                                return session.persist(stEntity)
                                           .chain(s -> {
                                             configureSecurityEntity(stEntity);
                                             log.debug("✅ Everywhere security token created successfully");
                                             return Uni.createFrom()
                                                        .item(stEntity);
                                           });
                              }
                              else
                              {
                                log.debug("✅ Everywhere security token already exists");
                                return Uni.createFrom()
                                           .item((S) result);
                              }
                            });
               });
        /*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(false);
            stAdmin.setUpdateAllowed(false);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }*/

    //return stAdmin;
  }

  private Uni<S> createDefaultSystemsSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity)
  {
    log.debug("🔧 Creating default systems security access with session");

    S stAdmin = get(findPersistentSecurityClass());
    return get(SecurityTokenService.class)
               .getSystemsFolder(session, system, identity)
               .chain(systemsFolder -> {
                 @SuppressWarnings("rawtypes")
                 QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder(session);

                 return securities.findLinkedSecurityToken((SecurityToken) systemsFolder, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                              if (throwable != null)
                              {
                                log.debug("🔧 Creating new systems security token");
                                S stEntity = get(findPersistentSecurityClass());
                                configureDefaultsForNewToken(stEntity, system);
                                stEntity.setSecurityTokenID((SecurityToken) systemsFolder);
                                stEntity.setCreateAllowed(true);
                                stEntity.setUpdateAllowed(true);
                                stEntity.setDeleteAllowed(false);
                                stEntity.setReadAllowed(true);

                                return session.persist(stEntity)
                                           .chain(s -> {
                                             configureSecurityEntity(stEntity);
                                             log.debug("✅ Systems security token created successfully");
                                             return Uni.createFrom()
                                                        .item(stEntity);
                                           });
                              }
                              else
                              {
                                log.debug("✅ Systems security token already exists");
                                return Uni.createFrom()
                                           .item((S) result);
                              }
                            });
               });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {

            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(true);
            stAdmin.setUpdateAllowed(true);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }
        return stAdmin;*/
  }

  private Uni<S> createDefaultApplicationsSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity)
  {
    log.debug("🔧 Creating default applications security access with session");

    S stAdmin = get(findPersistentSecurityClass());
    return get(SecurityTokenService.class)
               .getApplicationsFolder(session, system, identity)
               .chain(applicationsFolder -> {
                 @SuppressWarnings("rawtypes")
                 QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder(session);

                 return securities.findLinkedSecurityToken((SecurityToken) applicationsFolder, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                              if (throwable != null)
                              {
                                log.debug("🔧 Creating new applications security token");
                                S stEntity = get(findPersistentSecurityClass());
                                configureDefaultsForNewToken(stEntity, system);
                                stEntity.setSecurityTokenID((SecurityToken) applicationsFolder);
                                stEntity.setCreateAllowed(true);
                                stEntity.setUpdateAllowed(true);
                                stEntity.setDeleteAllowed(false);
                                stEntity.setReadAllowed(true);

                                return session.persist(stEntity)
                                           .chain(s -> {
                                             configureSecurityEntity(stEntity);
                                             log.debug("✅ Applications security token created successfully");
                                             return Uni.createFrom()
                                                        .item(stEntity);
                                           });
                              }
                              else
                              {
                                log.debug("✅ Applications security token already exists");
                                return Uni.createFrom()
                                           .item((S) result);
                              }
                            });
               });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {

            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(true);
            stAdmin.setUpdateAllowed(true);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }
        return stAdmin;*/
  }

  private Uni<S> createDefaultPluginsSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity)
  {
    log.debug("🔧 Creating default plugins security access with session");

    S stAdmin = get(findPersistentSecurityClass());
    return get(SecurityTokenService.class)
               .getPluginsFolder(session, system, identity)
               .chain(pluginsFolder -> {
                 @SuppressWarnings("rawtypes")
                 QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder(session);

                 return securities.findLinkedSecurityToken((SecurityToken) pluginsFolder, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                              if (throwable != null)
                              {
                                log.debug("🔧 Creating new plugins security token");
                                S stEntity = get(findPersistentSecurityClass());
                                configureDefaultsForNewToken(stEntity, system);
                                stEntity.setSecurityTokenID((SecurityToken) pluginsFolder);
                                stEntity.setCreateAllowed(true);
                                stEntity.setUpdateAllowed(true);
                                stEntity.setDeleteAllowed(false);
                                stEntity.setReadAllowed(true);

                                return session.persist(stEntity)
                                           .chain(s -> {
                                             configureSecurityEntity(stEntity);
                                             log.debug("✅ Plugins security token created successfully");
                                             return Uni.createFrom()
                                                        .item(stEntity);
                                           });
                              }
                              else
                              {
                                log.debug("✅ Plugins security token already exists");
                                return Uni.createFrom()
                                           .item((S) result);
                              }
                            });
               });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(true);
            stAdmin.setUpdateAllowed(true);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }
        return stAdmin;*/
  }

  private Uni<S> createDefaultGuestReadSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity)
  {
    log.debug("🔧 Creating default guest read security access with session");

    S stAdmin = get(findPersistentSecurityClass());
    return get(SecurityTokenService.class)
               .getGuestsFolder(session, system, identity)
               .chain(guestsFolder -> {
                 @SuppressWarnings("rawtypes")
                 QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder(session);

                 return securities.findLinkedSecurityToken((SecurityToken) guestsFolder, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                              if (throwable != null)
                              {
                                log.debug("🔧 Creating new guest read security token");
                                S stEntity = get(findPersistentSecurityClass());
                                configureDefaultsForNewToken(stEntity, system);
                                stEntity.setSecurityTokenID((SecurityToken) guestsFolder);
                                stEntity.setCreateAllowed(false);
                                stEntity.setUpdateAllowed(false);
                                stEntity.setDeleteAllowed(false);
                                stEntity.setReadAllowed(true);

                                return session.persist(stEntity)
                                           .chain(s -> {
                                             configureSecurityEntity(stEntity);
                                             log.debug("✅ Guest read security token created successfully");
                                             return Uni.createFrom()
                                                        .item(stEntity);
                                           });
                              }
                              else
                              {
                                log.debug("✅ Guest read security token already exists");
                                return Uni.createFrom()
                                           .item((S) result);
                              }
                            });
               });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(false);
            stAdmin.setUpdateAllowed(false);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }

        return stAdmin;*/
  }

  @SuppressWarnings("unchecked")
  protected Class<S> findPersistentSecurityClass()
  {
    return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
  }

  protected void configureDefaultsForNewToken(S stAdmin, ISystems<?, ?> system)
  {
    stAdmin.setSystemID((Systems) system);
    stAdmin.setActiveFlagID(((Systems) system).getActiveFlagID());
    stAdmin.setOriginalSourceSystemID((Systems) system);
    stAdmin.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
    stAdmin.setEnterpriseID((Enterprise) system.getEnterprise());
  }

  public Uni<S> createDefaultGuestNoSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity)
  {
    log.debug("🎭 Creating default guest no-security access token for system: {}", system.getName());

    return securityTokenService.getGuestsFolder(session, system, identity)
               .chain(administrators -> {
                 S stAdmin = get(findPersistentSecurityClass());

                 @SuppressWarnings("rawtypes")
                 QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder(session);

                 return securities.findLinkedSecurityToken((SecurityToken) administrators, this)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onFailure()
                            .recoverWithItem(() -> {
                              // Create new token if not found
                              S stEntity = get(findPersistentSecurityClass());
                              configureDefaultsForNewToken(stEntity, system);
                              stEntity.setSecurityTokenID(administrators);
                              stEntity.setCreateAllowed(false);
                              stEntity.setUpdateAllowed(false);
                              stEntity.setDeleteAllowed(false);
                              stEntity.setReadAllowed(false);

                              return session.persist(stEntity)
                                         .chain(persisted -> {
                                           configureSecurityEntity(stEntity);
                                           log.debug("✅ Guest no-security token created successfully");
                                           return Uni.createFrom()
                                                      .item(stEntity);
                                         });
                            })
                            .chain(result -> {
                              if (result instanceof Uni)
                              {
                                return (Uni<S>) result;
                              }
                              return Uni.createFrom()
                                         .item((S) result);
                            });
               });
  }

}
