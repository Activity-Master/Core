package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ClassificationDataConceptXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItemQueryBuilder,
		ClassificationDataConceptXResourceItem, UUID,ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ClassificationDataConceptXResourceItem, ClassificationDataConcept> getPrimaryAttribute()
	{
		return ClassificationDataConceptXResourceItem_.classificationDataConceptID;
	}
	
	@Override
	public SingularAttribute<ClassificationDataConceptXResourceItem, ResourceItem> getSecondaryAttribute()
	{
		return ClassificationDataConceptXResourceItem_.resourceItemID;
	}
}
