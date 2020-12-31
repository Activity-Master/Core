package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRulesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.rules.RulesXRules_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Rules, RulesXRulesQueryBuilder,
				                                              RulesXRules, java.util.UUID, RulesXRulesSecurityToken>
{
	@Override
	public SingularAttribute<RulesXRules, Rules> getPrimaryAttribute()
	{
		return RulesXRules_.parentRulesID;
	}

	@Override
	public SingularAttribute<RulesXRules, Rules> getSecondaryAttribute()
	{
		return RulesXRules_.childRulesID;
	}
}
