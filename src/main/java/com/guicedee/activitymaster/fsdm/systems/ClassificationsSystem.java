package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.*;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.IClassificationService.*;


@Log4j2
public class ClassificationsSystem
    extends ActivityMasterDefaultSystem<ClassificationsSystem>
    implements IActivityMasterSystem<ClassificationsSystem>
{
  @Inject
  private IClassificationService<?> service;

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
    logProgress("Classifications System", "Starting Classifications Creation");
    log.info("🚀 Creating classifications for enterprise: '{}' with external session: {}", 
        enterprise.getName(), session.hashCode());

    return sessionFactory.withTransaction((newSession, tx) ->
                                             systemsService.findSystem(newSession, enterprise, ActivityMasterSystemName)
                                                  .onItem().invoke(system -> log.debug("✅ Found ActivityMaster system: '{}' with session: {}", 
                                                      system.getName(), newSession.hashCode()))
                                                  .onFailure().invoke(error -> log.error("❌ Failed to find ActivityMaster system: {}", 
                                                      error.getMessage(), error))
                                                  .chain(system -> createEnterpriseRootClassification(newSession, enterprise, system))
        )
               .replaceWithVoid();
  }


  private Uni<Void> createEnterpriseRootClassification(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
  {
    log.info("🚀 Creating enterprise root classification for enterprise: '{}' with session: {}", 
        enterprise.getName(), session.hashCode());
    log.debug("📋 Preparing to create root classification with name: '{}'", enterprise.getName());
    
    return sessionFactory.withTransaction((newSession, tx) ->
        service.create(newSession, enterprise.getName(), enterprise.getName(),
                EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, system)
                .onItem().invoke(result -> {
                    log.debug("✅ Created enterprise root classification with ID: {}", result.getId());
                    logProgress("Classifications System", "Loaded Default Classifications...", 1);
                })
                .onFailure().invoke(error -> log.error("❌ Failed to create enterprise root classification: {}", 
                    error.getMessage(), error))
    )
    .chain(v -> {
        log.debug("🔄 Enterprise root classification created, proceeding to base classifications");
        return createBaseClassifications(session, enterprise, system);
    });
  }


  private Uni<Void> createBaseClassifications(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
  {
    log.info("🚀 Creating base classifications for enterprise: '{}' with session: {}", 
        enterprise.getName(), session.hashCode());
    log.debug("📋 Preparing to create hierarchy type and default classifications");

    return sessionFactory.withTransaction((newSession, tx) ->
        service.create(newSession, DefaultClassifications.HierarchyTypeClassification, system)
               .onItem().invoke(result -> log.debug("✅ Created HierarchyTypeClassification with ID: {}", result.getId()))
               .onFailure().invoke(error -> log.error("❌ Failed to create HierarchyTypeClassification: {}", 
                   error.getMessage(), error))
               .chain(v -> {
                   log.debug("📋 Creating HierarchyTypeClassification with enterprise name");
                   return service.create(newSession, DefaultClassifications.HierarchyTypeClassification, system, enterprise.getName());
               })
               .onItem().invoke(result -> log.debug("✅ Created HierarchyTypeClassification with enterprise name, ID: {}", result.getId()))
               .onFailure().invoke(error -> log.error("❌ Failed to create HierarchyTypeClassification with enterprise name: {}", 
                   error.getMessage(), error))
               .chain(v -> {
                   log.debug("📋 Creating NoClassification");
                   return service.create(newSession, DefaultClassifications.NoClassification, system, enterprise.getName());
               })
               .onItem().invoke(result -> log.debug("✅ Created NoClassification with ID: {}", result.getId()))
               .onFailure().invoke(error -> log.error("❌ Failed to create NoClassification: {}", 
                   error.getMessage(), error))
               .chain(v -> {
                   log.debug("📋 Creating DefaultClassification");
                   return service.create(newSession, DefaultClassifications.DefaultClassification, system, enterprise.getName());
               })
               .onItem().invoke(result -> log.debug("✅ Created DefaultClassification with ID: {}", result.getId()))
               .onFailure().invoke(error -> log.error("❌ Failed to create DefaultClassification: {}", 
                   error.getMessage(), error))
    )
    .chain(v -> {
        log.debug("🔄 Base classifications created, proceeding to security classifications");
        return createSecurityClassifications(session, enterprise, system);
    });
  }


  private Uni<Void> createSecurityClassifications(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
  {
    log.info("🚀 Creating security-related classifications for enterprise: '{}' with session: {}", 
        enterprise.getName(), session.hashCode());
    log.debug("📋 Preparing to create security classifications");

    return service.create(session, DefaultClassifications.Security, system, enterprise.getName())
               .onItem().invoke(result -> {
                   log.debug("✅ Created Security classification with ID: {}", result.getId());
                   logProgress("Classifications System", "Loading Security Classifications...", 1);
               })
               .onFailure().invoke(error -> log.error("❌ Failed to create Security classification: {}", 
                   error.getMessage(), error))
               .flatMap(securityClassification -> {
                   log.debug("📋 Creating SystemIdentity classification under Security");
                   return service.create(session, SystemsClassifications.SystemIdentity, system, DefaultClassifications.Security)
                              .onItem().invoke(result -> log.debug("✅ Created SystemIdentity classification with ID: {}", result.getId()))
                              .onFailure().invoke(error -> log.error("❌ Failed to create SystemIdentity classification: {}", 
                                  error.getMessage(), error));
               })
               .flatMap(v -> {
                   log.debug("📋 Creating SecurityPassword classification under Security");
                   return service.create(session, InvolvedPartyClassifications.SecurityPassword, system, DefaultClassifications.Security)
                              .onItem().invoke(result -> log.debug("✅ Created SecurityPassword classification with ID: {}", result.getId()))
                              .onFailure().invoke(error -> log.error("❌ Failed to create SecurityPassword classification: {}", 
                                  error.getMessage(), error));
               })
               .flatMap(v -> {
                   log.debug("📋 Creating SecurityPasswordSalt classification under Security");
                   return service.create(session, InvolvedPartyClassifications.SecurityPasswordSalt, system, DefaultClassifications.Security)
                              .onItem().invoke(result -> log.debug("✅ Created SecurityPasswordSalt classification with ID: {}", result.getId()))
                              .onFailure().invoke(error -> log.error("❌ Failed to create SecurityPasswordSalt classification: {}", 
                                  error.getMessage(), error));
               })
    .chain(v -> {
        log.debug("🔄 Security classifications created, proceeding to enterprise classifications");
        return createEnterpriseClassifications(session, enterprise, system);
    });
  }


  private Uni<Void> createEnterpriseClassifications(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> system)
  {
    log.info("🚀 Creating enterprise-related classifications for enterprise: '{}' with session: {}", 
        enterprise.getName(), session.hashCode());
    log.debug("📋 Preparing to create enterprise classifications");

    return sessionFactory.withTransaction((newSession, tx) ->
        service.create(newSession, EnterpriseClassifications.LastUpdateDate, system, enterprise.getName())
               .onItem().invoke(result -> log.debug("✅ Created LastUpdateDate classification with ID: {}", result.getId()))
               .onFailure().invoke(error -> log.error("❌ Failed to create LastUpdateDate classification: {}", 
                   error.getMessage(), error))
               .chain(v -> {
                   log.debug("📋 Creating UpdateClass classification");
                   return service.create(newSession, EnterpriseClassifications.UpdateClass, system, enterprise.getName());
               })
               .onItem().invoke(result -> log.debug("✅ Created UpdateClass classification with ID: {}", result.getId()))
               .onFailure().invoke(error -> log.error("❌ Failed to create UpdateClass classification: {}", 
                   error.getMessage(), error))
               .chain(v -> {
                   log.debug("📋 Creating EnterpriseIdentity classification");
                   return service.create(newSession, EnterpriseClassifications.EnterpriseIdentity, system, enterprise.getName());
               })
               .onItem().invoke(result -> {
                   log.debug("✅ Created EnterpriseIdentity classification with ID: {}", result.getId());
                   log.info("🎉 All enterprise classifications created successfully for enterprise: '{}'", enterprise.getName());
               })
               .onFailure().invoke(error -> log.error("❌ Failed to create EnterpriseIdentity classification: {}", 
                   error.getMessage(), error))
    )
    .replaceWithVoid();
  }


  @Override
  public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Starting reactive postStartup for Classifications System with session: {}", session.hashCode());
    log.debug("📋 Preparing to verify system and security token for enterprise: '{}'", enterprise.getName());

    // Create a reactive chain for the postStartup operations
    // Get the system
    return systemsService.findSystem(session, enterprise, getSystemName())
               .onItem().invoke(system -> {
                   if (system != null) {
                       log.debug("✅ Found system: '{}' with ID: {}", system.getName(), system.getId());
                   }
               })
               .onFailure().invoke(error -> log.error("❌ Error finding system '{}': {}", 
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
                            .onItem().invoke(token -> {
                                if (token != null) {
                                    log.debug("✅ Found security token for system: '{}'", system.getName());
                                }
                            })
                            .onFailure().invoke(error -> log.error("❌ Error finding security token for system '{}': {}", 
                                system.getName(), error.getMessage(), error))
                            .onItem()
                            .ifNull()
                            .failWith(() -> {
                                log.error("❌ Security token not found for system: '{}'", system.getName());
                                return new RuntimeException("Security token not found for system: " + system.getName());
                            })
                            .map(token -> {
                              log.info("🎉 Post-startup verification completed successfully for Classifications System");
                              return null; // Return Void
                            });
               })
               .replaceWith(Uni.createFrom()
                                .voidItem());
  }

  @Override
  public int totalTasks()
  {
    return 2;
  }

  @Override
  public String getSystemName()
  {
    return ClassificationSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for handling classifications";
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE + 4;
  }
}
