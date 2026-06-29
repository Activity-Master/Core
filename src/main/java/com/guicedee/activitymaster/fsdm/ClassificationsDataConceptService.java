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
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept_;
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

    // Stateless detached-prepped reference cache (data concept), keyed by enterpriseId → name.
    // Safe: detached scalar projection, stable install-time concepts; only cached on a real hit.
    private static final java.util.Map<UUID, java.util.Map<String, IClassificationDataConcept<?, ?>>> STATELESS_CONCEPT_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    @Inject
    private ActiveFlagService activeFlagService;

    @Override
    public IClassificationDataConcept<?, ?> get() {
        return new ClassificationDataConcept();
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
        return new ClassificationDataConcept()
                .builder(session)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .withName(name)
                .get()
                .map(com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept::getId)
                .flatMap(id -> getConceptById(session, id));
    }

    // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
    public Uni<IClassificationDataConcept<?, ?>> getConceptById(Mutiny.Session session, UUID id) {
        //noinspection unchecked
        return (Uni) session.find(ClassificationDataConcept.class, id);
    }

    /**
     * Stateless "fetch ids/scalars + prep" variant of {@link #find(Mutiny.Session, String, ISystems, UUID...)}.
     * {@code ClassificationDataConcept} is {@code @Cacheable} with no eager {@code @ManyToOne}, so projecting
     * its own scalars ({@code id, name, description}) and building a detached instance is stateless-safe.
     */
    public Uni<IClassificationDataConcept<?, ?>> find(Mutiny.StatelessSession session, String name, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        UUID enterpriseId = enterprise.getId();
        java.util.Map<String, IClassificationDataConcept<?, ?>> byName = STATELESS_CONCEPT_CACHE.computeIfAbsent(enterpriseId, k -> new java.util.concurrent.ConcurrentHashMap<>());
        IClassificationDataConcept<?, ?> hit = byName.get(name);
        if (hit != null) {
            return Uni.createFrom().item((IClassificationDataConcept<?, ?>) hit);
        }
        Uni<IClassificationDataConcept<?, ?>> resolved = new ClassificationDataConcept().builder(session)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .withName(name)
                .selectColumn(ClassificationDataConcept_.id)
                .selectColumn(ClassificationDataConcept_.name)
                .selectColumn(ClassificationDataConcept_.description)
                .get(Object[].class)
                .map(row -> {
                    ClassificationDataConcept prepped = new ClassificationDataConcept(
                            (UUID) row[0], (String) row[1], (String) row[2], null);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return (IClassificationDataConcept<?, ?>) prepped;
                });
        return resolved.onItem().invoke(t -> { if (t != null && t.getId() != null) byName.put(name, t); });
    }

    @Override
    //@CacheResult(cacheName = "NoDataConcept")
    public Uni<IClassificationDataConcept<?, ?>> getNoConcept(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return find(session, NoClassificationDataConceptName, system, identityToken);
    }

    /**
     * Stateless find-or-create of a {@code ClassificationDataConcept} — prepped existence check, else a
     * lean insert + the stateless default-security matrix, all on the {@link Mutiny.StatelessSession}.
     */
    public Uni<IClassificationDataConcept<?, ?>> createDataConcept(Mutiny.StatelessSession session,
                                                                   EnterpriseClassificationDataConcepts name,
                                                                   String description, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return find(session, name.classificationValue(), system, identityToken)
                .onFailure()
                .recoverWithUni(err -> {
                    ClassificationDataConcept newConcept = new ClassificationDataConcept();
                    newConcept.setDescription(description);
                    newConcept.setName(name.classificationValue());
                    newConcept.setSystemID(system);
                    newConcept.setOriginalSourceSystemID(system.getId());
                    newConcept.setEnterpriseID(enterprise);
                    com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService<?> sts =
                            com.guicedee.client.IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);
                    return activeFlagService.getActiveFlag(session, enterprise, identityToken)
                            .chain(activeFlag -> {
                                newConcept.setActiveFlagID(activeFlag);
                                return newConcept.builder(session).persist(newConcept)
                                        .chain(persisted -> sts.resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                .chain(tokens -> newConcept.createDefaultSecurity(session, system, enterprise, activeFlag, tokens))
                                                .onFailure().recoverWithItem(0L)
                                                .replaceWith((IClassificationDataConcept<?, ?>) newConcept));
                            });
                });
    }

    @Override
    //@CacheResult(cacheName = "SecurityHierarchyConcept")
    public Uni<IClassificationDataConcept<?, ?>> getSecurityHierarchyConcept(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return find(session, EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, identityToken);
    }
}
