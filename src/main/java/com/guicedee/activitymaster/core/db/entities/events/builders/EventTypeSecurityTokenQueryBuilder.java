package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventTypesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventTypesSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventTypeSecurityTokenQueryBuilder, EventTypesSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventTypesSecurityToken_.base;
	}
}
