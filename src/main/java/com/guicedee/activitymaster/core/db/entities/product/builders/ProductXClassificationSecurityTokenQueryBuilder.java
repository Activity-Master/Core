package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassificationSecurityToken_;

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
