package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.WarehouseClassificationRelationshipTable;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.*;
import com.guicedee.activitymaster.core.db.entities.classifications.*;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

public class ClassificationDataConceptXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ClassificationDataConcept, Classification, ClassificationDataConceptXClassificationQueryBuilder,
				                                              ClassificationDataConceptXClassification, java.util.UUID, ClassificationDataConceptXClassificationSecurityToken>
{
	@Override
	public SingularAttribute<ClassificationDataConceptXClassification, ClassificationDataConcept> getPrimaryAttribute()
	{
		return ClassificationDataConceptXClassification_.classificationDataConceptID;
	}

	@Override
	public  SingularAttribute<WarehouseClassificationRelationshipTable, Classification> getSecondaryAttribute()
	{
		return ClassificationDataConceptXClassification_.classificationID;
	}
}
