package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXProductSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXProductSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class EventXProductSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXProductSecurityTokenQueryBuilder, EventXProductSecurityToken, java.lang.String>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXProductSecurityToken_.base;
	}
}
