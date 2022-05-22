package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IClassificationService.*;


public class ClassificationsSystem
		extends ActivityMasterDefaultSystem<ClassificationsSystem>
		implements IActivityMasterSystem<ClassificationsSystem>
{
	@Inject
	private IClassificationService<?> service;

	@Inject
	private ISystemsService<?> systemsService;
	
	@Override
	public ISystems<?,?>  registerSystem(IEnterprise<?,?> enterprise)
	{
		return systemsService
		              .create(enterprise, getSystemName(), getSystemDescription());
	}
	
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);;
		//Create Root Enterprise Name
		service.create(enterprise.getName(),enterprise.getName(),
				EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, activityMasterSystem);
		
		logProgress("Classifications System", "Loaded Default Classifications...", 1);
		
		service.create(DefaultClassifications.HierarchyTypeClassification, activityMasterSystem);
		service.create(DefaultClassifications.HierarchyTypeClassification, activityMasterSystem, enterprise.getName());
		service.create(DefaultClassifications.NoClassification, activityMasterSystem, enterprise.getName());
		service.create(DefaultClassifications.DefaultClassification, activityMasterSystem, enterprise.getName());
		service.create(DefaultClassifications.Security, activityMasterSystem, enterprise.getName());
		
		logProgress("Classifications System", "Loading Security Classifications...", 1);
		
		service.create(SystemsClassifications.SystemIdentity, activityMasterSystem, DefaultClassifications.Security);
		service.create(InvolvedPartyClassifications.SecurityPassword, activityMasterSystem, DefaultClassifications.Security);
		service.create(InvolvedPartyClassifications.SecurityPasswordSalt, activityMasterSystem, DefaultClassifications.Security);

		service.create(EnterpriseClassifications.LastUpdateDate, activityMasterSystem, enterprise.getName());
		service.create(EnterpriseClassifications.UpdateClass, activityMasterSystem, enterprise.getName());
		service.create(EnterpriseClassifications.EnterpriseIdentity, activityMasterSystem, enterprise.getName());
	}

	@Override
	public int totalTasks()
	{
		return 2;
	}

	@Override
	public String getSystemName()
	{
		return ClassificationSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for handling classifications";
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 4;
	}
}
