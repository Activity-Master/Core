package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.implementations.YesNoService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.jwebmp.guicedinjection.GuiceContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YesNoSystem
		implements IActivityMasterSystem<YesNoSystem>
{

	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();

	@Override
	public void createDefaults(IEnterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);
		YesNoService service = GuiceContext.get(YesNoService.class);
		service.create("No", "N", "false", "Off", "Inactive", "Out", activityMasterSystem);
		service.create("Yes", "Y", "true", "On", "Active", "In", activityMasterSystem);

		logProgress("Yes No Service", "Completed with YesNo Defaults", 1, progressMonitor);
	}

	@Override
	public int totalTasks()
	{
		return 1;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 5;
	}


	@Override
	public void postUpdate(IEnterprise enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		Systems newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "YesNo System",
		                                        "The system for managing Yes No Displays", "");
		UUID securityToken = GuiceContext.get(SystemsSystem.class)
		                                 .registerNewSystem(enterprise, newSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}
}
