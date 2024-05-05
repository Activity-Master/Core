package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.EventException;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventType;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;

public class EventsService
		implements IEventService<EventsService>
{
	@Inject
	private IEnterprise<?, ?> enterprise;
	
	@Override
	public IEvent<?, ?> get()
	{
		return new Event();
	}
	@Transactional()
	@Override
	public IEvent<?, ?> find(java.lang.String id)
	{
		return new Event().builder()
		                  .find(id)
		                  .get()
		                  .orElse(null);
	}
	@Transactional()
	@Override
	public IEvent<?, ?> createEvent(String eventType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createEvent(eventType, null, system, identityToken);
	}
	
	@Override
	@Transactional()
	public IEvent<?, ?> createEvent(String eventType, java.lang.String key, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		Event event = new Event();
		if(key != null)
		event.setId(key);
		event.setEnterpriseID(enterprise);
		event.setSystemID(system);
		event.setOriginalSourceSystemID(system);
		IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
		event.setActiveFlagID(activeFlag);
		event.persist();
		event.createDefaultSecurity(system, identityToken);
		
		event.addEventTypes(eventType, "", NoClassification.toString(), system, identityToken);
		return event;
	}
	
	@Override
	@Transactional()
	public IEventType<?, ?> createEventType(String eventType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		EventType et = new EventType();
		
		boolean exists = et.builder()
		                   .withName(eventType)
		                   .withEnterprise(enterprise)
		                   .inActiveRange()
		                   .inDateRange()
		                   .getCount() > 0;
		
		if (!exists)
		{
			et.setName(eventType);
			et.setDescription(eventType);
			et.setSystemID(system);
			et.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
			et.setActiveFlagID(activeFlag);
			et.setOriginalSourceSystemID(system);
			et.persist();
			et.createDefaultSecurity(system, identityToken);
			
			return et;
		}
		else
		{
			return findEventType(eventType, system, identityToken);
		}
	}
	
	@Transactional()
	@Override
	@CacheResult(cacheName = "EventTypesStrings")
	public IEventType<?, ?> findEventType(@CacheKey String eventType, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		return new EventType().builder()
		                      .withName(eventType)
		                      .withEnterprise(enterprise)
		                      .inActiveRange()
		                      .inDateRange()
		                      //  .canRead(system, identityToken)
		                      .get()
		                      .orElseThrow(() -> new EventException("Invalid Event Type - " + eventType));
	}
}
