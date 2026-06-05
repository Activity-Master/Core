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

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

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
        return SessionUtils.<ClassificationDTO>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return session.find(Classification.class, classificationId)
                            .chain(classification -> {
                                if (classification == null) {
                                    return Uni.createFrom().failure(new NotFoundException("Classification not found: " + classificationId));
                                }
                                ClassificationDTO dto = new ClassificationDTO();
                                dto.classificationId = classificationId;
                                dto.name = classification.getName();
                                dto.description = classification.getDescription();
                                dto.sequenceNumber = classification.getClassificationSequenceNumber();
                                if (classification.getConcept() != null) {
                                    dto.concept = classification.getConcept().getName();
                                }

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
        return SessionUtils.<ClassificationDTO>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    int sequence = dto.sequenceNumber == null ? 1 : dto.sequenceNumber;
                    // Every classification must live under a parent in the hierarchy. When no parent is
                    // supplied, attach beneath the default root classification (NoClassification).
                    String parentName = (dto.parentName == null || dto.parentName.isBlank())
                            ? DefaultClassifications.NoClassification.name()
                            : dto.parentName;
                    return classificationService.create(session, dto.name, dto.description, concept, system, sequence, parentName, token)
                            .map(classification -> {
                                UUID classificationId = classification.getId();
                                if (dto.children != null && !dto.children.isEmpty()) {
                                    persistChildrenAsync(enterpriseName, requestingSystemName, classificationId, dto.children, null);
                                }
                                return buildCreateResponseFromDto((Classification) classification, dto);
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
        return SessionUtils.<UUID>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            return session.find(Classification.class, classificationId)
                    .chain(classification -> {
                        if (classification == null) {
                            return Uni.createFrom().failure(new NotFoundException("Classification not found: " + classificationId));
                        }
                        // Mutating the managed entity is flushed by the surrounding transaction.
                        if (dto.description != null) {
                            classification.setDescription(dto.description);
                        }
                        if (dto.sequenceNumber != null) {
                            classification.setClassificationSequenceNumber(dto.sequenceNumber);
                        }
                        return Uni.createFrom().item(classification.getId());
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

    private Uni<ClassificationDTO> fetchInclude(Mutiny.Session session, Classification classification,
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
        SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session s = tuple.getItem1();
            ISystems<?, ?> sys = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            return s.find(Classification.class, classificationId).chain(owner -> {
                if (owner == null) {
                    return Uni.createFrom().voidItem();
                }
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
            });
        }), label + " children");
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

