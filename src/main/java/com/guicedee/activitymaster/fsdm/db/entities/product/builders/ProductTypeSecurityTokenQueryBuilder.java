package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ProductTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductTypeSecurityTokenQueryBuilder, ProductTypeSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductTypeSecurityToken_.base;
	}
}
