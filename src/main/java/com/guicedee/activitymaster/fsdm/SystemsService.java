package com.guicedee.activitymaster.fsdm;

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


import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService.ActivateFlagSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.getISystem;
import static com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService.getISystemToken;
import static com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService.EnterpriseSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications.SystemIdentity;

@Log4j2
@Singleton
public class SystemsService implements ISystemsService<SystemsService> {
    private final Map<String, ISystems<?, ?>> systemKeyToEntity = new ConcurrentHashMap<>();
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
    public Uni<ISystems<?, ?>> getActivityMaster(Mutiny.StatelessSession session,
                                                 ISystems<?, ?> requestingSystem,
                                                 UUID... identityToken) {
        return findSystem(session, requestingSystem.getEnterprise(), ActivityMasterSystemName, identityToken);
    }

    @Override
    @CacheResult(cacheName = "SystemActivityMasterIdStateless")
    public Uni<UUID> getActivityMasterId(Mutiny.StatelessSession session,
                                         @CacheKey IEnterprise<?, ?> requestingSystem,
                                         UUID... identityToken) {
        return findSystemId(session, requestingSystem, ActivityMasterSystemName, identityToken);
    }

    @Override
    @CacheResult(cacheName = "SystemActivityMasterStateless")
    public Uni<ISystems<?, ?>> getActivityMaster(Mutiny.StatelessSession session,
                                                 @CacheKey IEnterprise<?, ?> requestingSystem,
                                                 UUID... identityToken) {
        return findSystem(session, requestingSystem, ActivityMasterSystemName, identityToken);
    }


    @Override
    public Uni<Boolean> doesSystemExist(Mutiny.StatelessSession session,
                                        IEnterprise<?, ?> enterprise,
                                        String systemName,
                                        UUID... identityToken) {
        // Stateless: count only — never hydrate the Systems entity. Systems has EAGER @ManyToOne
        // associations (enterprise, activeFlag); hydrating it through a stateless criteria query
        // underflows Hibernate Reactive's LoadContexts stack ("Illegal pop()"). A scalar COUNT avoids
        // entity result processing entirely.
        return new Systems().builder(session).withName(systemName).withEnterprise(enterprise).inDateRange()
                            .inActiveRange().getCount().map(count -> count != null && count > 0L).onFailure()
                            .recoverWithUni(t -> {
                                log.warn("System {} stateless existence check failed: {}", systemName, t.getMessage());
                                return Uni.createFrom().item(false);
                            });
    }

    @Override
    @CacheResult(cacheName = "SystemFindIdStateless")
    public Uni<UUID> findSystemId(Mutiny.StatelessSession session,
                                  @CacheKey IEnterprise<?, ?> enterprise,
                                  @CacheKey String systemName,
                                  UUID... identityToken) {
        // Stateless-safe resolution: project ONLY the PK (a scalar) — never hydrate the Systems entity.
        // Systems is @Cacheable with EAGER @ManyToOne associations (enterprise, activeFlag); on a
        // stateless session BOTH entity-load paths fail — a criteria query underflows the LoadContexts
        // stack ("Illegal pop()"), and session.get(...) trips the L2-cache assembler (it tries to set a
        // reactive CompletableFuture association into the field). Callers that need a managed Systems
        // entity must use a Mutiny.StatelessSession; stateless callers resolve the id and use it for tokens/FKs.
        return new Systems().builder(session).withName(systemName).withEnterprise(enterprise).inDateRange()
                            .inActiveRange().selectColumn(Systems_.id).get(UUID.class);
    }

    @Override
    @CacheResult(cacheName = "SystemFindByNameStateless")
    public Uni<ISystems<?, ?>> findSystem(Mutiny.StatelessSession session,
                                          @CacheKey IEnterprise<?, ?> enterprise,
                                          @CacheKey String systemName,
                                          UUID... identityToken) {
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
        return new Systems().builder(session).withName(systemName).withEnterprise(enterprise).inDateRange()
                            .inActiveRange().selectColumn(Systems_.id).selectColumn(Systems_.name)
                            .selectColumn(Systems_.description).selectColumn(Systems_.systemHistoryName)
                            .get(Object[].class).map(row -> {
                    Systems prepped = new Systems((UUID) row[0], (String) row[1], (String) row[2], (String) row[3]);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return (ISystems<?, ?>) prepped;
                });
    }

    private String systemCacheKey(IEnterprise<?, ?> enterprise, String systemName) {
        UUID enterpriseId = enterprise == null ? null : enterprise.getId();
        return (enterpriseId == null ? "" : enterpriseId.toString()) + "|" + (systemName == null ? "" : systemName.trim());
    }

