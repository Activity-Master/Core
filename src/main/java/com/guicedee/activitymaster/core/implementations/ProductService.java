package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductType;
import com.guicedee.activitymaster.core.db.entities.product.ProductXProductType;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IProductService;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

public class ProductService<J extends ProductService<J>>
		implements IProductService<J>
{
	@Override
	public IProduct<?> createProduct(String productType, String name, String description, String code, ISystems<?> system, UUID... identityToken)
	{
		
		boolean exists = new Product().builder()
		                              .withName(name)
		                              .inActiveRange(system.getEnterprise(), identityToken)
		                              .inDateRange()
		                              .withEnterprise(system.getEnterprise())
		                              .getCount() > 0;
		if (exists)
		{
			return new Product().builder()
			                    .withName(name)
			                    .inActiveRange(system.getEnterprise(), identityToken)
			                    .inDateRange()
			                    .withEnterprise(system.getEnterprise())
			                    .get()
			                    .orElseThrow();
		}
		
		Product product = new Product();
		product.setName(name);
		product.setProductCode(code);
		product.setDescription(description);
		
		product.setEnterpriseID((Enterprise) system.getEnterpriseID());
		product.setSystemID((Systems) system);
		product.setOriginalSourceSystemID((Systems) system);
		product.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getActiveFlag(system.getEnterpriseID(), identityToken));
		product.persist();
		product.createDefaultSecurity(system, identityToken);
		
		IProductType<?> pType = createProductType(productType, productType, system, identityToken);
		
		product.addProductType(NoClassification, pType, STRING_EMPTY, system, identityToken);
		
		return product;
	}
	
	@Override
	public IProductType<?> createProductType(IProductTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken)
	{
		return createProductType(rulesType.name(), rulesType.classificationDescription(), system, identityToken);
	}
	
	@Override
	public IProduct<?> findProduct(String name, ISystems<?> system, UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterprise();
		return new Product().builder()
		                    .withName(name)
		                    .inActiveRange(enterprise, identityToken)
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get()
		                    .orElseThrow();
	}
	
	@Override
	public IProductType<?> createProductType(String rulesType, String description, ISystems<?> system, UUID... identityToken)
	{
		ProductType et = new ProductType();
		
		boolean exists = et.builder()
		                   .withName(rulesType)
		                   .withEnterprise(system.getEnterpriseID())
		                   .inActiveRange(system.getEnterpriseID())
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(rulesType);
			et.setDescription(description);
			et.setSystemID((Systems) system);
			et.setEnterpriseID((Enterprise) system.getEnterpriseID());
			et.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                            .getActiveFlag(system.getEnterpriseID(), identityToken));
			et.setOriginalSourceSystemID((Systems) system);
			et.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				et.createDefaultSecurity(system, identityToken);
			}
			return et;
		}
		else
		{
			return findProductType(rulesType, system, identityToken);
		}
	}
	
	@Override
	public IProductType<?> findProductType(IProductTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken)
	{
		return findProductType(rulesType.classificationValue(), system, identityToken);
	}
	
	@Override
	public IProductType<?> findProductType(String productType, ISystems<?> system, UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterprise();
		return new ProductType().builder()
		                        .withName(productType)
		                        .withEnterprise(enterprise)
		                        .inActiveRange(enterprise, identityToken)
		                        .inDateRange()
		                        .canRead(system, identityToken)
		                        .get()
		                        .orElseThrow(() -> new NoSuchElementException("Product Type - " + productType + " not found"));
	}
	
	@Override
	public IProduct<?> findProduct(String productName, IClassification<?> classification, ISystems<?> system, UUID... identityToken)
	{
		IEnterprise<?> enterprise = system.getEnterprise();
		return new Product().builder()
		                    .withName(productName)
		                    .withClassification(classification)
		                    .inActiveRange(enterprise, identityToken)
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get()
		                    .orElseThrow();
	}
	
	@Override
	public IProductType<?> findProductType(IProduct<?> product, IClassification<?> classification, ISystems<?> system, UUID... identityToken)
	{
		return findProductType(product, classification.getName(), system, identityToken);
	}
	
	@Override
	public IProductType<?> findProductType(IProduct<?> product, String classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IEnterprise<?> enterprise = system.getEnterprise();
		IClassification<?> classification1 = classificationService.find(classification, system, identityToken);
		return new ProductXProductType().builder()
		                                .findLink((Product) product, null)
		                                .withClassification(classification1)
		                                .inActiveRange(enterprise, identityToken)
		                                .inDateRange()
		                                .withEnterprise(enterprise)
		                                .get()
		                                .orElseThrow()
		                                .getSecondary();
	}
	
	@Override
	public List<IProductType<?>> findProductTypes(IClassification<?> classification, ISystems<?> system, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> clazz = classificationService.find(classification.getName(), system, identityToken);
		List<IProductType<?>> list = new ArrayList<>();
		for (IRelationshipValue<IProductType<?>, IClassification<?>, ?> returns : new ProductType().findClassificationsAll((IClassificationValue<?>) clazz, system, identityToken))
		{
			IProductType<?> primary = returns.getPrimary();
			list.add(primary);
		}
		return list;
	}
	
	@Override
	public List<IProduct<?>> findByProductTypes(IProductType<?> type, ISystems<?> system, UUID... identityToken)
	{
		return findByProductTypes(type.getName(), system, identityToken);
	}
	
	@Override
	public List<IProduct<?>> findByProductTypes(String type, ISystems<?> system, UUID... identityToken)
	{
		return new ProductXProductType().builder()
		                                .withEnterprise(system.getEnterprise())
		                                .inActiveRange(system.getEnterprise(), identityToken)
		                                .inDateRange()
		                                .canRead(system, identityToken)
		                                .withType(type, system, identityToken)
		                                .getAll()
		                                .stream()
		                                .map(ProductXProductType::getProductID)
		                                .collect(Collectors.toList());
	}
}
