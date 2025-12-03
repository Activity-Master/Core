package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * 
 * [✓] One action per Mutiny.Session at a time
 *     - All operations on a session are sequential
 *     - No parallel operations on the same session
 * 
 * [✓] Pass Mutiny.Session through the chain
 *     - All methods accept session as parameter
 *     - Session is passed to all dependent operations
 * 
 * [✓] No await() usage
 *     - Using reactive chains instead of blocking operations
 * 
 * [✓] Synchronous execution of reactive chains
 *     - All reactive chains execute synchronously
 *     - No fire-and-forget operations with subscribe().with()
 * 
 * [✓] No parallel operations on a session
 *     - Not using Uni.combine().all().unis() with operations that share the same session
 * 
 * [✓] No session/transaction creation in libraries
 *     - Sessions are passed in from the caller
 *     - No sessionFactory.withTransaction() in methods
 * 
 * See ReactivityMigrationGuide.md for more details on these rules.
 */

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.ActivateFlagSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.getISystem;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.getISystemToken;
import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.EnterpriseSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications.SystemIdentity;

@SuppressWarnings("unchecked")
@Log4j2
@Singleton
public class SystemsService
    implements ISystemsService<SystemsService>
{
  // Local cache: key = enterpriseId + '|' + systemName, value = Systems UUID
  private final Map<String, UUID> systemKeyToId = new ConcurrentHashMap<>();
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
    return findSystem(session, requestingSystem.getEnterprise(), ActivityMasterSystemName, identityToken);
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
    UUID enterpriseId = null;
    if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
      enterpriseId = ent.getId();
    }
    String key = enterpriseId + "|" + systemName;
    UUID cachedId = systemKeyToId.get(key);
    if (cachedId != null) {
      log.trace("🔁 Systems cache hit for key '{}': {} — loading by UUID", key, cachedId);
      return (Uni) getSystemById(session, cachedId)
        .flatMap(found -> {
          if (found != null) {
            return Uni.createFrom().item(found);
          }
          // Stale mapping: remove and fallback to name+enterprise query
          systemKeyToId.remove(key);
          return (Uni) new Systems().builder(session)
                          .withName(systemName)
                          .withEnterprise(enterprise)
                          .inActiveRange()
                          .inDateRange()
                          //.canRead(enterprise, identityToken)
                          .get()
                          .invoke(sys -> {
                            if (sys != null && sys.getId() != null) {
                              systemKeyToId.put(key, (UUID) sys.getId());
                            }
                          });
        });
    }

    // Cold path: query by name+enterprise and remember UUID
    return (Uni) new Systems().builder(session)
                     .withName(systemName)
                     .withEnterprise(enterprise)
                     .inActiveRange()
                     .inDateRange()
                     //.canRead(enterprise, identityToken)
                     .get()
                     .invoke(sys -> {
                       if (sys != null && sys.getId() != null) {
                         systemKeyToId.put(key, (UUID) sys.getId());
                       }
                     });
  }

  // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
  public Uni<ISystems<?, ?>> getSystemById(Mutiny.Session session, UUID id) {
    //noinspection unchecked
    return (Uni) session.find(Systems.class, id);
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
    log.info("🚀 Registering new system: '{}' for enterprise: '{}'", newSystem.getName(), enterprise.getName());
    log.debug("📋 Starting registration with session: {}", session.hashCode());
    
    // Get the activity master system first, then get the token sequentially
    return getISystem(session, ActivityMasterSystemName, enterprise)
               .onItem()
               .invoke(activityMasterSystem -> log.debug("✅ Retrieved ActivityMaster system with session: {}", session.hashCode()))
               .onFailure()
               .invoke(error -> log.error("❌ Failed to retrieve ActivityMaster system with session {}: {}", 
                                        session.hashCode(), error.getMessage(), error))
               // Chain to get the token after getting the system
               .chain(activityMasterSystem -> 
                   getISystemToken(session, ActivityMasterSystemName, enterprise)
                       .onItem()
                       .invoke(activityMasterSystemUUID -> log.debug("✅ Retrieved ActivityMaster system UUID with session: {}", session.hashCode()))
                       .onFailure()
                       .invoke(error -> log.error("❌ Failed to retrieve ActivityMaster system UUID with session {}: {}", 
                                                session.hashCode(), error.getMessage(), error))
                       .map(activityMasterSystemUUID -> new Pair<>(activityMasterSystem, activityMasterSystemUUID))
               )
               .chain(pair -> {
                 ISystems<?, ?> activityMasterSystem = pair.getKey();
                 UUID activityMasterSystemUUID = pair.getValue();
                 log.debug("📋 Finding classification with session: {}", session.hashCode());

                 // Use the reactive classification service
                 return classificationService.find(
                         session, UserGroupSecurityTokenClassifications.System,
                         activityMasterSystem,
                         activityMasterSystemUUID
                     )
                            .onItem()
                            .invoke(classification -> log.debug("✅ Found classification: '{}' with session: {}", 
                                                             UserGroupSecurityTokenClassifications.System, session.hashCode()))
                            .onFailure()
                            .invoke(error -> log.error("❌ Failed to find classification with session {}: {}", 
                                                     session.hashCode(), error.getMessage(), error))
                            .chain(classification -> {
                              log.debug("📋 Creating security token for new system with session: {}", session.hashCode());
                              // Now that we have the classification, chain the reactive operations
                              return securityTokenService.create(
                                      session, UserGroupSecurityTokenClassifications.System.toString(),
                                      newSystem.getName(),
                                      newSystem.getDescription(),
                                      activityMasterSystem
                                  )
                                         .onItem()
                                         .invoke(token -> log.debug("✅ Created security token for new system with session: {}", session.hashCode()))
                                         .onFailure()
                                         .invoke(error -> log.error("❌ Failed to create security token for new system with session {}: {}", 
                                                                  session.hashCode(), error.getMessage(), error))
                                         .chain(newSystemsSecurityToken -> {
                                           log.debug("📋 Creating second security token with session: {}", session.hashCode());
                                           // Create second security token (reactive)
                                           return securityTokenService.create(
                                                   session, UserGroupSecurityTokenClassifications.System.toString(),
                                                   UserGroupSecurityTokenClassifications.System.toString(),
                                                   UserGroupSecurityTokenClassifications.System.classificationDescription(),
                                                   activityMasterSystem
                                               )
                                                      .onItem()
                                                      .invoke(token -> log.debug("✅ Created second security token with session: {}", session.hashCode()))
                                                      .onFailure()
                                                      .invoke(error -> log.error("❌ Failed to create second security token with session {}: {}", 
                                                                               session.hashCode(), error.getMessage(), error))
                                                      .chain(systemsToken -> {
                                                        log.debug("📋 Linking tokens with session: {}", session.hashCode());
                                                        // Link tokens (reactive)
                                                        return securityTokenService.link(
                                                                session, systemsToken,
                                                                newSystemsSecurityToken,
                                                                classification
                                                            )
                                                                   .onItem()
                                                                   .invoke(result -> log.debug("✅ Linked tokens with session: {}", session.hashCode()))
                                                                   .onFailure()
                                                                   .invoke(error -> log.error("❌ Failed to link tokens with session {}: {}", 
                                                                                            session.hashCode(), error.getMessage(), error))
                                                                   .chain(v -> {
                                                                     log.debug("📋 Adding classification to new system with session: {}", session.hashCode());
                                                                     // Add classification to new system - include in chain
                                                                     return newSystem.addOrReuseClassification(
                                                                         session, SystemsClassifications.SystemIdentity,
                                                                         ((SecurityToken) newSystemsSecurityToken).getSecurityToken(),
                                                                         newSystem,
                                                                         activityMasterSystemUUID
                                                                     )
                                                                            .onItem()
                                                                            .invoke(result -> log.debug("✅ Added classification to new system with session: {}", session.hashCode()))
                                                                            .onFailure()
                                                                            .invoke(error -> log.error("❌ Failed to add classification to new system with session {}: {}", 
                                                                                                     session.hashCode(), error.getMessage(), error))
                                                                            // Get security identity token
                                                                            .chain(result -> {
                                                                              log.debug("📋 Getting security identity token with session: {}", session.hashCode());
                                                                              return getSecurityIdentityToken(session, newSystem, activityMasterSystemUUID)
                                                                                         .onItem()
                                                                                         .invoke(uuid -> log.debug("✅ Got security identity token: '{}' with session: {}", 
                                                                                                                uuid, session.hashCode()))
                                                                                         .onFailure()
                                                                                         .invoke(error -> log.error("❌ Failed to get security identity token with session {}: {}", 
                                                                                                                  session.hashCode(), error.getMessage(), error))
                                                                                         .chain(newSystemUUID -> {
                                                                                           log.debug("📋 Creating default security for tokens sequentially with session: {}", session.hashCode());
                                                                                           
                                                                                           // Create default security sequentially (first token)
                                                                                           return ((SecurityToken) newSystemsSecurityToken).createDefaultSecurity(
                                                                                                   session,
                                                                                                   activityMasterSystem,
                                                                                                   activityMasterSystemUUID
                                                                                               )
                                                                                                      .onItem()
                                                                                                      .invoke(firstSecurityResult -> log.debug("✅ Created default security for first token with session: {}", session.hashCode()))
                                                                                                      .onFailure()
                                                                                                      .invoke(error -> log.error("❌ Failed to create default security for first token with session {}: {}", 
                                                                                                                               session.hashCode(), error.getMessage(), error))
                                                                                                      // Then create default security for second token
                                                                                                      .chain(firstSecurityComplete -> {
                                                                                                        log.debug("📋 Creating default security for second token with session: {}", session.hashCode());
                                                                                                        return ((SecurityToken) systemsToken).createDefaultSecurity(
                                                                                                                session,
                                                                                                                activityMasterSystem,
                                                                                                                activityMasterSystemUUID
                                                                                                            )
                                                                                                                   .onItem()
                                                                                                                   .invoke(secondSecurityResult -> log.debug("✅ Created default security for second token with session: {}", session.hashCode()))
                                                                                                                   .onFailure()
                                                                                                                   .invoke(error -> log.error("❌ Failed to create default security for second token with session {}: {}", 
                                                                                                                                            session.hashCode(), error.getMessage(), error));
                                                                                                      })
                                                                                                      // Then create involved party
                                                                                                      .chain(secondSecurityComplete -> {
                                                                                                        log.debug("📋 Creating involved party for new system with session: {}", session.hashCode());
                                                                                                        // Create involved party and wait for it to complete
                                                                                                        SystemsSystem systemsSystem = IGuiceContext.get(SystemsSystem.class);
                                                                                                        return systemsSystem.createInvolvedPartyForNewSystem(session, newSystem)
                                                                                                                   .onItem()
                                                                                                                   .invoke(ip -> log.debug("✅ Created involved party for new system with session: {}", session.hashCode()))
                                                                                                                   .onFailure()
                                                                                                                   .invoke(error -> log.error("❌ Failed to create involved party for new system with session {}: {}", 
                                                                                                                                            session.hashCode(), error.getMessage(), error))
                                                                                                                   // Finally return the system UUID as a string
                                                                                                                   .chain(ip -> {
                                                                                                                     log.info("🎉 Successfully registered new system: '{}' with UUID: '{}'", newSystem.getName(), newSystemUUID);
                                                                                                                     return Uni.createFrom().item(newSystemUUID.toString());
                                                                                                                   });
                                                                                                      });
                                                                                         });
                                                                            });
                                                                   });
                                                      });
                                         });
                            });
               });
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
                                                       persistedSystem.createDefaultSecurity(session, activityMaster, identityToken)
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
               .map(IRelationshipValue::getValueAsUUID);
  }
}

