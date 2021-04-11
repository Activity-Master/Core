package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;
import com.guicedee.activitymaster.fsdm.EventsService;
import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.IEventService;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.annotations.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventQueryBuilder;
import com.guicedee.activitymaster.fsdm.implementations.interceptors.*;

public class EventsBinder extends PrivateModule implements IGuiceModule<EventsBinder>
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
		
		expose(genericKey);
		expose(IEventService.class);
		
		//IEvent Provider
		@SuppressWarnings("Convert2Diamond")
		Key<IEvent<?,?>> genericKeyIEvent = Key.get(new TypeLiteral<IEvent<?,?>>() {});
		Key<com.guicedee.activitymaster.fsdm.db.entities.events.Event> realKeyIEvent
				= Key.get(com.guicedee.activitymaster.fsdm.db.entities.events.Event.class);
		
		bind(genericKeyIEvent).to(realKeyIEvent);
		bind(realKeyIEvent).toProvider(IEventProvider.class);
		
		expose(genericKeyIEvent);
	}
}
