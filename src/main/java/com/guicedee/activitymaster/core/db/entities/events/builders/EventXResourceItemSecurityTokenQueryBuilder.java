package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItemSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXResourceItemSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventXResourceItemSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXResourceItemSecurityTokenQueryBuilder, EventXResourceItemSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXResourceItemSecurityToken_.base;
	}
}
