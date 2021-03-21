package com.guicedee.activitymaster.core.implementations;

import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.ActivityMasterService;
import com.guicedee.activitymaster.core.TimeService;
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
		
		bind(ITimeSystem.class)
				.to(TimeSystem.class);
		expose(ITimeSystem.class);
		
		bind(IActivityMasterService.class)
				.to(ActivityMasterService.class);
		expose(IActivityMasterService.class);
		
		bind(ITimeService.class)
				.to(TimeService.class);
		expose(ITimeService.class);
		
		
	}
}
