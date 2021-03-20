package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Rules, ArrangementXRulesQueryBuilder,
						                                              ArrangementXRules, java.util.UUID>
{
	@Override
	public SingularAttribute<ArrangementXRules, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXRules_.arrangement;
	}

	@Override
	public SingularAttribute<ArrangementXRules, Rules> getSecondaryAttribute()
	{
		return ArrangementXRules_.rulesID;
	}
}
