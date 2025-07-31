package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemClassifications.*;
import static com.guicedee.activitymaster.fsdm.SystemsService.*;

@SortedUpdate(sortOrder = -500, taskCount = 3)
@Log4j2
public class ClassificationBaseSetup implements ISystemUpdate
{
	@Inject
	private IClassificationService<?> service;

	@Override
	public Uni<Boolean> update(Mutiny.Session session, IEnterprise<?,?> enterprise)
	{
		log.info("Starting sequential creation of classifications");
		log.info("Creating Languages and Hardware classifications sequentially");
		
		// Get the SystemsService and then the ActivityMaster system
		ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
		
		// Get the ActivityMaster system and then create classifications
		return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
			.chain(activityMasterSystem -> {
				// Create the Languages classification and its children
				return service.create(session, Languages, activityMasterSystem, DefaultClassifications.DefaultClassification)
					.chain(baseLanguage -> {
						log.info("Creating language classifications sequentially");
						
						// Chain language-related classification creation operations sequentially
						return service.create(session, InvolvedPartyClassifications.ISO639_1, activityMasterSystem, Languages)
							.chain(() -> service.create(session, InvolvedPartyClassifications.ISO639_2, activityMasterSystem, Languages))
							.chain(() -> service.create(session, ISO6392EnglishName, activityMasterSystem, Languages))
							.chain(() -> service.create(session, ISO6392FrenchName, activityMasterSystem, Languages))
							.chain(() -> service.create(session, ISO6392GermanName, activityMasterSystem, Languages))
							.onFailure().invoke(error -> log.error("Error creating language classifications: {}", error.getMessage(), error))
							.invoke(() -> logProgress("Classifications System", "Loading Base Languages...", 6));
					})
					// Chain to create the Hardware classification and its children
					.chain(() -> service.create(session, Hardware, activityMasterSystem))
					.chain(baseHardware -> {
						// First create the Computer classification
						return service.create(session, Computer, activityMasterSystem, Hardware);
					})
					.chain(computerClassification -> {
						log.info("Creating hardware and computer classifications sequentially");
						
						// Chain hardware-related classification creation operations sequentially
						return service.create(session, Scanner, activityMasterSystem, Hardware)
							.chain(() -> service.create(session, Printer, activityMasterSystem, Hardware))
							.chain(() -> service.create(session, Phone, activityMasterSystem, Hardware))
							// Chain computer-specific classification creation operations
							.chain(() -> service.create(session, Desktop, activityMasterSystem, Computer))
							.chain(() -> service.create(session, Laptop, activityMasterSystem, Computer))
							.onFailure().invoke(error -> log.error("Error creating hardware and computer classifications: {}", error.getMessage(), error));
					})
					.onFailure().invoke(error -> log.error("Error creating classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Classifications System", "Loading Default Devices...", 7))
					.map(result -> true); // Return Boolean
			});
	}

}
