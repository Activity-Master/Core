package com.guicedee.activitymaster.core.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SystemsProvider implements Provider<ISystems<Systems>>
{
	@Inject
	private Provider<IEnterprise<?>> enterprise;
	
	@Inject
	private Provider<ActivityMasterConfiguration> configuration;
	
	private String systemName;
	
	private static final Map<String, ISystems<?>> systemsMap = new ConcurrentHashMap<>();
	
	public SystemsProvider()
	{
	}
	
	public SystemsProvider(String systemName)
	{
		this.systemName = systemName;
	}
	
	@Override
	public ISystems<Systems> get()
	{
		if (enterprise.get().isFake())
		{
			return new Systems();
		}
		
		if(systemsMap.containsKey(systemName))
		{
			//noinspection unchecked
			return (ISystems<Systems>) systemsMap.get(systemName);
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
					ISystems<Systems> system1 = (ISystems<Systems>) system.getSystem(enterprise.get()
					                                                                           .classificationName());
					systemsMap.put(systemName, system1);
					return system1;
				}else {
					return new Systems();
				}
			}
		}
		return new Systems();
	}
	
	public static Map<String, ISystems<?>> getSystemsMap()
	{
		return systemsMap;
	}
}
