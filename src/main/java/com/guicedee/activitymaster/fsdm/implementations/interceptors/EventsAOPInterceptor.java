package com.guicedee.activitymaster.fsdm.implementations.interceptors;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.annotations.Event;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.IWarehouseTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.scopes.CallScopeProperties;
import com.guicedee.client.scopes.CallScoper;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import lombok.extern.java.Log;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serializable;
import java.util.UUID;
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


	public static IEvent<?, ?> getCurrentEvent() {
		CallScoper callScoper = IGuiceContext.get(CallScoper.class);
		if (!callScoper.isStartedScope()) return null;
		CallScopeProperties csp = IGuiceContext.get(CallScopeProperties.class);
		if (csp != null) {
			return (IEvent<?, ?>) csp.getProperties().get("fsdm.event");
		}
		return null;
	}

	public static void setCurrentEvent(IEvent<?, ?> event) {
		CallScoper callScoper = IGuiceContext.get(CallScoper.class);
		if (!callScoper.isStartedScope()) return;
		CallScopeProperties csp = IGuiceContext.get(CallScopeProperties.class);
		if (csp != null) {
			if (event == null) {
				csp.getProperties().remove("fsdm.event");
			} else {
				csp.getProperties().put("fsdm.event", event);
			}
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

		if (system.isFake() || configuration.getApplicationEnterpriseName() == null)
		{
			return methodInvocation.proceed();
		}

		Event eventAnnotation = methodInvocation.getMethod()
		                                        .getAnnotation(Event.class);

		String enterpriseName = configuration.getApplicationEnterpriseName();
		IEventService<?> eventService = get(IEventService.class);
		
		if (methodInvocation.getMethod().getReturnType().isAssignableFrom(Uni.class)) {
			return SessionUtils.<Object>withActivityMaster(enterpriseName, ActivityMasterSystemName, tuple -> {
				Mutiny.Session session = tuple.getItem1();
				ISystems<?, ?> activityMasterSystem = tuple.getItem3();
				return setupEvent(session, eventAnnotation, system, activityMasterSystem, eventService)
					.chain(event -> {
						try {
							return ((Uni<?>) methodInvocation.proceed())
								.chain(result -> recordOutcome(session, event, activityMasterSystem, true).replaceWith(result))
								.onFailure().call(error -> recordOutcome(session, event, activityMasterSystem, false));
						} catch (Throwable e) {
							return recordOutcome(session, event, activityMasterSystem, false)
								.chain(() -> Uni.createFrom().failure(e));
						}
					});
			});
		} else {
			// Non-reactive method: fire and forget the event creation and tracking
			SessionUtils.<IEvent<?, ?>>withActivityMaster(enterpriseName, ActivityMasterSystemName,
					tuple -> { return setupEvent(tuple.getItem1(), eventAnnotation, system, tuple.getItem3(), eventService); })
				.subscribe().with(
					event -> log.info("Event started for non-reactive method: " + eventAnnotation.value()),
					error -> log.log(Level.SEVERE, "Error starting event for non-reactive method", error)
				);

			try {
				Object result = methodInvocation.proceed();
				IEvent<?, ?> event = getCurrentEvent();
				if (event != null) {
					SessionUtils.<Void>withActivityMaster(enterpriseName, ActivityMasterSystemName,
							tuple -> { return recordOutcome(tuple.getItem1(), event, tuple.getItem3(), true); })
						.subscribe().with(s -> {}, e -> log.log(Level.SEVERE, "Error recording success", e));
				}
				return result;
			} catch (Throwable t) {
				IEvent<?, ?> event = getCurrentEvent();
				if (event != null) {
					SessionUtils.<Void>withActivityMaster(enterpriseName, ActivityMasterSystemName,
							tuple -> { return recordOutcome(tuple.getItem1(), event, tuple.getItem3(), false); })
						.subscribe().with(s -> {}, e -> log.log(Level.SEVERE, "Error recording failure", e));
				}
				throw t;
			}
		}
	}

	private Uni<IEvent<?, ?>> setupEvent(Mutiny.Session session, Event eventAnnotation, ISystems<?, ?> system, ISystems<?, ?> activityMasterSystem, IEventService<?> eventService) {
		IEvent<?, ?> previousEvent = getCurrentEvent();
		return eventService.findEventType(session, eventAnnotation.value(), system, identityToken)
			.onFailure().recoverWithUni(() -> eventService.createEventType(session, eventAnnotation.value(), system, identityToken))
			.chain(eventType -> eventService.createEvent(session, eventAnnotation.value(), system, identityToken))
			.chain(event -> {
				setCurrentEvent(event);
				if (previousEvent != null) {
					previousEvent.addChild(session, (IWarehouseTable<?, ?, ?, ?>) event, eventAnnotation.parentHierarchyClassificationName(), null, activityMasterSystem, identityToken);
				}
				return Uni.createFrom().item(event);
			});
	}

	private Uni<Void> recordOutcome(Mutiny.Session session, IEvent<?, ?> event, ISystems<?, ?> activityMasterSystem, boolean success) {
		String status = success ? "Successful" : "Failure";
		return event.addClassification(session, "EventStatus", status, activityMasterSystem, identityToken)
			.replaceWith(Uni.createFrom().voidItem())
			.onFailure().invoke(error -> log.log(Level.SEVERE, "Error recording outcome", error))
			.replaceWith(Uni.createFrom().voidItem());
	}



}
