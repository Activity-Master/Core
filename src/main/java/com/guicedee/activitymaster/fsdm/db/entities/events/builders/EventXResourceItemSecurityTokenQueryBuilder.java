package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXResourceItemSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXResourceItemSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class EventXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXResourceItemSecurityTokenQueryBuilder, EventXResourceItemSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXResourceItemSecurityToken_.base;
	}
}
