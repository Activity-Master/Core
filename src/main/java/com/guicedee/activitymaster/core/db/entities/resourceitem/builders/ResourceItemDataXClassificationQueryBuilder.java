package com.guicedee.activitymaster.core.db.entities.resourceitem.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemData;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataXClassification;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItemDataXClassification_;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ResourceItemDataXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItemData, Classification, ResourceItemDataXClassificationQueryBuilder,
						                                              ResourceItemDataXClassification, java.util.UUID, ResourceItemDataXClassificationSecurityToken>
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
