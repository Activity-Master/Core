package com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ActiveFlagXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ActiveFlag, Classification, ActiveFlagXClassificationQueryBuilder,
		ActiveFlagXClassification, UUID,ActiveFlagXClassificationSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<WarehouseSCDTable, ActiveFlag> getPrimaryAttribute()
	{
		return ActiveFlagXClassification_.activeFlagID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ActiveFlagXClassification_.classificationID;
	}
}
