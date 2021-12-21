package com.guicedee.activitymaster.fsdm.db.entities.rules.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.rules.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class RulesXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Rules, Classification, RulesXClassificationQueryBuilder,
		RulesXClassification, java.util.UUID>
{
	@Override
	public SingularAttribute<RulesXClassification, Rules> getPrimaryAttribute()
	{
		return RulesXClassification_.rulesID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return RulesXClassification_.classificationID;
	}
}
