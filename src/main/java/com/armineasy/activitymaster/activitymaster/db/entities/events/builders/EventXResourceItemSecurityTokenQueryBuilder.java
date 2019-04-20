package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXResourceItemSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXResourceItemSecurityToken_;

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
