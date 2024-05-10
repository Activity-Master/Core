package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;

public class ProductQueryBuilder
		extends QueryBuilderSCDNameDescription<ProductQueryBuilder, Product, java.lang.String,ProductSecurityTokenQueryBuilder>
		implements IProductQueryBuilder<ProductQueryBuilder, Product>
{

}
