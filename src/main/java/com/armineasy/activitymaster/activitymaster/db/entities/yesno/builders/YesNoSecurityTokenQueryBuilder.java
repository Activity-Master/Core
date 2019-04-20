package com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class YesNoSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<YesNoSecurityTokenQueryBuilder, YesNoSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return YesNoSecurityToken_.base;
	}
}
