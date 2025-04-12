package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ProductXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductXClassificationSecurityTokenQueryBuilder, ProductXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductXClassificationSecurityToken_.base;
	}
}
