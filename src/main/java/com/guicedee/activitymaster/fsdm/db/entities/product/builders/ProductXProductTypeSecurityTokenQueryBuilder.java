package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXProductTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductXProductTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ProductXProductTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ProductXProductTypeSecurityTokenQueryBuilder, ProductXProductTypeSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ProductXProductTypeSecurityToken_.base;
	}
}
