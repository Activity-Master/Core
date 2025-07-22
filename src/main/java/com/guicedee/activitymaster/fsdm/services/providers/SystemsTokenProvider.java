package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
				.findSystem(enterprise.get(), systemName)
				.chain(system -> {
					return systemsService.get()
							.getSecurityIdentityToken(system);
				})
				.await()
				.atMost(Duration.of(50L, ChronoUnit.SECONDS))
		;
	}
}
