package com.armineasy.activitymaster.activitymaster.db.entities.product.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ProductXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductXClassificationSecurityTokenQueryBuilder, ProductXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductXClassificationSecurityToken_.base;
	}
}
