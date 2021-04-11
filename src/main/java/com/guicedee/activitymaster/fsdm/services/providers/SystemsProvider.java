package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import lombok.extern.java.Log;
import com.guicedee.activitymaster.fsdm.client.services.ConsoleLogActivityMasterProgressMaster;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.builders.SystemsQueryBuilder;

import java.util.logging.Level;

import static com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem.*;

@Log
public class SystemsProvider implements Provider<ISystems<Systems, SystemsQueryBuilder>>
{
	@Inject
	private Provider<IEnterprise<?, ?>> enterprise;
	
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
		if (EnterpriseProvider.loadedEnterprise == null || EnterpriseProvider.loadedEnterprise.isFake())
		{
			return new Systems();
		}
		Class<? extends ActivityMasterDefaultSystem> dd = ActivityMasterDefaultSystem.systemsNamesToClasses.get(systemName);
		Pair eqPair = Pair.of(dd, null);
		if (systemsEnterpriseSystems.contains(eqPair))
		{
			return (ISystems<Systems, SystemsQueryBuilder>)
					systemsEnterpriseSystems.get(systemsEnterpriseSystems.indexOf(eqPair))
					                        .getValue()
					                        .get(enterprise.get());
		}
		else
		{
			try
			{
				if (ActivityMasterConfiguration.get()
				                               .isEnterpriseReady())
				{
					IEnterpriseService<?> enterpriseService = GuiceContext.get(IEnterpriseService.class);
					enterpriseService.performPostStartup(enterprise.get());
					if (systemsEnterpriseSystems.contains(eqPair))
					{
						return (ISystems<Systems, SystemsQueryBuilder>)
								systemsEnterpriseSystems.get(systemsEnterpriseSystems.indexOf(eqPair))
								                        .getValue()
								                        .get(enterprise.get());
					}
				}
				return new Systems();
			}
			catch (Throwable T)
			{
				log.log(Level.SEVERE, "Cannot check system ", T);
				return new Systems();
			}
		}
	}
}
