package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationXClassificationSecurityToken_;

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
