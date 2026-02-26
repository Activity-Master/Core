package com.guicedee.activitymaster.fsdm.rest.event;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.EventsService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
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

    @Inject
    private IResourceItemService<?> resourceItemService;

    @Inject
    private IInvolvedPartyService<?> involvedPartyService;

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
        //         Use the first entry in dto.types as the primary event type
        Map.Entry<String, String> primaryType = dto.types.entrySet().iterator().next();
        return SessionUtils.<ISystems<?, ?>>withActivityMaster(enterpriseName, requestingSystemName,
                (java.util.function.Function<Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]>, Uni<ISystems<?, ?>>>) tuple ->
                        Uni.createFrom().item(tuple.getItem3())
        ).chain(system -> eventService.createEvent(null, primaryType.getKey(), system)
                .map(event -> {
                    UUID eventId = event.getId();
                    // Step 2: Fire-and-forget relationship persistence in parallel
                    if (hasAnyRelationship(dto)) {
                        persistCreateRelationshipsAsync(enterpriseName, requestingSystemName, eventId, dto);
                    }

                    // Step 3: Build response immediately from the DTO input — no DB round-trip needed
                    return buildCreateResponseFromDto((Event) event, dto);
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
        // Step 1: Find the event in its own session (just to validate it exists)
        return SessionUtils.<UUID>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            return eventService.find(session, eventId).map(event -> event.getId());
        }).map(foundId -> {
            // Step 2: Fire-and-forget relationship persistence in parallel
            persistUpdateRelationshipsAsync(enterpriseName, requestingSystemName, foundId, dto);

            // Step 3: Build response immediately from the DTO input — no DB round-trip needed
            return buildUpdateResponseFromDto(eventId, dto);
        }).onFailure().invoke(e ->
                log.error("Error updating event {} for enterprise {} and system {}: {}",
                        eventId, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
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
    // Fire-and-forget relationship persistence
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Subscribes to relationship persistence for a newly created event.
     * Each relationship category runs in its own session in parallel.
     * Failures are logged but do not affect the caller.
     */
    private void persistCreateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                   UUID eventId, EventCreateDTO dto) {
        String label = "event " + eventId;

        if (dto.classifications != null && !dto.classifications.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1();
                ISystems<?, ?> sys = tuple.getItem3();
                UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.classifications.entrySet()) {
                        chain = chain.chain(() -> event
                                .addOrUpdateClassification(s, entry.getKey(), entry.getValue(), sys, token)
                                .replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " classifications");
        }

        if (dto.types != null && !dto.types.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1();
                ISystems<?, ?> sys = tuple.getItem3();
                UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.types.entrySet()) {
                        chain = chain.chain(() -> event
                                .addOrUpdateEventTypes(s, entry.getKey(), null, null, entry.getValue(), sys, token)
                                .replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " types");
        }

        if (dto.parties != null && !dto.parties.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1();
                ISystems<?, ?> sys = tuple.getItem3();
                UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.parties.entrySet()) {
                        String classificationName = entry.getKey();
                        UUID partyId = parseUuidOrNull(entry.getValue(), label + " parties");
                        if (partyId == null) continue;
                        chain = chain.chain(() -> involvedPartyService.find(s, partyId)
                                .chain(party -> event
                                        .addOrUpdateInvolvedParty(s, classificationName, party, null, null, sys, token)
                                        .replaceWithVoid()));
                    }
                    return chain;
                });
            }), label + " parties");
        }

        if (dto.resources != null && !dto.resources.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1();
                ISystems<?, ?> sys = tuple.getItem3();
                UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.resources.entrySet()) {
                        String classificationName = entry.getKey();
                        UUID riId = parseUuidOrNull(entry.getValue(), label + " resources");
                        if (riId == null) continue;
                        chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                                .chain(ri -> event
                                        .addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token)
                                        .replaceWithVoid()));
                    }
                    return chain;
                });
            }), label + " resources");
        }

        if (dto.products != null && !dto.products.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1();
                ISystems<?, ?> sys = tuple.getItem3();
                UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.products.entrySet()) {
                        chain = chain.chain(() -> event
                                .addOrUpdateProduct(s, entry.getKey(), null, null, entry.getValue(), sys, token)
                                .replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " products");
        }

        if (dto.rules != null && !dto.rules.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1();
                ISystems<?, ?> sys = tuple.getItem3();
                UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.rules.entrySet()) {
                        chain = chain.chain(() -> event
                                .addOrUpdateRules(s, entry.getKey(), null, null, entry.getValue(), sys, token)
                                .replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " rules");
        }

        if (dto.arrangements != null && !dto.arrangements.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1();
                ISystems<?, ?> sys = tuple.getItem3();
                UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.arrangements.entrySet()) {
                        chain = chain.chain(() -> event
                                .addOrUpdateArrangement(s, entry.getKey(), null, null, entry.getValue(), sys, token)
                                .replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " arrangements");
        }

        if (dto.children != null && !dto.children.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1();
                ISystems<?, ?> sys = tuple.getItem3();
                UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.children.entrySet()) {
                        String classificationName = entry.getKey();
                        UUID childId = parseUuidOrNull(entry.getValue(), label + " children");
                        if (childId == null) continue;
                        chain = chain.chain(() ->
                                eventService.find(s, childId)
                                        .chain(child -> event
                                                .addChild(s, (Event) child, classificationName, null, sys, token)
                                                .replaceWithVoid()));
                    }
                    return chain;
                });
            }), label + " children");
        }
    }


    /**
     * Each relationship category runs in its own session in parallel.
     * Failures are logged but do not affect the caller.
     */
    private void persistUpdateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                  UUID eventId, EventUpdateDTO dto) {
        String label = "event " + eventId;

        if (hasEntries(dto.classifications)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.classifications, (name, value) ->
                            event.addOrUpdateClassification(s, name, value, sys, token).replaceWithVoid());
                    chain = chainDelete(chain, dto.classifications, name ->
                            event.removeClassification(s, name, null, sys, token).replaceWithVoid());
                    return chain;
                });
            }), label + " classifications");
        }

        if (hasEntries(dto.types)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.types, (name, value) ->
                            event.addOrUpdateEventTypes(s, name, null, null, value, sys, token).replaceWithVoid());
                    chain = chainDelete(chain, dto.types, name ->
                            event.removeEventTypes(s, name, null, null, null, sys, token).replaceWithVoid());
                    return chain;
                });
            }), label + " types");
        }

        if (hasEntries(dto.parties)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    if (dto.parties.addOrUpdate != null) {
                        for (var e : dto.parties.addOrUpdate.entrySet()) {
                            String classificationName = e.getKey();
                            UUID partyId = parseUuidOrNull(e.getValue(), label + " parties addOrUpdate");
                            if (partyId == null) continue;
                            chain = chain.chain(() -> involvedPartyService.find(s, partyId)
                                    .chain(party -> event.addOrUpdateInvolvedParty(s, classificationName, party, null, null, sys, token).replaceWithVoid()));
                        }
                    }
                    chain = chainDeleteByExpire(chain, dto.parties, s, event, EventXInvolvedParty.class,
                            EventXInvolvedParty_.eventID, link -> link.getClassificationID(), cls -> cls != null ? cls.getName() : null);
                    return chain;
                });
            }), label + " parties");
        }

        if (hasEntries(dto.resources)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    if (dto.resources.addOrUpdate != null) {
                        for (var e : dto.resources.addOrUpdate.entrySet()) {
                            String classificationName = e.getKey();
                            UUID riId = parseUuidOrNull(e.getValue(), label + " resources addOrUpdate");
                            if (riId == null) continue;
                            chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                                    .chain(ri -> event.addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token).replaceWithVoid()));
                        }
                    }
                    chain = chainDeleteByExpire(chain, dto.resources, s, event, EventXResourceItem.class,
                            EventXResourceItem_.eventID, link -> link.getClassificationID(), cls -> cls != null ? cls.getName() : null);
                    return chain;
                });
            }), label + " resources");
        }

        if (hasEntries(dto.products)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.products, (name, value) ->
                            event.addOrUpdateProduct(s, name, null, null, value, sys, token).replaceWithVoid());
                    chain = chainDeleteByExpire(chain, dto.products, s, event, EventXProduct.class,
                            EventXProduct_.eventID, link -> link.getClassificationID(), cls -> cls != null ? cls.getName() : null);
                    return chain;
                });
            }), label + " products");
        }

        if (hasEntries(dto.rules)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.rules, (name, value) ->
                            event.addOrUpdateRules(s, name, null, null, value, sys, token).replaceWithVoid());
                    chain = chainDeleteByExpire(chain, dto.rules, s, event, EventXRules.class,
                            EventXRules_.eventID, link -> link.getClassificationID(), cls -> cls != null ? cls.getName() : null);
                    return chain;
                });
            }), label + " rules");
        }

        if (hasEntries(dto.arrangements)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.arrangements, (name, value) ->
                            event.addOrUpdateArrangement(s, name, null, null, value, sys, token).replaceWithVoid());
                    chain = chainDeleteByExpire(chain, dto.arrangements, s, event, EventXArrangement.class,
                            EventXArrangement_.eventID, link -> link.getClassificationID(), cls -> cls != null ? cls.getName() : null);
                    return chain;
                });
            }), label + " arrangements");
        }

        if (hasEntries(dto.children)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return eventService.find(s, eventId).chain(event -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    if (dto.children.addOrUpdate != null) {
                        for (var e : dto.children.addOrUpdate.entrySet()) {
                            String classificationName = e.getKey();
                            UUID childId = parseUuidOrNull(e.getValue(), label + " children addOrUpdate");
                            if (childId == null) continue;
                            chain = chain.chain(() -> eventService.find(s, childId)
                                    .chain(child -> event.addChild(s, (Event) child, classificationName, null, sys, token).replaceWithVoid()));
                        }
                    }
                    chain = chainDeleteChildrenByExpire(chain, dto.children, s, event);
                    return chain;
                });
            }), label + " children");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO-based response builders (no DB round-trip)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Builds the response for a create directly from the input DTO.
     * The caller already knows what was submitted, so we echo it back immediately.
     */
    private EventDTO buildCreateResponseFromDto(Event event, EventCreateDTO dto) {
        EventDTO response = new EventDTO();
        response.eventId = event.getId();
        response.types = dto.types != null ? new LinkedHashMap<>(dto.types) : null;
        response.classifications = dto.classifications != null ? new LinkedHashMap<>(dto.classifications) : null;
        response.parties = dto.parties != null ? new LinkedHashMap<>(dto.parties) : null;
        response.resources = dto.resources != null ? new LinkedHashMap<>(dto.resources) : null;
        response.products = dto.products != null ? new LinkedHashMap<>(dto.products) : null;
        response.rules = dto.rules != null ? new LinkedHashMap<>(dto.rules) : null;
        response.arrangements = dto.arrangements != null ? new LinkedHashMap<>(dto.arrangements) : null;
        response.children = dto.children != null ? new LinkedHashMap<>(dto.children) : null;
        return response;
    }

    /**
     * Builds the response for an update directly from the input DTO.
     * Returns the addOrUpdate entries (the current intended state from the caller's perspective).
     * Deleted entries are omitted from the response.
     */
    private EventDTO buildUpdateResponseFromDto(UUID eventId, EventUpdateDTO dto) {
        EventDTO response = new EventDTO();
        response.eventId = eventId;
        if (dto.classifications != null && dto.classifications.addOrUpdate != null) response.classifications = new LinkedHashMap<>(dto.classifications.addOrUpdate);
        if (dto.types != null && dto.types.addOrUpdate != null) response.types = new LinkedHashMap<>(dto.types.addOrUpdate);
        if (dto.parties != null && dto.parties.addOrUpdate != null) response.parties = new LinkedHashMap<>(dto.parties.addOrUpdate);
        if (dto.resources != null && dto.resources.addOrUpdate != null) response.resources = new LinkedHashMap<>(dto.resources.addOrUpdate);
        if (dto.products != null && dto.products.addOrUpdate != null) response.products = new LinkedHashMap<>(dto.products.addOrUpdate);
        if (dto.rules != null && dto.rules.addOrUpdate != null) response.rules = new LinkedHashMap<>(dto.rules.addOrUpdate);
        if (dto.arrangements != null && dto.arrangements.addOrUpdate != null) response.arrangements = new LinkedHashMap<>(dto.arrangements.addOrUpdate);
        if (dto.children != null && dto.children.addOrUpdate != null) response.children = new LinkedHashMap<>(dto.children.addOrUpdate);
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Response builders (DB-based — retained for find)
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
                                                    return ((WarehouseBaseTable) link).expire(session).replaceWithVoid();
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
                                                        return ((WarehouseBaseTable) link).expire(session).replaceWithVoid();
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

    private UUID parseUuidOrNull(String value, String context) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            log.warn("Skipping invalid UUID '{}' in {}", value, context);
            return null;
        }
    }
}

