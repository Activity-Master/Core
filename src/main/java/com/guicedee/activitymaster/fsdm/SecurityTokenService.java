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
    /**
     * Cache of detached, immutable bootstrap folder/group tokens (Administrators, Everyone, Everywhere,
     * Guests, System, Plugins, Applications) resolved on a stateless session, keyed by systemId then
     * folderType|name. Safe because the stateless folder resolver returns a fresh DETACHED SecurityToken
     * (scalar projection, no persistence context) and these canonical structures never change for the JVM
     * lifetime. Generic, user-created tokens are NOT cached (they can be created/renamed/retired), so only
     * these stable folder/group tokens are cached.
     */
    private static final Map<UUID, Map<String, ISecurityToken<?, ?>>> FOLDER_TOKEN_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private Mutiny.SessionFactory sessionFactory;

    @Override
    public ISecurityToken<?, ?> get() {
        return new SecurityToken();
    }

    @Override
    public Uni<Void> applyDefaultSecurityToRows(Mutiny.StatelessSession session,
                                                java.util.Collection<? extends IWarehouseCoreTable<?, ?, ?, ?>> rows,
                                                ISystems<?, ?> system, UUID... identityToken) {
        if (rows == null || rows.isEmpty()) {
            return Uni.createFrom().voidItem();
        }
        IEnterprise<?, ?> enterprise = system.getEnterprise();
        IActiveFlagService<?> activeFlags = IGuiceContext.get(IActiveFlagService.class);
        List<IWarehouseCoreTable<?, ?, ?, ?>> pending = new ArrayList<>(rows);
        return resolveDefaultGroupFolderTokens(session, system, identityToken)
                .chain(tokens -> activeFlags.getActiveFlag(session, enterprise, identityToken).chain(activeFlag -> {
                    Uni<Long> chain = Uni.createFrom().item(0L);
                    for (IWarehouseCoreTable<?, ?, ?, ?> item : pending) {
                        chain = chain.chain(total -> item
                                .createDefaultSecurity(session, system, enterprise, activeFlag, tokens, identityToken)
                                .map(per -> total + per));
                    }
                    return chain;
                }))
                .onFailure().invoke(error -> log.error("❌ Error batch-securing rows (stateless): {}", error.getMessage(), error))
                .replaceWithVoid();
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getSecurityTokenByName(Mutiny.StatelessSession session, String name, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        return new SecurityToken().builder(session)
                .withName(name)
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .selectColumn(SecurityToken_.id)
                .selectColumn(SecurityToken_.securityToken)
                .selectColumn(SecurityToken_.name)
                .selectColumn(SecurityToken_.description)
                .get(Object[].class)
                .map(row -> {
                    if (row == null) return null;
                    SecurityToken prepped = new SecurityToken((UUID) row[0], (String) row[1], (String) row[2], (String) row[3], null);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return prepped;
                });
    }

    /**
     * Stateless batch variant of
     * {@link #applyScopeRestrictedSecurity(Mutiny.StatelessSession, Map, ISystems, UUID...)}. Writes the
     * scope-restricted matrix for every {@code (record → scopeToken)} pair directly on the supplied
     * {@link Mutiny.StatelessSession} — it does <strong>not</strong> open its own transaction (so it must be
     * called from a flow that already owns a stateless transaction, avoiding the nested-tx FK-visibility
     * trap). The seven group/folder tokens and the active flag are resolved once (stateless prepped reads)
     * and reused for every record. Because records are written sequentially on one session, callers wanting
     * parallelism should partition the records across independent stateless sessions/transactions.
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<Void> applyScopeRestrictedSecurity(Mutiny.StatelessSession session,
                                                  Map<? extends IWarehouseCoreTable<?, ?, ?, ?>, ? extends ISecurityToken<?, ?>> recordScopes,
                                                  ISystems<?, ?> system, UUID... identityToken) {
        if (recordScopes == null || recordScopes.isEmpty()) {
            return Uni.createFrom().voidItem();
        }
        log.debug(" Applying scope-restricted security (stateless) for {} records", recordScopes.size());

        IEnterprise<?, ?> enterprise = system.getEnterprise();
        IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);

        // Snapshot the entries so the iteration order is stable inside the stateless transaction.
        List<Map.Entry<? extends IWarehouseCoreTable<?, ?, ?, ?>, ? extends ISecurityToken<?, ?>>> entries =
                new ArrayList<>(recordScopes.entrySet());

        return resolveDefaultGroupFolderTokens(session, system, identityToken)
                .chain(tokens -> acService.getActiveFlag(session, enterprise, identityToken)
                        .chain(activeFlag -> {
                            Uni<Long> chain = Uni.createFrom().item(0L);
                            for (Map.Entry<? extends IWarehouseCoreTable<?, ?, ?, ?>, ? extends ISecurityToken<?, ?>> entry : entries) {
                                IWarehouseCoreTable<?, ?, ?, ?> record = entry.getKey();
                                ISecurityToken<?, ?> scope = entry.getValue();
                                chain = chain.chain(runningTotal -> record
                                        .createScopeRestrictedSecurity(session, system, enterprise, activeFlag, tokens, scope, identityToken)
                                        .map(perRecord -> runningTotal + perRecord));
                            }
                            return chain;
                        }))
                .invoke(inserted -> log.debug("✅ Batched {} scope-restricted security rows across {} records (stateless)",
                        inserted, entries.size()))
                .onFailure()
                .invoke(error -> log.error("❌ Error applying stateless scope-restricted security: {}", error.getMessage(), error))
                .replaceWithVoid();
    }

    /**
     * Resolves the seven canonical group/folder tokens into {@code target}, keyed by the
     * {@code IWarehouseCoreTable.SECURITY_*} constants, in a single reactive chain.
     */
    private Uni<Void> resolveGroupFolderTokens(Mutiny.StatelessSession session, ISystems<?, ?> system,
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

    
    
    @Override
    public Uni<Void> grantAccessToToken(Mutiny.StatelessSession session, @NotNull ISecurityToken<?, ?> fromToken, @NotNull ISecurityToken<?, ?> toToken,
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
                                return session.insert(sta)
                                        .replaceWith(Uni.createFrom()
                                                .item(sta));
                            });
                })
                .chain(result -> Uni.createFrom()
                        .voidItem());
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
                    return prepped;
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
                    // The owning system FK: prefer the parent's, but fall back to the child's because prepped
                    // folder/group tokens (scalar-projected) carry a null systemID, while the freshly-created
                    // token in the pair always has it set — avoids a null securitytokenxsecuritytoken.systemid.
                    ISystems<?, ?> linkSystem = ((SecurityToken) parent).getSystemID() != null
                            ? ((SecurityToken) parent).getSystemID()
                            : ((SecurityToken) child).getSystemID();
                    root.setSystemID(linkSystem);
                    root.setOriginalSourceSystemID(linkSystem);
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
                            // Use the system parameter for the FK: the stateless prepped classification does not
                            // carry its systemID (the projection omits it), so reading it back would be NULL and
                            // violate the securitytoken.systemid not-null constraint.
                            st.setSystemID(system);
                            st.setOriginalSourceSystemID(system);
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

    
    @Override
    public Uni<Void> moveToken(Mutiny.StatelessSession session, ISecurityToken<?, ?> oldParent, ISecurityToken<?, ?> newParent,
                               ISecurityToken<?, ?> child, IClassification<?, ?> classification, String... identifyingToken) {
        var enterprise = child.getEnterprise();
        log.debug(" (stateless) Moving security token '{}' from '{}' to '{}'", child.getName(),
                oldParent != null ? oldParent.getName() : "<all parents>", newParent.getName());

        // Prepped/created tokens carry their enterprise, so the membership-policy name is available
        // synchronously (mirrors the stateless link()) — no managed lazy resolution needed. Enforce the
        // policy BEFORE mutating anything so an illegal move fails without having closed any edge.
        String enterpriseName = enterprise == null ? null : enterprise.getName();
        try {
            enforceMembershipPolicy(enterpriseName, newParent, child, classification);
        } catch (SecurityAccessException policyViolation) {
            log.warn("⛔ (stateless) Rejected security-token move of '{}' to '{}': {}",
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
                    Uni<Void> close = Uni.createFrom().voidItem();
                    for (Object next : edges) {
                        SecurityTokenXSecurityToken existing = (SecurityTokenXSecurityToken) next;
                        SecurityToken existingParent = existing.getParentSecurityTokenID();
                        // Skip any edge that already points at the new parent — an idempotent move/no-op.
                        if (existingParent != null && newParent.getId() != null
                                && newParent.getId().equals(existingParent.getId())) {
                            continue;
                        }
                        // Stateless has no dirty tracking and bulk HQL createMutationQuery is blocked on a
                        // stateless session, so close each edge with a full-row session.update — it writes
                        // every column by id, so the lazy effectiveToDate is persisted reliably before the
                        // new edge is created (and before getApplicableSecurityTokenIds reads it back).
                        existing.setEffectiveToDate(now);
                        close = close.chain(() -> session.update(existing).replaceWithVoid());
                    }
                    return close;
                })
                // Create (or reuse) the new parent edge.
                .chain(() -> link(session, newParent, child, classification, identifyingToken));
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
     * with {@link Mutiny.StatelessSession#find(Class, Object)} — which fully hydrates the {@code EAGER} name column —
     * and the name read off the now-managed instance. {@code find} is used in preference to
     * {@code session.fetch(...)} because the latter does not reliably initialise a bytecode
     * <em>EnhancementAsProxy</em> entity (only classic {@code HibernateProxy}/collection associations).
     *
     * @param session    the active reactive session
     * @param enterprise the (possibly lazy) enterprise reference
     * @return a Uni emitting the enterprise name, or {@code null} when no enterprise is supplied
     */
    private Uni<String> resolveEnterpriseName(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise) {
        if (enterprise == null) {
            return Uni.createFrom().nullItem();
        }
        return session.get(com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise.class, enterprise.getId())
                .map(found -> found == null ? null : found.getName());
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
        UUID systemId = system.getId();
        String cacheKey = folderType + "|" + name;
        Map<String, ISecurityToken<?, ?>> byKey = FOLDER_TOKEN_CACHE.computeIfAbsent(systemId, k -> new java.util.concurrent.ConcurrentHashMap<>());
        ISecurityToken<?, ?> cached = byKey.get(cacheKey);
        if (cached != null) {
            return Uni.createFrom().item((ISecurityToken<?, ?>) cached);
        }
        Uni<ISecurityToken<?, ?>> resolved = new SecurityToken().builder(session)
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
        return resolved.onItem().invoke(tok -> {
            if (tok != null && tok.getId() != null) {
                byKey.put(cacheKey, tok);
            }
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
    public Uni<ISecurityToken<?, ?>> getRegisteredGuestsFolder(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, UserGroup.toString(), Registered.toString(), system, identityToken);
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getVisitorsGuestsFolder(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID... identityToken) {
        return findFolderTokenStateless(session, UserGroup.toString(), Visitors.toString(), system, identityToken);
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
    public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.StatelessSession session, UUID identifyingToken, ISystems<?, ?> system, UUID... identityToken) {
        var enterprise = system.getEnterprise();
        // Prepped stateless read (scalar projection into a lean detached SecurityToken) — mirrors the
        // stateless getSecurityTokenByName, keyed on the SecurityToken varchar identity.
        return new SecurityToken().builder(session)
                .findBySecurityToken(identifyingToken.toString())
                .withEnterprise(enterprise)
                .inActiveRange()
                .inDateRange()
                .selectColumn(SecurityToken_.id)
                .selectColumn(SecurityToken_.securityToken)
                .selectColumn(SecurityToken_.name)
                .selectColumn(SecurityToken_.description)
                .get(Object[].class)
                .map(row -> {
                    if (row == null) return null;
                    SecurityToken prepped = new SecurityToken((UUID) row[0], (String) row[1], (String) row[2], (String) row[3], null);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return prepped;
                });
    }

    @Override
    public Uni<ISecurityToken<?, ?>> getSecurityToken(Mutiny.StatelessSession session, UUID identifyingToken, boolean overrideActiveFlag, ISystems<?, ?> system, UUID... identityToken) {
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

}

