package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.product.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class ProductXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, Product, ProductXProductQueryBuilder,
				                                              ProductXProduct, java.util.UUID>
{
	@Override
	public SingularAttribute<ProductXProduct, Product> getPrimaryAttribute()
	{
		return ProductXProduct_.parentProductID;
	}

	@Override
	public SingularAttribute<ProductXProduct, Product> getSecondaryAttribute()
	{
		return ProductXProduct_.childProductID;
	}
}
