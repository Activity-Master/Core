package com.guicedee.activitymaster.core.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.db.entities.systems.builders.SystemsQueryBuilder;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SystemsProvider implements Provider<ISystems<Systems, SystemsQueryBuilder>>
{
	@Inject
	private Provider<IEnterprise<?,?>> enterprise;
	
	@Inject
	private Provider<ActivityMasterConfiguration> configuration;
	
	private String systemName;
	
	private static final Map<String, ISystems<Systems, SystemsQueryBuilder>> systemsMap = new ConcurrentHashMap<>();
	
	public SystemsProvider()
	{
	}
	
	public SystemsProvider(String systemName)
	{
		this.systemName = systemName;
	}
	
	@Override
	public ISystems<Systems, SystemsQueryBuilder> get()
	{
		if(EnterpriseProvider.loadedEnterprise == null)
		{
			return new Systems();
		}
		
		if(systemsMap.containsKey(systemName))
		{
			//noinspection unchecked
			return systemsMap.get(systemName);
		}
		Set<IActivityMasterSystem<?>> systems = configuration.get().getAllSystems();
		for (IActivityMasterSystem<?> system : systems)
		{
			if (system.getSystemName()
			          .equals(systemName) || system.getClass()
			                                       .getSimpleName()
			                                       .equals(systemName))
			{
				if(system.hasSystemInstalled(enterprise.get()))
				{
					//noinspection unchecked
					ISystems<Systems, SystemsQueryBuilder> system1 = (ISystems<Systems, SystemsQueryBuilder>) system.getSystem(enterprise.get().getName());
					systemsMap.put(systemName, system1);
					return system1;
				}else {
					return new Systems();
				}
			}
		}
		return new Systems();
	}
	
	public static Map<String, ISystems<Systems, SystemsQueryBuilder>> getSystemsMap()
	{
		return systemsMap;
	}
}
