package com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.persistence.metamodel.SingularAttribute;

public class ArrangementTypeXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ArrangementType, Classification, ArrangementTypeXClassificationQueryBuilder,
		ArrangementTypeXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<ArrangementTypeXClassification, ArrangementType> getPrimaryAttribute()
	{
		return ArrangementTypeXClassification_.arrangementID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ArrangementTypeXClassification_.classificationID;
	}
}
