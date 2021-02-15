package com.guicedee.activitymaster.core.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.InvolvedPartyClassifications;
import com.guicedee.activitymaster.core.services.classifications.systems.SystemsClassifications;
import com.guicedee.activitymaster.core.services.concepts.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.system.*;

import static com.guicedee.activitymaster.core.SystemsService.*;
import static com.guicedee.activitymaster.core.services.system.IClassificationService.*;


public class ClassificationsSystem
		extends ActivityMasterDefaultSystem<ClassificationsSystem>
		implements IActivityMasterSystem<ClassificationsSystem>
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
		              .create(enterprise, getSystemName(), getSystemDescription());
	}
	
	
	@Override
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		//Create Root Enterprise Name
		service.create(enterprise.classificationName(),enterprise.classificationName(),
				EnterpriseClassificationDataConcepts.NoClassificationDataConceptName.classificationValue(), activityMasterSystem);
		
		logProgress("Classifications System", "Loaded Default Classifications...", 1, progressMonitor);
		
		service.create(Classifications.HierarchyTypeClassification, activityMasterSystem);
		service.create(Classifications.HierarchyTypeClassification, activityMasterSystem, enterprise.classificationName());
		service.create(Classifications.NoClassification, activityMasterSystem, enterprise.classificationName());
		service.create(Classifications.DefaultClassification, activityMasterSystem, enterprise.classificationName());
		service.create(Classifications.Security, activityMasterSystem, enterprise.classificationName());
		
		logProgress("Classifications System", "Loading Security Classifications...", 1, progressMonitor);
		
		service.create(SystemsClassifications.SystemIdentity, activityMasterSystem, Classifications.Security);
		service.create(InvolvedPartyClassifications.SecurityPassword, activityMasterSystem, Classifications.Security);
		service.create(InvolvedPartyClassifications.SecurityPasswordSalt, activityMasterSystem, Classifications.Security);

		service.create(EnterpriseClassifications.LastUpdateDate, activityMasterSystem, enterprise.classificationName());
		service.create(EnterpriseClassifications.UpdateClass, activityMasterSystem, enterprise.classificationName());
		service.create(EnterpriseClassifications.EnterpriseIdentity, activityMasterSystem, enterprise.classificationName());
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
