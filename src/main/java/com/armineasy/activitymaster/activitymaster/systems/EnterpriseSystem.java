package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.JobService;
import com.jwebmp.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.EnterpriseClassifications.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

public class EnterpriseSystem
		implements IActivityMasterSystem<EnterpriseSystem>
{
	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{

	}

	@Override
	public int totalTasks()
	{
		return 0;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE;
	}


	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public void postUpdate(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		defaultWaitTime = 5L;
		defaultWaitUnit = TimeUnit.MINUTES;
		JobService.getInstance().destroy();
		ISystems<?> newSystem = GuiceContext.get(SystemsService.class)
		                                    .create(enterprise, "Enterprise System",
		                                        "The system for handling enterprises", "");

		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);
		systemTokens.put(enterprise, securityToken);

		if (!enterprise.has(Version, newSystem, securityToken))
		{
			enterprise.addOrUpdate(Version, ActivityMasterConfiguration.version.toString(), newSystem, securityToken);
			enterprise.addOrUpdate(RequiresUpdate, Boolean.FALSE.toString(), newSystem, securityToken);
			ActivityMasterConfiguration.requiresUpdate = enterprise.find(RequiresUpdate, newSystem, securityToken)
			                                                       .orElseThrow()
			                                                       .getValueAsBoolean();
		}
	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
