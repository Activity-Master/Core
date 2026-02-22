package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IAddressService;
import com.guicedee.activitymaster.fsdm.AddressService;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class AddressBinder extends AbstractModule implements IGuiceModule<AddressBinder>
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

	}
}
