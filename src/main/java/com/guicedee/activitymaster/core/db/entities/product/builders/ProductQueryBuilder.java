package com.guicedee.activitymaster.core.db.entities.product.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.abstraction.builders.handlers.IContainsClassificationsQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductSecurityToken;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassification;

public class ProductQueryBuilder
		extends QueryBuilderSCDNameDescription<ProductQueryBuilder, Product, java.util.UUID, ProductSecurityToken>
		implements IContainsClassificationsQueryBuilder<ProductQueryBuilder,Product,java.util.UUID, ProductXClassification>
{

}
