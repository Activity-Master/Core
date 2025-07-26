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
		Product product = new Product();
		product.setId(key);
		product.setName(name);
		product.setProductCode(code);
		product.setDescription(description);
		var enterprise = system.getEnterprise();
		product.setEnterpriseID(enterprise);
		product.setSystemID(system);
		product.setOriginalSourceSystemID(system.getId());
		IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		// Get active flag using reactive pattern
		return acService.getActiveFlag(session, enterprise)
		        .chain(activeFlag -> {
		            product.setActiveFlagID(activeFlag);
					return session.persist(product)
								   .replaceWith(Uni.createFrom()
														.item(product));
		        })
		        .chain(persisted -> {
		            // Start createDefaultSecurity in parallel without waiting for it
		            product.createDefaultSecurity(system, identityToken)
		                   .subscribe().with(
		                       result -> {
		                           // Security setup completed successfully
		                       },
		                       error -> {
		                           // Log error but don't fail the main operation
		                           log.warn("Error in createDefaultSecurity", error);
		                       }
		                   );

		            // Create product type
		            return createProductType(session, productType, productType, system, identityToken)
		                   .chain(pType -> {
		                       // Add product types - this is a synchronous operation in the current implementation
		                       // We'll wrap it in a Uni to make it reactive
		                       return Uni.createFrom().emitter(emitter -> {
		                           try {
		                               product.addProductTypes(session, productType, "", NoClassification.toString(), system, identityToken);
		                               emitter.complete((IProduct<?, ?>) product);
		                           } catch (Exception e) {
		                               emitter.fail(e);
		                           }
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
		ProductType et = new ProductType();
		var enterprise = system.getEnterprise();
		// Check if product type exists
		return et.builder(session)
		         .withName(productsType)
		         .withEnterprise(enterprise)
		         .inActiveRange()
		         .inDateRange()
		         .getCount()
		         .onFailure().invoke(error -> log.error("Error checking if product type exists: {}", error.getMessage(), error))
		         .chain(count -> {
		             boolean exists = count > 0;
		             if (!exists)
		             {
		                 et.setId(key);
		                 et.setName(productsType);
		                 et.setDescription(description);
		                 et.setSystemID(system);
		                 et.setEnterpriseID(enterprise);
		                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                 // Get active flag using reactive pattern
		                 return acService.getActiveFlag(session, enterprise)
		                        .chain(activeFlag -> {
		                            et.setActiveFlagID(activeFlag);
		                            et.setOriginalSourceSystemID(system.getId());
									return session.persist(et)
												   .replaceWith(Uni.createFrom()
																		.item(et));
		                        })
		                        .chain(persisted -> {
		                            // Start createDefaultSecurity in parallel without waiting for it
		                            et.createDefaultSecurity(system, identityToken)
		                              .subscribe().with(
		                                  result -> {
		                                      // Security setup completed successfully
		                                  },
		                                  error -> {
		                                      // Log error but don't fail the main operation
		                                      log.warn("Error in createDefaultSecurity", error);
		                                  }
		                              );

		                            // Return the persisted product type immediately
		                            return Uni.createFrom().item((IProductType<?, ?>) et);
		                        });
		             }
		             else
		             {
		                 return findProductTypeForProduct(session, productsType, system, identityToken);
		             }
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
