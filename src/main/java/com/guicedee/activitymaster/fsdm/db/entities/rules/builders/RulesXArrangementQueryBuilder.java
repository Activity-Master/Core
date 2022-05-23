package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.Arrangement;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Arrangement, RulesXArrangementQueryBuilder,
		RulesXArrangement, java.lang.String>
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
