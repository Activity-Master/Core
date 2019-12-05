package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.db.entities.events.*;

import javax.persistence.metamodel.Attribute;

public class EventXEventTypeQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, EventType, EventXEventTypeQueryBuilder,
				                                              EventXEventType, Long, EventXEventTypeSecurityToken>
{
	@Override
	public Attribute getPrimaryAttribute()
	{
		return EventXEventType_.eventID;
	}

	@Override
	public Attribute getSecondaryAttribute()
	{
		return EventXEventType_.eventTypeID;
	}
}
