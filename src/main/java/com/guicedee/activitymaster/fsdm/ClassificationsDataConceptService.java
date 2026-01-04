package com.guicedee.activitymaster.fsdm;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;

@Log4j2
@Singleton
public class ClassificationsDataConceptService
    implements IClassificationDataConceptService<ClassificationsDataConceptService>
{
  // Using shared NameIdCache via IClassificationDataConceptService.resolveCdcIdByName; no separate local cache needed

  @Override
  public IClassificationDataConcept<?, ?> get()
  {
    return new ClassificationDataConcept();
  }

  @Override
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
    var enterprise = system.getEnterprise();
    UUID enterpriseId = null;
    UUID systemId = null;
    if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
      enterpriseId = ent.getId();
    }
    if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys) {
      systemId = sys.getId();
    }

    // ID-first resolution using ActiveFlag VisibleRangeAndUp IDs and SCD window, then load by UUID
    IActiveFlagService<?> afService = IGuiceContext.get(IActiveFlagService.class);
    final UUID entId = enterpriseId;
    final UUID sysId = systemId;
    return afService.getVisibleRangeAndUpIds(session, entId)
            .flatMap(visibleIds -> session.createNativeQuery(
                    "select classificationdataconceptid from classification.classificationdataconcept " +
                    "where enterpriseid = :ent and classificationdataconceptname = :name " +
                    "and (effectivefromdate <= current_timestamp) and (effectivetodate > current_timestamp) " +
                    "and activeflagid in (:visible)"
            )
            .setParameter("ent", entId)
            //.setParameter("sys", sysId)
            .setParameter("name", name)
            .setParameter("visible", visibleIds)
            .getSingleResult()
            .map(r -> (UUID) r))
            .flatMap(id -> (Uni) getConceptById(session, id));
  }

  // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
  public Uni<IClassificationDataConcept<?, ?>> getConceptById(Mutiny.Session session, UUID id) {
    //noinspection unchecked
    return (Uni) session.find(ClassificationDataConcept.class, id);
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
