package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Arrangement, ArrangementXArrangementQueryBuilder,
		ArrangementXArrangement, java.lang.String>
{
	@Override
	public SingularAttribute<ArrangementXArrangement, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXArrangement_.parentArrangementID;
	}
	
	@Override
	public SingularAttribute<ArrangementXArrangement, Arrangement> getSecondaryAttribute()
	{
		return ArrangementXArrangement_.childArrangementID;
	}
}
