package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationSecurityToken_;

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
