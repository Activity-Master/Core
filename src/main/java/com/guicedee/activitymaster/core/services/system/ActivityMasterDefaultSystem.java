package com.guicedee.activitymaster.core.services.system;

import com.google.inject.Inject;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.guicedinjection.pairing.Pair;
import lombok.extern.java.Log;

import java.util.*;

@SuppressWarnings("rawtypes")
@Log
public abstract class ActivityMasterDefaultSystem<J extends ActivityMasterDefaultSystem<J>>
		implements IActivityMasterSystem<J>
{
	private static final List<Pair<Class<? extends ActivityMasterDefaultSystem>, Map<IEnterprise<?>, ISystems<?>>>> systemsEnterpriseSystems = new ArrayList<>();
	private static final List<Pair<Class<? extends ActivityMasterDefaultSystem>, Map<IEnterprise<?>, UUID>>> systemsEnterpriseTokens = new ArrayList<>();
	
	@Inject
	private ISystemsService<?> systemsService;
	
	@Inject
	private IEnterpriseService<?> enterpriseService;
	
	@Override
	public void postStartup(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		getSystem(enterprise);
		getSystemToken(enterprise);
	}
	
	@Override
	public boolean hasSystemInstalled(IEnterprise<?> enterprise)
	{
		return systemsService.doesSystemExist(enterprise, getSystemName());
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
			ISystems<?> sys = systemsService.findSystem(enterprise, getSystemName());
			if (sys == null)
			{
				sys = systemsService.create(enterprise, getSystemName(), getSystemDescription(), getSystemName());
				systemsService.registerNewSystem(enterprise, sys);
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
			UUID token = systemsService.getSecurityIdentityToken(system);
			systemTokens.put(enterprise, token);
		}
		return systemTokens.get(enterprise);
	}
	
	public abstract String getSystemName();
	
	public abstract String getSystemDescription();
	
	public ISystems<?> getSystem(String enterpriseName)
	{
		IEnterprise<?> enterprise = enterpriseService.getEnterprise(enterpriseName);
		return getSystem(enterprise);
	}
	
	
	public UUID getSystemToken(String enterpriseName)
	{
		IEnterprise<?> enterprise = enterpriseService.getEnterprise(enterpriseName);
		return getSystemToken(enterprise);
	}
}
