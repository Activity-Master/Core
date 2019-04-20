package com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class YesNoXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<YesNoXClassificationSecurityTokenQueryBuilder, YesNoXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return YesNoXClassificationSecurityToken_.base;
	}
}
