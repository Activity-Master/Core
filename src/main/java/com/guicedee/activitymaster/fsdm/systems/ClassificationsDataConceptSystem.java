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
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Classifications Data Concept System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Classifications Data Concept System with session: {}", session.hashCode());

    return systemsService
        .create(session, enterprise, getSystemName(), getSystemDescription())
        .onItem()
        .invoke(system -> {
            log.debug("✅ Created Classifications Data Concept System: '{}' with session: {}", system.getName(), session.hashCode());
            
            // Chain the registerNewSystem call properly
            getSystem(session, enterprise)
                .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                .onItem()
                .invoke(() -> {
                    log.debug("✅ Registered system: {}", getSystemName());
                    log.info("🎉 Successfully registered Classifications Data Concept System for enterprise: '{}'", enterprise.getName());
                })
                .onFailure()
                .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                .chain(() -> Uni.createFrom().item(system)); // Chain back to return the original system
        })
        .onFailure()
        .invoke(error -> log.error("❌ Failed to create Classifications Data Concept System: '{}' with session {}: {}",
            getSystemName(), session.hashCode(), error.getMessage(), error));
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
    log.info("🚀 Starting reactive postStartup for Classification Data Concept System");
    log.debug("📋 Beginning postStartup operations for enterprise: '{}' with session: {}", 
             enterprise.getName(), session.hashCode());

    // Create a reactive chain for the postStartup operations
    // Get the system
    return systemsService.findSystem(session, enterprise, getSystemName())
               .onItem()
               .invoke(system -> log.debug("✅ Found system: '{}'", system.getName()))
               .onItem()
               .ifNull()
               .failWith(() -> new RuntimeException("System not found: " + getSystemName()))
               .onFailure()
               .invoke(error -> log.error("❌ Failed to find system: {}", error.getMessage(), error))
               .chain(system -> {
                 log.debug("🔍 Retrieving security token for system: '{}'", system.getName());
                 // Get the security token
                 return systemsService.getSecurityIdentityToken(session, system)
                            .onItem()
                            .invoke(token -> log.debug("🔑 Found security token for system: '{}'", system.getName()))
                            .onItem()
                            .ifNull()
                            .failWith(() -> new RuntimeException("Security token not found for system: " + system.getName()))
                            .onFailure()
                            .invoke(error -> log.error("❌ Failed to retrieve security token: {}", error.getMessage(), error))
                            .map(token -> {
                              log.debug("✅ Successfully completed postStartup for Classification Data Concept System");
                              return null; // Return Void
                            });
               })
               .replaceWith(Uni.createFrom().voidItem())
               .onItem()
               .invoke(() -> log.info("🎉 Classification Data Concept System postStartup completed successfully"))
               .onFailure()
               .invoke(error -> log.error("❌ Error in Classification Data Concept System postStartup: {}", error.getMessage(), error));
  }

  private Uni<Void> createDefaultConcepts(IEnterprise<?, ?> enterprise) {
  logProgress("Data Concept System", "Base Concepts");
  log.info("🚀 Creating default concepts for enterprise: '{}'", enterprise.getName());
  log.debug("📋 Starting with a new session and transaction");

  return sessionFactory.withTransaction((session, tx) -> {
    log.debug("📋 Created new transaction with session: {}", session.hashCode());
    
    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
      .onItem()
      .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                               system.getName(), session.hashCode()))
      .onFailure()
      .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                              error.getMessage(), error))
      .flatMap(system -> {
        log.debug("🔄 Creating base concepts sequentially with session: {}", session.hashCode());

        // Global Classifications
        return service.createDataConcept(session, EnterpriseClassificationDataConcepts.GlobalClassificationsDataConceptName,
                  "Any general classification", system)
          .onItem()
          .invoke(result -> log.debug("✅ Created GlobalClassificationsDataConcept"))
          .onFailure()
          .invoke(error -> log.error("❌ Failed to create GlobalClassificationsDataConcept: {}", 
                                  error.getMessage(), error))
          
          // No Classification
          .flatMap(v -> {
            log.debug("🔄 Creating NoClassificationDataConcept");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.NoClassificationDataConceptName,
                    "No classification is applicable", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created NoClassificationDataConcept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create NoClassificationDataConcept: {}", 
                                      error.getMessage(), error));
          })
          
          // Security concepts
          .flatMap(v -> {
            log.debug("🔄 Creating security concepts");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityToken,
                    "A security token identifies an entity possible to perform events on the system with", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created SecurityToken concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create SecurityToken concept: {}", 
                                      error.getMessage(), error));
          })
          .flatMap(v -> {
            log.debug("🔄 Creating SecurityTokenXClassification concept");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityTokenXClassification,
                    "Security Token Quick-Find Entries", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created SecurityTokenXClassification concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create SecurityTokenXClassification concept: {}", 
                                      error.getMessage(), error));
          })
          .flatMap(v -> {
            log.debug("🔄 Creating SecurityTokenXSecurityToken concept");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken,
                    "Security Token Hierarchy", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created SecurityTokenXSecurityToken concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create SecurityTokenXSecurityToken concept: {}", 
                                      error.getMessage(), error));
          })
          
          // Enterprise concepts
          .flatMap(v -> {
            log.debug("🔄 Creating enterprise concepts");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Enterprise,
                    "An enterprise is the top level", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created Enterprise concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create Enterprise concept: {}", 
                                      error.getMessage(), error));
          })
          .flatMap(v -> {
            log.debug("🔄 Creating EnterpriseXClassification concept");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EnterpriseXClassification,
                    "Classifications for an enterprise", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created EnterpriseXClassification concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create EnterpriseXClassification concept: {}", 
                                      error.getMessage(), error));
          })
          
          // System concepts
          .flatMap(v -> {
            log.debug("🔄 Creating system concepts");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Systems,
                    "Systems are sub-applications that provide functions and features to an enterprise", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created Systems concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create Systems concept: {}", 
                                      error.getMessage(), error));
          })
          .flatMap(v -> {
            log.debug("🔄 Creating SystemXClassification concept");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.SystemXClassification,
                    "A set of system classifications", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created SystemXClassification concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create SystemXClassification concept: {}", 
                                      error.getMessage(), error));
          })
          
          // Boolean concepts
          .flatMap(v -> {
            log.debug("🔄 Creating boolean concepts");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.YesNo,
                    "Designations for boolean entry fields", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created YesNo concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create YesNo concept: {}", 
                                      error.getMessage(), error));
          })
          .flatMap(v -> {
            log.debug("🔄 Creating YesNoXClassification concept");
            return service.createDataConcept(session, EnterpriseClassificationDataConcepts.YesNoXClassification,
                    "Different classifications for boolean fields", system)
              .onItem()
              .invoke(result -> log.debug("✅ Created YesNoXClassification concept"))
              .onFailure()
              .invoke(error -> log.error("❌ Failed to create YesNoXClassification concept: {}", 
                                      error.getMessage(), error));
          })
          .onItem()
          .invoke(() -> log.info("🎉 Successfully created all default base concepts"))
          .onFailure()
          .invoke(error -> log.error("❌ Error creating default concepts: {}", 
                                  error.getMessage(), error));
      });
  })
  .onItem()
  .invoke(() -> log.info("✅ Default concepts creation completed successfully"))
  .onFailure()
  .invoke(error -> log.error("❌ Error in default concepts creation: {}", 
                          error.getMessage(), error))
  .replaceWithVoid();
}



  private Uni<Void> createInvolvedPartyConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Involved Party Concepts");
    log.info("🚀 Creating involved party concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating involved party concepts sequentially with session: {}", session.hashCode());
          
          // Base involved party concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedParty, 
                    "Standard Table Based Classification", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created InvolvedParty concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create InvolvedParty concept: {}", 
                                    error.getMessage(), error))
            
            // Identification type
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyIdentificationType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyIdentificationType, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyIdentificationType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyIdentificationType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Name type
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyNameType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyNameType, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyNameType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyNameType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Non-organic
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyNonOrganic concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyNonOrganic, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyNonOrganic concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyNonOrganic concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Organic
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyOrganic concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyOrganic, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyOrganic concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyOrganic concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Organic type
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyOrganicType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyOrganicType, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyOrganicType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyOrganicType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Party type
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyType, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Relationship concepts
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXAddress concept");
              return service.createDataConcept(session, InvolvedPartyXAddress, 
                      "All involved party identification types", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXAddress concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXAddress concept: {}", 
                                        error.getMessage(), error));
            })
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXClassification concept");
              return service.createDataConcept(session, InvolvedPartyXClassification, 
                      "All involved party custom classifications", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXInvolvedParty concept");
              return service.createDataConcept(session, InvolvedPartyXInvolvedParty, 
                      "Involved Party Hierarchy", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXInvolvedParty concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXInvolvedParty concept: {}", 
                                        error.getMessage(), error));
            })
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXInvolvedPartyNameType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyNameType, 
                      "Involved Party Name Types", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXInvolvedPartyNameType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXInvolvedPartyNameType concept: {}", 
                                        error.getMessage(), error));
            })
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXInvolvedPartyType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyType, 
                      "Involved Party Types", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXInvolvedPartyType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXInvolvedPartyType concept: {}", 
                                        error.getMessage(), error));
            })
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXInvolvedPartyIdentificationType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXInvolvedPartyIdentificationType,
                      "All classifications for identification type and involved party relationships", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXInvolvedPartyIdentificationType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXInvolvedPartyIdentificationType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Cross-entity relationships
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXProduct concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXProduct, 
                      "All classifications for involved party and product relationships", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXProduct concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXProduct concept: {}", 
                                        error.getMessage(), error));
            })
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXRules concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXRules, 
                      "All classifications for involved party and product relationships", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXRules concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXRules concept: {}", 
                                        error.getMessage(), error));
            })
            .flatMap(v -> {
              log.debug("🔄 Creating InvolvedPartyXResourceItem concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.InvolvedPartyXResourceItem, 
                      "All classifications for involved party and resource item relationships", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created InvolvedPartyXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create InvolvedPartyXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all involved party concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating involved party concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Involved party concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in involved party concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }


  private Uni<Void> createProductConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Product Concepts");
    log.info("🚀 Creating product concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating product concepts sequentially with session: {}", session.hashCode());
          
          // Base product concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Product, 
                    "Standard Table Based Classification", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created Product concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create Product concept: {}", 
                                    error.getMessage(), error))
            
            // Product type
            .flatMap(v -> {
              log.debug("🔄 Creating ProductType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductType, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ProductType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ProductType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Product type relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ProductXProductType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXProductType, 
                      "Product Types", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ProductXProductType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ProductXProductType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Product hierarchy
            .flatMap(v -> {
              log.debug("🔄 Creating ProductXProduct concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXProduct, 
                      "Product Hierarchy", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ProductXProduct concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ProductXProduct concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Product resource item relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ProductXResourceItem concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXResourceItem, 
                      "All product and resource item relationship classifications", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ProductXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ProductXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Product classification relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ProductXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ProductXClassification, 
                      "All product classifications", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ProductXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ProductXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all product concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating product concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Product concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in product concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }

  private Uni<Void> createResourceItemConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Resource Item Concepts");
    log.info("🚀 Creating resource item concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating resource item concepts sequentially with session: {}", session.hashCode());
          
          // Base resource item concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItem, 
                    "Standard Table Based Classification", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created ResourceItem concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create ResourceItem concept: {}", 
                                    error.getMessage(), error))
            
            // Resource item data
            .flatMap(v -> {
              log.debug("🔄 Creating ResourceItemData concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemData, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ResourceItemData concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ResourceItemData concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Resource item type
            .flatMap(v -> {
              log.debug("🔄 Creating ResourceItemType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemType, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ResourceItemType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ResourceItemType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Resource item type relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ResourceItemXResourceItemType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemXResourceItemType, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ResourceItemXResourceItemType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ResourceItemXResourceItemType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Resource item data classification
            .flatMap(v -> {
              log.debug("🔄 Creating ResourceItemDataXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemDataXClassification, 
                      "Resource Item Data Classifications", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ResourceItemDataXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ResourceItemDataXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Resource item classification
            .flatMap(v -> {
              log.debug("🔄 Creating ResourceItemXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ResourceItemXClassification, 
                      "all resource item classifications", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ResourceItemXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ResourceItemXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all resource item concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating resource item concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Resource item concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in resource item concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }

  private Uni<Void> createRulesConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Rules Concepts");
    log.info("🚀 Creating rules concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating rules concepts sequentially with session: {}", session.hashCode());
          
          // Base rules concept
          return service.createDataConcept(session, Rules, 
                    "Calculated rules", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created Rules concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create Rules concept: {}", 
                                    error.getMessage(), error))
            
            // Rules type
            .flatMap(v -> {
              log.debug("🔄 Creating RulesType concept");
              return service.createDataConcept(session, RulesType, 
                      "Calculated rules types", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created RulesType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create RulesType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Rules type classification
            .flatMap(v -> {
              log.debug("🔄 Creating RulesTypeXClassification concept");
              return service.createDataConcept(session, RulesTypeXClassification, 
                      "Rule Types Calculations", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created RulesTypeXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create RulesTypeXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Rules product relationship
            .flatMap(v -> {
              log.debug("🔄 Creating RulesXProduct concept");
              return service.createDataConcept(session, RulesXProduct, 
                      "Rule Types Calculations", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created RulesXProduct concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create RulesXProduct concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Rules resource item relationship
            .flatMap(v -> {
              log.debug("🔄 Creating RulesXResourceItem concept");
              return service.createDataConcept(session, RulesXResourceItem, 
                      "Rule Types Calculations", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created RulesXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create RulesXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all rules concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating rules concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Rules concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in rules concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }


  private Uni<Void> createActiveFlagConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Active Flag Concepts");
    log.info("🚀 Creating active flag concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating active flag concepts sequentially with session: {}", session.hashCode());
          
          // Base active flag concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ActiveFlag, 
                    "An Active Flag", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created ActiveFlag concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create ActiveFlag concept: {}", 
                                    error.getMessage(), error))
            
            // Active flag classification relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ActiveFlagXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ActiveFlagXClassification, 
                      "Any active flag classifications", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ActiveFlagXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ActiveFlagXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all active flag concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating active flag concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Active flag concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in active flag concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }


  private Uni<Void> createGeographyConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Geography Concepts");
    log.info("🚀 Creating geography concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating geography concepts sequentially with session: {}", session.hashCode());
          
          // Base geography concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Geography, 
                    "Specific to a geography item", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created Geography concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create Geography concept: {}", 
                                    error.getMessage(), error))
            
            // Geography classification relationship
            .flatMap(v -> {
              log.debug("🔄 Creating GeographyXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXClassification, 
                      "All Geography Classifications", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created GeographyXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create GeographyXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Geography hierarchy relationship
            .flatMap(v -> {
              log.debug("🔄 Creating GeographyXGeography concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXGeography, 
                      "All Geography Relationships", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created GeographyXGeography concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create GeographyXGeography concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Geography resource item relationship
            .flatMap(v -> {
              log.debug("🔄 Creating GeographyXResourceItem concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.GeographyXResourceItem, 
                      "All geography resource items", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created GeographyXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create GeographyXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all geography concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating geography concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Geography concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in geography concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }

  private Uni<Void> createAddressConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Address Concepts");
    log.info("🚀 Creating address concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating address concepts sequentially with session: {}", session.hashCode());
          
          // Base address concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Address, 
                    "Addresses are a physical location in a certain geography", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created Address concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create Address concept: {}", 
                                    error.getMessage(), error))
            
            // Address geography relationship
            .flatMap(v -> {
              log.debug("🔄 Creating AddressXGeography concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXGeography, 
                      "Any classifications for Address Geography groupings", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created AddressXGeography concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create AddressXGeography concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Address resource item relationship
            .flatMap(v -> {
              log.debug("🔄 Creating AddressXResourceItem concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXResourceItem, 
                      "Any classification for resource items", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created AddressXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create AddressXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all address concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating address concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Address concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in address concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }

  private Uni<Void> createArrangementConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Arrangement Concepts");
    log.info("🚀 Creating arrangement concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating arrangement concepts sequentially with session: {}", session.hashCode());
          
          // Base arrangement concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Arrangement, 
                    "Standard Table Based Classification", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created Arrangement concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create Arrangement concept: {}", 
                                    error.getMessage(), error))
            
            // Arrangement involved party relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementXInvolvedParty concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXInvolvedParty, 
                      "Any relationships for the arrangement and the involved party", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementXInvolvedParty concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementXInvolvedParty concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Arrangement classification relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXClassification, 
                      "Any classifications for arrangements", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Arrangement type relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementXArrangementType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXArrangementType, 
                      "Any classifications for the type specified in the arrangement", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementXArrangementType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementXArrangementType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Arrangement hierarchy relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementXArrangement concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXArrangement, 
                      "Any classifications for the Relationships between Arrangement and X Arrangements", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementXArrangement concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementXArrangement concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Address involved party relationship
            .flatMap(v -> {
              log.debug("🔄 Creating AddressXInvolvedParty concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.AddressXInvolvedParty, 
                      "Any Address Relationship with Involved Parties", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created AddressXInvolvedParty concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create AddressXInvolvedParty concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Arrangement product relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementXProduct concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXProduct, 
                      "Arrangement classifications with product", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementXProduct concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementXProduct concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Arrangement resource item relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementXResourceItem concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementXResourceItem, 
                      "Any Arrangement Relationship with a Resource Item", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Arrangement rules relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementXRules concept");
              return service.createDataConcept(session, ArrangementXRules, 
                      "Classifications for an arrangements link to a set of rules", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementXRules concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementXRules concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Arrangement rules types relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementXRulesTypes concept");
              return service.createDataConcept(session, ArrangementXRulesTypes, 
                      "Classifications for an arrangements link to a set of rules", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementXRulesTypes concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementXRulesTypes concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Arrangement type
            .flatMap(v -> {
              log.debug("🔄 Creating ArrangementType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ArrangementType, 
                      "A type of arrangement or agreement", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ArrangementType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ArrangementType concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all arrangement concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating arrangement concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Arrangement concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in arrangement concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }

  private Uni<Void> createClassificationsConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Classification Concepts");
    log.info("🚀 Creating classification concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating classification concepts sequentially with session: {}", session.hashCode());
          
          // Base classification data concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConcept, 
                    "A designation of a table", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created ClassificationDataConcept concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create ClassificationDataConcept concept: {}", 
                                    error.getMessage(), error))
            
            // Classification data concept classification relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ClassificationDataConceptXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConceptXClassification, 
                      "All classifications for the data concepts", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ClassificationDataConceptXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ClassificationDataConceptXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Classification data concept resource item relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ClassificationDataConceptXResourceItem concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationDataConceptXResourceItem, 
                      "Resource Items for data concepts", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ClassificationDataConceptXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ClassificationDataConceptXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Base classification concept
            .flatMap(v -> {
              log.debug("🔄 Creating Classification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Classification, 
                      "Standard Table Based Classification", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created Classification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create Classification concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Classification hierarchy relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ClassificationXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationXClassification, 
                      "The Classification Hierarchy", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ClassificationXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ClassificationXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Classification resource item relationship
            .flatMap(v -> {
              log.debug("🔄 Creating ClassificationXResourceItem concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.ClassificationXResourceItem, 
                      "Any classification type that exists for classification and resource items, usually icons etc", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created ClassificationXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create ClassificationXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all classification concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating classification concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Classification concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in classification concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
  }

  private Uni<Void> createEventsConcepts(IEnterprise<?, ?> enterprise)
  {
    logProgress("Data Concept System", "Event Concepts");
    log.info("🚀 Creating event concepts for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting with a new session and transaction");

    return sessionFactory.withTransaction((session, tx) -> {
      log.debug("📋 Created new transaction with session: {}", session.hashCode());
      
      return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
        .onItem()
        .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                 system.getName(), session.hashCode()))
        .onFailure()
        .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                error.getMessage(), error))
        .flatMap(system -> {
          log.debug("🔄 Creating event concepts sequentially with session: {}", session.hashCode());
          
          // Base event concept
          return service.createDataConcept(session, EnterpriseClassificationDataConcepts.Event, 
                    "An event is an audit of any change in the system, post installation", system)
            .onItem()
            .invoke(result -> log.debug("✅ Created Event concept"))
            .onFailure()
            .invoke(error -> log.error("❌ Failed to create Event concept: {}", 
                                    error.getMessage(), error))
            
            // Event type
            .flatMap(v -> {
              log.debug("🔄 Creating EventType concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventType, 
                      "A specific type of event ", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event type relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXEventType concept");
              return service.createDataConcept(session, EventXEventType, 
                      "Classifications for events", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXEventType concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXEventType concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event classification relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXClassification concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXClassification, 
                      "All classifications that relate to events", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXClassification concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXClassification concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event address relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXAddress concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXAddress, 
                      "Any classifications that relate to Events and Classifications, such as occured at", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXAddress concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXAddress concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event arrangement relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXArrangement concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXArrangement, 
                      "Any classifications for event and arrangements", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXArrangement concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXArrangement concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event involved party relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXInvolvedParty concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXInvolvedParty, 
                      "Any classifications for involved parties and events like bought, purchased, called, emailed", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXInvolvedParty concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXInvolvedParty concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event geography relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXGeography concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXGeography, 
                      "Geographical mappings for events", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXGeography concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXGeography concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event product relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXProduct concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXProduct, 
                      "Any Product that exists for X product classifications", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXProduct concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXProduct concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event resource item relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXResourceItem concept");
              return service.createDataConcept(session, EnterpriseClassificationDataConcepts.EventXResourceItem, 
                      "Any event resource items, usually json or data received in some force", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXResourceItem concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXResourceItem concept: {}", 
                                        error.getMessage(), error));
            })
            
            // Event rules relationship
            .flatMap(v -> {
              log.debug("🔄 Creating EventXRules concept");
              return service.createDataConcept(session, EventXRules, 
                      "An events link to a set of rules", system)
                .onItem()
                .invoke(result -> log.debug("✅ Created EventXRules concept"))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to create EventXRules concept: {}", 
                                        error.getMessage(), error));
            })
            .onItem()
            .invoke(() -> log.info("🎉 Successfully created all event concepts"))
            .onFailure()
            .invoke(error -> log.error("❌ Error creating event concepts: {}", 
                                    error.getMessage(), error));
        });
    })
    .onItem()
    .invoke(() -> log.info("✅ Event concepts creation completed successfully"))
    .onFailure()
    .invoke(error -> log.error("❌ Error in event concepts creation: {}", 
                            error.getMessage(), error))
    .replaceWithVoid();
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
