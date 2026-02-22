package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IPasswordsService;
import com.guicedee.activitymaster.fsdm.PasswordsService;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class PasswordsServiceBinder extends AbstractModule implements IGuiceModule<PasswordsServiceBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IPasswordsService<?>> genericKey = Key.get(new TypeLiteral<IPasswordsService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IPasswordsService<PasswordsService>> realKey
				= Key.get(new TypeLiteral<IPasswordsService<PasswordsService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(PasswordsService.class);
		bind(IPasswordsService.class).to(genericKey);

	}
}
