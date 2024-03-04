package com.guicedee.activitymaster.fsdm.systems;

import com.entityassist.enumerations.ActiveFlag;
import com.guicedee.activitymaster.fsdm.ActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.*;


public class ActiveFlagSystem
		extends ActivityMasterDefaultSystem<ActiveFlagSystem>
		implements IActivityMasterSystem<ActiveFlagSystem>
{
	@Override
	public ISystems<?,?> registerSystem(IEnterprise<?,?> enterprise)
	{
		return null;
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		logProgress("Active Flag Service", "Loading Active Flags");
		for (ActiveFlag activeFlag : ActiveFlag.values())
		{
			((ActiveFlagService)com.guicedee.client.IGuiceContext.get(IActiveFlagService.class))
			            .create(enterprise, activeFlag.name(), activeFlag.getDescription());
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
	public String getSystemName()
	{
		return ActivateFlagSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for the active flag management";
	}
}
