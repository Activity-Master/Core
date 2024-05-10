package com.guicedee.activitymaster.fsdm.db.entities.systems.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.systems.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class SystemsXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Systems, Classification, SystemsXClassificationQueryBuilder,
		SystemsXClassification, java.lang.String,SystemXClassificationSecurityTokenQueryBuilder>
{
	@SuppressWarnings("rawtypes")
	@Override
	public SingularAttribute<WarehouseSCDTable, Systems> getPrimaryAttribute()
	{
		return SystemsXClassification_.systemID;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return SystemsXClassification_.classificationID;
	}
}
