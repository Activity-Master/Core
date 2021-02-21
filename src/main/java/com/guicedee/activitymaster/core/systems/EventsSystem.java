package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.*;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.Date;

import static com.guicedee.activitymaster.core.services.system.IEventService.*;


public class EventsSystem
		extends ActivityMasterDefaultSystem<EventsSystem>
		implements IActivityMasterSystem<EventsSystem>
{
	@Inject
	private Provider<ISystemsService<?>> systemsService;
	
	@Override
	public void registerSystem(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		systemsService.get()
		              .create(enterprise, getSystemName(), getSystemDescription());
		systemsService.get()
		              .registerNewSystem(enterprise, getSystem(enterprise));
	}
	
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		logProgress("Loading Events", "Events creating default types", progressMonitor);
	
		logProgress("Loading Time", "Loading in Today", progressMonitor);
		
		GuiceContext.get(ITimeSystem.class)
		            .getDay(new Date());
		
		logProgress("Loading Time", "Creating Hours and Minutes", progressMonitor);
		
		GuiceContext.get(ITimeSystem.class)
		            .createTime();
	}
	
	@Override
	public int totalTasks()
	{
		return 0;
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 9;
	}
	
	@Override
	public String getSystemName()
	{
		return EventSystemName;
	}
	
	@Override
	public String getSystemDescription()
	{
		return "The system for managing events";
	}
	
}
