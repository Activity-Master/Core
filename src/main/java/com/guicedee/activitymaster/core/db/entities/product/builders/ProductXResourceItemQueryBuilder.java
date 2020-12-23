package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItem;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;

public class ProductXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, ResourceItem, ProductXResourceItemQueryBuilder,
				                                              ProductXResourceItem, java.util.UUID, ProductXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ProductXResourceItem_.productID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ProductXResourceItem_.resourceItemID;
	}
}
