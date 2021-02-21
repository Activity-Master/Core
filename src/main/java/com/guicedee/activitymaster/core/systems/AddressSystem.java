package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.*;

import static com.guicedee.activitymaster.core.SystemsService.*;
import static com.guicedee.activitymaster.core.services.system.IAddressService.*;


public class AddressSystem
		extends ActivityMasterDefaultSystem<AddressSystem>
		implements IActivityMasterSystem<AddressSystem>
{
	@Inject
	private IClassificationService<?> service;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?> activityMasterSystem;
	
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public void registerSystem(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		systemsService.get()
		              .create(enterprise, AddressSystemName, "The system for the address management");
		systemsService.get()
		              .registerNewSystem(enterprise, getSystem(enterprise));
	}
	
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
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
