package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.MasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.IEventService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;


@Log4j2
public class EventsSystem
    extends MasterDefaultSystem<EventsSystem>
    implements IMasterSystem<EventsSystem>
{
  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private IEventService<?> eventService;

  @Inject
  private IResourceItemService<?> resourceItemServiceProvider;

  @Inject
  private IClassificationService<?> classificationServiceProvider;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Events System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Events System with session: {}", session.hashCode());

    return systemsService
        .create(session, enterprise, getSystemName(), getSystemDescription())
        .chain(system -> {
            log.debug("✅ Created Events System: '{}' with session: {}", system.getName(), session.hashCode());
            
            // Chain the registerNewSystem call properly
            return getSystem(session, enterprise)
                .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                .onItem()
                .invoke(() -> {
                    log.debug("✅ Registered system: {}", getSystemName());
                    log.info("🎉 Successfully registered Events System for enterprise: '{}'", enterprise.getName());
                })
                .onFailure()
                .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                .chain(() -> Uni.createFrom().item(system)); // Chain back to return the original system
        })
        .onFailure()
        .invoke(error -> log.error("❌ Failed to create Events System: '{}' with session {}: {}",
            getSystemName(), session.hashCode(), error.getMessage(), error))
               .map(result->result);
  }

  /**
   * Stateless end-to-end variant of {@link #createDefaults(Mutiny.StatelessSession, IEnterprise)} — provisions the
   * LogItemTypes / EventStatus concept classifications, every {@code LogItemTypes} value under LogItemTypes,
   * and the LogItem resource-item type, entirely on a {@link Mutiny.StatelessSession}.
   */
  @Override
  public Uni<Void> createDefaults(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Loading Events", "Events creating default types (stateless)");
    log.info("🚀 (stateless) Creating event defaults for enterprise: '{}'", enterprise.getName());

    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
               .chain(activityMasterSystem -> getSystemToken(session, enterprise)
                   .chain(systemToken -> {
                     UUID[] tokens = systemToken == null ? new UUID[0] : new UUID[]{systemToken};
                     return classificationServiceProvider.create(session, "LogItemTypes", "The log item event registered types", Classification, activityMasterSystem, tokens)
                                .chain(v -> classificationServiceProvider.create(session, "EventStatus", "The status of the event", EventXClassification, activityMasterSystem, tokens))
                                .chain(v -> {
                                  Uni<Void> chain = Uni.createFrom().voidItem();
                                  for (com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes value : com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes.values())
                                  {
                                    final com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes ct = value;
                                    chain = chain.chain(() -> classificationServiceProvider.create(session, ct, activityMasterSystem, "LogItemTypes", tokens).replaceWithVoid());
                                  }
                                  return chain;
                                })
                                .chain(v -> resourceItemServiceProvider.createType(session, "LogItem", "An attached log item", activityMasterSystem, tokens).replaceWithVoid());
                   }))
               .invoke(() -> log.info("✅ (stateless) Successfully created all event defaults"))
               .onFailure().invoke(error -> log.error("❌ (stateless) Failed to create event defaults: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Events System");
    log.debug("📋 Beginning postStartup operations for enterprise: '{}' with session: {}",
        enterprise.getName(), session.hashCode());

    // Create a reactive chain for the postStartup operations
    // Get the system
    return systemsService.findSystem(session, enterprise, getSystemName())
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
                              log.debug("✅ Successfully completed postStartup for Events System");
                              return system; // Return Void
                            });
               })
               .invoke(system -> log.debug("✅ Found system: '{}'", system.getName()))
               .replaceWith(Uni.createFrom()
                                .voidItem());
  }

  @Override
  public int totalTasks()
  {
    return 0;
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE + 9;
  }

  @Override
  public String getSystemName()
  {
    return EventSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for managing events";
  }
}
