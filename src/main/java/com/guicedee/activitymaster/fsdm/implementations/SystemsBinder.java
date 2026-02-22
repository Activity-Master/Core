package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.guicedee.activitymaster.fsdm.SystemsService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.client.services.lifecycle.IGuiceModule;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Set;

@Log4j2
public class SystemsBinder extends AbstractModule implements IGuiceModule<SystemsBinder>
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
	}
}
