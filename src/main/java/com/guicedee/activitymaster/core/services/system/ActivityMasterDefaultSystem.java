package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;

import java.util.*;

public abstract class ActivityMasterDefaultSystem<J extends ActivityMasterDefaultSystem<J>>
		implements IActivityMasterSystem<J>
{
	private List<Pair<Class<? extends ActivityMasterDefaultSystem>, Map<IEnterprise<?>, ISystems<?>>>> systemsEnterpriseSystems = new ArrayList<>();
	private List<Pair<Class<? extends ActivityMasterDefaultSystem>, Map<IEnterprise<?>, UUID>>> systemsEnterpriseTokens = new ArrayList<>();

	@Override
	public void postStartup(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		getSystem(enterprise);
		getSystemToken(enterprise);
	}

	public ISystems<?> getSystem(IEnterprise<?> enterprise)
	{
		var oSystemsMap = systemsEnterpriseSystems.stream()
		                                          .filter(a -> a.getKey()
		                                                        .equals(getClass()))
		                                          .findFirst();
		if (oSystemsMap.isEmpty())
		{
			Pair<Class<? extends ActivityMasterDefaultSystem>, Map<IEnterprise<?>, ISystems<?>>> pair = Pair.of(getClass(), new HashMap<>());
			oSystemsMap = Optional.of(pair);
			systemsEnterpriseSystems.add(pair);
		}
		Map<IEnterprise<?>, ISystems<?>> systemsMap = oSystemsMap.get()
		                                                         .getValue();
		if (systemsMap.get(enterprise) == null)
		{
			ISystems<?> sys = GuiceContext.get(ISystemsService.class)
			                              .findSystem(enterprise, getSystemName());
			if (sys == null)
			{
				sys = GuiceContext.get(ISystemsService.class)
				                  .create(enterprise, getSystemName(), getSystemDescription(), getSystemName());
				GuiceContext.get(ISystemsService.class)
				            .registerNewSystem(enterprise, sys);
			}
			systemsMap.put(enterprise, sys);
		}
		return systemsMap.get(enterprise);
	}

	public UUID getSystemToken(IEnterprise<?> enterprise)
	{
		var oSystemsMap = systemsEnterpriseTokens.stream()
		                                         .filter(a -> a.getKey()
		                                                       .equals(getClass()))
		                                         .findFirst();
		if (oSystemsMap.isEmpty())
		{
			Pair<Class<? extends ActivityMasterDefaultSystem>, Map<IEnterprise<?>, UUID>> pair = Pair.of(getClass(), new HashMap<>());
			oSystemsMap = Optional.of(pair);
			systemsEnterpriseTokens.add(pair);
		}
		Map<IEnterprise<?>, UUID> systemTokens = oSystemsMap.get()
		                                                    .getValue();
		if (systemTokens.get(enterprise) == null)
		{
			ISystems<?> system = getSystem(enterprise);
			UUID token = GuiceContext.get(SystemsService.class)
			                         .getSecurityIdentityToken(system);
			systemTokens.put(enterprise, token);
		}
		return systemTokens.get(enterprise);
	}

	public abstract String getSystemName();

	public abstract String getSystemDescription();
	
	public ISystems<?> getSystem(String enterpriseName)
	{
		IEnterprise<?> enterprise = GuiceContext.get(IEnterpriseService.class)
		                                        .getEnterprise(enterpriseName);
		return getSystem(enterprise);
	}
	
	
	public UUID getSystemToken(String enterpriseName)
	{
		IEnterprise<?> enterprise = GuiceContext.get(IEnterpriseService.class)
		                                        .getEnterprise(enterpriseName);
		return getSystemToken(enterprise);
	}
}
