package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IArrangementsService;
import com.guicedee.activitymaster.fsdm.ArrangementsService;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class ArrangementsBinder extends PrivateModule implements IGuiceModule<ArrangementsBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IArrangementsService<?>> genericKey = Key.get(new TypeLiteral<IArrangementsService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IArrangementsService<ArrangementsService>> realKey
				= Key.get(new TypeLiteral<IArrangementsService<ArrangementsService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(ArrangementsService.class);
		bind(IArrangementsService.class).to(genericKey);
		
		expose(genericKey);
		expose(IArrangementsService.class);
	}
}
