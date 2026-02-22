package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.EventsService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class EventsBinder extends AbstractModule implements IGuiceModule<EventsBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IEventService<?>> genericKey = Key.get(new TypeLiteral<IEventService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IEventService<EventsService>> realKey
				= Key.get(new TypeLiteral<IEventService<EventsService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(EventsService.class);
		bind(IEventService.class).to(genericKey);
		
		//IEvent Provider
		@SuppressWarnings("Convert2Diamond")
		Key<IEvent<?,?>> genericKeyIEvent = Key.get(new TypeLiteral<IEvent<?,?>>() {});
		Key<com.guicedee.activitymaster.fsdm.db.entities.events.Event> realKeyIEvent
				= Key.get(com.guicedee.activitymaster.fsdm.db.entities.events.Event.class);
		
		bind(genericKeyIEvent).to(realKeyIEvent);
		bind(IEvent.class).to(realKeyIEvent);
		bind(realKeyIEvent).toProvider(IEventProvider.class);

	}
}
