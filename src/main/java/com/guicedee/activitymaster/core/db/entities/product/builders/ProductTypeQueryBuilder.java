package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.client.services.builders.warehouse.products.IProductTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.core.db.entities.product.ProductType;

import java.util.UUID;

public class ProductTypeQueryBuilder
		extends QueryBuilderTable<ProductTypeQueryBuilder, ProductType, UUID>
		implements IProductTypeQueryBuilder<ProductTypeQueryBuilder,ProductType>
{

}
