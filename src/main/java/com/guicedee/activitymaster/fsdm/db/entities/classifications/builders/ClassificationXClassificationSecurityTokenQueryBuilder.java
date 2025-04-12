package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class ClassificationXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationXClassificationSecurityTokenQueryBuilder, ClassificationXClassificationSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationXClassificationSecurityToken_.base;
	}
}
