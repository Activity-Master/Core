package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventXEventSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXEventSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class EventXEventSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXEventSecurityTokenQueryBuilder, EventXEventSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXEventSecurityToken_.base;
	}
}
