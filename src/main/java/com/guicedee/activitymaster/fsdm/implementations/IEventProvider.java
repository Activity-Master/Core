package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.activitymaster.fsdm.db.entities.events.builders.EventQueryBuilder;
import com.guicedee.activitymaster.fsdm.implementations.interceptors.EventsAOPInterceptor;

class IEventProvider implements Provider<Event>
{
	@Override
	public Event get()
	{
		@SuppressWarnings("unchecked")
		IEvent<com.guicedee.activitymaster.fsdm.db.entities.events.Event, EventQueryBuilder> iEvent =
				(IEvent<Event, EventQueryBuilder>) EventsAOPInterceptor.getEventThreads()
				                                                       .get();
		if (iEvent == null || iEvent.getId() == null)
		{
			return new Event();
		}
		return (Event) iEvent;
	}
}
