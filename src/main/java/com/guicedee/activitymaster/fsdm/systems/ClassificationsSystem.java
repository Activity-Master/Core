package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
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
	public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		// Get the ActivityMaster system
		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				// Create Root Enterprise Name - this is a foundational classification
				return service.create(session, enterprise.getName(),
						enterprise.getName(), EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, activityMasterSystem)
					.invoke(result -> logProgress("Classifications System", "Loaded Default Classifications...", 1))
					.chain(rootClassification -> {
						// Create base classifications in parallel
						java.util.List<Uni<?>> baseClassifications = new java.util.ArrayList<>();
						baseClassifications.add(service.create(session, DefaultClassifications.HierarchyTypeClassification, activityMasterSystem));
						baseClassifications.add(service.create(session, DefaultClassifications.HierarchyTypeClassification, activityMasterSystem, enterprise.getName()));
						baseClassifications.add(service.create(session, DefaultClassifications.NoClassification, activityMasterSystem, enterprise.getName()));
						baseClassifications.add(service.create(session, DefaultClassifications.DefaultClassification, activityMasterSystem, enterprise.getName()));

						// Execute all base classifications in parallel
						return Uni.combine().all().unis(baseClassifications)
							.discardItems()
							.chain(v -> {
								// Create Security classification - this is a parent for other classifications
								return service.create(session, DefaultClassifications.Security, activityMasterSystem, enterprise.getName())
									.invoke(result -> logProgress("Classifications System", "Loading Security Classifications...", 1))
									.chain(securityClassification -> {
										// Create security-related classifications in parallel
										java.util.List<Uni<?>> securityClassifications = new java.util.ArrayList<>();
										securityClassifications.add(service.create(session, SystemsClassifications.SystemIdentity, activityMasterSystem, DefaultClassifications.Security));
										securityClassifications.add(service.create(session, InvolvedPartyClassifications.SecurityPassword, activityMasterSystem, DefaultClassifications.Security));
										securityClassifications.add(service.create(session, InvolvedPartyClassifications.SecurityPasswordSalt, activityMasterSystem, DefaultClassifications.Security));

										// Execute all security classifications in parallel
										return Uni.combine().all().unis(securityClassifications)
											.discardItems()
											.chain(v2 -> {
												// Create enterprise-related classifications in parallel
												java.util.List<Uni<?>> enterpriseClassifications = new java.util.ArrayList<>();
												enterpriseClassifications.add(service.create(session, EnterpriseClassifications.LastUpdateDate, activityMasterSystem, enterprise.getName()));
												enterpriseClassifications.add(service.create(session, EnterpriseClassifications.UpdateClass, activityMasterSystem, enterprise.getName()));
												enterpriseClassifications.add(service.create(session, EnterpriseClassifications.EnterpriseIdentity, activityMasterSystem, enterprise.getName()));

												// Execute all enterprise classifications in parallel
												return Uni.combine().all().unis(enterpriseClassifications)
													.discardItems();
											});
									});
							});
					});
			})
			.replaceWith(Uni.createFrom().voidItem());
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
