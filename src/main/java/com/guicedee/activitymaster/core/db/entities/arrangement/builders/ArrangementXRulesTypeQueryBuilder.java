package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXRulesTypeQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, RulesType, ArrangementXRulesTypeQueryBuilder,
						                                              ArrangementXRulesType, java.util.UUID>
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
