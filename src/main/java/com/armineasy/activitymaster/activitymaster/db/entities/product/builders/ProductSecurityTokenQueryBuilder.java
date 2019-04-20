package com.armineasy.activitymaster.activitymaster.db.entities.product.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductSecurityTokenQueryBuilder, ProductSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductSecurityToken_.base;
	}
}
