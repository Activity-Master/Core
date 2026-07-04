package com.guicedee.activitymaster.fsdm.rest.classification;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.rest.RelationshipUpdateEntry;
import com.guicedee.activitymaster.fsdm.client.services.rest.classifications.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification_;
import com.guicedee.client.IGuiceContext;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.entityassist.enumerations.Operand.Equals;

/**
 * REST surface for the ActivityMaster {@code Classification} domain — the reusable business values
 * held beneath a {@link com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept}.
 * <p>
 * Follows the FSDM REST conventions: create/update apply hierarchy relationships fire-and-forget
 * while the response echoes the submitted state immediately.
 */
@Path("{enterprise}/classification")
@Tag(name = "Classifications", description = "Classification value lifecycle — create, find and update classification values and their hierarchy.")
@Log4j2
public class ClassificationRestService {

    @Inject
    private IClassificationService<?> classificationService;

    // ──────────────────────────────────────────────────────────────────────────
    // Find
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/find")
    @Operation(summary = "Find a classification",
            description = "Returns a classification value by id, hydrating only the relationship categories named in the request's includes list.")
    @ApiResponse(responseCode = "200", description = "Classification found (relationships populated per includes)")
    @ApiResponse(responseCode = "500", description = "Lookup failure")
    public Uni<ClassificationDTO> find(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                       @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                       ClassificationFindDTO findDto) {
        UUID classificationId = findDto.classificationId;
        List<ClassificationDataIncludes> includesList = findDto.includes;
        return SessionUtils.<ClassificationDTO>withActivityMasterStateless(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.StatelessSession, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.StatelessSession session = tuple.getItem1();
                    IEnterprise<?, ?> enterprise = tuple.getItem2();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    // Prepped scalar projection (NOT session.get / an entity query): Classification is
                    // @Cacheable with an EAGER 'concept' (itself @Cacheable). On a stateless session loading
                    // the managed entity trips the reactive L2-cache assembly (CompletableFuture on 'concept')
                    // and an entity query trips the reactive load-context stack on the EAGER join. A lean
                    // projection avoids both; 'concept' is not returned on the stateless find.
                    return new Classification().builder(session)
                            .where(Classification_.id, Equals, classificationId)
                            .selectColumn(Classification_.id)
                            .selectColumn(Classification_.name)
                            .selectColumn(Classification_.description)
                            .selectColumn(Classification_.classificationSequenceNumber)
                            .get(Object[].class)
                            .onFailure(NoResultException.class).recoverWithNull()
                            .chain(row -> {
                                if (row == null) {
                                    return Uni.createFrom().failure(new NotFoundException("Classification not found: " + classificationId));
                                }
                                Classification classification = new Classification((UUID) row[0], (String) row[1], (String) row[2], ((Number) row[3]).intValue());
                                classification.setEnterpriseID(enterprise);
                                classification.setFake(false);
                                ClassificationDTO dto = new ClassificationDTO();
                                dto.classificationId = classificationId;
                                dto.name = (String) row[1];
                                dto.description = (String) row[2];
                                dto.sequenceNumber = ((Number) row[3]).intValue();

                                Uni<ClassificationDTO> chain = Uni.createFrom().item(dto);
                                if (includesList == null || includesList.isEmpty()) {
                                    return chain;
                                }
                                for (ClassificationDataIncludes include : includesList) {
                                    chain = chain.chain(d -> fetchInclude(session, classification, d, include, system, token));
                                }
                                return chain;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error finding classification {} for enterprise {} system {}: {}",
                                            classificationId, enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Create
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/create")
    @Operation(summary = "Create a classification",
            description = "Creates a classification value under the supplied concept (defaulting to NoClassification), optionally attaching it beneath a parent. Child hierarchy links persist asynchronously; the response echoes the submitted DTO immediately.")
    @ApiResponse(responseCode = "200", description = "Classification created; hierarchy persists asynchronously")
    @ApiResponse(responseCode = "500", description = "Creation failure")
    public Uni<ClassificationDTO> create(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                         @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                         ClassificationCreateDTO dto) {
        EnterpriseClassificationDataConcepts concept = toConcept(dto.concept);
        return SessionUtils.<ClassificationDTO>withActivityMasterStateless(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.StatelessSession, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.StatelessSession session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    int sequence = dto.sequenceNumber == null ? 1 : dto.sequenceNumber;
                    // Every classification must live under a parent in the hierarchy. When no parent is
                    // supplied, attach beneath the default root classification (NoClassification).
                    String parentName = (dto.parentName == null || dto.parentName.isBlank())
                            ? DefaultClassifications.NoClassification.name()
                            : dto.parentName;
                    // No stateless overload accepts a parent *name*, so resolve (find-or-create) the
                    // parent classification first, then create the child beneath it via the stateless
                    // parent-aware overload — all on this same session/transaction.
                    return classificationService.create(session, parentName, parentName, system, token)
                            .chain(parent -> classificationService.create(session, dto.name, dto.description, concept, system, sequence, parent, token))
                            .chain(classification -> {
                                // Child hierarchy links depend on the just-created owner. They MUST be
                                // chained inside this same transaction — firing them on a separate session
                                // races (and can lose to) the create commit, leaving the owner invisible to
                                // that session's get()/find() so the child writes are silently dropped.
                                Uni<Void> children = (dto.children != null && !dto.children.isEmpty())
                                        ? persistChildren(session, system, token, (Classification) classification, dto.children, null)
                                        : Uni.createFrom().voidItem();
                                return children.map(ignored -> buildCreateResponseFromDto((Classification) classification, dto));
                            });
                }
        ).onFailure().invoke(e ->
                log.error("Error creating classification '{}' for enterprise {} and system {}: {}",
                        dto.name, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update
    // ──────────────────────────────────────────────────────────────────────────

    @PUT
    @Path("{requestingSystemName}/update")
    @Operation(summary = "Update a classification",
            description = "Updates the classification's description/sequence in place and applies addOrUpdate (attach) and delete (archive) operations to its child hierarchy. Hierarchy persistence is fire-and-forget; the response echoes the intended state.")
    @ApiResponse(responseCode = "200", description = "Update accepted; hierarchy persists asynchronously")
    @ApiResponse(responseCode = "500", description = "Update failure")
    public Uni<ClassificationDTO> update(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                         @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                         ClassificationUpdateDTO dto) {
        UUID classificationId = dto.classificationId;
        return SessionUtils.<UUID>withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.StatelessSession session = tuple.getItem1();
            // A targeted criteria UPDATE (only the changed columns) — NOT a load + session.update.
            // Classification is @Cacheable with an EAGER 'concept' (itself @Cacheable): loading the managed
            // entity on a stateless session trips the reactive L2-cache assembly / load-context stack, and a
            // full-row stateless update would also need the NOT-NULL 'concept' FK. A criteria UPDATE writes
            // only the set columns and never loads the entity. A lean existence check first yields a clean 404.
            return new Classification().builder(session)
                    .where(Classification_.id, Equals, classificationId)
                    .selectColumn(Classification_.id)
                    .get(UUID.class)
                    .onFailure(NoResultException.class).recoverWithNull()
                    .chain(existingId -> {
                        if (existingId == null) {
                            return Uni.createFrom().failure(new NotFoundException("Classification not found: " + classificationId));
                        }
                        CriteriaBuilder cb = IGuiceContext.get(Mutiny.SessionFactory.class).getCriteriaBuilder();
                        CriteriaUpdate<Classification> cu = cb.createCriteriaUpdate(Classification.class);
                        Root<Classification> root = cu.from(Classification.class);
                        boolean anyChange = false;
                        if (dto.description != null) {
                            cu.set(Classification_.description, dto.description);
                            anyChange = true;
                        }
                        if (dto.sequenceNumber != null) {
                            cu.set(Classification_.classificationSequenceNumber, dto.sequenceNumber);
                            anyChange = true;
                        }
                        if (!anyChange) {
                            return Uni.createFrom().item(classificationId);
                        }
                        cu.where(cb.equal(root.get(Classification_.id), classificationId));
                        return session.createQuery(cu).executeUpdate().replaceWith(classificationId);
                    });
        }).map(foundId -> {
            if (hasEntries(dto.children)) {
                persistChildrenAsync(enterpriseName, requestingSystemName, foundId,
                        dto.children.addOrUpdate, dto.children.delete);
            }
            return buildUpdateResponseFromDto(classificationId, dto);
        }).onFailure().invoke(e ->
                log.error("Error updating classification {} for enterprise {} and system {}: {}",
                        classificationId, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Include fetching
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<ClassificationDTO> fetchInclude(Mutiny.StatelessSession session, Classification classification,
                                                ClassificationDTO dto, ClassificationDataIncludes include,
                                                ISystems<?, ?> system, UUID[] token) {
        return switch (include) {
            case Children -> classification.findChildren(session, (String) null, null, system, token)
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
    // Fire-and-forget hierarchy persistence
    // ──────────────────────────────────────────────────────────────────────────

    private void persistChildrenAsync(String enterpriseName, String requestingSystemName,
                                      UUID classificationId, Map<String, String> addOrUpdate, List<String> delete) {
        String label = "classification " + classificationId;
        SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.StatelessSession s = tuple.getItem1();
            ISystems<?, ?> sys = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            return s.get(Classification.class, classificationId).chain(owner -> {
                if (owner == null) {
                    return Uni.createFrom().voidItem();
                }
                return persistChildren(s, sys, token, owner, addOrUpdate, delete);
            });
        }), label + " children");
    }

    /**
     * Chains the child hierarchy add/archive operations onto the caller's session, returning a
     * {@link Uni} that completes when every write has been applied. Shared by the create path (run
     * in-transaction, since the owner is freshly created) and the update path (an already-committed
     * owner, dispatched fire-and-forget).
     */
    private Uni<Void> persistChildren(Mutiny.StatelessSession s, ISystems<?, ?> sys, UUID[] token,
                                      Classification owner, Map<String, String> addOrUpdate, List<String> delete) {
        Uni<Void> chain = Uni.createFrom().voidItem();
        if (addOrUpdate != null) {
            for (var entry : addOrUpdate.entrySet()) {
                chain = chain.chain(() -> classificationService.find(s, entry.getKey(), sys, token)
                        .chain(child -> owner.addChild(s, (Classification) child, null, entry.getValue(), sys, token))
                        .replaceWithVoid());
            }
        }
        if (delete != null) {
            for (String childName : delete) {
                chain = chain.chain(() -> classificationService.find(s, childName, sys, token)
                        .chain(child -> owner.archiveChild(s, (Classification) child, null, null, sys, token))
                        .replaceWithVoid());
            }
        }
        return chain;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO-based response builders (no DB round-trip)
    // ──────────────────────────────────────────────────────────────────────────

    private ClassificationDTO buildCreateResponseFromDto(Classification classification, ClassificationCreateDTO dto) {
        ClassificationDTO response = new ClassificationDTO();
        response.classificationId = classification.getId();
        response.name = dto.name;
        response.description = dto.description;
        response.sequenceNumber = dto.sequenceNumber;
        response.concept = dto.concept;
        response.children = dto.children != null ? new LinkedHashMap<>(dto.children) : null;
        return response;
    }

    private ClassificationDTO buildUpdateResponseFromDto(UUID classificationId, ClassificationUpdateDTO dto) {
        ClassificationDTO response = new ClassificationDTO();
        response.classificationId = classificationId;
        response.description = dto.description;
        response.sequenceNumber = dto.sequenceNumber;
        if (dto.children != null && dto.children.addOrUpdate != null) {
            response.children = new LinkedHashMap<>(dto.children.addOrUpdate);
        }
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────────────

    private boolean hasEntries(RelationshipUpdateEntry entry) {
        if (entry == null) return false;
        return (entry.addOrUpdate != null && !entry.addOrUpdate.isEmpty())
                || (entry.delete != null && !entry.delete.isEmpty());
    }

    /**
     * Resolves a concept name to a known {@link EnterpriseClassificationDataConcepts} value by either
     * enum name or its classification value. Returns {@code null} (defaulting to NoClassification) when
     * the name is blank or unknown.
     */
    private EnterpriseClassificationDataConcepts toConcept(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        for (EnterpriseClassificationDataConcepts c : EnterpriseClassificationDataConcepts.values()) {
            if (c.name().equalsIgnoreCase(name) || c.classificationValue().equalsIgnoreCase(name)) {
                return c;
            }
        }
        log.warn("Unknown classification data concept '{}', defaulting to NoClassification", name);
        return null;
    }
}

