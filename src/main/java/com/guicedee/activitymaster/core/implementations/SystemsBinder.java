package com.guicedee.activitymaster.core.implementations;

import com.google.inject.*;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.client.services.ISystemsService;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.core.SystemsService;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.db.entities.systems.builders.SystemsQueryBuilder;
import com.guicedee.activitymaster.core.services.providers.SystemsProvider;
import com.guicedee.activitymaster.core.services.providers.SystemsTokenProvider;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

import java.util.UUID;

public class SystemsBinder extends PrivateModule implements IGuiceModule<SystemsBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<ISystemsService<?>> genericKey = Key.get(new TypeLiteral<ISystemsService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<ISystemsService<SystemsService>> realKey
				= Key.get(new TypeLiteral<ISystemsService<SystemsService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(SystemsService.class);
		bind(ISystemsService.class).to(genericKey);
		
		expose(genericKey);
		expose(ISystemsService.class);

		//All systems named bindings
		for (IActivityMasterSystem<?> system : IActivityMasterSystem.allSystems())
		{
			@SuppressWarnings("Convert2Diamond")
			Key<ISystems<?,?>> aSystemGenericKey = Key.get(new TypeLiteral<ISystems<?,?>>() {}, Names.named(system.getSystemName()));
			@SuppressWarnings("Convert2Diamond")
			Key<ISystems<Systems, SystemsQueryBuilder>> aSystemRealKey = Key.get(new TypeLiteral<ISystems<Systems, SystemsQueryBuilder>>() {}, Names.named(system.getSystemName()));
			
			bind(aSystemGenericKey).to(aSystemRealKey);
			bind(aSystemRealKey).toProvider(new SystemsProvider(system.getSystemName()));
			
			expose(aSystemGenericKey);
			
			Key<UUID> aSystemTokenGenericKey = Key.get(UUID.class, Names.named(system.getSystemName()));
			bind(aSystemTokenGenericKey).toProvider(new SystemsTokenProvider(system.getSystemName()));
			expose(aSystemTokenGenericKey);
		}
	}
}
