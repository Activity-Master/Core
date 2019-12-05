package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ClassificationXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationXResourceItemSecurityTokenQueryBuilder, ClassificationXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationXResourceItemSecurityToken_.base;
	}
}
