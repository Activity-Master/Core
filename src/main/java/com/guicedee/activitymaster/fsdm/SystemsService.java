package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
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
import lombok.extern.log4j.Log4j2;

import javax.cache.annotation.CacheKey;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications.SystemIdentity;

@Log4j2
public class SystemsService
        implements ISystemsService<SystemsService>
{
    @Inject
    private IEnterprise<?, ?> enterprise;

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
    public Uni<ISystems<?, ?>> getActivityMaster(@CacheKey ISystems<?, ?> requestingSystem, @CacheKey java.util.UUID... identityToken)
    {
        return findSystem(requestingSystem, ActivityMasterSystemName, identityToken);
    }

    @Override
    //@CacheResult(cacheName = "GetActivityMasterEnterprise")
    public Uni<ISystems<?, ?>> getActivityMaster(@CacheKey IEnterprise<?, ?> requestingSystem, java.util.UUID... identityToken)
    {
        return findSystem(requestingSystem, ActivityMasterSystemName, identityToken);
    }

    //@Transactional()
    @Override
    public Uni<Boolean> doesSystemExist(IEnterprise<?, ?> enterprise, String systemName, java.util.UUID... identityToken)
    {
        return new Systems().builder()
                       .withName(systemName)
                       .withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange()
                       .getCount()
                       .onFailure().invoke(error -> log.error("Error checking if system exists: {}", error.getMessage(), error))
                       .map(count -> count > 0);
    }

    //@Transactional()
    //@CacheResult(cacheName = "FindSystemEnterpriseLevel")
    @Override
    public Uni<ISystems<?, ?>> findSystem(@CacheKey IEnterprise<?, ?> enterprise, @CacheKey String systemName, java.util.UUID... identityToken)
    {
        Systems search = new Systems();
        return search.builder()
                       .withName(systemName)
                       .withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange()
                       //.canRead(enterprise, identityToken)
                       .get()
                       .onFailure().invoke(error -> log.error("Error finding system by enterprise: {}", error.getMessage(), error))
                       .map(system -> system);
    }

    //@Transactional()
    //@CacheResult(cacheName = "FindSystemByIdentityClassification")
    @Override
    public Uni<ISystems<?, ?>> findSystem(@CacheKey ISystems<?, ?> requestingSystem, @CacheKey String parentSystem, java.util.UUID... identityToken)
    {
        SystemsXClassification systemClassifications = new SystemsXClassification();

        // Get identity classification using reactive pattern
        return classificationService.getIdentityType(requestingSystem, identityToken)
                .chain(identifyClassification -> {
                    // Use the classification to build the query
                    return systemClassifications.builder()
                               .findLink(null, (Classification) identifyClassification, parentSystem)
                               .inDateRange()
                               .withEnterprise(enterprise)
                               .canRead(requestingSystem, identityToken)
                               .get()
                               .onFailure().invoke(error -> log.error("Error finding system by identity classification: {}", error.getMessage(), error))
                               .map(system -> system.getSystemID());
                });
    }

    @Override
    //@Transactional()
    public Uni<String> registerNewSystem(IEnterprise<?, ?> enterprise, ISystems<?, ?> newSystem)
    {
        // Get the activity master system and token
        Uni<ISystems<?, ?>> activityMasterSystemUni = getISystemReactive(ActivityMasterSystemName);
        Uni<UUID> activityMasterSystemUUIDUni = getISystemTokenReactive(ActivityMasterSystemName);

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
                                   UserGroupSecurityTokenClassifications.System,
                                   activityMasterSystem,
                                   activityMasterSystemUUID
                           )
                           .chain(classification -> {
                               // Now that we have the classification, use an emitter to handle the rest
                               return Uni.createFrom()
                                          .emitter(emitter -> {
                                              try
                                              {
                                                  // Create first security token
                                                  ISecurityToken<?, ?> newSystemsSecurityToken = securityTokenService.create(
                                                          UserGroupSecurityTokenClassifications.System.toString(),
                                                          newSystem.getName(),
                                                          newSystem.getDescription(),
                                                          activityMasterSystem
                                                  );

                                                  // Create second security token
                                                  ISecurityToken<?, ?> systemsToken = securityTokenService.create(
                                                          UserGroupSecurityTokenClassifications.System.toString(),
                                                          UserGroupSecurityTokenClassifications.System.toString(),
                                                          UserGroupSecurityTokenClassifications.System.classificationDescription(),
                                                          activityMasterSystem
                                                  );

                                                  // Link tokens
                                                  securityTokenService.link(
                                                          systemsToken,
                                                          newSystemsSecurityToken,
                                                          classification
                                                  );

                                                  // Add classification to new system
                                                  newSystem.addOrReuseClassification(
                                                          SystemsClassifications.SystemIdentity,
                                                          ((SecurityToken) newSystemsSecurityToken).getSecurityToken(),
                                                          newSystem,
                                                          activityMasterSystemUUID
                                                  );

                                                  // Get security identity token
                                                  getSecurityIdentityToken(newSystem, activityMasterSystemUUID)
                                                          .subscribe()
                                                          .with(
                                                              newSystemUUID -> {
                                                                  try {
                                                                      // Create default security in parallel (fire and forget)
                                                                      ((SecurityToken) newSystemsSecurityToken).createDefaultSecurity(
                                                                              activityMasterSystem,
                                                                              activityMasterSystemUUID
                                                                      ).subscribe().with(
                                                                          result -> {
                                                                              // Security setup completed successfully
                                                                          },
                                                                          error -> {
                                                                              // Log error but don't fail the main operation
                                                                              log.warn("Error in createDefaultSecurity for newSystemsSecurityToken", error);
                                                                          }
                                                                      );

                                                                      ((SecurityToken) systemsToken).createDefaultSecurity(
                                                                              activityMasterSystem,
                                                                              activityMasterSystemUUID
                                                                      ).subscribe().with(
                                                                          result -> {
                                                                              // Security setup completed successfully
                                                                          },
                                                                          error -> {
                                                                              // Log error but don't fail the main operation
                                                                              log.warn("Error in createDefaultSecurity for systemsToken", error);
                                                                          }
                                                                      );

                                                                      // Create involved party
                                                                      SystemsSystem systemsSystem = IGuiceContext.get(SystemsSystem.class);
                                                                      systemsSystem.createInvolvedPartyForNewSystem(newSystem);

                                                                      // Complete with the new system UUID
                                                                      emitter.complete(newSystemUUID.toString());
                                                                  }
                                                                  catch (Exception e)
                                                                  {
                                                                      emitter.fail(e);
                                                                  }
                                                              },
                                                              emitter::fail
                                                          );
                                              }
                                              catch (Exception e)
                                              {
                                                  emitter.fail(e);
                                              }
                                          });
                           });
                       });
    }

    // Helper methods for reactive operations
    private Uni<ISystems<?, ?>> getISystemReactive(String systemName)
    {
        return IActivityMasterService.getISystem(systemName, enterprise)
                       .onItem().ifNull().failWith(() -> new NoSuchElementException("System not found: " + systemName));
    }

    private Uni<UUID> getISystemTokenReactive(String systemName)
    {
        return IActivityMasterService.getISystemToken(systemName, enterprise)
                       .onItem().ifNull().failWith(() -> new NoSuchElementException("System token not found: " + systemName));
    }

    @Override
    public Uni<ISystems<?, ?>> create(IEnterprise<?, ?> enterprise, String systemName, String systemDesc, java.util.UUID... identityToken)
    {
        return create(enterprise, systemName, systemDesc, systemName, identityToken);
    }

    @Override
    //@Transactional()
    public Uni<ISystems<?, ?>> create(IEnterprise<?, ?> enterprise, String systemName, String systemDesc, String historyName, java.util.UUID... identityToken)
    {
        Systems newSystem = new Systems();

        // Check if system exists
        return newSystem.builder()
                       .withEnterprise(enterprise)
                       .withName(systemName)
                       .get()
                       .onFailure().recoverWithItem(() -> {
                           // If get() fails (no system found), we'll create a new one
                           return null; // This null will be handled in the chain below
                       })
                       .onFailure().invoke(error -> log.error("Error checking if system exists for creation: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // System doesn't exist or get() failed, create it
                               // Set up the new system
                               newSystem.setName(systemName);
                               newSystem.setDescription(systemDesc);
                               newSystem.setSystemHistoryName(historyName);
                               newSystem.setEnterpriseID(enterprise);

                               // Get active flag service
                               IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

                               // Get active flag (reactive)
                               return acService.getActiveFlag(enterprise)
                                   .chain(activeFlag -> {
                                       // Set active flag
                                       newSystem.setActiveFlagID(activeFlag);

                                       // Persist the new system (reactive)
                                       return newSystem.persist()
                                           .map(persistedSystem -> {
                                               // Start getActivityMaster in parallel without waiting for it
                                               getActivityMaster(enterprise)
                                                   .subscribe().with(
                                                       activityMaster -> {
                                                               try {
                                                                   // Call createDefaultSecurity
                                                                   persistedSystem.createDefaultSecurity(activityMaster, identityToken)
                                                                       .subscribe().with(
                                                                           result -> {
                                                                               // Security setup completed successfully
                                                                           },
                                                                           error -> {
                                                                               // Log error but don't fail the main operation
                                                                               log.warn("Error in createDefaultSecurity", error);
                                                                           }
                                                                       );
                                                               } catch (Exception e) {
                                                                   // Log error but don't fail the main operation
                                                                   log.warn("Error in createDefaultSecurity", e);
                                                               }

                                                       },
                                                       error -> {
                                                           // Log error but don't fail the main operation
                                                           log.warn("Error in getActivityMaster", error);
                                                       }
                                                   );

                                               // Return the persisted system immediately without waiting for getActivityMaster or security setup
                                               return persistedSystem;
                                           });
                                   });
                           }
                           else
                           {
                               // System already exists, use it
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    //@Transactional()
    //@CacheResult(cacheName = "SystemGetSecurityToken")
    public Uni<ISecurityToken<?, ?>> getSecurityToken(@CacheKey String uuidIdentity, @CacheKey ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return new SecurityToken().builder()
                       .findBySecurityToken(uuidIdentity.toString(), enterprise)
                       .inActiveRange()
                       .inDateRange()
                       .withEnterprise(enterprise)
                       //      .canRead(system, identityToken)
                       .get()
                       .onFailure().invoke(error -> log.error("Error getting security token: {}", error.getMessage(), error))
                       .map(securityToken -> securityToken);
    }

    //@CacheResult(cacheName = "SystemSetSecurityTokenUUID")
    @Override
    public Uni<UUID> getSecurityIdentityToken(@CacheKey ISystems<?, ?> system, java.util.UUID... identityToken)
    {
        return system.findClassification(SystemIdentity, system, identityToken)
                       .onFailure().recoverWithItem(() -> null) // Handle failure case
                       .map(systemToken -> systemToken != null ? systemToken.getValueAsUUID() : null);
    }
}
