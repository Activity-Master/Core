package com.guicedee.activitymaster.core.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.core.EventsService;
import com.guicedee.activitymaster.core.services.system.IEventService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

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
	}
}
