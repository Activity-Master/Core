package com.guicedee.activitymaster.core.services.providers;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.enterprise.builders.EnterpriseQueryBuilder;
import com.guicedee.activitymaster.core.systems.SecurityTokenSystem;
import com.guicedee.guicedinjection.GuiceContext;

public class EnterpriseProvider implements Provider<IEnterprise<Enterprise, EnterpriseQueryBuilder>>
{
	@Inject
	private Provider<ActivityMasterConfiguration> configuration;
	
	public static IEnterprise<Enterprise,EnterpriseQueryBuilder> loadedEnterprise = null;
	
	@Override
	public IEnterprise<Enterprise,EnterpriseQueryBuilder> get()
	{
		if (loadedEnterprise != null)
		{
			return loadedEnterprise;
		}
		if (!Strings.isNullOrEmpty(configuration.get().getApplicationEnterpriseName()))
		{
			@SuppressWarnings("unchecked")
			IEnterprise<Enterprise,EnterpriseQueryBuilder> ent = (IEnterprise<Enterprise,EnterpriseQueryBuilder>) configuration.get().getEnterprise();
			if (ent == null)
			{
				return new Enterprise();
			}
			if(GuiceContext.get(SecurityTokenSystem.class).hasSystemInstalled(ent))
			{
				System.out.println("Enabling Authentication Modules");
				configuration.get().setSecurityEnabled(true);
			}
			loadedEnterprise = ent;
			return ent;
		}
		else
		{
			return new Enterprise();
		}
	}
}
