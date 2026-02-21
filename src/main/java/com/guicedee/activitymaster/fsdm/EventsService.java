package com.guicedee.activitymaster.fsdm;

import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.EventException;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventXClassificationQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.criteria.JoinType;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.LocalDateTime;
import java.util.*;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.applicationEnterpriseName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;

@Log4j2
@Singleton
public class EventsService
        implements IEventService<EventsService>
{
    @Inject
    private IClassificationService<?> classificationService;

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
        return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
            var createSession = tuple.getItem1();
            var createEnterprise = tuple.getItem2();
            var createSystem = tuple.getItem3();
            var createIdentityToken = tuple.getItem4();

            Event event = new Event();
            if (key != null)
            {
                event.setId(key);
            }
            event.setEnterpriseID(createEnterprise);
            event.setSystemID(createSystem);
            event.setOriginalSourceSystemID(createSystem.getId());

            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
            return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
                           .chain(activeFlag -> {
                               event.setActiveFlagID(activeFlag);
                               return createSession.persist(event)
                                              .replaceWith(Uni.createFrom()
                                                                   .item(event));
                           })
                           .chain(persistedEvent -> {
                               // Chain the createDefaultSecurity operation properly
                               return persistedEvent.createDefaultSecurity(createSession, createSystem, createIdentityToken)
                                   .onItem().invoke(() -> log.trace("Security setup completed successfully for event"))
                                   .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for event: " + error.getMessage()))
                                   .onFailure().recoverWithItem(() -> null) // Continue even if security setup fails
                                   .chain(() -> persistedEvent.addEventTypes(createSession, eventType, "", NoClassification.toString(), createSystem, createIdentityToken)
                                              .map(result -> persistedEvent));
                           });
        });
    }

    @Override
    public Uni<IEventType<?, ?>> createEventType(Mutiny.Session session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        return SessionUtils.withActivityMaster(applicationEnterpriseName, system.getName(), tuple -> {
            var createSession = tuple.getItem1();
            var createEnterprise = tuple.getItem2();
            var createSystem = tuple.getItem3();
            var createIdentityToken = tuple.getItem4();

            EventType et = new EventType();
            return et.builder(createSession)
                           .withName(eventType)
                           .withEnterprise(createEnterprise)
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
                                   etBuilt.setSystemID(createSystem);
                                   etBuilt.setEnterpriseID(createEnterprise);

                                   IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                   return acService.getActiveFlag(createSession, createEnterprise, createIdentityToken)
                                                  .chain(activeFlag -> {
                                                      etBuilt.setActiveFlagID(activeFlag);
                                                      etBuilt.setOriginalSourceSystemID(createSystem.getId());
                                                      return createSession.persist(etBuilt).replaceWith(Uni.createFrom().item(etBuilt));
                                                  })
                                                  .chain(persistedEt -> {
                                                      // Chain the createDefaultSecurity operation properly
                                                      return persistedEt.createDefaultSecurity(createSession, createSystem, createIdentityToken)
                                                          .onItem().invoke(() -> log.trace("Security setup completed successfully for event type"))
                                                          .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for event type: " + error.getMessage()))
                                                          .onFailure().recoverWithItem(() -> null) // Continue even if security setup fails
                                                          .map(_ -> persistedEt);
                                                  });
                               }
                               else
                               {
                                   return findEventType(createSession, eventType, createSystem, createIdentityToken);
                               }
                           });
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

    // --- Cross-domain searchable queries (EventX<DomainType>) ---

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassification(Mutiny.Session session, String classificationName, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding events by classification - name: {}, value: {}", classificationName, value);
        var enterprise = systems.getEnterprise();
        return classificationService.find(session, classificationName, systems, identityToken)
                .chain(classification -> {
                    if (classification == null)
                    {
                        return Uni.createFrom().item(Collections.<IEvent<?, ?>>emptyList());
                    }
                    EventQueryBuilder eqb = new Event().builder(session);
                    eqb.withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange();

                    JoinExpression<Event, Classification, ?> je = new JoinExpression<>();
                    EventXClassificationQueryBuilder qb = new EventXClassification().builder(session);
                    qb.withEnterprise(enterprise)
                      .withClassification((Classification) classification)
                      .withValue(value)
                      .inActiveRange()
                      .inDateRange();

                    eqb.join(Event_.classifications, qb, JoinType.INNER, je);
                    eqb.orderBy(Event_.effectiveFromDate, OrderByType.DESC);

                    return eqb.getAll().map(ArrayList::new);
                });
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassification(Mutiny.Session session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        log.trace("Finding events by classification with parent - name: {}, value: {}, parent: {}",
                classificationName, value, withParent != null ? withParent.getId() : null);
        var enterprise = systems.getEnterprise();
        return classificationService.find(session, classificationName, systems, identityToken)
                .chain(classification -> {
                    if (classification == null)
                    {
                        return Uni.createFrom().item(Collections.<IEvent<?, ?>>emptyList());
                    }
                    EventQueryBuilder eqb = new Event().builder(session);
                    eqb.withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange();

                    JoinExpression<Event, Classification, ?> je = new JoinExpression<>();
                    EventXClassificationQueryBuilder qb = new EventXClassification().builder(session);
                    qb.withEnterprise(enterprise)
                      .withClassification((Classification) classification)
                      .withValue(value)
                      .inActiveRange()
                      .inDateRange();
                    eqb.join(Event_.classifications, qb, JoinType.INNER, je);

                    if (withParent != null)
                    {
                        // Restrict to children of provided parent
                        return new EventXEvent().builder(session)
                                .inActiveRange()
                                .inDateRange()
                                .where(EventXEvent_.parentEventID, Equals, (Event) withParent)
                                .getAll()
                                .map(links -> {
                                    List<UUID> ids = new ArrayList<>();
                                    for (EventXEvent l : links)
                                    {
                                        Event child = l.getChildEventID();
                                        if (child != null && child.getId() != null)
                                        {
                                            ids.add(child.getId());
                                        }
                                    }
                                    return ids;
                                })
                                .chain(ids -> {
                                    if (ids.isEmpty())
                                    {
                                        return Uni.createFrom().item(Collections.<IEvent<?, ?>>emptyList());
                                    }
                                    eqb.where(Event_.id, InList, ids);
                                    eqb.orderBy(Event_.effectiveFromDate, OrderByType.DESC);
                                    return eqb.getAll().map(ArrayList::new);
                                });
                    }
                    eqb.orderBy(Event_.effectiveFromDate, OrderByType.DESC);
                    return eqb.getAll().map(ArrayList::new);
                });
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassificationGT(Mutiny.Session session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationWithOp(session, classificationName, withParent, value, systems, GreaterThan, identityToken);
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassificationGTE(Mutiny.Session session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationWithOp(session, classificationName, withParent, value, systems, GreaterThanEqualTo, identityToken);
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassificationLT(Mutiny.Session session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationWithOp(session, classificationName, withParent, value, systems, LessThan, identityToken);
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassificationLTE(Mutiny.Session session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationWithOp(session, classificationName, withParent, value, systems, LessThanEqualTo, identityToken);
    }

    private Uni<List<IEvent<?, ?>>> findEventsByClassificationWithOp(Mutiny.Session session,
                                                                      String classificationName,
                                                                      IEvent<?, ?> withParent,
                                                                      String value,
                                                                      ISystems<?, ?> systems,
                                                                      com.entityassist.enumerations.Operand op,
                                                                      UUID... identityToken)
    {
        log.trace("Finding events by classification op - name: {}, op: {}, value: {}", classificationName, op, value);
        var enterprise = systems.getEnterprise();
        return classificationService.find(session, classificationName, systems, identityToken)
                .chain(classification -> {
                    if (classification == null)
                    {
                        return Uni.createFrom().item(Collections.<IEvent<?, ?>>emptyList());
                    }
                    EventQueryBuilder eqb = new Event().builder(session);
                    eqb.withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange();

                    JoinExpression<Event, Classification, ?> je = new JoinExpression<>();
                    EventXClassificationQueryBuilder qb = new EventXClassification().builder(session);
                    qb.withEnterprise(enterprise)
                      .withClassification((Classification) classification)
                      .withValue(op, value)
                      .inActiveRange()
                      .inDateRange();
                    eqb.join(Event_.classifications, qb, JoinType.INNER, je);

                    if (withParent != null)
                    {
                        return new EventXEvent().builder(session)
                                .inActiveRange()
                                .inDateRange()
                                .where(EventXEvent_.parentEventID, Equals, (Event) withParent)
                                .getAll()
                                .map(links -> {
                                    List<UUID> ids = new ArrayList<>();
                                    for (EventXEvent l : links)
                                    {
                                        Event child = l.getChildEventID();
                                        if (child != null && child.getId() != null)
                                        {
                                            ids.add(child.getId());
                                        }
                                    }
                                    return ids;
                                })
                                .chain(ids -> {
                                    if (ids.isEmpty())
                                    {
                                        return Uni.createFrom().item(Collections.<IEvent<?, ?>>emptyList());
                                    }
                                    eqb.where(Event_.id, InList, ids);
                                    eqb.orderBy(Event_.effectiveFromDate, OrderByType.DESC);
                                    return eqb.getAll().map(ArrayList::new);
                                });
                    }
                    eqb.orderBy(Event_.effectiveFromDate, OrderByType.DESC);
                    return eqb.getAll().map(ArrayList::new);
                });
    }

    @Override
    public Uni<IEvent<?, ?>> findEventByResourceItem(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding event by resource item: {}, classification: {}, value: {}",
                resourceItem.getId(), classificationName, value);
        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        final String finalClassificationName = classificationName;
        return classificationService.find(session, classificationName, system, identityToken)
                .chain(classification -> new EventXResourceItem().builder(session)
                        .inActiveRange()
                        .inDateRange()
                        .withEnterprise(enterprise)
                        .withClassification(classification)
                        .withValue(value)
                        .where(EventXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
                        .orderBy(EventXResourceItem_.effectiveFromDate, OrderByType.DESC)
                        .get()
                        .map(result -> result != null ? result.getEventID() : null));
    }

    @Override
    public Uni<IEvent<?, ?>> findEventByArrangement(Mutiny.Session session, IArrangement<?, ?> arrangement, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding event by arrangement: {}, classification: {}, value: {}",
                arrangement.getId(), classificationName, value);
        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        final String finalClassificationName = classificationName;
        return classificationService.find(session, classificationName, system, identityToken)
                .chain(classification -> new EventXArrangement().builder(session)
                        .inActiveRange()
                        .inDateRange()
                        .withEnterprise(enterprise)
                        .withClassification(classification)
                        .withValue(value)
                        .where(EventXArrangement_.arrangementID, Equals, (Arrangement) arrangement)
                        .orderBy(EventXArrangement_.effectiveFromDate, OrderByType.DESC)
                        .get()
                        .map(result -> result != null ? result.getEventID() : null));
    }

    @Override
    public Uni<IEvent<?, ?>> findEventByProduct(Mutiny.Session session, IProduct<?, ?> product, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding event by product: {}, classification: {}, value: {}",
                product.getId(), classificationName, value);
        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        final String finalClassificationName = classificationName;
        return classificationService.find(session, classificationName, system, identityToken)
                .chain(classification -> new EventXProduct().builder(session)
                        .inActiveRange()
                        .inDateRange()
                        .withEnterprise(enterprise)
                        .withClassification(classification)
                        .withValue(value)
                        .where(EventXProduct_.productID, Equals, (Product) product)
                        .orderBy(EventXProduct_.effectiveFromDate, OrderByType.DESC)
                        .get()
                        .map(result -> result != null ? result.getEventID() : null));
    }

    @Override
    public Uni<IEvent<?, ?>> findEventByInvolvedParty(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding event by involved party: {}, classification: {}, value: {}",
                involvedParty.getId(), classificationName, value);
        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        final String finalClassificationName = classificationName;
        return classificationService.find(session, classificationName, system, identityToken)
                .chain(classification -> new EventXInvolvedParty().builder(session)
                        .inActiveRange()
                        .inDateRange()
                        .withEnterprise(enterprise)
                        .withClassification(classification)
                        .withValue(value)
                        .where(EventXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                        .orderBy(EventXInvolvedParty_.effectiveFromDate, OrderByType.DESC)
                        .get()
                        .map(result -> result != null ? result.getEventID() : null));
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByInvolvedParty(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding events by involved party: {}, classification: {}, value: {}",
                involvedParty.getId(), classificationName, value);
        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                .chain(classification -> new EventXInvolvedParty().builder(session)
                        .inActiveRange()
                        .inDateRange()
                        .withEnterprise(enterprise)
                        .withClassification(classification)
                        .withValue(value)
                        .where(EventXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                        .orderBy(EventXInvolvedParty_.effectiveFromDate, OrderByType.DESC)
                        .getAll()
                        .map(list -> {
                            List<IEvent<?, ?>> out = new ArrayList<>();
                            for (EventXInvolvedParty l : list)
                            {
                                out.add(l.getEventID());
                            }
                            return out;
                        }));
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByInvolvedParty(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding events by involved party (from date): {}, classification: {}, value: {}, startDate: {}",
                involvedParty.getId(), classificationName, value, startDate);
        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                .chain(classification -> new EventXInvolvedParty().builder(session)
                        .inActiveRange()
                        .inDateRange(startDate, com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.EndOfTime)
                        .withEnterprise(enterprise)
                        .withClassification(classification)
                        .withValue(value)
                        .where(EventXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                        .orderBy(EventXInvolvedParty_.effectiveFromDate, OrderByType.DESC)
                        .getAll()
                        .map(list -> {
                            List<IEvent<?, ?>> out = new ArrayList<>();
                            for (EventXInvolvedParty l : list)
                            {
                                out.add(l.getEventID());
                            }
                            return out;
                        }));
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByInvolvedParty(Mutiny.Session session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, LocalDateTime endDate, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding events by involved party (date window): {}, classification: {}, value: {}, startDate: {}, endDate: {}",
                involvedParty.getId(), classificationName, value, startDate, endDate);
        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                .chain(classification -> new EventXInvolvedParty().builder(session)
                        .inActiveRange()
                        .inDateRange(startDate, endDate)
                        .withEnterprise(enterprise)
                        .withClassification(classification)
                        .withValue(value)
                        .where(EventXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                        .orderBy(EventXInvolvedParty_.effectiveFromDate, OrderByType.DESC)
                        .getAll()
                        .map(list -> {
                            List<IEvent<?, ?>> out = new ArrayList<>();
                            for (EventXInvolvedParty l : list)
                            {
                                out.add(l.getEventID());
                            }
                            return out;
                        }));
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByRules(Mutiny.Session session, IRules<?, ?> rules, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding events by rules: {}, classification: {}, value: {}", rules.getId(), classificationName, value);
        if (Strings.isNullOrEmpty(classificationName))
        {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationName, system, identityToken)
                .chain(classification -> new EventXRules().builder(session)
                        .inActiveRange()
                        .inDateRange()
                        .withEnterprise(enterprise)
                        .withClassification(classification)
                        .withValue(value)
                        .where(EventXRules_.rulesID, Equals, (Rules) rules)
                        .orderBy(EventXRules_.effectiveFromDate, OrderByType.DESC)
                        .getAll()
                        .map(list -> {
                            List<IEvent<?, ?>> out = new ArrayList<>();
                            for (EventXRules l : list)
                            {
                                out.add(l.getEventID());
                            }
                            return out;
                        }));
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findAll(Mutiny.Session session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding all events of type: {}", eventType);
        return findEventType(session, eventType, system, identityToken)
                .chain(type -> new EventXEventType().builder(session)
                        .inActiveRange()
                        .inDateRange()
                        // .canRead(system, identityToken) // enable if security filtering is required here
                        .findLink(null, (EventType) type, null)
                        .getAll()
                        .map(links -> {
                            List<IEvent<?, ?>> out = new ArrayList<>();
                            for (EventXEventType l : links)
                            {
                                out.add(l.getEventID());
                            }
                            return out;
                        }));
    }
}

