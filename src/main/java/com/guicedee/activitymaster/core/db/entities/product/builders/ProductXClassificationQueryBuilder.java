package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassification;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassification_;

import jakarta.persistence.metamodel.Attribute;

public class ProductXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, Classification, ProductXClassificationQueryBuilder,
				                                              ProductXClassification, java.util.UUID, ProductXClassificationSecurityToken>
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
