package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ClassificationsSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationsSecurityTokenQueryBuilder, ClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationSecurityToken_.base;
	}
}
