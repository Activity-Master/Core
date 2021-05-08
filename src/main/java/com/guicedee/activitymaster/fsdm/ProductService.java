package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.*;
import java.util.stream.Collectors;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

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
	
	@Override
	public IProductType<?, ?> getType()
	{
		return new ProductType();
	}
	
	@Override
	public IProduct<?, ?> createProduct(String productType, String name, String description, String code, ISystems<?, ?> system, UUID... identityToken)
	{
		
		boolean exists = new Product().builder()
		                              .withName(name)
		                              .inActiveRange()
		                              .inDateRange()
		                              .withEnterprise(enterprise)
		                              .getCount() > 0;
		if (exists)
		{
			return new Product().builder()
			                    .withName(name)
			                    .inActiveRange()
			                    .inDateRange()
			                    .withEnterprise(enterprise)
			                    .get()
			                    .orElseThrow();
		}
		
		Product product = new Product();
		product.setName(name);
		product.setProductCode(code);
		product.setDescription(description);
		
		product.setEnterpriseID(enterprise);
		product.setSystemID(system);
		product.setOriginalSourceSystemID(system);
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
		product.setActiveFlagID(activeFlag);
		product.persist();
		product.createDefaultSecurity(system, identityToken);
		
		IProductType<?, ?> pType = createProductType(productType, productType, system, identityToken);
		
		product.addProductTypes(productType, STRING_EMPTY, NoClassification.toString(), system, identityToken);
		
		return product;
	}
	
	@Override
	public IProduct<?, ?> findProduct(String name, ISystems<?, ?> system, UUID... identityToken)
	{
		return new Product().builder()
		                    .withName(name)
		                    .inActiveRange()
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get()
		                    .orElseThrow();
	}
	
	@Override
	public IProductType<?, ?> createProductType(String productsType, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		ProductType et = new ProductType();
		
		boolean exists = et.builder()
		                   .withName(productsType)
		                   .withEnterprise(enterprise)
		                   .inActiveRange()
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(productsType);
			et.setDescription(description);
			et.setSystemID(system);
			et.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
			et.setActiveFlagID(activeFlag);
			et.setOriginalSourceSystemID(system);
			et.persist();
			et.createDefaultSecurity(system, identityToken);
			
			return et;
		}
		else
		{
			return findProductTypeForProduct(productsType, system, identityToken);
		}
	}
	
	@Override
	public IProductType<?, ?> findProductTypeForProduct(String productType, ISystems<?, ?> system, UUID... identityToken)
	{
		return new ProductType().builder()
		                        .withName(productType)
		                        .withEnterprise(enterprise)
		                        .inActiveRange()
		                        .inDateRange()
		                        // .canRead(system, identityToken)
		                        .get()
		                        .orElseThrow(() -> new NoSuchElementException("Product Type - " + productType + " not found"));
	}
	
	@Override
	public IProduct<?, ?> findProduct(String productName, IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken)
	{
		return new Product().builder()
		                    .withName(productName)
		                    .withClassification(classification)
		                    .inActiveRange()
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get()
		                    .orElseThrow();
	}
	
	@Override
	public IProductType<?, ?> findProductTypeForProduct(IProduct<?, ?> product, IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken)
	{
		return findProductTypeForProduct(product, classification.getName(), system, identityToken);
	}
	
	@Override
	public IProductType<?, ?> findProductTypeForProduct(IProduct<?, ?> product, String classification, ISystems<?, ?> system, UUID... identityToken)
	{
		IClassification<?, ?> classification1 = classificationService.find(classification, system, identityToken);
		return new ProductXProductType().builder()
		                                .findLink((Product) product, null, null)
		                                .withClassification(classification1)
		                                .inActiveRange()
		                                .inDateRange()
		                                .withEnterprise(enterprise)
		                                .get()
		                                .orElseThrow()
		                                .getSecondary();
	}
	
	public List<IProductType<?, ?>> findProductTypes(IClassification<?, ?> classification, ISystems<?, ?> system, UUID... identityToken)
	{
		return findProductTypes(classification.getName(), system, identityToken);
	}
	
	@Override
	public List<IProductType<?, ?>> findProductTypes(String classification, ISystems<?, ?> system, UUID... identityToken)
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
	public List<IProduct<?, ?>> findByProductTypes(IProductType<?, ?> type, ISystems<?, ?> system, UUID... identityToken)
	{
		return findByProductTypes(type.getName(), system, identityToken);
	}
	
	@Override
	public List<IProduct<?, ?>> findByProductTypes(String type, ISystems<?, ?> system, UUID... identityToken)
	{
		return new ProductXProductType().builder()
		                                .withEnterprise(enterprise)
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
