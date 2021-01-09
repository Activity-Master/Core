package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRulesType;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRulesTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRulesType_;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXRulesTypeQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, RulesType, ArrangementXRulesTypeQueryBuilder,
						                                              ArrangementXRulesType, java.util.UUID, ArrangementXRulesTypeSecurityToken>
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
