package com.guicedee.activitymaster.fsdm.systems;

import com.entityassist.enumerations.ActiveFlag;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.ActivateFlagSystemName;


@Log4j2
public class ActiveFlagSystem
    extends ActivityMasterDefaultSystem<ActiveFlagSystem>
    implements IActivityMasterSystem<ActiveFlagSystem>
{
  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private IActiveFlagService<?> activeFlagService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Active Flag System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Active Flag System with session: {}", session.hashCode());

    return systemsService
               .create(session, enterprise, ActivateFlagSystemName, "The system for the active flag management")
               .onItem()
               .invoke(system -> {
                 log.debug("✅ Created Active Flag System: '{}' with session: {}", system.getName(), session.hashCode());

                 // Chain the registerNewSystem call properly
                 getSystem(session, enterprise)
                            .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                            .onItem()
                            .invoke(() -> {
                                log.debug("✅ Registered system: {}", getSystemName());
                                log.info("🎉 Successfully registered Active Flag System for enterprise: '{}'", enterprise.getName());
                            })
                            .onFailure()
                            .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                            .chain(() -> Uni.createFrom().item(system)); // Chain back to return the original system
               })
               .onFailure()
               .invoke(error -> log.error("❌ Failed to create Active Flag System: '{}' with session {}: {}",
                   getSystemName(), session.hashCode(), error.getMessage(), error));
  }

  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Active Flag Service", "Loading Active Flags");
    log.info("🚀 Creating active flags for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with session: {}", session.hashCode());

    // Create a list of all active flags
    ActiveFlag[] activeFlags = ActiveFlag.values();
    log.debug("📋 Found {} active flags to create", activeFlags.length);

    // Create the first active flag and then chain the rest
    Uni<Void> createChain = null;

    for (ActiveFlag activeFlag : activeFlags)
    {
      if (createChain == null)
      {
        // First flag
        log.debug("🔄 Creating first active flag: '{}'", activeFlag.name());
        createChain = ((ActiveFlagService) activeFlagService)
                          .create(session, enterprise, activeFlag.name(), activeFlag.getDescription())
                          .onItem()
                          .invoke(result -> log.debug("✅ Created active flag: '{}'", activeFlag.name()))
                          .onFailure()
                          .invoke(error -> log.error("❌ Failed to create active flag '{}': {}",
                              activeFlag.name(), error.getMessage(), error))
                          .map(result -> null); // Convert to Void
      }
      else
      {
        // Chain subsequent flags
        final Uni<Void> finalChain = createChain;
        final ActiveFlag currentFlag = activeFlag; // Create final reference for lambda
        createChain = finalChain.chain(v -> {
          log.debug("🔄 Creating next active flag: '{}'", currentFlag.name());
          return ((ActiveFlagService) activeFlagService)
                     .create(session, enterprise, currentFlag.name(), currentFlag.getDescription())
                     .onItem()
                     .invoke(result -> log.debug("✅ Created active flag: '{}'", currentFlag.name()))
                     .onFailure()
                     .invoke(error -> log.error("❌ Failed to create active flag '{}': {}",
                         currentFlag.name(), error.getMessage(), error))
                     .map(result -> null); // Convert to Void
        });
      }
    }

    // Return the reactive chain or an empty one if no flags were created
    return (createChain != null ? createChain : Uni.createFrom()
                                                    .voidItem())
               .onItem()
               .invoke(() -> log.info("🎉 Successfully created all active flags"))
               .onFailure()
               .invoke(error -> log.error("❌ Error creating active flags: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Active Flag System");
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
                              log.debug("✅ Successfully completed postStartup for Active Flag System");
                              return null; // Return Void
                            });
               })
               .replaceWith(Uni.createFrom()
                                .voidItem())
               .onItem()
               .invoke(() -> log.info("🎉 Active Flag System postStartup completed successfully"))
               .onFailure()
               .invoke(error -> log.error("❌ Error in Active Flag System postStartup: {}", error.getMessage(), error));
  }

  @Override
  public int totalTasks()
  {
    return ActiveFlag.values().length;
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE + 1;
  }

  @Override
  public String getSystemName()
  {
    return ActivateFlagSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for the active flag management";
  }
}
