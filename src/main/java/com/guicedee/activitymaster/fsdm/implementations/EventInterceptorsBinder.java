package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.AbstractModule;
//import com.guicedee.activitymaster.fsdm.implementations.interceptors.*;
import com.guicedee.client.services.lifecycle.IGuiceModule;

/**
 * Special Abstract Module to apply interceptors to any binding located in the DI
 *
 * Assigned to the root/parent Module
 */
public class EventInterceptorsBinder
		extends AbstractModule
		implements IGuiceModule<EventInterceptorsBinder>
{
	public void configure()
	{
		/*bindInterceptor(Matchers.any(), Matchers.annotatedWith(Event.class), new EventsAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(AddressEvent.class), new AddressEventAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(ArrangementEvent.class), new ArrangementEventAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(ClassificationEvent.class), new ClassificationEventAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(GeographyEvent.class), new GeographyEventAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(InvolvedPartyEvent.class), new InvolvedPartyEventAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(ProductEvent.class), new ProductEventAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(ResourceItemEvent.class), new ResourceItemEventAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(RulesEvent.class), new RulesEventAOPInterceptor());
		bindInterceptor(Matchers.any(),Matchers.annotatedWith(LogItemEvent.class), new LogItemEventAOPInterceptor());*/
	}
}
