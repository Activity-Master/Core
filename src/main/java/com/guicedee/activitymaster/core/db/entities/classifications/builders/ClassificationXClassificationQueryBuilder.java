package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXClassification_;

import jakarta.persistence.metamodel.Attribute;

public class ClassificationXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Classification, Classification, ClassificationXClassificationQueryBuilder,
						                                              ClassificationXClassification, java.util.UUID, ClassificationXClassificationSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return ClassificationXClassification_.parentClassificationID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return ClassificationXClassification_.childClassificationID;
	}

}
