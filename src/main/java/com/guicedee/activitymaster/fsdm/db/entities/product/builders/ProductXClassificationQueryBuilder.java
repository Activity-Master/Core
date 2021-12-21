package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class ProductXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, Classification, ProductXClassificationQueryBuilder,
		ProductXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<ProductXClassification, Product> getPrimaryAttribute()
	{
		return ProductXClassification_.productID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ProductXClassification_.classificationID;
	}
}
