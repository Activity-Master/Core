package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.annotations.Event;
import com.guicedee.guicedinjection.GuiceContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IEventService.*;
import static com.guicedee.guicedinjection.GuiceContext.*;


public class EventsAOPInterceptor implements MethodInterceptor
{
	
	@Inject
	private ActivityMasterConfiguration configuration;
	@Inject
	@Named(EventSystemName)
	private Provider<ISystems<?, ?>> systemProvider;
	@Inject
	@Named(ActivityMasterSystemName)
	private UUID identityToken;
	
	/**
	 * Tracks if the unit of work was begun implicitly by this transaction.
	 */
	
	private static final ThreadLocal<IEvent<?, ?>> eventThreads = ThreadLocal.withInitial(com.guicedee.activitymaster.fsdm.db.entities.events.Event::new);
	
	public static ThreadLocal<IEvent<?, ?>> getEventThreads()
	{
		return eventThreads;
	}
	
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		if (systemProvider == null)
		{
			GuiceContext.inject()
			            .injectMembers(this);
		}
		ISystems<?, ?> system = systemProvider.get();
		if (system.isFake() || !configuration.isEnterpriseReady())
		{
			return methodInvocation.proceed();
		}
		
		Event eventAnnotation = methodInvocation.getMethod()
		                                        .getAnnotation(Event.class);
		
		Object result = null;
		IEventService<?> eventService = get(IEventService.class);
		IEvent<?, ?> event;
		IEvent<?, ?> previousEvent = null;
		try
		{
			eventService.findEventType(eventAnnotation.value(), system, identityToken);
		}
		catch (Throwable T)
		{
			eventService.createEventType(eventAnnotation.value(), system, identityToken);
		}
		if (eventThreads.get() == null)
		{
			event = eventService.createEvent(eventAnnotation.value(), system, identityToken);
			eventThreads.set(event);
		}
		else
		{
			previousEvent = eventThreads.get();
			event = eventService.createEvent(eventAnnotation.value(), system, identityToken);
			eventThreads.set(event);
			
			previousEvent.addChild(event, eventAnnotation.parentHierarchyClassificationName(), null, system, identityToken);
		}
		Object o = null;
		try
		{
			o = methodInvocation.proceed();
			event.addClassification("EventStatus", "Successful", getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
		}
		catch (Throwable t)
		{
			event.addClassification("EventStatus", "Failure", getISystem(ActivityMasterSystemName), getISystemToken(ActivityMasterSystemName));
			throw t;
		}
		finally
		{
			eventThreads.set(previousEvent);
		}
		return result;
	}
	
	
	
}


