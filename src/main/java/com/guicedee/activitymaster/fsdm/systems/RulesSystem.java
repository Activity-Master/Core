package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.MasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IMasterSystem;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
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
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
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


  /**
   * Stateless end-to-end variant of {@link #createDefaults(Mutiny.StatelessSession, IEnterprise)} — provisions the
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
  public Uni<Void> postStartup(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Rules System");
    log.debug("📋 Beginning postStartup operations for enterprise: '{}' with session: {}",
        enterprise.getName(), session.hashCode());

    return systemsService.findSystem(session, enterprise, getSystemName())
               .onItem().ifNull().failWith(() -> new NoResultException("System not found: " + getSystemName()))
               .onFailure(NoResultException.class).recoverWithUni(missing -> {
                 log.warn("Rules System is missing for enterprise '{}'; registering it", enterprise.getName());
                 return registerSystem(session, enterprise)
                            .chain(ignored -> systemsService.findSystem(session, enterprise, getSystemName()))
                            .onItem().ifNull().failWith(() -> new NoResultException("Rules System was not found after registration"));
               })
               .chain(system -> systemsService.getSecurityIdentityToken(session, system)
                   .onItem().ifNull().failWith(() -> new NoResultException("Security token not found for system: " + system.getName()))
                   .replaceWithVoid())
               .invoke(() -> log.debug("✅ Successfully completed postStartup for Rules System"))
               .onFailure().invoke(error -> log.error("Rules System startup failed for enterprise '{}'", enterprise.getName(), error));
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
