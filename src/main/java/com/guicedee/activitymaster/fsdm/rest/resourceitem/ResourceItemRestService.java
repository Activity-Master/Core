package com.guicedee.activitymaster.fsdm.rest.resourceitem;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.rest.resourceitems.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;

import com.guicedee.activitymaster.fsdm.client.services.rest.RelationshipUpdateEntry;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import static com.entityassist.enumerations.Operand.Equals;

@Path("{enterprise}/resource-item")
@Log4j2
public class ResourceItemRestService
{

    // ──────────────────────────────────────────────────────────────────────────
    // Service
    // ──────────────────────────────────────────────────────────────────────────

    @Inject
    private IResourceItemService<ResourceItemService> resourceItemService;

    // ──────────────────────────────────────────────────────────────────────────
    // Find
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/find")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ResourceItemDTO> find(@PathParam("enterprise") String enterpriseName,
                                     @PathParam("requestingSystemName") String requestingSystemName,
                                     ResourceItemFindDTO findDto) {
        UUID resourceItemId = findDto.resourceItemId;
        List<ResourceItemDataIncludes> includesList = findDto.includes;
        return SessionUtils.<ResourceItemDTO>withActivityMaster(enterpriseName, requestingSystemName,
            (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                Mutiny.Session session = tuple.getItem1();
                return resourceItemService.findByUUID(session, resourceItemId)
                    .map(resourceItem -> {
                        ResourceItemDTO dto = new ResourceItemDTO();
                        dto.resourceItemId = resourceItemId;
                        return dto;
                    })
                    .chain(dto -> {
                        ResourceItem ri = new ResourceItem();
                        ri.setId(resourceItemId);

                        Uni<ResourceItemDTO> chain = Uni.createFrom().item(dto);
                        if (includesList == null || includesList.isEmpty()) {
                            return chain;
                        }
                        for (ResourceItemDataIncludes include : includesList) {
                            chain = chain.chain(d -> fetchInclude(session, ri, d, include));
                        }
                        return chain;
                    })
                    .onFailure().invoke(e ->
                        log.error("Error finding resource item {} for enterprise {} system {}: {}",
                                resourceItemId, enterpriseName, requestingSystemName, e.getMessage(), e)
                    );
            }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Search by classification
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<ResourceItemDTO>> search(@PathParam("enterprise") String enterpriseName,
                                              @PathParam("requestingSystemName") String requestingSystemName,
                                              ResourceItemSearchDTO searchDto) {
        return SessionUtils.<List<ResourceItemDTO>>withActivityMaster(enterpriseName, requestingSystemName,
            (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                Mutiny.Session session = tuple.getItem1();
                ISystems<?, ?> system = tuple.getItem3();
                UUID[] identityToken = tuple.getItem4();

                return resourceItemService.findByClassificationAll(session,
                        searchDto.resourceItemType,
                        searchDto.classificationName,
                        searchDto.classificationValue,
                        system, identityToken)
                    .chain(results -> {
                        // Apply sorting if requested
                        if (searchDto.sortDirection != null) {
                            SearchSortField field = searchDto.sortField != null
                                    ? searchDto.sortField : SearchSortField.EFFECTIVE_FROM_DATE;
                            Comparator<Object> comparator = Comparator.comparing(o -> {
                                ResourceItemXClassification r = (ResourceItemXClassification) (Object) o;
                                return switch (field) {
                                    case WAREHOUSE_CREATED_TIMESTAMP -> r.getWarehouseCreatedTimestamp();
                                    case EFFECTIVE_FROM_DATE -> r.getEffectiveFromDate();
                                };
                            }, Comparator.nullsLast(Comparator.naturalOrder()));
                            if (searchDto.sortDirection == SortDirection.DESC) {
                                comparator = comparator.reversed();
                            }
                            results.sort(comparator);
                        }

                        // Apply maxResults limit if requested
                        List<?> limitedResults = (searchDto.maxResults != null && searchDto.maxResults > 0 && results.size() > searchDto.maxResults)
                                ? results.subList(0, searchDto.maxResults)
                                : results;

                        List<ResourceItemDataIncludes> includesList = searchDto.includes;
                        Uni<List<ResourceItemDTO>> listUni = Uni.createFrom().item(new ArrayList<>());

                        for (var relValue : limitedResults) {
                            listUni = listUni.chain(dtoList -> {
                                ResourceItemXClassification rixc = (ResourceItemXClassification) (Object) relValue;
                                return session.fetch(rixc.getResourceItemID())
                                    .chain(fetchedItem -> {
                                        ResourceItemDTO dto = new ResourceItemDTO();
                                        dto.resourceItemId = fetchedItem.getId();

                                        if (includesList == null || includesList.isEmpty()) {
                                            dtoList.add(dto);
                                            return Uni.createFrom().item(dtoList);
                                        }

                                        Uni<ResourceItemDTO> fetchChain = Uni.createFrom().item(dto);
                                        for (ResourceItemDataIncludes include : includesList) {
                                            fetchChain = fetchChain.chain(d -> fetchInclude(session, fetchedItem, d, include));
                                        }
                                        return fetchChain.map(d -> { dtoList.add(d); return dtoList; });
                                    });
                            });
                        }
                        return listUni;
                    })
                    .onFailure().invoke(e ->
                        log.error("Error searching resource items for enterprise {} system {}: {}",
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ResourceItemDTO> create(@PathParam("enterprise") String enterpriseName,
                                       @PathParam("requestingSystemName") String requestingSystemName,
                                       ResourceItemCreateDTO dto) {
        // Step 1: Resolve system, then create — ResourceItemService.create() manages its own session
        return SessionUtils.<ISystems<?,?>>withActivityMaster(enterpriseName, requestingSystemName,
            (java.util.function.Function<Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]>, Uni<ISystems<?,?>>>) tuple ->
                Uni.createFrom().item(tuple.getItem3())
        ).chain(system -> {
            Uni<IResourceItem<?, ?>> createUni;
            if (dto.data != null && dto.data.length > 0)
            {
                createUni = resourceItemService.create(null, dto.type,dto.key, dto.dataValue, dto.data, system);
            }
            else
            {
                createUni = resourceItemService.create(null, dto.type,dto.key, dto.dataValue, system);
            }

            return createUni.map(resourceItem -> {
                // Extract the ID before the session closes — the entity will be detached
                // after withActivityMaster's transaction completes
                UUID resourceItemId = resourceItem.getId();

                boolean hasRelationships = (dto.classifications != null && !dto.classifications.isEmpty())
                        || (dto.types != null && !dto.types.isEmpty())
                        || (dto.children != null && !dto.children.isEmpty());

                if (hasRelationships)
                {
                    // Pass the ID, not the detached entity — each fire-and-forget will
                    // re-find the entity in its own session
                    persistCreateRelationshipsAsync(enterpriseName, requestingSystemName, resourceItemId, dto);
                }

                return buildCreateResponseFromDto(resourceItem, dto);
            })
            .onFailure().invoke(e ->
                log.error("Error creating resource item for enterprise {} and system {}: {}",
                        enterpriseName, requestingSystemName, e.getMessage(), e)
            );
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update
    // ──────────────────────────────────────────────────────────────────────────

    @PUT
    @Path("{requestingSystemName}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ResourceItemDTO> update(@PathParam("enterprise") String enterpriseName,
                                       @PathParam("requestingSystemName") String requestingSystemName,
                                       ResourceItemUpdateDTO dto) {
        UUID resourceItemId = dto.resourceItemId;
        // Step 1: Find the resource item in its own session (just to validate it exists)
        return SessionUtils.<UUID>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            return resourceItemService.findByUUID(session, resourceItemId)
                    .map(resourceItem -> resourceItem.getId());
        }).map(foundId -> {
            // Step 2: Fire-and-forget relationship persistence — each gets its own session
            // and re-finds the entity by ID
            persistUpdateRelationshipsAsync(enterpriseName, requestingSystemName, foundId, dto);

            // Step 3: Build response immediately from the DTO input
            return buildUpdateResponseFromDto(resourceItemId, dto);
        }).onFailure().invoke(e ->
                log.error("Error updating resource item {} for enterprise {} and system {}: {}",
                        resourceItemId, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update Data
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Updates the binary data of an existing resource item.
     * Only the data payload is replaced — no metadata, classifications, or relationships are affected.
     */
    @PATCH
    @Path("{requestingSystemName}/data")
    public Uni<ResourceItemDTO> updateData(@PathParam("enterprise") String enterpriseName,
                                            @PathParam("requestingSystemName") String requestingSystemName,
                                            ResourceItemUpdateDataDTO dto) {
        UUID resourceItemId = dto.resourceItemId;
        return SessionUtils.<ResourceItemDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();

            return resourceItemService.updateResourceData(session, dto.data, resourceItemId, requestingSystemName)
                    .chain(() -> {
                        ResourceItemDTO response = new ResourceItemDTO();
                        response.resourceItemId = resourceItemId;
                        return Uni.createFrom().item(response);
                    })
                    .onFailure().invoke(e ->
                        log.error("Error updating resource item data {} for enterprise {} and system {}: {}",
                                resourceItemId, enterpriseName, requestingSystemName, e.getMessage(), e)
                    );
        });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Get Data
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Returns the binary data of a resource item as an octet-stream.
     */
    @GET
    @Path("{requestingSystemName}/data/{resourceItemId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Uni<byte[]> getData(@PathParam("enterprise") String enterpriseName,
                               @PathParam("requestingSystemName") String requestingSystemName,
                               @PathParam("resourceItemId") UUID resourceItemId) {
        return SessionUtils.<byte[]>withActivityMaster(enterpriseName, requestingSystemName,
            (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                Mutiny.Session session = tuple.getItem1();
                UUID[] identityToken = tuple.getItem4();

                return resourceItemService.findByUUID(session, resourceItemId)
                        .chain(resourceItem -> ((ResourceItem) resourceItem).getData(session, identityToken))
                        .onFailure(NoResultException.class).recoverWithItem(()->null)
                        .onFailure().invoke(e ->
                                log.error("Error getting data for resource item {} for enterprise {} system {}: {}",
                                        resourceItemId, enterpriseName, requestingSystemName, e.getMessage(), e)
                        );
            }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Include fetching
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<ResourceItemDTO> fetchInclude(Mutiny.Session session, ResourceItem resourceItem, ResourceItemDTO dto, ResourceItemDataIncludes include) {
        return switch (include) {
            case Types -> new ResourceItemXResourceItemType().builder(session)
                    .where(ResourceItemXResourceItemType_.resourceItemID, Equals, resourceItem)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ResourceItemXResourceItemType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getResourceItemTypeID())
                                    .invoke(type -> {
                                        String key = type != null && type.getName() != null ? type.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.types = map; return dto; });
                    });

            case Classifications -> new ResourceItemXClassification().builder(session)
                    .where(ResourceItemXClassification_.resourceItemID, Equals, resourceItem)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ResourceItemXClassification link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getClassificationID())
                                    .invoke(classification -> {
                                        String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.classifications = map; return dto; });
                    });

            case Children -> new ResourceItemXResourceItem().builder(session)
                    .where(ResourceItemXResourceItem_.parentResourceItemID, Equals, resourceItem)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ResourceItemXResourceItem link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getChildResourceItemID())
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
                                                  UUID resourceItemId, ResourceItemCreateDTO dto) {
        String label = "resource item " + resourceItemId;

        if (dto.classifications != null && !dto.classifications.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return resourceItemService.findByUUID(s, resourceItemId).chain(resourceItem -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.classifications.entrySet()) {
                        chain = chain.chain(() -> resourceItem.addOrUpdateClassification(s, entry.getKey(), entry.getValue(), sys, token).replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " classifications");
        }

        if (dto.types != null && !dto.types.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return resourceItemService.findByUUID(s, resourceItemId).chain(resourceItem -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.types.entrySet()) {
                        chain = chain.chain(() -> resourceItem.addOrUpdateResourceItemTypes(s, entry.getKey(), null, null, entry.getValue(), sys, token).replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " types");
        }

        // Children: Key = classification name (mandatory), Value = child resource item UUID
        if (dto.children != null && !dto.children.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return resourceItemService.findByUUID(s, resourceItemId).chain(resourceItem -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.children.entrySet()) {
                        String classificationName = entry.getKey();
                        UUID childId = parseUuidOrNull(entry.getValue(), label + " children");
                        if (childId == null) continue;
                        chain = chain.chain(() -> resourceItemService.findByUUID(s, childId)
                                .chain(child -> resourceItem.addChild(s, (ResourceItem) child, classificationName, null, sys, token).replaceWithVoid()));
                    }
                    return chain;
                });
            }), label + " children");
        }
    }

    private void persistUpdateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                  UUID resourceItemId, ResourceItemUpdateDTO dto) {
        String label = "resource item " + resourceItemId;

        if (hasEntries(dto.classifications)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return resourceItemService.findByUUID(s, resourceItemId).chain(resourceItem -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.classifications, (name, value) ->
                            resourceItem.addOrUpdateClassification(s, name, value, sys, token).replaceWithVoid());
                    chain = chainDelete(chain, dto.classifications, name ->
                            resourceItem.removeClassification(s, name, null, sys, token).replaceWithVoid());
                    return chain;
                });
            }), label + " classifications");
        }

        if (hasEntries(dto.types)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return resourceItemService.findByUUID(s, resourceItemId).chain(resourceItem -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.types, (name, value) ->
                            resourceItem.addOrUpdateResourceItemTypes(s, name, null, null, value, sys, token).replaceWithVoid());
                    chain = chainDelete(chain, dto.types, name ->
                            resourceItem.removeResourceItemTypes(s, name, null, null, null, sys, token).replaceWithVoid());
                    return chain;
                });
            }), label + " types");
        }

        if (hasEntries(dto.children)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.Session s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return resourceItemService.findByUUID(s, resourceItemId).chain(resourceItem -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    if (dto.children.addOrUpdate != null) {
                        for (var e : dto.children.addOrUpdate.entrySet()) {
                            String classificationName = e.getKey();
                            UUID childId = parseUuidOrNull(e.getValue(), label + " children addOrUpdate");
                            if (childId == null) continue;
                            chain = chain.chain(() -> resourceItemService.findByUUID(s, childId)
                                    .chain(child -> resourceItem.addChild(s, (ResourceItem) child, classificationName, null, sys, token).replaceWithVoid()));
                        }
                    }
                    chain = chainDeleteByExpire(chain, dto.children, s, resourceItem);
                    return chain;
                });
            }), label + " children");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO-based response builders (no DB round-trip)
    // ──────────────────────────────────────────────────────────────────────────

    private ResourceItemDTO buildCreateResponseFromDto(IResourceItem<?, ?> resourceItem, ResourceItemCreateDTO dto) {
        ResourceItemDTO response = new ResourceItemDTO();
        response.resourceItemId = resourceItem.getId();
        response.types = dto.types != null ? new LinkedHashMap<>(dto.types) : null;
        response.classifications = dto.classifications != null ? new LinkedHashMap<>(dto.classifications) : null;
        response.children = dto.children != null ? new LinkedHashMap<>(dto.children) : null;
        return response;
    }

    private ResourceItemDTO buildUpdateResponseFromDto(UUID resourceItemId, ResourceItemUpdateDTO dto) {
        ResourceItemDTO response = new ResourceItemDTO();
        response.resourceItemId = resourceItemId;
        if (dto.classifications != null && dto.classifications.addOrUpdate != null) response.classifications = new LinkedHashMap<>(dto.classifications.addOrUpdate);
        if (dto.types != null && dto.types.addOrUpdate != null) response.types = new LinkedHashMap<>(dto.types.addOrUpdate);
        if (dto.children != null && dto.children.addOrUpdate != null) response.children = new LinkedHashMap<>(dto.children.addOrUpdate);
        return response;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Response builders (DB-based — retained for find)
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<ResourceItemDTO> buildCreateResponse(String enterpriseName, String requestingSystemName,
                                                      IResourceItem<?, ?> resourceItem, ResourceItemCreateDTO createDto) {
        return SessionUtils.<ResourceItemDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            ResourceItem ri = (ResourceItem) resourceItem;

            ResourceItemDTO response = new ResourceItemDTO();
            response.resourceItemId = ri.getId();

            List<ResourceItemDataIncludes> includes = new ArrayList<>();
            includes.add(ResourceItemDataIncludes.Types);
            includes.add(ResourceItemDataIncludes.Classifications);
            if (createDto.children != null && !createDto.children.isEmpty())
                includes.add(ResourceItemDataIncludes.Children);

            Uni<ResourceItemDTO> fetchChain = Uni.createFrom().item(response);
            for (ResourceItemDataIncludes include : includes)
            {
                fetchChain = fetchChain.chain(d -> fetchInclude(session, ri, d, include));
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
        for (var e : entry.addOrUpdate.entrySet())
        {
            chain = chain.chain(() -> addOrUpdateFn.apply(e.getKey(), e.getValue()));
        }
        return chain;
    }

    private Uni<Void> chainDelete(Uni<Void> chain, RelationshipUpdateEntry entry,
                                   java.util.function.Function<String, Uni<Void>> deleteFn) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;
        for (String name : entry.delete)
        {
            chain = chain.chain(() -> deleteFn.apply(name));
        }
        return chain;
    }

    /**
     * Expires child resource item links by querying the hierarchy table and matching child UUIDs.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Uni<Void> chainDeleteByExpire(Uni<Void> chain, RelationshipUpdateEntry entry,
                                           Mutiny.Session session, IResourceItem<?, ?> resourceItem) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;
        Set<String> idsToDelete = new HashSet<>(entry.delete);
        ResourceItem ri = (ResourceItem) resourceItem;

        return chain.chain(() ->
            new ResourceItemXResourceItem().builder(session)
                    .where(ResourceItemXResourceItem_.parentResourceItemID, Equals, ri)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Uni<Void> expireChain = Uni.createFrom().voidItem();
                        for (ResourceItemXResourceItem link : (List<ResourceItemXResourceItem>) list)
                        {
                            expireChain = expireChain.chain(() ->
                                    session.fetch(link.getChildResourceItemID())
                                            .chain(child -> {
                                                String childId = child != null && child.getId() != null ? child.getId().toString() : null;
                                                if (childId != null && idsToDelete.contains(childId))
                                                {
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

    private List<ResourceItemDataIncludes> determineIncludes(ResourceItemUpdateDTO dto) {
        List<ResourceItemDataIncludes> includes = new ArrayList<>();
        //if (hasEntries(dto.classifications)) includes.add(ResourceItemDataIncludes.Classifications);
        includes.add(ResourceItemDataIncludes.Classifications);

        if (hasEntries(dto.types))           includes.add(ResourceItemDataIncludes.Types);
        if (hasEntries(dto.children))        includes.add(ResourceItemDataIncludes.Children);
        if (includes.isEmpty())
        {
            includes.add(ResourceItemDataIncludes.Types);
            includes.add(ResourceItemDataIncludes.Classifications);
        }
        return includes;
    }

    private boolean hasEntries(RelationshipUpdateEntry entry) {
        if (entry == null) return false;
        return (entry.addOrUpdate != null && !entry.addOrUpdate.isEmpty())
                || (entry.delete != null && !entry.delete.isEmpty());
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