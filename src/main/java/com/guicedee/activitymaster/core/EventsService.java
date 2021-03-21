package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IEventService;
import com.guicedee.activitymaster.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.client.services.builders.warehouse.events.IEventType;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.entities.events.Event;
import com.guicedee.activitymaster.core.db.entities.events.EventType;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.UUID;

import static com.guicedee.activitymaster.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;


public class EventsService
		implements IEventService<EventsService>
{
	@Inject
	@Named("Active")
	private IActiveFlag<?,?> activeFlag;
	
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Override
	public IEvent<?,?> createEvent(String eventType, ISystems<?,?> originatingSystem, UUID... identityToken)
	{
		Event event = new Event();
		event.setEnterpriseID(enterprise);
		event.setSystemID(originatingSystem);
		event.setOriginalSourceSystemID(originatingSystem);
		event.setActiveFlagID(activeFlag);
		event.persist();
		event.createDefaultSecurity(originatingSystem, identityToken);
		event.addEventTypes(NoClassification, eventType, STRING_EMPTY, originatingSystem, identityToken);
		return event;
	}
	
	@Override
	public IEventType<?,?> createEventType(String eventType, ISystems<?,?> originatingSystem, UUID... identityToken)
	{
		EventType et = new EventType();
		
		boolean exists = et.builder()
		                   .withName(eventType)
		                   .withEnterprise(enterprise)
		                   .inActiveRange(enterprise)
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(eventType);
			et.setDescription(eventType);
			et.setSystemID(originatingSystem);
			et.setEnterpriseID(enterprise);
			et.setActiveFlagID(activeFlag);
			et.setOriginalSourceSystemID(originatingSystem);
			et.persist();
				et.createDefaultSecurity(originatingSystem, identityToken);
			
			return et;
		}
		else
		{
			return findEventType(eventType, originatingSystem, identityToken);
		}
	}
	
	@Override
	@CacheResult(cacheName = "EventTypesStrings")
	public IEventType<?,?> findEventType(@CacheKey String eventType, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		return new EventType().builder()
		                      .withName(eventType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange(enterprise, identityToken)
		                      .inDateRange()
		                    //  .canRead(system, identityToken)
		                      .get()
		                      .orElseThrow();
	}
}
