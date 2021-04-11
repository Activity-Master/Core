package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConceptXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConceptXClassificationSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ClassificationDataConceptXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationDataConceptXClassificationSecurityTokenQueryBuilder, ClassificationDataConceptXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationDataConceptXClassificationSecurityToken_.base;
	}
}
