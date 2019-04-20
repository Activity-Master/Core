package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXClassificationSecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXClassificationSecurityTokenQueryBuilder, EventXClassificationSecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXClassificationSecurityToken_.base;
	}
}
