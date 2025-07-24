package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ClassificationsDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.ActivityMasterSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService.ClassificationDataConceptSystemName;
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
    public ISystems<?, ?> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        ISystems<?, ?> iSystems = systemsService
                                          .create(session, enterprise, getSystemName(), getSystemDescription())
                                          .await()
                                          .atMost(Duration.ofMinutes(1))
                ;
        getSystem(session, enterprise).chain(system -> {
                    return systemsService
                                   .registerNewSystem(session, enterprise, system);
                })
                .await()
                .atMost(Duration.ofMinutes(1))
        ;
        return iSystems;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Classification Data Concept System", "Checking/Creating Base Concepts");

        // Note: This method is called synchronously, but we're using reactive programming internally
        // We'll use await().atMost() to block until all operations are completed

        // First, create the default concepts
        // These are foundational and should be created before other concepts
        Uni<Void> defaultConceptsChain = createDefaultConcepts(session, enterprise);

        // Wait for default concepts to complete before proceeding
        defaultConceptsChain.await()
                .atMost(Duration.ofMinutes(2));

        // Now create all other concepts in parallel
        log.info("Starting parallel creation of classification data concepts");

        // Create all the Uni<Void> instances for concept creation
        List<Uni<Void>> operations = new ArrayList<>();
        operations.add(createInvolvedPartyConcepts(session, enterprise));
        operations.add(createProductConcepts(session, enterprise));
        operations.add(createResourceItemConcepts(session, enterprise));
        operations.add(createRulesConcepts(session, enterprise));
        operations.add(createActiveFlagConcepts(session, enterprise));
        operations.add(createGeographyConcepts(session, enterprise));
        operations.add(createAddressConcepts(session, enterprise));
        operations.add(createArrangementConcepts(session, enterprise));
        operations.add(createClassificationsConcepts(session, enterprise));
        operations.add(createEventsConcepts(session, enterprise));

        log.info("Running {} concept creation operations in parallel", operations.size());

        // Run all operations in parallel
        Uni<Void> parallelChain = Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error in parallel concept creation operations: {}", error.getMessage(), error))
                ;

        // Wait for all operations to complete
        parallelChain.await()
                .atMost(Duration.ofMinutes(5));

        log.info("Completed creation of classification data concepts");
    }

    @Override
    public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("Starting reactive postStartup for Classification Data Concept System");
        // Create a reactive chain for the postStartup operations
        // Get the system
        return systemsService.findSystem(session, enterprise, getSystemName())
                       .onItem()
                       .ifNull()
                       .failWith(() -> new RuntimeException("System not found: " + getSystemName()))
                       .chain(system -> {
                           log.debug("Found system: {}", system.getName());
                           // Get the security token
                           return systemsService.getSecurityIdentityToken(session, system)
                                          .onItem()
                                          .ifNull()
                                          .failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
                                          .map(token -> {
                                              log.debug("Found security token for system: {}", system.getName());
                                              return null; // Return Void
                                          });
                       })
                       .replaceWith(Uni.createFrom()
                                            .voidItem());
    }

    private Uni<Void> createDefaultConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Base Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName, "Any general classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, "No classification is applicable", activityMasterSystem));
                           //security
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityToken, "A security token identifies an entity possible to perform events on the system with", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityTokenXClassification, "Security Token Quick-Find Entries", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, "Security Token Hierarchy", activityMasterSystem));
                           //Enterprise
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.Enterprise, "An enterprise is the top level", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EnterpriseXClassification, "Classifications for an enterprise", activityMasterSystem));
                           //System
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.Systems, "Systems are sub-applications that provide functions and features to an enterprise", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.SystemXClassification, "A set of system classifications", activityMasterSystem));

                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.YesNo, "Designations for boolean entry fields", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.YesNoXClassification, "Different classifications for boolean fields", activityMasterSystem));

                           log.info("Running {} default concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating default concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }


    private Uni<Void> createInvolvedPartyConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Involved Party Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedParty, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyIdentificationType, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyNameType, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyNonOrganic, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyOrganic, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyOrganicType, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyType, "Standard Table Based Classification", activityMasterSystem));

                           operations.add(service.createDataConcept(session, InvolvedPartyXAddress, "All involved party identification types", activityMasterSystem));
                           operations.add(service.createDataConcept(session, InvolvedPartyXClassification, "All involved party custom classifications", activityMasterSystem));
                           operations.add(service.createDataConcept(session, InvolvedPartyXInvolvedParty, "Involved Party Hierarchy", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyNameType, "Involved Party Name Types", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyType, "Involved Party Types", activityMasterSystem));

                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyIdentificationType,
                                   "All classifications for identification type and involved party relationships", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXProduct, "All classifications for involved party and product relationships", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXRules, "All classifications for involved party and product relationships", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXResourceItem, "All classifications for involved party and resource item relationships",
                                   activityMasterSystem));

                           log.info("Running {} involved party concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating involved party concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }


    private Uni<Void> createProductConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Product Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.Product, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductType, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXProductType, "Product Types", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXProduct, "Product Hierarchy", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXResourceItem, "All product and resource item relationship classifications", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXClassification, "All product classifications", activityMasterSystem));

                           log.info("Running {} product concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating product concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }

    private Uni<Void> createResourceItemConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Resource Item Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItem, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemData, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemType, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemXResourceItemType, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemDataXClassification, "Resource Item Data Classifications", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemXClassification, "all resource item classifications", activityMasterSystem));

                           log.info("Running {} resource item concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating resource item concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }

    private Uni<Void> createRulesConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Rules Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, Rules, "Calculated rules", activityMasterSystem));
                           operations.add(service.createDataConcept(session, RulesType, "Calculated rules types", activityMasterSystem));
                           operations.add(service.createDataConcept(session, RulesTypeXClassification, "Rule Types Calculations", activityMasterSystem));
                           operations.add(service.createDataConcept(session, RulesXProduct, "Rule Types Calculations", activityMasterSystem));
                           operations.add(service.createDataConcept(session, RulesXResourceItem, "Rule Types Calculations", activityMasterSystem));

                           log.info("Running {} rules concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating rules concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }


    private Uni<Void> createActiveFlagConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Active Flag Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ActiveFlag, "An Active Flag", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ActiveFlagXClassification, "Any active flag classifications", activityMasterSystem));

                           log.info("Running {} active flag concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating active flag concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }


    private Uni<Void> createGeographyConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Geography Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.Geography, "Specific to a geography item", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXClassification, "All Geography Classifications", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXGeography, "All Geography Relationships", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXResourceItem, "All geography resource items", activityMasterSystem));

                           log.info("Running {} geography concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating geography concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }

    private Uni<Void> createAddressConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Address Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.Address, "Addresses are a physical location in a certain geography", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXGeography, "Any classifications for Address Geography groupings", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXResourceItem, "Any classification for resource items", activityMasterSystem));

                           log.info("Running {} address concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating address concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }

    private Uni<Void> createArrangementConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Arrangement Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.Arrangement, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXInvolvedParty, "Any relationships for the arrangement and the involved party", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXClassification, "Any classifications for arrangements", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXArrangementType, "Any classifications for the type specified in the arrangement", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXArrangement, "Any classifications for the Relationships between Arrangement and X Arrangements", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXInvolvedParty, "Any Address Relationship with Involved Parties", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXProduct, "Arrangement classifications with product", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXResourceItem, "Any Arrangement Relationship with a Resource Item", activityMasterSystem));
                           operations.add(service.createDataConcept(session, ArrangementXRules, "Classifications for an arrangements link to a set of rules", activityMasterSystem));
                           operations.add(service.createDataConcept(session, ArrangementXRulesTypes, "Classifications for an arrangements link to a set of rules", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementType, "A type of arrangement or agreement", activityMasterSystem));

                           log.info("Running {} arrangement concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating arrangement concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }

    private Uni<Void> createClassificationsConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Classification Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConcept, "A designation of a table", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConceptXClassification, "All classifications for the data concepts", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem, "Resource Items for data concepts", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.Classification, "Standard Table Based Classification", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationXClassification, "The Classification Hierarchy", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationXResourceItem, "Any classification type that exists for classification and resource items, usually icons etc", activityMasterSystem));

                           log.info("Running {} classification concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating classification concepts: {}", error.getMessage(), error))
                                          .map(result -> null); // Convert to Void
                       });
    }

    private Uni<Void> createEventsConcepts(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        logProgress("Data Concept System", "Event Concepts");

        return IActivityMasterService.getISystem(ActivityMasterSystemName, enterprise)
                       .chain(activityMasterSystem -> {
                           log.debug("Got system: {}", activityMasterSystem.getName());

                           // Create a list of operations to run in parallel
                           List<Uni<?>> operations = new ArrayList<>();

                           // Add all concept creation operations to the list
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.Event, "An event is an audit of any change in the system, post installation", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventType, "A specific type of event ", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EventXEventType, "Classifications for events", activityMasterSystem));

                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXClassification, "All classifications that relate to events", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXAddress, "Any classifications that relate to Events and Classifications, such as occured at", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXArrangement, "Any classifications for event and arrangements", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXInvolvedParty, "Any classifications for involved parties and events like bought, purchased, called, emailed", activityMasterSystem));

                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXGeography, "Geographical mappings for events", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXProduct, "Any Product that exists for X product classifications", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXResourceItem, "Any event resource items, usually json or data received in some force", activityMasterSystem));
                           operations.add(service.createDataConcept(session, EventXRules, "An events link to a set of rules", activityMasterSystem));

                           log.info("Running {} event concept creation operations in parallel", operations.size());

                           // Run all operations in parallel
                           return Uni.combine()
                                          .all()
                                          .unis(operations)
                                          .discardItems()
                                          .onFailure()
                                          .invoke(error -> log.error("Error creating event concepts: {}", error.getMessage(), error))
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
