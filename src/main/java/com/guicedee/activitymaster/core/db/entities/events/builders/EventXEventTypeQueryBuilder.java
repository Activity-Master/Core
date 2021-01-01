package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.IEventService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.UUID;

public class EventXEventTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Event,
		EventType,
		EventXEventTypeQueryBuilder,
		EventXEventType,
		IEventClassification<?>,
		java.util.UUID,
		EventXEventTypeSecurityToken>
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
	public EventXEventTypeQueryBuilder withType(String typeValue, ISystems<?> system, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IEventService<?> service = GuiceContext.get(IEventService.class);
			EventType at = (EventType) service.findEventType(typeValue, system, identityToken);
			where(EventXEventType_.eventTypeID, Operand.Equals, at);
		}
		return this;
	}
}
