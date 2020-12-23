package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.product.ProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductSecurityTokenQueryBuilder, ProductSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductSecurityToken_.base;
	}
}
