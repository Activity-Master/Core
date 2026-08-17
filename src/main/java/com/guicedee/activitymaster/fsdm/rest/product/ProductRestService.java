package com.guicedee.activitymaster.fsdm.rest.product;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.entityassist.services.entities.IRootEntity;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ProductService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.rest.RelationshipUpdateEntry;
import com.guicedee.activitymaster.fsdm.client.services.rest.products.*;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;

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

@Path("{enterprise}/product")
@Tag(name = "Products", description = "Product catalogue lifecycle — create, find, update and relationship management (types, classifications, resources).")
@Log4j2
public class ProductRestService {

    @Inject
    private IProductService<ProductService> productService;

    @Inject
    private IResourceItemService<?> resourceItemService;

    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private com.guicedee.activitymaster.fsdm.rest.EventActionSupport eventActionSupport;

    // ──────────────────────────────────────────────────────────────────────────
    // Find
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/find")
    @Operation(summary = "Find a product",
            description = "Returns a product by id, hydrating only the relationship categories named in the request's includes list.")
    @ApiResponse(responseCode = "200", description = "Product found (relationships populated per includes)")
    @ApiResponse(responseCode = "500", description = "Lookup failure")
    public Uni<ProductDTO> find(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                ProductFindDTO findDto) {
        UUID productId = findDto.productId;
        List<ProductDataIncludes> includesList = findDto.includes;
        return SessionUtils.<ProductDTO>withActivityMasterStateless(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.StatelessSession, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.StatelessSession session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return productService.find(session, productId)
                            .chain(product -> {
                                Product p = (Product) product;
                                ProductDTO dto = new ProductDTO();
                                dto.productId = productId;
                                dto.name = p.getName();
                                dto.description = p.getDescription();
                                dto.code = p.getProductCode();

                                Uni<ProductDTO> chain = Uni.createFrom().item(dto);
                                if (includesList == null || includesList.isEmpty()) {
                                    return chain;
                                }
                                for (ProductDataIncludes include : includesList) {
                                    chain = chain.chain(d -> fetchInclude(session, p, d, include, system, token));
                                }
                                return chain;
                            })
                            .onFailure().invoke(e ->
                                    log.error("Error finding product {} for enterprise {} system {}: {}",
                                            productId, enterpriseName, requestingSystemName, e.getMessage(), e)
                            );
                }
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Create
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/create")
    @Operation(summary = "Create a product",
            description = "Creates a product using the first entry in 'types' as the primary product type, then persists all supplied relationships asynchronously (fire-and-forget). The response echoes the submitted DTO immediately. Supply an optional 'event' block to associate this create with an event (records the action + a change summary).")
    @ApiResponse(responseCode = "200", description = "Product created; relationships persist asynchronously")
    @ApiResponse(responseCode = "500", description = "Creation failure")
    public Uni<ProductDTO> create(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                  @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                  ProductCreateDTO dto) {
        // Use the first entry in dto.types as the primary product type during creation
        Map.Entry<String, String> primaryType = dto.types.entrySet().iterator().next();
        // Holds the new product id so the fire-and-forget writes can be dispatched AFTER the create
        // transaction has committed — see below.
        AtomicReference<UUID> createdProductId = new AtomicReference<>();
        return SessionUtils.<ProductDTO>withActivityMasterStateless(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.StatelessSession, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.StatelessSession session = tuple.getItem1();
                    ISystems<?, ?> system = tuple.getItem3();
                    UUID[] token = tuple.getItem4();
                    return productService.createProduct(session, primaryType.getKey(),
                                    dto.name, dto.description, dto.code, system, token)
                            .map(product -> {
                                createdProductId.set(product.getId());
                                // Build response immediately from the DTO input — no DB round-trip needed
                                return buildCreateResponseFromDto((Product) product, dto);
                            });
                }
        ).onItem().invoke(response -> {
            // Each fire-and-forget write runs on its OWN session, so it may only be dispatched once THIS
            // create transaction has committed — otherwise its find(...) cannot see the still-uncommitted
            // product and fails with NoResultException, silently dropping the relationships.
            UUID productId = createdProductId.get();
            if (productId == null) {
                return;
            }
            if (hasAnyRelationship(dto)) {
                persistCreateRelationshipsAsync(enterpriseName, requestingSystemName, productId, dto);
            }
            eventActionSupport.recordProductAction(enterpriseName, requestingSystemName, dto.event, true, productId);
        }).onFailure().invoke(e ->
                log.error("Error creating product for enterprise {} and system {}: {}",
                        enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Update
    // ──────────────────────────────────────────────────────────────────────────

    @PUT
    @Path("{requestingSystemName}/update")
    @Operation(summary = "Update a product",
            description = "Applies addOrUpdate (upsert by name) and delete (expire by name) operations to each relationship category. Relationship persistence is fire-and-forget; the response echoes the intended addOrUpdate state. Supply an optional 'event' block to associate this update with an event (records the action + a change summary).")
    @ApiResponse(responseCode = "200", description = "Update accepted; relationships persist asynchronously")
    @ApiResponse(responseCode = "500", description = "Update failure")
    public Uni<ProductDTO> update(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                  @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                  ProductUpdateDTO dto) {
        UUID productId = dto.productId;
        // Step 1: Find the product in its own session (just to validate it exists)
        return SessionUtils.<UUID>withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.StatelessSession session = tuple.getItem1();
            return productService.find(session, productId).map(IRootEntity::getId);
        }).map(foundId -> {
            // Step 2: Fire-and-forget relationship persistence in parallel
            persistUpdateRelationshipsAsync(enterpriseName, requestingSystemName, foundId, dto);

            // Optionally associate this update with an event (fire-and-forget)
            eventActionSupport.recordProductAction(enterpriseName, requestingSystemName, dto.event, false, foundId);

            // Step 3: Build response immediately from the DTO input — no DB round-trip needed
            return buildUpdateResponseFromDto(productId, dto);
        }).onFailure().invoke(e ->
                log.error("Error updating product {} for enterprise {} and system {}: {}",
                        productId, enterpriseName, requestingSystemName, e.getMessage(), e)
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Include fetching — all use session.fetch() for lazy-loaded entities
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<ProductDTO> fetchInclude(Mutiny.StatelessSession session, Product product, ProductDTO dto, ProductDataIncludes include,
                                         ISystems<?, ?> system, UUID[] token) {
        return switch (include) {
            case Types -> new ProductXProductType().builder(session)
                    .where(ProductXProductType_.productID, Equals, product)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ProductXProductType link : list) {
                            fetchChain = fetchChain.chain(() -> session.fetch(link.getProductTypeID())
                                    .invoke(type -> {
                                        String key = type != null && type.getName() != null ? type.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.types = map; return dto; });
                    });

            case Classifications -> new ProductXClassification().builder(session)
                    .where(ProductXClassification_.productID, Equals, product)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ProductXClassification link : list) {
                            fetchChain = fetchChain.chain(() -> classificationService.find(session, link.getClassificationID().getId(), system, token)
                                    .invoke(classification -> {
                                        String key = classification != null && classification.getName() != null ? classification.getName() : String.valueOf(link.getId());
                                        map.put(key, link.getValue());
                                    }).replaceWithVoid());
                        }
                        return fetchChain.replaceWith(() -> { dto.classifications = map; return dto; });
                    });

            case Resources -> new ProductXResourceItem().builder(session)
                    .where(ProductXResourceItem_.productID, Equals, product)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(list -> {
                        Map<String, String> map = new LinkedHashMap<>();
                        Uni<Void> fetchChain = Uni.createFrom().voidItem();
                        for (ProductXResourceItem link : list) {
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
                                                  UUID productId, ProductCreateDTO dto) {
        String label = "product " + productId;

        if (dto.classifications != null && !dto.classifications.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.StatelessSession s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return productService.find(s, productId).chain(product -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.classifications.entrySet()) {
                        chain = chain.chain(() -> product.addOrUpdateClassification(s, entry.getKey(), entry.getValue(), sys, token).replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " classifications");
        }

        if (dto.types != null && !dto.types.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.StatelessSession s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return productService.find(s, productId).chain(product -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.types.entrySet()) {
                        chain = chain.chain(() -> product.addOrUpdateProductTypes(s, entry.getKey(), null, null, entry.getValue(), sys, token).replaceWithVoid());
                    }
                    return chain;
                });
            }), label + " types");
        }

