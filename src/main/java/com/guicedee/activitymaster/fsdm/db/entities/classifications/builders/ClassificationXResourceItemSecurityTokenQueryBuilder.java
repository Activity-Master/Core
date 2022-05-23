package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ClassificationXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationXResourceItemSecurityTokenQueryBuilder, ClassificationXResourceItemSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationXResourceItemSecurityToken_.base;
	}
}
