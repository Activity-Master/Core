package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderNamesAndDescriptions;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRuleTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.criteria.JoinType;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class RulesTypeQueryBuilder
		extends QueryBuilderSCD<RulesTypeQueryBuilder, RulesType, UUID, RulesTypeSecurityTokenQueryBuilder>
		implements IRuleTypeQueryBuilder<RulesTypeQueryBuilder, RulesType>,
		           IQueryBuilderNamesAndDescriptions<RulesTypeQueryBuilder, RulesType, UUID>
{
	
	public RulesTypeQueryBuilder withClassification(Classification classification, String value)
	{
		JoinExpression joinExpression = new JoinExpression();
		RulesTypeXClassificationQueryBuilder builder =
				isStateless() ?
				new RulesTypeXClassification()
						.builder(getEntityManagerStateless())
						.inActiveRange()
						.inDateRange()
						.where(RulesTypeXClassification_.classificationID, Equals, classification)
						:
				new RulesTypeXClassification()
						.builder(getEntityManager())
						.inActiveRange()
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
