package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import com.guicedee.activitymaster.core.services.dto.IClassification;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

import static com.entityassist.enumerations.Operand.*;

public class RulesXRulesTypeQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, RulesType, RulesXRulesTypeQueryBuilder,
		RulesXRulesType, java.util.UUID, RulesXRulesTypeSecurityToken>
{
	@Override
	public SingularAttribute<RulesXRulesType, Rules> getPrimaryAttribute()
	{
		return RulesXRulesType_.rulesID;
	}
	
	@Override
	public SingularAttribute<RulesXRulesType, RulesType> getSecondaryAttribute()
	{
		return RulesXRulesType_.rulesTypeID;
	}
	
	
	@jakarta.validation.constraints.NotNull
	public RulesXRulesTypeQueryBuilder withClassification(String classification, String value, ISystems<?> system, UUID...identityToken)
	{
		IClassificationService<?> classificationService = GuiceContext.get(IClassificationService.class);
		IClassification<?> classification1 = classificationService.find(classification, system, identityToken);
		where(RulesXRulesType_.classificationID, Equals, (Classification)classification1);
		if (!Strings.isNullOrEmpty(value))
		{
			where(RulesXRulesType_.value, Equals, value);
		}
		return this;
	}
}
