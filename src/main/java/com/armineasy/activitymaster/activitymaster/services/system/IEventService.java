package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventType;
import com.armineasy.activitymaster.activitymaster.services.dto.IEvent;
import com.armineasy.activitymaster.activitymaster.services.dto.IEventType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IEventTypeValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface IEventService<J extends IEventService<J>>
{
	IEvent<?> createEvent(IEventTypeValue<?> eventType, ISystems<?> originatingSystem, UUID...identityToken);

	IEventType<?> createEventType(IEventTypeValue<?> eventType, ISystems<?> originatingSystem, UUID... identityToken);

	IEventType<?> findEventType(IEventTypeValue<?> eventType, IEnterprise<?> enterprise, UUID... identityToken);
}
