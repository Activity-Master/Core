package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.Event;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.guicedinjection.GuiceContext;
import lombok.Getter;
import lombok.extern.java.Log;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IEventService.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@Log
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
	@Getter
	private static final ThreadLocal<IEvent<?, ?>> eventThreads = ThreadLocal.withInitial(() -> null);
	
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
	
	
	public static ISystems<?, ?> getISystem(String systemName)
	{
		IEnterprise<?, ?> enterprise = GuiceContext.get(IEnterprise.class);
		if (enterprise.isFake())
		{
			return null;
		}
		//noinspection Convert2Diamond
		return GuiceContext.get(Key.get(new TypeLiteral<ISystems<?, ?>>() {}, Names.named(systemName)));
	}
	
	public static UUID getISystemToken(String systemName)
	{
		IEnterprise<?, ?> enterprise = GuiceContext.get(IEnterprise.class);
		if (enterprise.isFake())
		{
			return null;
		}
		return GuiceContext.get(Key.get(UUID.class, Names.named(systemName)));
	}
}


