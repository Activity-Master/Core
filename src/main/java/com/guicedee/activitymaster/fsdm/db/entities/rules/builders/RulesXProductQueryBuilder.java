package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.product.Product;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import static com.entityassist.enumerations.Operand.*;

public class RulesXProductQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Product, RulesXProductQueryBuilder,
		RulesXProduct, java.lang.String>
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
	public RulesXProductQueryBuilder withClassification(String classification, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		JoinExpression joinExpression = new JoinExpression();
		RulesXProductQueryBuilder builder =
				new RulesXProduct()
						.builder()
						.withClassification(classification, system)
						.withValue(value)
						.inActiveRange()
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
