package com.guicedee.activitymaster.core.services.providers;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;

public class EnterpriseProvider implements Provider<IEnterprise<Enterprise>>
{
	@Inject
	private Provider<ActivityMasterConfiguration> configuration;
	
	private static IEnterprise<Enterprise> loadedEnterprise = null;
	
	@Override
	public IEnterprise<Enterprise> get()
	{
		if (loadedEnterprise != null)
		{
			return loadedEnterprise;
		}
		if (!Strings.isNullOrEmpty(configuration.get().getApplicationEnterpriseName()))
		{
			IEnterprise<Enterprise> ent = (IEnterprise<Enterprise>) configuration.get().getEnterprise();
			loadedEnterprise = ent;
			return ent;
		}
		else
		{
			return new Enterprise();
		}
	}
}
