package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ProductXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, ResourceItem, ProductXResourceItemQueryBuilder,
		ProductXResourceItem, UUID,
		ProductXResourceItemSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ProductXResourceItem, Product> getPrimaryAttribute()
	{
		return ProductXResourceItem_.productID;
	}
	
	@Override
	public SingularAttribute<ProductXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return ProductXResourceItem_.resourceItemID;
	}
}
