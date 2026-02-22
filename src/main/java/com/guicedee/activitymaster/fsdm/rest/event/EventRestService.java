package com.guicedee.activitymaster.fsdm.rest.event;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.EventsService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.rest.events.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import com.guicedee.activitymaster.fsdm.client.services.rest.RelationshipUpdateEntry;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import jakarta.ws.rs.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.entityassist.enumerations.Operand.Equals;

@Path("{enterprise}/event")
@Log4j2
public class EventRestService {

    @Inject
    private IEventService<EventsService> eventService;

    // ──────────────────────────────────────────────────────────────────────────
    // Find
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/find")
    public Uni<EventDTO> find(@PathParam("enterprise") String enterpriseName,
                              @PathParam("requestingSystemName") String requestingSystemName,
                              EventFindDTO findDto) {
        UUID eventId = findDto.eventId;
        List<EventDataIncludes> includesList = findDto.includes;
        return SessionUtils.<EventDTO>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    return eventService.find(session, eventId)
                            .chain(event -> {
                                EventDTO dto = new EventDTO();
                                dto.eventId = eventId;
                                Event ev = (Event) event;

                                Uni<EventDTO> chain = Uni.createFrom().item(dto);
                                if (includesList == null || includesList.isEmpty()) {
                                    return chain;
                                }
                                for (EventDataIncludes include : includesList) {
                                    chain = chain.chain(d -> fetchInclude(session, ev, d, include));
                                }
                                return chain;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error finding event {} for enterprise {} system {}: {}",
                                            eventId, enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Create
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/create")
    public Uni<EventDTO> create(@PathParam("enterprise") String enterpriseName,
                                @PathParam("requestingSystemName") String requestingSystemName,
                                EventCreateDTO dto) {
        // Step 1: Resolve system then create — EventsService.createEvent() manages its own session
        return SessionUtils.<ISystems<?, ?>>withActivityMaster(enterpriseName, requestingSystemName,
                (java.util.function.Function<Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]>, Uni<ISystems<?, ?>>>) tuple ->
                        Uni.createFrom().item(tuple.getItem3())
        ).chain(system -> eventService.createEvent(null, dto.type, system)
                .chain(event -> {
                    boolean hasRelationships = hasAnyRelationship(dto);

                    if (!hasRelationships) {
                        return buildCreateResponse(enterpriseName, requestingSystemName, (Event) event, dto);
                    }

                    // Step 2: Add relationships in a fresh session
                    return SessionUtils.<Void>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                                Mutiny.Session session = tuple.getItem1();
                                ISystems<?, ?> sys = tuple.getItem3();
                                UUID[] identityToken = tuple.getItem4();

                                Uni<Void> chain = Uni.createFrom().voidItem();

                                if (dto.classifications != null && !dto.classifications.isEmpty()) {
                                    for (var entry : dto.classifications.entrySet()) {
                                        chain = chain.chain(() -> event
                                                .addOrUpdateClassification(session, entry.getKey(), entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.types != null && !dto.types.isEmpty()) {
                                    for (var entry : dto.types.entrySet()) {
                                        chain = chain.chain(() -> event
                                                .addOrUpdateEventTypes(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.parties != null && !dto.parties.isEmpty()) {
                                    for (var entry : dto.parties.entrySet()) {
                                        chain = chain.chain(() -> event
                                                .addOrUpdateInvolvedParty(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.resources != null && !dto.resources.isEmpty()) {
                                    for (var entry : dto.resources.entrySet()) {
                                        chain = chain.chain(() -> event
                                                .addOrUpdateResourceItem(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.products != null && !dto.products.isEmpty()) {
                                    for (var entry : dto.products.entrySet()) {
                                        chain = chain.chain(() -> event
                                                .addOrUpdateProduct(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.rules != null && !dto.rules.isEmpty()) {
                                    for (var entry : dto.rules.entrySet()) {
                                        chain = chain.chain(() -> event
                                                .addOrUpdateRules(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.arrangements != null && !dto.arrangements.isEmpty()) {
                                    for (var entry : dto.arrangements.entrySet()) {
                                        chain = chain.chain(() -> event
                                                .addOrUpdateArrangement(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.children != null && !dto.children.isEmpty()) {
                                    for (var entry : dto.children.entrySet()) {
                                        UUID childId = UUID.fromString(entry.getKey());
                                        chain = chain.chain(() ->
                                                eventService.find(session, childId)
                                                        .chain(child -> event
                                                                .addChild(session, (Event) child, null, entry.getValue(), sys, identityToken)
                                                                .replaceWithVoid()));
                                    }
                                }

                                return chain;
                            })
                            // Step 3: Build response in a fresh session
                            .chain(() -> buildCreateResponse(enterpriseName, requestingSystemName, (Event) event, dto));
                })
                .onFailure().invoke(e ->
                        log.error("Error creating event for enterprise {} and system {}: {}",
                                enterpriseName, requestingSystemName, e.getMessage(), e)
                )
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update
    // ──────────────────────────────────────────────────────────────────────────

    @PUT
    @Path("{requestingSystemName}/update")
    public Uni<EventDTO> update(@PathParam("enterprise") String enterpriseName,
                                @PathParam("requestingSystemName") String requestingSystemName,
                                EventUpdateDTO dto) {
        UUID eventId = dto.eventId;
        return SessionUtils.<EventDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] identityToken = tuple.getItem4();

            return eventService.find(session, eventId)
                    .chain(event -> {
                        Uni<Void> chain = Uni.createFrom().voidItem();

                        // ── Classifications ──
                        chain = chainAddOrUpdate(chain, dto.classifications, (name, value) ->
                                event.addOrUpdateClassification(session, name, value, system, identityToken).replaceWithVoid());
                        chain = chainDelete(chain, dto.classifications, name ->
                                event.removeClassification(session, name, null, system, identityToken).replaceWithVoid());

                        // ── Event Types ──
                        chain = chainAddOrUpdate(chain, dto.types, (name, value) ->
                                event.addOrUpdateEventTypes(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDelete(chain, dto.types, name ->
                                event.removeEventTypes(session, name, null, null, null, system, identityToken).replaceWithVoid());

                        // ── Involved Parties ──
                        chain = chainAddOrUpdate(chain, dto.parties, (name, value) ->
                                event.addOrUpdateInvolvedParty(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.parties, session, event, EventXInvolvedParty.class,
                                EventXInvolvedParty_.eventID,
                                link -> link.getClassificationID(),
                                cls -> cls != null ? cls.getName() : null);

                        // ── Resource Items ──
                        chain = chainAddOrUpdate(chain, dto.resources, (name, value) ->
                                event.addOrUpdateResourceItem(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.resources, session, event, EventXResourceItem.class,
                                EventXResourceItem_.eventID,
                                link -> link.getClassificationID(),
                                cls -> cls != null ? cls.getName() : null);

                        // ── Products ──
                        chain = chainAddOrUpdate(chain, dto.products, (name, value) ->
                                event.addOrUpdateProduct(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.products, session, event, EventXProduct.class,
                                EventXProduct_.eventID,
                                link -> link.getClassificationID(),
                                cls -> cls != null ? cls.getName() : null);

                        // ── Rules ──
                        chain = chainAddOrUpdate(chain, dto.rules, (name, value) ->
                                event.addOrUpdateRules(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.rules, session, event, EventXRules.class,
                                EventXRules_.eventID,
                                link -> link.getClassificationID(),
                                cls -> cls != null ? cls.getName() : null);

                        // ── Arrangements ──
                        chain = chainAddOrUpdate(chain, dto.arrangements, (name, value) ->
                                event.addOrUpdateArrangement(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.arrangements, session, event, EventXArrangement.class,
                                EventXArrangement_.eventID,
                                link -> link.getClassificationID(),
                                cls -> cls != null ? cls.getName() : null);

                        // ── Children ──
                        chain = chainAddOrUpdate(chain, dto.children, (childIdStr, value) -> {
                            UUID childId = UUID.fromString(childIdStr);
                            return eventService.find(session, childId)
                                    .chain(child -> event.addChild(session, (Event) child, null, value, system, identityToken)
                                            .replaceWithVoid());
                        });
                        chain = chainDeleteChildrenByExpire(chain, dto.children, session, event);

                        // ── Build response ──
                        return chain.chain(() -> {
                            EventDTO response = new EventDTO();
                            response.eventId = eventId;
                            Event ev = (Event) event;

                            List<EventDataIncludes> includes = determineIncludes(dto);

                            Uni<EventDTO> fetchChain = Uni.createFrom().item(response);
                            for (EventDataIncludes include : includes) {
                                fetchChain = fetchChain.chain(d -> fetchInclude(session, ev, d, include));
                            }
                            return fetchChain;
                        });
                    })
                    .onFailure().invoke(e ->
                            log.error("Error updating event {} for enterprise {} and system {}: {}",
                                    eventId, enterpriseName, requestingSystemName, e.getMessage(), e)
                    );
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Include fetching — all use session.fetch() for lazy-loaded entities
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<EventDTO> fetchInclude(Mutiny.Session session, Event event, EventDTO dto, EventDataIncludes include) {
        return switch (include) {
            case Types -> new EventXEventType().builder(session)
                    .where(EventXEventType_.eventID, Equals, event)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXEventType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getEventTypeID())
                                    .invoke(type -> {
                                        String key = type != null && type.getName() != null ? type.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.types = map; return dto; });
                    });

            case Classifications -> new EventXClassification().builder(session)
                    .where(EventXClassification_.eventID, Equals, event)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXClassification link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .invoke(classification -> {
                                        String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.classifications = map; return dto; });
                    });

            case Parties -> new EventXInvolvedParty().builder(session)
                    .where(EventXInvolvedParty_.eventID, Equals, event)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXInvolvedParty link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getInvolvedPartyID())
                                            .invoke(ip -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = ip != null && ip.getId() != null ? ip.getId().toString() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.parties = map; return dto; });
                    });

            case Resources -> new EventXResourceItem().builder(session)
                    .where(EventXResourceItem_.eventID, Equals, event)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXResourceItem link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getResourceItemID())
                                            .invoke(resource -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = resource != null && resource.getId() != null ? resource.getId().toString() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.resources = map; return dto; });
                    });

            case Products -> new EventXProduct().builder(session)
                    .where(EventXProduct_.eventID, Equals, event)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXProduct link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getProductID())
                                            .invoke(product -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = product != null && product.getId() != null ? product.getId().toString() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.products = map; return dto; });
                    });

            case Rules -> new EventXRules().builder(session)
                    .where(EventXRules_.eventID, Equals, event)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXRules link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getRulesID())
                                            .invoke(rules -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = rules != null && rules.getId() != null ? rules.getId().toString() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.rules = map; return dto; });
                    });

            case Arrangements -> new EventXArrangement().builder(session)
                    .where(EventXArrangement_.eventID, Equals, event)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXArrangement link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getArrangementID())
                                            .invoke(arrangement -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = arrangement != null && arrangement.getId() != null ? arrangement.getId().toString() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.arrangements = map; return dto; });
                    });

            case Children -> new EventXEvent().builder(session)
                    .where(EventXEvent_.parentEventID, Equals, event)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXEvent link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getChildEventID())
                                    .invoke(child -> {
                                        String key = child != null && child.getId() != null ? child.getId().toString() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.children = map; return dto; });
                    });
        };
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Response builders
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<EventDTO> buildCreateResponse(String enterpriseName, String requestingSystemName,
                                               Event event, EventCreateDTO dto) {
        return SessionUtils.<EventDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();

            EventDTO response = new EventDTO();
            response.eventId = event.getId();

            List<EventDataIncludes> includes = new ArrayList<>();
            includes.add(EventDataIncludes.Types);
            includes.add(EventDataIncludes.Classifications);
            if (dto.parties != null && !dto.parties.isEmpty()) includes.add(EventDataIncludes.Parties);
            if (dto.resources != null && !dto.resources.isEmpty()) includes.add(EventDataIncludes.Resources);
            if (dto.products != null && !dto.products.isEmpty()) includes.add(EventDataIncludes.Products);
            if (dto.rules != null && !dto.rules.isEmpty()) includes.add(EventDataIncludes.Rules);
            if (dto.arrangements != null && !dto.arrangements.isEmpty()) includes.add(EventDataIncludes.Arrangements);
            if (dto.children != null && !dto.children.isEmpty()) includes.add(EventDataIncludes.Children);

            Uni<EventDTO> fetchChain = Uni.createFrom().item(response);
            for (EventDataIncludes include : includes) {
                fetchChain = fetchChain.chain(d -> fetchInclude(session, event, d, include));
            }
            return fetchChain;
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update helpers
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<Void> chainAddOrUpdate(Uni<Void> chain, RelationshipUpdateEntry entry,
                                       java.util.function.BiFunction<String, String, Uni<Void>> addOrUpdateFn) {
        if (entry == null || entry.addOrUpdate == null || entry.addOrUpdate.isEmpty()) return chain;
        for (var e : entry.addOrUpdate.entrySet()) {
            chain = chain.chain(() -> addOrUpdateFn.apply(e.getKey(), e.getValue()));
        }
        return chain;
    }

    private Uni<Void> chainDelete(Uni<Void> chain, RelationshipUpdateEntry entry,
                                  java.util.function.Function<String, Uni<Void>> deleteFn) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;
        for (String name : entry.delete) {
            chain = chain.chain(() -> deleteFn.apply(name));
        }
        return chain;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <L extends WarehouseBaseTable<L, ?, UUID>, R> Uni<Void> chainDeleteByExpire(
            Uni<Void> chain,
            RelationshipUpdateEntry entry,
            Mutiny.Session session,
            IEvent<?, ?> event,
            Class<L> linkClass,
            jakarta.persistence.metamodel.SingularAttribute eventAttr,
            java.util.function.Function<L, R> relatedGetter,
            java.util.function.Function<R, String> nameExtractor) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;

        Set<String> namesToDelete = new HashSet<>(entry.delete);

        return chain.chain(() -> {
            try {
                L instance = linkClass.getDeclaredConstructor().newInstance();
                return ((com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD) instance.builder(session)
                        .where(eventAttr, Equals, event))
                        .inActiveRange()
                        .inDateRange()
                        .getAll()
                        .chain(list -> {
                            Uni<Void> expireChain = Uni.createFrom().voidItem();
                            for (Object row : (List<?>) list) {
                                L link = (L) row;
                                expireChain = expireChain.chain(() -> {
                                    R relatedProxy = relatedGetter.apply(link);
                                    return session.fetch(relatedProxy)
                                            .chain(related -> {
                                                String name = nameExtractor.apply(related);
                                                if (name != null && namesToDelete.contains(name)) {
                                                    return ((WarehouseBaseTable) link).expire().replaceWithVoid();
                                                }
                                                return Uni.createFrom().voidItem();
                                            });
                                });
                            }
                            return expireChain;
                        });
            } catch (Exception e) {
                log.error("Failed to instantiate link class {} for expire: {}", linkClass.getSimpleName(), e.getMessage(), e);
                return Uni.createFrom().voidItem();
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Uni<Void> chainDeleteChildrenByExpire(Uni<Void> chain, RelationshipUpdateEntry entry,
                                                   Mutiny.Session session, IEvent<?, ?> event) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;
        Set<String> idsToDelete = new HashSet<>(entry.delete);
        Event ev = (Event) event;

        return chain.chain(() ->
                new EventXEvent().builder(session)
                        .where(EventXEvent_.parentEventID, Equals, ev)
                        .inActiveRange()
                        .inDateRange()
                        .getAll()
                        .chain(list -> {
                            Uni<Void> expireChain = Uni.createFrom().voidItem();
                            for (EventXEvent link : (List<EventXEvent>) list) {
                                expireChain = expireChain.chain(() ->
                                        session.fetch(link.getChildEventID())
                                                .chain(child -> {
                                                    String childId = child != null && child.getId() != null ? child.getId().toString() : null;
                                                    if (childId != null && idsToDelete.contains(childId)) {
                                                        return ((WarehouseBaseTable) link).expire().replaceWithVoid();
                                                    }
                                                    return Uni.createFrom().voidItem();
                                                })
                                );
                            }
                            return expireChain;
                        })
        );
    }

    private List<EventDataIncludes> determineIncludes(EventUpdateDTO dto) {
        List<EventDataIncludes> includes = new ArrayList<>();
        if (hasEntries(dto.types)) includes.add(EventDataIncludes.Types);
        includes.add(EventDataIncludes.Classifications);
        if (hasEntries(dto.parties)) includes.add(EventDataIncludes.Parties);
        //if (hasEntries(dto.resources)) includes.add(EventDataIncludes.Resources);
        includes.add(EventDataIncludes.Resources);
        if (hasEntries(dto.products)) includes.add(EventDataIncludes.Products);
        if (hasEntries(dto.rules)) includes.add(EventDataIncludes.Rules);
        if (hasEntries(dto.arrangements)) includes.add(EventDataIncludes.Arrangements);
        if (hasEntries(dto.children)) includes.add(EventDataIncludes.Children);
        if (includes.size() <= 1) {
            includes.add(EventDataIncludes.Types);
        }
        return includes;
    }

    private boolean hasEntries(RelationshipUpdateEntry entry) {
        if (entry == null) return false;
        return (entry.addOrUpdate != null && !entry.addOrUpdate.isEmpty())
                || (entry.delete != null && !entry.delete.isEmpty());
    }

    private boolean hasAnyRelationship(EventCreateDTO dto) {
        return (dto.classifications != null && !dto.classifications.isEmpty())
                || (dto.types != null && !dto.types.isEmpty())
                || (dto.parties != null && !dto.parties.isEmpty())
                || (dto.resources != null && !dto.resources.isEmpty())
                || (dto.products != null && !dto.products.isEmpty())
                || (dto.rules != null && !dto.rules.isEmpty())
                || (dto.arrangements != null && !dto.arrangements.isEmpty())
                || (dto.children != null && !dto.children.isEmpty());
    }
}


