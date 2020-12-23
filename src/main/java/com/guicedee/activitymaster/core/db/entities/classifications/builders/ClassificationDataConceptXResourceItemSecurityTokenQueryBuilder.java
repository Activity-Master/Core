package com.guicedee.activitymaster.core.db.entities.classifications.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.classifications.ClassificationDataConceptXResourceItemSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationDataConceptXResourceItemSecurityTokenQueryBuilder, ClassificationDataConceptXResourceItemSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationDataConceptXResourceItemSecurityToken_.base;
	}
}
