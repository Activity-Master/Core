package com.armineasy.activitymaster.activitymaster.db.entities.systems.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.SystemXClassificationSecurityToken_;

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
