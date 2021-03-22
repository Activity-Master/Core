package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IClassificationService;
import com.guicedee.activitymaster.client.services.ISystemsService;
import com.guicedee.activitymaster.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.client.services.systems.IActivityMasterSystem;

import static com.guicedee.activitymaster.client.services.IAddressService.*;
import static com.guicedee.activitymaster.core.SystemsService.*;


public class AddressSystem
		extends ActivityMasterDefaultSystem<AddressSystem>
		implements IActivityMasterSystem<AddressSystem>
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public ISystems<?,?>  registerSystem(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?, ?> iSystems = systemsService.get()
		                                        .create(enterprise, AddressSystemName, "The system for the address management");
		systemsService.get()
		              .registerNewSystem(enterprise, getSystem(enterprise));
		return iSystems;
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Address System", "Starting Address Checks", progressMonitor);
	}


	@Override
	public int totalTasks()
	{
		return 15;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 7;
	}

	@Override
	public String getSystemName()
	{
		return AddressSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for address management";
	}
}