    private void cacheSystem(IEnterprise<?, ?> enterprise,
                             String requestedName,
                             ISystems<?, ?> system,
                             Map<String, ISystems<?, ?>> targetCache,
                             boolean includeActiveFlag) {
        if (system == null || system.getId() == null) {
            return;
        }
        String systemName = system.getName() == null ? requestedName : system.getName();
        String historyName = system instanceof Systems concrete ? concrete.getSystemHistoryName() : systemName;
        Systems prepped = new Systems(system.getId(), systemName, system.getDescription(), historyName);
        prepped.setEnterpriseID(enterprise);
        if (includeActiveFlag && system.getActiveFlagID() != null) {
            prepped.setActiveFlagID(system.getActiveFlagID());
        }
        prepped.setFake(false);
        String cacheKey = systemCacheKey(enterprise, systemName);
        targetCache.put(cacheKey, prepped);
        if (requestedName != null && !requestedName.equals(systemName)) {
            String requestedKey = systemCacheKey(enterprise, requestedName);
            targetCache.put(requestedKey, prepped);
        }
    }

    // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
    public Uni<ISystems<?, ?>> getSystemById(Mutiny.StatelessSession session, UUID id) {
        //noinspection unchecked
        return (Uni) session.get(Systems.class, id);
    }

    @Override
    public Uni<ISystems<?, ?>> findSystem(Mutiny.StatelessSession session,
                                          ISystems<?, ?> requestingSystem,
                                          String parentSystem,
                                          UUID... identityToken) {
        SystemsXClassification systemClassifications = new SystemsXClassification();
        var enterprise = requestingSystem.getEnterprise();
        // Get identity classification using reactive pattern
        return classificationService.getIdentityType(session, requestingSystem, identityToken)
                                    .chain(identifyClassification -> {
                                        // Use the classification to build the query
                                        return systemClassifications.builder(session).findLink(null,
                                                                                               (Classification) identifyClassification,
                                                                                               parentSystem)
                                                                    .inDateRange().withEnterprise(enterprise)
                                                                    .canRead(requestingSystem, identityToken).get()
                                                                    .onFailure().invoke(error -> log.error(
                                                        "Error finding system by identity classification: {}",
                                                        error.getMessage(),
                                                        error)).map(WarehouseSCDTable::getSystemID);
                                    });
    }

