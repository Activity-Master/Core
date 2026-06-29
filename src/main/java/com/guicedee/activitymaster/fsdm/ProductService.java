package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * <p>
 * [✓] One action per Mutiny.Session at a time
 * - All operations on a session are sequential
 * - No parallel operations on the same session
 * <p>
 * [✓] Pass Mutiny.Session through the chain
 * - All methods accept session as parameter
 * - Session is passed to all dependent operations
 * <p>
 * [✓] No await() usage
 * - Using reactive chains instead of blocking operations
 * <p>
 * [✓] Synchronous execution of reactive chains
 * - All reactive chains execute synchronously
 * - createDefaultSecurity is properly chained with error handling
 * <p>
 * [✓] No parallel operations on a session
 * - Not using Uni.combine().all().unis() with operations that share the same session
 * <p>
 * [✓] No session/transaction creation in libraries
 * - Sessions are passed in from the caller
 * - No sessionFactory.withTransaction() in methods
 */

import com.google.common.base.Strings;
import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.capabilities.contains.IContainsHierarchy;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.*;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;

@Log4j2
@Singleton
public class ProductService
        implements IProductService<ProductService> {
    // Local cache: key = enterpriseId + '|' + systemId + '|' + productTypeName → ProductType UUID
    private final java.util.Map<String, java.util.UUID> productTypeKeyToId = new java.util.concurrent.ConcurrentHashMap<>();

    // Stateless detached-prepped reference-type cache (product type), keyed by enterpriseId → name.
    // Safe: detached scalar projection, stable install-time reference types; only cached on a real hit.
    private static final Map<UUID, Map<String, IProductType<?, ?>>> STATELESS_PRODUCT_TYPE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    // UUID-based lookup to leverage Hibernate 2nd-level cache
    public io.smallrye.mutiny.Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType<?, ?>> getProductTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id) {
        return (io.smallrye.mutiny.Uni) session.find(com.guicedee.activitymaster.fsdm.db.entities.product.ProductType.class, id);
    }

    @Inject
    private ClassificationService classificationService;

    @Override
    public IProduct<?, ?> get() {
        return new Product();
    }

    
    @Override
    public Uni<IProduct<?, ?>> find(Mutiny.Session session, UUID id) {
        return (Uni) new Product()
                .builder(session)
                .find(id)
                .get();
    }

    
    @Override
    public Uni<IProductType<?, ?>> findType(Mutiny.Session session, UUID id) {
        return (Uni) new ProductType()
                .builder(session)
                .find(id)
                .get();
    }

    @Override
    public IProductType<?, ?> getType() {
        return new ProductType();
    }

    @Override
    public Uni<IProduct<?, ?>> createProduct(Mutiny.Session session, String productType, String name, String description, String code, ISystems<?, ?> system, UUID... identityToken) {
        return createProduct(session, productType, null, name, description, code, system, identityToken);
    }

    @Override
    public Uni<IProduct<?, ?>> createProduct(Mutiny.Session session, String productType, UUID key, String name, String description, String code, ISystems<?, ?> system, UUID... identityToken) {
        // Public create — world-readable (public/default security matrix).
        return createProductWithSecurity(session, productType, key, name, description, code, system,
                p -> p.createDefaultSecurity(session, system, identityToken), identityToken);
    }

    /**
     * Opt-in <strong>scope-restricted</strong> product create. Identical to
     * {@link #createProduct(Mutiny.Session, String, UUID, String, String, String, ISystems, UUID...)} except the
     * product is secured with the restricted matrix: only Administrators / Systems / Applications / Plugins retain
     * access, plus a <em>read</em> grant for {@code scopeToken}. Only identity tokens at that scope node or below it
     * may read the product.
     */
    @Override
    public Uni<IProduct<?, ?>> createProductScopeRestricted(Mutiny.Session session, String productType, UUID key, String name, String description, String code, ISystems<?, ?> system,
                                                            com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                            UUID... identityToken) {
        return createProductWithSecurity(session, productType, key, name, description, code, system,
                p -> p.createScopeRestrictedSecurity(session, system, scopeToken, identityToken), identityToken);
    }


    private Uni<IProduct<?, ?>> createProductWithSecurity(Mutiny.Session session, String productType, UUID key, String name, String description, String code, ISystems<?, ?> system,
                                                          java.util.function.Function<Product, Uni<?>> securityFn, UUID... identityToken) {
        var enterprise = system.getEnterprise();

        Product newProduct = new Product();

        return findProduct(session, name, system, identityToken)
                .onFailure(NoResultException.class)
                .recoverWithUni(existingProduct -> {
                    newProduct.setId(key);
                    newProduct.setName(name);
                    newProduct.setProductCode(code);
                    newProduct.setDescription(description);
                    newProduct.setEnterpriseID(enterprise);
                    newProduct.setSystemID(system);
                    newProduct.setOriginalSourceSystemID(system.getId());

                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                    return acService
                            .getActiveFlag(session, enterprise, identityToken)
                            .chain(activeFlag -> {
                                newProduct.setActiveFlagID(activeFlag);
                                return session
                                        .persist(newProduct)
                                        .replaceWith(Uni
                                                .createFrom()
                                                .item(newProduct))
                                        .chain(product ->
                                                // Find existing product type; do not create here
                                                findProductTypeForProduct(session, productType, system, identityToken)
                                                        .chain(foundType -> {
                                                            // Link product -> product type (classification: NoClassification, value: empty)
                                                            return newProduct
                                                                    .addProductTypes(session, productType, "", NoClassification.toString(), system, identityToken)
                                                                    .onFailure()
                                                                    .invoke(err -> log.warn("Error linking product to product type", err))
                                                                    .onFailure()
                                                                    .recoverWithNull()
                                                                    .replaceWith(foundType);
                                                        })
                                                        // Continue with security creation for the product
                                                        .replaceWith(newProduct)
                                                        .chain(p -> securityFn.apply(newProduct)
                                                                .onFailure()
                                                                .invoke(error -> log.warn("Error in createProduct security", error))
                                                                .onFailure()
                                                                .recoverWithUni(err -> Uni
                                                                        .createFrom()
                                                                        .nullItem())
                                                                .replaceWith(newProduct)
                                                        ))
                                        ;
                            });
                });
    }

    @Override
    public Uni<IProduct<?, ?>> findProduct(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return new Product()
                .builder(session)
                .withName(name)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                .get()
                .onItem()
                .transform(product->product);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Uni<List<IRelationshipValue<IProduct<?, ?>, IResourceItem<?, ?>, ?>>> findProductByResourceItem(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken) {
        if (Strings.isNullOrEmpty(classificationName)) {
            classificationName = NoClassification.toString();
        }
        var enterprise = system.getEnterprise();
        return (Uni) classificationService
                .find(session, classificationName, system, identityToken)
                .chain(classification -> {
                    return new ProductXResourceItem()
                            .builder(session)
                            .inActiveRange()
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .withClassification(classification)
                            .withValue(value)
                            .where(ProductXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
                            .getAll();
                });
    }
    @Override
    public Uni<IProductType<?, ?>> createProductType(Mutiny.Session session, String productsType, String description, ISystems<?, ?> system, UUID... identityToken) {
        return createProductType(session, productsType, null, description, system, identityToken);
    }

    @Override
    public Uni<IProductType<?, ?>> createProductType(Mutiny.Session session, String productsType, UUID key, String description, ISystems<?, ?> system, UUID... identityToken) {
        // Public create — world-readable (public/default security matrix).
        return createProductTypeWithSecurity(session, productsType, key, description, system,
                pt -> pt.createDefaultSecurity(session, system, identityToken), identityToken);
    }

    /**
     * Opt-in <strong>scope-restricted</strong> product-type create. Same as
     * {@link #createProductType(Mutiny.Session, String, UUID, String, ISystems, UUID...)} but secured with the
     * restricted matrix plus a <em>read</em> grant for {@code scopeToken}.
     */
    @Override
    public Uni<IProductType<?, ?>> createProductTypeScopeRestricted(Mutiny.Session session, String productsType, UUID key, String description, ISystems<?, ?> system,
                                                                    com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                                    UUID... identityToken) {
        return createProductTypeWithSecurity(session, productsType, key, description, system,
                pt -> pt.createScopeRestrictedSecurity(session, system, scopeToken, identityToken), identityToken);
    }

    private Uni<IProductType<?, ?>> createProductTypeWithSecurity(Mutiny.Session session, String productsType, UUID key, String description, ISystems<?, ?> system,
                                                                 java.util.function.Function<ProductType, Uni<?>> securityFn, UUID... identityToken) {
        var enterprise = system.getEnterprise();

        ProductType newProductType = new ProductType();

        return findProductTypeForProduct(session, productsType, system, identityToken)
                .onFailure(NoResultException.class)
                .recoverWithUni(existingProductType -> {
                    newProductType.setId(key);
                    newProductType.setName(productsType);
                    newProductType.setDescription(description);
                    newProductType.setSystemID(system);
                    newProductType.setEnterpriseID(enterprise);
                    newProductType.setOriginalSourceSystemID(system.getId());

                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                    return acService
                            .getActiveFlag(session, enterprise, identityToken)
                            .chain(activeFlag -> {
                                newProductType.setActiveFlagID(activeFlag);
                                return session
                                        .persist(newProductType)
                                        .replaceWith(Uni
                                                .createFrom()
                                                .item(newProductType))
                                        .chain(persisted ->
                                                securityFn.apply(persisted)
                                                        .onFailure()
                                                        .invoke(error -> log.warn("Error in createProductType security", error))
                                                        .onFailure()
                                                        .recoverWithUni(err -> Uni
                                                                .createFrom()
                                                                .nullItem())
                                                        .replaceWith((IProductType<?, ?>) persisted)
                                        );
                            });
                });
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Uni<IProductType<?, ?>> findProductTypeForProduct(Mutiny.Session session, String productType, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        java.util.UUID enterpriseId = null;
        java.util.UUID systemId = null;
        if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
            enterpriseId = ent.getId();
        }
        if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys) {
            systemId = sys.getId();
        }
        String key = enterpriseId + "|" + systemId + "|" + productType;
        java.util.UUID cachedId = productTypeKeyToId.get(key);
        if (cachedId != null) {
            log.trace("🔁 ProductType cache hit for key '{}': {} — loading by UUID", key, cachedId);
            return (Uni) getProductTypeById(session, cachedId)
                    .flatMap(found -> {
                        if (found != null) {
                            return Uni.createFrom().item(found);
                        }
                        productTypeKeyToId.remove(key);
                        return (Uni) new ProductType()
                                .builder(session)
                                .withName(productType)
                                .withEnterprise(enterprise)
                                .inActiveRange()
                                .inDateRange()
                                .get()
                                .invoke(res -> {
                                    if (res != null && res.getId() != null) {
                                        productTypeKeyToId.put(key,res.getId());
                                    }
                                });
                    });
        }
        return (Uni) new ProductType()
                .builder(session)
                .withName(productType)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .get()
                .invoke(res -> {
                    if (res != null && res.getId() != null) {
                        productTypeKeyToId.put(key, res.getId());
                    }
                });
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Uni<IProductType<?, ?>> findProductTypeForProduct(Mutiny.StatelessSession session, String productType, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        UUID enterpriseId = enterprise.getId();
        Map<String, IProductType<?, ?>> byName = STATELESS_PRODUCT_TYPE_CACHE.computeIfAbsent(enterpriseId, k -> new java.util.concurrent.ConcurrentHashMap<>());
        IProductType<?, ?> hit = byName.get(productType);
        if (hit != null) {
            return Uni.createFrom().item((IProductType<?, ?>) hit);
        }
        Uni<IProductType<?, ?>> resolved = new ProductType().builder(session)
                .withName(productType)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .selectColumn(com.guicedee.activitymaster.fsdm.db.entities.product.ProductType_.id)
                .selectColumn(com.guicedee.activitymaster.fsdm.db.entities.product.ProductType_.name)
                .selectColumn(com.guicedee.activitymaster.fsdm.db.entities.product.ProductType_.description)
                .get(Object[].class)
                .map(row -> {
                    ProductType prepped = new ProductType((UUID) row[0], (String) row[1], (String) row[2]);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return (IProductType<?, ?>) prepped;
                });
        return resolved.onItem().invoke(t -> { if (t != null && t.getId() != null) byName.put(productType, t); });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Uni<IProduct<?, ?>> findProduct(Mutiny.Session session, String productName, IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return (Uni) new Product()
                .builder(session)
                .withName(productName)
                .withClassification(classification)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                .get();
    }


    @Override
    public Uni<IProductType<?, ?>> findProductTypeForProduct(Mutiny.Session session, IProduct<?, ?> product, IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken) {
        return findProductTypeForProduct(session, product, classification.getName(), system, identityToken);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Uni<IProductType<?, ?>> findProductTypeForProduct(Mutiny.Session session, IProduct<?, ?> product, String classification, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return (Uni) classificationService
                .find(session, classification, system, identityToken)
                .chain(classification1 -> {
                    return new ProductXProductType()
                            .builder(session)
                            .findLink((Product) product, null, null)
                            .withClassification(classification1)
                            .inActiveRange()
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .get();
                });
    }

    public Uni<List<IProductType<?, ?>>> findProductTypes(Mutiny.Session session, IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken) {
        return findProductTypes(session, classification.getName(), system, identityToken);
    }

    @Override
    public Uni<List<IProductType<?, ?>>> findProductTypes(Mutiny.Session session, String classification, ISystems<?, ?> system, UUID... identityToken) {
        return new ProductType()
                .findClassifications(session, classification, system, identityToken)
                .onFailure()
                .invoke(error -> log.error("Error finding product types: {}", error.getMessage(), error))
                .map(classifications -> {
                    List<IProductType<?, ?>> list = new ArrayList<>();
                    for (IRelationshipValue<ProductType, IClassification<?, ?>, ?> returns : classifications) {
                        IProductType<?, ?> primary = returns.getPrimary();
                        list.add(primary);
                    }
                    return list;
                });
    }

    @Override
    public Uni<List<IProduct<?, ?>>> findByProductTypes(Mutiny.Session session, IProductType<?, ?> type, ISystems<?, ?> system, UUID... identityToken) {
        return findByProductTypes(session, type.getName(), system, identityToken);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Uni<List<IProduct<?, ?>>> findByProductTypes(Mutiny.Session session, String type, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return (Uni) new ProductXProductType()
                .builder(session)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .canRead(system, identityToken)
                .withType(type, system, identityToken)
                .getAll();
    }
}

