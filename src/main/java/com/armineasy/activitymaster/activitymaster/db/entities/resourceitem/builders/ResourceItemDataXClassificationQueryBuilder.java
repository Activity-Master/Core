package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemData;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemDataXClassification_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemDataXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItemData, Classification, ResourceItemDataXClassificationQueryBuilder,
				                                              ResourceItemDataXClassification, Long, ResourceItemDataXClassificationSecurityToken>
{

	@Override
	public Attribute getPrimaryAttribute()
	{
		return ResourceItemDataXClassification_.resourceItemDataID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ResourceItemDataXClassification_.classificationID;
	}
}
