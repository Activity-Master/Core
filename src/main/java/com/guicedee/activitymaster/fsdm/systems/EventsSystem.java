package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.Date;

import static com.guicedee.activitymaster.fsdm.client.services.IEventService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;


@Log4j2
public class EventsSystem
    extends ActivityMasterDefaultSystem<EventsSystem>
    implements IActivityMasterSystem<EventsSystem>
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
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
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

  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Loading Events", "Events creating default types");
    logProgress("Loading Time", "Loading in Today");
    log.debug("📋 Starting event defaults creation for enterprise: '{}'", enterprise.getName());
    // Get the day using reactive ITimeSystem
//    log.debug("📅 Getting today's date using reactive ITimeSystem");
/*    com.guicedee.client.IGuiceContext.get(ITimeSystem.class)
        .getDay(session, new Date())
        .onItem().invoke(day -> log.debug("✅ Successfully retrieved day: {}", day.getId()))
        .onFailure().invoke(error -> log.error("❌ Failed to retrieve day: {}", error))
        .await().indefinitely();*/

    // Start reactive chain with getting the ActivityMaster system
    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
               .onItem()
               .invoke(activityMasterSystem ->
                           log.debug("✅ Found ActivityMaster system: '{}' with session: {}",
                               activityMasterSystem.getName(), session.hashCode()))
               .onFailure()
               .invoke(error ->
                           log.error("❌ Failed to find ActivityMaster system: {}", error.getMessage(), error))
               .chain(activityMasterSystem -> {
                 logProgress("Loading Logging Types", "Creating Log Types");
                 log.debug("🔍 Creating event classifications and types");

                 // Get system token once and reuse it
                 return getSystemToken(session, enterprise)
                            .onItem()
                            .invoke(systemToken ->
                                        log.debug("🔑 Retrieved system token for enterprise: '{}'", enterprise.getName()))
                            .onFailure()
                            .invoke(error ->
                                        log.error("❌ Failed to retrieve system token: {}", error.getMessage(), error))
                            .chain(systemToken -> {
                              // Create base classifications sequentially
                              log.debug("📋 Creating base event classifications");
                              return classificationServiceProvider.create(
                                      session, "LogItemTypes", "The log item event registered types",
                                      Classification, activityMasterSystem, systemToken)
                                         .onItem()
                                         .invoke(logItemTypes ->
                                                     log.debug("✅ Created LogItemTypes classification: '{}'", logItemTypes.getName()))
                                         .onFailure()
                                         .invoke(error ->
                                                     log.error("❌ Failed to create LogItemTypes classification: {}", error.getMessage(), error))
                                         .chain(logItemTypes -> {
                                           return classificationServiceProvider.create(
                                                   session, "EventStatus", "The status of the event",
                                                   EventXClassification, activityMasterSystem, systemToken)
                                                      .onItem()
                                                      .invoke(eventStatus ->
                                                                  log.debug("✅ Created EventStatus classification: '{}'", eventStatus.getName()))
                                                      .onFailure()
                                                      .invoke(error ->
                                                                  log.error("❌ Failed to create EventStatus classification: {}", error.getMessage(), error))
                                                      .chain(eventStatus -> {
                                                        // Create LogItemTypes classifications sequentially
                                                        log.debug("📋 Creating LogItemTypes classifications sequentially");
                                                        
                                                        // Start with a completed Uni to begin the chain
                                                        Uni<Void> sequentialChain = Uni.createFrom().voidItem();
                                                        
                                                        // Process each LogItemType sequentially by chaining operations
                                                        for (LogItemTypes value : LogItemTypes.values())
                                                        {
                                                          final LogItemTypes currentValue = value; // Create final reference for lambda
                                                          sequentialChain = sequentialChain.chain(() -> 
                                                              classificationServiceProvider.create(
                                                                      session, currentValue, activityMasterSystem, "LogItemTypes", systemToken)
                                                                  .onItem()
                                                                  .invoke(classification ->
                                                                              log.debug("✅ Created LogItemType classification: '{}'", classification.getName()))
                                                                  .onFailure()
                                                                  .invoke(error ->
                                                                              log.error("❌ Failed to create LogItemType classification '{}': {}",
                                                                                  currentValue, error.getMessage(), error))
                                                                  .replaceWithVoid()
                                                          );
                                                        }
                                                        
                                                        // Continue with the chain after all LogItemTypes are processed
                                                        return sequentialChain
                                                                   .onItem()
                                                                   .invoke(() -> log.debug("✅ Successfully created all LogItemTypes classifications sequentially"))
                                                                   .onFailure()
                                                                   .invoke(error ->
                                                                               log.error("❌ Error creating LogItemTypes classifications: {}", error.getMessage(), error))
                                                                   .chain(v -> {
                                                                     // Create LogItem resource type
                                                                     log.debug("📋 Creating LogItem resource type");
                                                                     return resourceItemServiceProvider.createType(
                                                                             session, "LogItem", "An attached log item",
                                                                             activityMasterSystem, systemToken)
                                                                                .onItem()
                                                                                .invoke(result -> {
                                                                                  log.debug("✅ Created LogItem resource type");
                                                                                  logProgress("Loading Time", "Creating Hours and Minutes");

                                                                                  log.debug("⏰ Creating time using reactive ITimeSystem");
                                                                                  com.guicedee.client.IGuiceContext.get(ITimeSystem.class)
                                                                                      .createTime()
                                                                                      .onItem().invoke(() -> log.debug("✅ Successfully created time entities"))
                                                                                      .onFailure().invoke(error -> log.error("❌ Failed to create time entities: {}", error))
                                                                                      .await().atMost(Duration.ofMinutes(1));
                                                                                })
                                                                                .onFailure()
                                                                                .invoke(error ->
                                                                                            log.error("❌ Failed to create LogItem resource type: {}", error.getMessage(), error));
                                                                   });
                                                      });
                                         });
                            });
               })
               .onItem()
               .invoke(() -> log.info("✅ Successfully created all event defaults"))
               .onFailure()
               .invoke(error ->
                           log.error("❌ Failed to create event defaults: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
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
