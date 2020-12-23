package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventXArrangementsSecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXArrangementsSecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class EventXArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXArrangementSecurityTokenQueryBuilder, EventXArrangementsSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXArrangementsSecurityToken_.base;
	}
}
