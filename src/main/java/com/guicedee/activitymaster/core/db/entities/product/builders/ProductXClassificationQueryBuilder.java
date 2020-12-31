package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassification;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassification_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ProductXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, Classification, ProductXClassificationQueryBuilder,
				                                              ProductXClassification, java.util.UUID, ProductXClassificationSecurityToken>
{
	@Override
	public SingularAttribute<ProductXClassification, Product> getPrimaryAttribute()
	{
		return ProductXClassification_.productID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ProductXClassification_.classificationID;
	}
}
