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
 * - No fire-and-forget operations with subscribe().with()
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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseSCDTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseRelationshipTable_;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems_;
import com.guicedee.activitymaster.fsdm.db.entities.systems.SystemsXClassification;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.ActivateFlagSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.getISystem;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.getISystemToken;
import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.EnterpriseSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications.SystemIdentity;

@SuppressWarnings("unchecked")
@Log4j2
@Singleton
public class SystemsService
        implements ISystemsService<SystemsService> {
    // Local cache: key = enterpriseId + '|' + systemName, value = Systems UUID
    private final Map<String, UUID> systemKeyToId = new ConcurrentHashMap<>();
    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private ISecurityTokenService<?> securityTokenService;

    @Inject
    private IActiveFlagService<?> activeFlagService;

    public ISystems<?, ?> get() {
        return new Systems();
    }

    @Override
    //@CacheResult(cacheName = "GetActivityMaster")
    public Uni<ISystems<?, ?>> getActivityMaster(Mutiny.Session session, ISystems<?, ?> requestingSystem, UUID... identityToken) {
        return findSystem(session, requestingSystem.getEnterprise(), ActivityMasterSystemName, identityToken);
    }

    @Override
    //@CacheResult(cacheName = "GetActivityMasterEnterprise")
    public Uni<ISystems<?, ?>> getActivityMaster(Mutiny.Session session, IEnterprise<?, ?> requestingSystem, UUID... identityToken) {
        return findSystem(session, requestingSystem, ActivityMasterSystemName, identityToken);
    }

    @Override
    public Uni<UUID> getActivityMasterId(Mutiny.StatelessSession session, IEnterprise<?, ?> requestingSystem, UUID... identityToken) {
        return findSystemId(session, requestingSystem, ActivityMasterSystemName, identityToken);
    }

    @Override
    public Uni<ISystems<?, ?>> getActivityMaster(Mutiny.StatelessSession session, IEnterprise<?, ?> requestingSystem, UUID... identityToken) {
        return findSystem(session, requestingSystem, ActivityMasterSystemName, identityToken);
    }

    
    @Override
    public Uni<Boolean> doesSystemExist(Mutiny.Session session, IEnterprise<?, ?> enterprise, String systemName, UUID... identityToken) {
        return new Systems().builder(session)
                .withName(systemName)
                .withEnterprise(enterprise)
                .inDateRange()
                .inActiveRange()
                .get()
                .map(system -> true)
                .onFailure()
                .recoverWithUni(t -> {
                    log.warn("System {} check failed: {}", systemName, t.getMessage());
                    return Uni.createFrom().item(false);
                });
    }

    @Override
    public Uni<Boolean> doesSystemExist(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, String systemName, UUID... identityToken) {
        // Stateless: count only — never hydrate the Systems entity. Systems has EAGER @ManyToOne
        // associations (enterprise, activeFlag); hydrating it through a stateless criteria query
        // underflows Hibernate Reactive's LoadContexts stack ("Illegal pop()"). A scalar COUNT avoids
        // entity result processing entirely.
        return new Systems().builder(session)
                .withName(systemName)
                .withEnterprise(enterprise)
                .inDateRange()
                .inActiveRange()
                .getCount()
                .map(count -> count != null && count > 0L)
                .onFailure()
                .recoverWithUni(t -> {
                    log.warn("System {} stateless existence check failed: {}", systemName, t.getMessage());
                    return Uni.createFrom().item(false);
                });
    }

    
    //@CacheResult(cacheName = "FindSystemEnterpriseLevel")
    @Override
    public Uni<ISystems<?, ?>> findSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise, String systemName, UUID... identityToken) {
        return (Uni) new Systems().builder(session)
                .withName(systemName)
                .withEnterprise(enterprise)
                .inDateRange()
                .inActiveRange()
                .get();
    }

    @Override
    public Uni<UUID> findSystemId(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, String systemName, UUID... identityToken) {
        // Stateless-safe resolution: project ONLY the PK (a scalar) — never hydrate the Systems entity.
        // Systems is @Cacheable with EAGER @ManyToOne associations (enterprise, activeFlag); on a
        // stateless session BOTH entity-load paths fail — a criteria query underflows the LoadContexts
        // stack ("Illegal pop()"), and session.get(...) trips the L2-cache assembler (it tries to set a
        // reactive CompletableFuture association into the field). Callers that need a managed Systems
        // entity must use a Mutiny.Session; stateless callers resolve the id and use it for tokens/FKs.
        return new Systems().builder(session)
                .withName(systemName)
                .withEnterprise(enterprise)
                .inDateRange()
                .inActiveRange()
                .selectColumn(Systems_.id)
                .get(UUID.class);
    }

    @Override
    public Uni<ISystems<?, ?>> findSystem(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, String systemName, UUID... identityToken) {
        // Stateless "fetch ids/scalars + prep" — the approach that avoids the eager-association trap.
        // Instead of hydrating the managed Systems entity (which underflows Hibernate Reactive's
        // LoadContexts on a stateless criteria query, and trips the L2-cache reactive-association
        // assembler on session.get), we project ONLY the row's OWN scalar columns
        // (id, name, description, systemHistoryName) — a multiselect of scalars, never an entity result —
        // and build a fresh DETACHED Systems from its 4-arg constructor. The enterprise reference is
        // wired from the supplied parameter (already in hand → no extra read); the eager @ManyToOne
        // associations (enterprise/activeFlag) are intentionally left for the caller to supply where a
        // managed graph is required. The prepped entity carries exactly the identity + descriptive
        // columns needed to drive FKs, tokens and logging on the same stateless session.
        return new Systems().builder(session)
                .withName(systemName)
                .withEnterprise(enterprise)
                .inDateRange()
                .inActiveRange()
                .selectColumn(Systems_.id)
                .selectColumn(Systems_.name)
                .selectColumn(Systems_.description)
                .selectColumn(Systems_.systemHistoryName)
                .get(Object[].class)
                .map(row -> {
                    Systems prepped = new Systems(
                            (UUID) row[0],
                            (String) row[1],
                            (String) row[2],
                            (String) row[3]);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return (ISystems<?, ?>) prepped;
                });
    }

    // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
    public Uni<ISystems<?, ?>> getSystemById(Mutiny.Session session, UUID id) {
        //noinspection unchecked
        return (Uni) session.find(Systems.class, id);
    }

    
    //@CacheResult(cacheName = "FindSystemByIdentityClassification")
    @Override
    public Uni<ISystems<?, ?>> findSystem(Mutiny.Session session, ISystems<?, ?> requestingSystem, String parentSystem, UUID... identityToken) {
        SystemsXClassification systemClassifications = new SystemsXClassification();
        var enterprise = requestingSystem.getEnterprise();
        // Get identity classification using reactive pattern
        return classificationService.getIdentityType(session, requestingSystem, identityToken)
                .chain(identifyClassification -> {
                    // Use the classification to build the query
                    return systemClassifications.builder(session)
                            .findLink(null, (Classification) identifyClassification, parentSystem)
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .canRead(requestingSystem, identityToken)
                            .get()
                            .onFailure()
                            .invoke(error -> log.error("Error finding system by identity classification: {}", error.getMessage(), error))
                            .map(WarehouseSCDTable::getSystemID);
                });
    }

    @Override
    
    public Uni<String> registerNewSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISystems<?, ?> newSystem) {
        log.info(" Registering new system: '{}' for enterprise: '{}'", newSystem.getName(), enterprise.getName());
        log.debug(" Starting registration with session: {}", session.hashCode());

        // Get the activity master system first, then get the token sequentially
        return getISystem(session, ActivityMasterSystemName, enterprise)
                .onItem()
                .invoke(activityMasterSystem -> log.debug("✅ Retrieved ActivityMaster system with session: {}", session.hashCode()))
                .onFailure()
                .invoke(error -> log.error("❌ Failed to retrieve ActivityMaster system with session {}: {}",
                        session.hashCode(), error.getMessage(), error))
                // Chain to get the token after getting the system
                .chain(activityMasterSystem ->
                        getISystemToken(session, ActivityMasterSystemName, enterprise)
                                .onItem()
                                .invoke(activityMasterSystemUUID -> log.debug("✅ Retrieved ActivityMaster system UUID with session: {}", session.hashCode()))
                                .onFailure()
                                .invoke(error -> log.error("❌ Failed to retrieve ActivityMaster system UUID with session {}: {}",
                                        session.hashCode(), error.getMessage(), error))
                                .map(activityMasterSystemUUID -> new Pair<>(activityMasterSystem, activityMasterSystemUUID))
                )
                .chain(pair -> {
                    ISystems<?, ?> activityMasterSystem = pair.getKey();
                    UUID activityMasterSystemUUID = pair.getValue();
                    log.debug(" Finding classification with session: {}", session.hashCode());

                    // Use the reactive classification service
                    return classificationService.find(
                                    session, UserGroupSecurityTokenClassifications.System,
                                    activityMasterSystem,
                                    activityMasterSystemUUID
                            )
                            .onItem()
                            .invoke(classification -> log.debug("✅ Found classification: '{}' with session: {}",
                                    UserGroupSecurityTokenClassifications.System, session.hashCode()))
                            .onFailure()
                            .invoke(error -> log.error("❌ Failed to find classification with session {}: {}",
                                    session.hashCode(), error.getMessage(), error))
                            .chain(classification -> {
                                log.debug(" Creating security token for new system with session: {}", session.hashCode());
                                // Now that we have the classification, chain the reactive operations
                                return securityTokenService.create(
                                                session, UserGroupSecurityTokenClassifications.System.toString(),
                                                newSystem.getName(),
                                                newSystem.getDescription(),
                                                activityMasterSystem
                                        )
                                        .onItem()
                                        .invoke(token -> log.debug("✅ Created security token for new system with session: {}", session.hashCode()))
                                        .onFailure()
                                        .invoke(error -> log.error("❌ Failed to create security token for new system with session {}: {}",
                                                session.hashCode(), error.getMessage(), error))
                                        .chain(newSystemsSecurityToken -> {
                                            log.debug(" Creating second security token with session: {}", session.hashCode());
                                            // Create second security token (reactive)
                                            return securityTokenService.create(
                                                            session, UserGroupSecurityTokenClassifications.System.toString(),
                                                            UserGroupSecurityTokenClassifications.System.toString(),
                                                            UserGroupSecurityTokenClassifications.System.classificationDescription(),
                                                            activityMasterSystem
                                                    )
                                                    .onItem()
                                                    .invoke(token -> log.debug("✅ Created second security token with session: {}", session.hashCode()))
                                                    .onFailure()
                                                    .invoke(error -> log.error("❌ Failed to create second security token with session {}: {}",
                                                            session.hashCode(), error.getMessage(), error))
                                                    .chain(systemsToken -> {
                                                        log.debug(" Linking tokens with session: {}", session.hashCode());
                                                        // Link tokens (reactive)
                                                        return securityTokenService.link(
                                                                        session, systemsToken,
                                                                        newSystemsSecurityToken,
                                                                        classification
                                                                )
                                                                .onItem()
                                                                .invoke(result -> log.debug("✅ Linked tokens with session: {}", session.hashCode()))
                                                                .onFailure()
                                                                .invoke(error -> log.error("❌ Failed to link tokens with session {}: {}",
                                                                        session.hashCode(), error.getMessage(), error))
                                                                .chain(v -> {
                                                                    log.debug(" Adding classification to new system with session: {}", session.hashCode());
                                                                    // Add classification to new system - include in chain
                                                                    return newSystem.addOrReuseClassification(
                                                                                    session, SystemsClassifications.SystemIdentity,
                                                                                    ((SecurityToken) newSystemsSecurityToken).getSecurityToken(),
                                                                                    newSystem,
                                                                                    activityMasterSystemUUID
                                                                            )
                                                                            .onItem()
                                                                            .invoke(result -> log.debug("✅ Added classification to new system with session: {}", session.hashCode()))
                                                                            .onFailure()
                                                                            .invoke(error -> log.error("❌ Failed to add classification to new system with session {}: {}",
                                                                                    session.hashCode(), error.getMessage(), error))
                                                                            // Get security identity token
                                                                            .chain(result -> {
                                                                                log.debug(" Getting security identity token with session: {}", session.hashCode());
                                                                                return getSecurityIdentityToken(session, newSystem, activityMasterSystemUUID)
                                                                                        .onItem()
                                                                                        .invoke(uuid -> log.debug("✅ Got security identity token: '{}' with session: {}",
                                                                                                uuid, session.hashCode()))
                                                                                        .onFailure()
                                                                                        .invoke(error -> log.error("❌ Failed to get security identity token with session {}: {}",
                                                                                                session.hashCode(), error.getMessage(), error))
                                                                                        .chain(newSystemUUID -> {
                                                                                            log.debug(" Creating default security for tokens sequentially with session: {}", session.hashCode());

                                                                                            // Create default security sequentially (first token)
                                                                                            return ((SecurityToken) newSystemsSecurityToken).createDefaultSecurity(
                                                                                                            session,
                                                                                                            activityMasterSystem,
                                                                                                            activityMasterSystemUUID
                                                                                                    )
                                                                                                    .onItem()
                                                                                                    .invoke(firstSecurityResult -> log.debug("✅ Created default security for first token with session: {}", session.hashCode()))
                                                                                                    .onFailure()
                                                                                                    .invoke(error -> log.error("❌ Failed to create default security for first token with session {}: {}",
                                                                                                            session.hashCode(), error.getMessage(), error))
                                                                                                    // Then create default security for second token
                                                                                                    .chain(firstSecurityComplete -> {
                                                                                                        log.debug(" Creating default security for second token with session: {}", session.hashCode());
                                                                                                        return ((SecurityToken) systemsToken).createDefaultSecurity(
                                                                                                                        session,
                                                                                                                        activityMasterSystem,
                                                                                                                        activityMasterSystemUUID
                                                                                                                )
                                                                                                                .onItem()
                                                                                                                .invoke(secondSecurityResult -> log.debug("✅ Created default security for second token with session: {}", session.hashCode()))
                                                                                                                .onFailure()
                                                                                                                .invoke(error -> log.error("❌ Failed to create default security for second token with session {}: {}",
                                                                                                                        session.hashCode(), error.getMessage(), error));
                                                                                                    })
                                                                                                    // Then create involved party
                                                                                                    .chain(secondSecurityComplete -> {
                                                                                                        log.debug(" Creating involved party for new system with session: {}", session.hashCode());
                                                                                                        // Create involved party and wait for it to complete
                                                                                                        SystemsSystem systemsSystem = IGuiceContext.get(SystemsSystem.class);
                                                                                                        return systemsSystem.createInvolvedPartyForNewSystem(session, newSystem)
                                                                                                                .onItem()
                                                                                                                .invoke(ip -> log.debug("✅ Created involved party for new system with session: {}", session.hashCode()))
                                                                                                                .onFailure()
                                                                                                                .invoke(error -> log.error("❌ Failed to create involved party for new system with session {}: {}",
                                                                                                                        session.hashCode(), error.getMessage(), error))
                                                                                                                // Finally return the system UUID as a string
                                                                                                                .chain(ip -> {
                                                                                                                    log.info(" Successfully registered new system: '{}' with UUID: '{}'", newSystem.getName(), newSystemUUID);
                                                                                                                    return Uni.createFrom().item(newSystemUUID.toString());
                                                                                                                });
                                                                                                    });
                                                                                        });
                                                                            });
                                                                });
                                                    });
                                        });
                            });
                });
    }

    @Override
    public Uni<String> registerNewSystem(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, ISystems<?, ?> newSystem) {
        log.info("(stateless) Registering new system: '{}' for enterprise: '{}'", newSystem.getName(), enterprise.getName());

        return getActivityMaster(session, enterprise)
                .chain(activityMasterSystem -> getSecurityIdentityToken(session, activityMasterSystem)
                        .chain(activityMasterSystemUUID -> classificationService.find(session,
                                        UserGroupSecurityTokenClassifications.System, activityMasterSystem, activityMasterSystemUUID)
                                .chain(classification -> securityTokenService.create(session,
                                                UserGroupSecurityTokenClassifications.System.toString(),
                                                newSystem.getName(), newSystem.getDescription(), activityMasterSystem)
                                        .chain(newSystemsSecurityToken -> securityTokenService.create(session,
                                                        UserGroupSecurityTokenClassifications.System.toString(),
                                                        UserGroupSecurityTokenClassifications.System.toString(),
                                                        UserGroupSecurityTokenClassifications.System.classificationDescription(),
                                                        activityMasterSystem)
                                                .chain(systemsToken -> securityTokenService.link(session, systemsToken, newSystemsSecurityToken, classification)
                                                        .chain(v -> newSystem.addOrReuseClassification(session, SystemIdentity,
                                                                newSystemsSecurityToken.getSecurityToken(), newSystem, activityMasterSystemUUID))
                                                        .chain(v -> getSecurityIdentityToken(session, newSystem, activityMasterSystemUUID)
                                                                .chain(newSystemUUID -> securityTokenService
                                                                        .resolveDefaultGroupFolderTokens(session, activityMasterSystem, activityMasterSystemUUID)
                                                                        .chain(tokens -> activeFlagService.getActiveFlag(session, enterprise, activityMasterSystemUUID)
                                                                                .chain(activeFlag -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable<?, ?, ?, ?>) newSystemsSecurityToken)
                                                                                        .createDefaultSecurity(session, activityMasterSystem, enterprise, activeFlag, tokens, activityMasterSystemUUID)
                                                                                        .chain(c1 -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable<?, ?, ?, ?>) systemsToken)
                                                                                                .createDefaultSecurity(session, activityMasterSystem, enterprise, activeFlag, tokens, activityMasterSystemUUID))
                                                                                        .onFailure().recoverWithItem(0L)))
                                                                        .chain(v2 -> IGuiceContext.get(SystemsSystem.class)
                                                                                .createInvolvedPartyForNewSystem(session, newSystem))
                                                                        .map(ip -> newSystemUUID.toString())))))))
                )
                .onFailure()
                .invoke(error -> log.error("(stateless) Failed to register new system '{}': {}", newSystem.getName(), error.getMessage(), error));
    }

    @Override
    public Uni<ISystems<?, ?>> create(Mutiny.Session session, IEnterprise<?, ?> enterprise, String systemName, String systemDesc, UUID... identityToken) {
        return create(session, enterprise, systemName, systemDesc, systemName, identityToken);
    }

    @Override
    
    public Uni<ISystems<?, ?>> create(Mutiny.Session session, IEnterprise<?, ?> enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken) {
        Systems newSystem = new Systems();

        // Check if system exists and recover with creating a new one if not found
        return findSystem(session, enterprise, systemName, identityToken)
                .onFailure(NoResultException.class)
                .recoverWithUni(() -> {
                    log.info("System {} not found, creating new system", systemName);
                    // Set up the new system
                    newSystem.setName(systemName);
                    newSystem.setDescription(systemDesc);
                    newSystem.setSystemHistoryName(historyName);
                    newSystem.setEnterpriseID(enterprise);

                    // Get active flag service
                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

                    // Get active flag (reactive)
                    return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                                // Set active flag
                                newSystem.setActiveFlagID(activeFlag);
                                // Persist the new system (reactive)
                                return session.persist(newSystem)
                                        //.chain(a -> session.flush())
                                        .replaceWith(Uni.createFrom()
                                                .item(newSystem))
                                        .map(persistedSystem -> {
                                            log.info("Successfully created new system: {}", systemName);
                                            return persistedSystem;
                                        });
                            });
                })
                .onItem()
                .invoke(item -> {
                    log.debug("System {} created successfully", systemName);
                });
    }

    @Override
    public Uni<ISystems<?, ?>> create(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, String systemName, String systemDesc, UUID... identityToken) {
        return create(session, enterprise, systemName, systemDesc, systemName, identityToken);
    }

    @Override
    public Uni<ISystems<?, ?>> create(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken) {
        // Stateless find-or-create: prepped existence check, else a lean insert of the Systems row.
        return findSystem(session, enterprise, systemName, identityToken)
                .onFailure()
                .recoverWithUni(err -> {
                    Systems newSystem = new Systems();
                    newSystem.setName(systemName);
                    newSystem.setDescription(systemDesc);
                    newSystem.setSystemHistoryName(historyName);
                    newSystem.setEnterpriseID(enterprise);
                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                    return acService.getActiveFlag(session, enterprise, identityToken)
                            .chain(activeFlag -> {
                                newSystem.setActiveFlagID(activeFlag);
                                return newSystem.builder(session).persist(newSystem)
                                        .replaceWith((ISystems<?, ?>) newSystem);
                            });
                });
    }

    
    //@CacheResult(cacheName = "SystemGetSecurityToken")
    public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.Session session, String uuidIdentity, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return new SecurityToken().builder(session)
                .findBySecurityToken(uuidIdentity.toString(), enterprise)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                //      .canRead(system, identityToken)
                .get()
                .onFailure()
                .invoke(error -> log.error("Error getting security token: {}", error.getMessage(), error))
                .map(securityToken -> securityToken);
    }

    //@CacheResult(cacheName = "SystemSetSecurityTokenUUID")
    @Override
    public Uni<UUID> getSecurityIdentityToken(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return system.findClassification(session, SystemIdentity, system, identityToken)
                .map(IRelationshipValue::getValueAsUUID);
    }

    @Override
    public Uni<UUID> getSecurityIdentityToken(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        // Stateless: project the SystemIdentity link row's scalar Value column (the token UUID string) —
        // never hydrate the @Cacheable SystemsXClassification link entity. Composes on the prepped
        // stateless classification + system; security filters (canRead) are omitted because the install
        // runs security-disabled and this only reads the stored identity value.
        var enterprise = system.getEnterprise();
        return classificationService.find(session, SystemIdentity.toString(), system, identityToken)
                .chain(identityClassification -> new SystemsXClassification().builder(session)
                        .findLink((Systems) system, (Classification) identityClassification, null)
                        .inDateRange()
                        .withEnterprise(enterprise)
                        .selectColumn(WarehouseRelationshipTable_.value)
                        .get(String.class)
                        .map(UUID::fromString));
    }
}

