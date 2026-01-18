package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.scopes.CallScopeProperties;
import com.guicedee.client.scopes.CallScoper;

class IEventProvider implements Provider<Event>
{
	@Override
	public Event get()
	{
		CallScoper scoper = IGuiceContext.get(CallScoper.class);
		if (scoper.isStartedScope())
		{
			CallScopeProperties csp = IGuiceContext.get(CallScopeProperties.class);
			if (csp != null)
			{
				IEvent<?, ?> iEvent = (IEvent<?, ?>) csp.getProperties().get("fsdm.event");
				if (iEvent != null && iEvent.getId() != null)
				{
					return (Event) iEvent;
				}
			}
		}
		return new Event();
	}
}
