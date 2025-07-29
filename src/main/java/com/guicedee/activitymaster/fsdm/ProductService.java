package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
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
public class ProductService
		implements IProductService<ProductService>
{
	@Inject
	private IClassificationService<?> classificationService;

	@Override
	public IProduct<?, ?> get()
	{
		return new Product();
	}
	//@Transactional()
	@Override
	public Uni<IProduct<?, ?>> find(Mutiny.Session session, UUID id)
	{
		return (Uni)new Product().builder(session)
		                    .find(id)
		                    .get();
	}

	//@Transactional()
	@Override
	public Uni<IProductType<?, ?>> findType(Mutiny.Session session, UUID id)
	{
		return (Uni) new ProductType().builder(session)
		                        .find(id)
		                        .get();
	}

	@Override
	public IProductType<?, ?> getType()
	{
		return new ProductType();
	}

	@Override
	public Uni<IProduct<?, ?>> createProduct(Mutiny.Session session, String productType, String name, String description, String code, ISystems<?, ?> system, UUID... identityToken)
	{
		return createProduct(session, productType, null, name, description, code, system, identityToken);
	}

	@Override
	public Uni<IProduct<?, ?>> createProduct(Mutiny.Session session, String productType, UUID key, String name, String description, String code, ISystems<?, ?> system, UUID... identityToken)
	{
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
		                   return acService.getActiveFlag(session, enterprise)
		                              .chain(activeFlag -> {
		                                  newProduct.setActiveFlagID(activeFlag);
		                                  return session.persist(newProduct)
		                                             .replaceWith(Uni.createFrom()
		                                                              .item(newProduct))
		                                             .chain(persisted -> {
		                                                 // Start createDefaultSecurity in parallel without waiting for it
		                                                 persisted.createDefaultSecurity(session, system, identityToken)
		                                                     .subscribe()
		                                                     .with(
		                                                         result -> {
		                                                             // Security setup completed successfully
		                                                         },
		                                                         error -> {
		                                                             // Log error but don't fail the main operation
		                                                             log.warn("Error in createDefaultSecurity", error);
		                                                         }
		                                                     );

		                                                 // Create product type and add it to product
		                                                 return createProductType(session, productType, productType, system, identityToken)
		                                                        .chain(pType -> {
		                                                            // Add product types - this is a synchronous operation in the current implementation
		                                                            // We'll wrap it in a Uni to make it reactive
		                                                            return Uni.createFrom().emitter(emitter -> {
		                                                                try {
		                                                                    newProduct.addProductTypes(session, productType, "", NoClassification.toString(), system, identityToken);
		                                                                    emitter.complete((IProduct<?, ?>) newProduct);
		                                                                } catch (Exception e) {
		                                                                    emitter.fail(e);
		                                                                }
		                                                            });
		                                                        });
		                                             });
		                              });
		               });
	}

	//@Transactional()
	@Override
	public Uni<IProduct<?, ?>> findProduct(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) new Product().builder(session)
		                    .withName(name)
		                    .inActiveRange()
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get();
	}
	//@Transactional()
	@Override
	public Uni<List<IRelationshipValue<IProduct<?, ?>, IResourceItem<?, ?>, ?>>> findProductByResourceItem(Mutiny.Session session, IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		final String finalClassificationName = classificationName;
		var enterprise = system.getEnterprise();
		return (Uni) classificationService.find(session, classificationName, system, identityToken)
		        .chain(classification -> {
		            return new ProductXResourceItem().builder(session)
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
	public Uni<IProductType<?, ?>> createProductType(Mutiny.Session session, String productsType, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		return createProductType(session, productsType, null, description, system, identityToken);
	}

	@Override
	//@Transactional()
	public Uni<IProductType<?, ?>> createProductType(Mutiny.Session session, String productsType, UUID key, String description, ISystems<?, ?> system, UUID... identityToken)
	{
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
		                   return acService.getActiveFlag(session, enterprise)
		                              .chain(activeFlag -> {
		                                  newProductType.setActiveFlagID(activeFlag);
		                                  return session.persist(newProductType)
		                                             .replaceWith(Uni.createFrom()
		                                                              .item(newProductType))
		                                             .map(persisted -> {
		                                                 // Start createDefaultSecurity in parallel without waiting for it
		                                                 persisted.createDefaultSecurity(session, system, identityToken)
		                                                     .subscribe()
		                                                     .with(
		                                                         result -> {
		                                                             // Security setup completed successfully
		                                                         },
		                                                         error -> {
		                                                             // Log error but don't fail the main operation
		                                                             log.warn("Error in createDefaultSecurity", error);
		                                                         }
		                                                     );
		                                                 // Return the persisted product type immediately without waiting for security setup
		                                                 return (IProductType<?, ?>) persisted;
		                                             });
		                              });
		               });
	}

	//@Transactional()
	@Override
	public Uni<IProductType<?, ?>> findProductTypeForProduct(Mutiny.Session session, String productType, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) new ProductType().builder(session)
		                        .withName(productType)
		                        .withEnterprise(enterprise)
		                        .inActiveRange()
		                        .inDateRange()
		                        // .canRead(system, identityToken)
		                        .get();
	}

	//@Transactional()
	@Override
	public Uni<IProduct<?, ?>> findProduct(Mutiny.Session session, String productName, IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) new Product().builder(session)
		                    .withName(productName)
		                    .withClassification(classification)
		                    .inActiveRange()
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get();
	}


	@Override
	public Uni<IProductType<?, ?>> findProductTypeForProduct(Mutiny.Session session, IProduct<?, ?> product, IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken)
	{
		return findProductTypeForProduct(session, product, classification.getName(), system, identityToken);
	}

	//@Transactional()
	@Override
	public Uni<IProductType<?, ?>> findProductTypeForProduct(Mutiny.Session session, IProduct<?, ?> product, String classification, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) classificationService.find(session, classification, system, identityToken)
		        .chain(classification1 -> {
		            return new ProductXProductType().builder(session)
		                                .findLink((Product) product, null, null)
		                                .withClassification(classification1)
		                                .inActiveRange()
		                                .inDateRange()
		                                .withEnterprise(enterprise)
		                                .get();
		        });
	}

	public Uni<List<IProductType<?, ?>>> findProductTypes(Mutiny.Session session, IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken)
	{
		return findProductTypes(session, classification.getName(), system, identityToken);
	}
	//@Transactional()
	@Override
	public Uni<List<IProductType<?, ?>>> findProductTypes(Mutiny.Session session, String classification, ISystems<?, ?> system, UUID... identityToken)
	{
		return new ProductType().findClassifications(session, classification, system, identityToken)
		        .onFailure().invoke(error -> log.error("Error finding product types: {}", error.getMessage(), error))
		        .map(classifications -> {
		            List<IProductType<?, ?>> list = new ArrayList<>();
		            for (IRelationshipValue<ProductType, IClassification<?, ?>, ?> returns : classifications)
		            {
		                IProductType<?, ?> primary = returns.getPrimary();
		                list.add(primary);
		            }
		            return list;
		        });
	}

	@Override
	public Uni<List<IProduct<?, ?>>> findByProductTypes(Mutiny.Session session, IProductType<?, ?> type, ISystems<?, ?> system, UUID... identityToken)
	{
		return findByProductTypes(session, type.getName(), system, identityToken);
	}
	//@Transactional()
	@Override
	public Uni<List<IProduct<?, ?>>> findByProductTypes(Mutiny.Session session, String type, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		return (Uni) new ProductXProductType().builder(session)
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .withType(type, system, identityToken)
		                                .getAll();
	}
}

