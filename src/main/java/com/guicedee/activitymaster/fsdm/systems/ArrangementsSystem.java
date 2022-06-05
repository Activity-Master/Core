package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;

import static com.guicedee.activitymaster.fsdm.client.services.IArrangementsService.*;


public class ArrangementsSystem
		extends ActivityMasterDefaultSystem<ArrangementsSystem>
		implements IActivityMasterSystem<ArrangementsSystem>
{
	@Inject
	private ISystemsService<?> systemsService;
	
	@Override
	public ISystems<?,?> registerSystem(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(enterprise, getSystemName(), getSystemDescription());
		systemsService
		              .registerNewSystem(enterprise, getSystem(enterprise));
		return iSystems;
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
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
