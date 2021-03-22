package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.client.services.ISystemsService;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;

import static com.guicedee.activitymaster.client.services.IResourceItemService.*;


public class ResourceItemSystem
		extends ActivityMasterDefaultSystem<ResourceItemSystem>
		implements IActivityMasterSystem<ResourceItemSystem>
{
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public ISystems<?,?> registerSystem(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?, ?> iSystems = systemsService.get()
		                                        .create(enterprise, getSystemName(), getSystemDescription());
		systemsService.get()
		              .registerNewSystem(enterprise, getSystem(enterprise));
		
		return iSystems;
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
	public String getSystemName()
	{
		return ResourceItemSystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for managing Resource Items";
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 10;
	}
}
