package com.guicedee.activitymaster.fsdm.systems;

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
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;

@Log4j2
public class EnterpriseSystem
    extends ActivityMasterDefaultSystem<EnterpriseSystem>
    implements IActivityMasterSystem<EnterpriseSystem>
{
  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
        return systemsService
               .create(session, enterprise, EnterpriseSystemName, "The Enterprise Management System", "Enterprise")
               .onItem()
               .ifNull()
               .continueWith(() -> {
                 log.warn("Enterprise system creation returned null, trying to find it");
                 return null;
               })
               .chain(system -> {
                 if (system == null) {
                   return systemsService.findSystem(session, enterprise, EnterpriseSystemName);
                 }
                 return Uni.createFrom().item(system);
               })
               .chain(system -> {
                 if (system == null) {
                   log.error("❌ Failed to resolve Enterprise system for enterprise: '{}'", enterprise.getName());
                   return Uni.createFrom().failure(new RuntimeException("Failed to resolve Enterprise system"));
                 }
                 log.debug("✅ Found/Created Enterprise System: '{}' with session: {}", system.getName(), session.hashCode());
                 return systemsService.registerNewSystem(session, enterprise, system)
                            .replaceWith(system);
               })
               .onFailure()
               .invoke(error -> log.error("❌ Failed to register Enterprise System: '{}' with session {}: {}",
                   EnterpriseSystemName, session.hashCode(), error.getMessage(), error))
               .map(result->result);
  }


  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Enterprise System", "Starting Enterprise Checks");
    log.info("🚀 Creating enterprise defaults for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with session: {}", session.hashCode());
    
    // No actual operations needed, just return a void item
    log.debug("✅ No specific defaults needed for Enterprise System");
    return Uni.createFrom()
               .voidItem()
               .onItem()
               .invoke(() -> log.info("🎉 Successfully completed Enterprise System defaults"))
               .onFailure()
               .invoke(error -> log.error("❌ Error in Enterprise System defaults: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Enterprise System");
    log.debug("📋 Beginning postStartup operations for enterprise: '{}' with session: {}", 
             enterprise.getName(), session.hashCode());

    // Create a reactive chain for the postStartup operations
    // Get the system
    return systemsService.findSystem(session, enterprise, getSystemName())
               .onItem()
               .invoke(system -> log.debug("✅ Found system: '{}'", system.getName()))
               .onItem()
               .ifNull()
               .failWith(() -> new RuntimeException("System not found: " + getSystemName()))
               .onFailure()
               .invoke(error -> log.error("❌ Failed to find system: {}", error.getMessage(), error))
               .chain(system -> {
                 log.debug("🔍 Retrieving security token for system: '{}'", system.getName());
                 // Get the security token
                 return systemsService.getSecurityIdentityToken(session, system)
                            .onItem()
                            .invoke(token -> log.debug("🔑 Found security token for system: '{}'", system.getName()))
                            .onItem()
                            .ifNull()
                            .failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
                            .onFailure()
                            .invoke(error -> log.error("❌ Failed to retrieve security token: {}", error.getMessage(), error))
                            .map(token -> {
                              log.debug("✅ Successfully completed postStartup for Enterprise System");
                              return null; // Return Void
                            });
               })
               .replaceWith(Uni.createFrom().voidItem())
               .onItem()
               .invoke(() -> log.info("🎉 Enterprise System postStartup completed successfully"))
               .onFailure()
               .invoke(error -> log.error("❌ Error in Enterprise System postStartup: {}", error.getMessage(), error));
  }

  @Override
  public int totalTasks()
  {
    return 0;
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE;
  }

  @Override
  public String getSystemName()
  {
    return EnterpriseSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for handling enterprises";
  }
}
