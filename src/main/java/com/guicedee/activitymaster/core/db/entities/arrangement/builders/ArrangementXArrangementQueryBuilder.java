package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXArrangementQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Arrangement, ArrangementXArrangementQueryBuilder,
						                                              ArrangementXArrangement, java.util.UUID>
{
	@Override
	public  SingularAttribute<ArrangementXArrangement, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXArrangement_.parentArrangementID;
	}

	@Override
	public SingularAttribute<ArrangementXArrangement, Arrangement> getSecondaryAttribute()
	{
		return ArrangementXArrangement_.childArrangementID;
	}
}
