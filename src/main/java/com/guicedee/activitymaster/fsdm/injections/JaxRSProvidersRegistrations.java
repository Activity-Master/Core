package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.activitymaster.fsdm.client.services.converters.providers.*;

public class JaxRSProvidersRegistrations implements IGuicePreStartup<JaxRSProvidersRegistrations>
{
	@Override
	public void onStartup()
	{
		if (ModuleLayer.boot()
		               .findModule("com.guicedee.guicedservlets.rest")
		               .isPresent())
		{
			com.guicedee.guicedservlets.rest.RESTContext.getProviders()
			                                            .add(ISystemsParameterConvertorProvider.class.getCanonicalName());
			com.guicedee.guicedservlets.rest.RESTContext.getProviders()
			                                            .add(IInvolvedPartyParameterConvertorProvider.class.getCanonicalName());
		}
	}
}
