package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXResourceItem;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;

public class ClassificationXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<Classification, ResourceItem, ClassificationXResourceItemQueryBuilder,
						                                              ClassificationXResourceItem, java.util.UUID, ClassificationXResourceItemSecurityToken>
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
