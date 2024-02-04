package com.guicedee.activitymaster.fsdm.implementations;

import com.guicedee.guicedinjection.interfaces.IGuiceConfig;
import com.guicedee.guicedinjection.interfaces.IGuiceConfigurator;

public class ActivityMasterScanConfiguration
		implements IGuiceConfigurator
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
