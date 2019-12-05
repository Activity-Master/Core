package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ClassificationXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationXClassificationSecurityTokenQueryBuilder, ClassificationXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationXClassificationSecurityToken_.base;
	}
}
