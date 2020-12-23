package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.rules.*;

import jakarta.persistence.metamodel.Attribute;

public class RulesXRulesTypeQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, RulesType, RulesXRulesTypeQueryBuilder,
		RulesXRulesType, java.util.UUID, RulesXRulesTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return RulesXRulesType_.rulesID;
	}
	
	@Override
	public Attribute getSecondaryAttribute()
	{
		return RulesXRulesType_.rulesTypeID;
	}
}
