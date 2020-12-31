package com.guicedee.activitymaster.core.db.entities.rules.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Arrangement, RulesXArrangementQueryBuilder,
		RulesXArrangement, java.util.UUID, RulesXArrangementsSecurityToken>
{
	@Override
	public SingularAttribute<RulesXArrangement, Rules> getPrimaryAttribute()
	{
		return RulesXArrangement_.rulesID;
	}

	@Override
	public SingularAttribute<RulesXArrangement, Arrangement> getSecondaryAttribute()
	{
		return RulesXArrangement_.arrangementID;
	}
}
