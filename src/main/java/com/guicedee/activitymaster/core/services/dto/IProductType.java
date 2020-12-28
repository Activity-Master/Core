package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.ProductType;
import com.guicedee.activitymaster.core.db.entities.product.ProductTypeXClassification;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsEnterprise;
import com.guicedee.activitymaster.core.services.capabilities.IHasActiveFlags;
import com.guicedee.activitymaster.core.services.capabilities.INameAndDescription;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;

public interface IProductType<J extends IProductType<J>>
		extends INameAndDescription<J>,
		        IProductTypeValue,
		        IContainsEnterprise<J>,
		        IHasActiveFlags<J>,
		        IContainsClassifications<ProductType, Classification, ProductTypeXClassification, IClassificationValue<?>, IProductType<?>,IClassification<?>,ProductType>
{

}
