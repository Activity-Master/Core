package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductType;

import java.util.UUID;

public class ProductTypeQueryBuilder
		extends QueryBuilderSCD<ProductTypeQueryBuilder, ProductType, UUID,ProductTypeSecurityTokenQueryBuilder>
		implements IProductTypeQueryBuilder<ProductTypeQueryBuilder, ProductType>,
		           IQueryBuilderNamesAndDescriptions<ProductTypeQueryBuilder,ProductType, UUID>
{

}
