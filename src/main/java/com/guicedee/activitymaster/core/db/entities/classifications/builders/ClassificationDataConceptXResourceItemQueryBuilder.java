package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.*;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import jakarta.persistence.metamodel.SingularAttribute;

public class ClassificationDataConceptXResourceItemQueryBuilder
		extends QueryBuilderRelationshipClassification<ClassificationDataConcept, ResourceItem, ClassificationDataConceptXResourceItemQueryBuilder,
								                                ClassificationDataConceptXResourceItem, java.util.UUID>
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
