package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;

import java.util.UUID;

public interface IProductService<J extends IProductService<J>>
{
	public IProduct<?> createProduct(String rulesType,String name, String description, String code, ISystems<?> originatingSystem, UUID... identityToken);
	
	IProductType<?> createProductType(IProductTypeValue<?> rulesType, ISystems<?> originatingSystem, UUID... identityToken);
	
	IProduct<?> findProduct(String name, IEnterprise<?> enterprise, UUID... identityToken);
	
	IProductType<?> createProductType(String rulesType, ISystems<?> originatingSystem, UUID... identityToken);
	
	IProductType<?> findProductType(IProductTypeValue<?> rulesType, IEnterprise<?> enterprise, UUID... identityToken);
	
	IProductType<?> findProductType(String rulesType, IEnterprise<?> enterprise, UUID... identityToken);
	
	IProduct<?> findProduct(String productName, IClassification<?> classificationDataConceptType, IEnterprise<?> enterpriseID, UUID... identityToken);
}
