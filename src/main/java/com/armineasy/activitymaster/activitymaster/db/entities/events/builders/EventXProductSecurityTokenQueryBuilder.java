package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXProductSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXProductSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXProductSecurityTokenQueryBuilder, EventXProductSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXProductSecurityToken_.base;
	}
}
