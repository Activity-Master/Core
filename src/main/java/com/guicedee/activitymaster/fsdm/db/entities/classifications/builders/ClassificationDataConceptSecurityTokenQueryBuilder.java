package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConceptSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConceptSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ClassificationDataConceptSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationDataConceptSecurityTokenQueryBuilder, ClassificationDataConceptSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationDataConceptSecurityToken_.base;
	}
}
