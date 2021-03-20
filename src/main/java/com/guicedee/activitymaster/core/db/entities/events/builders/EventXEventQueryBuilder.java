package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.core.db.entities.events.*;
import jakarta.persistence.metamodel.SingularAttribute;

public class EventXEventQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Event, EventXEventQueryBuilder,
		EventXEvent, java.util.UUID>
{
	@Override
	public SingularAttribute<EventXEvent, Event> getPrimaryAttribute()
	{
		return EventXEvent_.parentEventID;
	}

	@Override
	public SingularAttribute<EventXEvent, Event> getSecondaryAttribute()
	{
		return EventXEvent_.childEventID;
	}
}
