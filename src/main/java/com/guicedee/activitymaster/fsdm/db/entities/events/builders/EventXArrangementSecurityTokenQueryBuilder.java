package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXArrangementsSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXArrangementsSecurityToken_;

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
