package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class RulesXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Product, RulesXProductQueryBuilder,
		RulesXProduct, java.util.UUID>
{
	@Override
	public SingularAttribute<RulesXProduct, Rules> getPrimaryAttribute()
	{
		return RulesXProduct_.rulesID;
	}

	@Override
	public SingularAttribute<RulesXProduct, Product> getSecondaryAttribute()
	{
		return RulesXProduct_.productID;
	}
	
	
	@jakarta.validation.constraints.NotNull
	public RulesXProductQueryBuilder withClassification(String classification, String value, ISystems<?,?> system, UUID...identityToken)
	{
		JoinExpression joinExpression = new JoinExpression();
		RulesXProductQueryBuilder builder =
				new RulesXProduct()
						.builder()
						.withClassification(classification,system)
						.withValue(value)
						.inActiveRange(system.getEnterpriseID())
						.inDateRange()
						.withEnterprise(system.getEnterpriseID());
		
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(RulesXProduct_.value, Equals, value);
		}
		
		join(Rules_.products,
				builder,
				JoinType.INNER, joinExpression);
		return this;
	}
}
