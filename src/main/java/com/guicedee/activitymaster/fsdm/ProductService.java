package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.stream.Collectors;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;

@Log4j2
public class ProductService
		implements IProductService<ProductService>
{
	@Inject
	private IEnterprise<?, ?> enterprise;

	@Inject
	private IClassificationService<?> classificationService;

	@Override
	public IProduct<?, ?> get()
	{
		return new Product();
	}
	//@Transactional()
	@Override
	public Uni<IProduct<?, ?>> find(UUID id)
	{
		return new Product().builder()
		                    .find(id)
		                    .get()
		                    .onFailure().invoke(error -> log.error("Error finding product by ID: {}", error.getMessage(), error))
		                    .onItem().ifNull().continueWith(() -> null)
		                    .map(product -> (IProduct<?, ?>) product);
	}

	//@Transactional()
	@Override
	public Uni<IProductType<?, ?>> findType(UUID id)
	{
		return new ProductType().builder()
		                        .find(id)
		                        .get()
		                        .onFailure().invoke(error -> log.error("Error finding product type by ID: {}", error.getMessage(), error))
		                        .onItem().ifNull().continueWith(() -> null)
		                        .map(productType -> (IProductType<?, ?>) productType);
	}

	@Override
	public IProductType<?, ?> getType()
	{
		return new ProductType();
	}

	@Override
	public Uni<IProduct<?, ?>> createProduct(String productType, String name, String description, String code, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createProduct(productType, null, name, description, code, system, identityToken);
	}

	@Override
	public Uni<IProduct<?, ?>> createProduct(String productType, java.util.UUID key, String name, String description, String code, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		Product product = new Product();
		product.setId(key);
		product.setName(name);
		product.setProductCode(code);
		product.setDescription(description);

		product.setEnterpriseID(enterprise);
		product.setSystemID(system);
		product.setOriginalSourceSystemID(system.getId());
		IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		// Get active flag using reactive pattern
		return acService.getActiveFlag(enterprise)
		        .chain(activeFlag -> {
		            product.setActiveFlagID(activeFlag);
		            return product.persist();
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
		            return createProductType(productType, productType, system, identityToken)
		                   .chain(pType -> {
		                       // Add product types - this is a synchronous operation in the current implementation
		                       // We'll wrap it in a Uni to make it reactive
		                       return Uni.createFrom().emitter(emitter -> {
		                           try {
		                               product.addProductTypes(productType, "", NoClassification.toString(), system, identityToken);
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
	public Uni<IProduct<?, ?>> findProduct(String name, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new Product().builder()
		                    .withName(name)
		                    .inActiveRange()
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get()
		                    .onFailure().invoke(error -> log.error("Error finding product by name: {}", error.getMessage(), error))
		                    .onItem().ifNull().failWith(() -> new NoSuchElementException("Product not found with name: " + name))
		                    .map(product -> (IProduct<?, ?>) product);
	}
	//@Transactional()
	@Override
	public Uni<List<IRelationshipValue<IProduct<?, ?>, IResourceItem<?, ?>, ?>>> findProductByResourceItem(IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		final String finalClassificationName = classificationName;

		return classificationService.find(classificationName, system, identityToken)
		        .chain(classification -> {
		            return new ProductXResourceItem().builder()
		                                            .inActiveRange()
		                                            .inDateRange()
		                                            .withEnterprise(enterprise)
		                                            .withClassification(classification)
		                                            .withValue(value)
		                                            .where(ProductXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
		                                            .getAll()
		                                            .onFailure().invoke(error -> log.error("Error finding products by resource item: {}", error.getMessage(), error))
		                                            .map(list -> (List<IRelationshipValue<IProduct<?, ?>, IResourceItem<?, ?>, ?>>) (List<?>) list);
		        });
	}


	@Override
	public Uni<IProductType<?, ?>> createProductType(String productsType, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createProductType(productsType, null, description, system, identityToken);
	}

	@Override
	//@Transactional()
	public Uni<IProductType<?, ?>> createProductType(String productsType, java.util.UUID key, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		ProductType et = new ProductType();

		// Check if product type exists
		return et.builder()
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
		                 return acService.getActiveFlag(enterprise)
		                        .chain(activeFlag -> {
		                            et.setActiveFlagID(activeFlag);
		                            et.setOriginalSourceSystemID(system.getId());
		                            return et.persist();
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
		                 return findProductTypeForProduct(productsType, system, identityToken);
		             }
		         });
	}

	//@Transactional()
	@Override
	public Uni<IProductType<?, ?>> findProductTypeForProduct(String productType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new ProductType().builder()
		                        .withName(productType)
		                        .withEnterprise(enterprise)
		                        .inActiveRange()
		                        .inDateRange()
		                        // .canRead(system, identityToken)
		                        .get()
		                        .onFailure().invoke(error -> log.error("Error finding product type: {}", error.getMessage(), error))
		                        .onItem().ifNull().failWith(() -> new NoSuchElementException("Product Type - " + productType + " not found"))
		                        .map(productTypeObj -> (IProductType<?, ?>) productTypeObj);
	}

	//@Transactional()
	@Override
	public Uni<IProduct<?, ?>> findProduct(String productName, IClassification<?, ?> classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new Product().builder()
		                    .withName(productName)
		                    .withClassification(classification)
		                    .inActiveRange()
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get()
		                    .onFailure().invoke(error -> log.error("Error finding product by name and classification: {}", error.getMessage(), error))
		                    .onItem().ifNull().failWith(() -> new NoSuchElementException("Product not found with name: " + productName))
		                    .map(product -> (IProduct<?, ?>) product);
	}


	@Override
	public Uni<IProductType<?, ?>> findProductTypeForProduct(IProduct<?, ?> product, IClassification<?, ?> classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return findProductTypeForProduct(product, classification.getName(), system, identityToken);
	}

	//@Transactional()
	@Override
	public Uni<IProductType<?, ?>> findProductTypeForProduct(IProduct<?, ?> product, String classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return classificationService.find(classification, system, identityToken)
		        .chain(classification1 -> {
		            return new ProductXProductType().builder()
		                                .findLink((Product) product, null, null)
		                                .withClassification(classification1)
		                                .inActiveRange()
		                                .inDateRange()
		                                .withEnterprise(enterprise)
		                                .get()
		                                .onFailure().invoke(error -> log.error("Error finding product type for product: {}", error.getMessage(), error))
		                                .onItem().ifNull().failWith(() -> new NoSuchElementException("Product Type not found for product: " + product.getName()))
		                                .map(productXProductType -> (IProductType<?, ?>) productXProductType.getSecondary());
		        });
	}

	public Uni<List<IProductType<?, ?>>> findProductTypes(IClassification<?, ?> classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return findProductTypes(classification.getName(), system, identityToken);
	}
	//@Transactional()
	@Override
	public Uni<List<IProductType<?, ?>>> findProductTypes(String classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new ProductType().findClassifications(classification, system, identityToken)
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
	public Uni<List<IProduct<?, ?>>> findByProductTypes(IProductType<?, ?> type, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return findByProductTypes(type.getName(), system, identityToken);
	}
	//@Transactional()
	@Override
	public Uni<List<IProduct<?, ?>>> findByProductTypes(String type, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new ProductXProductType().builder()
		                                .withEnterprise(enterprise)
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .withType(type, system, identityToken)
		                                .getAll()
		                                .onFailure().invoke(error -> log.error("Error finding products by type: {}", error.getMessage(), error))
		                                .map(productXProductTypes -> {
		                                    List<IProduct<?, ?>> products = new ArrayList<>();
		                                    for (ProductXProductType pxpt : productXProductTypes) {
		                                        products.add(pxpt.getProductID());
		                                    }
		                                    return products;
		                                });
	}
}
