package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesType;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ArrangementXRulesTypeQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, RulesType, ArrangementXRulesTypeQueryBuilder,
		ArrangementXRulesType, UUID,ArrangementXRulesTypeSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ArrangementXRulesType, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXRulesType_.arrangement;
	}
	
	@Override
	public SingularAttribute<ArrangementXRulesType, RulesType> getSecondaryAttribute()
	{
		return ArrangementXRulesType_.rulesTypeID;
	}
}
