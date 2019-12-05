package com.guicedee.activitymaster.core.systems;

import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.implementations.ActiveFlagService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.entityassist.enumerations.ActiveFlag;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActiveFlagSystem
		implements IActivityMasterSystem<ActiveFlagSystem>
{
	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Active Flag Service", "Loading Active Flags", progressMonitor);
		for (ActiveFlag activeFlag : ActiveFlag.values())
		{
			GuiceContext.get(ActiveFlagService.class).create(enterprise, activeFlag.name(), activeFlag.getDescription());
		}
	}

	@Override
	public int totalTasks()
	{
		return ActiveFlag.values().length;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 1;
	}

	@Override
	public void postUpdate(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> newSystem = GuiceContext.get(SystemsService.class)
		                                    .create(enterprise, "Active Flag System", "The system for the active flag management", "Active Flag System");
		UUID securityToken = GuiceContext.get(ISystemsService.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
