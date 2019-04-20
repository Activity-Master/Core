package com.armineasy.activitymaster.activitymaster.db.entities.product.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItem_;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;

import javax.persistence.metamodel.Attribute;

public class ProductXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, ResourceItem, ProductXResourceItemQueryBuilder,
				                                              ProductXResourceItem, Long, ProductXResourceItemSecurityToken>
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
