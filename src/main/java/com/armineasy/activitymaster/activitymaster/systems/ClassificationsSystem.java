package com.armineasy.activitymaster.activitymaster.systems;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.ActivityMasterDB;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.implementations.ClassificationService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterSystem;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.EnterpriseClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.exceptions.ActivityMasterException;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.classification.Classifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.EnterpriseClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.InvolvedPartyClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.systems.SystemsClassifications.*;

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
		service.create(HierarchyTypeClassification, activityMasterSystem);
		service.create(HierarchyTypeClassification, activityMasterSystem,enterpriseName);
		service.create(NoClassification, activityMasterSystem,enterpriseName);
		service.create(DefaultClassification, activityMasterSystem,enterpriseName);
		service.create(Security, activityMasterSystem, enterpriseName);

		service.create(SystemIdentity, activityMasterSystem, Security);
		service.create(SecurityPassword, activityMasterSystem, Security);
		service.create(SecurityPasswordSalt, activityMasterSystem, Security);

		service.create(Version, activityMasterSystem,enterpriseName);
		service.create(RequiresUpdate, activityMasterSystem,enterpriseName);
		service.create(EnterpriseIdentity, activityMasterSystem,enterpriseName);

		//Checks
		List<Classification> output = rootClassification.findChildren();
		Classification parent = service.find(NoClassification, enterprise)
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
