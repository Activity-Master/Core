package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.client.services.builders.warehouse.rules.IRuleTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.abstraction.builders.assists.QueryBuilderSCDNameDescription;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import jakarta.persistence.criteria.JoinType;

import static com.entityassist.enumerations.Operand.*;

public class RulesTypeQueryBuilder
		extends QueryBuilderSCDNameDescription<RulesTypeQueryBuilder, RulesType, java.util.UUID>
		implements IRuleTypeQueryBuilder<RulesTypeQueryBuilder,RulesType>
{
	@jakarta.validation.constraints.NotNull
	public RulesTypeQueryBuilder withClassification(Classification classification, String value)
	{
		JoinExpression joinExpression = new JoinExpression();
		RulesTypeXClassificationQueryBuilder builder =
				new RulesTypeXClassification()
						.builder()
						.inActiveRange(classification.getEnterpriseID())
						.inDateRange()
						.where(RulesTypeXClassification_.classificationID, Equals, classification);
		
		if (!Strings.isNullOrEmpty(value))
		{
			builder.where(RulesTypeXClassification_.value, Equals, value);
		}
		
		join(RulesType_.classifications,
				builder,
				JoinType.INNER, joinExpression);
		return this;
	}
}
