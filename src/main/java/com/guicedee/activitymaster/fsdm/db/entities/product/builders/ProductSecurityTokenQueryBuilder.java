package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductSecurityTokenQueryBuilder, ProductSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductSecurityToken_.base;
	}
}
