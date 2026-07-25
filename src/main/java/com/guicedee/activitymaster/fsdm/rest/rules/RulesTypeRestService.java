package com.guicedee.activitymaster.fsdm.rest.rules;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.IRulesService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.rest.RelationshipUpdateEntry;
import com.guicedee.activitymaster.fsdm.client.services.rest.rules.*;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.entityassist.enumerations.Operand.Equals;

/**
 * REST surface for the ActivityMaster {@code RulesType} domain — the structural/implementation
 * classification of a rule (e.g. {@code Single Rule}, {@code Statement Rule}, {@code Range Rule},
 * {@code Matrix Rule}).
 * <p>
 * Rule types support two relationship categories: <b>Classifications</b> and <b>Resources</b>
 * (specifications, examples and supporting artefacts). Create/update apply relationships
 * fire-and-forget while the response echoes the submitted state.
 */
@Path("{enterprise}/rules-type")
@Tag(name = "Rule Types", description = "Rule type lifecycle — create, find and update rule types, their classifications and supporting resources.")
@Log4j2
public class RulesTypeRestService {

    @Inject
    private IRulesService<?> rulesService;

    @Inject
    private IResourceItemService<?> resourceItemService;

    @Inject
    private IClassificationService<?> classificationService;

    // ──────────────────────────────────────────────────────────────────────────
    // Find
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/find")
    @Operation(summary = "Find a rule type",
            description = "Returns a rule type by id, hydrating only the relationship categories named in the request's includes list.")
    @ApiResponse(responseCode = "200", description = "Rule type found (relationships populated per includes)")
    @ApiResponse(responseCode = "500", description = "Lookup failure")
    public Uni<RulesTypeDTO> find(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                  @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                  RulesTypeFindDTO findDto) {
        UUID rulesTypeId = findDto.rulesTypeId;
        List<RulesTypeDataIncludes> includesList = findDto.includes;
        return SessionUtils.<RulesTypeDTO>withActivityMasterStateless(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.StatelessSession, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.StatelessSession session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return rulesService.findType(session, rulesTypeId)
                            .chain(ruleType -> {
                                if (ruleType == null) {
                                    return Uni.createFrom().failure(new NotFoundException("Rule type not found: " + rulesTypeId));
                                }
                                RulesType rt = (RulesType) ruleType;
                                RulesTypeDTO dto = new RulesTypeDTO();
                                dto.rulesTypeId = rulesTypeId;
                                dto.name = rt.getName();
                                dto.description = rt.getDescription();

                                Uni<RulesTypeDTO> chain = Uni.createFrom().item(dto);
                                if (includesList == null || includesList.isEmpty()) {
                                    return chain;
                                }
                                for (RulesTypeDataIncludes include : includesList) {
                                    chain = chain.chain(d -> fetchInclude(session, rt, d, include, system, token));
                                }
                                return chain;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error finding rule type {} for enterprise {} system {}: {}",
                                            rulesTypeId, enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Create
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/create")
    @Operation(summary = "Create a rule type",
            description = "Creates a rule type (reusing an existing one of the same name), then persists relationships asynchronously. The response echoes the submitted DTO immediately.")
    @ApiResponse(responseCode = "200", description = "Rule type created; relationships persist asynchronously")
    @ApiResponse(responseCode = "500", description = "Creation failure")
    public Uni<RulesTypeDTO> create(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                    @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                    RulesTypeCreateDTO dto) {
        String description = dto.description != null ? dto.description : dto.name;
        return SessionUtils.<RulesTypeDTO>withActivityMasterStateless(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.StatelessSession, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.StatelessSession session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return rulesService.createRulesType(session, dto.name, description, system, token)
                            .map(ruleType -> {
                                UUID rulesTypeId = ruleType.getId();
                                if (hasMap(dto.classifications) || hasMap(dto.resources)) {
                                    persistCreateRelationshipsAsync(enterpriseName, requestingSystemName, rulesTypeId, dto);
                                }
                                return buildCreateResponseFromDto(rulesTypeId, dto);
                            });
                }
        ).onFailure().invoke(e ->
                log.error("Error creating rule type '{}' for enterprise {} and system {}: {}",
                        dto.name, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update
    // ──────────────────────────────────────────────────────────────────────────

    @PUT
    @Path("{requestingSystemName}/update")
    @Operation(summary = "Update a rule type",
            description = "Updates the rule type's description in place and applies addOrUpdate/delete operations to its classifications and resources. Relationship persistence is fire-and-forget; the response echoes the intended state.")
    @ApiResponse(responseCode = "200", description = "Update accepted; relationships persist asynchronously")
    @ApiResponse(responseCode = "500", description = "Update failure")
    public Uni<RulesTypeDTO> update(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                    @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                    RulesTypeUpdateDTO dto) {
        UUID rulesTypeId = dto.rulesTypeId;
        return SessionUtils.<UUID>withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.StatelessSession session = tuple.getItem1();
            return rulesService.findType(session, rulesTypeId)
                    .chain(ruleType -> {
                        if (ruleType == null) {
                            return Uni.createFrom().failure(new NotFoundException("Rule type not found: " + rulesTypeId));
                        }
                        if (dto.description != null) {
                            ((RulesType) ruleType).setDescription(dto.description);
                        }
                        return Uni.createFrom().item(ruleType.getId());
                    });
        }).map(foundId -> {
            persistUpdateRelationshipsAsync(enterpriseName, requestingSystemName, foundId, dto);
            return buildUpdateResponseFromDto(rulesTypeId, dto);
        }).onFailure().invoke(e ->
                log.error("Error updating rule type {} for enterprise {} and system {}: {}",
                        rulesTypeId, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Include fetching
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<RulesTypeDTO> fetchInclude(Mutiny.StatelessSession session, RulesType ruleType, RulesTypeDTO dto,
                                           RulesTypeDataIncludes include, ISystems<?, ?> system, UUID[] token) {
        return switch (include) {
            case Classifications -> new RulesTypeXClassification().builder(session)
                    .where(RulesTypeXClassification_.rulesTypeID, Equals, ruleType)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (RulesTypeXClassification link : list) {
                            fetchChain = fetchChain.chain(() -> classificationService.find(session, link.getClassificationID().getId(), system, token)
                                    .invoke(classification -> {
                                        String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.classifications = map; return dto; });
                    });

            case Resources -> new RulesTypeXResourceItem().builder(session)
                    .where(RulesTypeXResourceItem_.rulesTypeID, Equals, ruleType)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (RulesTypeXResourceItem link : list) {
                            fetchChain = fetchChain.chain(() -> classificationService.find(session, link.getClassificationID().getId(), system, token)
                                    .chain(classification -> session.fetch(link.getResourceItemID())
                                            .invoke(resource -> {
                                                String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                                String value = resource != null && resource.getId() != null ? resource.getId().toString() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.resources = map; return dto; });
                    });
        };
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Fire-and-forget relationship persistence
    // ──────────────────────────────────────────────────────────────────────────

    private void persistCreateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                 UUID rulesTypeId, RulesTypeCreateDTO dto) {
        String label = "rule type " + rulesTypeId;
        if (hasMap(dto.classifications)) {
            fireRuleType(enterpriseName, requestingSystemName, rulesTypeId, label + " classifications", (s, rt, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var e : dto.classifications.entrySet()) {
                    chain = chain.chain(() -> rt.addOrUpdateClassification(s, e.getKey(), e.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            });
        }
        if (hasMap(dto.resources)) {
            fireRuleType(enterpriseName, requestingSystemName, rulesTypeId, label + " resources", (s, rt, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var e : dto.resources.entrySet()) {
                    UUID riId = parseUuidOrNull(e.getValue(), label + " resources");
                    if (riId == null) continue;
                    String classificationName = e.getKey();
                    chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                            .chain(ri -> rt.addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token)).replaceWithVoid());
                }
                return chain;
            });
        }
    }

    private void persistUpdateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                UUID rulesTypeId, RulesTypeUpdateDTO dto) {
        String label = "rule type " + rulesTypeId;
        if (hasEntries(dto.classifications)) {
            fireRuleType(enterpriseName, requestingSystemName, rulesTypeId, label + " classifications", (s, rt, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.classifications, (name, value) ->
                        rt.addOrUpdateClassification(s, name, value, sys, token).replaceWithVoid());
                chain = chainDelete(chain, dto.classifications, name ->
                        rt.removeClassification(s, name, null, sys, token).replaceWithVoid());
                return chain;
            });
        }
        if (hasEntries(dto.resources)) {
            fireRuleType(enterpriseName, requestingSystemName, rulesTypeId, label + " resources", (s, rt, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                if (dto.resources.addOrUpdate != null) {
                    for (var e : dto.resources.addOrUpdate.entrySet()) {
                        UUID riId = parseUuidOrNull(e.getValue(), label + " resources addOrUpdate");
                        if (riId == null) continue;
                        String classificationName = e.getKey();
                        chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                                .chain(ri -> rt.addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token)).replaceWithVoid());
                    }
                }
                chain = expireResourcesByName(chain, dto.resources, s, rt, sys, token);
                return chain;
            });
        }
    }

    private Uni<Void> expireResourcesByName(Uni<Void> chain, RelationshipUpdateEntry entry, Mutiny.StatelessSession s, RulesType ruleType,
                                            ISystems<?, ?> system, UUID[] token) {
        if (entry.delete == null || entry.delete.isEmpty()) return chain;
        Set<String> namesToDelete = new HashSet<>(entry.delete);
        return chain.chain(() -> new RulesTypeXResourceItem().builder(s)
                .where(RulesTypeXResourceItem_.rulesTypeID, Equals, ruleType)
                .inActiveRange()
                .inDateRange()
                .getAll()
                .chain(list -> {
                    Uni<Void> expireChain = Uni.createFrom().voidItem();
                    for (RulesTypeXResourceItem link : list) {
                        expireChain = expireChain.chain(() -> classificationService.find(s, link.getClassificationID().getId(), system, token)
                                .chain(classification -> {
                                    String name = classification != null ? classification.getName() : null;
                                    if (name != null && namesToDelete.contains(name)) {
                                        return link.expire(s).replaceWithVoid();
                                    }
                                    return Uni.createFrom().voidItem();
                                }));
                    }
                    return expireChain;
                }));
    }

    @FunctionalInterface
    private interface RuleTypeWork {
        Uni<Void> apply(Mutiny.StatelessSession session, RulesType ruleType, ISystems<?, ?> system, UUID[] token);
    }

    private void fireRuleType(String enterpriseName, String requestingSystemName, UUID rulesTypeId, String label, RuleTypeWork work) {
        SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.StatelessSession s = tuple.getItem1();
            ISystems<?, ?> sys = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            return rulesService.findType(s, rulesTypeId).chain(rt -> {
                if (rt == null) return Uni.createFrom().voidItem();
                return work.apply(s, (RulesType) rt, sys, token);
            });
        }), label);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO-based response builders
    // ──────────────────────────────────────────────────────────────────────────

    private RulesTypeDTO buildCreateResponseFromDto(UUID rulesTypeId, RulesTypeCreateDTO dto) {
        RulesTypeDTO response = new RulesTypeDTO();
        response.rulesTypeId = rulesTypeId;
        response.name = dto.name;
        response.description = dto.description;
        response.classifications = copy(dto.classifications);
        response.resources = copy(dto.resources);
        return response;
    }

    private RulesTypeDTO buildUpdateResponseFromDto(UUID rulesTypeId, RulesTypeUpdateDTO dto) {
        RulesTypeDTO response = new RulesTypeDTO();
        response.rulesTypeId = rulesTypeId;
        response.description = dto.description;
        response.classifications = addOrUpdateOf(dto.classifications);
        response.resources = addOrUpdateOf(dto.resources);
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers
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

    private boolean hasEntries(RelationshipUpdateEntry entry) {
        if (entry == null) return false;
        return (entry.addOrUpdate != null && !entry.addOrUpdate.isEmpty())
                || (entry.delete != null && !entry.delete.isEmpty());
    }

    private boolean hasMap(Map<String, String> map) {
        return map != null && !map.isEmpty();
    }

    private Map<String, String> copy(Map<String, String> map) {
        return map != null ? new LinkedHashMap<>(map) : null;
    }

    private Map<String, String> addOrUpdateOf(RelationshipUpdateEntry entry) {
        return (entry != null && entry.addOrUpdate != null) ? new LinkedHashMap<>(entry.addOrUpdate) : null;
    }

    private UUID parseUuidOrNull(String value, String context) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Skipping invalid UUID '{}' in {}", value, context);
            return null;
        }
    }
}

