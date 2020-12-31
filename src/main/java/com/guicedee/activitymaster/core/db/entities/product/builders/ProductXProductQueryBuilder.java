package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductXProduct;
import com.guicedee.activitymaster.core.db.entities.product.ProductXProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXProduct_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ProductXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Product, Product, ProductXProductQueryBuilder,
				                                              ProductXProduct, java.util.UUID, ProductXProductSecurityToken>
{
	@Override
	public SingularAttribute<ProductXProduct, Product> getPrimaryAttribute()
	{
		return ProductXProduct_.parentProductID;
	}

	@Override
	public SingularAttribute<ProductXProduct, Product> getSecondaryAttribute()
	{
		return ProductXProduct_.childProductID;
	}
}
