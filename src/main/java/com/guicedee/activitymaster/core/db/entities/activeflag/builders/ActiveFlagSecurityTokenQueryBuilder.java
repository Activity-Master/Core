package com.guicedee.activitymaster.core.db.entities.activeflag.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagSecurityToken;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagSecurityToken_;

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
