package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItem;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItem_;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ClassificationDataConceptXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItemQueryBuilder,
								                                ClassificationDataConceptXResourceItem, java.util.UUID, ClassificationDataConceptXResourceItemSecurityToken>
{
	@Override
	public SingularAttribute<ClassificationDataConceptXResourceItem, ClassificationDataConcept> getPrimaryAttribute()
	{
		return ClassificationDataConceptXResourceItem_.classificationDataConceptID;
	}

	@Override
	public  SingularAttribute<ClassificationDataConceptXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return ClassificationDataConceptXResourceItem_.resourceItemID;
	}
}
