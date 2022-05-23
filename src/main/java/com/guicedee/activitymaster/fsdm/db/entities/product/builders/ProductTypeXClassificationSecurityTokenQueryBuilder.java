package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductTypeXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ProductTypeXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductTypeXClassificationSecurityTokenQueryBuilder, ProductTypeXClassificationSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductTypeXClassificationSecurityToken_.base;
	}
}