        if (dto.resources != null && !dto.resources.isEmpty()) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.StatelessSession s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return productService.find(s, productId).chain(product -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (var entry : dto.resources.entrySet()) {
                        String classificationName = entry.getKey();
                        UUID riId = parseUuidOrNull(entry.getValue(), label + " resources");
                        if (riId == null) continue;
                        chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                                .chain(ri -> product.addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token).replaceWithVoid()));
                    }
                    return chain;
                });
            }), label + " resources");
        }
    }

    private void persistUpdateRelationshipsAsync(String enterpriseName, String requestingSystemName,
                                                 UUID productId, ProductUpdateDTO dto) {
        String label = "product " + productId;

        if (hasEntries(dto.classifications)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.StatelessSession s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return productService.find(s, productId).chain(product -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.classifications, (name, value) ->
                            product.addOrUpdateClassification(s, name, value, sys, token).replaceWithVoid());
                    chain = chainDelete(chain, dto.classifications, name ->
                            product.removeClassification(s, name, null, sys, token).replaceWithVoid());
                    return chain;
                });
            }), label + " classifications");
        }

        if (hasEntries(dto.types)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.StatelessSession s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return productService.find(s, productId).chain(product -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    chain = chainAddOrUpdate(chain, dto.types, (name, value) ->
                            product.addOrUpdateProductTypes(s, name, null, null, value, sys, token).replaceWithVoid());
                    chain = chainDelete(chain, dto.types, name ->
                            product.removeProductTypes(s, name, null, null, null, sys, token).replaceWithVoid());
                    return chain;
                });
            }), label + " types");
        }

        if (hasEntries(dto.resources)) {
            SessionUtils.fireAndForget(SessionUtils.withActivityMasterStateless(enterpriseName, requestingSystemName, tuple -> {
                Mutiny.StatelessSession s = tuple.getItem1(); ISystems<?, ?> sys = tuple.getItem3(); UUID[] token = tuple.getItem4();
                return productService.find(s, productId).chain(product -> {
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    if (dto.resources.addOrUpdate != null) {
                        for (var e : dto.resources.addOrUpdate.entrySet()) {
                            String classificationName = e.getKey();
                            UUID riId = parseUuidOrNull(e.getValue(), label + " resources addOrUpdate");
                            if (riId == null) continue;
                            chain = chain.chain(() -> resourceItemService.findByUUID(s, riId)
                                    .chain(ri -> product.addOrUpdateResourceItem(s, classificationName, ri, null, null, sys, token).replaceWithVoid()));
                        }
                    }
                    chain = chainDeleteByExpire(chain, dto.resources, s, (Product) product, ProductXResourceItem.class,
                            ProductXResourceItem_.productID, WarehouseClassificationRelationshipTable::getClassificationID, cls -> cls != null ? cls.getName() : null);
                    return chain;
                });
            }), label + " resources");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO-based response builders (no DB round-trip)
    // ──────────────────────────────────────────────────────────────────────────

    private ProductDTO buildCreateResponseFromDto(Product product, ProductCreateDTO dto) {
        ProductDTO response = new ProductDTO();
        response.productId = product.getId();
        response.name = dto.name;
        response.description = dto.description;
        response.code = dto.code;
        response.types = dto.types != null ? new LinkedHashMap<>(dto.types) : null;
        response.classifications = dto.classifications != null ? new LinkedHashMap<>(dto.classifications) : null;
        response.resources = dto.resources != null ? new LinkedHashMap<>(dto.resources) : null;
        return response;
    }

    private ProductDTO buildUpdateResponseFromDto(UUID productId, ProductUpdateDTO dto) {
        ProductDTO response = new ProductDTO();
        response.productId = productId;
        if (dto.classifications != null && dto.classifications.addOrUpdate != null) response.classifications = new LinkedHashMap<>(dto.classifications.addOrUpdate);
        if (dto.types != null && dto.types.addOrUpdate != null) response.types = new LinkedHashMap<>(dto.types.addOrUpdate);
        if (dto.resources != null && dto.resources.addOrUpdate != null) response.resources = new LinkedHashMap<>(dto.resources.addOrUpdate);
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <L extends WarehouseBaseTable<L, ?, UUID>, R> Uni<Void> chainDeleteByExpire(
            Uni<Void> chain,
            RelationshipUpdateEntry entry,
            Mutiny.StatelessSession session,
            Product product,
            Class<L> linkClass,
            jakarta.persistence.metamodel.SingularAttribute productAttr,
            java.util.function.Function<L, R> relatedGetter,
            java.util.function.Function<R, String> nameExtractor) {
        if (entry == null || entry.delete == null || entry.delete.isEmpty()) return chain;

        Set<String> namesToDelete = new HashSet<>(entry.delete);

        return chain.chain(() -> {
            try {
                L instance = linkClass.getDeclaredConstructor().newInstance();
                return ((com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD) instance.builder(session)
                        .where(productAttr, Equals, product))
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

    private boolean hasEntries(RelationshipUpdateEntry entry) {
        if (entry == null) return false;
        return (entry.addOrUpdate != null && !entry.addOrUpdate.isEmpty())
                || (entry.delete != null && !entry.delete.isEmpty());
    }

    private boolean hasAnyRelationship(ProductCreateDTO dto) {
        return (dto.classifications != null && !dto.classifications.isEmpty())
                || (dto.types != null && !dto.types.isEmpty())
                || (dto.resources != null && !dto.resources.isEmpty());
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


