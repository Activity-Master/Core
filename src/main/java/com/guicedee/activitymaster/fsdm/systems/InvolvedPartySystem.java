package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IPTypes;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.NameTypes;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.SystemsService.ActivityMasterSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService.InvolvedPartySystemName;


@Log4j2
public class InvolvedPartySystem
    extends ActivityMasterDefaultSystem<InvolvedPartySystem>
    implements IActivityMasterSystem<InvolvedPartySystem>
{
  @Inject
  private IInvolvedPartyService<?> service;

  @Inject
  private ISystemsService<?> systemsService;

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

  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Involved Party System", "Starting Checks for Required Values");
    log.info("🚀 Creating involved party defaults for enterprise: '{}' with external session: {}",
        enterprise.getName(), session.hashCode());

    // Create all three types sequentially in a single transaction
    log.info("📋 Starting sequential creation of identification types, name types, and types");

    // Chain the operations in sequence using the same session
    return createIdentificationTypes(session, enterprise)
               .flatMap(v -> {
                 log.debug("🔄 Identification types created, proceeding to name types");
                 return createNameTypes(session, enterprise);
               })
               .flatMap(v -> {
                 log.debug("🔄 Name types created, proceeding to involved party types");
                 return createTypes(session, enterprise);
               })
               .onFailure()
               .invoke(error -> log.error("❌ Error in sequential operations: {}", error.getMessage(), error))
               .invoke(v -> {
                 log.info("🎉 Completed sequential creation of identification types, name types, and types");
                 // Create default users
                 createDefaultUsers(enterprise);
               });
  }

  private Uni<Void> createIdentificationTypes(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.debug("📋 Creating identification types for enterprise: '{}'", enterprise.getName());
    log.info("🚀 Creating identification types with session: {}", session.hashCode());

    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
               .onItem()
               .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}",
                   system.getName(), session.hashCode()))
               .onFailure()
               .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}",
                   error.getMessage(), error))
               .chain(system -> {
                 log.debug("📋 Preparing identification types with system: '{}' and session: {}",
                     system.getName(), session.hashCode());

                 log.info("🔄 Starting sequential creation of 15 identification types with session: {}",
                     session.hashCode());

                 // Create identification types sequentially using flatMap
                 return service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeDriversLicense, "Describes an Individuals Drivers Licence Number")
                            .invoke(() -> log.debug("✅ Created IdentificationTypeDriversLicense (1/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypePassportNumber, "Describes an Individuals Passport Number"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypePassportNumber (2/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeTaxNumber, "Tax ID Number"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeTaxNumber (3/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeVATNumber, "Describes an Organisation's VAT Registration number."))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeVATNumber (4/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeRegistrationNumber, "Describes an Organisation's Registration number."))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeRegistrationNumber (5/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeIdentityNumber, "An Individual Green-Bard Coded ID Document Number"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeIdentityNumber (6/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeEmailAddress, "An individuals email address"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeEmailAddress (7/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeCellPhoneNumber, "An individuals cell phone number"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeCellPhoneNumber (8/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeSocialSecurityNumber, "An individuals social security number"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeSocialSecurityNumber (9/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeUserName, "An individuals username"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeUserName (10/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeUUID, "A given unique system identifier"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeUUID (11/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeSessionID, "A Given JavascriptSession ID"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeSessionID (12/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeSystemID, "A unique system identifier granted to each each system on registration"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeSystemID (13/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeEnterpriseCreatorRole, "An identifier marking the involved party as the creator of the enterprise"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeEnterpriseCreatorRole (14/15)"))
                            .flatMap(v -> service.createIdentificationType(session, system, IdentificationTypes.IdentificationTypeUnassigned, "This involved party is unassigned and should be classified"))
                            .invoke(() -> log.debug("✅ Created IdentificationTypeUnassigned (15/15)"))
                            .onFailure()
                            .invoke(error -> log.error("❌ Error creating identification types: {}", error.getMessage(), error))
                            .invoke(() -> {
                              log.info("✅ Successfully created all identification types sequentially");
                              logProgress("Involved Party System", "Loaded Identification Types", 16);
                            })
                            .map(result -> null); // Convert to Void
               });
  }

  private Uni<Void> createNameTypes(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.debug("📋 Creating name types for enterprise: '{}'", enterprise.getName());
    log.info("🚀 Creating name types with session: {}", session.hashCode());

    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
               .onItem()
               .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}",
                   system.getName(), session.hashCode()))
               .onFailure()
               .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}",
                   error.getMessage(), error))
               .chain(system -> {
                 log.debug("📋 Preparing name types with system: '{}' and session: {}",
                     system.getName(), session.hashCode());

                 log.info("🔄 Starting sequential creation of 12 name types with session: {}",
                     session.hashCode());

                 // Create name types sequentially using flatMap
                 return service.createNameType(session, NameTypes.FirstNameType, "The first name of an individual or entity", system)
                            .invoke(() -> log.debug("✅ Created FirstNameType (1/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.FullNameType, "The full name of an individual or entity", system))
                            .invoke(() -> log.debug("✅ Created FullNameType (2/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.PreferredNameType, "An Alias for the Involved Party", system))
                            .invoke(() -> log.debug("✅ Created PreferredNameType (3/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.BirthNameType, "A Given Birth/Maiden Name for an Individual", system))
                            .invoke(() -> log.debug("✅ Created BirthNameType (4/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.LegalNameType, "The Legal Name associated with an Involved Party", system))
                            .invoke(() -> log.debug("✅ Created LegalNameType (5/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.CommonNameType, "The Common Name associated with an Individual", system))
                            .invoke(() -> log.debug("✅ Created CommonNameType (6/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.SalutationType, "The Salutation Associated with an Individual", system))
                            .invoke(() -> log.debug("✅ Created SalutationType (7/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.MiddleNameType, "The Middle Name of an Individual", system))
                            .invoke(() -> log.debug("✅ Created MiddleNameType (8/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.InitialsType, "The initials of an Individual", system))
                            .invoke(() -> log.debug("✅ Created InitialsType (9/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.SurnameType, "The last name of an Individual", system))
                            .invoke(() -> log.debug("✅ Created SurnameType (10/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.QualificationType, "The Qualification of an Individual", system))
                            .invoke(() -> log.debug("✅ Created QualificationType (11/12)"))
                            .flatMap(v -> service.createNameType(session, NameTypes.SuffixType, "The Suffix on an Individual's Name - e.g. \"Jnr, Snr\"", system))
                            .invoke(() -> log.debug("✅ Created SuffixType (12/12)"))
                            .onFailure()
                            .invoke(error -> log.error("❌ Error creating name types: {}", error.getMessage(), error))
                            .invoke(() -> {
                              log.info("✅ Successfully created all name types sequentially");
                              logProgress("Involved Party System", "Loaded Name Types", 12);
                            })
                            .map(result -> null); // Convert to Void
               });
  }

  private Uni<Void> createTypes(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.debug("📋 Creating types for enterprise: '{}'", enterprise.getName());
    log.info("🚀 Creating types with session: {}", session.hashCode());

    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
               .onItem()
               .invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}",
                   system.getName(), session.hashCode()))
               .onFailure()
               .invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}",
                   error.getMessage(), error))
               .chain(system -> {
                 log.debug("📋 Preparing involved party types with system: '{}' and session: {}",
                     system.getName(), session.hashCode());

                 log.info("🔄 Starting sequential creation of 7 involved party types with session: {}",
                     session.hashCode());

                 // Create types sequentially using flatMap
                 return service.createType(session, system, IPTypes.TypeIndividual, "Defines any involved party as a physical person")
                            .invoke(() -> log.debug("✅ Created TypeIndividual (1/7)"))
                            .flatMap(v -> service.createType(session, system, IPTypes.TypeOrganisation, "Defines any involved party as an organisation"))
                            .invoke(() -> log.debug("✅ Created TypeOrganisation (2/7)"))
                            .flatMap(v -> service.createType(session, system, IPTypes.TypeSystem, "Defines any involved party as a physical system"))
                            .invoke(() -> log.debug("✅ Created TypeSystem (3/7)"))
                            .flatMap(v -> service.createType(session, system, IPTypes.TypeDevice, "Defines any involved party as a physical system"))
                            .invoke(() -> log.debug("✅ Created TypeDevice (4/7)"))
                            .flatMap(v -> service.createType(session, system, IPTypes.TypeApplication, "Defines any involved party as an application"))
                            .invoke(() -> log.debug("✅ Created TypeApplication (5/7)"))
                            .flatMap(v -> service.createType(session, system, IPTypes.TypeAbstraction, "Represents an abstract entity such as Time"))
                            .invoke(() -> log.debug("✅ Created TypeAbstraction (6/7)"))
                            .flatMap(v -> service.createType(session, system, IPTypes.TypeUnknown, "The type of Involved Party is unknown."))
                            .invoke(() -> log.debug("✅ Created TypeUnknown (7/7)"))
                            .onFailure()
                            .invoke(error -> log.error("❌ Error creating types: {}", error.getMessage(), error))
                            .invoke(() -> {
                              log.info("✅ Successfully created all involved party types sequentially");
                              logProgress("Involved Party System", "Loaded Types", 6);
                            })
                            .map(result -> null); // Convert to Void
               });
  }

  private void createDefaultUsers(IEnterprise<?, ?> enterprise)
  {
    // This method is currently empty in the original code
    // If needed, it can be updated to use reactive programming
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Involved Party System with session: {}", session.hashCode());
    log.debug("📋 Preparing to verify system and security token for enterprise: '{}'", enterprise.getName());

    // Create a reactive chain for the postStartup operations
    // Get the system
    return systemsService.findSystem(session, enterprise, getSystemName())
               .onItem()
               .invoke(system -> {
                 if (system != null)
                 {
                   log.debug("✅ Found system: '{}' with ID: {}", system.getName(), system.getId());
                 }
               })
               .onFailure()
               .invoke(error -> log.error("❌ Error finding system '{}': {}",
                   getSystemName(), error.getMessage(), error))
               .onItem()
               .ifNull()
               .failWith(() -> {
                 log.error("❌ System not found: '{}'", getSystemName());
                 return new RuntimeException("System not found: " + getSystemName());
               })
               .chain(system -> {
                 log.debug("🔍 Verifying security token for system: '{}' with session: {}",
                     system.getName(), session.hashCode());
                 // Get the security token
                 return systemsService.getSecurityIdentityToken(session, system)
                            .onItem()
                            .invoke(token -> {
                              if (token != null)
                              {
                                log.debug("✅ Found security token for system: '{}'", system.getName());
                              }
                            })
                            .onFailure()
                            .invoke(error -> log.error("❌ Error finding security token for system '{}': {}",
                                system.getName(), error.getMessage(), error))
                            .onItem()
                            .ifNull()
                            .failWith(() -> {
                              log.error("❌ Security token not found for system: '{}'", system.getName());
                              return new RuntimeException("Security token not found for system: " + system.getName());
                            })
                            .map(token -> {
                              log.info("🎉 Post-startup verification completed successfully for Involved Party System");
                              return null; // Return Void
                            });
               })
               .replaceWith(Uni.createFrom()
                                .voidItem());
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
