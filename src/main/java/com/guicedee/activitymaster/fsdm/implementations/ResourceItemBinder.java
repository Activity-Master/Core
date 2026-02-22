package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.ResourceItemService;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class ResourceItemBinder extends AbstractModule implements IGuiceModule<ResourceItemBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IResourceItemService<?>> genericKey = Key.get(new TypeLiteral<IResourceItemService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IResourceItemService<ResourceItemService>> realKey
				= Key.get(new TypeLiteral<IResourceItemService<ResourceItemService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(ResourceItemService.class);
		bind(IResourceItemService.class).to(genericKey);
	}
}
