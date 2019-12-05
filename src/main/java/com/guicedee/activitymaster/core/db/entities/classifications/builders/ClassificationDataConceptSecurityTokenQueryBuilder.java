package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ClassificationDataConceptSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationDataConceptSecurityTokenQueryBuilder, ClassificationDataConceptSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationDataConceptSecurityToken_.base;
	}
}
