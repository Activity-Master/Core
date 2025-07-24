package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.client.services.IRulesService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;


@Log4j2
public class RulesSystem
		extends ActivityMasterDefaultSystem<RulesSystem>
		implements IActivityMasterSystem<RulesSystem>
{
	@Inject
	private ISystemsService<?> systemsService;

	@Inject
	private IClassificationService<?> classificationService;

	@Override
	public ISystems<?,?>  registerSystem(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		                                        .create(session, enterprise, getSystemName(), getSystemDescription())
		                                        .await().atMost(Duration.ofMinutes(1));

		getSystem(session, enterprise).chain(system ->{
					return systemsService
								   .registerNewSystem(session, enterprise, system);
		}).await().atMost(Duration.ofMinutes(1));

		return iSystems;
	}


	@Override
	public void createDefaults(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Starting createDefaults for Rules System");

		// Get the ActivityMaster system
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
		                                                           .await().atMost(Duration.ofMinutes(1));

		logProgress("Rules System", "Creating rule classifications...");

		// Create rule-related classifications in parallel
		List<Uni<?>> ruleClassifications = new ArrayList<>();

		// Add operations to create rule classifications
		ruleClassifications.add(classificationService.create(session, "Rules", "The main rules concept", activityMasterSystem));
		ruleClassifications.add(classificationService.create(session, "RulesType", "The concept for rule types", activityMasterSystem));

		// Execute all rule classifications in parallel and wait for completion
		Uni.combine().all().unis(ruleClassifications)
		             .discardItems()
		             .onFailure().invoke(error -> 
		                 log.error("Error creating rule classifications: {}", error.getMessage(), error))
		             .await().atMost(Duration.ofMinutes(1));

		logProgress("Rules System", "Loaded Rules Classifications...", 4);
		log.info("Completed createDefaults for Rules System");
	}

	@Override
	public int totalTasks()
	{
		return 0;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 11;
	}

	@Override
	public String getSystemName()
	{
		return RulesSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for managing Rules";
	}
}
