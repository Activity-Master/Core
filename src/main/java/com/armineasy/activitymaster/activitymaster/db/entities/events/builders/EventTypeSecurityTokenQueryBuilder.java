package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventTypesSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventTypesSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventTypeSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventTypeSecurityTokenQueryBuilder, EventTypesSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventTypesSecurityToken_.base;
	}
}
