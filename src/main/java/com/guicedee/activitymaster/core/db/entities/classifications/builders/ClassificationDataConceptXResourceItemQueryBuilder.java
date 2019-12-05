package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

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
