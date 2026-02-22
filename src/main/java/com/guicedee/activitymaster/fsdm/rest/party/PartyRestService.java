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
                .chain(party -> {
                    boolean hasRelationships = hasAnyRelationship(dto);

                    if (!hasRelationships) {
                        return buildCreateResponse(enterpriseName, requestingSystemName, (InvolvedParty) party, dto);
                    }

                    // Step 2: Add relationships in a fresh session
                    return SessionUtils.<Void>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                                Mutiny.Session session = tuple.getItem1();
                                ISystems<?, ?> sys = tuple.getItem3();
                                UUID[] identityToken = tuple.getItem4();

                                Uni<Void> chain = Uni.createFrom().voidItem();

                                if (dto.classifications != null && !dto.classifications.isEmpty()) {
                                    for (var entry : dto.classifications.entrySet()) {
                                        chain = chain.chain(() -> party
                                                .addOrUpdateClassification(session, entry.getKey(), entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.types != null && !dto.types.isEmpty()) {
                                    for (var entry : dto.types.entrySet()) {
                                        chain = chain.chain(() -> party
                                                .addOrUpdateInvolvedPartyType(session, entry.getKey(), (String) null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.nameTypes != null && !dto.nameTypes.isEmpty()) {
                                    for (var entry : dto.nameTypes.entrySet()) {
                                        chain = chain.chain(() -> party
                                                .addOrUpdateInvolvedPartyNameType(session, entry.getKey(), (String) null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.resources != null && !dto.resources.isEmpty()) {
                                    for (var entry : dto.resources.entrySet()) {
                                        chain = chain.chain(() -> party
                                                .addOrUpdateResourceItem(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.products != null && !dto.products.isEmpty()) {
                                    for (var entry : dto.products.entrySet()) {
                                        chain = chain.chain(() -> party
                                                .addOrUpdateProduct(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.rules != null && !dto.rules.isEmpty()) {
                                    for (var entry : dto.rules.entrySet()) {
                                        chain = chain.chain(() -> party
                                                .addOrUpdateRules(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.children != null && !dto.children.isEmpty()) {
                                    for (var entry : dto.children.entrySet()) {
                                        UUID childId = UUID.fromString(entry.getKey());
                                        chain = chain.chain(() ->
                                                involvedPartyService.find(session, childId)
                                                        .chain(child -> party
                                                                .addChild(session, (InvolvedParty) child, null, entry.getValue(), sys, identityToken)
                                                                .replaceWithVoid()));
                                    }
                                }

                                return chain;
                            })
                            // Step 3: Build response in a fresh session
                            .chain(() -> buildCreateResponse(enterpriseName, requestingSystemName, (InvolvedParty) party, dto));
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
        return SessionUtils.<PartyDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] identityToken = tuple.getItem4();

            return involvedPartyService.find(session, partyId)
                    .chain(party -> {
                        Uni<Void> chain = Uni.createFrom().voidItem();

                        // ── Classifications ──
                        chain = chainAddOrUpdate(chain, dto.classifications, (name, value) ->
                                party.addOrUpdateClassification(session, name, value, system, identityToken).replaceWithVoid());
                        chain = chainDelete(chain, dto.classifications, name ->
                                party.removeClassification(session, name, null, system, identityToken).replaceWithVoid());

                        // ── Involved Party Types ──
                        chain = chainAddOrUpdate(chain, dto.types, (name, value) ->
                                party.addOrUpdateInvolvedPartyType(session, name, (String) null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.types, session, party, InvolvedPartyXInvolvedPartyType.class,
                                InvolvedPartyXInvolvedPartyType_.involvedPartyID, InvolvedPartyXInvolvedPartyType::getInvolvedPartyTypeID,
                                type -> type != null && type.getName() != null ? type.getName() : null);

                        // ── Name Types ──
                        chain = chainAddOrUpdate(chain, dto.nameTypes, (name, value) ->
                                party.addOrUpdateInvolvedPartyNameType(session, name, (String) null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.nameTypes, session, party, InvolvedPartyXInvolvedPartyNameType.class,
                                InvolvedPartyXInvolvedPartyNameType_.involvedPartyID, InvolvedPartyXInvolvedPartyNameType::getInvolvedPartyNameTypeID,
                                nameType -> nameType != null && nameType.getName() != null ? nameType.getName() : null);

                        // ── Identification Types ──
                        chain = chainAddOrUpdate(chain, dto.identificationTypes, (name, value) ->
                                party.addOrUpdateInvolvedPartyIdentificationType(session, name, (String) null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.identificationTypes, session, party, InvolvedPartyXInvolvedPartyIdentificationType.class,
                                InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyID, InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyIdentificationTypeID,
                                idType -> idType != null && idType.getName() != null ? idType.getName() : null);

                        // ── Resource Items ──
                        chain = chainAddOrUpdate(chain, dto.resources, (name, value) ->
                                party.addOrUpdateResourceItem(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.resources, session, party, InvolvedPartyXResourceItem.class,
                                InvolvedPartyXResourceItem_.involvedPartyID, InvolvedPartyXResourceItem::getClassificationID,
                                cls -> cls != null && cls.getName() != null ? cls.getName() : null);

                        // ── Products ──
                        chain = chainAddOrUpdate(chain, dto.products, (name, value) ->
                                party.addOrUpdateProduct(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.products, session, party, InvolvedPartyXProduct.class,
                                InvolvedPartyXProduct_.involvedPartyID, InvolvedPartyXProduct::getClassificationID,
                                cls -> cls != null && cls.getName() != null ? cls.getName() : null);

                        // ── Rules ──
                        chain = chainAddOrUpdate(chain, dto.rules, (name, value) ->
                                party.addOrUpdateRules(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.rules, session, party, InvolvedPartyXRules.class,
                                InvolvedPartyXRules_.involvedPartyID, InvolvedPartyXRules::getClassificationID,
                                cls -> cls != null && cls.getName() != null ? cls.getName() : null);

                        // ── Children ──
                        chain = chainAddOrUpdate(chain, dto.children, (childIdStr, value) -> {
                            UUID childId = UUID.fromString(childIdStr);
                            return involvedPartyService.find(session, childId)
                                    .chain(child -> party.addChild(session, (InvolvedParty) child, null, value, system, identityToken)
                                            .replaceWithVoid());
                        });
                        chain = chainDeleteChildrenByExpire(chain, dto.children, session, party);

                        // ── Build response ──
                        return chain.chain(() -> {
                            PartyDTO response = new PartyDTO();
                            response.partyId = partyId;
                            InvolvedParty ip = (InvolvedParty) party;

                            List<PartyDataIncludes> includes = determineIncludes(dto);

                            Uni<PartyDTO> fetchChain = Uni.createFrom().item(response);
                            for (PartyDataIncludes include : includes) {
                                fetchChain = fetchChain.chain(d -> fetchInclude(session, ip, d, include));
                            }
                            return fetchChain;
                        });
                    })
                    .onFailure().invoke(e ->
                            log.error("Error updating party {} for enterprise {} and system {}: {}",
                                    partyId, enterpriseName, requestingSystemName, e.getMessage(), e)
                    );
        });
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
    // Response builders
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





