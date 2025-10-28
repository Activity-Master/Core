package com.guicedee.activitymaster.fsdm.db.entities.product.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.product.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class ProductXProductTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Product,
		ProductType,
		ProductXProductTypeQueryBuilder,
		ProductXProductType,
		UUID,
		ProductXProductTypeSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ProductXProductType, Product> getPrimaryAttribute()
	{
		return ProductXProductType_.productID;
	}
	
	@Override
	public SingularAttribute<ProductXProductType, ProductType> getSecondaryAttribute()
	{
		return ProductXProductType_.productTypeID;
	}
	
	@Override
	public ProductXProductTypeQueryBuilder withType(String productTypeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (productTypeValue != null)
		{
				JoinExpression<?, ?, ?> joinExpression = new JoinExpression<>();
				join(getAttribute(ProductXProductType_.PRODUCT_TYPE_ID),JoinType.INNER,joinExpression);
				var nameFilter = joinExpression.getFilter(ProductType_.NAME, Equals, productTypeValue);
				getFilters().add(nameFilter);
		}
		return this;
	}
	
	
}
