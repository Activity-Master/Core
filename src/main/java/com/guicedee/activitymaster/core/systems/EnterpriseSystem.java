package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.client.services.ISystemsService;
import com.guicedee.activitymaster.client.services.administration.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import lombok.extern.java.Log;

import static com.guicedee.activitymaster.client.services.IEnterpriseService.*;


@Log
public class EnterpriseSystem
		extends ActivityMasterDefaultSystem<EnterpriseSystem>
		implements IActivityMasterSystem<EnterpriseSystem>
{
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public void registerSystem(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
	
	}
	
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
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
	public void postStartup(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		super.postStartup(enterprise, progressMonitor);
	}
	
	@Override
	public String getSystemName()
	{
		return EnterpriseSystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for handling enterprises";
	}
	
}
