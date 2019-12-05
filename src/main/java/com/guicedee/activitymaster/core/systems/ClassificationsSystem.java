package com.guicedee.activitymaster.core.systems;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.InvolvedPartyClassifications;
import com.guicedee.activitymaster.core.services.classifications.systems.SystemsClassifications;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClassificationsSystem
		implements IActivityMasterSystem<ClassificationsSystem>
{
	//public static final String ClassificationHierarchyType = "Hierarchy";
	private static final Map<IEnterprise<?>, UUID> systemTokens = new HashMap<>();
	private static final Map<IEnterprise<?>, ISystems<?>> systemsMap = new HashMap<>();

	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,timeout = 300)
	public void createDefaults(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		ClassificationService service = GuiceContext.get(ClassificationService.class);

		//Create Root Enterprise Name
		IEnterpriseName<?> enterpriseName = GuiceContext.get(ActivityMasterConfiguration.class)
		                                                .getEnterpriseName();

		Classification rootClassification = (Classification) service.create(enterpriseName, activityMasterSystem);
		service.create(Classifications.HierarchyTypeClassification, activityMasterSystem);
		service.create(Classifications.HierarchyTypeClassification, activityMasterSystem, enterpriseName);
		service.create(Classifications.NoClassification, activityMasterSystem, enterpriseName);
		service.create(Classifications.DefaultClassification, activityMasterSystem, enterpriseName);
		service.create(Classifications.Security, activityMasterSystem, enterpriseName);

		service.create(SystemsClassifications.SystemIdentity, activityMasterSystem, Classifications.Security);
		service.create(InvolvedPartyClassifications.SecurityPassword, activityMasterSystem, Classifications.Security);
		service.create(InvolvedPartyClassifications.SecurityPasswordSalt, activityMasterSystem, Classifications.Security);

		service.create(EnterpriseClassifications.Version, activityMasterSystem, enterpriseName);
		service.create(EnterpriseClassifications.RequiresUpdate, activityMasterSystem, enterpriseName);
		service.create(EnterpriseClassifications.EnterpriseIdentity, activityMasterSystem, enterpriseName);

		//Checks
		List<Classification> output = rootClassification.findChildren();
		Classification parent = service.find(Classifications.NoClassification, enterprise)
		                               .findParent();

		if (output.isEmpty())
		{
			throw new ActivityMasterException("Hierarchy Children is not working");
		}
		if(parent == null)
			throw new ActivityMasterException("Hierarchy Parent is not working");

		logProgress("Classifications System", "Loaded Global Classifications...", 2, progressMonitor);
	}

	@Override
	public int totalTasks()
	{
		return 50;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 4;
	}

	@Override
	public void postUpdate(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		ISystems<?> newSystem = GuiceContext.get(SystemsService.class)
		                                .create(enterprise, "Classifications System", "The system for handling classifications", "");

		systemsMap.put(enterprise, newSystem);
		UUID securityToken = GuiceContext.get(ISystemsService.class)
		                                 .registerNewSystem(enterprise, newSystem);

		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                           .getActivityMaster(enterprise);

		GuiceContext.get(ClassificationService.class)
		            .create(EnterpriseClassifications.Version, activityMasterSystem);

		systemTokens.put(enterprise, securityToken);
	}

	public static Map<IEnterprise<?>, UUID> getSystemTokens()
	{
		return systemTokens;
	}

	public static Map<IEnterprise<?>, ISystems<?>> getSystemsMap()
	{
		return systemsMap;
	}
}
