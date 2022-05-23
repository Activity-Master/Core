package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class ProductTypeXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ProductType, Classification, ProductTypeXClassificationQueryBuilder,
		ProductTypeXClassification, java.lang.String>
{
	@Override
	public SingularAttribute<ProductTypeXClassification, ProductType> getPrimaryAttribute()
	{
		return ProductTypeXClassification_.productTypeID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ProductTypeXClassification_.classificationID;
	}
}
