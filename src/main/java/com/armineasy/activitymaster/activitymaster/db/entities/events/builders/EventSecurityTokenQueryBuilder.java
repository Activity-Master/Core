package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventSecurityToken_;

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
