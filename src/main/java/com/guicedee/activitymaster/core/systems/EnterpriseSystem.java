package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
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

@Singleton
public class EnterpriseSystem
		implements IActivityMasterSystem<EnterpriseSystem>
{
	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();
	private static final Map<IEnterprise<?>, ISystems<?>> systemsMap = new HashMap<>();

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
	public void postStartup(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		/*defaultWaitTime = 5L;
		defaultWaitUnit = TimeUnit.MINUTES;*/
		JobService.getInstance()
		          .destroy();

		final String systemName = "Enterprise System";
		final String systemDesc = "The system for handling enterprises";
		Systems sys = (Systems) GuiceContext.get(SystemsService.class)
		                                    .findSystem(enterprise, systemName);
		UUID securityToken = null;
		if (sys == null)
		{
			sys = (Systems) GuiceContext.get(SystemsService.class)
			                                    .create(enterprise, systemName, systemDesc, systemName);

			securityToken = GuiceContext.get(ISystemsService.class)
			                            .registerNewSystem(enterprise, sys);
		}
		else
		{
			securityToken = GuiceContext.get(SystemsService.class)
			                            .getSecurityIdentityToken(sys);
		}
		systemTokens.put(enterprise, securityToken);
		systemsMap.put(enterprise, sys);
	}

	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = (Systems) systemsMap.get(enterprise);
		UUID securityToken = systemTokens.get(enterprise);
		if (!enterprise.has(EnterpriseClassifications.Version, systemsMap.get(enterprise), systemTokens.get(enterprise)))
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
