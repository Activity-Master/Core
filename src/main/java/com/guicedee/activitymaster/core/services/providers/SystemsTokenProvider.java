package com.guicedee.activitymaster.core.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.ISecurityTokenService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;

import java.util.Set;
import java.util.UUID;

public class SystemsTokenProvider implements Provider<UUID>
{
	@Inject
	private Provider<IEnterprise<?>> enterprise;
	
	private String systemName;
	
	@Inject
	private Provider<ActivityMasterConfiguration> configuration;
	
	@Inject
	private ISystemsService<?> systemsService;
	
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
		
		Set<IActivityMasterSystem<?>> systems = configuration.get()
		                                                     .getAllSystems();
		if (systemsService.doesSystemExist(enterprise.get(), ISecurityTokenService.SecurityTokenSystemName))
		{
			for (IActivityMasterSystem<?> system : systems)
			{
				if (system.getSystemName()
				          .equals(systemName))
				{
					if (system.hasSystemInstalled(enterprise.get()))
					{
						try
						{
							return system.getSystemToken(enterprise.get()
							                                       .classificationName());
						}catch (Throwable T)
						{
							//Sys Tokens not ready
						}
					}
					else
					{
						break;
					}
				}
			}
		}
		return UUID.randomUUID();
	}
}