    @Override
    public Uni<String> registerNewSystem(Mutiny.StatelessSession session,
                                         IEnterprise<?, ?> enterprise,
                                         ISystems<?, ?> newSystem) {
        log.info("(stateless) Registering new system: '{}' for enterprise: '{}'",
                 newSystem.getName(),
                 enterprise.getName());

        return getActivityMaster(session, enterprise)
                .chain(activityMasterSystem -> getSecurityIdentityToken(session, activityMasterSystem).chain(
                        activityMasterSystemUUID -> classificationService.find(session,
                                                                               UserGroupSecurityTokenClassifications.System,
                                                                               activityMasterSystem,
                                                                               activityMasterSystemUUID)
                                                                         .chain(classification -> securityTokenService
                                                                                 .create(session,
                                                                                         UserGroupSecurityTokenClassifications.System.toString(),
                                                                                         newSystem.getName(),
                                                                                         newSystem.getDescription(),
                                                                                         activityMasterSystem)
                                                                                 .chain(newSystemsSecurityToken -> securityTokenService
                                                                                         .create(session,
                                                                                                 UserGroupSecurityTokenClassifications.System.toString(),
                                                                                                 UserGroupSecurityTokenClassifications.System.toString(),
                                                                                                 UserGroupSecurityTokenClassifications.System.classificationDescription(),
                                                                                                 activityMasterSystem)
                                                                                         .chain(systemsToken -> securityTokenService
                                                                                                 .link(session,
                                                                                                       systemsToken,
                                                                                                       newSystemsSecurityToken,
                                                                                                       classification)
                                                                                                 .chain(v -> newSystem.addOrReuseClassification(
                                                                                                         session,
                                                                                                         SystemIdentity,
                                                                                                         newSystemsSecurityToken.getSecurityToken(),
                                                                                                         newSystem,
                                                                                                         activityMasterSystemUUID))
                                                                                                 .chain(v -> getSecurityIdentityToken(
                                                                                                         session,
                                                                                                         newSystem,
                                                                                                         activityMasterSystemUUID).chain(
                                                                                                         newSystemUUID -> securityTokenService
                                                                                                                 .resolveDefaultGroupFolderTokens(
                                                                                                                         session,
                                                                                                                         activityMasterSystem,
                                                                                                                         activityMasterSystemUUID)
                                                                                                                 .chain(tokens -> activeFlagService
                                                                                                                         .getActiveFlag(
                                                                                                                                 session,
                                                                                                                                 enterprise,
                                                                                                                                 activityMasterSystemUUID)
                                                                                                                         .chain(activeFlag -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable<?, ?, ?, ?>) newSystemsSecurityToken)
                                                                                                                                 .createDefaultSecurity(
                                                                                                                                         session,
                                                                                                                                         activityMasterSystem,
                                                                                                                                         enterprise,
                                                                                                                                         activeFlag,
                                                                                                                                         tokens,
                                                                                                                                         activityMasterSystemUUID)
                                                                                                                                 .chain(c1 -> ((com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable<?, ?, ?, ?>) systemsToken).createDefaultSecurity(
                                                                                                                                         session,
                                                                                                                                         activityMasterSystem,
                                                                                                                                         enterprise,
                                                                                                                                         activeFlag,
                                                                                                                                         tokens,
                                                                                                                                         activityMasterSystemUUID))
                                                                                                                                 .onFailure()
                                                                                                                                 .recoverWithItem(
                                                                                                                                         0L)))
                                                                                                                 .chain(v2 -> IGuiceContext
                                                                                                                         .get(SystemsSystem.class)
                                                                                                                         .createInvolvedPartyForNewSystem(
                                                                                                                                 session,
                                                                                                                                 newSystem))
                                                                                                                 .map(ip -> newSystemUUID.toString()))))))))
                .onFailure().invoke(error -> log.error("(stateless) Failed to register new system '{}': {}",
                                                       newSystem.getName(),
                                                       error.getMessage(),
                                                       error));
    }

    @Override
    public Uni<ISystems<?, ?>> create(Mutiny.StatelessSession session,
                                      IEnterprise<?, ?> enterprise,
                                      String systemName,
                                      String systemDesc,
                                      UUID... identityToken) {
        return create(session, enterprise, systemName, systemDesc, systemName, identityToken);
    }

    @Override
    public Uni<ISystems<?, ?>> create(Mutiny.StatelessSession session,
                                      IEnterprise<?, ?> enterprise,
                                      String systemName,
                                      String systemDesc,
                                      String historyName,
                                      UUID... identityToken) {
        // Stateless find-or-create: prepped existence check, else a lean insert of the Systems row.
        return findSystem(session, enterprise, systemName, identityToken).onFailure().recoverWithUni(err -> {
            Systems newSystem = new Systems();
            newSystem.setName(systemName);
            newSystem.setDescription(systemDesc);
            newSystem.setSystemHistoryName(historyName);
            newSystem.setEnterpriseID(enterprise);
            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
            return acService.getActiveFlag(session, enterprise, identityToken).chain(activeFlag -> {
                newSystem.setActiveFlagID(activeFlag);
                return newSystem.builder(session).persist(newSystem).replaceWith((ISystems<?, ?>) newSystem);
            });
        });
    }


    //@CacheResult(cacheName = "SystemGetSecurityToken")
    public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.StatelessSession session,
                                                      String uuidIdentity,
                                                      ISystems<?, ?> system,
                                                      UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return new SecurityToken().builder(session).findBySecurityToken(uuidIdentity.toString(), enterprise)
                                  .inActiveRange().inDateRange().withEnterprise(enterprise)
                                  //      .canRead(system, identityToken)
                                  .get().onFailure().invoke(error -> log.error("Error getting security token: {}",
                                                                               error.getMessage(),
                                                                               error))
                                  .map(securityToken -> securityToken);
    }

    //@CacheResult(cacheName = "SystemSetSecurityTokenUUID")
    @Override
    public Uni<UUID> getSecurityIdentityToken(Mutiny.StatelessSession session,
                                              ISystems<?, ?> system,
                                              UUID... identityToken) {
        // Stateless: project the SystemIdentity link row's scalar Value column (the token UUID string) —
        // never hydrate the @Cacheable SystemsXClassification link entity. Composes on the prepped
        // stateless classification + system; security filters (canRead) are omitted because the install
        // runs security-disabled and this only reads the stored identity value.
        var enterprise = system.getEnterprise();
        return classificationService.find(session, SystemIdentity.toString(), system, identityToken)
                                    .chain(identityClassification -> new SystemsXClassification().builder(session)
                                                                                                 .findLink((Systems) system,
                                                                                                           (Classification) identityClassification,
                                                                                                           null)
                                                                                                 .inDateRange()
                                                                                                 .withEnterprise(
                                                                                                         enterprise)
                                                                                                 .selectColumn(
                                                                                                         WarehouseRelationshipTable_.value)
                                                                                                 .get(String.class)
                                                                                                 .map(UUID::fromString));
    }
}

