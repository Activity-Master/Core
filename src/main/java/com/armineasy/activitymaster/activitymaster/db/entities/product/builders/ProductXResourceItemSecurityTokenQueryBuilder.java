package com.armineasy.activitymaster.activitymaster.db.entities.product.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItemSecurityToken_;

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
