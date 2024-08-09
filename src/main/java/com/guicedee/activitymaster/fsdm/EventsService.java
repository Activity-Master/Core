package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.implementations.TransactionalSupplier;
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
import com.guicedee.client.IGuiceContext;
import lombok.extern.java.Log;
import org.jboss.logmanager.Level;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;

@Log
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
	
	@Override
	public IEvent<?, ?> find(java.lang.String id)
	{
		return new Event().builder()
		                  .find(id)
		                  .get()
		                  .orElse(null);
	}
	
	@Override
	public CompletableFuture<IEvent<?, ?>> createEvent(String eventType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createEvent(eventType, null, system, identityToken);
	}
	
	@Override
	
	public CompletableFuture<IEvent<?, ?>> createEvent(String eventType, java.lang.String key, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		Event event = new Event();
		if (key != null)
		{
			event.setId(key);
		}
		
		TransactionalSupplier<IEvent<?, ?>> ts = IGuiceContext.get(TransactionalSupplier.class);
		ts.setConsumer(() -> {
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
		});
		
		return CompletableFuture.supplyAsync(ts).whenComplete((response, error) -> {
			if (error != null)
			{
				log.log(Level.SEVERE, "Could not save event type!", error);
			}
			else
			{
				log.finer("Saved new event");
			}
		});
	}
	
	@Override
	
	public CompletableFuture<IEventType<?, ?>> createEventType(String eventType, ISystems<?, ?> system, java.util.UUID... identityToken)
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
			if (Strings.isNullOrEmpty(et.getId()))
			{
				et.setId(UUID.randomUUID()
				             .toString());
			}
			
			TransactionalSupplier<IEventType<?, ?>> ts = IGuiceContext.get(TransactionalSupplier.class);
			ts.setConsumer(() -> {
				EventType etBuilt = new EventType();
				
				etBuilt.setId(et.getId());
				etBuilt.setName(eventType);
				etBuilt.setDescription(eventType);
				etBuilt.setSystemID(system);
				etBuilt.setEnterpriseID(enterprise);
				IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
				IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
				etBuilt.setActiveFlagID(activeFlag);
				etBuilt.setOriginalSourceSystemID(system);
				etBuilt.persist();
				
				etBuilt.createDefaultSecurity(system, identityToken);
				return etBuilt;
			});
			
			return CompletableFuture.supplyAsync(ts)
			                        .whenComplete((response, error) -> {
				                        if (error != null)
				                        {
					                        log.log(Level.SEVERE, "Could not save event type!", error);
				                        }
				                        else
				                        {
					                        log.fine("Saved event type: " + et.getName());
				                        }
			                        });
			//return et;
		}
		else
		{
			return CompletableFuture.supplyAsync(() -> {
				return findEventType(eventType, system, identityToken);
			});
		}
	}
	
	
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
