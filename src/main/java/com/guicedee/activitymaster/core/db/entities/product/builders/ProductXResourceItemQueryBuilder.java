package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.product.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

public class ProductXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, ResourceItem, ProductXResourceItemQueryBuilder,
				                                              ProductXResourceItem, java.util.UUID>
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
