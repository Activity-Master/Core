package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventXEventTypeSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXEventTypeSecurityToken_;

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
