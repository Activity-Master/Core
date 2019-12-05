package com.guicedee.activitymaster.core.db.entities.systems.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.SystemXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class SystemXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<SystemXClassificationSecurityTokenQueryBuilder, SystemXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return SystemXClassificationSecurityToken_.base;
	}
}
