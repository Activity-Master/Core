package com.armineasy.activitymaster.activitymaster.db.entities.product.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXClassification_;

import javax.persistence.metamodel.Attribute;

public class ProductXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, Classification, ProductXClassificationQueryBuilder,
				                                              ProductXClassification, Long, ProductXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ProductXClassification_.productID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ProductXClassification_.classificationID;
	}
}
