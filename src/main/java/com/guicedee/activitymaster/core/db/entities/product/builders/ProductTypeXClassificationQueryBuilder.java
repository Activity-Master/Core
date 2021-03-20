package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class ProductTypeXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ProductType, Classification, ProductTypeXClassificationQueryBuilder,
				                                              ProductTypeXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<ProductTypeXClassification, ProductType> getPrimaryAttribute()
	{
		return ProductTypeXClassification_.productTypeID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ProductTypeXClassification_.classificationID;
	}
}
