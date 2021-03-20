package com.guicedee.activitymaster.core.db.entities.systems.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.systems.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class SystemsXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Systems, Classification, SystemsXClassificationQueryBuilder,
				                                              SystemXClassification, java.util.UUID>
{
	@SuppressWarnings("rawtypes")
	@Override
	public SingularAttribute<WarehouseSCDTable, Systems> getPrimaryAttribute()
	{
		return SystemXClassification_.systemID;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return SystemXClassification_.classificationID;
	}
}
