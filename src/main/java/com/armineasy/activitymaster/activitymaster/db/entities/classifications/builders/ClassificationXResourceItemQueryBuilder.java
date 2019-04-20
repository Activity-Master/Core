package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXResourceItem_;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;

import javax.persistence.metamodel.Attribute;

public class ClassificationXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Classification, ResourceItem, ClassificationXResourceItemQueryBuilder,
				                                              ClassificationXResourceItem, Long, ClassificationXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ClassificationXResourceItem_.classificationID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ClassificationXResourceItem_.resourceItemID;
	}
}
