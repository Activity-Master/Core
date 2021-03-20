package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.products.IProductQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.entities.product.Product;

public class ProductQueryBuilder
		extends QueryBuilderSCDNameDescription<ProductQueryBuilder, Product, java.util.UUID>
		implements IProductQueryBuilder<ProductQueryBuilder,Product>
{

}
