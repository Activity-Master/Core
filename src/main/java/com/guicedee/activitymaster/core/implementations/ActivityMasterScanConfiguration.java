package com.guicedee.activitymaster.core.implementations;

import com.guicedee.guicedinjection.GuiceConfig;
import com.guicedee.guicedinjection.interfaces.IGuiceConfigurator;

public class ActivityMasterScanConfiguration implements IGuiceConfigurator
{
	@Override
	public GuiceConfig<?> configure(GuiceConfig config)
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
