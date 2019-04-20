package com.armineasy.activitymaster.activitymaster.db.entities.product.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXProductSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ProductXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductXProductSecurityTokenQueryBuilder, ProductXProductSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductXProductSecurityToken_.base;
	}
}
