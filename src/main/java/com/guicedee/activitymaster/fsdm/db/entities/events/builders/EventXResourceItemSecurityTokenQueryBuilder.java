package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

import java.util.UUID;

public class EventXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXResourceItemSecurityTokenQueryBuilder, EventXResourceItemSecurityToken, UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXResourceItemSecurityToken_.base;
	}
}
