package com.guicedee.activitymaster.core.systems;

import com.entityassist.enumerations.ActiveFlag;
import com.guicedee.activitymaster.client.services.IActiveFlagService;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.core.ActiveFlagService;
import com.guicedee.guicedinjection.GuiceContext;

import static com.guicedee.activitymaster.client.services.IActiveFlagService.*;


public class ActiveFlagSystem
		extends ActivityMasterDefaultSystem<ActiveFlagSystem>
		implements IActivityMasterSystem<ActiveFlagSystem>
{
	@Override
	public ISystems<?,?> registerSystem(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		return null;
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Active Flag Service", "Loading Active Flags", progressMonitor);
		for (ActiveFlag activeFlag : ActiveFlag.values())
		{
			((ActiveFlagService)GuiceContext.get(IActiveFlagService.class))
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
