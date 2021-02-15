package com.guicedee.activitymaster.core.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.core.ResourceItemService;
import com.guicedee.activitymaster.core.services.system.IResourceItemService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class ResourceItemBinder extends PrivateModule implements IGuiceModule<ResourceItemBinder>
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
		
		expose(genericKey);
		expose(IResourceItemService.class);
	}
}
