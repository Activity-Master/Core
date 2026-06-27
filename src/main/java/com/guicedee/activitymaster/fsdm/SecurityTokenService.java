package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.db.abstraction.WarehouseBaseTable;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.*;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications.*;

@SuppressWarnings("Duplicates")
@Log4j2
@Singleton
public class SecurityTokenService
        implements ISecurityTokenService<SecurityTokenService> {
    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private Mutiny.SessionFactory sessionFactory;

    @Override
    public ISecurityToken<?, ?> get() {
        return new SecurityToken();
    }

    /**
     * Bulk, batched, stateless default-security application for an entire table. See
     * {@link ISecurityTokenService#applyDefaultSecurityToTable(Mutiny.Session, IWarehouseCoreTable, ISystems, UUID...)}.
     * <p>
     * The seven canonical group/folder tokens are resolved <strong>once</strong> for the whole pass (not
     * per row), rows that already carry security are skipped via a cheap COUNT gate, and the remaining
     * inserts are written in a single {@link Mutiny.StatelessSession} transaction so the persistence
     * context never grows. This is the efficient counterpart of the per-row
     * {@link IWarehouseCoreTable#createDefaultSecurity(Mutiny.Session, ISystems, UUID...)} and is the
     * preferred path after bulk imports (e.g. geography loads).
     */
    @Override
    public Uni<Void> applyDefaultSecurityToTable(Mutiny.Session session, IWarehouseCoreTable<?, ?, ?, ?> table,
                                                 ISystems<?, ?> system, UUID... identityToken) {
        log.debug("🔐 Applying batched/stateless default security for table: {}", table.getClass().getSimpleName());

        // Resolve the shared group/folder tokens ONCE for the whole table pass.
        Map<String, ISecurityToken<?, ?>> tokens = new LinkedHashMap<>();
        Uni<Void> resolve = resolveGroupFolderTokens(session, system, tokens, identityToken);

        IEnterprise<?, ?> enterprise = system.getEnterprise();
        IActiveFlag<?, ?> activeFlag = ((Systems) system).getActiveFlagID();

        return resolve.chain(() -> table.builder(session)
                .inDateRange()
                .getAll()
                .chain(items -> {
                    if (items == null || items.isEmpty()) {
                        log.debug("✅ No rows to secure for {}", table.getClass().getSimpleName());
                        return Uni.createFrom().voidItem();
                    }

                    // Idempotency gate: only rows without existing default security are (re)created.
                    List<IWarehouseCoreTable<?, ?, ?, ?>> pending = new ArrayList<>();
                    Uni<Void> gate = Uni.createFrom().voidItem();
                    for (Object next : items) {
                        final IWarehouseCoreTable<?, ?, ?, ?> item = (IWarehouseCoreTable<?, ?, ?, ?>) next;
                        gate = gate.chain(() -> item.countDefaultSecurity(session)
                                .invoke(count -> {
                                    if (count == null || count == 0L) {
                                        pending.add(item);
                                    }
                                })
                                .replaceWithVoid());
                    }

                    return gate.chain(() -> batchInsertSecurity(pending, system, enterprise, activeFlag, tokens,
                            table.getClass().getSimpleName(), identityToken));
                }));
    }

    @Override
    public Uni<Void> applyDefaultSecurityToRows(Mutiny.Session session,
                                                java.util.Collection<? extends IWarehouseCoreTable<?, ?, ?, ?>> rows,
                                                ISystems<?, ?> system, UUID... identityToken) {
        if (rows == null || rows.isEmpty()) {
            return Uni.createFrom().voidItem();
        }
        log.debug("🔐 Applying batched/stateless default security for {} explicit rows", rows.size());

        Map<String, ISecurityToken<?, ?>> tokens = new LinkedHashMap<>();
        Uni<Void> resolve = resolveGroupFolderTokens(session, system, tokens, identityToken);

        IEnterprise<?, ?> enterprise = system.getEnterprise();
        IActiveFlag<?, ?> activeFlag = ((Systems) system).getActiveFlagID();

        // Scan-free, gate-free: the caller guarantees these rows are new.
        List<IWarehouseCoreTable<?, ?, ?, ?>> pending = new ArrayList<>(rows);
        return resolve.chain(() -> batchInsertSecurity(pending, system, enterprise, activeFlag, tokens,
                "explicit-rows", identityToken));
    }

    @Override
    public Uni<Void> applyScopeRestrictedSecurity(Mutiny.Session session,
                                                  Map<? extends IWarehouseCoreTable<?, ?, ?, ?>, ? extends ISecurityToken<?, ?>> recordScopes,
                                                  ISystems<?, ?> system, UUID... identityToken) {
        if (recordScopes == null || recordScopes.isEmpty()) {
            return Uni.createFrom().voidItem();
        }
        log.debug("🔐 Applying scope-restricted security for {} records", recordScopes.size());

        Map<String, ISecurityToken<?, ?>> tokens = new LinkedHashMap<>();
        Uni<Void> resolve = resolveGroupFolderTokens(session, system, tokens, identityToken);

        IEnterprise<?, ?> enterprise = system.getEnterprise();
        IActiveFlag<?, ?> activeFlag = ((Systems) system).getActiveFlagID();

        // Snapshot the entries so the iteration order is stable inside the stateless transaction.
        List<Map.Entry<? extends IWarehouseCoreTable<?, ?, ?, ?>, ? extends ISecurityToken<?, ?>>> entries =
                new ArrayList<>(recordScopes.entrySet());

        return resolve.chain(() -> sessionFactory.withStatelessTransaction(statelessSession -> {
                    Uni<Long> chain = Uni.createFrom().item(0L);
                    for (Map.Entry<? extends IWarehouseCoreTable<?, ?, ?, ?>, ? extends ISecurityToken<?, ?>> entry : entries) {
                        IWarehouseCoreTable<?, ?, ?, ?> record = entry.getKey();
                        ISecurityToken<?, ?> scope = entry.getValue();
                        chain = chain.chain(runningTotal -> record
                                .createScopeRestrictedSecurity(statelessSession, system, enterprise, activeFlag, tokens, scope, identityToken)
                                .map(perRecord -> runningTotal + perRecord));
                    }
                    return chain;
                })
                .invoke(inserted -> log.debug("✅ Batched {} scope-restricted security rows across {} records",
                        inserted, entries.size()))
                .onFailure()
                .invoke(error -> log.error("❌ Error applying scope-restricted security: {}", error.getMessage(), error))
                .replaceWithVoid());
    }

    /**
     * Resolves the seven canonical group/folder tokens into {@code target}, keyed by the
     * {@code IWarehouseCoreTable.SECURITY_*} constants, in a single reactive chain.
     */
    private Uni<Void> resolveGroupFolderTokens(Mutiny.Session session, ISystems<?, ?> system,
                                               Map<String, ISecurityToken<?, ?>> target, UUID... identityToken) {
        return getAdministratorsFolder(session, system, identityToken)
                .invoke(t -> target.put(IWarehouseCoreTable.SECURITY_ADMINISTRATORS, t))
                .chain(() -> getEveryoneGroup(session, system, identityToken)
                        .invoke(t -> target.put(IWarehouseCoreTable.SECURITY_EVERYONE, t)))
                .chain(() -> getEverywhereGroup(session, system, identityToken)
                        .invoke(t -> target.put(IWarehouseCoreTable.SECURITY_EVERYWHERE, t)))
                .chain(() -> getSystemsFolder(session, system, identityToken)
                        .invoke(t -> target.put(IWarehouseCoreTable.SECURITY_SYSTEMS, t)))
                .chain(() -> getApplicationsFolder(session, system, identityToken)
                        .invoke(t -> target.put(IWarehouseCoreTable.SECURITY_APPLICATIONS, t)))
                .chain(() -> getPluginsFolder(session, system, identityToken)
                        .invoke(t -> target.put(IWarehouseCoreTable.SECURITY_PLUGINS, t)))
                .chain(() -> getGuestsFolder(session, system, identityToken)
                        .invoke(t -> target.put(IWarehouseCoreTable.SECURITY_GUESTS, t)))
                .replaceWithVoid();
    }

    /**
     * Writes default security for every row in {@code pending} in ONE stateless transaction.
     */
    private Uni<Void> batchInsertSecurity(List<IWarehouseCoreTable<?, ?, ?, ?>> pending, ISystems<?, ?> system,
                                          IEnterprise<?, ?> enterprise, IActiveFlag<?, ?> activeFlag,
                                          Map<String, ISecurityToken<?, ?>> tokens, String label, UUID... identityToken) {
        if (pending.isEmpty()) {
            log.debug("✅ No pending rows to secure for {}", label);
            return Uni.createFrom().voidItem();
        }
        return sessionFactory.withStatelessTransaction(statelessSession -> {
                    Uni<Long> chain = Uni.createFrom().item(0L);
                    for (IWarehouseCoreTable<?, ?, ?, ?> item : pending) {
                        chain = chain.chain(runningTotal -> item
                                .createDefaultSecurity(statelessSession, system, enterprise, activeFlag, tokens, identityToken)
                                .map(perRecord -> runningTotal + perRecord));
                    }
                    return chain;
                })
                .invoke(inserted -> log.debug("✅ Batched {} default-security rows across {} {} records",
                        inserted, pending.size(), label))
                .onFailure()
                .invoke(error -> log.error("❌ Error batch-securing {}: {}", label, error.getMessage(), error))
                .replaceWithVoid();
    }

    //@Transactional()
    @Override
    public Uni<Void> grantAccessToToken(Mutiny.Session session, ISecurityToken<?, ?> fromToken, ISecurityToken<?, ?> toToken,
                                        boolean create, boolean update, boolean delete, boolean read, ISystems<?, ?> system) {
        return grantAccessToToken(session, fromToken, toToken, create, update, delete, read, system, null, null, null);
    }

    //@Transactional()
    @Override
    public Uni<Void> grantAccessToToken(Mutiny.Session session, @NotNull ISecurityToken<?, ?> fromToken, @NotNull ISecurityToken<?, ?> toToken,
                                        boolean create, boolean update, boolean delete, boolean read,
                                        ISystems<?, ?> system, String originalId,
                                        Date effectiveFromDate, Date effectiveToDate) {
        SecurityTokensSecurityToken sta = new SecurityTokensSecurityToken();
        var enterprise = system.getEnterprise();
        return sta.builder(session)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .findBySecurityToken((SecurityToken) fromToken, (SecurityToken) toToken)
                .get()
                .onFailure(NoResultException.class)
                .recoverWithUni(() -> {
                    sta.setSystemID(system);
                    sta.setOriginalSourceSystemID(system);
                    sta.setEnterpriseID(enterprise);
                    sta.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                    return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                                sta.setActiveFlagID(activeFlag);
                                sta.setSecurityTokenID(fromToken);
                                sta.setBase((SecurityToken) toToken);
                                sta.setCreateAllowed(create);
                                sta.setUpdateAllowed(update);
                                sta.setDeleteAllowed(delete);
                                sta.setReadAllowed(read);
                                return session.persist(sta)
                                        .replaceWith(Uni.createFrom()
                                                .item(sta));
                            });
                })
                .chain(result -> Uni.createFrom()
                        .voidItem());
    }

    //@Transactional()
    @Override
    public Uni<ISecurityToken<?, ?>> create(Mutiny.Session session, String classificationValue, String name, String description, ISystems<?, ?> system) {
        return create(session, classificationValue, name, description, system, null);
    }

    //@Transactional()
    @Override
    public Uni<ISecurityToken<?, ?>> create(Mutiny.Session session, String classificationValue, String name, String description, ISystems<?, ?> system, ISecurityToken<?, ?> parent, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        log.debug("🔐 Creating security token: '{}' for system: '{}' with session: {}",
                name, system.getName(), session.hashCode());

        return classificationService.find(session, classificationValue, system, identityToken)
                .chain(classification -> {
                    SecurityToken st = new SecurityToken();

                    // First try to find by security token and enterprise
                    return st.builder(session)
                            .withEnterprise(enterprise)
                            .findBySecurityToken(name, enterprise)
                            .inActiveRange()
                            .inDateRange()
                            .withEnterprise(enterprise)
                            .get()
                            .onFailure(NoResultException.class)
                            .recoverWithNull()
                            .chain(existingToken -> {
                                if (existingToken != null) {
                                    log.debug("✅ Found existing security token: '{}' with ID: {}", existingToken.getName(), existingToken.getId());
                                    return Uni.createFrom()
                                            .item(existingToken);
                                }

                                // Try to find by name
                                return st.builder(session)
                                        .withName(name)
                                        .inActiveRange()
                                        .inDateRange()
                                        .get()
                                        .onFailure(NoResultException.class)
                                        .recoverWithNull()
                                        .chain(existingNameToken -> {
                                            if (existingNameToken != null) {
                                                log.debug("✅ Found existing token with name: '{}' with ID: {}", existingNameToken.getName(), existingNameToken.getId());
                                                return Uni.createFrom()
                                                        .item(existingNameToken);
                                            }

                                            log.debug("🆕 Creating new security token: '{}'", name);
                                            // Create new token
                                            st.setName(name);
                                            st.setDescription(description);
                                            st.setSystemID(system);
                                            st.setSecurityToken(UUID.randomUUID()
                                                    .toString());
                                            st.setEnterpriseID(enterprise);
                                            st.setSystemID(((Classification) classification).getSystemID());
                                            st.setOriginalSourceSystemID(((Classification) classification).getSystemID());
                                            st.setSecurityTokenClassificationID((Classification) classification);

                                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                            return acService.getActiveFlag(session, enterprise)
                                                    .chain(activeFlag -> {
                                                        st.setActiveFlagID(activeFlag);
                                                        return session.persist(st)
                                                                .replaceWith(Uni.createFrom()
                                                                        .item(st))
                                                                .chain(persisted -> {
                                                                    // Start createDefaultSecurity in parallel without waiting for it
                                                                    return persisted.createDefaultSecurity(session, system, identityToken);
                                                                }).replaceWith(st);
                                                    });
                                        });
                            })
                            .chain(securityToken -> {
                                if (parent == null) {
                                    return Uni.createFrom()
                                            .item(securityToken);
                                }

                                return link(session, parent, securityToken, classification)
                                        .map(v -> securityToken);
                            });
                });
    }

    //@Transactional()
    @Override
    public Uni<Void> link(Mutiny.Session session, ISecurityToken<?, ?> parent, ISecurityToken<?, ?> child, IClassification<?, ?> classification, String... identifyingToken) {
        SecurityTokenXSecurityToken root = new SecurityTokenXSecurityToken();
        var enterprise = child.getEnterprise();
        log.debug("🔗 Linking security tokens: parent '{}' -> child '{}' with session: {}",
                parent.getName(), child.getName(), session.hashCode());

        // Enforce the canonical membership policy: once the base hierarchy is built the type folders are
        // locked down — Systems accepts only System-typed tokens, Applications only Application-typed
        // (always involved parties), Plugins only Plugin-typed; conversely System/Application/Plugin
        // tokens may only be parented under their matching folder (or the enterprise root during build).
        // Generic groups/folders may add further groups and users, but never into the type folders.
        //
        // The enterprise name is resolved reactively first: child.getEnterprise() may be an uninitialized
        // bytecode-lazy association under the reactive session, and accessing it synchronously inside the
        // policy check throws HR000085 (reactive sessions do not support transparent lazy fetching).
        return resolveEnterpriseName(session, enterprise).chain(enterpriseName -> {
            try {
                enforceMembershipPolicy(enterpriseName, parent, child, classification);
            } catch (SecurityAccessException policyViolation) {
                log.warn("⛔ Rejected security-token link parent '{}' -> child '{}': {}",
                        parent.getName(), child.getName(), policyViolation.getMessage());
                return Uni.createFrom().<Void>failure(policyViolation);
            }

            return root.builder(session)
                    .withEnterprise(enterprise)
                    .findLink((SecurityToken) parent, (SecurityToken) child, null)
                    .withClassification(classification)
                    .inActiveRange()
                    .inDateRange()
                    .get()
                    .onFailure(NoResultException.class)
                    .recoverWithUni(() -> {
                        log.debug("🆕 Creating new security token link: parent '{}' -> child '{}'", parent.getName(), child.getName());
                        // No existing link found, create a new one
                        root.setParentSecurityTokenID((SecurityToken) parent);
                        root.setChildSecurityTokenID((SecurityToken) child);
                        root.setClassificationID(classification);
                        root.setSystemID(((SecurityToken) parent).getSystemID());
                        root.setOriginalSourceSystemID(((SecurityToken) parent).getSystemID());
                        root.setValue(child.getSecurityToken());
                        root.setEnterpriseID(enterprise);

                        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                        return acService.getActiveFlag(session, enterprise)
                                .chain(activeFlag -> {
                                    root.setActiveFlagID(activeFlag);
                                    return session.persist(root)
                                            .replaceWith(Uni.createFrom()
                                                    .item(root))
                                            .invoke(v -> {
                                                updateSecurityHierarchy(child.getId());
                                            });
                                });
                    })
                    .onItem()
                    .invoke(existingLink -> {
                        if (existingLink != null) {
                            log.debug("✅ Found existing security token link: parent '{}' -> child '{}'", parent.getName(), child.getName());
                        }
                    })
                    .chain(existingLink -> Uni.createFrom()
                            .voidItem());
        });
    }

    private void updateSecurityHierarchy(UUID securityTokenID) {
        //TODO hierarchy updates? i wonder
    }

    // ============================================================================================
    // Stateless security-bootstrap write primitives. Existence is checked with a scalar getCount();
    // missing rows are written with session.insert. Prepped reference reads (scalar projection) keep
    // the @Cacheable entities off the stateless entity-load path.
    // ============================================================================================

    private Uni<ISecurityToken<?, ?>> findTokenByNameStateless(Mutiny.StatelessSession session, String name, IEnterprise<?, ?> enterprise) {
        return new SecurityToken().builder(session)
                .withName(name)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                .selectColumn(SecurityToken_.id)
                .selectColumn(SecurityToken_.securityToken)
                .selectColumn(SecurityToken_.name)
                .selectColumn(SecurityToken_.description)
                .get(Object[].class)
                .map(row -> {
                    SecurityToken prepped = new SecurityToken((UUID) row[0], (String) row[1], (String) row[2], (String) row[3], null);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setSystemID(null);
                    prepped.setFake(false);
                    return (ISecurityToken<?, ?>) prepped;
                });
    }

    @Override
    public Uni<Void> grantAccessToToken(Mutiny.StatelessSession session, ISecurityToken<?, ?> fromToken, ISecurityToken<?, ?> toToken,
                                        boolean create, boolean update, boolean delete, boolean read, ISystems<?, ?> system) {
        SecurityTokensSecurityToken sta = new SecurityTokensSecurityToken();
        var enterprise = system.getEnterprise();
        return sta.builder(session)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .findBySecurityToken((SecurityToken) fromToken, (SecurityToken) toToken)
                .getCount()
                .chain(count -> {
                    if (count != null && count > 0) {
                        return Uni.createFrom().voidItem();
                    }
                    sta.setSystemID(system);
                    sta.setOriginalSourceSystemID(system);
                    sta.setEnterpriseID(enterprise);
                    sta.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                    return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                                sta.setActiveFlagID(activeFlag);
                                sta.setSecurityTokenID(fromToken);
                                sta.setBase((SecurityToken) toToken);
                                sta.setCreateAllowed(create);
                                sta.setUpdateAllowed(update);
                                sta.setDeleteAllowed(delete);
                                sta.setReadAllowed(read);
                                return session.insert(sta).replaceWithVoid();
                            });
                });
    }

    @Override
    public Uni<Void> link(Mutiny.StatelessSession session, ISecurityToken<?, ?> parent, ISecurityToken<?, ?> child, IClassification<?, ?> classification, String... identifyingToken) {
        SecurityTokenXSecurityToken root = new SecurityTokenXSecurityToken();
        var enterprise = child.getEnterprise();
        // The enterprise name is available synchronously on the prepped/created token (its enterprise is
        // wired at create-time), so no managed lazy resolution is needed for the membership policy.
        String enterpriseName = enterprise == null ? null : enterprise.getName();
        try {
            enforceMembershipPolicy(enterpriseName, parent, child, classification);
        } catch (SecurityAccessException policyViolation) {
            log.warn("⛔ (stateless) Rejected security-token link parent '{}' -> child '{}': {}",
                    parent.getName(), child.getName(), policyViolation.getMessage());
            return Uni.createFrom().<Void>failure(policyViolation);
        }
        return root.builder(session)
                .withEnterprise(enterprise)
                .findLink((SecurityToken) parent, (SecurityToken) child, null)
                .withClassification(classification)
                .inActiveRange()
                .inDateRange()
                .getCount()
                .chain(count -> {
                    if (count != null && count > 0) {
                        return Uni.createFrom().voidItem();
                    }
                    root.setParentSecurityTokenID((SecurityToken) parent);
                    root.setChildSecurityTokenID((SecurityToken) child);
                    root.setClassificationID(classification);
                    root.setSystemID(((SecurityToken) parent).getSystemID());
                    root.setOriginalSourceSystemID(((SecurityToken) parent).getSystemID());
                    root.setValue(child.getSecurityToken());
                    root.setEnterpriseID(enterprise);
                    IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                    return acService.getActiveFlag(session, enterprise)
                            .chain(activeFlag -> {
                                root.setActiveFlagID(activeFlag);
                                return session.insert(root).replaceWithVoid();
                            });
                });
    }

    @Override
    public Uni<ISecurityToken<?, ?>> create(Mutiny.StatelessSession session, String classificationValue, String name, String description, ISystems<?, ?> system) {
        return create(session, classificationValue, name, description, system, null);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> create(Mutiny.StatelessSession session, String classificationValue, String name, String description, ISystems<?, ?> system, ISecurityToken<?, ?> parent, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return classificationService.find(session, classificationValue, system, identityToken)
                .chain(classification -> findTokenByNameStateless(session, name, enterprise)
                        .onFailure()
                        .recoverWithUni(err -> {
                            SecurityToken st = new SecurityToken();
                            st.setName(name);
                            st.setDescription(description);
                            st.setSecurityToken(UUID.randomUUID().toString());
                            st.setEnterpriseID(enterprise);
                            st.setSystemID(((Classification) classification).getSystemID());
                            st.setOriginalSourceSystemID(((Classification) classification).getSystemID());
                            st.setSecurityTokenClassificationID((Classification) classification);
                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                            return acService.getActiveFlag(session, enterprise)
                                    .chain(activeFlag -> {
                                        st.setActiveFlagID(activeFlag);
                                        return st.builder(session).persist(st)
                                                .chain(persisted -> resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                        .chain(tokens -> st.createDefaultSecurity(session, system, enterprise, activeFlag, tokens))
                                                        .onFailure().recoverWithItem(0L)
                                                        .replaceWith((ISecurityToken<?, ?>) st));
                                    });
                        })
                        .chain(securityToken -> {
                            if (parent == null) {
                                return Uni.createFrom().item(securityToken);
                            }
                            return link(session, parent, securityToken, classification).replaceWith(securityToken);
                        }));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<Void> applyDefaultSecurityToTable(Mutiny.StatelessSession session, IWarehouseCoreTable<?, ?, ?, ?> table, ISystems<?, ?> system, UUID... identityToken) {
        IEnterprise<?, ?> enterprise = system.getEnterprise();
        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        WarehouseCoreTable<?, ?, ?, ?> proto = (WarehouseCoreTable<?, ?, ?, ?>) table;
        return resolveDefaultGroupFolderTokens(session, system, identityToken)
                .chain(tokens -> acService.getActiveFlag(session, enterprise, identityToken)
                        .chain(activeFlag -> proto.allInDateRowIds(session)
                                .chain(ids -> {
                                    if (ids == null || ids.isEmpty()) {
                                        return Uni.createFrom().voidItem();
                                    }
                                    Uni<Void> chain = Uni.createFrom().voidItem();
                                    for (Object idObj : ids) {
                                        final UUID rid = (UUID) idObj;
                                        chain = chain.chain(() -> {
                                            WarehouseCoreTable lean = newLeanEntity(table, rid);
                                            return lean.countDefaultSecurity(session)
                                                    .chain(cntObj -> {
                                                        Long cnt = (Long) cntObj;
                                                        return (cnt != null && cnt > 0L)
                                                                ? Uni.createFrom().voidItem()
                                                                : ((IWarehouseCoreTable) lean)
                                                                .createDefaultSecurity(session, system, enterprise, activeFlag, tokens)
                                                                .replaceWithVoid();
                                                    });
                                        });
                                    }
                                    return chain;
                                })));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static WarehouseCoreTable newLeanEntity(IWarehouseCoreTable<?, ?, ?, ?> proto, UUID id) {
        try {
            WarehouseCoreTable lean = (WarehouseCoreTable) proto.getClass().getDeclaredConstructor().newInstance();
            ((WarehouseBaseTable) lean).setId(id);
            return lean;
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException("Cannot instantiate lean entity for " + proto.getClass().getSimpleName(), ex);
        }
    }

    //@Transactional()
    @Override
    public Uni<Void> moveToken(Mutiny.Session session, ISecurityToken<?, ?> oldParent, ISecurityToken<?, ?> newParent,
                               ISecurityToken<?, ?> child, IClassification<?, ?> classification, String... identifyingToken) {
        var enterprise = child.getEnterprise();
        log.debug("🔀 Moving security token '{}' from '{}' to '{}'", child.getName(),
                oldParent != null ? oldParent.getName() : "<all parents>", newParent.getName());

        // Enforce the canonical membership policy on the destination BEFORE mutating anything, so an
        // illegal move fails without having closed any existing edge. The enterprise name is resolved
        // reactively first (see link()) to avoid HR000085 on a lazy enterprise association.
        return resolveEnterpriseName(session, enterprise).chain(enterpriseName -> {
            try {
                enforceMembershipPolicy(enterpriseName, newParent, child, classification);
            } catch (SecurityAccessException policyViolation) {
                log.warn("⛔ Rejected security-token move of '{}' to '{}': {}",
                        child.getName(), newParent.getName(), policyViolation.getMessage());
                return Uni.createFrom().<Void>failure(policyViolation);
            }

            // Find the child's current, in-range parent edges (optionally narrowed to a single oldParent).
            SecurityTokenXSecurityToken edge = new SecurityTokenXSecurityToken();
            return edge.builder(session)
                    .withEnterprise(enterprise)
                    .findLink(oldParent == null ? null : (SecurityToken) oldParent, (SecurityToken) child, null)
                    .inActiveRange()
                    .inDateRange()
                    .getAll()
                    .chain(edges -> {
                        java.time.OffsetDateTime now = com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD
                                .convertToUTCDateTime(com.entityassist.RootEntity.getNow());
                        // Collect the edges that must be closed (skip any that already point at the new parent —
                        // an idempotent move/no-op).
                        java.util.List<UUID> toClose = new java.util.ArrayList<>();
                        for (Object next : edges) {
                            SecurityTokenXSecurityToken existing = (SecurityTokenXSecurityToken) next;
                            SecurityToken existingParent = existing.getParentSecurityTokenID();
                            if (existingParent != null && newParent.getId() != null
                                    && newParent.getId().equals(existingParent.getId())) {
                                continue;
                            }
                            toClose.add(existing.getId());
                        }
                        if (toClose.isEmpty()) {
                            return Uni.createFrom().voidItem();
                        }
                        // Close the edges with a direct bulk mutation rather than mutate-and-merge. EffectiveToDate
                        // is a @Basic(fetch=LAZY) column; under a reactive session Hibernate's merge-driven dirty
                        // tracking does not reliably emit the UPDATE for a lazy basic attribute, which would leave
                        // the old parent edge open (EffectiveToDate still EndOfTime) in the DB even though it was
                        // "closed" in the session — so the moved child would surface under both the old and new
                        // parent. A bulk HQL update writes straight to the DB, guaranteeing the closure is durable
                        // before the new edge is created and before getApplicableSecurityTokenIds reads it back.
                        return session.createMutationQuery(
                                        "update SecurityTokenXSecurityToken e set e.effectiveToDate = :now, " +
                                                "e.warehouseLastUpdatedTimestamp = :now where e.id in (:ids)")
                                .setParameter("now", now)
                                .setParameter("ids", toClose)
                                .executeUpdate()
                                .replaceWithVoid();
                    })
                    // Create (or reuse) the new parent edge.
                    .chain(() -> link(session, newParent, child, classification, identifyingToken));
        });
    }

    /**
     * Enforces the canonical security-hierarchy membership policy on a parent &rarr; child link.
     *
     * <p>Once the base security hierarchy is built the root and the default groups/folders are
     * structurally read-only (only the administrators group may restructure them). The type folders
     * are additionally constrained by the <em>type</em> of token they accept:</p>
     *
     * <ul>
     *   <li><strong>Systems</strong> folder — accepts only {@code System}-typed tokens.</li>
     *   <li><strong>Applications</strong> folder — accepts only {@code Application}-typed tokens
     *       (which are always involved parties).</li>
     *   <li><strong>Plugins</strong> folder — accepts only {@code Plugin}-typed tokens.</li>
     *   <li>Conversely a {@code System}/{@code Application}/{@code Plugin}-typed token may only be
     *       parented under its matching folder (or under the enterprise root while the canonical tree
     *       is first being built).</li>
     *   <li>Generic groups/folders may add further groups and users (membership types), but never into
     *       the type folders — so a group can add groups/users <em>except</em> for the Systems folder.</li>
     * </ul>
     *
     * @throws SecurityAccessException when the link would violate the policy.
     */
    private void enforceMembershipPolicy(String enterpriseName, ISecurityToken<?, ?> parent,
                                         ISecurityToken<?, ?> child, IClassification<?, ?> classification) {
        if (parent == null || classification == null) {
            return;
        }
        String childType = classification.getName();
        if (childType == null) {
            return;
        }
        String parentName = parent.getName();

        boolean parentIsRoot = enterpriseName != null && enterpriseName.equals(parentName);
        boolean parentIsSystemsFolder = UserGroupSecurityTokenClassifications.System.toString().equals(parentName);
        boolean parentIsApplicationsFolder = UserGroupSecurityTokenClassifications.Applications.toString().equals(parentName);
        boolean parentIsPluginsFolder = UserGroupSecurityTokenClassifications.Plugins.toString().equals(parentName);

        if (UserGroupSecurityTokenClassifications.System.toString().equals(childType)) {
            if (!parentIsSystemsFolder && !parentIsRoot) {
                throw new SecurityAccessException(
                        "System-typed security tokens may only be added under the Systems folder (parent was '" + parentName + "')");
            }
        } else if (Application.toString().equals(childType)) {
            if (!parentIsApplicationsFolder && !parentIsRoot) {
                throw new SecurityAccessException(
                        "Application-typed security tokens may only be added under the Applications folder (parent was '" + parentName + "')");
            }
        } else if (Plugin.toString().equals(childType)) {
            if (!parentIsPluginsFolder && !parentIsRoot) {
                throw new SecurityAccessException(
                        "Plugin-typed security tokens may only be added under the Plugins folder (parent was '" + parentName + "')");
            }
        } else {
            // Membership types (UserGroup, User, Guests, Visitors, Registered, Identity): groups/folders
            // may add further groups and users, but never into the locked type folders.
            if (parentIsSystemsFolder) {
                throw new SecurityAccessException(
                        "The Systems folder only accepts System-typed tokens; groups/users cannot be added to it");
            }
            if (parentIsApplicationsFolder) {
                throw new SecurityAccessException(
                        "The Applications folder only accepts Application-typed tokens");
            }
            if (parentIsPluginsFolder) {
                throw new SecurityAccessException(
                        "The Plugins folder only accepts Plugin-typed tokens");
            }
        }
    }

    /**
     * Reactively resolves an enterprise's name without tripping HR000085.
     * <p>
     * The enterprise reference handed to {@link #link}/{@link #moveToken} (via {@code child.getEnterprise()})
     * can be an uninitialized bytecode-lazy association under the reactive session. Reading any of its
     * attributes synchronously throws {@link org.hibernate.LazyInitializationException} (HR000085). The
     * id is always available on the (enhanced) proxy without a DB hit, so the enterprise is reloaded by id
     * with {@link Mutiny.Session#find(Class, Object)} — which fully hydrates the {@code EAGER} name column —
     * and the name read off the now-managed instance. {@code find} is used in preference to
     * {@code session.fetch(...)} because the latter does not reliably initialise a bytecode
     * <em>EnhancementAsProxy</em> entity (only classic {@code HibernateProxy}/collection associations).
     *
     * @param session    the active reactive session
     * @param enterprise the (possibly lazy) enterprise reference
     * @return a Uni emitting the enterprise name, or {@code null} when no enterprise is supplied
     */
    private Uni<String> resolveEnterpriseName(Mutiny.Session session, IEnterprise<?, ?> enterprise) {
        if (enterprise == null) {
            return Uni.createFrom().nullItem();
        }
        return session.find(com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise.class, enterprise.getId())
                .map(found -> found == null ? null : found.getName());
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getEveryoneGroup(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(UserGroup.toString(), system, identityToken)
                .withName(Everyone)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                //   .canRead(enterprise, identityToken)
                .get()
                .onItem()
                .transform(token -> (ISecurityToken<?, ?>) token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getEverywhereGroup(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(UserGroup.toString(), system, identityToken)
                .withName(Everywhere)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                //      .canRead(enterprise, identityToken)
                .get()
                .onItem()
                .transform(token -> (ISecurityToken<?, ?>) token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getGuestsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(UserGroup.toString(), system, identityToken)
                .withName(Guests)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                //     .canRead(enterprise,identityToken)
                .get()
                .onItem()
                .transform(token -> (ISecurityToken<?, ?>) token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getRegisteredGuestsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(UserGroup.toString(), system, identityToken)
                .withName(Registered)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                //    .canRead(enterprise, identityToken)
                .get()
                .onItem()
                .transform(token -> (ISecurityToken<?, ?>) token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getVisitorsGuestsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(UserGroup.toString(), system, identityToken)
                .withName(Visitors)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                //     .canRead(enterprise, identityToken)
                .get()
                .onItem()
                .transform(token -> (ISecurityToken<?, ?>) token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getAdministratorsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(UserGroup.toString(), system, identityToken)
                .withName(Administrators)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                //  .canRead(enterprise, identityToken)
                .get()
                .onItem()
                .transform(token -> (ISecurityToken<?, ?>) token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getSystemsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(UserGroupSecurityTokenClassifications.System.toString(), system, identityToken)
                .withName(System)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                //.canRead(enterprise, identityToken)
                .get()
                .onItem()
                .transform(token ->  token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getPluginsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(Plugin.toString(), system, identityToken)
                .withName(Plugins)
                .inActiveRange()
                //  .canRead(enterprise, identityToken)
                .inDateRange()
                .withEnterprise(enterprise)
                .get()
                .onItem()
                .transform(token -> token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getApplicationsFolder(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken) {
        SecurityToken st = new SecurityToken();
        var enterprise = system.getEnterprise();
        return st.builder(session)
                .findFolder(Application.toString(), system, identityToken)
                .withName(Applications)
                .inActiveRange()
                //   .canRead(enterprise, identityToken)
                .inDateRange()
                .withEnterprise(enterprise)
                .get()
                .onItem()
                .transform(token -> token);
    }

    // =============================================================================================
    // Stateless "fetch ids/scalars + prep" folder/group resolvers. Same filters as the managed
    // getters above (findFolder + withName + enterprise + ranges), but project the token's OWN scalar
    // columns (id, securityToken, name, description) and build a DETACHED SecurityToken from its 5-arg
    // constructor — a scalar multiselect, never an entity result — wiring the enterprise reference from
    // system.getEnterprise(). These return the pre-resolved tokens consumed by the stateless default-
    // security insert API, so the canonical grant matrix can be resolved + written on one stateless unit.
    // =============================================================================================

    private Uni<ISecurityToken<?, ?>> findFolderTokenStateless(Mutiny.StatelessSession session, String folderType,
                                                               String name, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return new SecurityToken().builder(session)
                .findFolder(folderType, system, identityToken)
                .withName(name)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                .selectColumn(SecurityToken_.id)
                .selectColumn(SecurityToken_.securityToken)
                .selectColumn(SecurityToken_.name)
                .selectColumn(SecurityToken_.description)
                .get(Object[].class)
                .map(row -> {
                    SecurityToken prepped = new SecurityToken(
                            (UUID) row[0],
                            (String) row[1],
                            (String) row[2],
                            (String) row[3],
                            null);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return (ISecurityToken<?, ?>) prepped;
                });
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getEveryoneGroup(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, UserGroup.toString(), Everyone.toString(), system, identityToken);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getEverywhereGroup(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, UserGroup.toString(), Everywhere.toString(), system, identityToken);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getGuestsFolder(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, UserGroup.toString(), Guests.toString(), system, identityToken);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getAdministratorsFolder(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, UserGroup.toString(), Administrators.toString(), system, identityToken);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getSystemsFolder(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, UserGroupSecurityTokenClassifications.System.toString(), System.toString(), system, identityToken);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getPluginsFolder(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, Plugin.toString(), Plugins.toString(), system, identityToken);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getApplicationsFolder(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, Application.toString(), Applications.toString(), system, identityToken);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.Session session, UUID identifyingToken, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return new SecurityToken().builder(session)
                .findBySecurityToken(identifyingToken.toString())
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .withEnterprise(enterprise)
                // .canRead(enterprise, identityToken)
                .get()
                .onFailure(NoResultException.class)
                .recoverWithNull()
                .onItem()
                .transform(token -> token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.Session session, UUID identifyingToken, boolean overrideActiveFlag, ISystems<?, ?> system, UUID... identityToken) {
        SecurityTokenQueryBuilder builder = new SecurityToken().builder(session);
        var enterprise = system.getEnterprise();
        builder = builder.findBySecurityToken(identifyingToken.toString())
                .withEnterprise(enterprise)
                .inDateRange();
        if (overrideActiveFlag) {
            builder.inActiveRange();
        }

        return builder
                .get()
                .onFailure(NoResultException.class)
                .recoverWithNull()
                .onItem()
                .transform(token -> (ISecurityToken<?, ?>) token);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getSecurityTokenByName(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return new SecurityToken().builder(session)
                .withName(name)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .get()
                .onFailure(NoResultException.class)
                .recoverWithNull()
                .onItem()
                .transform(token -> token);
    }
}

