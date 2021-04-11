package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.matcher.Matchers;
import com.guicedee.guicedinjection.abstractions.GuiceInjectorModule;
import com.guicedee.guicedinjection.interfaces.IGuiceDefaultBinder;
import com.guicedee.activitymaster.fsdm.client.services.annotations.*;
import com.guicedee.activitymaster.fsdm.implementations.interceptors.*;

/**
 * Special Abstract Module to apply interceptors to any binding located in the DI
 *
 * Assigned to the root/parent Module
 */
public class EventInterceptorsBinder implements IGuiceDefaultBinder<EventInterceptorsBinder, GuiceInjectorModule>
{
	@Override
	public void onBind(GuiceInjectorModule module)
	{
		module.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Event.class), new EventsAOPInterceptor());
		
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(AddressEvent.class), new AddressEventAOPInterceptor());
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(ArrangementEvent.class), new ArrangementEventAOPInterceptor());
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(ClassificationEvent.class), new ClassificationEventAOPInterceptor());
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(GeographyEvent.class), new GeographyEventAOPInterceptor());
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(InvolvedPartyEvent.class), new InvolvedPartyEventAOPInterceptor());
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(ProductEvent.class), new ProductEventAOPInterceptor());
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(ResourceItemEvent.class), new ResourceItemEventAOPInterceptor());
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(RulesEvent.class), new RulesEventAOPInterceptor());
		module.bindInterceptor(Matchers.any(),Matchers.annotatedWith(LogItemEvent.class), new LogItemEventAOPInterceptor());
	}
}
