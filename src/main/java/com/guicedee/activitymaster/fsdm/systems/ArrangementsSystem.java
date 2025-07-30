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

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.client.services.IArrangementsService.ArrangementSystemName;


@Log4j2
public class ArrangementsSystem
    extends ActivityMasterDefaultSystem<ArrangementsSystem>
    implements IActivityMasterSystem<ArrangementsSystem>
{
  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Arrangements System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Arrangements System with session: {}", session.hashCode());

    return systemsService
               .create(session, enterprise, getSystemName(), getSystemDescription())
               .chain(system -> {
                 log.debug("✅ Created Arrangements System: '{}' with session: {}", system.getName(), session.hashCode());

                 // Chain the registerNewSystem call properly
                 return getSystem(session, enterprise)
                            .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                            .onItem()
                            .invoke(() -> {
                              log.debug("✅ Registered system: {}", getSystemName());
                              log.info("🎉 Successfully registered Arrangements System for enterprise: '{}'", enterprise.getName());
                            })
                            .onFailure()
                            .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                            .chain(() -> Uni.createFrom()
                                             .item(system));
               })
               .onFailure()
               .invoke(error -> log.error("❌ Failed to create Arrangements System: '{}' with session {}: {}",
                   getSystemName(), session.hashCode(), error.getMessage(), error))
               .map(result -> result);
  }

  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Arrangements System", "Starting Arrangements Checks");
    log.info("🚀 Creating arrangement defaults for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with session: {}", session.hashCode());
    // No actual operations needed, just return a void item
    log.debug("✅ No specific defaults needed for Arrangements System");
    return Uni.createFrom()
               .voidItem()
               .onItem()
               .invoke(() -> log.info("🎉 Successfully completed Arrangements System defaults"))
               .onFailure()
               .invoke(error -> log.error("❌ Error in Arrangements System defaults: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Arrangements System");
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
                              log.debug("✅ Successfully completed postStartup for Arrangements System");
                              return null; // Return Void
                            });
               })
               .replaceWith(Uni.createFrom()
                                .voidItem())
               .onItem()
               .invoke(() -> log.info("🎉 Arrangements System postStartup completed successfully"))
               .onFailure()
               .invoke(error -> log.error("❌ Error in Arrangements System postStartup: {}", error.getMessage(), error));
  }

  @Override
  public int totalTasks()
  {
    return 4;
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE + 8;
  }

  @Override
  public String getSystemName()
  {
    return ArrangementSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for the arrangement management";
  }

}
