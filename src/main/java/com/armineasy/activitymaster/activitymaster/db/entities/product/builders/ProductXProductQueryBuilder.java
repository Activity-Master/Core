package com.armineasy.activitymaster.activitymaster.db.entities.product.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXProduct;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXProduct_;

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
