package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * 
 * [✓] One action per Mutiny.Session at a time
 *     - All operations on a session are sequential
 *     - No parallel operations on the same session
 * 
 * [✓] Pass Mutiny.Session through the chain
 *     - All methods accept session as parameter
 *     - Session is passed to all dependent operations
 * 
 * [✓] No await() usage
 *     - Using reactive chains instead of blocking operations
 * 
 * [✓] Synchronous execution of reactive chains
 *     - All reactive chains execute synchronously
 *     - createDefaultSecurity is properly chained with error handling
 * 
 * [✓] No parallel operations on a session
 *     - Not using Uni.combine().all().unis() with operations that share the same session
 * 
 * [✓] No session/transaction creation in libraries
 *     - Sessions are passed in from the caller
 *     - No sessionFactory.withTransaction() in methods
 * 
 * See ReactivityMigrationGuide.md for more details on these rules.
 */

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ClassificationException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications.*;


@Log4j2
@Singleton
public class ClassificationService
    implements IClassificationService<ClassificationService>
{

  @Inject
  private ClassificationsDataConceptService dataConceptService;
  
  // ActiveFlag service is retrieved via Guice context when needed to keep interface lean

  // Local cache: key = enterpriseId + '|' + systemId + '|' + classificationName, value = Classification UUID
  private final Map<String, UUID> classificationKeyToId = new ConcurrentHashMap<>();

  public IClassification<?, ?> get()
  {
    return new Classification();
  }

  @Override
  public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts concept,
                                           ISystems<?, ?> system, Integer sequenceOrder, String parentName, UUID... identityToken)
  {
    return find(session, parentName, system, identityToken)
               .chain(classification -> create(session, name, description, concept, system, sequenceOrder, classification, identityToken));
  }

  @Override
  public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name,
                                           ISystems<?, ?> system, UUID... identityToken)
  {
    return create(session, name, name, null, system, 0, identityToken);
  }

  @Override
  public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description,
                                           ISystems<?, ?> system, UUID... identityToken)
  {
    return create(session, name, description, null, system, 0, identityToken);
  }

  @Override
  public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
                                           ISystems<?, ?> system, UUID... identityToken)
  {
    return create(session, name, description, conceptName, system, 0, identityToken);
  }

  @Override
  public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
                                           ISystems<?, ?> system,
                                           Integer sequenceNumber, UUID... identityToken)
  {
    return create(session, name, description, conceptName, system, sequenceNumber, (IClassification<?, ?>) null, identityToken);
  }

  @Override
  //@Transactional()
  public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
                                           ISystems<?, ?> system,
                                           Integer sequenceNumber, IClassification<?, ?> parent, UUID... identityToken)
  {
    var sessionFactory = IGuiceContext.get(Mutiny.SessionFactory.class);

    log.trace("🚀 Creating new classification: '{}' for system: '{}' with external session", name, system.getName());
    log.trace("📝 Classification details - Name: '{}', Description: '{}', System ID: {}, Session: {}",
        name, description, system.getId(), session.hashCode());

    Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>> dataConceptUni;
    if (conceptName != null)
    {
      log.trace("📋 Finding data concept: '{}' for system: '{}' with session: {}", conceptName, system.getName(), session.hashCode());
      dataConceptUni = dataConceptService.find(session, conceptName, system, identityToken);
    }
    else
    {
      log.trace("📋 Finding default 'NoClassification' data concept for system: '{}' with session: {}", system.getName(), session.hashCode());
      dataConceptUni = dataConceptService.find(session, "NoClassification", system, identityToken);
    }

    var enterprise = system.getEnterprise();

    log.trace("🔍 Checking if classification '{}' already exists with session: {}", name, session.hashCode());
    return find(session, name, conceptName, system, identityToken)
               .onItem()
               .invoke(existing ->
                           log.trace("✅ Found existing classification: '{}' with ID: {}", existing.getName(), existing.getId())
               )
               .onFailure()
               .recoverWithUni(error -> {
                 log.info("📋 Classification '{}' not found, creating new one", name);
                 log.trace("🏛️ Opening new session and transaction for classification creation");

                 log.trace("📋 Preparing classification entity with session: {}", session.hashCode());

                 Classification rootCl = new Classification();
                 rootCl.setName(name);
                 rootCl.setDescription(description);
                 rootCl.setClassificationSequenceNumber(sequenceNumber == null ? 1 : sequenceNumber);
                 rootCl.setSystemID(system);
                 rootCl.setOriginalSourceSystemID(system.getId());
                 rootCl.setOriginalSourceSystemUniqueID(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                 rootCl.setEnterpriseID(enterprise);

                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

                 return dataConceptUni
                            .onItem()
                            .invoke(dataConcept -> log.trace("✅ Data concept retrieved for classification '{}'", name))
                            .onFailure()
                            .invoke(dataConceptError ->
                                        log.error("❌ Failed to retrieve data concept for classification '{}': {}", name, dataConceptError.getMessage(), dataConceptError)
                            )
                            .chain(dataConcept -> {
                              if (dataConcept instanceof ClassificationDataConcept cdc)
                              {
                                rootCl.setConcept(cdc);
                                log.debug("🔗 Linked data concept to classification '{}'", name);
                              }
                              else
                              {
                                log.warn("⚠️ DataConcept is not an instance of ClassificationDataConcept: {}",
                                    dataConcept.getClass()
                                        .getName());
                              }

                              log.trace("📋 Retrieving active flag for enterprise: '{}' with session: {}", enterprise.getName(), session.hashCode());
                              return acService.getActiveFlag(session, enterprise);
                            })
                            .onItem()
                            .invoke(activeFlag -> log.trace("✅ Active flag retrieved: {}", activeFlag.getId()))
                            .onFailure()
                            .invoke(activeFlagError ->
                                        log.error("❌ Failed to retrieve active flag for enterprise '{}': {}", enterprise.getName(), activeFlagError.getMessage(), activeFlagError)
                            )
                            .chain(activeFlag -> {
                              rootCl.setActiveFlagID(activeFlag);
                              log.trace("🔗 Linked active flag to classification '{}'", name);
                              log.trace("💾 Persisting classification '{}' to database using session: {}", name, session.hashCode());
                              return rootCl.builder(session)
                                         .persist(rootCl);
                            })
                            .onItem()
                            .invoke(persisted ->
                                        log.trace("✅ Classification '{}' successfully persisted with ID: {}", name, persisted.getId())
                            )
                            .onFailure()
                            .invoke(persistError ->
                                        log.error("❌ Failed to persist classification '{}': {}", name, persistError.getMessage(), persistError)
                            )
                            .chain(persisted -> {
                              log.trace("🔐 Starting security creation for classification '{}'", name);
                              // Use chain instead of await for reactive flow
                              return rootCl.createDefaultSecurity(session, system, identityToken)
                                         .onItem()
                                         .invoke(result -> log.trace("🛡️ Security setup completed successfully for classification '{}'", name))
                                         .onFailure()
                                         .recoverWithItem(securityError -> {
                                           log.warn("⚠️ Security creation failed for classification '{}': {}", name, securityError.getMessage());
                                           return null; // Continue the chain even if security creation fails
                                         })
                                         .chain(securityResult -> {
                                           // Handle parent-child relationship if needed
                                           if (parent != null && !NoClassification.toString()
                                                                      .equals(name))
                                           {
                                             log.trace("👶 Setting up parent-child relationship for classification '{}' with parent '{}'", name, parent.getName());
                                             return find(session, parent.getName(), system, identityToken)
                                                        .onFailure()
                                                        .recoverWithItem(e -> {
                                                          log.warn("⚠️ Error finding parent classification '{}': {}", parent.getName(), e.getMessage());
                                                          return null;
                                                        })
                                                        .chain(foundParent -> {
                                                          if (foundParent != null)
                                                          {
                                                            log.trace("✅ Found parent classification: '{}'", parent.getName());
                                                            try
                                                            {
                                                              @SuppressWarnings("unchecked")
                                                              IClassification<Classification, ClassificationQueryBuilder> pp =
                                                                  (IClassification<Classification, ClassificationQueryBuilder>) foundParent;
                                                              // Ensure reactive addChild is chained so the link is created before we complete
                                                              return pp.addChild(session, rootCl, NoClassification.toString(), null, system, identityToken)
                                                                      .onItem().invoke(v -> log.trace("🔗 Added classification '{}' as child to parent '{}'", name, parent.getName()))
                                                                      .onFailure().invoke(e -> log.warn("⚠️ Error adding child to parent: {}", e.getMessage(), e))
                                                                      .replaceWith((IClassification<?, ?>) rootCl);
                                                            }
                                                            catch (Exception e)
                                                            {
                                                              log.warn("⚠️ Error adding child to parent: {}", e.getMessage(), e);
                                                            }
                                                          }
                                                          log.info("🎉 Classification '{}' creation completed successfully", name);
                                                          return Uni.createFrom()
                                                                     .item((IClassification<?, ?>) rootCl);
                                                        });
                                           }
                                           else
                                           {
                                             log.info("🎉 Classification '{}' creation completed successfully", name);
                                             return Uni.createFrom()
                                                        .item((IClassification<?, ?>) rootCl);
                                           }
                                         });
                            });
               });
  }


  //@CacheResult(cacheName = "ClassificationFindWithSimpleString")
  @Override
  public Uni<IClassification<?, ?>> find(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken)
  {
    log.trace("🔍 Finding classification '{}' for system: '{}' with session: {}",
        name, system.getName(), session.hashCode());
    return find(session, name, null, system, identityToken);
  }

  //@Transactional()
  //@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
  @Override
  @SuppressWarnings("unchecked")
  public Uni<IClassification<?, ?>> find(Mutiny.Session session, String name, EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system, UUID... identityToken)
  {
    log.trace("🔍 Finding classification '{}' with concept: '{}' for system: '{}' with session: {}",
        name, concept != null ? concept : "null", system.getName(), session.hashCode());

    var enterprise = system.getEnterprise();

    UUID enterpriseId = null;
    UUID systemId = null;
    if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
      enterpriseId = ent.getId();
    }
    if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys) {
      systemId = sys.getId();
    }
    final UUID entId = enterpriseId;
    final UUID sysId = systemId;

    // If we have a concept, resolve its ID via CDC resolver; then resolve classification ID; else resolve by enterprise+name
    if (concept != null) {
      return dataConceptService
          .resolveCdcIdByName(session, entId, sysId, concept.classificationValue())
          .flatMap(conceptId -> {
            var afService = IGuiceContext.get(IActiveFlagService.class);
            return afService.getVisibleRangeAndUpIds(session, entId)
                .flatMap(visibleIds -> session.createNativeQuery(
                        "select classificationid from classification.classification " +
                        "where enterpriseid = :ent and classificationdataconceptid = :cdc and classificationname = :name " +
                        "and (effectivefromdate <= current_timestamp) and (effectivetodate > current_timestamp) " +
                        "and activeflagid in (:visible)"
                )
                .setParameter("ent", entId)
               // .setParameter("sys", sysId)
                .setParameter("cdc", conceptId)
                .setParameter("name", name)
                .setParameter("visible", visibleIds)
                .getSingleResult()
                .map(r -> (UUID) r));
          })
          .flatMap(id ->getClassificationById(session, (UUID) id));
    } else {
      var afService = IGuiceContext.get(IActiveFlagService.class);
      return afService.getVisibleRangeAndUpIds(session, entId)
          .flatMap(visibleIds -> session.createNativeQuery(
                  "select classificationid from classification.classification " +
                  "where enterpriseid = :ent and classificationname = :name " +
                  "and (effectivefromdate <= current_timestamp) and (effectivetodate > current_timestamp) " +
                  "and activeflagid in (:visible)"
          )
          .setParameter("ent", entId)
          .setParameter("name", name)
          .setParameter("visible", visibleIds)
          .getSingleResult()
          .map(r -> (UUID) r))
          .flatMap(id -> (Uni) getClassificationById(session, (UUID) id));
    }
  }

  // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
  public Uni<IClassification<?, ?>> getClassificationById(Mutiny.Session session, UUID id) {
    //noinspection unchecked
    return (Uni) session.find(Classification.class, id);
  }

  //@Transactional()
  //@CacheResult(cacheName = "GetHierarchyTypeClassification")
  @Override
  public Uni<IClassification<?, ?>> getHierarchyType(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    log.trace("🔍 Getting hierarchy type classification for system: '{}' with session: {}",
        system.getName(), session.hashCode());
    return find(session,
        HierarchyTypeClassification.toString(),
        system, identityToken)
               .onItem()
               .invoke(result -> {
                 if (result != null)
                 {
                   log.trace("✅ Found hierarchy type classification with ID: {}", result.getId());
                 }
                 else
                 {
                   log.warn("⚠️ Hierarchy type classification not found");
                 }
               })
               .onFailure()
               .invoke(error ->
                           log.error("❌ Error finding hierarchy type classification: {}", error.getMessage(), error));
  }

  //@Transactional()
  //@CacheResult(cacheName = "GetNoClassification")
  @Override
  public Uni<IClassification<?, ?>> getNoClassification(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    log.trace("🔍 Getting 'NoClassification' for system: '{}' with session: {}",
        system.getName(), session.hashCode());
    return find(session,
        NoClassification.toString(),
        system, identityToken)
               .onItem()
               .invoke(result -> {
                 if (result != null)
                 {
                   log.trace("✅ Found 'NoClassification' with ID: {}", result.getId());
                 }
                 else
                 {
                   log.debug("⚠️ 'NoClassification' not found");
                 }
               })
               .onFailure()
               .invoke(error ->
                           log.error("❌ Error finding 'NoClassification': {}", error.getMessage(), error));
  }

  //@Transactional()
  //@CacheResult(cacheName = "IdentityTypeClassification")
  @Override
  public Uni<IClassification<?, ?>> getIdentityType(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    log.trace("🔍 Getting identity type classification for system: '{}' with session: {}",
        system.getName(), session.hashCode());
    return find(session,
        Identity.name(),
        system, identityToken)
               .onItem()
               .invoke(result -> {
                 if (result != null)
                 {
                   log.trace("✅ Found identity type classification with ID: {}", result.getId());
                 }
                 else
                 {
                   log.debug("⚠️ Identity type classification not found");
                 }
               })
               .onFailure()
               .invoke(error ->
                           log.error("❌ Error finding identity type classification: {}", error.getMessage(), error));
  }
}

