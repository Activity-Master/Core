package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRules;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRulesSecurityToken;

import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementXRules_;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import jakarta.persistence.metamodel.Attribute;

public class ArrangementXRulesQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Rules, ArrangementXRulesQueryBuilder,
						                                              ArrangementXRules, java.util.UUID, ArrangementXRulesSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ArrangementXRules_.arrangement;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ArrangementXRules_.rulesID;
	}
}
