package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import jakarta.persistence.metamodel.SingularAttribute;

import static com.entityassist.enumerations.Operand.*;

public class ProductXProductTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Product,
		ProductType,
		ProductXProductTypeQueryBuilder,
		ProductXProductType,
		java.lang.String,
		ProductXProductTypeSecurityTokenQueryBuilder>
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
	public ProductXProductTypeQueryBuilder withType(String productTypeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (!Strings.isNullOrEmpty(productTypeValue))
		{
			IProductService<?> productService = com.guicedee.client.IGuiceContext.get(IProductService.class);
			ProductType pt = (ProductType) productService.findProductTypeForProduct(productTypeValue, system, identityToken);
			where(ProductXProductType_.productTypeID, Equals, pt);
		}
		return this;
	}
}
