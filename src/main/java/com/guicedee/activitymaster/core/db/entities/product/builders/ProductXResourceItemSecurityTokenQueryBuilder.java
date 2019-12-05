package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ProductXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductXResourceItemSecurityTokenQueryBuilder, ProductXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductXResourceItemSecurityToken_.base;
	}
}
