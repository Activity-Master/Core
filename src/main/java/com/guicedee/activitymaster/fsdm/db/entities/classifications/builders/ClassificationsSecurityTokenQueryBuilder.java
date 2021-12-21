package com.guicedee.activitymaster.fsdm.db.entities.classifications.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class ClassificationsSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ClassificationsSecurityTokenQueryBuilder, ClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ClassificationSecurityToken_.base;
	}
}
