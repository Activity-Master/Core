package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.product.*;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;
import com.guicedee.activitymaster.core.services.system.IProductService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class ProductXProductTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Product,
		ProductType,
		ProductXProductTypeQueryBuilder,
		ProductXProductType,
		IProductTypeValue<?>,
		java.util.UUID,
		ProductXProductTypeSecurityToken>
{
	@Override
	public SingularAttribute<ProductXProductType, Product> getPrimaryAttribute()
	{
		return ProductXProductType_.productID;
	}
	
	@Override
	public SingularAttribute<ProductXProductType, ProductType> getSecondaryAttribute()
	{
		return ProductXProductType_.productTypeID;
	}
	
	@Override
	public ProductXProductTypeQueryBuilder withType(String productTypeValue, ISystems<?> system, UUID... identityToken)
	{
		if(!Strings.isNullOrEmpty(productTypeValue))
		{
			IProductService<?> productService = GuiceContext.get(IProductService.class);
			ProductType pt = (ProductType) productService.findProductType(productTypeValue, system, identityToken);
			where(ProductXProductType_.productTypeID, Equals, pt);
		}
		return this;
	}
}
