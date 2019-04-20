package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.*;

import javax.persistence.metamodel.Attribute;

public class ClassificationDataConceptXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<ClassificationDataConcept, Classification, ClassificationDataConceptXClassificationQueryBuilder,
				                                              ClassificationDataConceptXClassification, Long, ClassificationDataConceptXClassificationSecurityToken>
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
