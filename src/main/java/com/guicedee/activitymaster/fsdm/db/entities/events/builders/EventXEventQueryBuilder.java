package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassification;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class EventXEventQueryBuilder
		extends QueryBuilderRelationshipClassification<Event, Event, EventXEventQueryBuilder,
		EventXEvent, UUID,EventXEventSecurityTokenQueryBuilder>
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
