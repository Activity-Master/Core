package com.guicedee.activitymaster.fsdm.implementations;

import com.guicedee.client.services.IGuiceConfig;
import com.guicedee.client.services.lifecycle.IGuiceConfigurator;

public class ActivityMasterScanConfiguration
		implements IGuiceConfigurator<ActivityMasterScanConfiguration>
{
	@Override
	public IGuiceConfig<?> configure(IGuiceConfig<?> config)
	{
		config.setIncludeModuleAndJars(true)
		      .setClasspathScanning(true)
		      .setMethodInfo(true)
		      .setFieldInfo(true)
		      .setAnnotationScanning(true)
		;
		return config;
	}
}
