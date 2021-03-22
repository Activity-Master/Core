package com.guicedee.activitymaster.core.services.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.guicedinjection.pairing.Pair;

import java.util.UUID;

import static com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem.*;

public class SystemsTokenProvider implements Provider<UUID>
{
	@Inject
	private Provider<IEnterprise<?,?>> enterprise;
	
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
		
		Class<? extends ActivityMasterDefaultSystem> aClass = ActivityMasterDefaultSystem.systemsNamesToClasses.get(systemName);
		Pair eqPair = Pair.of(aClass,null);
		if (systemsEnterpriseTokens.contains(eqPair))
		{
			UUID uuid = systemsEnterpriseTokens.get(systemsEnterpriseTokens.indexOf(eqPair))
			                                   .getValue()
			                                   .get(enterprise.get());
			return uuid;
		}
		else
		{
			return UUID.randomUUID();
		}
		
	}
}
