package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.core.db.entities.events.EventXGeographySecurityToken;
import com.guicedee.activitymaster.core.db.entities.events.EventXGeographySecurityToken_;

import jakarta.persistence.metamodel.Attribute;

public class EventXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXGeographySecurityTokenQueryBuilder, EventXGeographySecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXGeographySecurityToken_.base;
	}
}
