package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.client.services.IResourceItemService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;


@Log4j2
public class ResourceItemSystem
		extends ActivityMasterDefaultSystem<ResourceItemSystem>
		implements IActivityMasterSystem<ResourceItemSystem>
{
	@Inject
	private ISystemsService<?> systemsService;

	@Override
	public ISystems<?,?> registerSystem(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(enterprise, getSystemName(), getSystemDescription())
		                                        .await().atMost(Duration.ofMinutes(1));
		systemsService
		              .registerNewSystem(enterprise, getSystem(enterprise))
		              .await().atMost(Duration.ofMinutes(1));

		return iSystems;
	}

	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		log.info("Starting createDefaults for Resource Item System");

		// Get the ActivityMaster system
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));

		// Currently no default resource items to create
		// This method is kept for consistency with other system classes
		// and to provide a structure for future additions

		log.info("Completed createDefaults for Resource Item System");
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
