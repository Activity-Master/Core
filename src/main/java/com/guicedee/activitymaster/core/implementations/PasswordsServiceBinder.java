package com.guicedee.activitymaster.core.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.client.services.IPasswordsService;
import com.guicedee.activitymaster.core.PasswordsService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class PasswordsServiceBinder extends PrivateModule implements IGuiceModule<PasswordsServiceBinder>
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
		
		expose(genericKey);
		expose(IPasswordsService.class);
	}
}
