package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXEventTypeSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXEventTypeSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventXEventTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXEventTypeSecurityTokenQueryBuilder, EventXEventTypeSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXEventTypeSecurityToken_.base;
	}
}
