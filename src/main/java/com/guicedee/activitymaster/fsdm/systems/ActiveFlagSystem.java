package com.guicedee.activitymaster.fsdm.systems;

import com.entityassist.enumerations.ActiveFlag;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.MasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.ActivateFlagSystemName;


@Log4j2
public class ActiveFlagSystem
    extends MasterDefaultSystem<ActiveFlagSystem>
    implements IMasterSystem<ActiveFlagSystem>
{
  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private IActiveFlagService<?> activeFlagService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Active Flag System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Active Flag System with session: {}", session.hashCode());

    return systemsService
               .create(session, enterprise, ActivateFlagSystemName, "The system for the active flag management")
               .onItem()
               .ifNull()
               .continueWith(() -> {
                 log.warn("Active Flag system creation returned null, trying to find it");
                 return null;
               })
               .chain(system -> {
                 if (system == null) {
                   return systemsService.findSystem(session, enterprise, ActivateFlagSystemName);
                 }
                 return Uni.createFrom().item(system);
               })
               .chain(system -> {
                 if (system == null) {
                   log.error("❌ Failed to resolve Active Flag system for enterprise: '{}'", enterprise.getName());
                   return Uni.createFrom().failure(new RuntimeException("Failed to resolve Active Flag system"));
                 }
                 log.debug("✅ Found/Created Active Flag System: '{}' with session: {}", system.getName(), session.hashCode());
                 return systemsService.registerNewSystem(session, enterprise, system)
                            .replaceWith(system);
               })
               .onFailure()
               .invoke(error -> log.error("❌ Failed to create Active Flag System: '{}' with session {}: {}",
                   getSystemName(), session.hashCode(), error.getMessage(), error))
               .map(result -> result);
  }

  /**
   * Stateless variant of {@link #createDefaults(Mutiny.StatelessSession, IEnterprise)} — creates every
   * {@link ActiveFlag} reference row on a {@link Mutiny.StatelessSession} via the stateless find-or-create.
   */
  @Override
  public Uni<Void> createDefaults(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Active Flag Service", "Loading Active Flags (stateless)");
    log.info("🚀 (stateless) Creating active flags for enterprise: '{}'", enterprise.getName());

    Uni<Void> chain = Uni.createFrom().voidItem();
    for (ActiveFlag activeFlag : ActiveFlag.values())
    {
      final ActiveFlag ct = activeFlag;
      chain = chain.chain(() -> ((ActiveFlagService) activeFlagService)
                 .create(session, enterprise, ct.name(), ct.getDescription())
                 .replaceWithVoid());
    }
    return chain
               .invoke(() -> log.info("🎉 (stateless) Successfully created all active flags"))
               .onFailure().invoke(error -> log.error("❌ (stateless) Error creating active flags: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
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
