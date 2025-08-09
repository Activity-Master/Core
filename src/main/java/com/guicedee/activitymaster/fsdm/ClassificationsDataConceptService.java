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
 *     - No fire-and-forget operations with subscribe().with()
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

//import com.google.inject.persist.Transactional;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;

@Log4j2
@Singleton
public class ClassificationsDataConceptService
    implements IClassificationDataConceptService<ClassificationsDataConceptService>
{

  @Override
  public IClassificationDataConcept<?, ?> get()
  {
    return new ClassificationDataConcept();
  }

  //@Transactional()
  public Uni<IClassificationDataConcept<?, ?>> createDataConcept(Mutiny.Session session, EnterpriseClassificationDataConcepts name,
                                                                 String description,
                                                                 ISystems<?, ?> system,
                                                                 UUID... identityToken)
  {
    ClassificationDataConcept newConcept = new ClassificationDataConcept();
    var enterprise = system.getEnterprise();
    return find(session, name, system, identityToken)
               .onFailure(NoResultException.class)
               .recoverWithUni(existingConcept -> {
                 newConcept.setDescription(description);
                 newConcept.setName(name.classificationValue());
                 newConcept.setSystemID(system);
                 newConcept.setOriginalSourceSystemID(system.getId());
                 newConcept.setOriginalSourceSystemUniqueID(null);

                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                 return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                              newConcept.setActiveFlagID(activeFlag);
                              newConcept.setEnterpriseID(enterprise);
                              return session.persist(newConcept)
                                         .replaceWith(Uni.createFrom()
                                                          .item(newConcept))
                                         .chain(persisted -> {
                                           log.debug("🔐 Starting security setup for classification data concept: '{}'", persisted.getName());
                                           // Chain the security setup operation properly
                                           return persisted.createDefaultSecurity(session, system, identityToken)
                                               .onItem()
                                               .invoke(() -> log.debug("✅ Security setup completed successfully for: '{}'", persisted.getName()))
                                               .onFailure()
                                               .invoke(error -> log.warn("⚠️ Error in createDefaultSecurity for '{}': {}", persisted.getName(), error.getMessage(), error))
                                               .chain(() -> Uni.createFrom().item((IClassificationDataConcept<?, ?>) persisted)); // Continue with persisted object regardless of security setup outcome
                                         });
                            });

               });
  }

  @Override
  //@CacheResult(cacheName = "GetGlobalConcept")
  public Uni<IClassificationDataConcept<?, ?>> getGlobalConcept(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    return find(session, GlobalClassificationsDataConceptName, system, identityToken);
  }

  @Override
  //@CacheResult(cacheName = "FindConceptWithConceptValueAndSystem")
  public Uni<IClassificationDataConcept<?, ?>> find(Mutiny.Session session, EnterpriseClassificationDataConcepts name, ISystems<?, ?> system, UUID... identityToken)
  {
    return find(session, name.classificationValue(), system, identityToken);
  }

  //@Transactional()
  //@CacheResult(cacheName = "FindConceptWithConceptValueAndSystemString")
  @SuppressWarnings("unchecked")
  public Uni<IClassificationDataConcept<?, ?>> find(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken)
  {
    ClassificationDataConcept cdc = new ClassificationDataConcept();
    var enterprise = system.getEnterprise();
    return (Uni) cdc.builder(session)
                     .withName(name)
                     .withEnterprise(enterprise)
                     .inActiveRange()
                     .inDateRange()
                     .get();
  }

  @Override
  //@CacheResult(cacheName = "NoDataConcept")
  public Uni<IClassificationDataConcept<?, ?>> getNoConcept(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    return find(session, NoClassificationDataConceptName, system, identityToken);
  }

  @Override
  //@CacheResult(cacheName = "SecurityHierarchyConcept")
  public Uni<IClassificationDataConcept<?, ?>> getSecurityHierarchyConcept(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
  {
    return find(session, EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, identityToken);
  }
}
