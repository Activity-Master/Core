package com.guicedee.activitymaster.fsdm;

//import com.google.inject.persist.Transactional;

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

                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                 return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                              newConcept.setActiveFlagID(activeFlag);
                              newConcept.setEnterpriseID(enterprise);
                              return session.persist(newConcept)
                                         .replaceWith(Uni.createFrom()
                                                          .item(newConcept))
                                         .map(persisted -> {
                                           // Start createDefaultSecurity in parallel without waiting for it
                                           persisted.createDefaultSecurity(system, identityToken)
                                               .subscribe()
                                               .with(
                                                   result -> {
                                                     // Security setup completed successfully
                                                   },
                                                   error -> {
                                                     // Log error but don't fail the main operation
                                                     log.warn("Error in createDefaultSecurity", error);
                                                   }
                                               );
                                           // Return the persisted concept immediately without waiting for security setup
                                           return (IClassificationDataConcept<?, ?>) persisted;
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
