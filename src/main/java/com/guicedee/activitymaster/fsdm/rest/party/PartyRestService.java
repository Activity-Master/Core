package com.guicedee.activitymaster.fsdm.rest.party;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.rest.parties.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;

import com.guicedee.activitymaster.fsdm.client.services.rest.RelationshipUpdateEntry;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import jakarta.ws.rs.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.entityassist.enumerations.Operand.Equals;

@Path("{enterprise}/party")
@Log4j2
public class PartyRestService {

    @Inject
    private IInvolvedPartyService<InvolvedPartyService> involvedPartyService;

    @Inject
    private IClassificationService<?> classificationService;

    // ──────────────────────────────────────────────────────────────────────────
    // Find
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/find")
    public Uni<PartyDTO> find(@PathParam("enterprise") String enterpriseName,
                              @PathParam("requestingSystemName") String requestingSystemName,
                              PartyFindDTO findDto) {
        UUID partyId = findDto.partyId;
        List<PartyDataIncludes> includesList = findDto.includes;
        return SessionUtils.<PartyDTO>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    return involvedPartyService.find(session, partyId)
                            .chain(party -> {
                                PartyDTO dto = new PartyDTO();
                                dto.partyId = partyId;
                                InvolvedParty ip = (InvolvedParty) party;

                                Uni<PartyDTO> chain = Uni.createFrom().item(dto);
                                if (includesList == null || includesList.isEmpty()) {
                                    return chain;
                                }
                                for (PartyDataIncludes include : includesList) {
                                    chain = chain.chain(d -> fetchInclude(session, ip, d, include));
                                }
                                return chain;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error finding party {} for enterprise {} system {}: {}",
                                            partyId, enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Search by classification
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/search/classification")
    public Uni<List<PartyDTO>> searchByClassification(@PathParam("enterprise") String enterpriseName,
                                                       @PathParam("requestingSystemName") String requestingSystemName,
                                                       PartySearchByClassificationDTO searchDto) {
        return SessionUtils.<List<PartyDTO>>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] identityToken = tuple.getItem4();

                    return classificationService.find(session, searchDto.classificationName, system, identityToken)
                            .chain(classification -> {
                                var builder = new InvolvedPartyXClassification().builder(session)
                                        .withClassification(classification)
                                        .inActiveRange()
                                        .inDateRange();
                                if (searchDto.classificationValue != null) {
                                    builder.withValue(searchDto.classificationValue);
                                }
                                return builder.getAll();
                            })
                            .chain(results -> {
                                // Apply maxResults limit
                                List<InvolvedPartyXClassification> limited = (searchDto.maxResults != null && searchDto.maxResults > 0 && results.size() > searchDto.maxResults)
                                        ? results.subList(0, searchDto.maxResults)
                                        : results;

                                List<PartyDataIncludes> includesList = searchDto.includes;
                                Uni<List<PartyDTO>> listUni = Uni.createFrom().item(new ArrayList<>());

                                for (InvolvedPartyXClassification link : limited) {
                                    listUni = listUni.chain(dtoList ->
                                            session.fetch(link.getInvolvedPartyID())
                                                    .chain(fetchedParty -> {
                                                        InvolvedParty ip = (InvolvedParty) fetchedParty;
                                                        PartyDTO dto = new PartyDTO();
                                                        dto.partyId = ip.getId();

                                                        if (includesList == null || includesList.isEmpty()) {
                                                            dtoList.add(dto);
                                                            return Uni.createFrom().item(dtoList);
                                                        }

                                                        Uni<PartyDTO> fetchChain = Uni.createFrom().item(dto);
                                                        for (PartyDataIncludes include : includesList) {
                                                            fetchChain = fetchChain.chain(d -> fetchInclude(session, ip, d, include));
                                                        }
                                                        return fetchChain.map(d -> { dtoList.add(d); return dtoList; });
                                                    })
                                    );
                                }
                                return listUni;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error searching parties by classification for enterprise {} system {}: {}",
                                            enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Search by identification type
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/search/identification")
    public Uni<List<PartyDTO>> searchByIdentification(@PathParam("enterprise") String enterpriseName,
                                                       @PathParam("requestingSystemName") String requestingSystemName,
                                                       PartySearchByIdentificationDTO searchDto) {
        return SessionUtils.<List<PartyDTO>>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();

                    return involvedPartyService.findAllByIdentificationType(session,
                                    searchDto.identificationType, searchDto.identificationValue)
                            .chain(results -> {
                                // Apply maxResults limit
                                var limited = (searchDto.maxResults != null && searchDto.maxResults > 0 && results.size() > searchDto.maxResults)
                                        ? results.subList(0, searchDto.maxResults)
                                        : results;

                                List<PartyDataIncludes> includesList = searchDto.includes;
                                Uni<List<PartyDTO>> listUni = Uni.createFrom().item(new ArrayList<>());

                                for (var relValue : limited) {
                                    listUni = listUni.chain(dtoList -> {
                                        var party = relValue.getPrimary();
                                        return session.fetch(party)
                                                .chain(fetchedParty -> {
                                                    InvolvedParty ip = (InvolvedParty) fetchedParty;
                                                    PartyDTO dto = new PartyDTO();
                                                    dto.partyId = ip.getId();

                                                    if (includesList == null || includesList.isEmpty()) {
                                                        dtoList.add(dto);
                                                        return Uni.createFrom().item(dtoList);
                                                    }

                                                    Uni<PartyDTO> fetchChain = Uni.createFrom().item(dto);
                                                    for (PartyDataIncludes include : includesList) {
                                                        fetchChain = fetchChain.chain(d -> fetchInclude(session, ip, d, include));
                                                    }
                                                    return fetchChain.map(d -> { dtoList.add(d); return dtoList; });
                                                });
                                    });
                                }
                                return listUni;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error searching parties by identification type for enterprise {} system {}: {}",
                                            enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Create
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/create")
    public Uni<PartyDTO> create(@PathParam("enterprise") String enterpriseName,
                                @PathParam("requestingSystemName") String requestingSystemName,
                                PartyCreateDTO dto) {
        return SessionUtils.<ISystems<?, ?>>withActivityMaster(enterpriseName, requestingSystemName,
                (java.util.function.Function<Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]>, Uni<ISystems<?, ?>>>) tuple ->
                        Uni.createFrom().item(tuple.getItem3())
        ).chain(system -> involvedPartyService.create(null, system,
                        new Pair<>(dto.identificationType, dto.identificationValue), dto.organic)
                .map(party -> {
                    if (hasAnyRelationship(dto)) {
                        persistCreateRelationshipsAsync(enterpriseName, requestingSystemName, party, dto);
                    }

                    return buildCreateResponseFromDto((InvolvedParty) party, dto);
                })
                .onFailure().invoke(e ->
                        log.error("Error creating party for enterprise {} and system {}: {}",
                                enterpriseName, requestingSystemName, e.getMessage(), e)
                )
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update
    // ──────────────────────────────────────────────────────────────────────────

    @PUT
    @Path("{requestingSystemName}/update")
    public Uni<PartyDTO> update(@PathParam("enterprise") String enterpriseName,
                                @PathParam("requestingSystemName") String requestingSystemName,
                                PartyUpdateDTO dto) {
        UUID partyId = dto.partyId;
        // Step 1: Find the party in its own session
        return SessionUtils.<IInvolvedParty<?, ?>>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            return involvedPartyService.find(session, partyId);
        }).map(party -> {
            // Step 2: Fire-and-forget relationship persistence
            persistUpdateRelationshipsAsync(enterpriseName, requestingSystemName, party, dto);

            // Step 3: Build response immediately from the DTO input
            return buildUpdateResponseFromDto(partyId, dto);
        }).onFailure().invoke(e ->
                log.error("Error updating party {} for enterprise {} and system {}: {}",
                        partyId, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Include fetching — all use session.fetch() for lazy-loaded entities
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<PartyDTO> fetchInclude(Mutiny.Session session, InvolvedParty party, PartyDTO dto, PartyDataIncludes include) {
        return switch (include) {
            case Types -> new InvolvedPartyXInvolvedPartyType().builder(session)
                    .where(InvolvedPartyXInvolvedPartyType_.involvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXInvolvedPartyType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getInvolvedPartyTypeID())
                                            .invoke(type -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = type != null && type.getName() != null ? type.getName() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.types = map; return dto; });
                    });

            case Classifications -> new InvolvedPartyXClassification().builder(session)
                    .where(InvolvedPartyXClassification_.involvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXClassification link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .invoke(classification -> {
                                        String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.classifications = map; return dto; });
                    });

            case NameTypes -> new InvolvedPartyXInvolvedPartyNameType().builder(session)
                    .where(InvolvedPartyXInvolvedPartyNameType_.involvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXInvolvedPartyNameType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getInvolvedPartyNameTypeID())
                                            .invoke(nameType -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = nameType != null && nameType.getName() != null ? nameType.getName() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.nameTypes = map; return dto; });
                    });

            case IdentificationTypes -> new InvolvedPartyXInvolvedPartyIdentificationType().builder(session)
                    .where(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXInvolvedPartyIdentificationType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getInvolvedPartyIdentificationTypeID())
                                            .invoke(idType -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = idType != null && idType.getName() != null ? idType.getName() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.identificationTypes = map; return dto; });
                    });

            case Resources -> new InvolvedPartyXResourceItem().builder(session)
                    .where(InvolvedPartyXResourceItem_.involvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXResourceItem link : list) {
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

            case Products -> new InvolvedPartyXProduct().builder(session)
                    .where(InvolvedPartyXProduct_.involvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXProduct link : list) {
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

            case Rules -> new InvolvedPartyXRules().builder(session)
                    .where(InvolvedPartyXRules_.involvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXRules link : list) {
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

            case Addresses -> new InvolvedPartyXAddress().builder(session)
                    .where(InvolvedPartyXAddress_.involvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXAddress link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getAddressID())
                                            .invoke(address -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = address != null && address.getId() != null ? address.getId().toString() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.addresses = map; return dto; });
                    });

            case Children -> new InvolvedPartyXInvolvedParty().builder(session)
                    .where(InvolvedPartyXInvolvedParty_.parentInvolvedPartyID, Equals, party)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (InvolvedPartyXInvolvedParty link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getChildInvolvedPartyID())
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

    private void persistCreateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                  IInvolvedParty<?, ?> party, PartyCreateDTO dto) {
        String label = "party " + party.getId();

        if (dto.classifications != null && !dto.classifications.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var entry : dto.classifications.entrySet()) {
                    chain = chain.chain(() -> party.addOrUpdateClassification(s, entry.getKey(), entry.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            }), label + " classifications");
        }
        if (dto.types != null && !dto.types.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var entry : dto.types.entrySet()) {
                    chain = chain.chain(() -> party.addOrUpdateInvolvedPartyType(s, entry.getKey(), (String) null, null, entry.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            }), label + " types");
        }
        if (dto.nameTypes != null && !dto.nameTypes.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var entry : dto.nameTypes.entrySet()) {
                    chain = chain.chain(() -> party.addOrUpdateInvolvedPartyNameType(s, entry.getKey(), (String) null, null, entry.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            }), label + " nameTypes");
        }
        if (dto.resources != null && !dto.resources.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var entry : dto.resources.entrySet()) {
                    chain = chain.chain(() -> party.addOrUpdateResourceItem(s, entry.getKey(), null, null, entry.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            }), label + " resources");
        }
        if (dto.products != null && !dto.products.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var entry : dto.products.entrySet()) {
                    chain = chain.chain(() -> party.addOrUpdateProduct(s, entry.getKey(), null, null, entry.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            }), label + " products");
        }
        if (dto.rules != null && !dto.rules.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var entry : dto.rules.entrySet()) {
                    chain = chain.chain(() -> party.addOrUpdateRules(s, entry.getKey(), null, null, entry.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            }), label + " rules");
        }
        if (dto.children != null && !dto.children.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var entry : dto.children.entrySet()) {
                    UUID childId = UUID.fromString(entry.getKey());
                    chain = chain.chain(() -> involvedPartyService.find(s, childId)
                            .chain(child -> party.addChild(s, (InvolvedParty) child, null, entry.getValue(), sys, token).replaceWithVoid()));
                }
                return chain;
            }), label + " children");
        }
    }

    private void persistUpdateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                  IInvolvedParty<?, ?> party, PartyUpdateDTO dto) {
        String label = "party " + party.getId();

        if (hasEntries(dto.classifications)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.classifications, (name, value) -> party.addOrUpdateClassification(s, name, value, sys, token).replaceWithVoid());
                chain = chainDelete(chain, dto.classifications, name -> party.removeClassification(s, name, null, sys, token).replaceWithVoid());
                return chain;
            }), label + " classifications");
        }
        if (hasEntries(dto.types)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.types, (name, value) -> party.addOrUpdateInvolvedPartyType(s, name, (String) null, null, value, sys, token).replaceWithVoid());
                chain = chainDeleteByExpire(chain, dto.types, s, party, InvolvedPartyXInvolvedPartyType.class,
                        InvolvedPartyXInvolvedPartyType_.involvedPartyID, InvolvedPartyXInvolvedPartyType::getInvolvedPartyTypeID,
                        type -> type != null && type.getName() != null ? type.getName() : null);
                return chain;
            }), label + " types");
        }
        if (hasEntries(dto.nameTypes)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.nameTypes, (name, value) -> party.addOrUpdateInvolvedPartyNameType(s, name, (String) null, null, value, sys, token).replaceWithVoid());
                chain = chainDeleteByExpire(chain, dto.nameTypes, s, party, InvolvedPartyXInvolvedPartyNameType.class,
                        InvolvedPartyXInvolvedPartyNameType_.involvedPartyID, InvolvedPartyXInvolvedPartyNameType::getInvolvedPartyNameTypeID,
                        nameType -> nameType != null && nameType.getName() != null ? nameType.getName() : null);
                return chain;
            }), label + " nameTypes");
        }
        if (hasEntries(dto.identificationTypes)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.identificationTypes, (name, value) -> party.addOrUpdateInvolvedPartyIdentificationType(s, name, (String) null, null, value, sys, token).replaceWithVoid());
                chain = chainDeleteByExpire(chain, dto.identificationTypes, s, party, InvolvedPartyXInvolvedPartyIdentificationType.class,
                        InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID, InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyIdentificationTypeID,
                        idType -> idType != null && idType.getName() != null ? idType.getName() : null);
                return chain;
            }), label + " identificationTypes");
        }
        if (hasEntries(dto.resources)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.resources, (name, value) -> party.addOrUpdateResourceItem(s, name, null, null, value, sys, token).replaceWithVoid());
                chain = chainDeleteByExpire(chain, dto.resources, s, party, InvolvedPartyXResourceItem.class,
                        InvolvedPartyXResourceItem_.involvedPartyID, InvolvedPartyXResourceItem::getClassificationID,
                        cls -> cls != null && cls.getName() != null ? cls.getName() : null);
                return chain;
            }), label + " resources");
        }
        if (hasEntries(dto.products)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.products, (name, value) -> party.addOrUpdateProduct(s, name, null, null, value, sys, token).replaceWithVoid());
                chain = chainDeleteByExpire(chain, dto.products, s, party, InvolvedPartyXProduct.class,
                        InvolvedPartyXProduct_.involvedPartyID, InvolvedPartyXProduct::getClassificationID,
                        cls -> cls != null && cls.getName() != null ? cls.getName() : null);
                return chain;
            }), label + " products");
        }
        if (hasEntries(dto.rules)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.rules, (name, value) -> party.addOrUpdateRules(s, name, null, null, value, sys, token).replaceWithVoid());
                chain = chainDeleteByExpire(chain, dto.rules, s, party, InvolvedPartyXRules.class,
                        InvolvedPartyXRules_.involvedPartyID, InvolvedPartyXRules::getClassificationID,
                        cls -> cls != null && cls.getName() != null ? cls.getName() : null);
                return chain;
            }), label + " rules");
        }
        if (hasEntries(dto.children)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.children, (childIdStr, value) -> {
                    UUID childId = UUID.fromString(childIdStr);
                    return involvedPartyService.find(s, childId)
                            .chain(child -> party.addChild(s, (InvolvedParty) child, null, value, sys, token).replaceWithVoid());
                });
                chain = chainDeleteChildrenByExpire(chain, dto.children, s, party);
                return chain;
            }), label + " children");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO-based response builders (no DB round-trip)
    // ──────────────────────────────────────────────────────────────────────────

    private PartyDTO buildCreateResponseFromDto(InvolvedParty party, PartyCreateDTO dto) {
        PartyDTO response = new PartyDTO();
        response.partyId = party.getId();
        response.classifications = dto.classifications != null ? new LinkedHashMap<>(dto.classifications) : null;
        response.types = dto.types != null ? new LinkedHashMap<>(dto.types) : null;
        response.nameTypes = dto.nameTypes != null ? new LinkedHashMap<>(dto.nameTypes) : null;
        response.resources = dto.resources != null ? new LinkedHashMap<>(dto.resources) : null;
        response.products = dto.products != null ? new LinkedHashMap<>(dto.products) : null;
        response.rules = dto.rules != null ? new LinkedHashMap<>(dto.rules) : null;
        response.children = dto.children != null ? new LinkedHashMap<>(dto.children) : null;
        return response;
    }

    private PartyDTO buildUpdateResponseFromDto(UUID partyId, PartyUpdateDTO dto) {
        PartyDTO response = new PartyDTO();
        response.partyId = partyId;
        if (dto.classifications != null && dto.classifications.addOrUpdate != null) response.classifications = new LinkedHashMap<>(dto.classifications.addOrUpdate);
        if (dto.types != null && dto.types.addOrUpdate != null) response.types = new LinkedHashMap<>(dto.types.addOrUpdate);
        if (dto.nameTypes != null && dto.nameTypes.addOrUpdate != null) response.nameTypes = new LinkedHashMap<>(dto.nameTypes.addOrUpdate);
        if (dto.identificationTypes != null && dto.identificationTypes.addOrUpdate != null) response.identificationTypes = new LinkedHashMap<>(dto.identificationTypes.addOrUpdate);
        if (dto.resources != null && dto.resources.addOrUpdate != null) response.resources = new LinkedHashMap<>(dto.resources.addOrUpdate);
        if (dto.products != null && dto.products.addOrUpdate != null) response.products = new LinkedHashMap<>(dto.products.addOrUpdate);
        if (dto.rules != null && dto.rules.addOrUpdate != null) response.rules = new LinkedHashMap<>(dto.rules.addOrUpdate);
        if (dto.children != null && dto.children.addOrUpdate != null) response.children = new LinkedHashMap<>(dto.children.addOrUpdate);
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Response builders (DB-based — retained for find)
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<PartyDTO> buildCreateResponse(String enterpriseName, String requestingSystemName,
                                              InvolvedParty party, PartyCreateDTO dto) {
        return SessionUtils.<PartyDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();

            PartyDTO response = new PartyDTO();
            response.partyId = party.getId();

            List<PartyDataIncludes> includes = new ArrayList<>();
            includes.add(PartyDataIncludes.Classifications);
            includes.add(PartyDataIncludes.IdentificationTypes);
            if (dto.types != null && !dto.types.isEmpty()) includes.add(PartyDataIncludes.Types);
            if (dto.nameTypes != null && !dto.nameTypes.isEmpty()) includes.add(PartyDataIncludes.NameTypes);
            if (dto.resources != null && !dto.resources.isEmpty()) includes.add(PartyDataIncludes.Resources);
            if (dto.products != null && !dto.products.isEmpty()) includes.add(PartyDataIncludes.Products);
            if (dto.rules != null && !dto.rules.isEmpty()) includes.add(PartyDataIncludes.Rules);
            if (dto.children != null && !dto.children.isEmpty()) includes.add(PartyDataIncludes.Children);

            Uni<PartyDTO> fetchChain = Uni.createFrom().item(response);
            for (PartyDataIncludes include : includes) {
                fetchChain = fetchChain.chain(d -> fetchInclude(session, party, d, include));
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
            IInvolvedParty<?, ?> party,
            Class<L> linkClass,
            jakarta.persistence.metamodel.SingularAttribute partyAttr,
            java.util.function.Function<L, R> relatedGetter,
            java.util.function.Function<R, String> nameExtractor) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;

        Set<String> namesToDelete = new HashSet<>(entry.delete);

        return chain.chain(() -> {
            try {
                L instance = linkClass.getDeclaredConstructor().newInstance();
                return ((com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD) instance.builder(session)
                        .where(partyAttr, Equals, party))
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
                                                   Mutiny.Session session, IInvolvedParty<?, ?> party) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;
        Set<String> idsToDelete = new HashSet<>(entry.delete);
        InvolvedParty ip = (InvolvedParty) party;

        return chain.chain(() ->
                new InvolvedPartyXInvolvedParty().builder(session)
                        .where(InvolvedPartyXInvolvedParty_.parentInvolvedPartyID, Equals, ip)
                        .inActiveRange()
                        .inDateRange()
                        .getAll()
                        .chain(list -> {
                            Uni<Void> expireChain = Uni.createFrom().voidItem();
                            for (InvolvedPartyXInvolvedParty link : (List<InvolvedPartyXInvolvedParty>) list) {
                                expireChain = expireChain.chain(() ->
                                        session.fetch(link.getChildInvolvedPartyID())
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

    private List<PartyDataIncludes> determineIncludes(PartyUpdateDTO dto) {
        List<PartyDataIncludes> includes = new ArrayList<>();
        if (hasEntries(dto.types)) includes.add(PartyDataIncludes.Types);
        includes.add(PartyDataIncludes.Classifications);
        if (hasEntries(dto.nameTypes)) includes.add(PartyDataIncludes.NameTypes);
        if (hasEntries(dto.identificationTypes)) includes.add(PartyDataIncludes.IdentificationTypes);
        includes.add(PartyDataIncludes.Resources);
        if (hasEntries(dto.products)) includes.add(PartyDataIncludes.Products);
        if (hasEntries(dto.rules)) includes.add(PartyDataIncludes.Rules);
        if (hasEntries(dto.children)) includes.add(PartyDataIncludes.Children);
        if (includes.size() <= 2) {
            includes.add(PartyDataIncludes.Types);
            includes.add(PartyDataIncludes.IdentificationTypes);
        }
        return includes;
    }

    private boolean hasEntries(RelationshipUpdateEntry entry) {
        if (entry == null) return false;
        return (entry.addOrUpdate != null && !entry.addOrUpdate.isEmpty())
                || (entry.delete != null && !entry.delete.isEmpty());
    }

    private boolean hasAnyRelationship(PartyCreateDTO dto) {
        return (dto.classifications != null && !dto.classifications.isEmpty())
                || (dto.types != null && !dto.types.isEmpty())
                || (dto.nameTypes != null && !dto.nameTypes.isEmpty())
                || (dto.resources != null && !dto.resources.isEmpty())
                || (dto.products != null && !dto.products.isEmpty())
                || (dto.rules != null && !dto.rules.isEmpty())
                || (dto.children != null && !dto.children.isEmpty());
    }
}





