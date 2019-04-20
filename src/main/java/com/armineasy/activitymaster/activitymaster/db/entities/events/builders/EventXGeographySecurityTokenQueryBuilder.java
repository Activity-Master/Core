package com.armineasy.activitymaster.activitymaster.db.entities.events.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilderSecurities;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXGeographySecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventXGeographySecurityToken_;

import javax.persistence.metamodel.Attribute;

public class EventXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXGeographySecurityTokenQueryBuilder, EventXGeographySecurityToken, Long>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXGeographySecurityToken_.base;
	}
}
