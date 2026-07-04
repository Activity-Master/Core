package com.guicedee.activitymaster.fsdm.rest.rules;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
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
 * REST surface for the ActivityMaster {@code Rules} domain — reusable business requirements, limits,
 * thresholds, eligibility criteria, prices and permissions.
 * <p>
 * Rules support five relationship categories: <b>Classifications</b> (purpose/argument/lifecycle),
 * <b>Resources</b> (supporting documentation), <b>Products</b> (rules applied to products),
 * <b>RuleTypes</b> (structural type links) and <b>Children</b> (rule-to-rule composition hierarchy).
 * Create/update apply relationships fire-and-forget while the response echoes the submitted state.
 */
@Path("{enterprise}/rules")
@Tag(name = "Rules", description = "Business rule lifecycle — create, find and update rules, their classifications, products, rule types and composition hierarchy.")
@Log4j2
public class RulesRestService {

    @Inject
    private IRulesService<?> rulesService;

    @Inject
    private IResourceItemService<?> resourceItemService;

    @Inject
    private IProductService<?> productService;

    @Inject
    private com.guicedee.activitymaster.fsdm.rest.EventActionSupport eventActionSupport;

    // ──────────────────────────────────────────────────────────────────────────
    // Find
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/find")
    @Operation(summary = "Find a rule",
            description = "Returns a rule by id, hydrating only the relationship categories named in the request's includes list.")
    @ApiResponse(responseCode = "200", description = "Rule found (relationships populated per includes)")
    @ApiResponse(responseCode = "500", description = "Lookup failure")
    public Uni<RulesDTO> find(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                              @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                              RulesFindDTO findDto) {
        UUID rulesId = findDto.rulesId;
        List<RulesDataIncludes> includesList = findDto.includes;
        return SessionUtils.<RulesDTO>withActivityMasterStateless(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.StatelessSession, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.StatelessSession session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return rulesService.find(session, rulesId)
                            .chain(rule -> {
                                if (rule == null) {
                                    return Uni.createFrom().failure(new NotFoundException("Rule not found: " + rulesId));
                                }
                                Rules r = (Rules) rule;
                                RulesDTO dto = new RulesDTO();
                                dto.rulesId = rulesId;
                                dto.name = r.getName();
                                dto.description = r.getDescription();

                                Uni<RulesDTO> chain = Uni.createFrom().item(dto);
                                if (includesList == null || includesList.isEmpty()) {
                                    return chain;
                                }
                                for (RulesDataIncludes include : includesList) {
                                    chain = chain.chain(d -> fetchInclude(session, r, d, include, system, token));
                                }
                                return chain;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error finding rule {} for enterprise {} system {}: {}",
                                            rulesId, enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Create
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/create")
    @Operation(summary = "Create a rule",
            description = "Creates a rule, then persists all supplied relationships asynchronously (fire-and-forget). The response echoes the submitted DTO immediately. Supply an optional 'event' block to associate this create with an event (records the action + a change summary).")
    @ApiResponse(responseCode = "200", description = "Rule created; relationships persist asynchronously")
    @ApiResponse(responseCode = "500", description = "Creation failure")
    public Uni<RulesDTO> create(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                RulesCreateDTO dto) {
        String primaryRuleType = (dto.ruleTypes != null && !dto.ruleTypes.isEmpty())
                ? dto.ruleTypes.keySet().iterator().next() : null;
        return SessionUtils.<RulesDTO>withActivityMasterStateless(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.StatelessSession, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.StatelessSession session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return rulesService.createRules(session, primaryRuleType, dto.name, dto.description, system, token)
                            .map(rule -> {
                                UUID rulesId = rule.getId();
                                if (hasAnyRelationship(dto)) {
                                    persistCreateRelationshipsAsync(enterpriseName, requestingSystemName, rulesId, dto);
                                }
                                // Optionally associate this create with an event (fire-and-forget)
                                eventActionSupport.recordRulesAction(enterpriseName, requestingSystemName, dto.event, true, rulesId);
                                return buildCreateResponseFromDto(rule.getId(), dto);
                            });
                }
        ).onFailure().invoke(e ->
                log.error("Error creating rule '{}' for enterprise {} and system {}: {}",
                        dto.name, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update
    // ──────────────────────────────────────────────────────────────────────────

    @PUT
    @Path("{requestingSystemName}/update")
    @Operation(summary = "Update a rule",
            description = "Updates the rule's description in place and applies addOrUpdate/delete operations to each relationship category. Relationship persistence is fire-and-forget; the response echoes the intended addOrUpdate state. Supply an optional 'event' block to associate this update with an event (records the action + a change summary).")
    @ApiResponse(responseCode = "200", description = "Update accepted; relationships persist asynchronously")
    @ApiResponse(responseCode = "500", description = "Update failure")
    public Uni<RulesDTO> update(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                RulesUpdateDTO dto) {
        UUID rulesId = dto.rulesId;
        return SessionUtils.<UUID>withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.StatelessSession session = tuple.getItem1();
            return rulesService.find(session, rulesId)
                    .chain(rule -> {
                        if (rule == null) {
                            return Uni.createFrom().failure(new NotFoundException("Rule not found: " + rulesId));
                        }
                        if (dto.description != null) {
                            ((Rules) rule).setDescription(dto.description);
                        }
                        return Uni.createFrom().item(rule.getId());
                    });
        }).map(foundId -> {
            persistUpdateRelationshipsAsync(enterpriseName, requestingSystemName, foundId, dto);
            // Optionally associate this update with an event (fire-and-forget)
            eventActionSupport.recordRulesAction(enterpriseName, requestingSystemName, dto.event, false, foundId);
            return buildUpdateResponseFromDto(rulesId, dto);
        }).onFailure().invoke(e ->
                log.error("Error updating rule {} for enterprise {} and system {}: {}",
                        rulesId, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Include fetching
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<RulesDTO> fetchInclude(Mutiny.StatelessSession session, Rules rule, RulesDTO dto,
                                       RulesDataIncludes include, ISystems<?, ?> system, UUID[] token) {
        return switch (include) {
            case Classifications -> new RulesXClassification().builder(session)
                    .where(RulesXClassification_.rulesID, Equals, rule)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (RulesXClassification link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .invoke(classification -> {
                                        String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.classifications = map; return dto; });
                    });

            case Resources -> new RulesXResourceItem().builder(session)
                    .where(RulesXResourceItem_.rulesID, Equals, rule)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (RulesXResourceItem link : list) {
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

            case Products -> new RulesXProduct().builder(session)
                    .where(RulesXProduct_.rulesID, Equals, rule)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (RulesXProduct link : list) {
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

            case RuleTypes -> new RulesXRulesType().builder(session)
                    .where(RulesXRulesType_.rulesID, Equals, rule)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (RulesXRulesType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getRulesTypeID())
                                    .invoke(ruleType -> {
                                        String key = ruleType != null && ruleType.getName() != null ? ruleType.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.ruleTypes = map; return dto; });
                    });

            case Children -> rule.findChildren(session, (String) null, null, system, token)
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (var link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getSecondary())
                                    .invoke(child -> {
                                        String key = child != null && child.getName() != null ? child.getName() : String.valueOf(link.getValue());
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
                                                 UUID rulesId, RulesCreateDTO dto) {
        String label = "rule " + rulesId;

        if (hasMap(dto.classifications)) {
            fireRule(enterpriseName, requestingSystemName, rulesId, label + " classifications", (s, rule, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var e : dto.classifications.entrySet()) {
                    chain = chain.chain(() -> rule.addOrUpdateClassification(s, e.getKey(), e.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            });
        }
        if (hasMap(dto.ruleTypes)) {
            fireRule(enterpriseName, requestingSystemName, rulesId, label + " ruleTypes", (s, rule, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var e : dto.ruleTypes.entrySet()) {
                    chain = chain.chain(() -> rule.addOrUpdateRuleTypes(s, e.getKey(), null, null, e.getValue(), sys, token).replaceWithVoid());
                }
                return chain;
            });
        }
        if (hasMap(dto.resources)) {
            fireRule(enterpriseName, requestingSystemName, rulesId, label + " resources", (s, rule, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var e : dto.resources.entrySet()) {
                    UUID riId = parseUuidOrNull(e.getValue(), label + " resources");
                    if (riId == null) continue;
                    String classificationName = e.getKey();
                    chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                            .chain(ri -> rule.addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token)).replaceWithVoid());
                }
                return chain;
            });
        }
        if (hasMap(dto.products)) {
            fireRule(enterpriseName, requestingSystemName, rulesId, label + " products", (s, rule, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var e : dto.products.entrySet()) {
                    UUID prodId = parseUuidOrNull(e.getValue(), label + " products");
                    if (prodId == null) continue;
                    String classificationName = e.getKey();
                    chain = chain.chain(() -> productService.find(s, prodId)
                            .chain(product -> rule.addOrUpdateProduct(s, classificationName, product, null, null, sys, token)).replaceWithVoid());
                }
                return chain;
            });
        }
        if (hasMap(dto.children)) {
            fireRuleWithEnterprise(enterpriseName, requestingSystemName, rulesId, label + " children", (s, rule, ent, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                for (var e : dto.children.entrySet()) {
                    chain = chain.chain(() -> rulesService.findRules(s, e.getKey(), ent, token)
                            .chain(child -> rule.addChild(s, (Rules) child, null, e.getValue(), sys, token)).replaceWithVoid());
                }
                return chain;
            });
        }
    }

    private void persistUpdateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                UUID rulesId, RulesUpdateDTO dto) {
        String label = "rule " + rulesId;

        if (hasEntries(dto.classifications)) {
            fireRule(enterpriseName, requestingSystemName, rulesId, label + " classifications", (s, rule, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.classifications, (name, value) ->
                        rule.addOrUpdateClassification(s, name, value, sys, token).replaceWithVoid());
                chain = chainDelete(chain, dto.classifications, name ->
                        rule.removeClassification(s, name, null, sys, token).replaceWithVoid());
                return chain;
            });
        }
        if (hasEntries(dto.ruleTypes)) {
            fireRule(enterpriseName, requestingSystemName, rulesId, label + " ruleTypes", (s, rule, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                chain = chainAddOrUpdate(chain, dto.ruleTypes, (name, value) ->
                        rule.addOrUpdateRuleTypes(s, name, null, null, value, sys, token).replaceWithVoid());
                chain = chainDelete(chain, dto.ruleTypes, name ->
                        rule.removeRuleTypes(s, name, null, null, null, sys, token).replaceWithVoid());
                return chain;
            });
        }
        if (hasEntries(dto.resources)) {
            fireRule(enterpriseName, requestingSystemName, rulesId, label + " resources", (s, rule, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                if (dto.resources.addOrUpdate != null) {
                    for (var e : dto.resources.addOrUpdate.entrySet()) {
                        UUID riId = parseUuidOrNull(e.getValue(), label + " resources addOrUpdate");
                        if (riId == null) continue;
                        String classificationName = e.getKey();
                        chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                                .chain(ri -> rule.addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token)).replaceWithVoid());
                    }
                }
                chain = expireResourcesByName(chain, dto.resources, s, rule);
                return chain;
            });
        }
        if (hasEntries(dto.products)) {
            fireRule(enterpriseName, requestingSystemName, rulesId, label + " products", (s, rule, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                if (dto.products.addOrUpdate != null) {
                    for (var e : dto.products.addOrUpdate.entrySet()) {
                        UUID prodId = parseUuidOrNull(e.getValue(), label + " products addOrUpdate");
                        if (prodId == null) continue;
                        String classificationName = e.getKey();
                        chain = chain.chain(() -> productService.find(s, prodId)
                                .chain(product -> rule.addOrUpdateProduct(s, classificationName, product, null, null, sys, token)).replaceWithVoid());
                    }
                }
                chain = expireProductsByName(chain, dto.products, s, rule);
                return chain;
            });
        }
        if (hasEntries(dto.children)) {
            fireRuleWithEnterprise(enterpriseName, requestingSystemName, rulesId, label + " children", (s, rule, ent, sys, token) -> {
                Uni<Void> chain = Uni.createFrom().voidItem();
                if (dto.children.addOrUpdate != null) {
                    for (var e : dto.children.addOrUpdate.entrySet()) {
                        chain = chain.chain(() -> rulesService.findRules(s, e.getKey(), ent, token)
                                .chain(child -> rule.addChild(s, (Rules) child, null, e.getValue(), sys, token)).replaceWithVoid());
                    }
                }
                if (dto.children.delete != null) {
                    for (String childName : dto.children.delete) {
                        chain = chain.chain(() -> rulesService.findRules(s, childName, ent, token)
                                .chain(child -> rule.archiveChild(s, (Rules) child, null, null, sys, token)).replaceWithVoid());
                    }
                }
                return chain;
            });
        }
    }

    // Expire RulesXResourceItem rows whose classification name appears in the delete list.
    private Uni<Void> expireResourcesByName(Uni<Void> chain, RelationshipUpdateEntry entry, Mutiny.StatelessSession s, Rules rule) {
        if (entry.delete == null || entry.delete.isEmpty()) return chain;
        Set<String> namesToDelete = new HashSet<>(entry.delete);
        return chain.chain(() -> new RulesXResourceItem().builder(s)
                .where(RulesXResourceItem_.rulesID, Equals, rule)
                .inActiveRange()
                .inDateRange()
                .getAll()
                .chain(list -> {
                    Uni<Void> expireChain = Uni.createFrom().voidItem();
                    for (RulesXResourceItem link : list) {
                        expireChain = expireChain.chain(() -> s.fetch(link.getClassificationID())
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

    // Expire RulesXProduct rows whose classification name appears in the delete list.
    private Uni<Void> expireProductsByName(Uni<Void> chain, RelationshipUpdateEntry entry, Mutiny.StatelessSession s, Rules rule) {
        if (entry.delete == null || entry.delete.isEmpty()) return chain;
        Set<String> namesToDelete = new HashSet<>(entry.delete);
        return chain.chain(() -> new RulesXProduct().builder(s)
                .where(RulesXProduct_.rulesID, Equals, rule)
                .inActiveRange()
                .inDateRange()
                .getAll()
                .chain(list -> {
                    Uni<Void> expireChain = Uni.createFrom().voidItem();
                    for (RulesXProduct link : list) {
                        expireChain = expireChain.chain(() -> s.fetch(link.getClassificationID())
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

    // ──────────────────────────────────────────────────────────────────────────
    // Fire-and-forget helpers
    // ──────────────────────────────────────────────────────────────────────────

    @FunctionalInterface
    private interface RuleWork {
        Uni<Void> apply(Mutiny.StatelessSession session, Rules rule, ISystems<?, ?> system, UUID[] token);
    }

    @FunctionalInterface
    private interface RuleWorkWithEnterprise {
        Uni<Void> apply(Mutiny.StatelessSession session, Rules rule, IEnterprise<?, ?> enterprise, ISystems<?, ?> system, UUID[] token);
    }

    private void fireRule(String enterpriseName, String requestingSystemName, UUID rulesId, String label, RuleWork work) {
        SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.StatelessSession s = tuple.getItem1();
            ISystems<?, ?> sys = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            return rulesService.find(s, rulesId).chain(rule -> {
                if (rule == null) return Uni.createFrom().voidItem();
                return work.apply(s, (Rules) rule, sys, token);
            });
        }), label);
    }

    private void fireRuleWithEnterprise(String enterpriseName, String requestingSystemName, UUID rulesId, String label, RuleWorkWithEnterprise work) {
        SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.StatelessSession s = tuple.getItem1();
            IEnterprise<?, ?> ent = tuple.getItem2();
            ISystems<?, ?> sys = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            return rulesService.find(s, rulesId).chain(rule -> {
                if (rule == null) return Uni.createFrom().voidItem();
                return work.apply(s, (Rules) rule, ent, sys, token);
            });
        }), label);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO-based response builders (no DB round-trip)
    // ──────────────────────────────────────────────────────────────────────────

    private RulesDTO buildCreateResponseFromDto(UUID rulesId, RulesCreateDTO dto) {
        RulesDTO response = new RulesDTO();
        response.rulesId = rulesId;
        response.name = dto.name;
        response.description = dto.description;
        response.classifications = copy(dto.classifications);
        response.resources = copy(dto.resources);
        response.products = copy(dto.products);
        response.ruleTypes = copy(dto.ruleTypes);
        response.children = copy(dto.children);
        return response;
    }

    private RulesDTO buildUpdateResponseFromDto(UUID rulesId, RulesUpdateDTO dto) {
        RulesDTO response = new RulesDTO();
        response.rulesId = rulesId;
        response.description = dto.description;
        response.classifications = addOrUpdateOf(dto.classifications);
        response.resources = addOrUpdateOf(dto.resources);
        response.products = addOrUpdateOf(dto.products);
        response.ruleTypes = addOrUpdateOf(dto.ruleTypes);
        response.children = addOrUpdateOf(dto.children);
        return response;
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

    private boolean hasEntries(RelationshipUpdateEntry entry) {
        if (entry == null) return false;
        return (entry.addOrUpdate != null && !entry.addOrUpdate.isEmpty())
                || (entry.delete != null && !entry.delete.isEmpty());
    }

    private boolean hasMap(Map<String, String> map) {
        return map != null && !map.isEmpty();
    }

    private boolean hasAnyRelationship(RulesCreateDTO dto) {
        return hasMap(dto.classifications) || hasMap(dto.resources) || hasMap(dto.products)
                || hasMap(dto.ruleTypes) || hasMap(dto.children);
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

