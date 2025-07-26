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

  @Inject
  private Mutiny.SessionFactory sessionFactory;

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
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Classification Data Concept System", "Checking/Creating Base Concepts");

    // First, create the default concepts
    // These are foundational and should be created before other concepts
    return createDefaultConcepts(enterprise)
               .flatMap(v -> {
                 log.info("📋 Starting sequential creation of classification data concepts");
                 return createInvolvedPartyConcepts(enterprise)
                            .flatMap(x -> createProductConcepts(enterprise))
                            .flatMap(x -> createResourceItemConcepts(enterprise))
                            .flatMap(x -> createRulesConcepts(enterprise))
                            .flatMap(x -> createActiveFlagConcepts(enterprise))
                            .flatMap(x -> createGeographyConcepts(enterprise))
                            .flatMap(x -> createAddressConcepts(enterprise))
                            .flatMap(x -> createArrangementConcepts(enterprise))
                            .flatMap(x -> createClassificationsConcepts(enterprise))
                            .flatMap(x -> createEventsConcepts(enterprise))
                            .invoke(() -> log.info("🎉 Completed creation of all classification data concepts"));
               });
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

  private Uni<Void> createDefaultConcepts(IEnterprise<?, ?> enterprise) {
  logProgress("Data Concept System", "Base Concepts");
  log.info("Creating default concepts in a new session and transaction");

  return sessionFactory.withTransaction((session, tx) ->
    systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
      .flatMap(system -> {
        log.debug("Got system: {}", system.getName());

        return service.createDataConcept(session, EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName,
                  "Any general classification", system)
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.NoClassificationDataConceptName,
                  "No classification is applicable", system))
          // security
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityToken,
                  "A security token identifies an entity possible to perform events on the system with", system))
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityTokenXClassification,
                  "Security Token Quick-Find Entries", system))
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken,
                  "Security Token Hierarchy", system))
          // enterprise
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.Enterprise,
                  "An enterprise is the top level", system))
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EnterpriseXClassification,
                  "Classifications for an enterprise", system))
          // system
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.Systems,
                  "Systems are sub-applications that provide functions and features to an enterprise", system))
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.SystemXClassification,
                  "A set of system classifications", system))
          // boolean
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.YesNo,
                  "Designations for boolean entry fields", system))
          .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.YesNoXClassification,
                  "Different classifications for boolean fields", system))
          .invoke(() -> log.info("✅ Successfully created all default base concepts"));
      })
  ).replaceWithVoid();
}



  private Uni<Void> createInvolvedPartyConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Involved Party Concepts");
    log.info("Creating involved party concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedParty, 
                    "Standard Table Based Classification", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyIdentificationType, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyNameType, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyNonOrganic, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyOrganic, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyOrganicType, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyType, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, InvolvedPartyXAddress, 
                    "All involved party identification types", system))
            .flatMap(v -> service.createDataConcept(session, InvolvedPartyXClassification, 
                    "All involved party custom classifications", system))
            .flatMap(v -> service.createDataConcept(session, InvolvedPartyXInvolvedParty, 
                    "Involved Party Hierarchy", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyNameType, 
                    "Involved Party Name Types", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyType, 
                    "Involved Party Types", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyIdentificationType,
                    "All classifications for identification type and involved party relationships", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXProduct, 
                    "All classifications for involved party and product relationships", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXRules, 
                    "All classifications for involved party and product relationships", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXResourceItem, 
                    "All classifications for involved party and resource item relationships", system))
            .invoke(() -> log.info("✅ Successfully created all involved party concepts"));
        })
    ).replaceWithVoid();
  }


  private Uni<Void> createProductConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Product Concepts");
    log.info("Creating product concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Product, 
                    "Standard Table Based Classification", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductType, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXProductType, 
                    "Product Types", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXProduct, 
                    "Product Hierarchy", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXResourceItem, 
                    "All product and resource item relationship classifications", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXClassification, 
                    "All product classifications", system))
            .invoke(() -> log.info("✅ Successfully created all product concepts"));
        })
    ).replaceWithVoid();
  }

  private Uni<Void> createResourceItemConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Resource Item Concepts");
    log.info("Creating resource item concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItem, 
                    "Standard Table Based Classification", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemData, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemType, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemXResourceItemType, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemDataXClassification, 
                    "Resource Item Data Classifications", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemXClassification, 
                    "all resource item classifications", system))
            .invoke(() -> log.info("✅ Successfully created all resource item concepts"));
        })
    ).replaceWithVoid();
  }

  private Uni<Void> createRulesConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Rules Concepts");
    log.info("Creating rules concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, Rules, 
                    "Calculated rules", system)
            .flatMap(v -> service.createDataConcept(session, RulesType, 
                    "Calculated rules types", system))
            .flatMap(v -> service.createDataConcept(session, RulesTypeXClassification, 
                    "Rule Types Calculations", system))
            .flatMap(v -> service.createDataConcept(session, RulesXProduct, 
                    "Rule Types Calculations", system))
            .flatMap(v -> service.createDataConcept(session, RulesXResourceItem, 
                    "Rule Types Calculations", system))
            .invoke(() -> log.info("✅ Successfully created all rules concepts"));
        })
    ).replaceWithVoid();
  }


  private Uni<Void> createActiveFlagConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Active Flag Concepts");
    log.info("Creating active flag concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ActiveFlag, 
                    "An Active Flag", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ActiveFlagXClassification, 
                    "Any active flag classifications", system))
            .invoke(() -> log.info("✅ Successfully created all active flag concepts"));
        })
    ).replaceWithVoid();
  }


  private Uni<Void> createGeographyConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Geography Concepts");
    log.info("Creating geography concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Geography, 
                    "Specific to a geography item", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXClassification, 
                    "All Geography Classifications", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXGeography, 
                    "All Geography Relationships", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXResourceItem, 
                    "All geography resource items", system))
            .invoke(() -> log.info("✅ Successfully created all geography concepts"));
        })
    ).replaceWithVoid();
  }

  private Uni<Void> createAddressConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Address Concepts");
    log.info("Creating address concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Address, 
                    "Addresses are a physical location in a certain geography", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXGeography, 
                    "Any classifications for Address Geography groupings", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXResourceItem, 
                    "Any classification for resource items", system))
            .invoke(() -> log.info("✅ Successfully created all address concepts"));
        })
    ).replaceWithVoid();
  }

  private Uni<Void> createArrangementConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Arrangement Concepts");
    log.info("Creating arrangement concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Arrangement, 
                    "Standard Table Based Classification", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXInvolvedParty, 
                    "Any relationships for the arrangement and the involved party", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXClassification, 
                    "Any classifications for arrangements", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXArrangementType, 
                    "Any classifications for the type specified in the arrangement", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXArrangement, 
                    "Any classifications for the Relationships between Arrangement and X Arrangements", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXInvolvedParty, 
                    "Any Address Relationship with Involved Parties", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXProduct, 
                    "Arrangement classifications with product", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXResourceItem, 
                    "Any Arrangement Relationship with a Resource Item", system))
            .flatMap(v -> service.createDataConcept(session, ArrangementXRules, 
                    "Classifications for an arrangements link to a set of rules", system))
            .flatMap(v -> service.createDataConcept(session, ArrangementXRulesTypes, 
                    "Classifications for an arrangements link to a set of rules", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementType, 
                    "A type of arrangement or agreement", system))
            .invoke(() -> log.info("✅ Successfully created all arrangement concepts"));
        })
    ).replaceWithVoid();
  }

  private Uni<Void> createClassificationsConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Classification Concepts");
    log.info("Creating classification concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConcept, 
                    "A designation of a table", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConceptXClassification, 
                    "All classifications for the data concepts", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem, 
                    "Resource Items for data concepts", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.Classification, 
                    "Standard Table Based Classification", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationXClassification, 
                    "The Classification Hierarchy", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationXResourceItem, 
                    "Any classification type that exists for classification and resource items, usually icons etc", system))
            .invoke(() -> log.info("✅ Successfully created all classification concepts"));
        })
    ).replaceWithVoid();
  }

  private Uni<Void> createEventsConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Event Concepts");
    log.info("Creating event concepts in a new session and transaction");

    return sessionFactory.withTransaction((session, tx) ->
      systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .flatMap(system -> {
          log.debug("Got system: {}", system.getName());
          
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Event, 
                    "An event is an audit of any change in the system, post installation", system)
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventType, 
                    "A specific type of event ", system))
            .flatMap(v -> service.createDataConcept(session, EventXEventType, 
                    "Classifications for events", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXClassification, 
                    "All classifications that relate to events", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXAddress, 
                    "Any classifications that relate to Events and Classifications, such as occured at", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXArrangement, 
                    "Any classifications for event and arrangements", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXInvolvedParty, 
                    "Any classifications for involved parties and events like bought, purchased, called, emailed", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXGeography, 
                    "Geographical mappings for events", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXProduct, 
                    "Any Product that exists for X product classifications", system))
            .flatMap(v -> service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXResourceItem, 
                    "Any event resource items, usually json or data received in some force", system))
            .flatMap(v -> service.createDataConcept(session, EventXRules, 
                    "An events link to a set of rules", system))
            .invoke(() -> log.info("✅ Successfully created all event concepts"));
        })
    ).replaceWithVoid();
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
