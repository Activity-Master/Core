package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptXResourceItem_;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;

import javax.persistence.metamodel.Attribute;

public class ClassificationDataConceptXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItemQueryBuilder,
						                                ClassificationDataConceptXResourceItem, Long, ClassificationDataConceptXResourceItemSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ClassificationDataConceptXResourceItem_.classificationDataConceptID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ClassificationDataConceptXResourceItem_.resourceItemID;
	}
}
