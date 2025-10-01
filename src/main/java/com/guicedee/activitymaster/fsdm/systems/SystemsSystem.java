package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.guicedee.activitymaster.fsdm.InvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.*;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.guicedinjection.pairing.Pair;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.*;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;


@Log4j2
public class SystemsSystem
    extends ActivityMasterDefaultSystem<SystemsSystem>
    implements IActivityMasterSystem<SystemsSystem>
{

  @Inject
  private ISystemsService<?> systemsService;

  /**
   * Creates all the applicable systems that are required before the actual activity master system can be created.
   * After the user groups setup then security controls kick in
   *
   * @param session
   * @param enterprise
   * @return
   */
  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Activity Master System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Activity Master System with session: {}", session.hashCode());

    //Must register EnterpriseSystem, ActiveFlagSystem, and Activity Master System

    return systemsService
               .create(session, enterprise, ActivityMasterSystemName, "The Core Enterprise Activity Monitoring Application", "Activity Master")
               .invoke(system -> log.debug("✅ Created Activity Master System: '{}' with session: {}", system.getName(), session.hashCode()))
               .chain(system -> {
                 return systemsService.registerNewSystem(session, enterprise, system)
                            .replaceWith(system);
               })
               .onFailure()
               .invoke(error -> log.error("❌ Failed to create Activity Master System: '{}' with session {}: {}",
                   ActivityMasterSystemName, session.hashCode(), error.getMessage(), error))
               .map(result->result);
  }

  /**
   * Creates default systems for the enterprise sequentially.
   * <p>
   * This method creates three systems in sequence:
   * 1. Enterprise System
   * 2. Active Flag System
   * 3. Activity Master System
   * <p>
   * The systems are created sequentially using flatMap() to ensure database session consistency.
   * The method returns a Uni<Void> that completes only when all system creations are complete.
   *
   * @param session    The Hibernate reactive session
   * @param enterprise The enterprise for which to create systems
   * @return A Uni<Void> that completes when all systems have been created
   */
  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering default systems for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Starting sequential system creation with session: {}", session.hashCode());

    return systemsService
               .create(session, enterprise, EnterpriseSystemName, "The system for handling enterprises")
               .onItem()
               .invoke(system ->
                           log.debug("✅ Created Enterprise System: '{}' with session: {}", system.getName(), session.hashCode()))
               .onFailure()
               .invoke(error ->
                           log.error("❌ Failed to create Enterprise System with session {}: {}", session.hashCode(), error.getMessage(), error))

               .flatMap(entSystem -> systemsService
                                         .create(session, enterprise, ActivateFlagSystemName, "The system for the active flag management")
                                         .onItem()
                                         .invoke(system ->
                                                     log.debug("✅ Created Active Flag System: '{}' with session: {}", system.getName(), session.hashCode()))
                                         .onFailure()
                                         .invoke(error ->
                                                     log.error("❌ Failed to create Active Flag System with session {}: {}", session.hashCode(), error.getMessage(), error))
               )

               .flatMap(flagSystem -> systemsService
                                          .create(session, enterprise, ActivityMasterSystemName,
                                              "The Core Enterprise Activity Monitoring Application", "Activity Master")
                                          .onItem()
                                          .invoke(system ->
                                                      log.debug("✅ Created Activity Master System: '{}' with session: {}", system.getName(), session.hashCode()))
                                          .onFailure()
                                          .invoke(error ->
                                                      log.error("❌ Failed to create Activity Master System with session {}: {}", session.hashCode(), error.getMessage(), error))
               )

               .onItem()
               .invoke(() ->
                           log.info("🎉 Successfully registered all systems for enterprise: '{}'", enterprise.getName()))
               .replaceWithVoid();
  }


  @Override
  public int totalTasks()
  {
    return 2;
  }

  /**
   * Creates an involved party for a new system in a reactive way.
   * <p>
   * This method follows the reactive pattern by:
   * 1. Retrieving the Activity Master System
   * 2. Getting the Activity Master System UUID
   * 3. Getting the System UUID
   * 4. Creating the involved party
   * 5. Running the remaining operations sequentially:
   * - Adding identification type to the involved party
   * - Adding party type to the involved party
   * - Adding name type to the involved party
   *
   * @param session       The Hibernate reactive session
   * @param system        The system for which to create an involved party
   * @param identityToken Optional identity tokens
   * @return A Uni that emits the created involved party when complete
   */
  public Uni<IInvolvedParty<?, ?>> createInvolvedPartyForNewSystem(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    log.info("🚀 Creating involved party for system: '{}'", system.getName());
    log.debug("📋 Starting involved party creation with session: {}", session.hashCode());

    InvolvedPartyService ipService = com.guicedee.client.IGuiceContext.get(InvolvedPartyService.class);

    // Get Activity Master System
    log.debug("📋 Retrieving Activity Master System for enterprise: '{}' with session: {}",
        system.getEnterpriseID()
            .getName(), session.hashCode());

    return systemsService
               .getActivityMaster(session, system.getEnterpriseID())
               .onItem()
               .invoke(s -> log.debug("✅ Retrieved Activity Master System: '{}' with session: {}",
                   s.getName(), session.hashCode()))
               .onFailure()
               .invoke(error -> log.error("❌ Failed to retrieve Activity Master System with session {}: {}",
                   session.hashCode(), error.getMessage(), error))
               // Chain to get Activity Master System UUID
               .chain(activityMasterSystem -> {
                 log.debug("📋 Retrieving security identity token for Activity Master System with session: {}", session.hashCode());
                 return systemsService
                            .getSecurityIdentityToken(session, activityMasterSystem)
                            .onItem()
                            .invoke(uuid -> log.debug("✅ Retrieved Activity Master System UUID: '{}' with session: {}",
                                uuid, session.hashCode()))
                            .onFailure()
                            .invoke(error -> log.error("❌ Failed to retrieve Activity Master System UUID with session {}: {}",
                                session.hashCode(), error.getMessage(), error))
                            // Chain to get System UUID
                            .chain(activityMasterSystemUUID -> {
                              log.debug("📋 Retrieving security identity token for system: '{}' with session: {}",
                                  system.getName(), session.hashCode());
                              return systemsService
                                         .getSecurityIdentityToken(session, system, activityMasterSystemUUID)
                                         .onItem()
                                         .invoke(uuid -> log.debug("✅ Retrieved System UUID: '{}' with session: {}",
                                             uuid, session.hashCode()))
                                         .onFailure()
                                         .invoke(error -> log.error("❌ Failed to retrieve System UUID with session {}: {}",
                                             session.hashCode(), error.getMessage(), error))
                                         // Chain to create involved party
                                         .chain(newSystemUUID -> {
                                           if (newSystemUUID == null)
                                           {
                                             log.error("❌ No UUID found for newly created system: '{}' with session: {}",
                                                 system.getName(), session.hashCode());
                                             return Uni.createFrom()
                                                        .failure(
                                                            new ActivityMasterException("No UUID for newly created system"));
                                           }

                                           // Create the involved party
                                           log.debug("📋 Creating involved party with UUID identification for system: '{}' with session: {}",
                                               system.getName(), session.hashCode());
                                           return ipService.create(
                                                   session,
                                                   system,
                                                   Pair.of(IdentificationTypes.IdentificationTypeUUID.toString(), newSystemUUID.toString()),
                                                   false,
                                                   activityMasterSystemUUID
                                               )
                                                      .onItem()
                                                      .invoke(ip -> log.debug("✅ Created involved party for system: '{}' with session: {}",
                                                          system.getName(), session.hashCode()))
                                                      .onFailure()
                                                      .invoke(error -> log.error("❌ Failed to create involved party with session {}: {}",
                                                          session.hashCode(), error.getMessage(), error))
                                                      // Chain to run the remaining operations sequentially
                                                      .chain(involvedParty -> {
                                                        log.debug("🔄 Starting sequential operations for involved party with session: {}", session.hashCode());

                                                        // 1. Find and add identification type (first operation in the chain)
                                                        return ipService.findInvolvedPartyIdentificationType(
                                                                session,
                                                                IdentificationTypes.IdentificationTypeSystemID.toString(),
                                                                system,
                                                                identityToken
                                                            )
                                                                   .onItem()
                                                                   .invoke(type -> log.debug("✅ Found identification type with session: {}", session.hashCode()))
                                                                   .onFailure()
                                                                   .invoke(error -> log.error("❌ Failed to find identification type with session {}: {}",
                                                                       session.hashCode(), error.getMessage(), error))
                                                                   .chain(involvedPartyIdentificationType -> {
                                                                     log.debug("🔗 Linking identification type to involved party with session: {}", session.hashCode());
                                                                     return involvedParty.addOrReuseInvolvedPartyIdentificationType(
                                                                         session,
                                                                         NoClassification.toString(),
                                                                         involvedPartyIdentificationType,
                                                                         system.getId()
                                                                             .toString(),
                                                                         system,
                                                                         activityMasterSystemUUID
                                                                     );
                                                                   })
                                                                   .onItem()
                                                                   .invoke(result -> log.debug("✅ Successfully linked identification type with session: {}", session.hashCode()))
                                                                   .onFailure()
                                                                   .invoke(error -> log.error("❌ Failed to link identification type with session {}: {}",
                                                                       session.hashCode(), error.getMessage(), error))

                                                                   // 2. Chain to find and add party type (second operation in the chain)
                                                                   .chain(result -> {
                                                                     log.debug("🔄 Starting second operation: find and add party type with session: {}", session.hashCode());
                                                                     return ipService.findType(
                                                                         session,
                                                                         IPTypes.TypeSystem.toString(),
                                                                         system,
                                                                         identityToken
                                                                     );
                                                                   })
                                                                   .onItem()
                                                                   .invoke(type -> log.debug("✅ Found party type with session: {}", session.hashCode()))
                                                                   .onFailure()
                                                                   .invoke(error -> log.error("❌ Failed to find party type with session {}: {}",
                                                                       session.hashCode(), error.getMessage(), error))
                                                                   .chain(ipType -> {
                                                                     log.debug("🔗 Linking party type to involved party with session: {}", session.hashCode());
                                                                     return involvedParty.addOrReuseInvolvedPartyType(
                                                                         session,
                                                                         NoClassification.toString(),
                                                                         ipType,
                                                                         newSystemUUID.toString(),
                                                                         system,
                                                                         activityMasterSystemUUID
                                                                     );
                                                                   })
                                                                   .onItem()
                                                                   .invoke(result -> log.debug("✅ Successfully linked party type with session: {}", session.hashCode()))
                                                                   .onFailure()
                                                                   .invoke(error -> log.error("❌ Failed to link party type with session {}: {}",
                                                                       session.hashCode(), error.getMessage(), error))

                                                                   // 3. Chain to find and add name type (third operation in the chain)
                                                                   .chain(result -> {
                                                                     log.debug("🔄 Starting third operation: find and add name type with session: {}", session.hashCode());
                                                                     return ipService.findInvolvedPartyNameType(
                                                                         session,
                                                                         NameTypes.PreferredNameType.toString(),
                                                                         system,
                                                                         identityToken
                                                                     );
                                                                   })
                                                                   .onItem()
                                                                   .invoke(type -> log.debug("✅ Found name type with session: {}", session.hashCode()))
                                                                   .onFailure()
                                                                   .invoke(error -> log.error("❌ Failed to find name type with session {}: {}",
                                                                       session.hashCode(), error.getMessage(), error))
                                                                   .chain(nameType -> {
                                                                     log.debug("🔗 Linking name type to involved party with session: {}", session.hashCode());
                                                                     return involvedParty.addOrReuseInvolvedPartyNameType(
                                                                         session,
                                                                         NoClassification.toString(),
                                                                         nameType,
                                                                         system.getName(),
                                                                         system,
                                                                         activityMasterSystemUUID
                                                                     );
                                                                   })
                                                                   .onItem()
                                                                   .invoke(result -> log.debug("✅ Successfully linked name type with session: {}", session.hashCode()))
                                                                   .onFailure()
                                                                   .invoke(error -> log.error("❌ Failed to link name type with session {}: {}",
                                                                       session.hashCode(), error.getMessage(), error))

                                                                   // After all three operations complete sequentially
                                                                   .onItem()
                                                                   .invoke(() -> log.info("🎉 Successfully created involved party for system: '{}'", system.getName()))
                                                                   .onFailure()
                                                                   .invoke(error -> log.error("💥 Failed to complete sequential operations for system '{}' with session {}: {}",
                                                                       system.getName(), session.hashCode(), error.getMessage(), error))
                                                                   .replaceWith(involvedParty) // Return the involved party after all operations complete
                                                                   .map(ip -> (IInvolvedParty<?, ?>) ip); // Ensure the return type matches the method signature exactly
                                                      });
                                         });
                            });
               })
               .onFailure()
               .recoverWithUni(error -> {
                 log.error("💥 Failed to build InvolvedParty for system '{}' with session {}: {}",
                     system.getName(), session.hashCode(), error.getMessage(), error);
                 return Uni.<IInvolvedParty<?, ?>>createFrom()
                            .failure(error);
               })
               // Final map to ensure the return type is exactly Uni<IInvolvedParty<?, ?>>
               .map(involvedParty -> involvedParty);
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE + 2;
  }

  @Override
  public String getSystemName()
  {
    return ActivityMasterSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The Core Enterprise Activity Monitoring Application";
  }
}
