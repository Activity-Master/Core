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
 * [!] Synchronous execution of reactive chains
 *     - Most reactive chains execute synchronously
 *     - The createEvent and createEventType methods call createDefaultSecurity
 *       without properly chaining its result, explicitly not waiting for it to complete
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
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.EventException;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventType;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import lombok.extern.java.Log;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;
import static com.guicedee.client.IGuiceContext.get;

@Log
@Singleton
public class EventsService
        implements IEventService<EventsService>
{
    @Inject
    private Vertx vertx;

    @Override
    public Uni<IEvent<?, ?>> get()
    {
        return Uni.createFrom()
                       .item(new Event());
    }

    @Override
    public Uni<IEvent<?, ?>> find(Mutiny.Session session, UUID id)
    {
        return new Event().builder(session)
                       .find(id)
                       .get()
                       .onItem()
                       .ifNull()
                       .failWith(() -> new NoSuchElementException("Event not found with id: " + id))
                       .map(result -> result);
    }

    @Override
    public Uni<IEvent<?, ?>> createEvent(Mutiny.Session session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        return createEvent(session, eventType, null, system, identityToken);
    }

    @Override
    public Uni<IEvent<?, ?>> createEvent(Mutiny.Session session, String eventType, UUID key, ISystems<?, ?> system, UUID... identityToken)
    {
        Event event = new Event();
        if (key != null)
        {
            event.setId(key);
        }
      var enterprise = system.getEnterprise();
        event.setEnterpriseID(enterprise);
        event.setSystemID(system);
        event.setOriginalSourceSystemID(system.getId());

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        return acService.getActiveFlag(session, enterprise)
                       .chain(activeFlag -> {
                           event.setActiveFlagID(activeFlag);
                           return session.persist(event)
                                          .replaceWith(Uni.createFrom()
                                                               .item(event));
                       })
                       .chain(persistedEvent -> {
                           // Chain the createDefaultSecurity operation properly
                           return persistedEvent.createDefaultSecurity(session, system, identityToken)
                               .onItem().invoke(() -> log.info("Security setup completed successfully for event"))
                               .onFailure().invoke(error -> log.warning("Error in createDefaultSecurity for event: " + error.getMessage()))
                               .onFailure().recoverWithItem(() -> null) // Continue even if security setup fails
                               .chain(() -> persistedEvent.addEventTypes(session, eventType, "", NoClassification.toString(), system, identityToken)
                                          .map(result -> persistedEvent));
                       });

    }

    @Override
    public Uni<IEventType<?, ?>> createEventType(Mutiny.Session session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        EventType et = new EventType();
        var enterprise = system.getEnterprise();
        return et.builder(session)
                       .withName(eventType)
                       .withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange()
                       .getCount()
                       .map(count -> count > 0)
                       .chain(exists -> {
                           if (!exists)
                           {
                               if (et.getId() == null)
                               {
                                   et.setId(UUID.randomUUID());
                               }

                               EventType etBuilt = new EventType();
                               etBuilt.setId(et.getId());
                               etBuilt.setName(eventType);
                               etBuilt.setDescription(eventType);
                               etBuilt.setSystemID(system);
                               etBuilt.setEnterpriseID(enterprise);

                               IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                               return acService.getActiveFlag(session, enterprise)
                                              .chain(activeFlag -> {
                                                  etBuilt.setActiveFlagID(activeFlag);
                                                  etBuilt.setOriginalSourceSystemID(system.getId());
                                                  return session.persist(etBuilt).replaceWith(Uni.createFrom().item(etBuilt));
                                              })
                                              .chain(persistedEt -> {
                                                  // Chain the createDefaultSecurity operation properly
                                                  return persistedEt.createDefaultSecurity(session, system, identityToken)
                                                      .onItem().invoke(() -> log.info("Security setup completed successfully for event type"))
                                                      .onFailure().invoke(error -> log.warning("Error in createDefaultSecurity for event type: " + error.getMessage()))
                                                      .onFailure().recoverWithItem(() -> null) // Continue even if security setup fails
                                                      .map(result -> persistedEt);
                                              });
                           }
                           else
                           {
                               return findEventType(session, eventType, system, identityToken);
                           }
                       });
    }

    @Override
    //@CacheResult(cacheName = "EventTypesStrings")
    public Uni<IEventType<?, ?>> findEventType(Mutiny.Session session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        return new EventType().builder(session)
                       .withName(eventType)
                       .withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange()
                       //  .canRead(system, identityToken)
                       .get()
                       .onItem()
                       .ifNull()
                       .failWith(() -> new EventException("Invalid Event Type - " + eventType))
                       .map(result -> result);
    }
}

