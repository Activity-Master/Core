package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.MasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.IRulesService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;


@Log4j2
public class RulesSystem
    extends MasterDefaultSystem<RulesSystem>
    implements IMasterSystem<RulesSystem>
{
  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private IClassificationService<?> classificationService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Rules System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Rules System with session: {}", session.hashCode());

    return systemsService
               .create(session, enterprise, getSystemName(), getSystemDescription())
               .chain(system -> {
                 log.debug("✅ Created Rules System: '{}' with session: {}", system.getName(), session.hashCode());

                 // Chain the registerNewSystem call properly
                 return getSystem(session, enterprise)
                            .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                            .onItem()
                            .invoke(() -> {
                              log.debug("✅ Registered system: {}", getSystemName());
                              log.info("🎉 Successfully registered Rules System for enterprise: '{}'", enterprise.getName());
                            })
                            .onFailure()
                            .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                            .chain(() -> Uni.createFrom()
                                             .item(system)); // Chain back to return the original system
               })
               .onFailure()
               .invoke(error -> log.error("❌ Failed to create Rules System: '{}' with session {}: {}",
                   getSystemName(), session.hashCode(), error.getMessage(), error))
               .map(result -> result);
  }


  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Rules System", "Creating rule classifications...");
    log.info("🚀 Creating rules defaults in a new session and transaction");
    log.debug("📋 Starting rule defaults creation for enterprise: '{}'", enterprise.getName());

    // Use the passed-in session
    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
                     .onItem()
                     .invoke(activityMasterSystem ->
                                 log.debug("✅ Found ActivityMaster system: '{}' with session: {}",
                                     activityMasterSystem.getName(), session.hashCode()))
                     .onFailure()
                     .invoke(error ->
                                 log.error("❌ Failed to find ActivityMaster system: {}", error.getMessage(), error))
                     .chain(activityMasterSystem -> {
                       logProgress("Rules System", "Creating rule classifications...");
                       log.debug("🔍 Creating rule classifications");

                       // Get system token once and reuse it
                       return getSystemToken(session, enterprise)
                                  .onItem()
                                  .invoke(systemToken ->
                                              log.debug("🔑 Retrieved system token for enterprise: '{}'", enterprise.getName()))
                                  .onFailure()
                                  .invoke(error ->
                                              log.error("❌ Failed to retrieve system token: {}", error.getMessage(), error))
                                  .chain(systemToken -> {
                                    // Create rule-related classifications sequentially
                                    log.debug("📋 Creating rule classifications sequentially");

                                    // Create Rules classification first
                                    return classificationService.create(session, "Rules", "The main rules concept", activityMasterSystem, systemToken)
                                               .onItem()
                                               .invoke(classification ->
                                                           log.debug("✅ Created Rules classification: '{}'", classification.getName()))
                                               .onFailure()
                                               .invoke(error ->
                                                           log.error("❌ Failed to create Rules classification: {}", error.getMessage(), error))

                                               // Then create RulesType classification
                                               .chain(rulesClassification ->
                                                          classificationService.create(session, "RulesType", "The concept for rule types", activityMasterSystem, systemToken)
                                                              .onItem()
                                                              .invoke(classification ->
                                                                          log.debug("✅ Created RulesType classification: '{}'", classification.getName()))
                                                              .onFailure()
                                                              .invoke(error ->
                                                                          log.error("❌ Failed to create RulesType classification: {}", error.getMessage(), error))
                                               )

                                               // Complete the sequence
                                               .onItem()
                                               .invoke(() -> log.debug("✅ Successfully created all rule classifications sequentially"))
                                               .onFailure()
                                               .invoke(error ->
                                                           log.error("❌ Error creating rule classifications: {}", error.getMessage(), error))
                                               .invoke(v -> {
                                                 logProgress("Rules System", "Loaded Rules Classifications...", 4);
                                               });
                                  });
                     })
                     .onItem()
                     .invoke(() -> log.info("✅ Successfully created all rule defaults"))
                     .onFailure()
                     .invoke(error ->
                                 log.error("❌ Failed to create rule defaults: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  /**
   * Stateless end-to-end variant of {@link #createDefaults(Mutiny.Session, IEnterprise)} — provisions the
   * Rules / RulesType classifications entirely on a {@link Mutiny.StatelessSession}, composing the prepped
   * stateless system resolution, the stateless system-identity token, and the stateless
   * {@code IClassificationService.create} (which itself does the lean insert + stateless default security).
   */
  @Override
  public Uni<Void> createDefaults(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Rules System", "Creating rule classifications (stateless)...");
    log.info("🚀 Creating rules defaults on a stateless session for enterprise: '{}'", enterprise.getName());

    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
               .chain(activityMasterSystem -> getSystemToken(session, enterprise)
                   .chain(systemToken -> {
                     UUID[] tokens = systemToken == null ? new UUID[0] : new UUID[]{systemToken};
                     return classificationService.create(session, "Rules", "The main rules concept", activityMasterSystem, tokens)
                                .invoke(c -> log.debug("✅ (stateless) Created Rules classification: '{}'", c.getName()))
                                .chain(rulesClassification ->
                                           classificationService.create(session, "RulesType", "The concept for rule types", activityMasterSystem, tokens)
                                               .invoke(c -> log.debug("✅ (stateless) Created RulesType classification: '{}'", c.getName())))
                                .invoke(v -> logProgress("Rules System", "Loaded Rules Classifications (stateless)...", 4))
                                .replaceWithVoid();
                   }))
               .onItem().invoke(() -> log.info("✅ (stateless) Successfully created all rule defaults"))
               .onFailure().invoke(error -> log.error("❌ (stateless) Failed to create rule defaults: {}", error.getMessage(), error));
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Rules System");
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
                              log.debug("✅ Successfully completed postStartup for Rules System");
                              return null; // Return Void
                            });
               })
               .replaceWith(Uni.createFrom()
                                .voidItem());
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
