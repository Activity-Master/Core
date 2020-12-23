package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductType;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IProductService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

public class ProductService<J extends ProductService<J>> implements IProductService<J>
{
	@Override
	public IProduct<?> createProduct(String rulesType,String name, String description, String code, ISystems<?> originatingSystem, UUID... identityToken)
	{
		
		boolean exists = new Product().builder()
				.withName(name)
				.inActiveRange(originatingSystem.getEnterprise(),identityToken)
				.inDateRange()
				.withEnterprise(originatingSystem.getEnterprise())
				.getCount() > 0;
		if(exists)
		{
			return new Product().builder()
			                    .withName(name)
			                    .inActiveRange(originatingSystem.getEnterprise(), identityToken)
			                    .inDateRange()
			                    .withEnterprise(originatingSystem.getEnterprise())
			                    .get()
			                    .orElseThrow();
		}
		
		Product product = new Product();
		product.setName(name);
		product.setProductCode(code);
		product.setDescription(description);
		
		product.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
		product.setSystemID((Systems) originatingSystem);
		product.setOriginalSourceSystemID((Systems) originatingSystem);
		product.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class)
				.getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
		product.persist();
		product.createDefaultSecurity(originatingSystem, identityToken);
		
		IProductType<?> productType = findProductType(rulesType, originatingSystem.getEnterprise(), identityToken);
		product.add(NoClassification, productType, STRING_EMPTY, originatingSystem, identityToken);
		
		return product;
	}
	@Override
	public IProductType<?> createProductType(IProductTypeValue<?> rulesType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		return createProductType(rulesType.name(), originatingSystem, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "FindProductByName")
	public IProduct<?> findProduct(@CacheKey String name, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return new Product().builder()
		             .withName(name)
		             .inActiveRange(enterprise, identityToken)
		             .inDateRange()
		             .withEnterprise(enterprise)
		             .get()
		             .orElseThrow();
	}
	
	@Override
	public IProductType<?> createProductType(String rulesType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		ProductType et = new ProductType();
		
		boolean exists = et.builder()
		                   .withName(rulesType)
		                   .withEnterprise(originatingSystem.getEnterpriseID())
		                   .inActiveRange(originatingSystem.getEnterpriseID())
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(rulesType);
			et.setDescription(rulesType);
			et.setSystemID((Systems) originatingSystem);
			et.setEnterpriseID((Enterprise) originatingSystem.getEnterpriseID());
			et.setActiveFlagID((ActiveFlag) GuiceContext.get(IActiveFlagService.class)
			                                            .getActiveFlag(originatingSystem.getEnterpriseID(), identityToken));
			et.setOriginalSourceSystemID((Systems) originatingSystem);
			et.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				et.createDefaultSecurity(originatingSystem, identityToken);
			}
			return et;
		}
		else
		{
			return findProductType(rulesType, originatingSystem.getEnterprise(), identityToken);
		}
	}
	
	@Override
	@CacheResult(cacheName = "ProductTypes")
	public IProductType<?> findProductType(@CacheKey IProductTypeValue<?> rulesType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return findProductType(rulesType.classificationValue(), enterprise, identityToken);
	}
	
	@Override
	@CacheResult(cacheName = "ProductTypesString")
	public IProductType<?> findProductType(@CacheKey String rulesType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... identityToken)
	{
		return new ProductType().builder()
		                      .withName(rulesType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise, identityToken)
		                      .inDateRange()
		                      .canRead(enterprise, identityToken)
		                      .get()
		                      .orElseThrow();
	}
	
	@Override
	@CacheResult(cacheName = "FindProductWithClassification")
	public IProduct<?> findProduct(@CacheKey String productName,@CacheKey  IClassification<?> classification,@CacheKey  IEnterprise<?> enterprise, UUID... identityToken)
	{
		return new Product().builder()
		                    .withName(productName)
		                    .withClassification(classification)
		                    .inActiveRange(enterprise, identityToken)
		                    .inDateRange()
		                    .withEnterprise(enterprise)
		                    .get()
		                    .orElseThrow();
	}
}
