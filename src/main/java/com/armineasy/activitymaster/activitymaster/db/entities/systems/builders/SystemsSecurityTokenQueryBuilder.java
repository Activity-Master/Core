package com.armineasy.activitymaster.activitymaster.db.entities.systems.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemsSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemsSecurityToken_;

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
