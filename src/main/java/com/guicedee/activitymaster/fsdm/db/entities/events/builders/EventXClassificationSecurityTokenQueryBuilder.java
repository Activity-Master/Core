package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXClassificationSecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventXClassificationSecurityToken_;
import jakarta.persistence.metamodel.Attribute;

public class EventXClassificationSecurityTokenQueryBuilder
		extends QueryBuilderSecurities<EventXClassificationSecurityTokenQueryBuilder, EventXClassificationSecurityToken, java.util.UUID>
{
	@Override
	protected Attribute getMyAttribute()
	{
		return EventXClassificationSecurityToken_.base;
	}
}
