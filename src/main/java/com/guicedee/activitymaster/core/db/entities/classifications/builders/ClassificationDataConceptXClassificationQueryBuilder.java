package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.*;
import com.guicedee.activitymaster.core.db.entities.classifications.*;

import jakarta.persistence.metamodel.Attribute;

public class ClassificationDataConceptXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ClassificationDataConcept, Classification, ClassificationDataConceptXClassificationQueryBuilder,
				                                              ClassificationDataConceptXClassification, java.util.UUID, ClassificationDataConceptXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ClassificationDataConceptXClassification_.classificationDataConceptID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ClassificationDataConceptXClassification_.classificationID;
	}
}
