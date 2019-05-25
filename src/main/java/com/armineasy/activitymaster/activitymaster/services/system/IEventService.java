package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.events.EventType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IEventTypeValue;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface IEventService
{
	Event createEvent(IEventTypeValue<?> eventType, ISystems originatingSystem, UUID...identityToken);

	@CacheResult(cacheName = "EventTypes")
	EventType findEventType(@CacheKey IEventTypeValue<?> eventType, @CacheKey Enterprise enterprise, @CacheKey UUID... identityToken);
}
