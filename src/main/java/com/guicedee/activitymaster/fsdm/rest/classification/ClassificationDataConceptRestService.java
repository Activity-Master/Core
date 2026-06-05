package com.guicedee.activitymaster.fsdm.rest.classification;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.rest.RelationshipUpdateEntry;
import com.guicedee.activitymaster.fsdm.client.services.rest.classifications.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConceptXResourceItem_;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification_;

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
 * REST surface for the ActivityMaster {@code ClassificationDataConcept} domain — the reusable
 * buckets/schemes that group classification values.
 * <p>
 * Concept names must resolve to a known {@link EnterpriseClassificationDataConcepts} value. Create and
 * update apply resource-item relationships fire-and-forget while the response echoes the submitted state.
 */
@Path("{enterprise}/classification-data-concept")
@Tag(name = "Classification Data Concepts", description = "Classification data concept (bucket/scheme) lifecycle — create, find and update concepts, their values and attached resources.")
@Log4j2
public class ClassificationDataConceptRestService {

    @Inject
    private IClassificationDataConceptService<?> dataConceptService;

    @Inject
    private IResourceItemService<?> resourceItemService;

    // ──────────────────────────────────────────────────────────────────────────
    // Find
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/find")
    @Operation(summary = "Find a classification data concept",
            description = "Returns a concept by id, hydrating only the relationship categories named in the request's includes list.")
    @ApiResponse(responseCode = "200", description = "Concept found (relationships populated per includes)")
    @ApiResponse(responseCode = "500", description = "Lookup failure")
    public Uni<ClassificationDataConceptDTO> find(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                                  @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                                  ClassificationDataConceptFindDTO findDto) {
        UUID conceptId = findDto.conceptId;
        List<ClassificationDataConceptDataIncludes> includesList = findDto.includes;
        return SessionUtils.<ClassificationDataConceptDTO>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    IEnterprise<?, ?> enterprise = tuple.getItem2();
                    return session.find(ClassificationDataConcept.class, conceptId)
                            .chain(concept -> {
                                if (concept == null) {
                                    return Uni.createFrom().failure(new NotFoundException("Classification data concept not found: " + conceptId));
                                }
                                ClassificationDataConceptDTO dto = new ClassificationDataConceptDTO();
                                dto.conceptId = conceptId;
                                dto.name = concept.getName();
                                dto.description = concept.getDescription();

                                Uni<ClassificationDataConceptDTO> chain = Uni.createFrom().item(dto);
                                if (includesList == null || includesList.isEmpty()) {
                                    return chain;
                                }
                                for (ClassificationDataConceptDataIncludes include : includesList) {
                                    chain = chain.chain(d -> fetchInclude(session, concept, d, include, enterprise));
                                }
                                return chain;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error finding classification data concept {} for enterprise {} system {}: {}",
                                            conceptId, enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Create
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/create")
    @Operation(summary = "Create a classification data concept",
            description = "Creates a concept identified by a known EnterpriseClassificationDataConcepts name. Attached resource items persist asynchronously; the response echoes the submitted DTO immediately.")
    @ApiResponse(responseCode = "200", description = "Concept created; resources persist asynchronously")
    @ApiResponse(responseCode = "400", description = "Unknown concept name")
    @ApiResponse(responseCode = "500", description = "Creation failure")
    public Uni<ClassificationDataConceptDTO> create(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                                    @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                                    ClassificationDataConceptCreateDTO dto) {
        EnterpriseClassificationDataConcepts concept = toConcept(dto.name);
        if (concept == null) {
            return Uni.createFrom().failure(new BadRequestException("Unknown classification data concept name: " + dto.name));
        }
        return SessionUtils.<ClassificationDataConceptDTO>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return dataConceptService.createDataConcept(session, concept, dto.description, system, token)
                            .map(created -> {
                                UUID conceptId = created.getId();
                                if (dto.resources != null && !dto.resources.isEmpty()) {
                                    persistResourcesAsync(enterpriseName, requestingSystemName, conceptId, dto.resources, null);
                                }
                                return buildCreateResponseFromDto(conceptId, dto);
                            });
                }
        ).onFailure().invoke(e ->
                log.error("Error creating classification data concept '{}' for enterprise {} and system {}: {}",
                        dto.name, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update
    // ──────────────────────────────────────────────────────────────────────────

    @PUT
    @Path("{requestingSystemName}/update")
    @Operation(summary = "Update a classification data concept",
            description = "Updates the concept's description in place and applies addOrUpdate (upsert) and delete (expire) operations to its attached resource items. Resource persistence is fire-and-forget; the response echoes the intended state.")
    @ApiResponse(responseCode = "200", description = "Update accepted; resources persist asynchronously")
    @ApiResponse(responseCode = "500", description = "Update failure")
    public Uni<ClassificationDataConceptDTO> update(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                                    @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                                    ClassificationDataConceptUpdateDTO dto) {
        UUID conceptId = dto.conceptId;
        return SessionUtils.<UUID>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            return session.find(ClassificationDataConcept.class, conceptId)
                    .chain(concept -> {
                        if (concept == null) {
                            return Uni.createFrom().failure(new NotFoundException("Classification data concept not found: " + conceptId));
                        }
                        if (dto.description != null) {
                            concept.setDescription(dto.description);
                        }
                        return Uni.createFrom().item(concept.getId());
                    });
        }).map(foundId -> {
            if (hasEntries(dto.resources)) {
                persistResourcesAsync(enterpriseName, requestingSystemName, foundId,
                        dto.resources.addOrUpdate, dto.resources.delete);
            }
            return buildUpdateResponseFromDto(conceptId, dto);
        }).onFailure().invoke(e ->
                log.error("Error updating classification data concept {} for enterprise {} and system {}: {}",
                        conceptId, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Include fetching
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<ClassificationDataConceptDTO> fetchInclude(Mutiny.Session session, ClassificationDataConcept concept,
                                                           ClassificationDataConceptDTO dto,
                                                           ClassificationDataConceptDataIncludes include, IEnterprise<?, ?> enterprise) {
        return switch (include) {
            case Classifications -> new Classification().builder(session)
                    .withEnterprise(enterprise)
                    .where(Classification_.concept, Equals, concept)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .map(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        for (Classification c : list) {
                            map.put(c.getName(), c.getDescription());
                        }
                        dto.classifications = map;
                        return dto;
                    });

            case Resources -> new ClassificationDataConceptXResourceItem().builder(session)
                    .where(ClassificationDataConceptXResourceItem_.classificationDataConceptID, Equals, concept)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ClassificationDataConceptXResourceItem link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .chain(classification -> session.fetch(link.getResourceItemID())
                                            .invoke(resource -> {
                                                String key = classification != null && classification.getName() != null
                                                        ? classification.getName() : String.valueOf(link.getId());
                                                String value = resource != null && resource.getId() != null
                                                        ? resource.getId().toString() : link.getValue();
                                                map.put(key, value);
                                            })).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.resources = map; return dto; });
                    });
        };
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Fire-and-forget resource persistence
    // ──────────────────────────────────────────────────────────────────────────

    private void persistResourcesAsync(String enterpriseName, String requestingSystemName,
                                       UUID conceptId, Map<String, String> addOrUpdate, List<String> delete) {
        String label = "classification data concept " + conceptId;
        SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session s = tuple.getItem1();
            ISystems<?, ?> sys = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            return s.find(ClassificationDataConcept.class, conceptId).chain(concept -> {
                if (concept == null) {
                    return Uni.createFrom().voidItem();
                }
                Uni<Void> chain = Uni.createFrom().voidItem();
                if (addOrUpdate != null) {
                    for (var entry : addOrUpdate.entrySet()) {
                        String classificationName = entry.getKey();
                        UUID riId = parseUuidOrNull(entry.getValue(), label + " resources addOrUpdate");
                        if (riId == null) continue;
                        chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                                .chain(ri -> concept.addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token))
                                .replaceWithVoid());
                    }
                }
                if (delete != null && !delete.isEmpty()) {
                    Set<String> namesToDelete = new HashSet<>(delete);
                    chain = chain.chain(() -> new ClassificationDataConceptXResourceItem().builder(s)
                            .where(ClassificationDataConceptXResourceItem_.classificationDataConceptID, Equals, concept)
                            .inActiveRange()
                            .inDateRange()
                            .getAll()
                            .chain(list -> {
                                Uni<Void> expireChain = Uni.createFrom().voidItem();
                                for (ClassificationDataConceptXResourceItem link : list) {
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
                return chain;
            });
        }), label + " resources");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO-based response builders (no DB round-trip)
    // ──────────────────────────────────────────────────────────────────────────

    private ClassificationDataConceptDTO buildCreateResponseFromDto(UUID conceptId, ClassificationDataConceptCreateDTO dto) {
        ClassificationDataConceptDTO response = new ClassificationDataConceptDTO();
        response.conceptId = conceptId;
        response.name = dto.name;
        response.description = dto.description;
        response.resources = dto.resources != null ? new LinkedHashMap<>(dto.resources) : null;
        return response;
    }

    private ClassificationDataConceptDTO buildUpdateResponseFromDto(UUID conceptId, ClassificationDataConceptUpdateDTO dto) {
        ClassificationDataConceptDTO response = new ClassificationDataConceptDTO();
        response.conceptId = conceptId;
        response.description = dto.description;
        if (dto.resources != null && dto.resources.addOrUpdate != null) {
            response.resources = new LinkedHashMap<>(dto.resources.addOrUpdate);
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

    private UUID parseUuidOrNull(String value, String context) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Skipping invalid UUID '{}' in {}", value, context);
            return null;
        }
    }

    private EnterpriseClassificationDataConcepts toConcept(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        for (EnterpriseClassificationDataConcepts c : EnterpriseClassificationDataConcepts.values()) {
            if (c.name().equalsIgnoreCase(name) || c.classificationValue().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }
}



