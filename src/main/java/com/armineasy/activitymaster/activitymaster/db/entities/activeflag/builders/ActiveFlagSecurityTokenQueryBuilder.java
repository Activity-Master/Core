package com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ActiveFlagSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ActiveFlagSecurityTokenQueryBuilder, ActiveFlagSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ActiveFlagSecurityToken_.base;
	}

}
