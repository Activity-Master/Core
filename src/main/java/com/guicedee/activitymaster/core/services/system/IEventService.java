package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;

import java.util.UUID;

public interface IEventService<J extends IEventService<J>>
{
	String EventSystemName =  "Events System";
	
	IEvent<?> createEvent(IEventClassification<?> eventType, ISystems<?> originatingSystem, UUID... identityToken);
	
	IEventType<?> createEventType(IEventTypeValue<?> eventType, ISystems<?> originatingSystem, UUID... identityToken);
	
	IEventType<?> findEventType(IEventTypeValue<?> eventType, ISystems<?> system, UUID... identityToken);
	
	IEventType<?> findEventType(String eventType, ISystems<?> system, UUID... identityToken);
}
