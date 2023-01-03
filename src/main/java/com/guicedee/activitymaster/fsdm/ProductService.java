package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.EntityManager;

import java.util.*;
import java.util.stream.Collectors;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.DefaultClassifications.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

public class ProductService
		implements IProductService<ProductService>
{

	@Inject
	private IClassificationService<?> classificationService;
	@Inject
	@com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB
	private EntityManager entityManager;
	
	@Override
	public IProduct<?, ?> get()
	{
		return new Product();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IProduct<?, ?> find(java.lang.String id)
	{
		return new Product().builder(entityManager)
		                    .find(id)
		                    .get()
		                    .orElse(null);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IProductType<?, ?> findType(java.lang.String id)
	{
		return new ProductType().builder(entityManager)
		                        .find(id)
		                        .get()
		                        .orElse(null);
	}
	
	@Override
	public IProductType<?, ?> getType()
	{
		return new ProductType();
	}
	
	@Override
	public IProduct<?, ?> createProduct(String productType, String name, String description, String code, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createProduct(productType, null, name, description, code, system, identityToken);
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IProduct<?, ?> createProduct(String productType, java.lang.String key, String name, String description, String code, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		Product product = new Product();
		product.setId(key);
		product.setName(name);
		product.setProductCode(code);
		product.setDescription(description);
		
		product.setEnterpriseID(system.getEnterpriseID());
		product.setSystemID(system);
		product.setOriginalSourceSystemID(system);
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
		product.setActiveFlagID(activeFlag);
		product.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
		product.createDefaultSecurity(system, identityToken);
		
		IProductType<?, ?> pType = createProductType(productType, productType, system, identityToken);
		
		product.addProductTypes(productType, STRING_EMPTY, NoClassification.toString(), system, identityToken);
		
		return product;
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IProduct<?, ?> findProduct(String name, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new Product().builder(entityManager)
		                    .withName(name)
		                    .inActiveRange()
		                    .inDateRange()
		                    .withEnterprise(system.getEnterpriseID())
		                    .get()
		                    .orElseThrow();
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IRelationshipValue<IProduct<?, ?>, IResourceItem<?, ?>, ?>> findProductByResourceItem(IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List arrangementXResourceItem = new ProductXResourceItem().builder(entityManager)
		                                                          .inActiveRange()
		                                                          .inDateRange()
		                                                          .withEnterprise(system.getEnterpriseID())
		                                                          .withClassification(classification)
		                                                          .withValue(value)
		                                                          .where(ProductXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
		                                                          .getAll();
		return arrangementXResourceItem;
	}
	
	
	@Override
	public IProductType<?, ?> createProductType(String productsType, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createProductType(productsType, null, description, system, identityToken);
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IProductType<?, ?> createProductType(String productsType, java.lang.String key, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		ProductType et = new ProductType();
		
		boolean exists = et.builder(entityManager)
		                   .withName(productsType)
		                   .withEnterprise(system.getEnterpriseID())
		                   .inActiveRange()
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setId(key);
			et.setName(productsType);
			et.setDescription(description);
			et.setSystemID(system);
			et.setEnterpriseID(system.getEnterpriseID());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
			et.setActiveFlagID(activeFlag);
			et.setOriginalSourceSystemID(system);
			et.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
			et.createDefaultSecurity(system, identityToken);
			
			return et;
		}
		else
		{
			return findProductTypeForProduct(productsType, system, identityToken);
		}
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IProductType<?, ?> findProductTypeForProduct(String productType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new ProductType().builder(entityManager)
		                        .withName(productType)
		                        .withEnterprise(system.getEnterpriseID())
		                        .inActiveRange()
		                        .inDateRange()
		                        // .canRead(system, identityToken)
		                        .get()
		                        .orElseThrow(() -> new NoSuchElementException("Product Type - " + productType + " not found"));
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IProduct<?, ?> findProduct(String productName, IClassification<?, ?> classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new Product().builder(entityManager)
		                    .withName(productName)
		                    .withClassification(classification)
		                    .inActiveRange()
		                    .inDateRange()
		                    .withEnterprise(system.getEnterpriseID())
		                    .get()
		                    .orElseThrow();
	}
	
	
	@Override
	public IProductType<?, ?> findProductTypeForProduct(IProduct<?, ?> product, IClassification<?, ?> classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return findProductTypeForProduct(product, classification.getName(), system, identityToken);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IProductType<?, ?> findProductTypeForProduct(IProduct<?, ?> product, String classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification1 = classificationService.find(classification, system, identityToken);
		return new ProductXProductType().builder(entityManager)
		                                .findLink((Product) product, null, null)
		                                .withClassification(classification1)
		                                .inActiveRange()
		                                .inDateRange()
		                                .withEnterprise(system.getEnterpriseID())
		                                .get()
		                                .orElseThrow()
		                                .getSecondary();
	}
	
	public List<IProductType<?, ?>> findProductTypes(IClassification<?, ?> classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return findProductTypes(classification.getName(), system, identityToken);
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IProductType<?, ?>> findProductTypes(String classification, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		List<IProductType<?, ?>> list = new ArrayList<>();
		List<IRelationshipValue<ProductType, IClassification<?, ?>, ?>> classifications = new ProductType().findClassifications(classification, system, identityToken);
		for (IRelationshipValue<ProductType, IClassification<?, ?>, ?> returns : classifications)
		{
			IProductType<?, ?> primary = returns.getPrimary();
			list.add(primary);
		}
		return list;
	}
	
	@Override
	public List<IProduct<?, ?>> findByProductTypes(IProductType<?, ?> type, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return findByProductTypes(type.getName(), system, identityToken);
	}
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IProduct<?, ?>> findByProductTypes(String type, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new ProductXProductType().builder(entityManager)
		                                .withEnterprise(system.getEnterpriseID())
		                                .inActiveRange()
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .withType(type, system, identityToken)
		                                .getAll()
		                                .stream()
		                                .map(ProductXProductType::getProductID)
		                                .collect(Collectors.toList());
	}
}
