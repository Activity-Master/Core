package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ProductXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, Product, ProductXProductQueryBuilder,
		ProductXProduct, UUID,ProductXProductSecurityTokenQueryBuilder>
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
