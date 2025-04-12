package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventTypesSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventTypesSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class EventTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventTypeSecurityTokenQueryBuilder, EventTypesSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventTypesSecurityToken_.base;
	}
}
