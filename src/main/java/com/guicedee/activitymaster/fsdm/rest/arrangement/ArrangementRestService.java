package com.guicedee.activitymaster.fsdm.rest.arrangement;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ArrangementsService;
import com.guicedee.activitymaster.fsdm.client.services.IArrangementsService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.rest.arrangements.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXArrangement;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXArrangement_;

import com.guicedee.activitymaster.fsdm.client.services.rest.EntityRef;
import com.guicedee.activitymaster.fsdm.client.services.rest.PivotEntry;
import com.guicedee.activitymaster.fsdm.client.services.rest.RelationshipUpdateEntry;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import jakarta.ws.rs.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.entityassist.enumerations.Operand.Equals;

@Path("{enterprise}/arrangement")
@Log4j2
public class ArrangementRestService {


    @Inject
    private IArrangementsService<ArrangementsService> arrangementsService;

    @POST
    @Path("{requestingSystemName}/find")
    public Uni<ArrangementDTO> find(@PathParam("enterprise") String enterpriseName,
                                    @PathParam("requestingSystemName") String requestingSystemName,
                                    ArrangementFindDTO findDto) {
        UUID arrangementId = findDto.arrangementId;
        List<ArrangementDataIncludes> includesList = findDto.includes;
        return SessionUtils.<ArrangementDTO>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    return arrangementsService.find(session, arrangementId, tuple.getItem3(), tuple.getItem4())
                            .chain(arrangement -> {
                                ArrangementDTO dto = new ArrangementDTO();
                                dto.arrangementId = arrangementId;
                                Arrangement arr = (Arrangement) arrangement;

                                // Build the chain of queries sequentially based on requested includes
                                Uni<ArrangementDTO> chain = Uni.createFrom().item(dto);

                                if (includesList == null || includesList.isEmpty()) {
                                    return chain;
                                }

                                for (ArrangementDataIncludes include : includesList) {
                                    chain = chain.chain(d -> fetchInclude(session, arr, d, include));
                                }

                                return chain;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error finding arrangement with ID {} for enterprise {} and system {}", arrangementId, enterpriseName, requestingSystemName, e)
                            );
                }
        );
    }

    /**
     * Fetches a single include for the given arrangement and populates the DTO.
     * Each include queries its respective relationship table, pivoting the results into a Map.
     */
    private Uni<ArrangementDTO> fetchInclude(Mutiny.Session session, Arrangement arrangement, ArrangementDTO dto, ArrangementDataIncludes include) {
        return switch (include) {
            case Types -> new ArrangementXArrangementType().builder(session)
                    .where(ArrangementXArrangementType_.arrangement, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ArrangementXArrangementType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getType())
                                    .invoke(type -> {
                                        String key = type != null && type.getName() != null ? type.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.types = map; return dto; });
                    });

            case Classifications -> new ArrangementXClassification().builder(session)
                    .where(ArrangementXClassification_.arrangementID, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ArrangementXClassification link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .invoke(classification -> {
                                        String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.classifications = map; return dto; });
                    });

            case Parties -> new ArrangementXInvolvedParty().builder(session)
                    .where(ArrangementXInvolvedParty_.arrangementID, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ArrangementXInvolvedParty link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getInvolvedPartyID())
                                    .invoke(ip -> {
                                        String key = ip != null && ip.getId() != null ? ip.getId().toString() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.parties = map; return dto; });
                    });

            case Resources -> new ArrangementXResourceItem().builder(session)
                    .where(ArrangementXResourceItem_.arrangementID, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ArrangementXResourceItem link : list) {
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

            case Events -> new EventXArrangement().builder(session)
                    .where(EventXArrangement_.arrangementID, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (EventXArrangement link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getEventID())
                                    .invoke(event -> {
                                        String key = event != null && event.getId() != null ? event.getId().toString() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.events = map; return dto; });
                    });

            case Rules -> new ArrangementXRules().builder(session)
                    .where(ArrangementXRules_.arrangement, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ArrangementXRules link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getRulesID())
                                    .invoke(rules -> {
                                        String key = rules != null && rules.getId() != null ? rules.getId().toString() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.rules = map; return dto; });
                    });

            case Products -> new ArrangementXProduct().builder(session)
                    .where(ArrangementXProduct_.arrangementID, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ArrangementXProduct link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getProductID())
                                    .invoke(product -> {
                                        String key = product != null && product.getId() != null ? product.getId().toString() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.products = map; return dto; });
                    });

            case RuleTypes -> new ArrangementXRulesType().builder(session)
                    .where(ArrangementXRulesType_.arrangement, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ArrangementXRulesType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getRulesTypeID())
                                    .invoke(rulesType -> {
                                        String key = rulesType != null && rulesType.getId() != null ? rulesType.getId().toString() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.ruleTypes = map; return dto; });
                    });

            case Arrangements -> new ArrangementXArrangement().builder(session)
                    .where(ArrangementXArrangement_.parentArrangementID, Equals, arrangement)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ArrangementXArrangement link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getChildArrangementID())
                                    .invoke(child -> {
                                        String key = child != null && child.getId() != null ? child.getId().toString() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.arrangements = map; return dto; });
                    });
        };
    }


    /**
     * Fetches arrangement data using a single optimized native SQL query.
     * Each requested classification and arrangement type is returned as a rich object
     * containing the entity reference (id + name), the stored relationship value,
     * and the relationship row's warehouseCreatedTimestamp.
     *
     * <p>The SQL uses CTEs to resolve entity IDs by name, then joins the relationship
     * tables to return one row per matched relationship. Results are tagged with a
     * source discriminator ('classification' or 'type') via UNION ALL so a single
     * database round-trip fetches everything.
     *
     * <p>Example response:
     * <pre>{@code
     * {
     *   "arrangementId": "...",
     *   "classifications": [
     *     { "classification": { "id": "...", "name": "Status" }, "value": "Active", "timestamp": "2025-..." }
     *   ],
     *   "types": [
     *     { "type": { "id": "...", "name": "Order" }, "value": "PO-2025", "timestamp": "2025-..." }
     *   ]
     * }
     * }</pre>
     */
    @POST
    @Path("{requestingSystemName}/pivot")
    public Uni<ArrangementPivotResponse> findPivoted(@PathParam("enterprise") String enterpriseName,
                                                     @PathParam("requestingSystemName") String requestingSystemName,
                                                     ArrangementPivotRequest request) {
        UUID arrangementId = request.arrangementId;
        return SessionUtils.<ArrangementPivotResponse>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();

                    List<String> classificationNames = request.classifications != null ? request.classifications : Collections.emptyList();
                    List<String> typeNames = request.types != null ? request.types : Collections.emptyList();

                    ArrangementPivotResponse response = new ArrangementPivotResponse();
                    response.arrangementId = arrangementId;

                    if (classificationNames.isEmpty() && typeNames.isEmpty()) {
                        return Uni.createFrom().item(response);
                    }

                    // ── Build a UNION ALL query returning one row per matched relationship ──
                    // Columns: source ('classification'|'type'), entity_id, entity_name, value, timestamp
                    Map<String, Object> params = new LinkedHashMap<>();
                    params.put("arrId", arrangementId);
                    List<String> unionParts = new ArrayList<>();

                    if (!classificationNames.isEmpty()) {
                        for (int i = 0; i < classificationNames.size(); i++) {
                            params.put("cls_" + i, classificationNames.get(i));
                        }
                        StringBuilder clsSql = new StringBuilder();
                        clsSql.append("""
                                SELECT 'classification' AS source,
                                       c.classificationid::text AS entity_id,
                                       c.classificationdesc AS entity_name,
                                       xc.value,
                                       xc.warehousecreatedtimestamp::text AS ts
                                FROM arrangement.arrangementxclassification xc
                                JOIN classification.classification c ON c.classificationid = xc.classificationid
                                WHERE xc.arrangementid = :arrId
                                  AND xc.effectivefromdate <= current_timestamp
                                  AND xc.effectivetodate > current_timestamp
                                  AND c.classificationdesc IN (""");
                        for (int i = 0; i < classificationNames.size(); i++) {
                            if (i > 0) clsSql.append(", ");
                            clsSql.append(":cls_").append(i);
                        }
                        clsSql.append(")");
                        unionParts.add(clsSql.toString());
                    }

                    if (!typeNames.isEmpty()) {
                        for (int i = 0; i < typeNames.size(); i++) {
                            params.put("typ_" + i, typeNames.get(i));
                        }
                        StringBuilder typSql = new StringBuilder();
                        typSql.append("""
                                SELECT 'type' AS source,
                                       at.arrangementtypeid::text AS entity_id,
                                       at.arrangementtypename AS entity_name,
                                       xat.value,
                                       xat.warehousecreatedtimestamp::text AS ts
                                FROM arrangement.arrangementxarrangementtype xat
                                JOIN arrangement.arrangementtype at ON at.arrangementtypeid = xat.arrangementtypeid
                                WHERE xat.arrangementid = :arrId
                                  AND xat.effectivefromdate <= current_timestamp
                                  AND xat.effectivetodate > current_timestamp
                                  AND at.arrangementtypename IN (""");
                        for (int i = 0; i < typeNames.size(); i++) {
                            if (i > 0) typSql.append(", ");
                            typSql.append(":typ_").append(i);
                        }
                        typSql.append(")");
                        unionParts.add(typSql.toString());
                    }

                    String nativeSql = String.join("\nUNION ALL\n", unionParts);
                    log.debug("Pivot SQL: {}", nativeSql);

                    var nativeQuery = session.createNativeQuery(nativeSql);
                    for (var entry : params.entrySet()) {
                        nativeQuery.setParameter(entry.getKey(), entry.getValue());
                    }

                    return nativeQuery.getResultList()
                            .map(rows -> {
                                List<PivotEntry> clsList = new ArrayList<>();
                                List<PivotEntry> typList = new ArrayList<>();

                                for (Object rowObj : rows) {
                                    Object[] cols = (Object[]) rowObj;
                                    String source = cols[0] != null ? cols[0].toString() : "";
                                    String entityId = cols[1] != null ? cols[1].toString() : null;
                                    String entityName = cols[2] != null ? cols[2].toString() : null;
                                    String value = cols[3] != null ? cols[3].toString() : null;
                                    String timestamp = cols[4] != null ? cols[4].toString() : null;

                                    PivotEntry entry = new PivotEntry();
                                    entry.value = value;
                                    entry.timestamp = timestamp;

                                    switch (source) {
                                        case "classification" -> {
                                            entry.entity = new EntityRef(entityId, entityName);
                                            clsList.add(entry);
                                        }
                                        case "type" -> {
                                            entry.entity = new EntityRef(entityId, entityName);
                                            typList.add(entry);
                                        }
                                    }
                                }

                                if (!clsList.isEmpty()) response.classifications = clsList;
                                if (!typList.isEmpty()) response.types = typList;
                                return response;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error in pivot query for arrangement {} in enterprise {} system {}: {}",
                                            arrangementId, enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }


    @POST
    @Path("{requestingSystemName}/create")
    public Uni<ArrangementDTO> create(@PathParam("enterprise") String enterpriseName,
                                      @PathParam("requestingSystemName") String requestingSystemName,
                                      ArrangementCreateDTO dto) {
        // Step 1: Create the arrangement — ArrangementsService.create() manages its own session+transaction
        return SessionUtils.<ISystems<?, ?>>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            return Uni.createFrom().item(tuple.getItem3());
        }).chain(system -> arrangementsService.create(null, dto.type, dto.classification, dto.typeValue, system)
                .chain(arrangement -> {
                    boolean hasRelationships = (dto.classifications != null && !dto.classifications.isEmpty())
                            || (dto.parties != null && !dto.parties.isEmpty())
                            || (dto.resources != null && !dto.resources.isEmpty())
                            || (dto.rules != null && !dto.rules.isEmpty())
                            || (dto.products != null && !dto.products.isEmpty())
                            || (dto.childArrangements != null && !dto.childArrangements.isEmpty());

                    if (!hasRelationships) {
                        // No relationships to add — just build the response DTO
                        return buildCreateResponse(enterpriseName, requestingSystemName, (Arrangement) arrangement, dto);
                    }

                    // Step 2: Add relationships in a fresh session
                    return SessionUtils.<Void>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                                Mutiny.Session session = tuple.getItem1();
                                ISystems<?, ?> sys = tuple.getItem3();
                                UUID[] identityToken = tuple.getItem4();

                                Uni<Void> chain = Uni.createFrom().voidItem();

                                if (dto.classifications != null && !dto.classifications.isEmpty()) {
                                    for (var entry : dto.classifications.entrySet()) {
                                        chain = chain.chain(() -> arrangement
                                                .addOrUpdateClassification(session, entry.getKey(), entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.parties != null && !dto.parties.isEmpty()) {
                                    for (var entry : dto.parties.entrySet()) {
                                        chain = chain.chain(() -> arrangement
                                                .addOrUpdateInvolvedParty(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.resources != null && !dto.resources.isEmpty()) {
                                    for (var entry : dto.resources.entrySet()) {
                                        chain = chain.chain(() -> arrangement
                                                .addOrUpdateResourceItem(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.rules != null && !dto.rules.isEmpty()) {
                                    for (var entry : dto.rules.entrySet()) {
                                        chain = chain.chain(() -> arrangement
                                                .addOrUpdateRules(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.products != null && !dto.products.isEmpty()) {
                                    for (var entry : dto.products.entrySet()) {
                                        chain = chain.chain(() -> arrangement
                                                .addOrUpdateProduct(session, entry.getKey(), null, null, entry.getValue(), sys, identityToken)
                                                .replaceWithVoid());
                                    }
                                }

                                if (dto.childArrangements != null && !dto.childArrangements.isEmpty()) {
                                    for (var entry : dto.childArrangements.entrySet()) {
                                        UUID childId = UUID.fromString(entry.getKey());
                                        chain = chain.chain(() ->
                                                arrangementsService.find(session, childId, sys, identityToken)
                                                        .chain(child -> arrangement
                                                                .addChild(session, (Arrangement) child, null, entry.getValue(), sys, identityToken)
                                                                .replaceWithVoid()));
                                    }
                                }

                                return chain;
                            })
                            // Step 3: Build the response DTO in a fresh session
                            .chain(() -> buildCreateResponse(enterpriseName, requestingSystemName, (Arrangement) arrangement, dto));
                })
                .onFailure().invoke(e ->
                        log.error("Error creating arrangement for enterprise {} and system {}: {}",
                                enterpriseName, requestingSystemName, e.getMessage(), e)
                )
        );
    }

    /**
     * Builds the {@link ArrangementDTO} response after arrangement creation,
     * fetching the relevant includes in a fresh session.
     */
    private Uni<ArrangementDTO> buildCreateResponse(String enterpriseName, String requestingSystemName,
                                                    Arrangement arrangement, ArrangementCreateDTO dto) {
        return SessionUtils.<ArrangementDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();

            ArrangementDTO response = new ArrangementDTO();
            response.arrangementId = arrangement.getId();

            List<ArrangementDataIncludes> includes = new ArrayList<>();
            includes.add(ArrangementDataIncludes.Types);
            includes.add(ArrangementDataIncludes.Classifications);
            if (dto.parties != null && !dto.parties.isEmpty())
                includes.add(ArrangementDataIncludes.Parties);
            if (dto.resources != null && !dto.resources.isEmpty())
                includes.add(ArrangementDataIncludes.Resources);
            if (dto.rules != null && !dto.rules.isEmpty())
                includes.add(ArrangementDataIncludes.Rules);
            if (dto.products != null && !dto.products.isEmpty())
                includes.add(ArrangementDataIncludes.Products);
            if (dto.childArrangements != null && !dto.childArrangements.isEmpty())
                includes.add(ArrangementDataIncludes.Arrangements);

            Uni<ArrangementDTO> fetchChain = Uni.createFrom().item(response);
            for (ArrangementDataIncludes include : includes) {
                fetchChain = fetchChain.chain(d -> fetchInclude(session, arrangement, d, include));
            }
            return fetchChain;
        });
    }

    /**
     * Updates an existing arrangement's relationships.
     * Each relationship type supports {@code addOrUpdate} (upsert by name) and {@code delete} (expire by name).
     * All keys are <b>names</b> — classification names, type names, product names, rule names, rule type names.
     * For child arrangements, keys are UUID strings.
     *
     * <pre>{@code
     * PUT /{enterprise}/arrangement/{system}/{arrangementId}
     * {
     *   "classifications": {
     *     "addOrUpdate": { "Status": "Active", "Priority": "High" },
     *     "delete": ["Deprecated"]
     *   },
     *   "types": {
     *     "addOrUpdate": { "Invoice": "INV-2025-001" }
     *   },
     *   "ruleTypes": {
     *     "addOrUpdate": { "ValidationRule": "Enabled" },
     *     "delete": ["OldRule"]
     *   }
     * }
     * }</pre>
     */
    @PUT
    @Path("{requestingSystemName}/update")
    public Uni<ArrangementDTO> update(@PathParam("enterprise") String enterpriseName,
                                      @PathParam("requestingSystemName") String requestingSystemName,
                                      ArrangementUpdateDTO dto) {
        UUID arrangementId = dto.arrangementId;
        return SessionUtils.<ArrangementDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] identityToken = tuple.getItem4();

            return arrangementsService.find(session, arrangementId, system, identityToken)
                    .chain(arrangement -> {
                        Uni<Void> chain = Uni.createFrom().voidItem();

                        // ── Classifications ──
                        chain = chainAddOrUpdate(chain, dto.classifications, (name, value) ->
                                arrangement.addOrUpdateClassification(session, name, value, system, identityToken).replaceWithVoid());
                        chain = chainDelete(chain, dto.classifications, name ->
                                arrangement.removeClassification(session, name, null, system, identityToken).replaceWithVoid());

                        // ── Arrangement Types ──
                        chain = chainAddOrUpdate(chain, dto.types, (name, value) ->
                                arrangement.addOrUpdateArrangementType(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.types, session, arrangement, ArrangementXArrangementType.class,
                                ArrangementXArrangementType_.arrangement, ArrangementXArrangementType::getType,
                                type -> type != null ? type.getName() : null);

                        // ── Involved Parties ──
                        chain = chainAddOrUpdate(chain, dto.parties, (name, value) ->
                                arrangement.addOrUpdateInvolvedParty(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.parties, session, arrangement, ArrangementXInvolvedParty.class,
                                ArrangementXInvolvedParty_.arrangementID, ArrangementXInvolvedParty::getInvolvedPartyID,
                                ip -> ip != null && ip.getId() != null ? ip.getId().toString() : null);

                        // ── Resource Items ──
                        chain = chainAddOrUpdate(chain, dto.resources, (name, value) ->
                                arrangement.addOrUpdateResourceItem(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.resources, session, arrangement, ArrangementXResourceItem.class,
                                ArrangementXResourceItem_.arrangementID, ArrangementXResourceItem::getResourceItemID,
                                ri -> ri != null && ri.getId() != null ? ri.getId().toString() : null);

                        // ── Rules ──
                        chain = chainAddOrUpdate(chain, dto.rules, (name, value) ->
                                arrangement.addOrUpdateRules(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.rules, session, arrangement, ArrangementXRules.class,
                                ArrangementXRules_.arrangement, ArrangementXRules::getRulesID,
                                rule -> rule != null && rule.getId() != null ? rule.getId().toString() : null);

                        // ── Products ──
                        chain = chainAddOrUpdate(chain, dto.products, (name, value) ->
                                arrangement.addOrUpdateProduct(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDeleteByExpire(chain, dto.products, session, arrangement, ArrangementXProduct.class,
                                ArrangementXProduct_.arrangementID, ArrangementXProduct::getProductID,
                                product -> product != null && product.getId() != null ? product.getId().toString() : null);

                        // ── Rule Types ──
                        chain = chainAddOrUpdate(chain, dto.ruleTypes, (name, value) ->
                                arrangement.addOrUpdateRuleTypes(session, name, null, null, value, system, identityToken).replaceWithVoid());
                        chain = chainDelete(chain, dto.ruleTypes, name ->
                                arrangement.removeRuleTypes(session, name, null, null, null, system, identityToken).replaceWithVoid());

                        // ── Child Arrangements ──
                        chain = chainAddOrUpdate(chain, dto.childArrangements, (childIdStr, value) -> {
                            UUID childId = UUID.fromString(childIdStr);
                            return arrangementsService.find(session, childId, system, identityToken)
                                    .chain(child -> arrangement.addChild(session, (Arrangement) child, null, value, system, identityToken)
                                            .replaceWithVoid());
                        });
                        chain = chainDeleteByExpire(chain, dto.childArrangements, session, arrangement, ArrangementXArrangement.class,
                                ArrangementXArrangement_.parentArrangementID, ArrangementXArrangement::getChildArrangementID,
                                child -> child != null && child.getId() != null ? child.getId().toString() : null);

                        // ── Build response ──
                        return chain.chain(() -> {
                            ArrangementDTO response = new ArrangementDTO();
                            response.arrangementId = arrangementId;
                            Arrangement arr = (Arrangement) arrangement;

                            List<ArrangementDataIncludes> includes = determineIncludes(dto);

                            Uni<ArrangementDTO> fetchChain = Uni.createFrom().item(response);
                            for (ArrangementDataIncludes include : includes) {
                                fetchChain = fetchChain.chain(d -> fetchInclude(session, arr, d, include));
                            }
                            return fetchChain;
                        });
                    })
                    .onFailure().invoke(e ->
                            log.error("Error updating arrangement {} for enterprise {} and system {}: {}",
                                    arrangementId, enterpriseName, requestingSystemName, e.getMessage(), e)
                    );
        });
    }

    // ── Update helper methods ──

    /**
     * Chains addOrUpdate operations for a {@link RelationshipUpdateEntry}.
     */
    private Uni<Void> chainAddOrUpdate(Uni<Void> chain, RelationshipUpdateEntry entry,
                                       java.util.function.BiFunction<String, String, Uni<Void>> addOrUpdateFn) {
        if (entry == null || entry.addOrUpdate == null || entry.addOrUpdate.isEmpty()) return chain;
        for (var e : entry.addOrUpdate.entrySet()) {
            chain = chain.chain(() -> addOrUpdateFn.apply(e.getKey(), e.getValue()));
        }
        return chain;
    }

    /**
     * Chains delete (expire) operations for a {@link RelationshipUpdateEntry} that has a dedicated remove method.
     */
    private Uni<Void> chainDelete(Uni<Void> chain, RelationshipUpdateEntry entry,
                                  java.util.function.Function<String, Uni<Void>> deleteFn) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;
        for (String name : entry.delete) {
            chain = chain.chain(() -> deleteFn.apply(name));
        }
        return chain;
    }

    /**
     * Chains delete (expire) operations by querying the relationship table, matching by the related entity's name/id,
     * and expiring matched rows. Used for relationship types that don't have a dedicated remove method.
     *
     * @param <L>             the link/relationship entity type
     * @param <R>             the related entity type
     * @param linkClass       the relationship entity class (e.g. ArrangementXProduct.class)
     * @param arrangementAttr the SingularAttribute linking to the arrangement
     * @param relatedGetter   extracts the related entity from the link row
     * @param nameExtractor   extracts the name/ID string from the related entity for matching against the delete list
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <L extends WarehouseBaseTable<L, ?, UUID>, R> Uni<Void> chainDeleteByExpire(
            Uni<Void> chain,
            RelationshipUpdateEntry entry,
            Mutiny.Session session,
            IArrangement<?, ?> arrangement,
            Class<L> linkClass,
            jakarta.persistence.metamodel.SingularAttribute arrangementAttr,
            java.util.function.Function<L, R> relatedGetter,
            java.util.function.Function<R, String> nameExtractor) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;

        Set<String> namesToDelete = new HashSet<>(entry.delete);

        return chain.chain(() -> {
            try {
                L instance = linkClass.getDeclaredConstructor().newInstance();
                return ((com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD) instance.builder(session)
                        .where(arrangementAttr, Equals, arrangement))
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

    /**
     * Determines which {@link ArrangementDataIncludes} to fetch based on which relationship types
     * were modified in the update DTO.
     */
    private List<ArrangementDataIncludes> determineIncludes(ArrangementUpdateDTO dto) {
        List<ArrangementDataIncludes> includes = new ArrayList<>();
        if (hasEntries(dto.types)) includes.add(ArrangementDataIncludes.Types);
        //if (hasEntries(dto.classifications)) includes.add(ArrangementDataIncludes.Classifications);
        includes.add(ArrangementDataIncludes.Classifications);
        if (hasEntries(dto.parties)) includes.add(ArrangementDataIncludes.Parties);
        //if (hasEntries(dto.resources)) includes.add(ArrangementDataIncludes.Resources);
        includes.add(ArrangementDataIncludes.Resources);
        if (hasEntries(dto.rules)) includes.add(ArrangementDataIncludes.Rules);
        if (hasEntries(dto.products)) includes.add(ArrangementDataIncludes.Products);
        if (hasEntries(dto.ruleTypes)) includes.add(ArrangementDataIncludes.RuleTypes);
        if (hasEntries(dto.childArrangements)) includes.add(ArrangementDataIncludes.Arrangements);
        // Always include types and classifications if nothing else was requested
        if (includes.isEmpty()) {
            includes.add(ArrangementDataIncludes.Types);
            includes.add(ArrangementDataIncludes.Classifications);
        }
        return includes;
    }

    private boolean hasEntries(RelationshipUpdateEntry entry) {
        if (entry == null) return false;
        return (entry.addOrUpdate != null && !entry.addOrUpdate.isEmpty())
                || (entry.delete != null && !entry.delete.isEmpty());
    }


}
