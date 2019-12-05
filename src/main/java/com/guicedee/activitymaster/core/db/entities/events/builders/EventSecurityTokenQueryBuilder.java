package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventSecurityTokenQueryBuilder, EventSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventSecurityToken_.base;
	}
}
