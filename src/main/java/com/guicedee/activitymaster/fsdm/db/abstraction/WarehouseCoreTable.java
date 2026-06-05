package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.SecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.google.inject.Inject;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.guicedee.client.IGuiceContext.*;


/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@SuppressWarnings("unchecked")
@MappedSuperclass()
@Log4j2
public abstract class WarehouseCoreTable<J extends WarehouseCoreTable<J, Q, I, S>,
        Q extends QueryBuilderCore<Q, J, I>,
        I extends java.util.UUID,
        S extends WarehouseSecurityTable<S, ?, ?>
        >
        extends WarehouseBaseTable<J, Q, I>
        implements IWarehouseCoreTable<J, Q, I, S> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @Transient
    private SecurityTokenService securityTokenService;

    public WarehouseCoreTable() {

    }

    public abstract void configureSecurityEntity(S securityEntity);

    @Override
    public Uni<Void> createDefaultSecurity(Mutiny.Session session, ISystems<?, ?> system, UUID... identity) {
        log.trace("🛡️ Creating default security for system: {} with session: {}", system.getName(), session.hashCode());

        // Use the provided session and execute operations sequentially
        log.trace("📋 Starting sequential security operations with session: {}", session.hashCode());
        if (false)
            // Chain all security operations sequentially
            return createDefaultAdministratorSecurityAccess(session, system, identity)
                    .chain(() -> createDefaultEveryoneSecurityAccess(session, system, identity))
                    .chain(() -> createDefaultEverywhereSecurityAccess(session, system, identity))
                    .chain(() -> createDefaultSystemsSecurityAccess(session, system, identity))
                    .chain(() -> createDefaultApplicationsSecurityAccess(session, system, identity))
                    .chain(() -> createDefaultPluginsSecurityAccess(session, system, identity))
                    .chain(() -> createDefaultGuestReadSecurityAccess(session, system, identity))
                    .onItem()
                    .invoke(() -> log.trace("✅ All security operations completed successfully"))
                    .onFailure()
                    .invoke(error -> log.error("❌ Failed to complete security operations: {}", error.getMessage(), error))
                    .replaceWithVoid();
        else {
            return Uni.createFrom()
                    .voidItem();
        }
    }

    /**
     * Batch, stateless-session variant of default-security creation. See
     * {@link com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable#createDefaultSecurity(Mutiny.StatelessSession, ISystems, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag, java.util.Map, java.util.UUID...)}.
     * <p>
     * Performs pure inserts on a {@link Mutiny.StatelessSession}: no first-level cache, no
     * dirty-checking, so the persistence context never grows regardless of how many rows are written.
     * All shared references (system/enterprise/active-flag/tokens) are supplied pre-resolved, so only
     * their identifiers are needed for the foreign keys.
     */
    @Override
    public Uni<Long> createDefaultSecurity(Mutiny.StatelessSession session,
                                           ISystems<?, ?> system,
                                           com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?> enterprise,
                                           com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag<?, ?> activeFlag,
                                           java.util.Map<String, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?>> groupFolderTokens,
                                           UUID... identityToken) {
        // Standard access policy per token key: {create, update, delete, read}
        record Grant(String key, boolean create, boolean update, boolean delete, boolean read) {
        }
        List<Grant> grants = List.of(
                new Grant(SECURITY_ADMINISTRATORS, true, true, true, true),
                new Grant(SECURITY_EVERYONE, false, false, false, false),
                new Grant(SECURITY_EVERYWHERE, false, false, false, true),
                new Grant(SECURITY_SYSTEMS, true, true, false, true),
                new Grant(SECURITY_APPLICATIONS, true, true, false, true),
                new Grant(SECURITY_PLUGINS, true, true, false, true),
                new Grant(SECURITY_GUESTS, false, false, false, true)
        );

        long[] inserted = {0L};
        Uni<Void> chain = Uni.createFrom().voidItem();
        for (Grant grant : grants) {
            com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> token =
                    groupFolderTokens.get(grant.key());
            if (token == null) {
                continue;
            }
            chain = chain.chain(() -> {
                S st = get(findPersistentSecurityClass());
                st.setSystemID(system);
                st.setOriginalSourceSystemID(system.getId());
                st.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
                st.setEnterpriseID(enterprise);
                st.setActiveFlagID(activeFlag);
                st.setSecurityTokenID(token);
                st.setCreateAllowed(grant.create());
                st.setUpdateAllowed(grant.update());
                st.setDeleteAllowed(grant.delete());
                st.setReadAllowed(grant.read());
                // Links the security row back to this owning entity (sets the base FK).
                configureSecurityEntity(st);
                inserted[0]++;
                return st.builder(session).persist().replaceWithVoid();
            });
        }
        return chain.replaceWith(() -> inserted[0]);
    }

    @Override
    public Uni<Long> countDefaultSecurity(Mutiny.Session session) {
        S stAdmin = get(findPersistentSecurityClass());
        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?, ?, ?> securities = stAdmin.builder(session);
        return securities.findLinkedSecurityTokens(this)
                .inDateRange()
                .getCount();
    }

    @Override
    public Uni<Boolean> canRead(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return hasGrant(session, system, row -> row.isReadAllowed(), identityToken);
    }

    @Override
    public Uni<Boolean> canWrite(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return hasGrant(session, system, row -> row.isCreateAllowed() || row.isUpdateAllowed(), identityToken);
    }

    @Override
    public Uni<java.util.Set<UUID>> readableIds(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        return get(SecurityTokenService.class).getApplicableSecurityTokenIds(session, system, identityToken)
                .chain(applicable -> {
                    if (applicable == null || applicable.isEmpty()) {
                        return Uni.createFrom().item(java.util.Collections.<UUID>emptySet());
                    }
                    S stAdmin = get(findPersistentSecurityClass());
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities<?, ?, ?> securities = stAdmin.builder(session);
                    // Load all in-date-range security rows for this entity type, then keep only the rows
                    // whose token is in the applicable set and that grant read — exactly the
                    // (securityTokenID IN applicable) AND (ReadAllowed = true) rule, collected as base ids.
                    return securities.inDateRange()
                            .getAll()
                            .map(rows -> {
                                java.util.Set<UUID> ids = new java.util.LinkedHashSet<>();
                                for (Object o : rows) {
                                    @SuppressWarnings("unchecked")
                                    S row = (S) o;
                                    var token = row.getSecurityTokenID();
                                    if (token != null && applicable.contains(token.getId()) && row.isReadAllowed()) {
                                        UUID baseId = extractBaseId(row);
                                        if (baseId != null) {
                                            ids.add(baseId);
                                        }
                                    }
                                }
                                return ids;
                            });
                });
    }

    /**
     * Reads the owning-entity id from a security row. The {@code base} back-reference is declared
     * (typed) on each concrete security entity rather than on {@link WarehouseSecurityTable}, so it is
     * accessed reflectively here to keep this generic superclass free of an interface-wide ripple.
     */
    private UUID extractBaseId(S row) {
        try {
            Object base = row.getClass().getMethod("getBase").invoke(row);
            if (base instanceof WarehouseBaseTable<?, ?, ?> baseTable) {
                return (UUID) baseTable.getId();
            }
        } catch (ReflectiveOperationException e) {
            log.debug("Unable to resolve base id for security row {}: {}", row.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    /**
     * Shared row-level access evaluation. Expands the caller's identity tokens into the full set of
     * applicable security-token ids (token + every group/folder it belongs to, transitively) via
     * {@link SecurityTokenService#getApplicableSecurityTokenIds}, then returns {@code true} when this
     * entity has an in-date-range security row whose token is in that set and whose grant flag (per the
     * supplied predicate) is set.
     */
    private Uni<Boolean> hasGrant(Mutiny.Session session, ISystems<?, ?> system,
                                  java.util.function.Predicate<S> grant, UUID... identityToken) {
        return get(SecurityTokenService.class).getApplicableSecurityTokenIds(session, system, identityToken)
                .chain(applicable -> {
                    if (applicable == null || applicable.isEmpty()) {
                        return Uni.createFrom().item(false);
                    }
                    S stAdmin = get(findPersistentSecurityClass());
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities<?, ?, ?> securities = stAdmin.builder(session);
                    return securities.findLinkedSecurityTokens(this)
                            .inDateRange()
                            .getAll()
                            .map(rows -> {
                                for (Object o : rows) {
                                    @SuppressWarnings("unchecked")
                                    S row = (S) o;
                                    var token = row.getSecurityTokenID();
                                    if (token != null && applicable.contains(token.getId()) && grant.test(row)) {
                                        return true;
                                    }
                                }
                                return false;
                            });
                });
    }

    public Uni<Void> updateSecurity(Mutiny.Session session, J newCoreTable, Systems system) {
        log.trace("🔄 Updating security for table with system: {}", system.getName());

        S stAdmin = get(findPersistentSecurityClass());
        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?, ?, ?> securities = stAdmin.builder(session);

        return securities.findLinkedSecurityTokens(this)
                .inDateRange()
                .getAll()
                .chain(result -> {
                    log.debug("📋 Found {} security tokens to update sequentially", result.size());

                    // Start with a completed Uni to begin the chain
                    Uni<Void> sequentialChain = Uni.createFrom()
                            .voidItem();

                    // Process each token sequentially by chaining operations
                    for (Object exist : result) {
                        final S existingToken = (S) exist;
                        existingToken.setId(null);
                        configureDefaultsForNewToken(existingToken, system);

                        // Add this operation to the chain
                        sequentialChain = sequentialChain.chain(() -> {
                            log.debug("🔄 Updating security token sequentially");
                            return session.persist(existingToken)
                                    .onItem()
                                    .invoke(() -> log.debug("✅ Security token updated successfully"))
                                    .onFailure()
                                    .invoke(error -> log.error("❌ Failed to update security token: {}", error.getMessage(), error))
                                    .replaceWithVoid();
                        });
                    }

                    // Return the complete chain
                    return sequentialChain
                            .onItem()
                            .invoke(() -> log.debug("✅ All security tokens updated successfully"))
                            .onFailure()
                            .invoke(error -> log.error("❌ Error updating security tokens: {}", error.getMessage(), error));
                });
    }

    private Uni<S> createDefaultAdministratorSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity) {
        log.debug("🔧 Creating default administrator security access");

        S stAdmin = get(findPersistentSecurityClass());
        return securityTokenService.getAdministratorsFolder(session, system, identity)
                .chain(administrators -> {
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities<?, ?, ?> securities = stAdmin.builder(session);
                    return securities.findLinkedSecurityToken((SecurityToken) administrators, this)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onFailure()
                            .recoverWithUni(() -> {
                                log.debug("🔧 Creating new administrator security token");
                                S stEntity = get(findPersistentSecurityClass());
                                configureDefaultsForNewToken(stEntity, system);
                                stEntity.setSecurityTokenID(administrators);
                                stEntity.setCreateAllowed(true);
                                stEntity.setUpdateAllowed(true);
                                stEntity.setDeleteAllowed(true);
                                stEntity.setReadAllowed(true);

                                return (Uni) session.persist(stEntity)
                                        .chain(s -> {
                                            configureSecurityEntity(stEntity);
                                            return session.merge(stEntity);
                                        });
                            })
                            .chain(result -> {
                                if (result instanceof Uni) {
                                    return (Uni<S>) result;
                                }
                                log.debug("✅ Administrator security token already exists");
                                return Uni.createFrom()
                                        .item((S) result);
                            });
                });
    }

    private Uni<S> createDefaultEveryoneSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity) {
        log.debug("🔧 Creating default everyone security access with session");

        S stAdmin = get(findPersistentSecurityClass());
        return (Uni) get(SecurityTokenService.class)
                .getEveryoneGroup(session, system, identity)
                .chain(everyoneGroup -> {
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities<?, ?, ?> securities = stAdmin.builder(session);

                    return securities.findLinkedSecurityToken((SecurityToken) everyoneGroup, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                                if (throwable != null) {
                                    log.debug("🔧 Creating new everyone security token");
                                    S stEntity = get(findPersistentSecurityClass());
                                    configureDefaultsForNewToken(stEntity, system);
                                    stEntity.setSecurityTokenID(everyoneGroup);
                                    stEntity.setCreateAllowed(false);
                                    stEntity.setUpdateAllowed(false);
                                    stEntity.setDeleteAllowed(false);
                                    stEntity.setReadAllowed(false);

                                    return session.persist(stEntity)
                                            .chain(s -> {
                                                configureSecurityEntity(stEntity);
                                                log.debug("✅ Everyone security token created successfully");
                                                return Uni.createFrom()
                                                        .item(stEntity);
                                            });
                                } else {
                                    log.debug("✅ Everyone security token already exists");
                                    return Uni.createFrom()
                                            .item((S) result);
                                }
                            });
                });
    }

    private Uni<S> createDefaultEverywhereSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity) {
        log.debug("🔧 Creating default everywhere security access with session");

        S stAdmin = get(findPersistentSecurityClass());
        return get(SecurityTokenService.class)
                .getEverywhereGroup(session, system, identity)
                .chain(everywhereGroup -> {
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities securities = stAdmin.builder(session);

                    return securities.findLinkedSecurityToken((SecurityToken) everywhereGroup, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                                if (throwable != null) {
                                    log.debug("🔧 Creating new everywhere security token");
                                    S stEntity = get(findPersistentSecurityClass());
                                    configureDefaultsForNewToken(stEntity, system);
                                    stEntity.setSecurityTokenID(everywhereGroup);
                                    stEntity.setCreateAllowed(false);
                                    stEntity.setUpdateAllowed(false);
                                    stEntity.setDeleteAllowed(false);
                                    stEntity.setReadAllowed(true);

                                    return session.persist(stEntity)
                                            .chain(s -> {
                                                configureSecurityEntity(stEntity);
                                                log.debug("✅ Everywhere security token created successfully");
                                                return Uni.createFrom()
                                                        .item(stEntity);
                                            });
                                } else {
                                    log.debug("✅ Everywhere security token already exists");
                                    return Uni.createFrom()
                                            .item((S) result);
                                }
                            });
                });
    }

    private Uni<S> createDefaultSystemsSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity) {
        log.debug("🔧 Creating default systems security access with session");

        S stAdmin = get(findPersistentSecurityClass());
        return get(SecurityTokenService.class)
                .getSystemsFolder(session, system, identity)
                .chain(systemsFolder -> {
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities securities = stAdmin.builder(session);

                    return securities.findLinkedSecurityToken((SecurityToken) systemsFolder, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                                if (throwable != null) {
                                    log.debug("🔧 Creating new systems security token");
                                    S stEntity = get(findPersistentSecurityClass());
                                    configureDefaultsForNewToken(stEntity, system);
                                    stEntity.setSecurityTokenID(systemsFolder);
                                    stEntity.setCreateAllowed(true);
                                    stEntity.setUpdateAllowed(true);
                                    stEntity.setDeleteAllowed(false);
                                    stEntity.setReadAllowed(true);

                                    return session.persist(stEntity)
                                            .chain(s -> {
                                                configureSecurityEntity(stEntity);
                                                log.debug("✅ Systems security token created successfully");
                                                return Uni.createFrom()
                                                        .item(stEntity);
                                            });
                                } else {
                                    log.debug("✅ Systems security token already exists");
                                    return Uni.createFrom()
                                            .item((S) result);
                                }
                            });
                });
    }

    private Uni<S> createDefaultApplicationsSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity) {
        log.debug("🔧 Creating default applications security access with session");

        S stAdmin = get(findPersistentSecurityClass());
        return get(SecurityTokenService.class)
                .getApplicationsFolder(session, system, identity)
                .chain(applicationsFolder -> {
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities securities = stAdmin.builder(session);

                    return securities.findLinkedSecurityToken((SecurityToken) applicationsFolder, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                                if (throwable != null) {
                                    log.debug("🔧 Creating new applications security token");
                                    S stEntity = get(findPersistentSecurityClass());
                                    configureDefaultsForNewToken(stEntity, system);
                                    stEntity.setSecurityTokenID(applicationsFolder);
                                    stEntity.setCreateAllowed(true);
                                    stEntity.setUpdateAllowed(true);
                                    stEntity.setDeleteAllowed(false);
                                    stEntity.setReadAllowed(true);

                                    return session.persist(stEntity)
                                            .chain(s -> {
                                                configureSecurityEntity(stEntity);
                                                log.debug("✅ Applications security token created successfully");
                                                return Uni.createFrom()
                                                        .item(stEntity);
                                            });
                                } else {
                                    log.debug("✅ Applications security token already exists");
                                    return Uni.createFrom()
                                            .item((S) result);
                                }
                            });
                });
    }

    private Uni<S> createDefaultPluginsSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity) {
        log.debug("🔧 Creating default plugins security access with session");

        S stAdmin = get(findPersistentSecurityClass());
        return get(SecurityTokenService.class)
                .getPluginsFolder(session, system, identity)
                .chain(pluginsFolder -> {
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities securities = stAdmin.builder(session);

                    return securities.findLinkedSecurityToken((SecurityToken) pluginsFolder, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                                if (throwable != null) {
                                    log.debug("🔧 Creating new plugins security token");
                                    S stEntity = get(findPersistentSecurityClass());
                                    configureDefaultsForNewToken(stEntity, system);
                                    stEntity.setSecurityTokenID(pluginsFolder);
                                    stEntity.setCreateAllowed(true);
                                    stEntity.setUpdateAllowed(true);
                                    stEntity.setDeleteAllowed(false);
                                    stEntity.setReadAllowed(true);

                                    return session.persist(stEntity)
                                            .chain(s -> {
                                                configureSecurityEntity(stEntity);
                                                log.debug("✅ Plugins security token created successfully");
                                                return Uni.createFrom()
                                                        .item(stEntity);
                                            });
                                } else {
                                    log.debug("✅ Plugins security token already exists");
                                    return Uni.createFrom()
                                            .item((S) result);
                                }
                            });
                });
    }

    private Uni<S> createDefaultGuestReadSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity) {
        log.debug("🔧 Creating default guest read security access with session");

        S stAdmin = get(findPersistentSecurityClass());
        return get(SecurityTokenService.class)
                .getGuestsFolder(session, system, identity)
                .chain(guestsFolder -> {
                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities securities = stAdmin.builder(session);

                    return securities.findLinkedSecurityToken((SecurityToken) guestsFolder, this)
                            //.inActiveRange(enterprise)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onItemOrFailure()
                            .call((result, throwable) -> {
                                if (throwable != null) {
                                    log.debug("🔧 Creating new guest read security token");
                                    S stEntity = get(findPersistentSecurityClass());
                                    configureDefaultsForNewToken(stEntity, system);
                                    stEntity.setSecurityTokenID(guestsFolder);
                                    stEntity.setCreateAllowed(false);
                                    stEntity.setUpdateAllowed(false);
                                    stEntity.setDeleteAllowed(false);
                                    stEntity.setReadAllowed(true);

                                    return session.persist(stEntity)
                                            .chain(s -> {
                                                configureSecurityEntity(stEntity);
                                                log.debug("✅ Guest read security token created successfully");
                                                return Uni.createFrom()
                                                        .item(stEntity);
                                            });
                                } else {
                                    log.debug("✅ Guest read security token already exists");
                                    return Uni.createFrom()
                                            .item((S) result);
                                }
                            });
                });
    }

    @SuppressWarnings("unchecked")
    protected Class<S> findPersistentSecurityClass() {
        return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
    }

    protected void configureDefaultsForNewToken(S stAdmin, ISystems<?, ?> system) {
        stAdmin.setSystemID(system);
        stAdmin.setActiveFlagID(((Systems) system).getActiveFlagID());
        stAdmin.setOriginalSourceSystemID(system);
        stAdmin.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
        stAdmin.setEnterpriseID(system.getEnterprise());
    }

    public Uni<S> createDefaultGuestNoSecurityAccess(Mutiny.Session session, ISystems<?, ?> system, java.util.UUID... identity) {
        log.debug("🎭 Creating default guest no-security access token for system: {}", system.getName());

        return securityTokenService.getGuestsFolder(session, system, identity)
                .chain(administrators -> {
                    S stAdmin = get(findPersistentSecurityClass());

                    @SuppressWarnings("rawtypes")
                    QueryBuilderSecurities securities = stAdmin.builder(session);

                    return securities.findLinkedSecurityToken((SecurityToken) administrators, this)
                            .inDateRange()
                            .setReturnFirst(true)
                            .get()
                            .onFailure()
                            .recoverWithItem(() -> {
                                // Create new token if not found
                                S stEntity = get(findPersistentSecurityClass());
                                configureDefaultsForNewToken(stEntity, system);
                                stEntity.setSecurityTokenID(administrators);
                                stEntity.setCreateAllowed(false);
                                stEntity.setUpdateAllowed(false);
                                stEntity.setDeleteAllowed(false);
                                stEntity.setReadAllowed(false);

                                return session.persist(stEntity)
                                        .chain(persisted -> {
                                            configureSecurityEntity(stEntity);
                                            log.debug("✅ Guest no-security token created successfully");
                                            return Uni.createFrom()
                                                    .item(stEntity);
                                        });
                            })
                            .chain(result -> {
                                if (result instanceof Uni) {
                                    return (Uni<S>) result;
                                }
                                return Uni.createFrom()
                                        .item((S) result);
                            });
                });
    }

}
