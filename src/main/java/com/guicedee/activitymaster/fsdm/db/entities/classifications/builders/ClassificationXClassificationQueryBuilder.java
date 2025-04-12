package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class ClassificationXClassificationQueryBuilder
		extends QueryBuilderRelationshipClassification<Classification, Classification, ClassificationXClassificationQueryBuilder,
		ClassificationXClassification, UUID,ClassificationXClassificationSecurityTokenQueryBuilder>
{
	@Override
	public SingularAttribute<ClassificationXClassification, Classification> getPrimaryAttribute()
	{
		return ClassificationXClassification_.parentClassificationID;
	}
	
	@Override
	public SingularAttribute<ClassificationXClassification, Classification> getSecondaryAttribute()
	{
		return ClassificationXClassification_.childClassificationID;
	}
	
}
