package com.guicedee.activitymaster.fsdm;

import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
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
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;

@Log4j2
@Singleton
public class EventsService
        implements IEventService<EventsService>
{
    // Stateless detached-prepped reference-type cache (event type), keyed by enterpriseId → name.
    // Safe: detached scalar projection, stable install-time reference types; only cached on a real hit.
    private static final Map<UUID, Map<String, IEventType<?, ?>>> STATELESS_EVENT_TYPE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

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
        // Public create → world-readable (public/default security matrix).
        return createEventWithSecurity(session, eventType, key,
                event -> event.createDefaultSecurity(session, system, identityToken), system, identityToken);
    }

    @Override
    public Uni<IEvent<?, ?>> createEventScopeRestricted(Mutiny.Session session, String eventType, UUID key,
            com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
            ISystems<?, ?> system, UUID... identityToken)
    {
        // Opt-in scope-restricted create: only Administrators/Systems/Apps/Plugins + the scope token may read.
        return createEventWithSecurity(session, eventType, key,
                event -> event.createScopeRestrictedSecurity(session, system, scopeToken, identityToken), system, identityToken);
    }

    private Uni<IEvent<?, ?>> createEventWithSecurity(Mutiny.Session session, String eventType, UUID key,
            java.util.function.Function<Event, Uni<?>> securityFn, ISystems<?, ?> system, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();

        Event event = new Event();
        if (key != null)
        {
            event.setId(key);
        }
        event.setEnterpriseID(enterprise);
        event.setSystemID(system);
        event.setOriginalSourceSystemID(system.getId());

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        return acService.getActiveFlag(session, enterprise, identityToken)
                       .chain(activeFlag -> {
                           event.setActiveFlagID(activeFlag);
                           return session.persist(event)
                                          .replaceWith(Uni.createFrom()
                                                               .item(event));
                       })
                       .chain(persistedEvent -> {
                           return securityFn.apply(persistedEvent)
                               .onItem().invoke(() -> log.trace("Security setup completed successfully for event"))
                               .onFailure().invoke(error -> log.warn("Error in security setup for event: " + error.getMessage()))
                               .onFailure().recoverWithItem(() -> null)
                               .chain(() -> persistedEvent.addEventTypes(session, eventType, "", NoClassification.toString(), system, identityToken)
                                          .map(result -> persistedEvent));
                       });
    }

    // ============================================================================================
    // Stateless event create (world-readable default security), mirroring the managed
    // createEvent(Mutiny.Session, …). Uses session.insert + the stateless resolveDefaultGroupFolderTokens/
    // createDefaultSecurity path and the stateless addEventTypes mixin. The enterprise/system references
    // are taken from the (prepped) system parameter — no managed fetch.
    // ============================================================================================

    @Override
    public Uni<IEvent<?, ?>> createEvent(Mutiny.StatelessSession session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        return createEvent(session, eventType, null, system, identityToken);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEvent<?, ?>> createEvent(Mutiny.StatelessSession session, String eventType, UUID key, ISystems<?, ?> system, UUID... identityToken)
    {
        return createEventStateless(session, eventType, key, system, null, false, identityToken);
    }

    /**
     * Stateless opt-in <strong>scope-restricted</strong> event create — the stateless twin of
     * {@link #createEventScopeRestricted(Mutiny.Session, String, UUID, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken, ISystems, UUID...)}.
     * The event is secured with the restricted matrix (no Everyone/Everywhere/Guests; {@code scopeToken}=read).
     * Each create runs on its own stateless unit, so independent stateless sessions can provision events in parallel.
     */
    @Override
    public Uni<IEvent<?, ?>> createEventScopeRestricted(Mutiny.StatelessSession session, String eventType, UUID key,
            com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
            ISystems<?, ?> system, UUID... identityToken)
    {
        return createEventStateless(session, eventType, key, system, scopeToken, true, identityToken);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Uni<IEvent<?, ?>> createEventStateless(Mutiny.StatelessSession session, String eventType, UUID key, ISystems<?, ?> system,
            com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
            boolean restricted, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();

        Event event = new Event();
        event.setId(key != null ? key : UUID.randomUUID());
        event.setEnterpriseID(enterprise);
        event.setSystemID(system);
        event.setOriginalSourceSystemID(system.getId());

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService<?> sts =
                IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);

        return acService.getActiveFlag(session, enterprise, identityToken)
                       .chain(activeFlag -> {
                           event.setActiveFlagID(activeFlag);
                           return session.insert(event)
                                          .chain(() -> sts.resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                  .chain(tokens -> restricted
                                                          ? event.createScopeRestrictedSecurity(session, system, enterprise, activeFlag, tokens, scopeToken, identityToken)
                                                          : event.createDefaultSecurity(session, system, enterprise, activeFlag, tokens, identityToken))
                                                  .onFailure().recoverWithItem(0L)
                                                  .replaceWithVoid())
                                          .chain(() -> event.addEventTypes(session, eventType, "", NoClassification.toString(), system, identityToken))
                                          .replaceWith((IEvent<?, ?>) event);
                       });
    }

    @Override
    public Uni<IEventType<?, ?>> createEventType(Mutiny.Session session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();

        EventType et = new EventType();
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
                               return acService.getActiveFlag(session, enterprise, identityToken)
                                              .chain(activeFlag -> {
                                                  etBuilt.setActiveFlagID(activeFlag);
                                                  etBuilt.setOriginalSourceSystemID(system.getId());
                                                  return session.persist(etBuilt).replaceWith(Uni.createFrom().item(etBuilt));
                                              })
                                              .chain(persistedEt -> {
                                                  return persistedEt.createDefaultSecurity(session, system, identityToken)
                                                      .onItem().invoke(() -> log.trace("Security setup completed successfully for event type"))
                                                      .onFailure().invoke(error -> log.warn("Error in createDefaultSecurity for event type: " + error.getMessage()))
                                                      .onFailure().recoverWithItem(() -> null)
                                                      .map(_ -> persistedEt);
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

    @Override
    public Uni<IEventType<?, ?>> findEventType(Mutiny.StatelessSession session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        UUID enterpriseId = enterprise.getId();
        Map<String, IEventType<?, ?>> byName = STATELESS_EVENT_TYPE_CACHE.computeIfAbsent(enterpriseId, k -> new java.util.concurrent.ConcurrentHashMap<>());
        IEventType<?, ?> hit = byName.get(eventType);
        if (hit != null) {
            return Uni.createFrom().item((IEventType<?, ?>) hit);
        }
        Uni<IEventType<?, ?>> resolved = new EventType().builder(session)
                       .withName(eventType)
                       .withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange()
                       .selectColumn(com.guicedee.activitymaster.fsdm.db.entities.events.EventType_.id)
                       .selectColumn(com.guicedee.activitymaster.fsdm.db.entities.events.EventType_.name)
                       .selectColumn(com.guicedee.activitymaster.fsdm.db.entities.events.EventType_.description)
                       .get(Object[].class)
                       .onItem().ifNull().failWith(() -> new EventException("Invalid Event Type - " + eventType))
                       .map(row -> {
                           EventType prepped = new EventType((UUID) row[0], (String) row[1], (String) row[2]);
                           prepped.setEnterpriseID(enterprise);
                           prepped.setFake(false);
                           return (IEventType<?, ?>) prepped;
                       });
        return resolved.onItem().invoke(t -> { if (t != null && t.getId() != null) byName.put(eventType, t); });
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

    // ============================================================================================
    // Stateless finder twins (builder reads; event associations resolved via session.fetch).
    // ============================================================================================

    private <L> Uni<List<IEvent<?, ?>>> fetchEventsStateless(Mutiny.StatelessSession session, List<L> links, java.util.function.Function<L, IEvent<?, ?>> getEvent)
    {
        Uni<List<IEvent<?, ?>>> acc = Uni.createFrom().item(new ArrayList<>());
        for (L l : links)
        {
            acc = acc.chain(out -> session.fetch(getEvent.apply(l)).map(e -> { out.add(e); return out; }));
        }
        return acc;
    }

    private Uni<List<java.util.UUID>> childEventIdsStateless(Mutiny.StatelessSession session, IEvent<?, ?> withParent)
    {
        return new EventXEvent().builder(session).inActiveRange().inDateRange()
                .where(EventXEvent_.parentEventID, Equals, (Event) withParent)
                .getAll()
                .chain(links -> {
                    Uni<List<java.util.UUID>> acc = Uni.createFrom().item(new ArrayList<>());
                    for (EventXEvent l : links)
                    {
                        acc = acc.chain(ids -> session.fetch(l.getChildEventID()).map(child -> {
                            if (child != null && child.getId() != null) { ids.add(child.getId()); }
                            return ids;
                        }));
                    }
                    return acc;
                });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Uni<List<IEvent<?, ?>>> findEventsByClassificationOpStateless(Mutiny.StatelessSession session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, com.entityassist.enumerations.Operand op, UUID... identityToken)
    {
        var enterprise = systems.getEnterprise();
        return classificationService.find(session, classificationName, systems, identityToken)
                .chain(classification -> {
                    if (classification == null) { return Uni.createFrom().item(Collections.<IEvent<?, ?>>emptyList()); }
                    EventQueryBuilder eqb = new Event().builder(session);
                    eqb.withEnterprise(enterprise).inActiveRange().inDateRange();
                    JoinExpression<Event, Classification, ?> je = new JoinExpression<>();
                    EventXClassificationQueryBuilder qb = new EventXClassification().builder(session);
                    qb.withEnterprise(enterprise).withClassification((Classification) classification).withValue(op, value).inActiveRange().inDateRange();
                    eqb.join(Event_.classifications, qb, JoinType.INNER, je);
                    if (withParent != null) {
                        return childEventIdsStateless(session, withParent).chain(ids -> {
                            if (ids.isEmpty()) { return Uni.createFrom().item(Collections.<IEvent<?, ?>>emptyList()); }
                            eqb.where(Event_.id, InList, ids);
                            eqb.orderBy(Event_.effectiveFromDate, OrderByType.DESC);
                            return eqb.getAll().map(l -> new ArrayList<IEvent<?, ?>>(l));
                        });
                    }
                    eqb.orderBy(Event_.effectiveFromDate, OrderByType.DESC);
                    return eqb.getAll().map(l -> new ArrayList<IEvent<?, ?>>(l));
                });
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEvent<?, ?>> find(Mutiny.StatelessSession session, UUID id)
    {
        return (Uni) new Event().builder(session).find(id).get()
                .onItem().ifNull().failWith(() -> new NoSuchElementException("Event not found with id: " + id));
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassification(Mutiny.StatelessSession session, String classificationName, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationOpStateless(session, classificationName, null, value, systems, Equals, identityToken);
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassification(Mutiny.StatelessSession session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationOpStateless(session, classificationName, withParent, value, systems, Equals, identityToken);
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassificationGT(Mutiny.StatelessSession session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationOpStateless(session, classificationName, withParent, value, systems, GreaterThan, identityToken);
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassificationGTE(Mutiny.StatelessSession session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationOpStateless(session, classificationName, withParent, value, systems, GreaterThanEqualTo, identityToken);
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassificationLT(Mutiny.StatelessSession session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationOpStateless(session, classificationName, withParent, value, systems, LessThan, identityToken);
    }

    @Override
    public Uni<List<IEvent<?, ?>>> findEventsByClassificationLTE(Mutiny.StatelessSession session, String classificationName, IEvent<?, ?> withParent, String value, ISystems<?, ?> systems, UUID... identityToken)
    {
        return findEventsByClassificationOpStateless(session, classificationName, withParent, value, systems, LessThanEqualTo, identityToken);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEvent<?, ?>> findEventByResourceItem(Mutiny.StatelessSession session, IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        final String cn = Strings.isNullOrEmpty(classificationName) ? NoClassification.toString() : classificationName;
        var enterprise = system.getEnterprise();
        return (Uni) classificationService.find(session, cn, system, identityToken)
                .chain(classification -> new EventXResourceItem().builder(session)
                        .inActiveRange().inDateRange().withEnterprise(enterprise).withClassification(classification).withValue(value)
                        .where(EventXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
                        .orderBy(EventXResourceItem_.effectiveFromDate, OrderByType.DESC)
                        .get()
                        .chain(result -> result == null ? Uni.createFrom().nullItem() : session.fetch(result.getEventID())));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEvent<?, ?>> findEventByArrangement(Mutiny.StatelessSession session, IArrangement<?, ?> arrangement, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        final String cn = Strings.isNullOrEmpty(classificationName) ? NoClassification.toString() : classificationName;
        var enterprise = system.getEnterprise();
        return (Uni) classificationService.find(session, cn, system, identityToken)
                .chain(classification -> new EventXArrangement().builder(session)
                        .inActiveRange().inDateRange().withEnterprise(enterprise).withClassification(classification).withValue(value)
                        .where(EventXArrangement_.arrangementID, Equals, (Arrangement) arrangement)
                        .orderBy(EventXArrangement_.effectiveFromDate, OrderByType.DESC)
                        .get()
                        .chain(result -> result == null ? Uni.createFrom().nullItem() : session.fetch(result.getEventID())));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEvent<?, ?>> findEventByProduct(Mutiny.StatelessSession session, IProduct<?, ?> product, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        final String cn = Strings.isNullOrEmpty(classificationName) ? NoClassification.toString() : classificationName;
        var enterprise = system.getEnterprise();
        return (Uni) classificationService.find(session, cn, system, identityToken)
                .chain(classification -> new EventXProduct().builder(session)
                        .inActiveRange().inDateRange().withEnterprise(enterprise).withClassification(classification).withValue(value)
                        .where(EventXProduct_.productID, Equals, (Product) product)
                        .orderBy(EventXProduct_.effectiveFromDate, OrderByType.DESC)
                        .get()
                        .chain(result -> result == null ? Uni.createFrom().nullItem() : session.fetch(result.getEventID())));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEvent<?, ?>> findEventByInvolvedParty(Mutiny.StatelessSession session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        final String cn = Strings.isNullOrEmpty(classificationName) ? NoClassification.toString() : classificationName;
        var enterprise = system.getEnterprise();
        return (Uni) classificationService.find(session, cn, system, identityToken)
                .chain(classification -> new EventXInvolvedParty().builder(session)
                        .inActiveRange().inDateRange().withEnterprise(enterprise).withClassification(classification).withValue(value)
                        .where(EventXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                        .orderBy(EventXInvolvedParty_.effectiveFromDate, OrderByType.DESC)
                        .get()
                        .chain(result -> result == null ? Uni.createFrom().nullItem() : session.fetch(result.getEventID())));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IEvent<?, ?>>> findEventsByInvolvedParty(Mutiny.StatelessSession session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        final String cn = Strings.isNullOrEmpty(classificationName) ? NoClassification.toString() : classificationName;
        var enterprise = system.getEnterprise();
        return classificationService.find(session, cn, system, identityToken)
                .chain(classification -> new EventXInvolvedParty().builder(session)
                        .inActiveRange().inDateRange().withEnterprise(enterprise).withClassification(classification).withValue(value)
                        .where(EventXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                        .orderBy(EventXInvolvedParty_.effectiveFromDate, OrderByType.DESC)
                        .getAll()
                        .chain(list -> fetchEventsStateless(session, list, EventXInvolvedParty::getEventID)));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IEvent<?, ?>>> findEventsByInvolvedParty(Mutiny.StatelessSession session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, ISystems<?, ?> system, UUID... identityToken)
    {
        final String cn = Strings.isNullOrEmpty(classificationName) ? NoClassification.toString() : classificationName;
        var enterprise = system.getEnterprise();
        return classificationService.find(session, cn, system, identityToken)
                .chain(classification -> new EventXInvolvedParty().builder(session)
                        .inActiveRange().inDateRange(startDate, com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD.EndOfTime)
                        .withEnterprise(enterprise).withClassification(classification).withValue(value)
                        .where(EventXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                        .orderBy(EventXInvolvedParty_.effectiveFromDate, OrderByType.DESC)
                        .getAll()
                        .chain(list -> fetchEventsStateless(session, list, EventXInvolvedParty::getEventID)));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IEvent<?, ?>>> findEventsByInvolvedParty(Mutiny.StatelessSession session, IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, LocalDateTime endDate, ISystems<?, ?> system, UUID... identityToken)
    {
        final String cn = Strings.isNullOrEmpty(classificationName) ? NoClassification.toString() : classificationName;
        var enterprise = system.getEnterprise();
        return classificationService.find(session, cn, system, identityToken)
                .chain(classification -> new EventXInvolvedParty().builder(session)
                        .inActiveRange().inDateRange(startDate, endDate)
                        .withEnterprise(enterprise).withClassification(classification).withValue(value)
                        .where(EventXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
                        .orderBy(EventXInvolvedParty_.effectiveFromDate, OrderByType.DESC)
                        .getAll()
                        .chain(list -> fetchEventsStateless(session, list, EventXInvolvedParty::getEventID)));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IEvent<?, ?>>> findEventsByRules(Mutiny.StatelessSession session, IRules<?, ?> rules, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        final String cn = Strings.isNullOrEmpty(classificationName) ? NoClassification.toString() : classificationName;
        var enterprise = system.getEnterprise();
        return classificationService.find(session, cn, system, identityToken)
                .chain(classification -> new EventXRules().builder(session)
                        .inActiveRange().inDateRange().withEnterprise(enterprise).withClassification(classification).withValue(value)
                        .where(EventXRules_.rulesID, Equals, (Rules) rules)
                        .orderBy(EventXRules_.effectiveFromDate, OrderByType.DESC)
                        .getAll()
                        .chain(list -> fetchEventsStateless(session, list, EventXRules::getEventID)));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IEvent<?, ?>>> findAll(Mutiny.StatelessSession session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        return findEventType(session, eventType, system, identityToken)
                .chain(type -> new EventXEventType().builder(session)
                        .inActiveRange().inDateRange()
                        .findLink(null, (EventType) type, null)
                        .getAll()
                        .chain(links -> fetchEventsStateless(session, links, EventXEventType::getEventID)));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IEventType<?, ?>> createEventType(Mutiny.StatelessSession session, String eventType, ISystems<?, ?> system, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        return new EventType().builder(session)
                .withName(eventType)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .getCount()
                .chain(count -> {
                    if (count != null && count > 0) {
                        return findEventType(session, eventType, system, identityToken);
                    }
                    EventType etBuilt = new EventType();
                    etBuilt.setId(UUID.randomUUID());
                    etBuilt.setName(eventType);
                    etBuilt.setDescription(eventType);
                    etBuilt.setSystemID(system);
                    etBuilt.setEnterpriseID(enterprise);
                    etBuilt.setOriginalSourceSystemID(system.getId());
                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                    com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService<?> sts =
                            IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);
                    return acService.getActiveFlag(session, enterprise, identityToken)
                            .chain(activeFlag -> {
                                etBuilt.setActiveFlagID(activeFlag);
                                com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable core =
                                        (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable) etBuilt;
                                return session.insert(etBuilt)
                                        .chain(() -> sts.resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                .chain(tokens -> core.createDefaultSecurity(session, system, enterprise, activeFlag, tokens, identityToken))
                                                .onFailure().recoverWithItem(0L))
                                        .replaceWith((IEventType<?, ?>) etBuilt);
                            });
                });
    }
}

