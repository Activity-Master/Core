package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Rules, RulesXRulesQueryBuilder,
				                                              RulesXRules, java.util.UUID>
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
