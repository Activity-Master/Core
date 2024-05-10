package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Arrangement, Classification, ArrangementXClassificationQueryBuilder,
		ArrangementXClassification, java.lang.String,ArrangementXClassificationSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ArrangementXClassification, Arrangement> getPrimaryAttribute()
	{
		return ArrangementXClassification_.arrangementID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ArrangementXClassification_.classificationID;
	}
}
