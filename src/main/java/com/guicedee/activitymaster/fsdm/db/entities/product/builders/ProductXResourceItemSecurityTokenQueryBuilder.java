package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ProductXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductXResourceItemSecurityTokenQueryBuilder, ProductXResourceItemSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductXResourceItemSecurityToken_.base;
	}
}
