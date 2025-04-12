package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.rules.Rules;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ArrangementXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Rules, ArrangementXRulesQueryBuilder,
		ArrangementXRules, UUID,ArrangementXRulesSecurityTokenQueryBuilder>
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
