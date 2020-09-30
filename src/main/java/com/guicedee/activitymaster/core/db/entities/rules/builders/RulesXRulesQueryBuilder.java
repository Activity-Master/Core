package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRules_;

import javax.persistence.metamodel.Attribute;

public class RulesXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Rules, RulesXRulesQueryBuilder,
				                                              RulesXRules, Long, RulesXRulesSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return RulesXRules_.parentRulesID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return RulesXRules_.childRulesID;
	}
}
