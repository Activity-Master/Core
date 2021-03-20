package com.guicedee.activitymaster.core.db.entities.arrangement.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Classification, ArrangementXClassificationQueryBuilder,
						                                              ArrangementXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<ArrangementXClassification, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXClassification_.arrangementID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ArrangementXClassification_.classificationID;
	}
}
