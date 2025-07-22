package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyNameType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.*;


@Log4j2
public class InvolvedPartySystem
		extends ActivityMasterDefaultSystem<InvolvedPartySystem>
		implements IActivityMasterSystem<InvolvedPartySystem>
{
	@Inject
	private IInvolvedPartyService<?> service;

	@Inject
	private ISystemsService<?> systemsService;

	@Override
	public ISystems<?,?>  registerSystem(IEnterprise<?,?> enterprise)
	{
		ISystems<?, ?> iSystems = systemsService
		              .create(enterprise, getSystemName(), getSystemDescription())
		              .await().atMost(Duration.ofMinutes(1));
		systemsService
		              .registerNewSystem(enterprise, getSystem(enterprise))
		              .await().atMost(Duration.ofMinutes(1));
		return iSystems;
	}

	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		logProgress("Involved Party System", "Starting Checks for Required Values");

		// Note: This method is called synchronously, but we're using reactive programming internally
		// We'll use await().atMost() to block until all operations are completed

		// Create all three types in parallel
		log.info("Starting parallel creation of identification types, name types, and types");

		// Create the three Uni<Void> instances
		Uni<Void> identificationTypesChain = createIdentificationTypes(enterprise);
		Uni<Void> nameTypesChain = createNameTypes(enterprise);
		Uni<Void> typesChain = createTypes(enterprise);

		// Run all three operations in parallel
		Uni<Void> parallelChain = Uni.combine().all().unis(identificationTypesChain, nameTypesChain, typesChain)
			.discardItems()
			.onFailure().invoke(error -> log.error("Error in parallel operations: {}", error.getMessage(), error));

		// Wait for all operations to complete
		parallelChain.await().atMost(Duration.ofMinutes(5));

		log.info("Completed parallel creation of identification types, name types, and types");

		// Create default users
		createDefaultUsers(enterprise);
	}

	private Uni<Void> createIdentificationTypes(IEnterprise<?,?> enterprise)
	{
		log.debug("Creating identification types for enterprise: {}", enterprise.getName());

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(system -> {
				log.debug("Got system: {}", system.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all identification type creation operations to the list
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeDriversLicense, "Describes an Individuals Drivers Licence Number"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypePassportNumber, "Describes an Individuals Passport Number"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeTaxNumber, "Tax ID Number"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeVATNumber, "Describes an Organisation's VAT Registration number."));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeRegistrationNumber, "Describes an Organisation's Registration number."));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeIdentityNumber, "An Individual Green-Bard Coded ID Document Number"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeEmailAddress, "An individuals email address"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeCellPhoneNumber, "An individuals cell phone number"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeSocialSecurityNumber, "An individuals social security number"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeUserName, "An individuals username"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeUUID, "A given unique system identifier"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeSessionID, "A Given JavascriptSession ID"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeSystemID, "A unique system identifier granted to each each system on registration"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeEnterpriseCreatorRole, "An identifier marking the involved party as the creator of the enterprise"));
				operations.add(service.createIdentificationType(system, IdentificationTypes.IdentificationTypeUnassigned, "This involved party is unassigned and should be classified"));

				log.info("Running {} identification type creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating identification types: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Involved Party System", "Loaded Identification Types", 16))
					.map(result -> null); // Convert to Void
			});
	}

	private Uni<Void> createNameTypes(IEnterprise<?,?> enterprise)
	{
		log.debug("Creating name types for enterprise: {}", enterprise.getName());

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(system -> {
				log.debug("Got system: {}", system.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all name type creation operations to the list
				operations.add(service.createNameType(NameTypes.FirstNameType, "The first name of an individual or entity", system));
				operations.add(service.createNameType(NameTypes.FullNameType, "The full name of an individual or entity", system));
				operations.add(service.createNameType(NameTypes.PreferredNameType, "An Alias for the Involved Party", system));
				operations.add(service.createNameType(NameTypes.BirthNameType, "A Given Birth/Maiden Name for an Individual", system));
				operations.add(service.createNameType(NameTypes.LegalNameType, "The Legal Name associated with an Involved Party", system));
				operations.add(service.createNameType(NameTypes.CommonNameType, "The Common Name associated with an Individual", system));
				operations.add(service.createNameType(NameTypes.SalutationType, "The Salutation Associated with an Individual", system));
				operations.add(service.createNameType(NameTypes.MiddleNameType, "The Middle Name of an Individual", system));
				operations.add(service.createNameType(NameTypes.InitialsType, "The initials of an Individual", system));
				operations.add(service.createNameType(NameTypes.SurnameType, "The last name of an Individual", system));
				operations.add(service.createNameType(NameTypes.QualificationType, "The Qualification of an Individual", system));
				operations.add(service.createNameType(NameTypes.SuffixType, "The Suffix on an Individual's Name - e.g. \"Jnr, Snr\"", system));

				log.info("Running {} name type creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating name types: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Involved Party System", "Loaded Name Types", 12))
					.map(result -> null); // Convert to Void
			});
	}

	private Uni<Void> createTypes(IEnterprise<?,?> enterprise)
	{
		log.debug("Creating types for enterprise: {}", enterprise.getName());

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(system -> {
				log.debug("Got system: {}", system.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all type creation operations to the list
				operations.add(service.createType(system, IPTypes.TypeIndividual, "Defines any involved party as a physical person"));
				operations.add(service.createType(system, IPTypes.TypeOrganisation, "Defines any involved party as an organisation"));
				operations.add(service.createType(system, IPTypes.TypeSystem, "Defines any involved party as a physical system"));
				operations.add(service.createType(system, IPTypes.TypeDevice, "Defines any involved party as a physical system"));
				operations.add(service.createType(system, IPTypes.TypeApplication, "Defines any involved party as an application"));
				operations.add(service.createType(system, IPTypes.TypeAbstraction, "Represents an abstract entity such as Time"));
				operations.add(service.createType(system, IPTypes.TypeUnknown, "The type of Involved Party is unknown."));

				log.info("Running {} type creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating types: {}", error.getMessage(), error))
					.invoke(() -> logProgress("Involved Party System", "Loaded Types", 6))
					.map(result -> null); // Convert to Void
			});
	}

	private void createDefaultUsers(IEnterprise<?,?> enterprise)
	{
		// This method is currently empty in the original code
		// If needed, it can be updated to use reactive programming
	}

	@Override
	public Uni<Void> postStartup(IEnterprise<?,?> enterprise)
	{
		log.info("Starting reactive postStartup for Involved Party System");

		// Create a reactive chain for the postStartup operations
		Uni<Void> postStartupChain = ReactiveTransactionUtil.withTransaction(session -> {
			// Get the system
			return systemsService.findSystem(enterprise, getSystemName())
				.onItem().ifNull().failWith(() -> new RuntimeException("System not found: " + getSystemName()))
				.chain(system -> {
					log.debug("Found system: {}", system.getName());
					// Get the security token
					return systemsService.getSecurityIdentityToken(system)
						.onItem().ifNull().failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
						.map(token -> {
							log.debug("Found security token for system: {}", system.getName());
							return null; // Return Void
						});
				});
		});

		// Subscribe to the reactive chain
		postStartupChain.subscribe().with(
			result -> log.info("Involved Party System postStartup completed successfully"),
			error -> log.error("Error in Involved Party System postStartup: {}", error.getMessage(), error)
		);
		return postStartupChain.replaceWith(Uni.createFrom().voidItem());
	}

	@Override
	public int totalTasks()
	{
		return 5;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 5;
	}

	@Override
	public String getSystemName()
	{
		return InvolvedPartySystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for managing Involved Parties";
	}
}
