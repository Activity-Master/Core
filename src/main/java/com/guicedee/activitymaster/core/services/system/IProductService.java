package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;

import java.util.List;
import java.util.UUID;

public interface IProductService<J extends IProductService<J>>
{
	String ProductSystemName = "Products System";
	
	IProduct<?> createProduct(String productType,String name, String description, String code, ISystems<?> system, UUID... identityToken);
	
	IProductType<?> createProductType(IProductTypeValue<?> productType, ISystems<?> system, UUID... identityToken);
	
	IProduct<?> findProduct(String name, ISystems<?> system, UUID... identityToken);
	
	IProductType<?> createProductType(String productType,String description, ISystems<?> system, UUID... identityToken);
	
	IProductType<?> findProductType(IProductTypeValue<?> productType,ISystems<?> system, UUID... identityToken);
	
	IProductType<?> findProductType(String productType, ISystems<?> system, UUID... identityToken);
	
	IProduct<?> findProduct(String productName, IClassification<?> classificationDataConceptType, ISystems<?> system, UUID... identityToken);
	
	IProductType<?> findProductType(IProduct<?> product, IClassification<?> classification, ISystems<?> system, UUID... identityToken);
	
	IProductType<?> findProductType(IProduct<?> product, String classification, ISystems<?> system, UUID... identityToken);
	
	List<IProductType<?>> findProductTypes(IClassification<?> classification, ISystems<?> system, UUID... identityToken);
	
	List<IProduct<?>> findByProductTypes(IProductType<?> type, ISystems<?> system, UUID... identityToken);
	
	List<IProduct<?>> findByProductTypes(String type, ISystems<?> system, UUID... identityToken);
}
