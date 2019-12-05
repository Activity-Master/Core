package com.guicedee.activitymaster.core.db.entities.activeflag.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagXClassificationSecurityToken;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlagXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ActiveFlagXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ActiveFlagXClassificationSecurityTokenQueryBuilder, ActiveFlagXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ActiveFlagXClassificationSecurityToken_.base;
	}
}
