package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConceptSecurityToken_;

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
