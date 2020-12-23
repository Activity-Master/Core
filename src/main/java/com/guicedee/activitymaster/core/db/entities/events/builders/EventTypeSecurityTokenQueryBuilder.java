package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventTypesSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventTypesSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class EventTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventTypeSecurityTokenQueryBuilder, EventTypesSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventTypesSecurityToken_.base;
	}
}
