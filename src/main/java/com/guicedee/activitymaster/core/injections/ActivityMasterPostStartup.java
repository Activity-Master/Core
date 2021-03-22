package com.guicedee.activitymaster.core.injections;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Inject;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.deserializers.EnterpriseDeserializer;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.IGuicePostStartup;

import static com.guicedee.guicedinjection.interfaces.ObjectBinderKeys.*;

public class ActivityMasterPostStartup implements IGuicePostStartup<ActivityMasterPostStartup>
{
	@Inject
	private ActivityMasterConfiguration configuration;
	
	@Override
	public void postLoad()
	{
		GuiceContext.get(DefaultObjectMapper)
		            .registerModule(new SimpleModule("ActivityMasterJsonModule", Version.unknownVersion()).addDeserializer(IEnterprise.class, new EnterpriseDeserializer()));
	}
}
