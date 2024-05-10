package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesTypeXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<RulesType, Classification, RulesTypeXClassificationQueryBuilder,
		RulesTypeXClassification, java.lang.String,RulesTypeXClassificationSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<RulesTypeXClassification, RulesType> getPrimaryAttribute()
	{
		return RulesTypeXClassification_.rulesTypeID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return RulesTypeXClassification_.classificationID;
	}
}
