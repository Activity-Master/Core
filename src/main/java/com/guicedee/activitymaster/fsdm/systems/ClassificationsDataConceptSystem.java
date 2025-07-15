package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ClassificationsDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.ReactiveTransactionUtil;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;


@Log4j2
public class ClassificationsDataConceptSystem
		extends ActivityMasterDefaultSystem<ClassificationsDataConceptSystem>
		implements IActivityMasterSystem<ClassificationsDataConceptSystem>
{
	@Inject
	private ISystemsService<?> systemsService;

	@Inject
	private ClassificationsDataConceptService service;

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

	@SuppressWarnings("Duplicates")
	@Override
	public void createDefaults(IEnterprise<?,?> enterprise)
	{
		logProgress("Classification Data Concept System", "Checking/Creating Base Concepts");

		// Note: This method is called synchronously, but we're using reactive programming internally
		// We'll use await().atMost() to block until all operations are completed

		// First, create the default concepts
		// These are foundational and should be created before other concepts
		Uni<Void> defaultConceptsChain = createDefaultConcepts(enterprise);

		// Wait for default concepts to complete before proceeding
		defaultConceptsChain.await().atMost(Duration.ofMinutes(2));

		// Now create all other concepts in parallel
		log.info("Starting parallel creation of classification data concepts");

		// Create all the Uni<Void> instances for concept creation
		List<Uni<Void>> operations = new ArrayList<>();
		operations.add(createInvolvedPartyConcepts(enterprise));
		operations.add(createProductConcepts(enterprise));
		operations.add(createResourceItemConcepts(enterprise));
		operations.add(createRulesConcepts(enterprise));
		operations.add(createActiveFlagConcepts(enterprise));
		operations.add(createGeographyConcepts(enterprise));
		operations.add(createAddressConcepts(enterprise));
		operations.add(createArrangementConcepts(enterprise));
		operations.add(createClassificationsConcepts(enterprise));
		operations.add(createEventsConcepts(enterprise));

		log.info("Running {} concept creation operations in parallel", operations.size());

		// Run all operations in parallel
		Uni<Void> parallelChain = Uni.combine().all().unis(operations)
			.discardItems()
			.onFailure().invoke(error -> log.error("Error in parallel concept creation operations: {}", error.getMessage(), error));

		// Wait for all operations to complete
		parallelChain.await().atMost(Duration.ofMinutes(5));

		log.info("Completed creation of classification data concepts");
	}

	@Override
	public void postStartup(IEnterprise<?,?> enterprise)
	{
		log.info("Starting reactive postStartup for Classification Data Concept System");

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
			result -> log.info("Classification Data Concept System postStartup completed successfully"),
			error -> log.error("Error in Classification Data Concept System postStartup: {}", error.getMessage(), error)
		);
	}

	private Uni<Void> createDefaultConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Base Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName, "Any general classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, "No classification is applicable", activityMasterSystem));
				//security
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityToken, "A security token identifies an entity possible to perform events on the system with", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityTokenXClassification, "Security Token Quick-Find Entries", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, "Security Token Hierarchy", activityMasterSystem));
				//Enterprise
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.Enterprise, "An enterprise is the top level", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EnterpriseXClassification, "Classifications for an enterprise", activityMasterSystem));
				//System
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.Systems, "Systems are sub-applications that provide functions and features to an enterprise", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.SystemXClassification, "A set of system classifications", activityMasterSystem));

				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.YesNo, "Designations for boolean entry fields", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.YesNoXClassification, "Different classifications for boolean fields", activityMasterSystem));

				log.info("Running {} default concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating default concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}


	private Uni<Void> createInvolvedPartyConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Involved Party Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedParty, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyIdentificationType, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyNameType, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyNonOrganic, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyOrganic, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyOrganicType, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyType, "Standard Table Based Classification", activityMasterSystem));

				operations.add(service.createDataConcept(InvolvedPartyXAddress, "All involved party identification types", activityMasterSystem));
				operations.add(service.createDataConcept(InvolvedPartyXClassification, "All involved party custom classifications", activityMasterSystem));
				operations.add(service.createDataConcept(InvolvedPartyXInvolvedParty, "Involved Party Hierarchy", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyNameType, "Involved Party Name Types", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyType, "Involved Party Types", activityMasterSystem));

				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyIdentificationType,
						"All classifications for identification type and involved party relationships", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXProduct, "All classifications for involved party and product relationships", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXRules, "All classifications for involved party and product relationships", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.InvolvedPartyXResourceItem, "All classifications for involved party and resource item relationships",
						activityMasterSystem));

				log.info("Running {} involved party concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating involved party concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}


	private Uni<Void> createProductConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Product Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.Product, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ProductType, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXProductType, "Product Types", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXProduct, "Product Hierarchy", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXResourceItem, "All product and resource item relationship classifications", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ProductXClassification, "All product classifications", activityMasterSystem));

				log.info("Running {} product concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating product concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}

	private Uni<Void> createResourceItemConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Resource Item Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItem, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemData, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemType, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemXResourceItemType, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemDataXClassification, "Resource Item Data Classifications", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ResourceItemXClassification, "all resource item classifications", activityMasterSystem));

				log.info("Running {} resource item concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating resource item concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}

	private Uni<Void> createRulesConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Rules Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(Rules, "Calculated rules", activityMasterSystem));
				operations.add(service.createDataConcept(RulesType, "Calculated rules types", activityMasterSystem));
				operations.add(service.createDataConcept(RulesTypeXClassification, "Rule Types Calculations", activityMasterSystem));
				operations.add(service.createDataConcept(RulesXProduct, "Rule Types Calculations", activityMasterSystem));
				operations.add(service.createDataConcept(RulesXResourceItem, "Rule Types Calculations", activityMasterSystem));

				log.info("Running {} rules concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating rules concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}


	private Uni<Void> createActiveFlagConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Active Flag Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ActiveFlag, "An Active Flag", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ActiveFlagXClassification, "Any active flag classifications", activityMasterSystem));

				log.info("Running {} active flag concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating active flag concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}


	private Uni<Void> createGeographyConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Geography Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.Geography, "Specific to a geography item", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXClassification, "All Geography Classifications", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXGeography, "All Geography Relationships", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.GeographyXResourceItem, "All geography resource items", activityMasterSystem));

				log.info("Running {} geography concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating geography concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}

	private Uni<Void> createAddressConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Address Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.Address, "Addresses are a physical location in a certain geography", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXGeography, "Any classifications for Address Geography groupings", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXResourceItem, "Any classification for resource items", activityMasterSystem));

				log.info("Running {} address concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating address concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}

	private Uni<Void> createArrangementConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Arrangement Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.Arrangement, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXInvolvedParty, "Any relationships for the arrangement and the involved party", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXClassification, "Any classifications for arrangements", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXArrangementType, "Any classifications for the type specified in the arrangement", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXArrangement, "Any classifications for the Relationships between Arrangement and X Arrangements", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.AddressXInvolvedParty, "Any Address Relationship with Involved Parties", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXProduct, "Arrangement classifications with product", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementXResourceItem, "Any Arrangement Relationship with a Resource Item", activityMasterSystem));
				operations.add(service.createDataConcept(ArrangementXRules, "Classifications for an arrangements link to a set of rules", activityMasterSystem));
				operations.add(service.createDataConcept(ArrangementXRulesTypes, "Classifications for an arrangements link to a set of rules", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ArrangementType, "A type of arrangement or agreement", activityMasterSystem));

				log.info("Running {} arrangement concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating arrangement concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}

	private Uni<Void> createClassificationsConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Classification Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConcept, "A designation of a table", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConceptXClassification, "All classifications for the data concepts", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem, "Resource Items for data concepts", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.Classification, "Standard Table Based Classification", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationXClassification, "The Classification Hierarchy", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.ClassificationXResourceItem, "Any classification type that exists for classification and resource items, usually icons etc", activityMasterSystem));

				log.info("Running {} classification concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating classification concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}

	private Uni<Void> createEventsConcepts(IEnterprise<?,?> enterprise)
	{
		logProgress("Data Concept System", "Event Concepts");

		return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
			.chain(activityMasterSystem -> {
				log.debug("Got system: {}", activityMasterSystem.getName());

				// Create a list of operations to run in parallel
				List<Uni<?>> operations = new ArrayList<>();

				// Add all concept creation operations to the list
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.Event, "An event is an audit of any change in the system, post installation", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EventType, "A specific type of event ", activityMasterSystem));
				operations.add(service.createDataConcept(EventXEventType, "Classifications for events", activityMasterSystem));

				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EventXClassification, "All classifications that relate to events", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EventXAddress, "Any classifications that relate to Events and Classifications, such as occured at", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EventXArrangement, "Any classifications for event and arrangements", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EventXInvolvedParty, "Any classifications for involved parties and events like bought, purchased, called, emailed", activityMasterSystem));

				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EventXGeography, "Geographical mappings for events", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EventXProduct, "Any Product that exists for X product classifications", activityMasterSystem));
				operations.add(service.createDataConcept(EnterpriseClassificationDataConcepts.EventXResourceItem, "Any event resource items, usually json or data received in some force", activityMasterSystem));
				operations.add(service.createDataConcept(EventXRules, "An events link to a set of rules", activityMasterSystem));

				log.info("Running {} event concept creation operations in parallel", operations.size());

				// Run all operations in parallel
				return Uni.combine().all().unis(operations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error creating event concepts: {}", error.getMessage(), error))
					.map(result -> null); // Convert to Void
			});
	}


	@Override
	public int totalTasks()
	{
		return 7;
	}

	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE + 3;
	}

	@Override
	public String getSystemName()
	{
		return ClassificationDataConceptSystemName;
	}

	@Override
	public String getSystemDescription()
	{
		return "The system for handling classification data concepts";
	}

}
