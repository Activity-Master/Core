package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;

import java.util.List;
import java.util.UUID;

public interface IProductService<J extends IProductService<J>>
{
	public IProduct<?> createProduct(String productType,String name, String description, String code, ISystems<?> originatingSystem, UUID... identityToken);
	
	IProductType<?> createProductType(IProductTypeValue<?> productType, ISystems<?> originatingSystem, UUID... identityToken);
	
	IProduct<?> findProduct(String name, IEnterprise<?> enterprise, UUID... identityToken);
	
	IProductType<?> createProductType(String productType,String description, ISystems<?> originatingSystem, UUID... identityToken);
	
	IProductType<?> findProductType(IProductTypeValue<?> productType, IEnterprise<?> enterprise, UUID... identityToken);
	
	IProductType<?> findProductType(String productType, IEnterprise<?> enterprise, UUID... identityToken);
	
	IProduct<?> findProduct(String productName, IClassification<?> classificationDataConceptType, IEnterprise<?> enterpriseID, UUID... identityToken);
	
	List<IProductType<?>> findProductTypes(IClassification<?> classification, ISystems<?> system, UUID... identityToken);
	
	List<IProduct<?>> findByProductTypes(IProductType<?> type, ISystems<?> system, UUID... identityToken);
	
	List<IProduct<?>> findByProductTypes(String type, ISystems<?> system, UUID... identityToken);
}
