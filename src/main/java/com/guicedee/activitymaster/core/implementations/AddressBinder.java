package com.guicedee.activitymaster.core.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.client.services.IAddressService;
import com.guicedee.activitymaster.core.AddressService;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class AddressBinder extends PrivateModule implements IGuiceModule<AddressBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IAddressService<?>> genericKey = Key.get(new TypeLiteral<IAddressService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IAddressService<AddressService>> realKey
				= Key.get(new TypeLiteral<IAddressService<AddressService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(AddressService.class);
		bind(IAddressService.class).to(genericKey);
		
		expose(genericKey);
		expose(IAddressService.class);
	}
}
