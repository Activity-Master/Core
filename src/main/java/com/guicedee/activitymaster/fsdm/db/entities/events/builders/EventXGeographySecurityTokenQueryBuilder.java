package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXGeographySecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXGeographySecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class EventXGeographySecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXGeographySecurityTokenQueryBuilder, EventXGeographySecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXGeographySecurityToken_.base;
	}
}
