package com.guicedee.activitymaster.core.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.core.InvolvedPartyService;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class InvolvedPartiesBinder extends PrivateModule implements IGuiceModule<InvolvedPartiesBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IInvolvedPartyService<?>> genericKey = Key.get(new TypeLiteral<IInvolvedPartyService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IInvolvedPartyService<InvolvedPartyService>> realKey
				= Key.get(new TypeLiteral<IInvolvedPartyService<InvolvedPartyService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(InvolvedPartyService.class);
		bind(IInvolvedPartyService.class).to(genericKey);
		
		expose(genericKey);
		expose(IInvolvedPartyService.class);
	}
}
