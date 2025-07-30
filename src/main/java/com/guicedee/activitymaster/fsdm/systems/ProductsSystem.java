package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ProductClassifications;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IProductService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;


@Log4j2
public class ProductsSystem
    extends ActivityMasterDefaultSystem<ProductsSystem>
    implements IActivityMasterSystem<ProductsSystem>
{
  @Inject
  private IClassificationService<?> service;

  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Products System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Products System with session: {}", session.hashCode());

    return systemsService
               .create(session, enterprise, getSystemName(), getSystemDescription())
               .chain(system -> {
                 log.debug("✅ Created Products System: '{}' with session: {}", system.getName(), session.hashCode());

                 // Chain the registerNewSystem call properly
                 return getSystem(session, enterprise)
                            .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                            .onItem()
                            .invoke(() -> {
                              log.debug("✅ Registered system: {}", getSystemName());
                              log.info("🎉 Successfully registered Products System for enterprise: '{}'", enterprise.getName());
                            })
                            .onFailure()
                            .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                            .chain(() -> Uni.createFrom()
                                             .item(system)); // Chain back to return the original system
               })
               .onFailure()
               .invoke(error -> log.error("❌ Failed to create Products System: '{}' with session {}: {}",
                   getSystemName(), session.hashCode(), error.getMessage(), error))
               .map(result -> result);
  }

  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Products System", "Starting Products Checks");
    log.info("🚀 Creating product defaults in a new session and transaction");
    log.debug("📋 Starting product defaults creation for enterprise: '{}'", enterprise.getName());

    log.debug("📋 Created new transaction with session: {}", session.hashCode());

    // Get the ActivityMaster system
    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
               .onItem()
               .invoke(activityMasterSystem ->
                           log.debug("✅ Found ActivityMaster system: '{}' with session: {}",
                               activityMasterSystem.getName(), session.hashCode()))
               .onFailure()
               .invoke(error ->
                           log.error("❌ Failed to find ActivityMaster system: {}", error.getMessage(), error))
               .chain(activityMasterSystem -> {
                 // Get system token once and reuse it
                 return getSystemToken(session, enterprise)
                            .onItem()
                            .invoke(systemToken ->
                                        log.debug("🔑 Retrieved system token for enterprise: '{}'", enterprise.getName()))
                            .onFailure()
                            .invoke(error ->
                                        log.error("❌ Failed to retrieve system token: {}", error.getMessage(), error))
                            .chain(systemToken -> {
                              // Create base product classification
                              log.debug("📋 Creating base product classification");
                              return service.create(session, ProductClassifications.Products, activityMasterSystem, systemToken)
                                         .onItem()
                                         .invoke(baseClassification ->
                                                     log.debug("✅ Created base product classification: '{}'", baseClassification.getName()))
                                         .onFailure()
                                         .invoke(error ->
                                                     log.error("❌ Failed to create base product classification: {}", error.getMessage(), error))
                                         .chain(baseClassification -> {
                                           logProgress("Products System", "Creating product classifications...");
                                           log.debug("📋 Creating product classifications sequentially");

                                           // Create product-related classifications sequentially
                                           return service.create(session, ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.Products, systemToken)
                                                      .onItem()
                                                      .invoke(classification ->
                                                                  log.debug("✅ Created ProductGroup classification: '{}'", classification.getName()))
                                                      .onFailure()
                                                      .invoke(error ->
                                                                  log.error("❌ Failed to create ProductGroup classification: {}", error.getMessage(), error))

                                                      // Then create ProductTypeName classification
                                                      .chain(productGroup ->
                                                                 service.create(session, ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup, systemToken)
                                                                     .onItem()
                                                                     .invoke(classification ->
                                                                                 log.debug("✅ Created ProductTypeName classification: '{}'", classification.getName()))
                                                                     .onFailure()
                                                                     .invoke(error ->
                                                                                 log.error("❌ Failed to create ProductTypeName classification: {}", error.getMessage(), error))
                                                      )

                                                      // Then create ProductPremiumType classification
                                                      .chain(productTypeName ->
                                                                 service.create(session, ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup, systemToken)
                                                                     .onItem()
                                                                     .invoke(classification ->
                                                                                 log.debug("✅ Created ProductPremiumType classification: '{}'", classification.getName()))
                                                                     .onFailure()
                                                                     .invoke(error ->
                                                                                 log.error("❌ Failed to create ProductPremiumType classification: {}", error.getMessage(), error))
                                                      )

                                                      // Then create ProductBaseCost classification
                                                      .chain(productPremiumType ->
                                                                 service.create(session, ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup, systemToken)
                                                                     .onItem()
                                                                     .invoke(classification ->
                                                                                 log.debug("✅ Created ProductBaseCost classification: '{}'", classification.getName()))
                                                                     .onFailure()
                                                                     .invoke(error ->
                                                                                 log.error("❌ Failed to create ProductBaseCost classification: {}", error.getMessage(), error))
                                                      )

                                                      // Complete the sequence
                                                      .onItem()
                                                      .invoke(() -> {
                                                        log.debug("✅ Successfully created all product classifications sequentially");
                                                        logProgress("Products System", "Loaded Product Classifications...", 4);
                                                      })
                                                      .onFailure()
                                                      .invoke(error ->
                                                                  log.error("❌ Error creating product classifications: {}", error.getMessage(), error));
                                         });
                            });
               })
               .onItem()
               .invoke(() -> log.info("✅ Successfully created all product defaults"))
               .onFailure()
               .invoke(error ->
                           log.error("❌ Failed to create product defaults: {}", error.getMessage(), error))
               .replaceWithVoid();
  }

  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Products System");
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
                              log.debug("✅ Successfully completed postStartup for Products System");
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
    return ProductSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for managing Products";
  }
}
