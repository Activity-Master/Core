package com.guicedee.activitymaster.core.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.db.entities.systems.builders.SystemsQueryBuilder;
import com.guicedee.guicedinjection.pairing.Pair;

import static com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem.*;

public class SystemsProvider implements Provider<ISystems<Systems, SystemsQueryBuilder>>
{
	@Inject
	private Provider<IEnterprise<?,?>> enterprise;
	
	private String systemName;
	
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
		if(EnterpriseProvider.loadedEnterprise == null || EnterpriseProvider.loadedEnterprise.isFake())
		{
			return new Systems();
		}
		Class<? extends ActivityMasterDefaultSystem> dd = ActivityMasterDefaultSystem.systemsNamesToClasses.get(systemName);
		
		Pair eqPair = Pair.of(dd,null);
		if(systemsEnterpriseSystems.contains(eqPair))
		{
			return (ISystems<Systems, SystemsQueryBuilder>) systemsEnterpriseSystems.get(systemsEnterpriseSystems.indexOf(eqPair)).getValue().get(enterprise.get());
		}else {
			System.out.println("System not yet ready - " + this.systemName);
			return new Systems();
		}
		/*
		if(systemsMap.containsKey(systemName))
		{
			//noinspection
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
					break;
				}else {
					systemsMap.put(systemName, new Systems());
				}
			}
		}
		return systemsMap.get(systemName);*/
	}
	
	/*public static Map<String, ISystems<Systems, SystemsQueryBuilder>> getSystemsMap()
	{
		return systemsMap;
	}*/
}
