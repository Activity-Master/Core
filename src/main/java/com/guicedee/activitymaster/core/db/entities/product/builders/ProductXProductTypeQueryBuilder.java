package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.product.*;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.enumtypes.IProductTypeValue;
import com.guicedee.activitymaster.core.services.system.IProductService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.metamodel.Attribute;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class ProductXProductTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Product,
		ProductType,
		ProductXProductTypeQueryBuilder,
		ProductXProductType,
		IProductTypeValue<?>,
		Long,
		ProductXProductTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ProductXProductType_.productID;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return ProductXProductType_.productTypeID;
	}
	
	@Override
	public ProductXProductTypeQueryBuilder withType(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if(!Strings.isNullOrEmpty(typeValue))
		{
			IProductService<?> productService = GuiceContext.get(IProductService.class);
			ProductType pt = (ProductType) productService.findProductType(typeValue, enterprise, identityToken);
			where(ProductXProductType_.productTypeID, Equals, pt);
		}
		return this;
	}
}
