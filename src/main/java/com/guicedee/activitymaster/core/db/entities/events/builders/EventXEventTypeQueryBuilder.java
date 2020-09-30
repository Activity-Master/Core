package com.guicedee.activitymaster.core.db.entities.events.builders;

import com.entityassist.enumerations.Operand;
import com.guicedee.activitymaster.core.db.abstraction.builders.QueryBuilderRelationshipClassificationTypes;
import com.guicedee.activitymaster.core.db.entities.events.*;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.IEventService;
import com.guicedee.guicedinjection.GuiceContext;

import javax.persistence.metamodel.Attribute;
import java.util.UUID;

public class EventXEventTypeQueryBuilder
		extends QueryBuilderRelationshipClassificationTypes<Event,
		EventType,
		EventXEventTypeQueryBuilder,
		EventXEventType,
		IEventClassification<?>,
		Long,
		EventXEventTypeSecurityToken>
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
	
	@Override
	public EventXEventTypeQueryBuilder withType(String typeValue, IEnterprise<?> enterprise, UUID... identityToken)
	{
		if (typeValue != null)
		{
			IEventService<?> service = GuiceContext.get(IEventService.class);
			EventType at = (EventType) service.findEventType(typeValue, enterprise, identityToken);
			where(EventXEventType_.eventTypeID, Operand.Equals, at);
		}
		return this;
	}
}
