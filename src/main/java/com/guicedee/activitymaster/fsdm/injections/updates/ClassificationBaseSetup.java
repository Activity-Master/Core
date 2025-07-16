package com.guicedee.activitymaster.fsdm.injections.updates;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications;
import com.guicedee.activitymaster.fsdm.client.services.systems.*;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

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

	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?,?> activityMasterSystem;

	@Override
	public Uni<Boolean> update(IEnterprise<?,?> enterprise)
	{
		log.info("Starting parallel creation of classifications");

		log.info("Creating Languages and Hardware classifications in parallel");

		// Create the Languages classification and its children
		Uni<Void> languagesUni = service.create(Languages, activityMasterSystem, DefaultClassifications.DefaultClassification)
			.chain(baseLanguage -> {
				// Create a list of operations to run in parallel for language classifications
				List<Uni<?>> languageOperations = new ArrayList<>();

				// Add all language-related classification creation operations to the list
				languageOperations.add(service.create(InvolvedPartyClassifications.ISO639_1, activityMasterSystem, Languages));
				languageOperations.add(service.create(InvolvedPartyClassifications.ISO639_2, activityMasterSystem, Languages));
				languageOperations.add(service.create(ISO6392EnglishName, activityMasterSystem, Languages));
				languageOperations.add(service.create(ISO6392FrenchName, activityMasterSystem, Languages));
				languageOperations.add(service.create(ISO6392GermanName, activityMasterSystem, Languages));

				log.info("Running {} language classification creation operations in parallel", languageOperations.size());

				// Run all language operations in parallel
				return Uni.combine().all().unis(languageOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating language classifications: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Classifications System", "Loading Base Languages...", 6))
					.map(result -> null); // Return Void
			});

		// Create the Hardware classification and its children (including Computer and its children)
		Uni<Void> hardwareUni = service.create(Hardware, activityMasterSystem)
			.chain(baseHardware -> {
				// First create the Computer classification
				return service.create(Computer, activityMasterSystem, Hardware)
					.chain(computerClassification -> {
						// Create a list of operations to run in parallel for all hardware-related classifications
						List<Uni<?>> hardwareOperations = new ArrayList<>();

						// Add hardware-related classification creation operations to the list
						hardwareOperations.add(service.create(Scanner, activityMasterSystem, Hardware));
						hardwareOperations.add(service.create(Printer, activityMasterSystem, Hardware));
						hardwareOperations.add(service.create(Phone, activityMasterSystem, Hardware));

						// Add computer-specific classification creation operations to the list
						hardwareOperations.add(service.create(Desktop, activityMasterSystem, Computer));
						hardwareOperations.add(service.create(Laptop, activityMasterSystem, Computer));

						log.info("Running {} hardware and computer classification creation operations in parallel", hardwareOperations.size());

						// Run all hardware and computer operations in parallel
						return Uni.combine().all().unis(hardwareOperations)
							.discardItems()
							.onFailure().invoke(error -> log.error("Error creating hardware and computer classifications: {}", error.getMessage(), error))
							.map(result -> null); // Return Void
					});
			});

		// Run all classification creations in parallel
		return Uni.combine().all().unis(languagesUni, hardwareUni)
			.discardItems()
			.onFailure().invoke(error -> log.error("Error creating classifications: {}", error.getMessage(), error))
			.invoke(() -> logProgress("Classifications System", "Loading Default Devices...", 7))
			.map(result -> true); // Return Boolean
	}

}
