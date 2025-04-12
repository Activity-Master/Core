package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;

import java.util.UUID;

public class ProductQueryBuilder
		extends QueryBuilderSCD<ProductQueryBuilder, Product, UUID, ProductSecurityTokenQueryBuilder>
		implements IProductQueryBuilder<ProductQueryBuilder, Product>,
		           IQueryBuilderNamesAndDescriptions<ProductQueryBuilder, Product, UUID>
{

}
