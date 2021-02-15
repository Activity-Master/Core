package com.guicedee.activitymaster.core.implementations;

import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.*;
import com.guicedee.activitymaster.core.services.system.*;
import com.guicedee.activitymaster.core.systems.TimeSystem;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;

public class ActivityMasterBinder
		extends PrivateModule
		implements IGuiceModule<ActivityMasterBinder>
{
	@Override
	protected void configure()
	{
		bind(ActivityMasterConfiguration.class).in(Singleton.class);
		expose(ActivityMasterConfiguration.class);
		
		bind(IAddressService.class)
				.to(AddressService.class);
		expose(IAddressService.class);
		
		bind(IArrangementsService.class)
				.to(ArrangementsService.class);
		expose(IArrangementsService.class);
		
		bind(ITimeSystem.class)
				.to(TimeSystem.class);
		expose(ITimeSystem.class);
		
		bind(IProductService.class)
				.to(ProductService.class);
		expose(IProductService.class);
		
		bind(IRulesService.class)
				.to(RulesService.class);
		expose(IRulesService.class);
		
		bind(IActivityMasterService.class)
				.to(ActivityMasterService.class);
		expose(IActivityMasterService.class);
		
		bind(ITimeService.class)
				.to(TimeService.class);
		expose(ITimeService.class);
	}
}
