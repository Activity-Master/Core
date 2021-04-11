package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.annotations.LogItemTypes;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import com.guicedee.guicedinjection.GuiceContext;

import java.util.Date;

import static com.guicedee.activitymaster.fsdm.client.services.IEventService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;


public class EventsSystem
		extends ActivityMasterDefaultSystem<EventsSystem>
		implements IActivityMasterSystem<EventsSystem>
{
	@Inject
	private ISystemsService<?> systemsService;
	
	@Inject
	private IEventService<?> eventService;
	
	@Inject
	private IResourceItemService<?> resourceItemServiceProvider;
	
	@Inject
	private IClassificationService<?> classificationServiceProvider;
	
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
		logProgress("Loading Events", "Events creating default types");
	
		logProgress("Loading Time", "Loading in Today");
		
		GuiceContext.get(ITimeSystem.class)
		            .getDay(new Date());
		
		IClassificationService<?> iClassificationService = classificationServiceProvider;
		
		logProgress("Loading Logging Types", "Creating Log Types");
		iClassificationService.create("LogItemTypes","The log item event registered types",Classification,getSystem(enterprise),getSystemToken(enterprise));
		
		iClassificationService.create("EventStatus","The status of the event",EventXClassification,getSystem(enterprise),getSystemToken(enterprise));
	
		
		for (LogItemTypes value : LogItemTypes.values())
		{
			iClassificationService.create(value,getSystem(enterprise),"LogItemTypes",getSystemToken(enterprise));
		}
		
		resourceItemServiceProvider.createType("LogItem", "An attached log item", getSystem(enterprise), getSystemToken(enterprise));
		
		logProgress("Loading Time", "Creating Hours and Minutes");
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
