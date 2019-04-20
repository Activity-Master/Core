package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXArrangementsSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXArrangementsSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventXArrangementSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXArrangementSecurityTokenQueryBuilder, EventXArrangementsSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXArrangementsSecurityToken_.base;
	}
}
