package com.guicedee.activitymaster.core.db.entities.systems.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassification_;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class SystemsXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Systems, Classification, SystemsXClassificationQueryBuilder,
				                                              SystemXClassification, java.util.UUID, SystemXClassificationSecurityToken>
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
