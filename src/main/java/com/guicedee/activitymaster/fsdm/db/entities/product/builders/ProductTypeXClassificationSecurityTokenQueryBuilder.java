package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductTypeXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductTypeXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ProductTypeXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductTypeXClassificationSecurityTokenQueryBuilder, ProductTypeXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductTypeXClassificationSecurityToken_.base;
	}
}
