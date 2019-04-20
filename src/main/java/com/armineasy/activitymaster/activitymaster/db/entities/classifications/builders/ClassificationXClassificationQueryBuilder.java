package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXClassification_;

import javax.persistence.metamodel.Attribute;

public class ClassificationXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Classification, Classification, ClassificationXClassificationQueryBuilder,
				                                              ClassificationXClassification, Long, ClassificationXClassificationSecurityToken>
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
