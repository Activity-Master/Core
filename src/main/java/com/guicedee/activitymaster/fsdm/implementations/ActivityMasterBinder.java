package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.AbstractModule;
import com.google.inject.PrivateModule;
import com.guicedee.activitymaster.fsdm.ActivityMasterService;
import com.guicedee.activitymaster.fsdm.TimeService;
import com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService;
import com.guicedee.activitymaster.fsdm.client.services.ITimeService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import com.guicedee.activitymaster.fsdm.systems.TimeSystem;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class ActivityMasterBinder
		extends AbstractModule
		implements IGuiceModule<ActivityMasterBinder>
{
	@Override
	protected void configure()
	{
		bind(ActivityMasterConfiguration.class).toInstance(ActivityMasterConfiguration.get());
	//	expose(ActivityMasterConfiguration.class);
		
		// Bind TimeSystem for use by TimeSystemAdapter
		//bind(TimeSystem.class);
		
		// Bind ITimeSystem to TimeSystemAdapter for reactive interface
		bind(ITimeSystem.class)
				.to(TimeSystem.class);
		//expose(ITimeSystem.class);
		
		bind(IActivityMasterService.class)
				.to(ActivityMasterService.class);
		//expose(IActivityMasterService.class);
		
		bind(ITimeService.class)
				.to(TimeService.class);
		//expose(ITimeService.class);
	}
}
