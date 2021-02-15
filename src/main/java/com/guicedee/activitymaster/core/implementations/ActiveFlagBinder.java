package com.guicedee.activitymaster.core.implementations;

import com.google.inject.*;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.core.ActiveFlagService;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.services.dto.IActiveFlag;
import com.guicedee.activitymaster.core.services.providers.ActiveFlagProvider;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class ActiveFlagBinder extends PrivateModule implements IGuiceModule<ActiveFlagBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IActiveFlagService<?>> genericKey = Key.get(new TypeLiteral<IActiveFlagService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IActiveFlagService<ActiveFlagService>> realKey
				= Key.get(new TypeLiteral<IActiveFlagService<ActiveFlagService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(ActiveFlagService.class);
		bind(IActiveFlagService.class).to(genericKey);
		
		expose(genericKey);
		expose(IActiveFlagService.class);
		
		for (com.entityassist.enumerations.ActiveFlag value : com.entityassist.enumerations.ActiveFlag.values())
		{
			@SuppressWarnings("Convert2Diamond")
			Key<IActiveFlag<?>> genericATypeKey = Key.get(new TypeLiteral<IActiveFlag<?>>() {}, Names.named(value.name()));
			@SuppressWarnings("Convert2Diamond")
			Key<IActiveFlag<ActiveFlag>> realATypeKey = Key.get(new TypeLiteral<IActiveFlag<ActiveFlag>>() {}, Names.named(value.name()));
			bind(genericATypeKey).to(realATypeKey);
			bind(realATypeKey).toProvider(new ActiveFlagProvider(value));
			
			expose(genericATypeKey);
		}
	}
}
