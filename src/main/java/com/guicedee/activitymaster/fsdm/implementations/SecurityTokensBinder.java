package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.SecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class SecurityTokensBinder extends PrivateModule implements IGuiceModule<SecurityTokensBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<ISecurityTokenService<?>> genericKey = Key.get(new TypeLiteral<ISecurityTokenService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<ISecurityTokenService<SecurityTokenService>> realKey
				= Key.get(new TypeLiteral<ISecurityTokenService<SecurityTokenService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(SecurityTokenService.class);
		bind(ISecurityTokenService.class).to(genericKey);
		
		expose(genericKey);
		expose(ISecurityTokenService.class);
	}
}
