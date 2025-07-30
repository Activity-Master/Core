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

import static com.guicedee.activitymaster.fsdm.client.services.IAddressService.AddressSystemName;


@Log4j2
public class AddressSystem
    extends ActivityMasterDefaultSystem<AddressSystem>
    implements IActivityMasterSystem<AddressSystem>
{
  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Address System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Address System with session: {}", session.hashCode());

    return systemsService
        .create(session, enterprise, AddressSystemName, "The system for the address management")
        .chain(system -> {
            log.debug("✅ Created Address System: '{}' with session: {}", system.getName(), session.hashCode());
            
            // Chain the registerNewSystem call properly
            return getSystem(session, enterprise)
                .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                .onItem()
                .invoke(() -> {
                    log.debug("✅ Registered system: {}", getSystemName());
                    log.info("🎉 Successfully registered Address System for enterprise: '{}'", enterprise.getName());
                })
                .onFailure()
                .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                .chain(() -> Uni.createFrom().item(system)); // Chain back to return the original system
        })
        .onFailure()
        .invoke(error -> log.error("❌ Failed to create Address System: '{}' with session {}: {}",
            getSystemName(), session.hashCode(), error.getMessage(), error))
               .map(result->result);
  }

  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Address System", "Starting Address Checks");
    log.info("🚀 Creating address defaults for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with session: {}", session.hashCode());
    // No actual operations needed, just return a void item
    log.debug("✅ No specific defaults needed for Address System");
    return Uni.createFrom()
               .voidItem()
               .onItem()
               .invoke(() -> log.info("🎉 Successfully completed Address System defaults"))
               .onFailure()
               .invoke(error -> log.error("❌ Error in Address System defaults: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Address System");
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
                              log.debug("✅ Successfully completed postStartup for Address System");
                              return null; // Return Void
                            });
               })
               .replaceWith(Uni.createFrom()
                                .voidItem())
               .onItem()
               .invoke(() -> log.info("🎉 Address System postStartup completed successfully"))
               .onFailure()
               .invoke(error -> log.error("❌ Error in Address System postStartup: {}", error.getMessage(), error));
  }


  @Override
  public int totalTasks()
  {
    return 15;
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE + 7;
  }

  @Override
  public String getSystemName()
  {
    return AddressSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for address management";
  }
}
