package com.guicedee.activitymaster.fsdm.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ResourceItemDataXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItemData, Classification, ResourceItemDataXClassificationQueryBuilder,
		ResourceItemDataXClassification, UUID,ResourceItemDataXClassificationSecurityTokenQueryBuilder>
{
	
	@Override
	public SingularAttribute<ResourceItemDataXClassification, ResourceItemData> getPrimaryAttribute()
	{
		return ResourceItemDataXClassification_.resourceItemDataID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ResourceItemDataXClassification_.classificationID;
	}
}
