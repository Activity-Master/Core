package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXEventSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXEventSecurityToken_;
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
