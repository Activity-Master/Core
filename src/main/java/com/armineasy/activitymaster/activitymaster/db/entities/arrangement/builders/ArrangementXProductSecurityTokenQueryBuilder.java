package com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXProductSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class ArrangementXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<ArrangementXProductSecurityTokenQueryBuilder, ArrangementXProductSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return ArrangementXProductSecurityToken_.base;
	}
}
