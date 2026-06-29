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

    // Stateless detached-prepped reference cache (Active/Archived/Deleted flags), keyed by enterpriseId →
    // flag name. Safe: detached scalar projection, immutable reference flags; only cached on a real hit.
    private static final Map<UUID, Map<String, IActiveFlag<?, ?>>> STATELESS_FLAG_CACHE = new ConcurrentHashMap<>();

    @Override
    public IActiveFlag<?, ?> get() {
        return new ActiveFlag();
    }

    public Uni<UUID> resolveActiveFlagIdByName(Mutiny.Session session, IEnterprise<?, ?> enterpriseId, String flagName) {
        return NameIdCache
                .getActiveFlagId(session, enterpriseId.getId(), flagName, (sess, name) -> {
                                     return new ActiveFlag()
                                             .builder(session)
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


    public Uni<IActiveFlag<?, ?>> create(Mutiny.Session session, IEnterprise<?, ?> enterprise, String name, String description, UUID... identifyingToken) {
        // Public create — ActiveFlags are enterprise reference data; no per-record security is stamped (unchanged).
        return createWithSecurity(session, enterprise, name, description,
                                  af -> Uni.createFrom().nullItem(), identifyingToken
        );
    }

    /**
     * Opt-in <strong>scope-restricted</strong> ActiveFlag create. Unlike the public {@link #create} (which stamps
     * <em>no</em> security on this reference-data row), this variant secures the new flag with the restricted
     * matrix: only Administrators / Systems / Applications / Plugins retain access, plus a <em>read</em> grant for
     * {@code scopeToken}. Because the applicable-token climb is child&rarr;parent, only identity tokens at the
     * {@code scopeToken} node <em>or below it</em> may read the flag.
     *
     * <p><strong>Caveat:</strong> ActiveFlags gate row visibility for every record that references them and are
     * normally enterprise-global. Restricting a flag is unusual — use only for tenant/branch-private flags.</p>
     */
    @Override
    public Uni<IActiveFlag<?, ?>> createScopeRestricted(Mutiny.Session session, IEnterprise<?, ?> enterprise, String name, String description,
                                                        com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?> system,
                                                        com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken<?, ?> scopeToken,
                                                        UUID... identifyingToken
    ) {
        return createWithSecurity(session,
                                  enterprise,
                                  name,
                                  description,
                                  af -> af.createScopeRestrictedSecurity(session, system, scopeToken, identifyingToken),
                                  identifyingToken
        );
    }

    private Uni<IActiveFlag<?, ?>> createWithSecurity(Mutiny.Session session, IEnterprise<?, ?> enterprise, String name, String description,
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


    @Override
    public Uni<IActiveFlag<?, ?>> findFlagByName(Mutiny.Session session, com.entityassist.enumerations.ActiveFlag flag, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByName(session, flag.name(), enterprise, identifyingToken);
    }


    //@CacheResult(cacheName = "FindActiveByName")
    @Override
    public Uni<IActiveFlag<?, ?>> findFlagByName(Mutiny.Session session, String flag, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        UUID enterpriseId = null;
        if (enterprise instanceof Enterprise ent) {
            enterpriseId = ent.getId();
        }
        String key = enterpriseId + "|" + flag;
        UUID cachedId = flagKeyToId.get(key);
        if (cachedId != null) {
            log.trace("🔁 ActiveFlag cache hit for key '{}': {} — loading by UUID", key, cachedId);
            return getFlagById(session, cachedId);
        }

        // Use ID-first resolution with shared NameIdCache and native SQL, then load by UUID
        return resolveActiveFlagIdByName(session, enterprise, flag)
                .flatMap(id -> {
                    if (id == null) {
                        return Uni.createFrom().failure(new NoResultException("ActiveFlag not found: " + flag));
                    }
                    flagKeyToId.put(key, id);
                    return getFlagById(session, id);
                });
    }

    // UUID-based lookup to leverage L2 cache (@Cacheable on entity + L2 cache enabled)
    public Uni<IActiveFlag<?, ?>> getFlagById(Mutiny.Session session, UUID id) {
        //noinspection unchecked
        return (Uni) session.find(ActiveFlag.class, id);
    }


    @Override
    //@CacheResult(cacheName = "FindActiveFlagRange")
    public Uni<List<IActiveFlag<?, ?>>> findActiveRange(Mutiny.Session session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) find(session,
                          getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getActiveRangeAndUp()),
                          enterprise,
                          identifyingToken
        );
    }


    Uni<List<ActiveFlag>> find(Mutiny.Session session, String[] name, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
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

    @Override
    //@CacheResult(cacheName = "GetVisibleRange")
    public Uni<List<IActiveFlag<?, ?>>> getVisibleRange(Mutiny.Session session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) find(session,
                          getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getVisibleRangeAndUp()),
                          enterprise,
                          identifyingToken
        );
    }

    @Override
    //@CacheResult
    public Uni<List<IActiveFlag<?, ?>>> getRemovedRange(Mutiny.Session session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) find(session,
                          getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getRemovedRange()),
                          enterprise,
                          identifyingToken
        );
    }

    @Override
    //@CacheResult(cacheName = "GetArchivedRange")
    public Uni<List<IActiveFlag<?, ?>>> getArchiveRange(Mutiny.Session session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) find(session,
                          getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getArchivedRange()),
                          enterprise,
                          identifyingToken
        );
    }

    @Override
    //@CacheResult(cacheName = "GetHighlightedRange")
    public Uni<List<IActiveFlag<?, ?>>> getHighlightedRange(Mutiny.Session session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return (Uni) find(session,
                          getNamesForFlags(com.entityassist.enumerations.ActiveFlag.getHighlightedRange()),
                          enterprise,
                          identifyingToken
        );
    }

    @Override
    //@CacheResult(cacheName = "GetActiveFlag")
    public Uni<IActiveFlag<?, ?>> getActiveFlag(Mutiny.Session session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByName(session, com.entityassist.enumerations.ActiveFlag.Active, enterprise, identifyingToken);
    }

    @Override
    public Uni<IActiveFlag<?, ?>> getActiveFlag(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        // Stateless "fetch ids/scalars + prep": ActiveFlag is @Cacheable but its @ManyToOne is LAZY, so only
        // scalar columns are eager. Project the flag's own scalars (id, name, description, allowAccess) and
        // build a fresh DETACHED ActiveFlag, wiring the enterprise reference from the supplied parameter.
        return findFlagByNameStateless(session, com.entityassist.enumerations.ActiveFlag.Active, enterprise);
    }

    /**
     * Shared stateless scalar-projection + prep for the named flags (Active / Archived / Deleted).
     */
    private Uni<IActiveFlag<?, ?>> findFlagByNameStateless(Mutiny.StatelessSession session, com.entityassist.enumerations.ActiveFlag flag, IEnterprise<?, ?> enterprise) {
        UUID enterpriseId = enterprise.getId();
        String flagName = flag.name();
        Map<String, IActiveFlag<?, ?>> byName = STATELESS_FLAG_CACHE.computeIfAbsent(enterpriseId,
                                                                                     k -> new ConcurrentHashMap<>()
        );
        IActiveFlag<?, ?> hit = byName.get(flagName);
        if (hit != null) {
            return Uni.createFrom().item((IActiveFlag<?, ?>) hit);
        }
        Uni<IActiveFlag<?, ?>> resolved = new ActiveFlag().builder(session)
                .withName(getNamesForFlags(Set.of(flag)))
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
        return resolved.onItem().invoke(t -> {
            if (t != null && t.getId() != null) byName.put(flagName, t);
        });
    }

    @Override
    //@CacheResult(cacheName = "GetArchivedFlag")
    public Uni<IActiveFlag<?, ?>> getArchivedFlag(Mutiny.Session session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByName(session, com.entityassist.enumerations.ActiveFlag.Archived, enterprise, identifyingToken);
    }

    @Override
    public Uni<IActiveFlag<?, ?>> getArchivedFlag(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByNameStateless(session, com.entityassist.enumerations.ActiveFlag.Archived, enterprise);
    }

    @Override
    //@CacheResult(cacheName = "GetDeletedFlag")
    public Uni<IActiveFlag<?, ?>> getDeletedFlag(Mutiny.Session session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByName(session, com.entityassist.enumerations.ActiveFlag.Deleted, enterprise, identifyingToken);
    }

    @Override
    public Uni<IActiveFlag<?, ?>> getDeletedFlag(Mutiny.StatelessSession session, IEnterprise<?, ?> enterprise, UUID... identifyingToken) {
        return findFlagByNameStateless(session, com.entityassist.enumerations.ActiveFlag.Deleted, enterprise);
    }
}
