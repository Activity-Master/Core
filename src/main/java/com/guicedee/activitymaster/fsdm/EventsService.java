package com.guicedee.activitymaster.fsdm;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEventType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.EventException;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.db.entities.events.EventType;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import lombok.extern.java.Log;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;
import static com.guicedee.client.IGuiceContext.get;

@Log
public class EventsService
		implements IEventService<EventsService>
{
	@Inject
	private IEnterprise<?, ?> enterprise;

	@Inject
	private Vertx vertx;

	@Override
	public Uni<IEvent<?, ?>> get()
	{
		return Uni.createFrom().item(new Event());
	}

	@Override
	public Uni<IEvent<?, ?>> find(UUID id)
	{
		return new Event().builder()
		                  .find(id)
		                  .get()
		                  .onItem().ifNull().failWith(() -> new NoSuchElementException("Event not found with id: " + id))
					   .map(result->result);
	}

	@Override
	public Uni<IEvent<?, ?>> createEvent(String eventType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createEvent(eventType, null, system, identityToken);
	}

	@Override
	public Uni<IEvent<?, ?>> createEvent(String eventType, java.util.UUID key, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return ReactiveTransactionUtil.withTransaction(session -> {
			Event event = new Event();
			if (key != null)
			{
				event.setId(key);
			}

			event.setEnterpriseID(enterprise);
			event.setSystemID(system);
			event.setOriginalSourceSystemID(system.getId());

			IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
			return acService.getActiveFlag(enterprise)
				.chain(activeFlag -> {
					event.setActiveFlagID(activeFlag);
					return event.persist();
				})
				.chain(persistedEvent -> {
					// Start the createDefaultSecurity operation but don't wait for it to complete
					persistedEvent.createDefaultSecurity(system, identityToken);
					return persistedEvent.addEventTypes(eventType, "", NoClassification.toString(), system, identityToken)
						.map(result -> persistedEvent);
				});
		});
	}

	@Override
	public Uni<IEventType<?, ?>> createEventType(String eventType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return ReactiveTransactionUtil.withTransaction(session -> {
			EventType et = new EventType();
			return et.builder()
				.withName(eventType)
				.withEnterprise(enterprise)
				.inActiveRange()
				.inDateRange()
				.getCount()
				.map(count -> count > 0)
				.chain(exists -> {
					if (!exists) {
						if (et.getId() == null) {
							et.setId(UUID.randomUUID());
						}

						EventType etBuilt = new EventType();
						etBuilt.setId(et.getId());
						etBuilt.setName(eventType);
						etBuilt.setDescription(eventType);
						etBuilt.setSystemID(system);
						etBuilt.setEnterpriseID(enterprise);

						IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
						return acService.getActiveFlag(enterprise)
							.chain(activeFlag -> {
								etBuilt.setActiveFlagID(activeFlag);
								etBuilt.setOriginalSourceSystemID(system.getId());
								return etBuilt.persist();
							})
							.chain(persistedEt -> {
								// Start the createDefaultSecurity operation but don't wait for it to complete
								persistedEt.createDefaultSecurity(system, identityToken);
								return Uni.createFrom().item(persistedEt);
							});
					} else {
						return findEventType(eventType, system, identityToken);
					}
				});
		});
	}

	@Override
	//@CacheResult(cacheName = "EventTypesStrings")
	public Uni<IEventType<?, ?>> findEventType(String eventType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new EventType().builder()
			.withName(eventType)
			.withEnterprise(enterprise)
			.inActiveRange()
			.inDateRange()
			//  .canRead(system, identityToken)
			.get()
			.onItem().ifNull().failWith(() -> new EventException("Invalid Event Type - " + eventType))
					   .map(result->result);
	}
}
