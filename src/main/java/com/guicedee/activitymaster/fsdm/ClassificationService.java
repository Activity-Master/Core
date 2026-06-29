package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * <p>
 * [✓] One action per Mutiny.Session at a time
 * - All operations on a session are sequential
 * - No parallel operations on the same session
 * <p>
 * [✓] Pass Mutiny.Session through the chain
 * - All methods accept session as parameter
 * - Session is passed to all dependent operations
 * <p>
 * [✓] No await() usage
 * - Using reactive chains instead of blocking operations
 * <p>
 * [✓] Synchronous execution of reactive chains
 * - All reactive chains execute synchronously
 * - createDefaultSecurity is properly chained with error handling
 * <p>
 * [✓] No parallel operations on a session
 * - Not using Uni.combine().all().unis() with operations that share the same session
 * <p>
 * [✓] No session/transaction creation in libraries
 * - Sessions are passed in from the caller
 * - No sessionFactory.withTransaction() in methods
 * <p>
 * See ReactivityMigrationGuide.md for more details on these rules.
 */

import com.entityassist.enumerations.Operand;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification_;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.HierarchyTypeClassification;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications.Identity;


@Log4j2
@Singleton
public class ClassificationService
        implements IClassificationService<ClassificationService> {

    /**
     * Cache of detached, immutable reference-type classifications (Identity, HierarchyType,
     * NoClassification) resolved on a stateless session, keyed by systemId then classification name.
     * Safe because stateless {@code find} returns a fresh DETACHED Classification (scalar projection, no
     * persistence context) and these bootstrap types never change for the JVM lifetime. Mutable,
     * user-created classifications are intentionally NOT cached (they can be created/archived/updated at
     * runtime), so only these stable reference types are cached.
     */
    private static final Map<UUID, Map<String, IClassification<?, ?>>> REFERENCE_TYPE_CACHE = new ConcurrentHashMap<>();

    @Inject
    private ClassificationsDataConceptService dataConceptService;

    public IClassification<?, ?> get() {
        return new Classification();
    }

    @Override
    public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts concept,
                                             ISystems<?, ?> system, Integer sequenceOrder, String parentName, UUID... identityToken) {
        return find(session, parentName, system, identityToken)
                .chain(classification -> create(session, name, description, concept, system, sequenceOrder, classification, identityToken));
    }

    @Override
    public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name,
                                             ISystems<?, ?> system, UUID... identityToken) {
        return create(session, name, name, null, system, 0, identityToken);
    }

    @Override
    public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description,
                                             ISystems<?, ?> system, UUID... identityToken) {
        return create(session, name, description, null, system, 0, identityToken);
    }

    @Override
    public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
                                             ISystems<?, ?> system, UUID... identityToken) {
        return create(session, name, description, conceptName, system, 0, identityToken);
    }

    @Override
    public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
                                             ISystems<?, ?> system,
                                             Integer sequenceNumber, UUID... identityToken) {
        return create(session, name, description, conceptName, system, sequenceNumber, (IClassification<?, ?>) null, identityToken);
    }

    @Override
    
    public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
                                             ISystems<?, ?> system,
                                             Integer sequenceNumber, IClassification<?, ?> parent, UUID... identityToken) {
        // Public create → world-readable (public/default security matrix).
        return createWithSecurity(session, name, description, conceptName, system, sequenceNumber, parent,
                rootCl -> rootCl.createDefaultSecurity(session, system, identityToken), identityToken);
    }

    /**
     * Opt-in <strong>scope-restricted</strong> classification create. Identical to
     * {@link #create(Mutiny.Session, String, String, EnterpriseClassificationDataConcepts, ISystems, Integer, IClassification, UUID...)}
     * except the classification is secured with the <em>restricted</em> matrix (NOT world-readable): only
     * Administrators / Systems / Applications / Plugins retain access, plus a <em>read</em> grant for
     * {@code scopeToken}. Because the applicable-token climb is child&rarr;parent, only identity tokens at
     * that scope node <em>or below it</em> may then read the classification.
     */
    @Override
    public Uni<IClassification<?, ?>> createScopeRestricted(Mutiny.Session session, String name, String description,
                                                            EnterpriseClassificationDataConcepts conceptName, ISystems<?, ?> system,
                                                            Integer sequenceNumber, IClassification<?, ?> parent,
                                                            com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                            UUID... identityToken) {
        return createWithSecurity(session, name, description, conceptName, system, sequenceNumber, parent,
                rootCl -> rootCl.createScopeRestrictedSecurity(session, system, scopeToken, identityToken), identityToken);
    }

    private Uni<IClassification<?, ?>> createWithSecurity(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
                                             ISystems<?, ?> system,
                                             Integer sequenceNumber, IClassification<?, ?> parent,
                                             java.util.function.Function<Classification, Uni<?>> securityFn, UUID... identityToken) {

        log.trace("🚀 Creating new classification: '{}' for system: '{}' with session: {}", name, system.getName(), session.hashCode());

        var enterprise = system.getEnterprise();

        log.trace("📝 Classification details - Name: '{}', Description: '{}', System ID: {}, Session: {}",
                name, description, system.getId(), session.hashCode());

        Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>> dataConceptUni;
        if (conceptName != null) {
            log.trace("📋 Finding data concept: '{}' for system: '{}' with session: {}", conceptName, system.getName(), session.hashCode());
            dataConceptUni = dataConceptService.find(session, conceptName, system, identityToken);
        } else {
            log.trace("📋 Finding default 'NoClassification' data concept for system: '{}' with session: {}", system.getName(), session.hashCode());
            dataConceptUni = dataConceptService.find(session, "NoClassification", system, identityToken);
        }

        log.trace("🔍 Checking if classification '{}' already exists with session: {}", name, session.hashCode());
        return find(session, name, conceptName, system, identityToken)
                .onItem()
                .invoke(existing ->
                        log.trace("✅ Found existing classification: '{}' with ID: {}", existing.getName(), existing.getId())
                )
                .onFailure()
                .recoverWithUni(error -> {
                    log.info("📋 Classification '{}' not found, creating new one", name);

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
                                if (dataConcept instanceof ClassificationDataConcept cdc) {
                                    rootCl.setConcept(cdc);
                                    log.debug("🔗 Linked data concept to classification '{}'", name);
                                } else {
                                    log.warn("⚠️ DataConcept is not an instance of ClassificationDataConcept: {}",
                                            dataConcept.getClass()
                                                    .getName());
                                }

                                log.trace("📋 Retrieving active flag for enterprise: '{}' with session: {}", enterprise.getName(), session.hashCode());
                                return acService.getActiveFlag(session, enterprise, identityToken);
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
                                return securityFn.apply(rootCl)
                                        .onItem()
                                        .invoke(result -> log.trace("🛡️ Security setup completed successfully for classification '{}'", name))
                                        .onFailure()
                                        .recoverWithItem(securityError -> {
                                            log.warn("⚠️ Security creation failed for classification '{}': {}", name, securityError.getMessage());
                                            return null;
                                        })
                                        .chain(securityResult -> {
                                            if (parent != null && !NoClassification.toString()
                                                    .equals(name)) {
                                                log.trace("👶 Setting up parent-child relationship for classification '{}' with parent '{}'", name, parent.getName());
                                                return find(session, parent.getName(), system, identityToken)
                                                        .onFailure()
                                                        .recoverWithItem(e -> {
                                                            log.warn("⚠️ Error finding parent classification '{}': {}", parent.getName(), e.getMessage());
                                                            return null;
                                                        })
                                                        .chain(foundParent -> {
                                                            if (foundParent != null) {
                                                                log.trace("✅ Found parent classification: '{}'", parent.getName());
                                                                try {
                                                                    @SuppressWarnings("unchecked")
                                                                    IClassification<Classification, ClassificationQueryBuilder> pp =
                                                                            (IClassification<Classification, ClassificationQueryBuilder>) foundParent;
                                                                    return pp.addChild(session, rootCl, NoClassification.toString(), null, system, identityToken)
                                                                            .onItem().invoke(v -> log.trace("🔗 Added classification '{}' as child to parent '{}'", name, parent.getName()))
                                                                            .onFailure().invoke(e -> log.warn("⚠️ Error adding child to parent: {}", e.getMessage(), e))
                                                                            .replaceWith((IClassification<?, ?>) rootCl);
                                                                } catch (Exception e) {
                                                                    log.warn("⚠️ Error adding child to parent: {}", e.getMessage(), e);
                                                                }
                                                            }
                                                            log.info("🎉 Classification '{}' creation completed successfully", name);
                                                            return Uni.createFrom()
                                                                    .item((IClassification<?, ?>) rootCl);
                                                        });
                                            } else {
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
    public Uni<IClassification<?, ?>> find(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken) {
        log.trace("🔍 Finding classification '{}' for system: '{}' with session: {}",
                name, system.getName(), session.hashCode());
        return find(session, name, null, system, identityToken);
    }

    
    //@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
    @Override
    @SuppressWarnings("unchecked")
    public Uni<IClassification<?, ?>> find(Mutiny.Session session, String name, EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system, UUID... identityToken) {
        log.trace("🔍 Finding classification '{}' with concept: '{}' for system: '{}' with session: {}",
                name, concept != null ? concept : "null", system.getName(), session.hashCode());

        var enterprise = system.getEnterprise();

        if (concept != null) {
            return dataConceptService.find(session, concept, system, identityToken)
                    .chain(dc -> (Uni) new Classification()
                            .builder(session)
                            .withEnterprise(enterprise)
                            .withName(name)
                            .where(Classification_.concept, Operand.Equals, (ClassificationDataConcept) dc)
                            .inActiveRange()
                            .inDateRange()
                            .get());
        } else {
            return (Uni) new Classification()
                    .builder(session)
                    .withEnterprise(enterprise)
                    .withName(name)
                    .inActiveRange()
                    .inDateRange()
                    .get();
        }
    }

    @Override
    public Uni<IClassification<?, ?>> getHierarchyType(Mutiny.Session session, ISystems<?, ?> system, UUID...
            identityToken) {
        log.trace("🔍 Getting hierarchy type classification for system: '{}' with session: {}",
                system.getName(), session.hashCode());
        return find(session,
                HierarchyTypeClassification.toString(),
                system, identityToken)
                .onItem()
                .invoke(result -> {
                    if (result != null) {
                        log.trace("✅ Found hierarchy type classification with ID: {}", result.getId());
                    } else {
                        log.warn("⚠️ Hierarchy type classification not found");
                    }
                })
                .onFailure()
                .invoke(error ->
                        log.error("❌ Error finding hierarchy type classification: {}", error.getMessage(), error));
    }

    @Override
    public Uni<IClassification<?, ?>> getNoClassification(Mutiny.Session session, ISystems<?, ?> system, UUID...
            identityToken) {
        log.trace("🔍 Getting 'NoClassification' for system: '{}' with session: {}",
                system.getName(), session.hashCode());
        return find(session,
                NoClassification.toString(),
                system, identityToken)
                .onItem()
                .invoke(result -> {
                    if (result != null) {
                        log.trace("✅ Found 'NoClassification' with ID: {}", result.getId());
                    } else {
                        log.debug("⚠️ 'NoClassification' not found");
                    }
                })
                .onFailure()
                .invoke(error ->
                        log.error("❌ Error finding 'NoClassification': {}", error.getMessage(), error));
    }

    
    //@CacheResult(cacheName = "IdentityTypeClassification")
    @Override
    public Uni<IClassification<?, ?>> getIdentityType(Mutiny.Session session, ISystems<?, ?> system, UUID...
            identityToken) {
        log.trace("🔍 Getting identity type classification for system: '{}' with session: {}",
                system.getName(), session.hashCode());
        return find(session,
                Identity.name(),
                system, identityToken)
                .onItem()
                .invoke(result -> {
                    if (result != null) {
                        log.trace("✅ Found identity type classification with ID: {}", result.getId());
                    } else {
                        log.debug("⚠️ Identity type classification not found");
                    }
                })
                .onFailure()
                .invoke(error ->
                        log.error("❌ Error finding identity type classification: {}", error.getMessage(), error));
    }

    // ---------------------------------------------------------------------------------------------
    // Stateless "fetch ids/scalars + prep" resolvers. Classification is @Cacheable with an eager
    // @ManyToOne concept, so the managed entity cannot be hydrated on a Mutiny.StatelessSession.
    // These project the row's OWN scalar columns (id, name, description, classificationSequenceNumber)
    // — a scalar multiselect, never an entity result — and build a fresh DETACHED Classification from
    // its 4-arg constructor, wiring the enterprise reference from system.getEnterprise() (already in
    // hand). The eager concept association is intentionally left null.
    // ---------------------------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IClassification<?, ?>> find(Mutiny.StatelessSession session, String name, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return new Classification()
                .builder(session)
                .withEnterprise(enterprise)
                .withName(name)
                .inActiveRange()
                .inDateRange()
                .selectColumn(Classification_.id)
                .selectColumn(Classification_.name)
                .selectColumn(Classification_.description)
                .selectColumn(Classification_.classificationSequenceNumber)
                .get(Object[].class)
                .map(row -> {
                    Classification prepped = new Classification(
                            (UUID) row[0],
                            (String) row[1],
                            (String) row[2],
                            ((Number) row[3]).intValue());
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return (IClassification<?, ?>) prepped;
                });
    }

    @Override
    public Uni<IClassification<?, ?>> getHierarchyType(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findReferenceType(session, HierarchyTypeClassification.toString(), system, identityToken);
    }

    @Override
    public Uni<IClassification<?, ?>> getNoClassification(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findReferenceType(session, NoClassification.toString(), system, identityToken);
    }

    @Override
    public Uni<IClassification<?, ?>> getIdentityType(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findReferenceType(session, Identity.name(), system, identityToken);
    }

    /**
     * Cached stateless resolve for the immutable reference-type classifications. Reuses the detached
     * prepped {@code find} result keyed by systemId → name; only invoked for stable bootstrap types.
     */
    private Uni<IClassification<?, ?>> findReferenceType(Mutiny.StatelessSession session, String name, ISystems<?, ?> system, UUID... identityToken) {
        UUID systemId = system.getId();
        Map<String, IClassification<?, ?>> byName = REFERENCE_TYPE_CACHE.computeIfAbsent(systemId, k -> new ConcurrentHashMap<>());
        IClassification<?, ?> cached = byName.get(name);
        if (cached != null) {
            return Uni.createFrom().item(cached);
        }
        return find(session, name, system, identityToken)
                .onItem().invoke(cl -> {
                    if (cl != null && cl.getId() != null) {
                        byName.put(name, cl);
                    }
                });
    }

    /**
     * Stateless, end-to-end classification create — the write counterpart that lets a system's
     * {@code createDefaults} run entirely on a {@link Mutiny.StatelessSession}. Idempotent: returns the
     * existing (prepped) classification if present; otherwise inserts a new row and provisions its default
     * security, all on the supplied stateless session. It composes the stateless building blocks:
     * <ul>
     *   <li>prepped {@link #find(Mutiny.StatelessSession, String, ISystems, UUID...)} existence check;</li>
     *   <li>prepped data-concept + active-flag FK references (no eager-association hydration);</li>
     *   <li>a stateless {@code insert} of the lean classification row;</li>
     *   <li>the stateless default-security matrix via
     *       {@link ISecurityTokenService#resolveDefaultGroupFolderTokens(Mutiny.StatelessSession, ISystems, UUID...)}
     *       + {@code createDefaultSecurity(Mutiny.StatelessSession, …)}.</li>
     * </ul>
     */
    @Override
    public Uni<IClassification<?, ?>> create(Mutiny.StatelessSession session, String name, String description,
                                             ISystems<?, ?> system, UUID... identityToken) {
        return create(session, name, description, (EnterpriseClassificationDataConcepts) null, system, (IClassification<?, ?>) null, identityToken);
    }

    @Override
    public Uni<IClassification<?, ?>> create(Mutiny.StatelessSession session, String name, String description,
                                             ISystems<?, ?> system, IClassification<?, ?> parent, UUID... identityToken) {
        return create(session, name, description, (EnterpriseClassificationDataConcepts) null, system, parent, identityToken);
    }

    @Override
    public Uni<IClassification<?, ?>> create(Mutiny.StatelessSession session, String name, String description,
                                             EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system, UUID... identityToken) {
        return create(session, name, description, concept, system, (IClassification<?, ?>) null, identityToken);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IClassification<?, ?>> create(Mutiny.StatelessSession session, String name, String description,
                                             EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system,
                                             IClassification<?, ?> parent, UUID... identityToken) {
        return create(session, name, description, concept, system, (Integer) null, parent, identityToken);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IClassification<?, ?>> create(Mutiny.StatelessSession session, String name, String description,
                                             EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system,
                                             Integer sequenceNumber, IClassification<?, ?> parent, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        // Resolve the data-concept name to project/prep: the supplied concept, or the default "NoClassification".
        String conceptName = concept != null ? concept.classificationValue() : "NoClassification";
        return find(session, name, system, identityToken)
                .onFailure()
                .recoverWithUni(err -> {
                    Classification rootCl = new Classification();
                    rootCl.setName(name);
                    rootCl.setDescription(description);
                    rootCl.setClassificationSequenceNumber(sequenceNumber == null ? 1 : sequenceNumber);
                    rootCl.setSystemID(system);
                    rootCl.setOriginalSourceSystemID(system.getId());
                    rootCl.setOriginalSourceSystemUniqueID(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                    rootCl.setEnterpriseID(enterprise);

                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                    ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);

                    return dataConceptService.find(session, conceptName, system, identityToken)
                            .chain(dc -> {
                                if (dc instanceof ClassificationDataConcept cdc) {
                                    rootCl.setConcept(cdc);
                                }
                                return acService.getActiveFlag(session, enterprise, identityToken);
                            })
                            .chain(activeFlag -> {
                                rootCl.setActiveFlagID((ActiveFlag) activeFlag);
                                return rootCl.builder(session)
                                        .persist(rootCl)
                                        .chain(persisted -> sts.resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                .chain(tokens -> rootCl.createDefaultSecurity(session, system, enterprise, activeFlag, tokens))
                                                .onFailure()
                                                .recoverWithItem(0L)
                                                .replaceWith((IClassification<?, ?>) rootCl));
                            });
                })
                // Link to the parent hierarchy (idempotent) when a parent is supplied — stateless addChild.
                .chain(cl -> {
                    if (parent == null) {
                        return Uni.createFrom().item(cl);
                    }
                    IClassification<Classification, ClassificationQueryBuilder> pp =
                            (IClassification<Classification, ClassificationQueryBuilder>) parent;
                    return pp.addChild(session, (Classification) cl, NoClassification.toString(), null, system, identityToken)
                            .replaceWith(cl);
                });
    }
}
