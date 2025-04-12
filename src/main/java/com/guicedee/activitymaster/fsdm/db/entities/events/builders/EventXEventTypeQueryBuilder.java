package com.guicedee.activitymaster.fsdm.db.entities.events.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.fsdm.db.entities.events.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;


public class EventXEventTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Event,
		EventType,
		EventXEventTypeQueryBuilder,
		EventXEventType,
		UUID,
		EventXEventTypeSecurityTokenQueryBuilder
		>
{
	@Override
	public SingularAttribute<EventXEventType, Event> getPrimaryAttribute()
	{
		return EventXEventType_.eventID;
	}
	
	@Override
	public SingularAttribute<EventXEventType, EventType> getSecondaryAttribute()
	{
		return EventXEventType_.eventTypeID;
	}
	
	@Override
	public EventXEventTypeQueryBuilder withType(String typeValue, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (typeValue != null)
		{
			IEventService<?> service = com.guicedee.client.IGuiceContext.get(IEventService.class);
			EventType at = (EventType) service.findEventType(typeValue, system, identityToken);
			where(EventXEventType_.eventTypeID, Operand.Equals, at);
		}
		return this;
	}
}
