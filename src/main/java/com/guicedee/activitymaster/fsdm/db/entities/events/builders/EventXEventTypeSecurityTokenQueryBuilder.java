package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXEventTypeSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXEventTypeSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class EventXEventTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXEventTypeSecurityTokenQueryBuilder, EventXEventTypeSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXEventTypeSecurityToken_.base;
	}
}
