package com.guicedee.activitymaster.core.implementations.interceptors;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IEventService;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.client.services.annotations.Event;
import com.guicedee.activitymaster.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.logger.LogFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.UUID;
import java.util.logging.Logger;

import static com.guicedee.activitymaster.core.SystemsService.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

public class EventsAOPInterceptor implements MethodInterceptor
{
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	@Inject
	@Named(ActivityMasterSystemName)
	private UUID identityToken;
	
	/**
	 * Tracks if the unit of work was begun implicitly by this transaction.
	 */
	private final ThreadLocal<IEvent<?,?>> eventThreads = ThreadLocal.withInitial(()->null);
	
	private static final Logger log = LogFactory.getLog("EventsAOPInterceptor");
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable
	{
		Event eventAnnotation = invocation.getMethod()
		                                  .getAnnotation(Event.class);
		Object result = null;
		if(GuiceContext.get(ActivityMasterConfiguration.class).isSecurityEnabled())
		{
			IEventService<?> eventService = get(IEventService.class);
			IEvent<?, ?> event = null;
			if (eventThreads.get() == null)
			{
				event = eventService.createEvent(eventAnnotation.eventTypeName(), activityMasterSystem, identityToken);
				eventThreads.set(event);
			}
			try
			{
				result = invocation.proceed();
			}
			catch (Throwable e)
			{
				throw e;
			}
			finally
			{
				eventThreads.set(null);
			}
		}
		return result;
	}
}


