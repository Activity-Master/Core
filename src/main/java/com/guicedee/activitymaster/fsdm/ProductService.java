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

@SuppressWarnings("unchecked")
@Log4j2
@Singleton
public class ProductService
        implements IProductService<ProductService> {
    // Local cache: key = enterpriseId + '|' + systemId + '|' + productTypeName → ProductType UUID
    private final java.util.Map<String, java.util.UUID> productTypeKeyToId = new java.util.concurrent.ConcurrentHashMap<>();

    // UUID-based lookup to leverage Hibernate 2nd-level cache
    public io.smallrye.mutiny.Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType<?, ?>> getProductTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id) {
        return (io.smallrye.mutiny.Uni) session.find(com.guicedee.activitymaster.fsdm.db.entities.product.ProductType.class, id);
    }

    @Inject
    private IClassificationService<?> classificationService;

    @Override
    public IProduct<?, ?> get() {
        return new Product();
    }

    //@Transactional()
    @Override
    public Uni<IProduct<?, ?>> find(Mutiny.Session session, UUID id) {
        return (Uni) new Product()
                .builder(session)
                .find(id)
                .get();
    }

    //@Transactional()
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
        Product newProduct = new Product();
        var enterprise = system.getEnterprise();

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
                            .getActiveFlag(session, enterprise)
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
                                                        .chain(p -> newProduct
                                                                .createDefaultSecurity(session, system, identityToken)
                                                                .onFailure()
                                                                .invoke(error -> log.warn("Error in createDefaultSecurity", error))
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

    //@Transactional()
    @Override
    public Uni<IProduct<?, ?>> findProduct(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return (Uni) new Product()
                .builder(session)
                .withName(name)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                .get();
    }

    //@Transactional()
    @Override
    public Uni<List<IRelationshipValue<IProduct<?, ?>, IResourceItem<?, ?>, ?>>> findProductByResourceItem(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken) {
        if (Strings.isNullOrEmpty(classificationName)) {
            classificationName = NoClassification.toString();
        }
        final String finalClassificationName = classificationName;
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
    //@Transactional()
    public Uni<IProductType<?, ?>> createProductType(Mutiny.Session session, String productsType, UUID key, String description, ISystems<?, ?> system, UUID... identityToken) {
        ProductType newProductType = new ProductType();
        var enterprise = system.getEnterprise();

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
                            .getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                                newProductType.setActiveFlagID(activeFlag);
                                return session
                                        .persist(newProductType)
                                        .replaceWith(Uni
                                                .createFrom()
                                                .item(newProductType))
                                        .chain(persisted ->
                                                persisted
                                                        .createDefaultSecurity(session, system, identityToken)
                                                        .onFailure()
                                                        .invoke(error -> log.warn("Error in createDefaultSecurity", error))
                                                        .onFailure()
                                                        .recoverWithUni(err -> Uni
                                                                .createFrom()
                                                                .nullItem())
                                                        .replaceWith((IProductType<?, ?>) persisted)
                                        );
                            });
                });
    }

    //@Transactional()
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
                                        productTypeKeyToId.put(key, (java.util.UUID) res.getId());
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
                        productTypeKeyToId.put(key, (java.util.UUID) res.getId());
                    }
                });
    }

    //@Transactional()
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

    //@Transactional()
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

    //@Transactional()
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

    //@Transactional()
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

