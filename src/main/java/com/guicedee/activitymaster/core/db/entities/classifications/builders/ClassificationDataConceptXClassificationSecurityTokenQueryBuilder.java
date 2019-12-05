package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ClassificationDataConceptXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationDataConceptXClassificationSecurityTokenQueryBuilder, ClassificationDataConceptXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationDataConceptXClassificationSecurityToken_.base;
	}
}
