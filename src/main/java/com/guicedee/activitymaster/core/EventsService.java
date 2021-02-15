package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventType;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.events.EventThread;
import com.guicedee.activitymaster.core.services.classifications.events.IEventClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IEventTypeValue;
import com.guicedee.activitymaster.core.services.system.IEventService;
import com.guicedee.guicedinjection.GuiceContext;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;


public class EventsService
		implements IEventService<EventsService>
{
	@Inject
	@Named("Active")
	private IActiveFlag<?> activeFlag;
	
	@Inject
	private IEnterprise<?> enterprise;
	
	@Override
	public IEvent<?> createEvent(IEventClassification<?> eventType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		Event event = new Event();
		event.setEnterpriseID((Enterprise) enterprise);
		event.setSystemID((Systems) originatingSystem);
		event.setOriginalSourceSystemID((Systems) originatingSystem);
		event.setActiveFlagID(activeFlag);
		event.persist();
		event.createDefaultSecurity(originatingSystem, identityToken);
		event.addEventType(NoClassification, eventType, STRING_EMPTY, originatingSystem, identityToken);
		EventThread.event.set(event);
		return event;
	}
	
	@Override
	public IEventType<?> createEventType(IEventTypeValue<?> eventType, ISystems<?> originatingSystem, UUID... identityToken)
	{
		EventType et = new EventType();
		
		boolean exists = et.builder()
		                   .withName(eventType.name())
		                   .withEnterprise(enterprise)
		                   .inActiveRange(enterprise)
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(eventType.name());
			et.setDescription(eventType.classificationValue());
			et.setSystemID((Systems) originatingSystem);
			et.setEnterpriseID((Enterprise) enterprise);
			et.setActiveFlagID(activeFlag);
			et.setOriginalSourceSystemID((Systems) originatingSystem);
			et.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				et.createDefaultSecurity(originatingSystem, identityToken);
			}
			return et;
		}
		else
		{
			return findEventType(eventType, originatingSystem, identityToken);
		}
	}
	
	@Override
	@CacheResult(cacheName = "EventTypes")
	public IEventType<?> findEventType(@CacheKey IEventTypeValue<?> eventType, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return new EventType().builder()
		                      .withName(eventType.name())
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise, identityToken)
		                      .inDateRange()
		                      .canRead(system, identityToken)
		                      .get()
		                      .orElseThrow();
	}
	
	@Override
	@CacheResult(cacheName = "EventTypesStrings")
	public IEventType<?> findEventType(@CacheKey String eventType, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		return new EventType().builder()
		                      .withName(eventType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise, identityToken)
		                      .inDateRange()
		                      .canRead(system, identityToken)
		                      .get()
		                      .orElseThrow();
	}
}
