package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import jakarta.persistence.metamodel.Attribute;

public class RulesXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Arrangement, RulesXArrangementQueryBuilder,
		RulesXArrangement, java.util.UUID, RulesXArrangementsSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return RulesXArrangement_.rulesID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return RulesXArrangement_.arrangementID;
	}
}
