package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
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

@Singleton
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
	public void postStartup(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		final String systemName = "Classifications System";
		final String systemDesc = "The system for handling classifications";
		Systems sys = (Systems) GuiceContext.get(SystemsService.class)
		                                    .findSystem(enterprise, systemName);
		UUID securityToken = null;
		if (sys == null)
		{
			sys = (Systems) GuiceContext.get(SystemsService.class)
			                                    .create(enterprise, systemName, systemDesc, systemName);

			securityToken = GuiceContext.get(ISystemsService.class)
			                            .registerNewSystem(enterprise, sys);
		}
		else
		{
			securityToken = GuiceContext.get(SystemsService.class)
			                            .getSecurityIdentityToken(sys);
		}
		systemTokens.put(enterprise, securityToken);
		systemsMap.put(enterprise, sys);
	}

	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{

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
