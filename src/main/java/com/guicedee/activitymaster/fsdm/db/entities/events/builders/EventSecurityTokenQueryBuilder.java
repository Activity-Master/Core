package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class EventSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventSecurityTokenQueryBuilder, EventSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventSecurityToken_.base;
	}
}
