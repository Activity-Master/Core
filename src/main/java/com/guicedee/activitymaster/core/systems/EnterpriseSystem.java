package com.guicedee.activitymaster.core.systems;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.JobService;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.guicedee.guicedinjection.GuiceContext.*;

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

		UUID securityToken = GuiceContext.get(ISystemsService.class)
		                                 .registerNewSystem(enterprise, newSystem);
		systemTokens.put(enterprise, securityToken);

		if (!enterprise.has(EnterpriseClassifications.Version, newSystem, securityToken))
		{
			enterprise.addOrUpdate(EnterpriseClassifications.Version, ActivityMasterConfiguration.version.toString(), newSystem, securityToken);
			enterprise.addOrUpdate(EnterpriseClassifications.RequiresUpdate, Boolean.FALSE.toString(), newSystem, securityToken);
			ActivityMasterConfiguration.requiresUpdate = enterprise.find(EnterpriseClassifications.RequiresUpdate, newSystem, securityToken)
			                                                       .orElseThrow()
			                                                       .getValueAsBoolean();
		}
	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
