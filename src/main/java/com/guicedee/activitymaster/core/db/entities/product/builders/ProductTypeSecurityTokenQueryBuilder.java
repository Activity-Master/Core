package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.product.ProductTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductTypeSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ProductTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductTypeSecurityTokenQueryBuilder, ProductTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductTypeSecurityToken_.base;
	}
}
