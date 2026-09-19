package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.MasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ProductClassifications;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.IProductService.*;
import static com.guicedee.activitymaster.fsdm.client.services.ISystemsService.ActivityMasterSystemName;


@Log4j2
public class ProductsSystem
    extends MasterDefaultSystem<ProductsSystem>
    implements IMasterSystem<ProductsSystem>
{
  @Inject
  private IClassificationService<?> service;

  @Inject
  private ISystemsService<?> systemsService;

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
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

  /**
   * Stateless end-to-end variant of {@link #createDefaults(Mutiny.StatelessSession, IEnterprise)} — provisions the
   * full Products classification hierarchy (Products → ProductGroup → ProductTypeName / ProductPremiumType
   * / ProductBaseCost) entirely on a {@link Mutiny.StatelessSession}, using the prepped system resolution,
   * the stateless system-identity token, and the parent-aware stateless {@code IClassificationService.create}
   * (which performs the lean insert, stateless default-security, and the stateless hierarchy {@code addChild}).
   */
  @Override
  public Uni<Void> createDefaults(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
  {
    logProgress("Products System", "Starting Products Checks (stateless)");
    log.info("🚀 Creating product defaults on a stateless session for enterprise: '{}'", enterprise.getName());

    return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
               .chain(activityMasterSystem -> getSystemToken(session, enterprise)
                   .chain(systemToken -> {
                     UUID[] tokens = systemToken == null ? new UUID[0] : new UUID[]{systemToken};
                     return service.create(session, ProductClassifications.Products, activityMasterSystem, tokens)
                                .chain(base -> service.create(session, ProductClassifications.ProductGroup, activityMasterSystem, ProductClassifications.Products, tokens))
                                .chain(group -> service.create(session, ProductClassifications.ProductTypeName, activityMasterSystem, ProductClassifications.ProductGroup, tokens))
                                .chain(typeName -> service.create(session, ProductClassifications.ProductPremiumType, activityMasterSystem, ProductClassifications.ProductGroup, tokens))
                                .chain(premium -> service.create(session, ProductClassifications.ProductBaseCost, activityMasterSystem, ProductClassifications.ProductGroup, tokens))
                                .invoke(v -> logProgress("Products System", "Loaded Product Classifications (stateless)...", 4))
                                .replaceWithVoid();
                   }))
               .onItem().invoke(() -> log.info("✅ (stateless) Successfully created all product defaults"))
               .onFailure().invoke(error -> log.error("❌ (stateless) Failed to create product defaults: {}", error.getMessage(), error));
  }

  @Override
  public Uni<Void> postStartup(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
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
