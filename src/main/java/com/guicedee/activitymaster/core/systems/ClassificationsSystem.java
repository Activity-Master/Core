package com.guicedee.activitymaster.core.systems;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.implementations.ClassificationService;
import com.guicedee.activitymaster.core.implementations.SystemsService;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.IActivityMasterSystem;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.classifications.enterprise.EnterpriseClassifications;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.InvolvedPartyClassifications;
import com.guicedee.activitymaster.core.services.classifications.systems.SystemsClassifications;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.system.ActivityMasterDefaultSystem;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.guicedee.activitymaster.core.services.classifications.involvedparty.InvolvedPartyClassifications.*;

@Singleton
public class ClassificationsSystem
		extends ActivityMasterDefaultSystem<ClassificationsSystem>
		implements IActivityMasterSystem<ClassificationsSystem>
{
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class,
			timeout = 300)
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
		if (parent == null)
		{
			throw new ActivityMasterException("Hierarchy Parent is not working");
		}

		logProgress("Classifications System", "Loaded Global Classifications...", 2, progressMonitor);
	}

	@Override
	public int totalTasks()
	{
		return 50;
	}

	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		super.loadUpdates(enterprise, progressMonitor);
		ISystems<?> activityMasterSystem = GuiceContext.get(SystemsService.class)
		                                               .getActivityMaster(enterprise);
		UUID identityToken = GuiceContext.get(SystemsService.class)
		                                 .getSecurityIdentityToken(activityMasterSystem);
		ClassificationService service = GuiceContext.get(ClassificationService.class);

		//Create Root Enterprise Name
		IEnterpriseName<?> enterpriseName = GuiceContext.get(ActivityMasterConfiguration.class)
		                                                .getEnterpriseName();
		try
		{
			service.find(Languages, enterprise, identityToken);
		}
		catch (NoSuchElementException nre)
		{
			service.create(Languages, activityMasterSystem, Classifications.DefaultClassification);
			service.create(InvolvedPartyClassifications.ISO639_1, activityMasterSystem, Languages);
			service.create(InvolvedPartyClassifications.ISO639_2, activityMasterSystem, Languages);
			service.create(ISO6392EnglishName, activityMasterSystem, Languages);
			service.create(ISO6392FrenchName, activityMasterSystem, Languages);
			service.create(ISO6392GermanName, activityMasterSystem, Languages);
		}
	}

	@Override
	public String getSystemName()
	{
		return "Classifications System";
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
