package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.ActiveFlagService;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.builders.ActiveFlagQueryBuilder;
import com.guicedee.activitymaster.fsdm.services.providers.ActiveFlagProvider;
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
			Key<IActiveFlag<?,?>> genericATypeKey = Key.get(new TypeLiteral<IActiveFlag<?,?>>() {}, Names.named(value.name()));
			@SuppressWarnings("Convert2Diamond")
			Key<IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder>> realATypeKey = Key.get(new TypeLiteral<IActiveFlag<ActiveFlag, ActiveFlagQueryBuilder>>() {}, Names.named(value.name()));
			bind(genericATypeKey).to(realATypeKey);
			
			bind(realATypeKey).toProvider(new ActiveFlagProvider(value));
			
			expose(genericATypeKey);
		}
	}
}
