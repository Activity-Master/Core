package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.*;
import jakarta.persistence.metamodel.Attribute;

public class ProductTypeXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ProductType, Classification, ProductTypeXClassificationQueryBuilder,
				                                              ProductTypeXClassification, java.util.UUID, ProductTypeXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ProductTypeXClassification_.productTypeID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ProductTypeXClassification_.classificationID;
	}
}
