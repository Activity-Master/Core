package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsXClassification;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.ActivateFlagSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.EnterpriseSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications.SystemIdentity;

@SuppressWarnings("unchecked")
@Log4j2
public class SystemsService
    implements ISystemsService<SystemsService>
{
  @Inject
  private IClassificationService<?> classificationService;

  @Inject
  private ISecurityTokenService<?> securityTokenService;

  public ISystems<?, ?> get()
  {
    return new Systems();
  }

  @Override
  //@CacheResult(cacheName = "GetActivityMaster")
  public Uni<ISystems<?, ?>> getActivityMaster(Mutiny.Session session, ISystems<?, ?> requestingSystem, UUID... identityToken)
  {
    return findSystem(session, requestingSystem, ActivityMasterSystemName, identityToken);
  }

  @Override
  //@CacheResult(cacheName = "GetActivityMasterEnterprise")
  public Uni<ISystems<?, ?>> getActivityMaster(Mutiny.Session session, IEnterprise<?, ?> requestingSystem, UUID... identityToken)
  {
    return findSystem(session, requestingSystem, ActivityMasterSystemName, identityToken);
  }

  //@Transactional()
  @Override
  public Uni<Boolean> doesSystemExist(Mutiny.Session session, IEnterprise<?, ?> enterprise, String systemName, UUID... identityToken)
  {
    return new Systems().builder(session)
               .withName(systemName)
               .withEnterprise(enterprise)
               .inActiveRange()
               .inDateRange()
               .getCount()
               .onFailure()
               .invoke(error -> log.error("Error checking if system exists: {}", error.getMessage(), error))
               .map(count -> count > 0);
  }

  //@Transactional()
  //@CacheResult(cacheName = "FindSystemEnterpriseLevel")
  @Override
  public Uni<ISystems<?, ?>> findSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise, String systemName, UUID... identityToken)
  {
    Systems search = new Systems();
    return (Uni) search.builder(session)
                     .withName(systemName)
                     .withEnterprise(enterprise)
                     .inActiveRange()
                     .inDateRange()
                     //.canRead(enterprise, identityToken)
                     .get();
                     //.onFailure()
                     //.invoke(error -> log.error("Error finding system by name {}: {}", systemName, error.getMessage(), error));
  }

  //@Transactional()
  //@CacheResult(cacheName = "FindSystemByIdentityClassification")
  @Override
  public Uni<ISystems<?, ?>> findSystem(Mutiny.Session session, ISystems<?, ?> requestingSystem, String parentSystem, UUID... identityToken)
  {
    SystemsXClassification systemClassifications = new SystemsXClassification();
    var enterprise = requestingSystem.getEnterprise();
    // Get identity classification using reactive pattern
    return classificationService.getIdentityType(session, requestingSystem, identityToken)
               .chain(identifyClassification -> {
                 // Use the classification to build the query
                 return systemClassifications.builder(session)
                            .findLink(null, (Classification) identifyClassification, parentSystem)
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .canRead(requestingSystem, identityToken)
                            .get()
                            .onFailure()
                            .invoke(error -> log.error("Error finding system by identity classification: {}", error.getMessage(), error))
                            .map(system -> system.getSystemID());
               });
  }

  @Override
  //@Transactional()
  public Uni<String> registerNewSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> newSystem)
  {
    // Get the activity master system and token
    Uni<ISystems<?, ?>> activityMasterSystemUni = getISystemReactive(enterprise, ActivityMasterSystemName);
    Uni<UUID> activityMasterSystemUUIDUni = getISystemTokenReactive(enterprise, ActivityMasterSystemName);

    // Chain the operations
    return Uni.combine()
               .all()
               .unis(activityMasterSystemUni, activityMasterSystemUUIDUni)
               .asTuple()
               .chain(tuple -> {
                 ISystems<?, ?> activityMasterSystem = tuple.getItem1();
                 UUID activityMasterSystemUUID = tuple.getItem2();

                 // Use the reactive classification service but handle other operations with emitter
                 return classificationService.find(
                         session, UserGroupSecurityTokenClassifications.System,
                         activityMasterSystem,
                         activityMasterSystemUUID
                     )
                            .chain(classification -> {
                              // Now that we have the classification, chain the reactive operations
                              return securityTokenService.create(
                                      session, UserGroupSecurityTokenClassifications.System.toString(),
                                      newSystem.getName(),
                                      newSystem.getDescription(),
                                      activityMasterSystem
                                  )
                                         .chain(newSystemsSecurityToken -> {
                                           // Create second security token (reactive)
                                           return securityTokenService.create(
                                                   session, UserGroupSecurityTokenClassifications.System.toString(),
                                                   UserGroupSecurityTokenClassifications.System.toString(),
                                                   UserGroupSecurityTokenClassifications.System.classificationDescription(),
                                                   activityMasterSystem
                                               )
                                                      .chain(systemsToken -> {
                                                        // Link tokens (reactive)
                                                        return securityTokenService.link(
                                                                session, systemsToken,
                                                                newSystemsSecurityToken,
                                                                classification
                                                            )
                                                                   .chain(v -> {
                                                                     // Add classification to new system
                                                                     newSystem.addOrReuseClassification(
                                                                         session, SystemsClassifications.SystemIdentity,
                                                                         ((SecurityToken) newSystemsSecurityToken).getSecurityToken(),
                                                                         newSystem,
                                                                         activityMasterSystemUUID
                                                                     );

                                                                     // Get security identity token
                                                                     return getSecurityIdentityToken(session, newSystem, activityMasterSystemUUID)
                                                                                .chain(newSystemUUID -> {
                                                                                  // Create default security in parallel (fire and forget)
                                                                                  ((SecurityToken) newSystemsSecurityToken).createDefaultSecurity(
                                                                                          activityMasterSystem,
                                                                                          activityMasterSystemUUID
                                                                                      )
                                                                                      .subscribe()
                                                                                      .with(
                                                                                          result -> {
                                                                                            // Security setup completed successfully
                                                                                          },
                                                                                          error -> {
                                                                                            // Log error but don't fail the main operation
                                                                                            log.warn("Error in createDefaultSecurity for newSystemsSecurityToken", error);
                                                                                          }
                                                                                      )
                                                                                  ;

                                                                                  ((SecurityToken) systemsToken).createDefaultSecurity(
                                                                                          activityMasterSystem,
                                                                                          activityMasterSystemUUID
                                                                                      )
                                                                                      .subscribe()
                                                                                      .with(
                                                                                          result -> {
                                                                                            // Security setup completed successfully
                                                                                          },
                                                                                          error -> {
                                                                                            // Log error but don't fail the main operation
                                                                                            log.warn("Error in createDefaultSecurity for systemsToken", error);
                                                                                          }
                                                                                      )
                                                                                  ;

                                                                                  // Create involved party
                                                                                  SystemsSystem systemsSystem = IGuiceContext.get(SystemsSystem.class);
                                                                                  return systemsSystem.createInvolvedPartyForNewSystem(session, newSystem)
                                                                                             .replaceWith(Uni.createFrom()
                                                                                                              .item(newSystemUUID.toString()));
                                                                                });
                                                                   });
                                                      });
                                         });
                            });
               });
  }

  // Helper methods for reactive operations
  private Uni<ISystems<?, ?>> getISystemReactive(IEnterprise<?, ?> enterprise, String systemName)
  {
    return IActivityMasterService.getISystem(systemName, enterprise)
               .onItem()
               .ifNull()
               .failWith(() -> new NoSuchElementException("System not found: " + systemName));
  }

  private Uni<UUID> getISystemTokenReactive(IEnterprise<?, ?> enterprise, String systemName)
  {
    return IActivityMasterService.getISystemToken(systemName, enterprise)
               .onItem()
               .ifNull()
               .failWith(() -> new NoSuchElementException("System token not found: " + systemName));
  }

  @Override
  public Uni<ISystems<?, ?>> create(Mutiny.Session session, IEnterprise<?, ?> enterprise, String systemName, String systemDesc, UUID... identityToken)
  {
    return create(session, enterprise, systemName, systemDesc, systemName, identityToken);
  }

  @Override
  //@Transactional()
  public Uni<ISystems<?, ?>> create(Mutiny.Session session, IEnterprise<?, ?> enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken)
  {
    Systems newSystem = new Systems();

    // Check if system exists and recover with creating a new one if not found
    return findSystem(session, enterprise, systemName, identityToken)
               .onFailure(NoResultException.class)
               .recoverWithUni(() -> {
                 log.info("System {} not found, creating new system", systemName);
                 // Set up the new system
                 newSystem.setName(systemName);
                 newSystem.setDescription(systemDesc);
                 newSystem.setSystemHistoryName(historyName);
                 newSystem.setEnterpriseID(enterprise);

                 // Get active flag service
                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

                 // Get active flag (reactive)
                 return (Uni) acService.getActiveFlag(session, enterprise)
                                  .chain(activeFlag -> {
                                    // Set active flag
                                    newSystem.setActiveFlagID(activeFlag);

                                    // Persist the new system (reactive)
                                    return session.persist(newSystem)
                                               .chain(a -> session.flush())
                                               .replaceWith(Uni.createFrom()
                                                                .item(newSystem))
                                               .map(persistedSystem -> {
                                                 log.info("Successfully created new system: {}", systemName);

                                                 // Skip ActivityMaster setup for EnterpriseSystem and ActiveFlagSystem
                                                 // since ActivityMaster system is created after these systems
                                                 if (systemName.equals(EnterpriseSystemName) || systemName.equals(ActivateFlagSystemName))
                                                 {
                                                   log.debug("Skipping ActivityMaster setup for system: {} as ActivityMaster doesn't exist yet", systemName);
                                                 }
                                                 else
                                                 {
                                                   // Start getActivityMaster in parallel without waiting for it
                                                   //todo
                                       /*return getActivityMaster(session, enterprise)
                                           .subscribe().with(
                                               activityMaster -> {
                                                   try {
                                                       // Call createDefaultSecurity
                                                       persistedSystem.createDefaultSecurity(activityMaster, identityToken)
                                                           .subscribe().with(
                                                               result -> {
                                                                   // Security setup completed successfully
                                                                   log.debug("Security setup completed for system: {}", systemName);
                                                               },
                                                               error -> {
                                                                   // Log error but don't fail the main operation
                                                                   log.warn("Error in createDefaultSecurity for system {}: {}", systemName, error.getMessage(), error);
                                                               }
                                                           );
                                                   } catch (Exception e) {
                                                       // Log error but don't fail the main operation
                                                       log.warn("Exception in createDefaultSecurity for system {}: {}", systemName, e.getMessage(), e);
                                                   }
                                               },
                                               error -> {
                                                   // Log error but don't fail the main operation
                                                   log.warn("Error in getActivityMaster for system {}: {}", systemName, error.getMessage(), error);
                                               }
                                           );*/
                                                 }

                                                 // Return the persisted system immediately without waiting for getActivityMaster or security setup
                                                 return persistedSystem;
                                               });
                                  });
               });
  }

  //@Transactional()
  //@CacheResult(cacheName = "SystemGetSecurityToken")
  public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.Session session, String uuidIdentity, ISystems<?, ?> system, UUID... identityToken)
  {
    var enterprise = system.getEnterprise();
    return new SecurityToken().builder(session)
               .findBySecurityToken(uuidIdentity.toString(), enterprise)
               .inActiveRange()
               .inDateRange()
               .withEnterprise(enterprise)
               //      .canRead(system, identityToken)
               .get()
               .onFailure()
               .invoke(error -> log.error("Error getting security token: {}", error.getMessage(), error))
               .map(securityToken -> securityToken);
  }

  //@CacheResult(cacheName = "SystemSetSecurityTokenUUID")
  @Override
  public Uni<UUID> getSecurityIdentityToken(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    return system.findClassification(session, SystemIdentity, system, identityToken)
               .onFailure()
               .recoverWithItem(() -> null) // Handle failure case
               .map(systemToken -> systemToken != null ? systemToken.getValueAsUUID() : null);
  }
}
