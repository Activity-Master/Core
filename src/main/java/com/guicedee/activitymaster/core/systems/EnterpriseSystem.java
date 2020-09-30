package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.guicedinjection.interfaces.JobService;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.UUID;

@Singleton
public class EnterpriseSystem
		extends ActivityMasterDefaultSystem<EnterpriseSystem>
		implements IActivityMasterSystem<EnterpriseSystem>
{
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
		super.postStartup(enterprise, progressMonitor);
	}
	
	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = (Systems) getSystem(enterprise);
		UUID securityToken = getSystemToken(enterprise);
		if (!enterprise.hasClassifications(EnterpriseClassifications.Version, newSystem, securityToken))
		{
			enterprise.addOrUpdate(EnterpriseClassifications.Version, ActivityMasterConfiguration.version.toString(), newSystem, securityToken);
			enterprise.addOrUpdate(EnterpriseClassifications.RequiresUpdate, Boolean.FALSE.toString(), newSystem, securityToken);
			ActivityMasterConfiguration.requiresUpdate = enterprise.findClassifications(EnterpriseClassifications.RequiresUpdate, enterprise, securityToken)
			                                                       .orElseThrow()
			                                                       .getValueAsBoolean();
		}
	}
	
	@Override
	public String getSystemName()
	{
		return "Enterprise System";
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for handling enterprises";
	}
	
}
