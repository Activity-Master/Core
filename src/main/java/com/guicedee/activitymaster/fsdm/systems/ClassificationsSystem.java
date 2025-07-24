package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

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
	public ISystems<?,?>  registerSystem(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		return getSystem(session, enterprise).invoke(system ->{
					systemsService.registerNewSystem(session, enterprise, system).await().atMost(Duration.ofMinutes(1));
		}).await().atMost(Duration.ofMinutes(1));
	}

	@Override
	public void createDefaults(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		// Get the ActivityMaster system
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(java.time.Duration.ofMinutes(1));

		// Create Root Enterprise Name - this is a foundational classification
		service.create(session, enterprise.getName(),
                        enterprise.getName(), EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, activityMasterSystem)
				.await().atMost(java.time.Duration.ofMinutes(1));

		logProgress("Classifications System", "Loaded Default Classifications...", 1);

		// Create base classifications sequentially as they are foundational
		// These don't have parent-child relationships so we can create them in parallel
		java.util.List<io.smallrye.mutiny.Uni<?>> baseClassifications = new java.util.ArrayList<>();
		baseClassifications.add(service.create(session, DefaultClassifications.HierarchyTypeClassification, activityMasterSystem));
		baseClassifications.add(service.create(session, DefaultClassifications.HierarchyTypeClassification, activityMasterSystem, enterprise.getName()));
		baseClassifications.add(service.create(session, DefaultClassifications.NoClassification, activityMasterSystem, enterprise.getName()));
		baseClassifications.add(service.create(session, DefaultClassifications.DefaultClassification, activityMasterSystem, enterprise.getName()));

		// Execute all base classifications in parallel and wait for completion
		io.smallrye.mutiny.Uni.combine().all().unis(baseClassifications)
		                     .discardItems()
		                     .await().atMost(java.time.Duration.ofMinutes(1));

		// Create Security classification - this is a parent for other classifications
		service.create(session, DefaultClassifications.Security, activityMasterSystem, enterprise.getName())
		      .await().atMost(java.time.Duration.ofMinutes(1));

		logProgress("Classifications System", "Loading Security Classifications...", 1);

		// Create security-related classifications in parallel since they all have the same parent
		// and the parent has already been created
		java.util.List<io.smallrye.mutiny.Uni<?>> securityClassifications = new java.util.ArrayList<>();
		securityClassifications.add(service.create(session, SystemsClassifications.SystemIdentity, activityMasterSystem, DefaultClassifications.Security));
		securityClassifications.add(service.create(session, InvolvedPartyClassifications.SecurityPassword, activityMasterSystem, DefaultClassifications.Security));
		securityClassifications.add(service.create(session, InvolvedPartyClassifications.SecurityPasswordSalt, activityMasterSystem, DefaultClassifications.Security));

		// Execute all security classifications in parallel and wait for completion
		io.smallrye.mutiny.Uni.combine().all().unis(securityClassifications)
		                     .discardItems()
		                     .await().atMost(java.time.Duration.ofMinutes(1));

		// Create enterprise-related classifications in parallel since they all have the same parent
		// and the parent has already been created
		java.util.List<io.smallrye.mutiny.Uni<?>> enterpriseClassifications = new java.util.ArrayList<>();
		enterpriseClassifications.add(service.create(session, EnterpriseClassifications.LastUpdateDate, activityMasterSystem, enterprise.getName()));
		enterpriseClassifications.add(service.create(session, EnterpriseClassifications.UpdateClass, activityMasterSystem, enterprise.getName()));
		enterpriseClassifications.add(service.create(session, EnterpriseClassifications.EnterpriseIdentity, activityMasterSystem, enterprise.getName()));

		// Execute all enterprise classifications in parallel and wait for completion
		io.smallrye.mutiny.Uni.combine().all().unis(enterpriseClassifications)
		                     .discardItems()
		                     .await().atMost(java.time.Duration.ofMinutes(1));
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
