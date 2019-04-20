package com.armineasy.activitymaster.activitymaster.db.entities.activeflag.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlagXClassificationSecurityToken_;

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
