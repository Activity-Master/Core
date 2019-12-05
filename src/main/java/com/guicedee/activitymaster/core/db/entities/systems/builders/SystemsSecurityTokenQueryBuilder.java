package com.guicedee.activitymaster.core.db.entities.systems.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.systems.SystemsSecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.SystemsSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class SystemsSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<SystemsSecurityTokenQueryBuilder, SystemsSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return SystemsSecurityToken_.base;
	}
}
