package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXProductSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXProductSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ProductXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductXProductSecurityTokenQueryBuilder, ProductXProductSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductXProductSecurityToken_.base;
	}
}
