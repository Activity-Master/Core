package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
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
