package com.guicedee.activitymaster.fsdm.systems;

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
    return Uni.createFrom().nullItem();
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
