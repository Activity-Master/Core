package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;

import java.util.UUID;

public class SystemsTokenProvider implements Provider<UUID>
{
	@Inject
	private Provider<IEnterprise<?, ?>> enterprise;
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	private String systemName;
	
	public SystemsTokenProvider()
	{
	}
	
	public SystemsTokenProvider(String systemName)
	{
		this.systemName = systemName;
	}
	
	@Override
	public UUID get()
	{
		if (enterprise.get()
		              .isFake())
		{
			return UUID.randomUUID();
		}
		return systemsService.get()
		                     .getSecurityIdentityToken(systemsService.get()
		                                                             .findSystem(enterprise.get(), systemName));
	}
}
