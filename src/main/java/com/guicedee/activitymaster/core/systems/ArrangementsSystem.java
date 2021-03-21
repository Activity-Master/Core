package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.client.services.ISystemsService;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;

import static com.guicedee.activitymaster.client.services.IArrangementsService.*;


public class ArrangementsSystem
		extends ActivityMasterDefaultSystem<ArrangementsSystem>
		implements IActivityMasterSystem<ArrangementsSystem>
{
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public void registerSystem(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		systemsService.get()
		              .create(enterprise, getSystemName(), getSystemDescription());
		systemsService.get()
		              .registerNewSystem(enterprise, getSystem(enterprise));
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
	
	}
	
	@Override
	public int totalTasks()
	{
		return 4;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 8;
	}
	
	@Override
	public String getSystemName()
	{
		return ArrangementSystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for the arrangement management";
	}
}
