package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
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
        implements IClassificationDataConceptService<ClassificationsDataConceptService> {
    // Using shared NameIdCache via IClassificationDataConceptService.resolveCdcIdByName; no separate local cache needed

    @Inject
    private ActiveFlagService activeFlagService;

    @Override
    public IClassificationDataConcept<?, ?> get() {
        return new ClassificationDataConcept();
    }

    public Uni<UUID> resolveCdcIdByName(Mutiny.Session session, IEnterprise<?, ?> enterpriseId, UUID systemId, String conceptName) {
        return com.guicedee.activitymaster.fsdm.client.services.cache.NameIdCache
                .getClassificationDataConceptId(session, enterpriseId.getId(), systemId, conceptName, (sess, name) -> {
                    // Get the visible range IDs from the ActiveFlag service (via Guice context) and query with IN clause
                    return activeFlagService.getVisibleRangeAndUpIds(sess, enterpriseId)
                            .flatMap(visibleIds -> {
                                String sql = "select classificationdataconceptid from classification.classificationdataconcept " +
                                        "where enterpriseid = :ent and classificationdataconceptname = :name " +
                                        "and (effectivefromdate <= current_timestamp) " +
                                        "and (effectivetodate > current_timestamp) " +
                                        "and activeflagid in (:visibleIds)";
                                return sess.createNativeQuery(sql)
                                        .setParameter("ent", enterpriseId)
                                        .setParameter("name", name)
                                        .setParameter("visibleIds", visibleIds)
                                        .getSingleResult()
                                        .map(result -> (UUID) result);
                            });
                });
    }

    @Override
    public Uni<IClassificationDataConcept<?, ?>> createDataConcept(Mutiny.Session session, EnterpriseClassificationDataConcepts name,
                                                                   String description,
                                                                   ISystems<?, ?> system,
                                                                   UUID... identityToken) {
        var enterprise = system.getEnterprise();
        ClassificationDataConcept newConcept = new ClassificationDataConcept();
        return find(session, name, system, identityToken)
                .onFailure(NoResultException.class)
                .recoverWithUni(_ -> {
                    newConcept.setDescription(description);
                    newConcept.setName(name.classificationValue());
                    newConcept.setSystemID(system);
                    newConcept.setOriginalSourceSystemID(system.getId());
                    newConcept.setOriginalSourceSystemUniqueID(null);
                    return activeFlagService.getActiveFlag(session, enterprise, identityToken)
                            .chain(activeFlag -> {
                                newConcept.setActiveFlagID(activeFlag);
                                newConcept.setEnterpriseID(enterprise);
                                return session.persist(newConcept)
                                        .replaceWith(Uni.createFrom()
                                                .item(newConcept))
                                        .chain(persisted -> {
                                            log.debug("🔐 Starting security setup for classification data concept: '{}'", persisted.getName());
                                            return persisted.createDefaultSecurity(session, system, identityToken)
                                                    .onItem()
                                                    .invoke(() -> log.debug("✅ Security setup completed successfully for: '{}'", persisted.getName()))
                                                    .onFailure()
                                                    .invoke(error -> log.warn("⚠️ Error in createDefaultSecurity for '{}': {}", persisted.getName(), error.getMessage(), error))
                                                    .chain(() -> Uni.createFrom().item((IClassificationDataConcept<?, ?>) persisted));
                                        });
                            });

                });
    }

    @Override
    //@CacheResult(cacheName = "GetGlobalConcept")
    public Uni<IClassificationDataConcept<?, ?>> getGlobalConcept(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return find(session, GlobalClassificationsDataConceptName, system, identityToken);
    }

    @Override
    //@CacheResult(cacheName = "FindConceptWithConceptValueAndSystem")
    public Uni<IClassificationDataConcept<?, ?>> find(Mutiny.Session session, EnterpriseClassificationDataConcepts name, ISystems<?, ?> system, UUID... identityToken) {
        return find(session, name.classificationValue(), system, identityToken);
    }

    public Uni<IClassificationDataConcept<?, ?>> find(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        // ID-first resolution using ActiveFlag VisibleRangeAndUp IDs and SCD window, then load by UUID
        return activeFlagService.getVisibleRangeAndUpIds(session, enterprise)
                .flatMap(visibleIds ->
                        new ClassificationDataConcept()
                                .builder(session)
                                .withEnterprise(enterprise)
                                .inActiveRange()
                                .inDateRange()
                                .withName(name)
                                .get()
                                .map(r -> r.getId()))
                .flatMap(id -> getConceptById(session, id));
    }

    // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
    public Uni<IClassificationDataConcept<?, ?>> getConceptById(Mutiny.Session session, UUID id) {
        //noinspection unchecked
        return (Uni) session.find(ClassificationDataConcept.class, id);
    }

    @Override
    //@CacheResult(cacheName = "NoDataConcept")
    public Uni<IClassificationDataConcept<?, ?>> getNoConcept(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return find(session, NoClassificationDataConceptName, system, identityToken);
    }

    @Override
    //@CacheResult(cacheName = "SecurityHierarchyConcept")
    public Uni<IClassificationDataConcept<?, ?>> getSecurityHierarchyConcept(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return find(session, EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, identityToken);
    }
}
