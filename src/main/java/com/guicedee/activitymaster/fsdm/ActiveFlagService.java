package com.guicedee.activitymaster.fsdm;

/**
 * Reactivity Migration Checklist:
 * <p>
 * [✓] One action per Mutiny.StatelessSession at a time
 * - All operations on a session are sequential
 * - No parallel operations on the same session
 * <p>
 * [✓] Pass Mutiny.StatelessSession through the chain
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

import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.cache.NameIdCache;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag_;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Singleton
public class ActiveFlagService
        implements IActiveFlagService<ActiveFlagService> {
    // Local cache: key = enterpriseId + '|' + flagName, value = ActiveFlag UUID
    private final Map<String, UUID> flagKeyToId = new ConcurrentHashMap<>();

    @Override
    public IActiveFlag<?, ?> get() {
        return new ActiveFlag();
    }

    @Override
    public Uni<UUID> resolveActiveFlagIdByName(Mutiny.StatelessSession session, IEnterprise<?, ?> enterpriseId, String flagName) {
        return NameIdCache
                .getActiveFlagId(session, enterpriseId.getId(), flagName, (sess, name) -> {
                                     return new ActiveFlag()
                                             .builder(sess)
                                             .withName(flagName)
                                             .withEnterprise(enterpriseId)
                                             .inActiveRange(enterpriseId)
                                             .get()
                                             .chain(flag -> {
                                                 return Uni.createFrom().item(flag.getId());
                                             });
                                 }
                );
    }


    private Uni<IActiveFlag<?, ?>> createWithSecurity(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, String name, String description,
                                                      java.util.function.Function<ActiveFlag, Uni<?>> securityFn, UUID... identifyingToken
    ) {
        return findFlagByName(session, name, enterprise, identifyingToken)
                .onFailure(NoResultException.class).recoverWithUni(() -> {
                    ActiveFlag af = new ActiveFlag();
                    af.setName(name);
                    af.setDescription(description);
                    af.setAllowAccess(true);
                    af.setEnterpriseID((Enterprise) enterprise);
                    return af.builder(session)
                            .persist(af).replaceWith(Uni.createFrom().item(af))
                            .call(persisted -> securityFn.apply(af));
                });
    }

    /**
     * Stateless find-or-create of an ActiveFlag reference row (no security is stamped — ActiveFlags are
     * enterprise-global reference data). Existence is checked with a scalar {@code getCount()}; an existing
     * flag is returned prepped (scalar projection), otherwise a new row is inserted on the
     * {@link Mutiny.StatelessSession}.
     */
    public Uni<IActiveFlag<?, ?>> create(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, String name, String description, UUID... identifyingToken) {
        return new ActiveFlag().builder(session)
                .withName(name)
                .inDateRange()
                .withEnterprise(enterprise)
                .getCount()
                .chain(count -> {
                    if (count != null && count > 0) {
                        return new ActiveFlag().builder(session)
                                .withName(name)
                                .inDateRange()
                                .withEnterprise(enterprise)
                                .selectColumn(ActiveFlag_.id)
                                .selectColumn(ActiveFlag_.name)
                                .selectColumn(ActiveFlag_.description)
                                .selectColumn(ActiveFlag_.allowAccess)
                                .get(Object[].class)
                                .map(row -> {
                                    boolean allow = row[3] instanceof Boolean b ? b
                                            : row[3] instanceof Number n ? n.intValue() != 0
                                              : Boolean.parseBoolean(String.valueOf(row[3]));
                                    ActiveFlag prepped = new ActiveFlag((UUID) row[0], (String) row[1], allow);
                                    prepped.setDescription((String) row[2]);
                                    prepped.setEnterpriseID(enterprise);
                                    prepped.setFake(false);
                                    return (IActiveFlag<?, ?>) prepped;
                                });
                    }
                    ActiveFlag af = new ActiveFlag();
                    af.setName(name);
                    af.setDescription(description);
                    af.setAllowAccess(true);
                    af.setEnterpriseID((Enterprise) enterprise);
                    return af.builder(session).persist(af).replaceWith((IActiveFlag<?, ?>) af);
                });
    }

    /**
     * Stateless opt-in <strong>scope-restricted</strong> ActiveFlag create — the stateless twin of
     * {@link #createScopeRestricted(Mutiny.StatelessSession, IEnterprise, String, String, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken, UUID...)}.
     * Existence is checked with a scalar {@code getCount()} (an existing flag is returned prepped); a new flag is
     * inserted and secured with the restricted matrix (no Everyone/Everywhere/Guests; {@code scopeToken}=read).
     * Each create runs on its own stateless unit, so independent stateless sessions can provision flags in parallel.
     */
    @Override
    public Uni<IActiveFlag<?, ?>> createScopeRestricted(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, String name, String description,
                                                        com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?> system,
                                                        com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                        UUID... identifyingToken) {
        return new ActiveFlag().builder(session)
                .withName(name)
                .inDateRange()
                .withEnterprise(enterprise)
                .getCount()
                .chain(count -> {
                    if (count != null && count > 0) {
                        return create(session, enterprise, name, description, identifyingToken);
                    }
                    ActiveFlag af = new ActiveFlag();
                    af.setId(UUID.randomUUID());
                    af.setName(name);
                    af.setDescription(description);
                    af.setAllowAccess(true);
                    af.setEnterpriseID((Enterprise) enterprise);
                    com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService<?> sts =
                            com.guicedee.client.IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService.class);
                    return af.builder(session).persist(af)
                            .chain(persisted -> sts.resolveDefaultGroupFolderTokens(session, system, identifyingToken)
                                    // The flag secures itself: it is its own ActiveFlag reference for the security rows.
                                    .chain(tokens -> af.createScopeRestrictedSecurity(session, system, enterprise, af, tokens, scopeToken, identifyingToken))
                                    .onFailure().recoverWithItem(0L)
                                    .replaceWith((IActiveFlag<?, ?>) af));
                });
    }


    //@CacheResult(cacheName = "FindActiveByName")
    // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
    public Uni<IActiveFlag<?, ?>> getFlagById(Mutiny.StatelessSession session, UUID id) {
        //noinspection unchecked
        return (Uni) session.get(ActiveFlag.class, id);
    }


    Uni<List<ActiveFlag>> find(Mutiny.StatelessSession session, String[] name, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return new ActiveFlag().builder(session)
                .withName(name)
                .inDateRange()
                //     .canRead(enterprise, true, identifyingToken)
                .withEnterprise(enterprise)
                .getAll();
    }

    private String[] getNamesForFlags(Set<com.entityassist.enumerations.ActiveFlag> flags) {
        return com.entityassist.enumerations.ActiveFlag.activeFlagToStrings(flags)
                .toArray(new String[]{});
    }

    // ============================================================================================
    // Stateless finder twins.
    // ============================================================================================

    Uni<List<ActiveFlag>> findStateless(Mutiny.StatelessSession session, String[] name, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return new ActiveFlag().builder(session)
                .withName(name)
                .inDateRange()
                .withEnterprise(enterprise)
                .getAll();
    }

    @Override
    @CacheResult(cacheName = "ActiveFlagFindByEnumStateless")
    public Uni<IActiveFlag<?, ?>> findFlagByName(Mutiny.StatelessSession session, @CacheKey com.entityassist.enumerations.ActiveFlag flag, @CacheKey IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByNameStateless(session, flag.name(), enterprise);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    @CacheResult(cacheName = "ActiveFlagFindByNameStateless")
    public Uni<IActiveFlag<?, ?>> findFlagByName(Mutiny.StatelessSession session, @CacheKey String flag, @CacheKey IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByNameStateless(session, flag, enterprise);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IActiveFlag<?, ?>>> findActiveRange(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) findStateless(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getActiveRangeAndUp()), enterprise, identifyingToken);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IActiveFlag<?, ?>>> getVisibleRange(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) findStateless(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getVisibleRangeAndUp()), enterprise, identifyingToken);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IActiveFlag<?, ?>>> getRemovedRange(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) findStateless(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getRemovedRange()), enterprise, identifyingToken);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IActiveFlag<?, ?>>> getArchiveRange(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) findStateless(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getArchivedRange()), enterprise, identifyingToken);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<List<IActiveFlag<?, ?>>> getHighlightedRange(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) findStateless(session, getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getHighlightedRange()), enterprise, identifyingToken);
    }

    @Override
    @CacheResult(cacheName = "ActiveFlagActiveStateless")
    public Uni<IActiveFlag<?, ?>> getActiveFlag(Mutiny.StatelessSession session, @CacheKey IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        // Stateless "fetch ids/scalars + prep": ActiveFlag is @Cacheable but its @ManyToOne is LAZY, so only
        // scalar columns are eager. Project the flag's own scalars (id, name, description, allowAccess) and
        // build a fresh DETACHED ActiveFlag, wiring the enterprise reference from the supplied parameter.
        return findFlagByNameStateless(session, com.entityassist.enumerations.ActiveFlag.Active.name(), enterprise);
    }

    /**
     * Shared stateless scalar-projection + prep for the named flags (Active / Archived / Deleted).
     */
    private Uni<IActiveFlag<?, ?>> findFlagByNameStateless(Mutiny.StatelessSession session, String flagName, IEnterprise<?, ?> enterprise) {
        return new ActiveFlag().builder(session)
                .withName(flagName)
                .inDateRange()
                .withEnterprise(enterprise)
                .selectColumn(ActiveFlag_.id)
                .selectColumn(ActiveFlag_.name)
                .selectColumn(ActiveFlag_.description)
                .selectColumn(ActiveFlag_.allowAccess)
                .get(Object[].class)
                .map(row -> {
                    boolean allow = row[3] instanceof Boolean b ? b
                            : row[3] instanceof Number n ? n.intValue() != 0
                              : Boolean.parseBoolean(String.valueOf(row[3]));
                    ActiveFlag prepped = new ActiveFlag((UUID) row[0], (String) row[1], allow);
                    prepped.setDescription((String) row[2]);
                    prepped.setEnterpriseID(enterprise);
                    prepped.setFake(false);
                    return (IActiveFlag<?, ?>) prepped;
                });
    }

    @Override
    @CacheResult(cacheName = "ActiveFlagArchivedStateless")
    public Uni<IActiveFlag<?, ?>> getArchivedFlag(Mutiny.StatelessSession session, @CacheKey IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByNameStateless(session, com.entityassist.enumerations.ActiveFlag.Archived.name(), enterprise);
    }

    @Override
    @CacheResult(cacheName = "ActiveFlagDeletedStateless")
    public Uni<IActiveFlag<?, ?>> getDeletedFlag(Mutiny.StatelessSession session, @CacheKey IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByNameStateless(session, com.entityassist.enumerations.ActiveFlag.Deleted.name(), enterprise);
    }
}
