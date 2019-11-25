package com.armineasy.activitymaster;

import com.google.inject.AbstractModule;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

public class ActivityMasterTestBinder extends AbstractModule
		implements IGuiceModule<ActivityMasterTestBinder>
{

	@Override
	protected void configure()
	{
		super.configure();
		UserAgentStringParser userAgentParser = UADetectorServiceFactory.getResourceModuleParser();
		bind(ReadableUserAgent.class).toInstance(userAgentParser.parse("Mozilla/5.0 (X11; Linux i686; rv:64.0) Gecko/20100101 Firefox/64.0"));
	}
}
