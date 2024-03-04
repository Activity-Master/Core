package com.guicedee.activitymaster.fsdm.services.providers;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.fsdm.systems.SecurityTokenSystem;
import lombok.extern.java.Log;

@Log
public class EnterpriseProvider implements Provider<IEnterprise<Enterprise, EnterpriseQueryBuilder>>
{
	@Inject
	private Provider<ActivityMasterConfiguration> configuration;
	
	@Inject
	private Provider<IEnterpriseService<?>> enterpriseService;
	
	public static IEnterprise<Enterprise, EnterpriseQueryBuilder> loadedEnterprise = null;
	
	@Override
	public IEnterprise<Enterprise, EnterpriseQueryBuilder> get()
	{
		if (loadedEnterprise != null)
		{
			return loadedEnterprise;
		}
		ActivityMasterConfiguration activityMasterConfiguration = configuration.get();
		if (!Strings.isNullOrEmpty(activityMasterConfiguration.getApplicationEnterpriseName()))
		{
			if(enterpriseService.get().doesEnterpriseExist(activityMasterConfiguration.getApplicationEnterpriseName()))
			{
				@SuppressWarnings("unchecked")
				IEnterprise<Enterprise, EnterpriseQueryBuilder> ent = (IEnterprise<Enterprise, EnterpriseQueryBuilder>)
						enterpriseService.get()
						                 .getEnterprise(activityMasterConfiguration.getApplicationEnterpriseName());
				loadedEnterprise = ent;
				if (com.guicedee.client.IGuiceContext.get(SecurityTokenSystem.class)
				                .hasSystemInstalled(ent))
				{
					log.info("Enabling Enterprise Security Stack.....");
					activityMasterConfiguration.setSecurityEnabled(true);
				}
			}
		}
		if (loadedEnterprise == null)
		{
			return new Enterprise();
		}
		return loadedEnterprise;
	}
}
