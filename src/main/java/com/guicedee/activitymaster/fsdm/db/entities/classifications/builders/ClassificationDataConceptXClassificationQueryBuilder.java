package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class ClassificationDataConceptXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ClassificationDataConcept, Classification, ClassificationDataConceptXClassificationQueryBuilder,
		ClassificationDataConceptXClassification, java.lang.String>
{
	@Override
	public SingularAttribute<ClassificationDataConceptXClassification, ClassificationDataConcept> getPrimaryAttribute()
	{
		return ClassificationDataConceptXClassification_.classificationDataConceptID;
	}
	
	@Override
	public SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ClassificationDataConceptXClassification_.classificationID;
	}
}
