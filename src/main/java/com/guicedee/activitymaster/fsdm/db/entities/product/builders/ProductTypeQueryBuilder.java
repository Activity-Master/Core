package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProductTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderTable;
import com.guicedee.activitymaster.fsdm.db.entities.product.ProductType;

import java.util.UUID;

public class ProductTypeQueryBuilder
		extends QueryBuilderTable<ProductTypeQueryBuilder, ProductType, UUID>
		implements IProductTypeQueryBuilder<ProductTypeQueryBuilder,ProductType>
{

}
