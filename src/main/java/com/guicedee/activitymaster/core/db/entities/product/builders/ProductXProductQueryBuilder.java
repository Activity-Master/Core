package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductXProduct;
import com.guicedee.activitymaster.core.db.entities.product.ProductXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXProduct_;

import javax.persistence.metamodel.Attribute;

public class ProductXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, Product, ProductXProductQueryBuilder,
				                                              ProductXProduct, Long, ProductXProductSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ProductXProduct_.parentProductID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ProductXProduct_.childProductID;
	}
}
