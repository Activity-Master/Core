package com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItemXClassification_;

import javax.persistence.metamodel.Attribute;

public class ResourceItemXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ResourceItem, Classification, ResourceItemXClassificationQueryBuilder,
				                                              ResourceItemXClassification, Long, ResourceItemXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ResourceItemXClassification_.resourceItemID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ResourceItemXClassification_.classificationID;
	}
}
