package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXResourceItemSecurityToken_;

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
