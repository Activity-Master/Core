package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.IEvent;
import com.guicedee.activitymaster.core.services.dto.IEventType;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import java.util.UUID;

public interface IEventService<J extends IEventService<J>>
{
	IEvent<?> createEvent(IEventTypeValue<?> eventType, ISystems<?> originatingSystem, UUID...identityToken);

	IEventType<?> createEventType(IEventTypeValue<?> eventType, ISystems<?> originatingSystem, UUID... identityToken);

	IEventType<?> findEventType(IEventTypeValue<?> eventType, IEnterprise<?> enterprise, UUID... identityToken);
}
