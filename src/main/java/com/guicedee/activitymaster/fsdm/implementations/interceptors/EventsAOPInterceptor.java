package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.Event;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import lombok.extern.java.Log;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IEventService.*;
import static com.guicedee.client.IGuiceContext.*;

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

	@Inject
	private IEnterpriseService<?> enterpriseService;

	/**
	 * Tracks events by thread ID for reactive programming
	 */
	private static final ConcurrentHashMap<Long, IEvent<?, ?>> eventsByThread = new ConcurrentHashMap<>();

	public static IEvent<?, ?> getCurrentEvent() {
		return eventsByThread.get(Thread.currentThread().getId());
	}

	public static void setCurrentEvent(IEvent<?, ?> event) {
		if (event == null) {
			eventsByThread.remove(Thread.currentThread().getId());
		} else {
			eventsByThread.put(Thread.currentThread().getId(), event);
		}
	}

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		if (systemProvider == null)
		{
			com.guicedee.client.IGuiceContext.instance().inject()
			            .injectMembers(this);
		}
		ISystems<?, ?> system = systemProvider.get();
		if (system.isFake() || !configuration.isEnterpriseReady())
		{
			return methodInvocation.proceed();
		}

		Event eventAnnotation = methodInvocation.getMethod()
		                                        .getAnnotation(Event.class);

		// Get enterprise from configuration
		String enterpriseName = configuration.getApplicationEnterpriseName();

		// Create variables to store results
		Object result = null;
		IEventService<?> eventService = get(IEventService.class);
		IEvent<?, ?> previousEvent = getCurrentEvent();

		// Ensure event type exists
		try
		{
			eventService.findEventType(eventAnnotation.value(), system, identityToken);
		}
		catch (Throwable T)
		{
			eventService.createEventType(eventAnnotation.value(), system, identityToken);
		}

		// Create a new event
		Future<IEvent<?,?>> eventFuture = eventService.createEvent(eventAnnotation.value(), system, identityToken);
		IEvent<?, ?> event = eventFuture.toCompletionStage().toCompletableFuture().get();
		setCurrentEvent(event);

		// If there's a previous event, add this event as a child
		if (previousEvent != null)
		{
			// Get enterprise from name
			enterpriseService.getEnterprise(enterpriseName)
				.subscribe().with(
					enterprise -> {
						// Get system and token
						getISystem(ActivityMasterSystemName, enterprise)
							.chain(activityMasterSystem -> {
								return getISystemToken(ActivityMasterSystemName, enterprise)
									.chain(token -> {
										// Add child event to parent event
										return Uni.createFrom().item(() -> {
											previousEvent.addChild((IWarehouseTable<?, ?, ?, ?>) event, eventAnnotation.parentHierarchyClassificationName(), null, activityMasterSystem, token);
											return null;
										});
									});
							})
							.subscribe().with(
								success -> log.info("Added child event to parent event"),
								error -> log.log(Level.SEVERE, "Error adding child event to parent event", error)
							);
					},
					error -> log.log(Level.SEVERE, "Error getting enterprise", error)
				);
		}

		try
		{
			// Execute the method
			result = methodInvocation.proceed();

			// Add success classification
			enterpriseService.getEnterprise(enterpriseName)
				.subscribe().with(
					enterprise -> {
						// Get system and token
						getISystem(ActivityMasterSystemName, enterprise)
							.chain(activityMasterSystem -> {
								return getISystemToken(ActivityMasterSystemName, enterprise)
									.chain(token -> {
										// Add success classification
										return Uni.createFrom().item(() -> {
											event.addClassification("EventStatus", "Successful", activityMasterSystem, token);
											return null;
										});
									});
							})
							.subscribe().with(
								success -> log.info("Added success classification to event"),
								error -> log.log(Level.SEVERE, "Error adding success classification to event", error)
							);
					},
					error -> log.log(Level.SEVERE, "Error getting enterprise", error)
				);
		}
		catch (Throwable t)
		{
			// Add failure classification
			enterpriseService.getEnterprise(enterpriseName)
				.subscribe().with(
					enterprise -> {
						// Get system and token
						getISystem(ActivityMasterSystemName, enterprise)
							.chain(activityMasterSystem -> {
								return getISystemToken(ActivityMasterSystemName, enterprise)
									.chain(token -> {
										// Add failure classification
										return Uni.createFrom().item(() -> {
											event.addClassification("EventStatus", "Failure", activityMasterSystem, token);
											return null;
										});
									});
							})
							.subscribe().with(
								success -> log.info("Added failure classification to event"),
								error -> log.log(Level.SEVERE, "Error adding failure classification to event", error)
							);
					},
					error -> log.log(Level.SEVERE, "Error getting enterprise", error)
				);
			throw t;
		}
		finally
		{
			// Restore previous event
			setCurrentEvent(previousEvent);
		}

		return result;
	}



}
