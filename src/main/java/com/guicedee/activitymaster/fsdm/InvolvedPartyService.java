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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyNameType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.InvolvedPartyException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.JoinType;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.entityassist.enumerations.Operand.Equals;
import static com.entityassist.enumerations.OrderByType.DESC;
import static com.guicedee.activitymaster.fsdm.SystemsService.ActivityMasterSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.IdentificationTypeUUID;

@Log4j2
@Singleton
public class InvolvedPartyService implements IInvolvedPartyService<InvolvedPartyService>
{
    // Local caches for type lookups to enable ID-based L2 cache hits
    // Bounded caches (access-order LRU) sized under an aggregate configurable memory budget (default ~300MB)
    private static final double DEFAULT_SPLIT_PARTY = parseDoubleProp("fsdm.ip.cache.split.party", 0.4);
    private static final double DEFAULT_SPLIT_TYPE = parseDoubleProp("fsdm.ip.cache.split.type", 0.2);
    private static final double DEFAULT_SPLIT_NAME_TYPE = parseDoubleProp("fsdm.ip.cache.split.nameType", 0.2);
    private static final double DEFAULT_SPLIT_IDENT_TYPE = parseDoubleProp("fsdm.ip.cache.split.identType", 0.2);
    private static final long TOTAL_MB = parseLongProp("fsdm.ip.cache.total.mb", 300);
    private static final long ENTRY_EST_BYTES = parseLongProp("fsdm.ip.cache.entry.estimate.bytes", 256);
    private static final double[] SPLITS = normalizeSplits(new double[]{DEFAULT_SPLIT_PARTY, DEFAULT_SPLIT_TYPE, DEFAULT_SPLIT_NAME_TYPE, DEFAULT_SPLIT_IDENT_TYPE});
    private static final int PARTY_MAX = computeMaxEntries(SPLITS[0], TOTAL_MB, ENTRY_EST_BYTES);
    private static final int TYPE_MAX = computeMaxEntries(SPLITS[1], TOTAL_MB, ENTRY_EST_BYTES);
    private static final int NAME_TYPE_MAX = computeMaxEntries(SPLITS[2], TOTAL_MB, ENTRY_EST_BYTES);
    private static final int IDENT_TYPE_MAX = computeMaxEntries(SPLITS[3], TOTAL_MB, ENTRY_EST_BYTES);

    // New: cache for InvolvedParty entity lookups by identification key
    private final com.guicedee.activitymaster.fsdm.util.BoundedLruCache<String, java.util.UUID> involvedPartyKeyToId = new com.guicedee.activitymaster.fsdm.util.BoundedLruCache<>(PARTY_MAX);
    private final com.guicedee.activitymaster.fsdm.util.BoundedLruCache<java.util.UUID, IInvolvedParty<?, ?>> involvedPartyIdToStatelessEntity = new com.guicedee.activitymaster.fsdm.util.BoundedLruCache<>(PARTY_MAX);
    private final com.guicedee.activitymaster.fsdm.util.BoundedLruCache<String, java.util.UUID> involvedPartyTypeKeyToId = new com.guicedee.activitymaster.fsdm.util.BoundedLruCache<>(TYPE_MAX);
    private final com.guicedee.activitymaster.fsdm.util.BoundedLruCache<String, java.util.UUID> involvedPartyNameTypeKeyToId = new com.guicedee.activitymaster.fsdm.util.BoundedLruCache<>(NAME_TYPE_MAX);
    private final com.guicedee.activitymaster.fsdm.util.BoundedLruCache<String, java.util.UUID> involvedPartyIdentificationTypeKeyToId = new com.guicedee.activitymaster.fsdm.util.BoundedLruCache<>(
            IDENT_TYPE_MAX);

    // --- Cache configuration helpers & static log ---
    private static long mbToBytes(long mb)
    {
        return mb * 1024L * 1024L;
    }

    private static long parseLongProp(String key, long def)
    {
        try {
            return Long.parseLong(System.getProperty(key, String.valueOf(def)));
        } catch (Exception e) {
            return def;
        }
    }

    private static double parseDoubleProp(String key, double def)
    {
        try {
            return Double.parseDouble(System.getProperty(key, String.valueOf(def)));
        } catch (Exception e) {
            return def;
        }
    }

    private static double[] normalizeSplits(double[] s)
    {
        double sum = 0.0;
        for (double v : s) {
            sum += Math.max(0.0, v);
        }
        if (sum <= 0.0) {
            return new double[]{1.0, 0.0, 0.0, 0.0};
        }
        double[] out = new double[s.length];
        for (int i = 0; i < s.length; i++) {
            out[i] = Math.max(0.0, s[i]) / sum;
        }
        return out;
    }

    private static int computeMaxEntries(double split, long totalMb, long entryEstBytes)
    {
        long budgetBytes = (long) (mbToBytes(totalMb) * split);
        if (budgetBytes <= 0L || entryEstBytes <= 0L) {
            return 1000;
        }
        long n = budgetBytes / Math.max(1L, entryEstBytes);
        if (n < 1L) {
            n = 1L;
        }
        if (n > Integer.MAX_VALUE) {
            n = Integer.MAX_VALUE;
        }
        return (int) n;
    }

    static {
        log.info("InvolvedParty caches configured: totalMb={}, entryEstBytes={}, splits=[party:{}, type:{}, nameType:{}, identType:{}], maxEntries=[party:{}, type:{}, nameType:{}, identType:{}]",
                 TOTAL_MB,
                 ENTRY_EST_BYTES,
                 SPLITS[0],
                 SPLITS[1],
                 SPLITS[2],
                 SPLITS[3],
                 PARTY_MAX,
                 TYPE_MAX,
                 NAME_TYPE_MAX,
                 IDENT_TYPE_MAX
        );
    }

    // Helper to load InvolvedParty by ID (eligible for Hibernate 2nd-level cache)
    public io.smallrye.mutiny.Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty<?, ?>> getInvolvedPartyById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id)
    {
        return (io.smallrye.mutiny.Uni) session.find(com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty.class, id);
    }

    private Uni<IInvolvedParty<?, ?>> getStatelessInvolvedPartyById(Mutiny.StatelessSession session, UUID id)
    {
        IInvolvedParty<?, ?> cached = involvedPartyIdToStatelessEntity.get(id);
        if (cached != null) {
            log.trace("InvolvedParty stateless entity cache hit for UUID {}", id);
            return Uni.createFrom()
                      .item(cached);
        }
        return loadStatelessInvolvedPartyById(session, id)
                .invoke(this::cacheStatelessInvolvedParty);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Uni<IInvolvedParty<?, ?>> loadStatelessInvolvedPartyById(Mutiny.StatelessSession session, UUID id)
    {
        return (Uni) new InvolvedParty().builder(session)
                                        .find(id)
                                        .get()
                                        .onFailure(NoResultException.class)
                                        .invoke(e -> log.warn("InvolvedParty with UUID '{}' could not be found (stateless)", id))
                                        .onItem()
                                        .ifNull()
                                        .failWith(() -> new InvolvedPartyException("The InvolvedParty does not exist - " + id));
    }

    private void cacheStatelessInvolvedParty(IInvolvedParty<?, ?> involvedParty)
    {
        if (involvedParty != null && involvedParty.getId() != null) {
            involvedPartyIdToStatelessEntity.put((UUID) involvedParty.getId(), involvedParty);
        }
    }

    // UUID-based helpers delegating to session.find(...)
    public io.smallrye.mutiny.Uni getInvolvedPartyTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id)
    {
        return session.find(InvolvedPartyType.class, id);
    }

    public io.smallrye.mutiny.Uni getInvolvedPartyNameTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id)
    {
        return session.find(InvolvedPartyNameType.class, id);
    }

    public io.smallrye.mutiny.Uni getInvolvedPartyIdentificationTypeById(org.hibernate.reactive.mutiny.Mutiny.Session session, java.util.UUID id)
    {
        return session.find(InvolvedPartyIdentificationType.class, id);
    }

    @Inject
    private IClassificationService<?> classificationService;

    @Inject
    private ISystemsService<?> systemsService;

    @Override
    public IInvolvedParty<?, ?> get()
    {
        log.debug("Getting new InvolvedParty instance");
        return new InvolvedParty();
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByID(Mutiny.Session session, UUID id)
    {
        log.debug("🔍 Finding InvolvedParty by ID: {} with session: {}", id, session.hashCode());
        // Use session.find to leverage Hibernate 2nd-level cache on repeat loads
        return (Uni) session.find(InvolvedParty.class, id);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IInvolvedParty<?, ?>> findByID(Mutiny.StatelessSession session, UUID id)
    {
        log.debug("🔍 Finding InvolvedParty by ID (stateless): {}", id);
        return (Uni) session.get(InvolvedParty.class, id);
    }

    @Override
    public Uni<IInvolvedPartyNameType<?, ?>> createNameType(Mutiny.Session session, String name, String description, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Creating InvolvedPartyNameType: name={}, description={}", name, description);

        var enterprise = system.getEnterprise();

        // Find-or-create using NoResultException handling (finders never return null)
        return findInvolvedPartyNameType(session, name, system, identityToken).onItem()
                                                                              .invoke(found -> log.debug("InvolvedPartyNameType already exists: {}", name))
                                                                              .onFailure(NoResultException.class)
                                                                              .recoverWithUni(err -> {
                                                                                  log.debug("Creating new InvolvedPartyNameType: {} (not found)", name);
                                                                                  InvolvedPartyNameType xr = new InvolvedPartyNameType();
                                                                                  xr.setName(name);
                                                                                  xr.setDescription(description);
                                                                                  xr.setSystemID(system);
                                                                                  xr.setOriginalSourceSystemID(system.getId());
                                                                                  xr.setEnterpriseID(enterprise);

                                                                                  IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                                                                  return acService.getActiveFlag(session, enterprise, identityToken)
                                                                                                  .chain(activeFlag -> {
                                                                                                      xr.setActiveFlagID(activeFlag);
                                                                                                      return session.persist(xr)
                                                                                                                    .replaceWith(Uni.createFrom()
                                                                                                                                    .item(xr));
                                                                                                  })
                                                                                                  .chain(persisted -> {
                                                                                                      // Handle security setup sequentially on the same session/thread
                                                                                                      return persisted.createDefaultSecurity(session, system, identityToken)
                                                                                                                      .onItem()
                                                                                                                      .invoke(result -> log.debug(
                                                                                                                              "Security setup completed successfully for name type {}",
                                                                                                                              persisted.getName()
                                                                                                                      ))
                                                                                                                      .onFailure()
                                                                                                                      .recoverWithItem(error2 -> {
                                                                                                                          log.warn("Error in createDefaultSecurity for name type", error2);
                                                                                                                          return null; // Continue chain even if security creation fails
                                                                                                                      })
                                                                                                                      .chain(() -> Uni.createFrom()
                                                                                                                                      .item((IInvolvedPartyNameType<?, ?>) persisted));
                                                                                                  });
                                                                              });
    }

    @Override
    public Uni<IInvolvedPartyIdentificationType<?, ?>> createIdentificationType(Mutiny.Session session, ISystems<?, ?> system, String name, String description, UUID... identityToken)
    {
        log.debug("Creating InvolvedPartyIdentificationType: name={}, description={}", name, description);

        var enterprise = system.getEnterprise();

        // Find-or-create using NoResultException handling (finders never return null)
        return findInvolvedPartyIdentificationType(session, name, system, identityToken).onItem()
                                                                                        .invoke(found -> log.debug("InvolvedPartyIdentificationType already exists: {}", name))
                                                                                        .onFailure(NoResultException.class)
                                                                                        .recoverWithUni(err -> {
                                                                                            log.debug("Creating new InvolvedPartyIdentificationType: {} (not found)", name);
                                                                                            InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
                                                                                            xr.setName(name);
                                                                                            xr.setDescription(description);
                                                                                            xr.setSystemID(system);
                                                                                            xr.setOriginalSourceSystemID(system.getId());
                                                                                            xr.setEnterpriseID(enterprise);

                                                                                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                                                                            return acService.getActiveFlag(session, enterprise, identityToken)
                                                                                                            .chain(activeFlag -> {
                                                                                                                xr.setActiveFlagID(activeFlag);
                                                                                                                return session.persist(xr)
                                                                                                                              .replaceWith(Uni.createFrom()
                                                                                                                                              .item(xr));
                                                                                                            })
                                                                                                            .chain(persisted -> {
                                                                                                                // Handle security setup sequentially on the same session/thread
                                                                                                                return persisted.createDefaultSecurity(session, system, identityToken)
                                                                                                                                .onItem()
                                                                                                                                .invoke(result -> log.debug(
                                                                                                                                        "Security setup completed successfully for identification type {}",
                                                                                                                                        persisted.getName()
                                                                                                                                ))
                                                                                                                                .onFailure()
                                                                                                                                .recoverWithItem(error2 -> {
                                                                                                                                    log.warn("Error in createDefaultSecurity for identification type",
                                                                                                                                             error2
                                                                                                                                    );
                                                                                                                                    return null; // Continue chain even if security creation fails
                                                                                                                                })
                                                                                                                                .chain(() -> Uni.createFrom()
                                                                                                                                                .item((IInvolvedPartyIdentificationType<?, ?>) persisted));
                                                                                                            });
                                                                                        });
    }

    @Override
    public Uni<IInvolvedPartyType<?, ?>> createType(Mutiny.Session session, ISystems<?, ?> system, String name, String description, UUID... identityToken)
    {
        log.debug("Creating InvolvedPartyType: name={}, description={}", name, description);

        var enterprise = system.getEnterprise();

        // First try to find the entity
        return findType(session, name, system, identityToken).onItem()
                                                             .invoke(found -> log.debug("InvolvedPartyType already exists: {}", name))
                                                             .onFailure(NoResultException.class)
                                                             .recoverWithUni(err -> {
                                                                 // Create new entity if not found
                                                                 log.debug("Creating new InvolvedPartyType: {} (not found)", name);
                                                                 InvolvedPartyType xr = new InvolvedPartyType();
                                                                 xr.setName(name);
                                                                 xr.setDescription(description);
                                                                 xr.setSystemID(system);
                                                                 xr.setOriginalSourceSystemID(system.getId());
                                                                 xr.setEnterpriseID(enterprise);

                                                                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                                                 return acService.getActiveFlag(session, enterprise, identityToken)
                                                                                 .chain(activeFlag -> {
                                                                                     xr.setActiveFlagID(activeFlag);
                                                                                     return session.persist(xr)
                                                                                                   .replaceWith(Uni.createFrom()
                                                                                                                   .item(xr));
                                                                                 })
                                                                                 .chain(persisted -> {
                                                                                     // Get activity master system and handle security setup sequentially
                                                                                     return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
                                                                                                          .chain(activityMasterSystem -> {
                                                                                                              return persisted.createDefaultSecurity(session, activityMasterSystem, identityToken)
                                                                                                                              .onItem()
                                                                                                                              .invoke(result -> log.debug(
                                                                                                                                      "Security setup completed successfully for type {}",
                                                                                                                                      persisted.getName()
                                                                                                                              ))
                                                                                                                              .onFailure()
                                                                                                                              .recoverWithItem(error2 -> {
                                                                                                                                  log.warn("Error in createDefaultSecurity for type", error2);
                                                                                                                                  return null; // Continue the chain even if security creation fails
                                                                                                                              })
                                                                                                                              .chain(() -> Uni.createFrom()
                                                                                                                                              .item((IInvolvedPartyType<?, ?>) persisted));
                                                                                                          });
                                                                                 });
                                                             });
    }

    // ============================================================================================
    // Stateless "fetch ids/scalars + prep" finders + end-to-end creates for the InvolvedParty *Type
    // reference entities. All three are @Cacheable with no eager @ManyToOne, so a scalar projection of
    // (id, name, description) + a detached prep is stateless-safe; the creates then do a stateless insert
    // + the stateless default-security matrix. Mirrors the managed find-or-create methods above.
    // ============================================================================================

    @Override
    @CacheResult(cacheName = "InvolvedPartyIdentificationTypeFindByNameStateless")
    public Uni<IInvolvedPartyIdentificationType<?, ?>> findInvolvedPartyIdentificationType(Mutiny.StatelessSession session, @CacheKey String idType, @CacheKey ISystems<?, ?> system, UUID... identityToken)
    {
        if (idType == null) {
            return Uni.createFrom().failure(new NoResultException("InvolvedPartyIdentificationType name must not be null"));
        }
        var enterprise = system.getEnterprise();
        return new InvolvedPartyIdentificationType().builder(session)
                                                    .withName(idType)
                                                    .inActiveRange()
                                                    .inDateRange()
                                                    .withEnterprise(enterprise)
                                                    .selectColumn(InvolvedPartyIdentificationType_.id)
                                                    .selectColumn(InvolvedPartyIdentificationType_.name)
                                                    .selectColumn(InvolvedPartyIdentificationType_.description)
                                                    .get(Object[].class)
                                                    .map(row -> {
                                                        InvolvedPartyIdentificationType prepped = new InvolvedPartyIdentificationType((UUID) row[0],
                                                                                                                                      (String) row[1],
                                                                                                                                      (String) row[2]
                                                        );
                                                        prepped.setEnterpriseID(enterprise);
                                                        prepped.setFake(false);
                                                        return (IInvolvedPartyIdentificationType<?, ?>) prepped;
                                                    });
    }

    @Override
    @CacheResult(cacheName = "InvolvedPartyNameTypeFindByNameStateless")
    public Uni<IInvolvedPartyNameType<?, ?>> findInvolvedPartyNameType(Mutiny.StatelessSession session, @CacheKey String nameType, @CacheKey ISystems<?, ?> system, UUID... identityToken)
    {
        if (nameType == null) {
            return Uni.createFrom().failure(new NoResultException("InvolvedPartyNameType name must not be null"));
        }
        var enterprise = system.getEnterprise();
        return new InvolvedPartyNameType().builder(session)
                                          .withName(nameType)
                                          .inActiveRange()
                                          .inDateRange()
                                          .withEnterprise(enterprise)
                                          .selectColumn(InvolvedPartyNameType_.id)
                                          .selectColumn(InvolvedPartyNameType_.name)
                                          .selectColumn(InvolvedPartyNameType_.description)
                                          .get(Object[].class)
                                          .map(row -> {
                                              InvolvedPartyNameType prepped = new InvolvedPartyNameType((UUID) row[0], (String) row[1], (String) row[2]);
                                              prepped.setEnterpriseID(enterprise);
                                              prepped.setFake(false);
                                              return (IInvolvedPartyNameType<?, ?>) prepped;
                                          });
    }

    @Override
    @CacheResult(cacheName = "InvolvedPartyTypeFindByNameStateless")
    public Uni<IInvolvedPartyType<?, ?>> findType(Mutiny.StatelessSession session, @CacheKey String nameType, @CacheKey ISystems<?, ?> system, UUID... identityToken)
    {
        if (nameType == null) {
            return Uni.createFrom().failure(new NoResultException("InvolvedPartyType name must not be null"));
        }
        var enterprise = system.getEnterprise();
        return new InvolvedPartyType().builder(session)
                                      .withName(nameType)
                                      .inActiveRange()
                                      .inDateRange()
                                      .withEnterprise(enterprise)
                                      .selectColumn(InvolvedPartyType_.id)
                                      .selectColumn(InvolvedPartyType_.name)
                                      .selectColumn(InvolvedPartyType_.description)
                                      .get(Object[].class)
                                      .map(row -> {
                                          InvolvedPartyType prepped = new InvolvedPartyType((UUID) row[0], (String) row[1], (String) row[2]);
                                          prepped.setEnterpriseID(enterprise);
                                          prepped.setFake(false);
                                          return (IInvolvedPartyType<?, ?>) prepped;
                                      });
    }

    @Override
    public Uni<IInvolvedPartyIdentificationType<?, ?>> createIdentificationType(Mutiny.StatelessSession session, ISystems<?, ?> system, String name, String description, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        return findInvolvedPartyIdentificationType(session, name, system, identityToken).onFailure()
                                                                                        .recoverWithUni(err -> {
                                                                                            InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
                                                                                            xr.setName(name);
                                                                                            xr.setDescription(description);
                                                                                            xr.setSystemID(system);
                                                                                            xr.setOriginalSourceSystemID(system.getId());
                                                                                            xr.setEnterpriseID(enterprise);
                                                                                            IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                                                                            ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);
                                                                                            return acService.getActiveFlag(session, enterprise, identityToken)
                                                                                                            .chain(activeFlag -> {
                                                                                                                xr.setActiveFlagID(activeFlag);
                                                                                                                return xr.builder(session)
                                                                                                                         .persist(xr)
                                                                                                                         .chain(persisted -> sts.resolveDefaultGroupFolderTokens(session,
                                                                                                                                                                                 system,
                                                                                                                                                                                 identityToken
                                                                                                                                                )
                                                                                                                                                .chain(tokens -> xr.createDefaultSecurity(session,
                                                                                                                                                                                          system,
                                                                                                                                                                                          enterprise,
                                                                                                                                                                                          activeFlag,
                                                                                                                                                                                          tokens
                                                                                                                                                ))
                                                                                                                                                .onFailure()
                                                                                                                                                .recoverWithItem(0L)
                                                                                                                                                .replaceWith((IInvolvedPartyIdentificationType<?, ?>) xr));
                                                                                                            });
                                                                                        });
    }

    @Override
    public Uni<IInvolvedPartyNameType<?, ?>> createNameType(Mutiny.StatelessSession session, String name, String description, ISystems<?, ?> system, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        return findInvolvedPartyNameType(session, name, system, identityToken).onFailure()
                                                                              .recoverWithUni(err -> {
                                                                                  InvolvedPartyNameType xr = new InvolvedPartyNameType();
                                                                                  xr.setName(name);
                                                                                  xr.setDescription(description);
                                                                                  xr.setSystemID(system);
                                                                                  xr.setOriginalSourceSystemID(system.getId());
                                                                                  xr.setEnterpriseID(enterprise);
                                                                                  IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                                                                  ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);
                                                                                  return acService.getActiveFlag(session, enterprise, identityToken)
                                                                                                  .chain(activeFlag -> {
                                                                                                      xr.setActiveFlagID(activeFlag);
                                                                                                      return xr.builder(session)
                                                                                                               .persist(xr)
                                                                                                               .chain(persisted -> sts.resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                                                                                                      .chain(tokens -> xr.createDefaultSecurity(session,
                                                                                                                                                                                system,
                                                                                                                                                                                enterprise,
                                                                                                                                                                                activeFlag,
                                                                                                                                                                                tokens
                                                                                                                                      ))
                                                                                                                                      .onFailure()
                                                                                                                                      .recoverWithItem(0L)
                                                                                                                                      .replaceWith((IInvolvedPartyNameType<?, ?>) xr));
                                                                                                  });
                                                                              });
    }

    @Override
    public Uni<IInvolvedPartyType<?, ?>> createType(Mutiny.StatelessSession session, ISystems<?, ?> system, String name, String description, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        return findType(session, name, system, identityToken).onFailure()
                                                             .recoverWithUni(err -> {
                                                                 InvolvedPartyType xr = new InvolvedPartyType();
                                                                 xr.setName(name);
                                                                 xr.setDescription(description);
                                                                 xr.setSystemID(system);
                                                                 xr.setOriginalSourceSystemID(system.getId());
                                                                 xr.setEnterpriseID(enterprise);
                                                                 IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
                                                                 ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);
                                                                 return acService.getActiveFlag(session, enterprise, identityToken)
                                                                                 .chain(activeFlag -> {
                                                                                     xr.setActiveFlagID(activeFlag);
                                                                                     return xr.builder(session)
                                                                                              .persist(xr)
                                                                                              .chain(persisted -> sts.resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                                                                                     .chain(tokens -> xr.createDefaultSecurity(session,
                                                                                                                                                               system,
                                                                                                                                                               enterprise,
                                                                                                                                                               activeFlag,
                                                                                                                                                               tokens
                                                                                                                     ))
                                                                                                                     .onFailure()
                                                                                                                     .recoverWithItem(0L)
                                                                                                                     .replaceWith((IInvolvedPartyType<?, ?>) xr));
                                                                                 });
                                                             });
    }

    // ============================================================================================
    // Stateless involved-party create (world-readable default security), mirroring the managed
    // create(Mutiny.Session, system, key, idTypes, isOrganic, …). Uses session.insert + the stateless
    // resolveDefaultGroupFolderTokens/createDefaultSecurity path and the stateless addOrReuse* mixins.
    // The enterprise/system references are taken from the (prepped) system parameter — no managed fetch.
    // ============================================================================================

    @Override
    public Uni<IInvolvedParty<?, ?>> create(Mutiny.StatelessSession session, ISystems<?, ?> system, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken)
    {
        return create(session, system, null, idTypes, isOrganic, identityToken);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IInvolvedParty<?, ?>> create(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken)
    {
        return createStateless(session, system, key, idTypes, isOrganic, null, false, identityToken);
    }

    /**
     * Stateless opt-in <strong>scope-restricted</strong> involved-party create — the stateless twin of
     * {@link #createScopeRestricted(Mutiny.Session, ISystems, UUID, Pair, boolean, ISecurityToken, UUID...)}.
     * The party <em>and</em> its organic/non-organic sub-record are secured with the restricted matrix (no
     * Everyone/Everywhere/Guests; {@code scopeToken}=read). Each create runs on its own stateless unit, so
     * independent stateless sessions can provision parties in parallel.
     */
    @Override
    public Uni<IInvolvedParty<?, ?>> createScopeRestricted(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, ISecurityToken<?, ?> scopeToken, UUID... identityToken)
    {
        return createStateless(session, system, key, idTypes, isOrganic, scopeToken, true, identityToken);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Uni<IInvolvedParty<?, ?>> createStateless(Mutiny.StatelessSession session, ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, ISecurityToken<?, ?> scopeToken, boolean restricted, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        final InvolvedParty ip = new InvolvedParty();
        ip.setEnterpriseID(enterprise);
        final UUID finalKey = (key == null) ? UUID.randomUUID() : key;
        ip.setId(finalKey);
        ip.setSystemID(system);
        ip.setOriginalSourceSystemID(system.getId());

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);
        return acService.getActiveFlag(session, enterprise, identityToken)
                        .chain(activeFlag -> {
                            ip.setActiveFlagID(activeFlag);
                            return session.insert(ip)
                                          .chain(() -> sts.resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                          .chain(tokens -> restricted ? ip.createScopeRestrictedSecurity(session,
                                                                                                                         system,
                                                                                                                         enterprise,
                                                                                                                         activeFlag,
                                                                                                                         tokens,
                                                                                                                         scopeToken,
                                                                                                                         identityToken
                                                          ) : ip.createDefaultSecurity(session, system, enterprise, activeFlag, tokens, identityToken))
                                                          .onFailure()
                                                          .recoverWithItem(0L)
                                                          .replaceWithVoid())
                                          .chain(() -> findInvolvedPartyIdentificationType(session,
                                                                                           idTypes.getKey(),
                                                                                           system,
                                                                                           identityToken
                                          ).chain(ipIdType -> ip.addOrReuseInvolvedPartyIdentificationType(session, NoClassification.toString(), ipIdType, idTypes.getValue(), system, identityToken)))
                                          .chain(() -> setupInvolvedPartyOrganicStatusStateless(session, isOrganic, ip, system, scopeToken, restricted, identityToken))
                                          .replaceWith((IInvolvedParty<?, ?>) ip);
                        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Uni<Void> setupInvolvedPartyOrganicStatusStateless(Mutiny.StatelessSession session, boolean isOrganic, InvolvedParty ip, ISystems<?, ?> system, ISecurityToken<?, ?> scopeToken, boolean restricted, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);
        return acService.getActiveFlag(session, enterprise, identityToken)
                        .chain(activeFlag -> {
                            final IWarehouseCoreTable<?, ?, ?, ?> record;
                            if (isOrganic) {
                                InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
                                ipo.setInvolvedParty(ip);
                                ipo.setId(ip.getId());
                                ipo.setEnterpriseID(enterprise);
                                ipo.setSystemID(system);
                                ipo.setOriginalSourceSystemID(system.getId());
                                ipo.setActiveFlagID(activeFlag);
                                record = ipo;
                            }
                            else {
                                InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
                                ipo.setInvolvedParty(ip);
                                ipo.setId(ip.getId());
                                ipo.setEnterpriseID(enterprise);
                                ipo.setSystemID(system);
                                ipo.setOriginalSourceSystemID(system.getId());
                                ipo.setActiveFlagID(activeFlag);
                                record = ipo;
                            }
                            return session.insert(record)
                                          .chain(() -> sts.resolveDefaultGroupFolderTokens(session, system, identityToken)
                                                          .chain(tokens -> restricted ? ((IWarehouseCoreTable) record).createScopeRestrictedSecurity(session,
                                                                                                                                                     system,
                                                                                                                                                     enterprise,
                                                                                                                                                     activeFlag,
                                                                                                                                                     tokens,
                                                                                                                                                     scopeToken,
                                                                                                                                                     identityToken
                                                          ) : ((IWarehouseCoreTable) record).createDefaultSecurity(session, system, enterprise, activeFlag, tokens, identityToken))
                                                          .onFailure()
                                                          .recoverWithItem(0L)
                                                          .replaceWithVoid());
                        });
    }

    private Uni<InvolvedPartyOrganicType> createOrganicType(Mutiny.Session session, ISystems<?, ?> system, UUID key, String name, String description, UUID... identityToken)
    {
        log.debug("Creating InvolvedPartyOrganicType: name={}, description={}", name, description);

        var enterprise = system.getEnterprise();

        InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
        xr.setId(key);
        xr.setName(name);
        xr.setDescription(description);
        xr.setSystemID(system);
        xr.setOriginalSourceSystemID(system.getId());
        xr.setEnterpriseID(enterprise);

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        return acService.getActiveFlag(session, enterprise, identityToken)
                        .chain(activeFlag -> {
                            xr.setActiveFlagID(activeFlag);
                            return session.persist(xr)
                                          .replaceWith(Uni.createFrom()
                                                          .item(xr));
                        })
                        .chain(persisted -> {
                            // Get activity master system and handle security setup sequentially
                            return systemsService.findSystem(session, enterprise, ActivityMasterSystemName)
                                                 .chain(activityMasterSystem -> {
                                                     return persisted.createDefaultSecurity(session, activityMasterSystem, identityToken)
                                                                     .onItem()
                                                                     .invoke(result -> log.debug("Security setup completed successfully for organic type {}", persisted.getName()))
                                                                     .chain(() -> Uni.createFrom()
                                                                                     .item(persisted));
                                                 });
                        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IInvolvedPartyIdentificationType<?, ?>> findInvolvedPartyIdentificationType(Mutiny.Session session, String idType, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedPartyIdentificationType by name: {}", idType);
        var enterprise = system.getEnterprise();
        java.util.UUID enterpriseId = null;
        java.util.UUID systemId = null;
        if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
            enterpriseId = ent.getId();
        }
        if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys) {
            systemId = sys.getId();
        }
        String key = enterpriseId + "|" + systemId + "|" + idType;
        java.util.UUID cachedId = involvedPartyIdentificationTypeKeyToId.get(key);
        if (cachedId != null) {
            log.trace("🔁 InvolvedPartyIdentificationType cache hit for key '{}': {} — loading by UUID", key, cachedId);
            return getInvolvedPartyIdentificationTypeById(session, cachedId).flatMap(found -> {
                if (found != null) {
                    return Uni.createFrom()
                              .item(found);
                }
                involvedPartyIdentificationTypeKeyToId.remove(key);
                InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
                return xr.builder(session)
                         .withName(idType)
                         .inActiveRange()
                         .inDateRange()
                         .withEnterprise(enterprise)
                         .get()
                         .onFailure(NoResultException.class)
                         .invoke(e -> log.warn("InvolvedPartyIdentificationType with name '{}' could not be found", idType))
                         .invoke(res -> {
                             if (res != null && res.getId() != null) {
                                 involvedPartyIdentificationTypeKeyToId.put(key, res.getId());
                             }
                         });
            });
        }
        InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
        return (Uni) xr.builder(session)
                       .withName(idType)
                       .inActiveRange()
                       .inDateRange()
                       .withEnterprise(enterprise)
                       .get()
                       .onFailure(NoResultException.class)
                       .invoke(e -> log.warn("InvolvedPartyIdentificationType with name '{}' could not be found", idType))
                       .invoke(res -> {
                           if (res != null && res.getId() != null) {
                               involvedPartyIdentificationTypeKeyToId.put(key, res.getId());
                           }
                       });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByResourceItem(Mutiny.Session session, IResourceItem<?, ?> idType, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedParty by ResourceItem: value={}", value);
        return new InvolvedPartyXResourceItem().builder(session)
                                               .canRead(system, identityToken)
                                               .inActiveRange()
                                               .inDateRange()
                                               .findLink(null, (ResourceItem) idType, value)
                                               .setReturnFirst(true)
                                               .get()
                                               .onFailure(NoResultException.class)
                                               .invoke(e -> log.warn("InvolvedParty by ResourceItem with value '{}' could not be found", value))
                                               .onItem()
                                               .transform(InvolvedPartyXResourceItem::getInvolvedPartyID);

    }

    @Override
    public Uni<IInvolvedParty<?, ?>> create(Mutiny.Session session, ISystems<?, ?> system, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken)
    {
        return create(session, system, null, idTypes, isOrganic, identityToken);
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> create(Mutiny.Session session, ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, UUID... identityToken)
    {
        // Public create → world-readable (public/default security matrix).
        return createWithSecurity(session, system, key, idTypes, isOrganic, rec -> rec.createDefaultSecurity(session, system, identityToken), identityToken);
    }

    /**
     * Opt-in <strong>scope-restricted</strong> involved-party create. Identical to
     * {@link #create(Mutiny.Session, ISystems, UUID, Pair, boolean, UUID...)} except the party (and its
     * organic/non-organic record) are secured with the restricted matrix: only Administrators / Systems /
     * Applications / Plugins retain access, plus a <em>read</em> grant for {@code scopeToken}. Only identity
     * tokens at that scope node or below it may read the party.
     */
    @Override
    public Uni<IInvolvedParty<?, ?>> createScopeRestricted(Mutiny.Session session, ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, ISecurityToken<?, ?> scopeToken, UUID... identityToken)
    {
        return createWithSecurity(session, system, key, idTypes, isOrganic, rec -> rec.createScopeRestrictedSecurity(session, system, scopeToken, identityToken), identityToken);
    }

    private Uni<IInvolvedParty<?, ?>> createWithSecurity(Mutiny.Session session, ISystems<?, ?> system, UUID key, Pair<String, String> idTypes, boolean isOrganic, java.util.function.Function<IWarehouseCoreTable<?, ?, ?, ?>, Uni<?>> securityFn, UUID... identityToken)
    {
        log.trace("Creating InvolvedParty: key={}, idTypes={}, isOrganic={}", key, idTypes, isOrganic);

        var enterprise = system.getEnterprise();

        final InvolvedParty ip = new InvolvedParty();
        ip.setEnterpriseID(enterprise);

        final UUID finalKey = (key == null) ? UUID.randomUUID() : key;
        ip.setId(finalKey);
        ip.setSystemID(system);
        ip.setOriginalSourceSystemID(system.getId());

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        return acService.getActiveFlag(session, enterprise, identityToken)
                        .chain(activeFlag -> {
                            ip.setActiveFlagID(activeFlag);
                            return session.persist(ip)
                                          .replaceWith(ip);
                        })
                        .chain(persisted -> {
                            // Handle security setup sequentially (strategy supplied by the caller).
                            return securityFn.apply(persisted)
                                             .chain(() -> findInvolvedPartyIdentificationType(session, idTypes.getKey(), system, identityToken).chain(involvedPartyIdentificationType -> {
                                                                                                                                                   // Add identification type to involved party
                                                                                                                                                   try {
                                                                                                                                                       return persisted.addOrUpdateInvolvedPartyIdentificationType(session,
                                                                                                                                                                                                                   NoClassification.toString(),
                                                                                                                                                                                                                   involvedPartyIdentificationType,
                                                                                                                                                                                                                   idTypes.getValue(),
                                                                                                                                                                                                                   idTypes.getValue(),
                                                                                                                                                                                                                   system,
                                                                                                                                                                                                                   identityToken
                                                                                                                                                                       )
                                                                                                                                                                       .replaceWith(persisted);
                                                                                                                                                   } catch (Exception e) {
                                                                                                                                                       return Uni.createFrom()
                                                                                                                                                                 .failure(e);
                                                                                                                                                   }
                                                                                                                                               })
                                                                                                                                               .chain(updatedPersisted -> {
                                                                                                                                                   // Setup organic status sequentially
                                                                                                                                                   return setupInvolvedPartyOrganicStatus(session,
                                                                                                                                                                                          isOrganic,
                                                                                                                                                                                          updatedPersisted,
                                                                                                                                                                                          securityFn,
                                                                                                                                                                                          system,
                                                                                                                                                                                          identityToken
                                                                                                                                                   ).chain(() -> Uni.createFrom()
                                                                                                                                                                    .item((IInvolvedParty<?, ?>) updatedPersisted));
                                                                                                                                               }));
                        });
    }

    private Uni<Void> setupInvolvedPartyOrganicStatus(Mutiny.Session session, boolean isOrganic, IInvolvedParty<?, ?> ip, java.util.function.Function<IWarehouseCoreTable<?, ?, ?, ?>, Uni<?>> securityFn, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Setting up InvolvedParty organic status: isOrganic={}, id={}", isOrganic, ip.getId());

        var enterprise = system.getEnterprise();

        IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
        if (isOrganic) {
            InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
            ipo.setInvolvedParty((InvolvedParty) ip);
            ipo.setId(ip.getId());
            ipo.setEnterpriseID(enterprise);
            ipo.setSystemID(system);
            ipo.setOriginalSourceSystemID(system.getId());

            return acService.getActiveFlag(session, enterprise, identityToken)
                            .chain(activeFlag -> {
                                ipo.setActiveFlagID(activeFlag);
                                return session.persist(ipo)
                                              .replaceWith(ipo);
                            })
                            .chain(persisted -> {
                                // Handle security setup sequentially
                                return securityFn.apply(persisted)
                                                 .onItem()
                                                 .invoke(result -> log.debug("Security setup completed successfully for organic involved party {}", persisted.getId()))
                                                 .onFailure()
                                                 .recoverWithItem(error -> {
                                                     log.warn("Error in security setup for organic", error);
                                                     return null; // Continue the chain even if security creation fails
                                                 })
                                                 .chain(() -> Uni.createFrom()
                                                                 .voidItem());
                            });
        }
        else {
            InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
            ipo.setInvolvedParty((InvolvedParty) ip);
            ipo.setId(ip.getId());
            ipo.setEnterpriseID(enterprise);
            ipo.setSystemID(system);
            ipo.setOriginalSourceSystemID(system.getId());

            return acService.getActiveFlag(session, enterprise, identityToken)
                            .chain(activeFlag -> {
                                ipo.setActiveFlagID(activeFlag);
                                return session.persist(ipo)
                                              .replaceWith(ipo);
                            })
                            .chain(persisted -> {
                                // Handle security setup sequentially
                                return securityFn.apply(persisted)
                                                 .onItem()
                                                 .invoke(result -> log.debug("Security setup completed successfully for non-organic involved party {}", persisted.getId()))
                                                 .onFailure()
                                                 .recoverWithItem(error -> {
                                                     log.warn("Error in security setup for non-organic", error);
                                                     return null; // Continue the chain even if security creation fails
                                                 })
                                                 .chain(() -> Uni.createFrom()
                                                                 .voidItem());
                            });
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IInvolvedPartyType<?, ?>> findType(Mutiny.Session session, String nameType, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedPartyType by name: {}", nameType);
        var enterprise = system.getEnterprise();
        java.util.UUID enterpriseId = null;
        java.util.UUID systemId = null;
        if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
            enterpriseId = ent.getId();
        }
        if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys) {
            systemId = sys.getId();
        }
        String key = enterpriseId + "|" + systemId + "|" + nameType;
        java.util.UUID cachedId = involvedPartyTypeKeyToId.get(key);
        if (cachedId != null) {
            log.trace("🔁 InvolvedPartyType cache hit for key '{}': {} — loading by UUID", key, cachedId);
            return getInvolvedPartyTypeById(session, cachedId).flatMap(found -> {
                if (found != null) {
                    return Uni.createFrom()
                              .item(found);
                }
                involvedPartyTypeKeyToId.remove(key);
                InvolvedPartyType xr = new InvolvedPartyType();
                return xr.builder(session)
                         .withName(nameType)
                         .inActiveRange()
                         .withEnterprise(enterprise)
                         .inDateRange()
                         .get()
                         .onFailure(NoResultException.class)
                         .invoke(e -> log.warn("InvolvedPartyType with name '{}' could not be found", nameType))
                         .invoke(res -> {
                             if (res != null && res.getId() != null) {
                                 involvedPartyTypeKeyToId.put(key, res.getId());
                             }
                         });
            });
        }
        InvolvedPartyType xr = new InvolvedPartyType();
        return (Uni) xr.builder(session)
                       .withName(nameType)
                       .inActiveRange()
                       .withEnterprise(enterprise)
                       .inDateRange()
                       .get()
                       .onFailure(NoResultException.class)
                       .invoke(e -> log.warn("InvolvedPartyType with name '{}' could not be found", nameType))
                       .invoke(res -> {
                           if (res != null && res.getId() != null) {
                               involvedPartyTypeKeyToId.put(key, (java.util.UUID) res.getId());
                           }
                       });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Uni<IInvolvedPartyNameType<?, ?>> findInvolvedPartyNameType(Mutiny.Session session, String nameType, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedPartyNameType by name: {}", nameType);
        var enterprise = system.getEnterprise();
        java.util.UUID enterpriseId = null;
        java.util.UUID systemId = null;
        if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
            enterpriseId = ent.getId();
        }
        if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems sys) {
            systemId = sys.getId();
        }
        String key = enterpriseId + "|" + systemId + "|" + nameType;
        java.util.UUID cachedId = involvedPartyNameTypeKeyToId.get(key);
        if (cachedId != null) {
            log.trace("🔁 InvolvedPartyNameType cache hit for key '{}': {} — loading by UUID", key, cachedId);
            return getInvolvedPartyNameTypeById(session, cachedId).flatMap(found -> {
                if (found != null) {
                    return Uni.createFrom()
                              .item(found);
                }
                involvedPartyNameTypeKeyToId.remove(key);
                InvolvedPartyNameType xr = new InvolvedPartyNameType();
                return xr.builder(session)
                         .withName(nameType)
                         .inActiveRange()
                         .inDateRange()
                         .withEnterprise(enterprise)
                         .get()
                         .onFailure(NoResultException.class)
                         .invoke(e -> log.warn("InvolvedPartyNameType with name '{}' could not be found", nameType))
                         .invoke(res -> {
                             if (res != null && res.getId() != null) {
                                 involvedPartyNameTypeKeyToId.put(key, res.getId());
                             }
                         });
            });
        }
        InvolvedPartyNameType xr = new InvolvedPartyNameType();
        return (Uni) xr.builder(session)
                       .withName(nameType)
                       .inActiveRange()
                       .inDateRange()
                       .withEnterprise(enterprise)
                       .get()
                       .onFailure(NoResultException.class)
                       .invoke(e -> log.warn("InvolvedPartyNameType with name '{}' could not be found", nameType))
                       .invoke(res -> {
                           if (res != null && res.getId() != null) {
                               involvedPartyNameTypeKeyToId.put(key, res.getId());
                           }
                       });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByToken(Mutiny.Session session, ISecurityToken<?, ?> token, UUID... identityToken)
    {
        log.debug("Finding InvolvedParty by token: {}", token.getSecurityToken());

        var sys = ((SecurityToken) token).getSystemID();
        var enterprise = sys.getEnterprise();
        java.util.UUID enterpriseId = null;
        java.util.UUID systemId = null;
        if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
            enterpriseId = ent.getId();
        }
        if (sys instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems s) {
            systemId = s.getId();
        }
        String identTypeName = IdentificationTypeUUID.toString();
        String key = enterpriseId + "|" + systemId + "|" + identTypeName + "|" + token.getSecurityToken();

        java.util.UUID cachedId = involvedPartyKeyToId.get(key);
        if (cachedId != null) {
            log.trace("🔁 InvolvedParty cache hit for key '{}' → {} — loading by UUID", key, cachedId);
            return getInvolvedPartyById(session, cachedId).flatMap(found -> {
                if (found != null) {
                    return Uni.createFrom()
                              .item(found);
                }
                involvedPartyKeyToId.remove(key); // stale
                return proceedFindByTokenColdPath(session, token, identityToken).invoke(p -> {
                    if (p != null && p.getId() != null) {
                        involvedPartyKeyToId.put(key, p.getId());
                    }
                });
            });
        }

        // Cold path
        return proceedFindByTokenColdPath(session, token, identityToken).invoke(p -> {
            if (p != null && p.getId() != null) {
                involvedPartyKeyToId.put(key, p.getId());
            }
        });
    }

    private Uni<IInvolvedParty<?, ?>> proceedFindByTokenColdPath(Mutiny.Session session, ISecurityToken<?, ?> token, UUID... identityToken)
    {
        return findInvolvedPartyIdentificationType(session, IdentificationTypeUUID.toString(), ((SecurityToken) token).getSystemID(), identityToken).chain(id -> {
            InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
            return idType.builder(session)
                         .findLink(null, (InvolvedPartyIdentificationType) id, token.getSecurityToken())
                         .inActiveRange()
                         .inDateRange()
                         .canRead(((SecurityToken) token).getSystemID(), identityToken)
                         .get()
                         .onFailure(NoResultException.class)
                         .invoke(e -> log.warn("InvolvedParty by token '{}' could not be found", token.getSecurityToken()))
                         .onItem()
                         .transform(item -> (IInvolvedParty<?, ?>) item.getInvolvedPartyID());
        });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> find(Mutiny.Session session, UUID uuid)
    {
        log.debug("🔍 Finding InvolvedParty by UUID: {} with session: {}", uuid, session.hashCode());
        return new InvolvedParty().builder(session)
                                  .find(uuid)
                                  .get()
                                  .onFailure(NoResultException.class)
                                  .invoke(e -> log.warn("InvolvedParty with UUID '{}' could not be found", uuid))
                                  .onItem()
                                  .invoke(result -> {
                                      if (result != null) {
                                          log.debug("✅ Found InvolvedParty with UUID: {}", uuid);
                                      }
                                      else {
                                          log.debug("⚠️ InvolvedParty with UUID: {} not found", uuid);
                                      }
                                  })
                                  .onItem()
                                  .ifNull()
                                  .failWith(() -> new InvolvedPartyException("The InvolvedParty does not exist - " + uuid))
                                  .map(involvedParty -> involvedParty);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IInvolvedParty<?, ?>> find(Mutiny.StatelessSession session, UUID uuid)
    {
        log.trace("🔍 Finding InvolvedParty by UUID (stateless): {}", uuid);
        return getStatelessInvolvedPartyById(session, uuid);
    }

    @Override
    public Uni<IInvolvedPartyType<?, ?>> findType(Mutiny.Session session, UUID uuid)
    {
        log.debug("🔍 Finding InvolvedPartyType by UUID: {} with session: {}", uuid, session.hashCode());
        return new InvolvedPartyType().builder(session)
                                      .find(uuid)
                                      .get()
                                      .onFailure(NoResultException.class)
                                      .invoke(e -> log.warn("InvolvedPartyType with UUID '{}' could not be found", uuid))
                                      .onItem()
                                      .invoke(result -> {
                                          if (result != null) {
                                              log.debug("✅ Found InvolvedPartyType with UUID: {}", uuid);
                                          }
                                          else {
                                              log.debug("⚠️ InvolvedPartyType with UUID: {} not found", uuid);
                                          }
                                      })
                                      .onItem()
                                      .ifNull()
                                      .failWith(() -> new InvolvedPartyException("The InvolvedPartyType does not exist - " + uuid))
                                      .map(involvedPartyType -> involvedPartyType);
    }

    @Override
    public Uni<IInvolvedPartyNameType<?, ?>> findNameType(Mutiny.Session session, UUID uuid)
    {
        log.debug("🔍 Finding InvolvedPartyNameType by UUID: {} with session: {}", uuid, session.hashCode());
        return new InvolvedPartyNameType().builder(session)
                                          .find(uuid)
                                          .get()
                                          .onFailure(NoResultException.class)
                                          .invoke(e -> log.warn("InvolvedPartyNameType with UUID '{}' could not be found", uuid))
                                          .onItem()
                                          .invoke(result -> {
                                              if (result != null) {
                                                  log.debug("✅ Found InvolvedPartyNameType with UUID: {}", uuid);
                                              }
                                              else {
                                                  log.debug("⚠️ InvolvedPartyNameType with UUID: {} not found", uuid);
                                              }
                                          })
                                          .onItem()
                                          .ifNull()
                                          .failWith(() -> new InvolvedPartyException("The InvolvedPartyNameType does not exist - " + uuid))
                                          .map(nameType -> nameType);
    }

    @Override
    public Uni<IInvolvedPartyIdentificationType<?, ?>> findIdentificationType(Mutiny.Session session, UUID uuid)
    {
        log.debug("Finding InvolvedPartyIdentificationType by UUID: {}", uuid);
        return (Uni) new InvolvedPartyIdentificationType().builder(session)
                                                          .find(uuid)
                                                          .get()
                                                          .onFailure(NoResultException.class)
                                                          .invoke(e -> log.warn("InvolvedPartyIdentificationType with UUID '{}' could not be found", uuid));

    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IInvolvedPartyNameType<?, ?>> findNameType(Mutiny.StatelessSession session, UUID uuid)
    {
        log.debug("🔍 Finding InvolvedPartyNameType by UUID (stateless): {}", uuid);
        return (Uni) new InvolvedPartyNameType().builder(session)
                                                .find(uuid)
                                                .get()
                                                .onFailure(NoResultException.class)
                                                .invoke(e -> log.warn("InvolvedPartyNameType with UUID '{}' could not be found (stateless)", uuid))
                                                .onItem()
                                                .ifNull()
                                                .failWith(() -> new InvolvedPartyException("The InvolvedPartyNameType does not exist - " + uuid));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IInvolvedPartyIdentificationType<?, ?>> findIdentificationType(Mutiny.StatelessSession session, UUID uuid)
    {
        log.debug("Finding InvolvedPartyIdentificationType by UUID (stateless): {}", uuid);
        return (Uni) new InvolvedPartyIdentificationType().builder(session)
                                                          .find(uuid)
                                                          .get()
                                                          .onFailure(NoResultException.class)
                                                          .invoke(e -> log.warn("InvolvedPartyIdentificationType with UUID '{}' could not be found (stateless)", uuid));
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByUUID(Mutiny.Session session, UUID token, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding InvolvedParty by UUID token: {}", token);
        var enterprise = system.getEnterprise();
        java.util.UUID enterpriseId = null;
        java.util.UUID systemId = null;
        if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
            enterpriseId = ent.getId();
        }
        if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems s) {
            systemId = s.getId();
        }
        String identTypeName = IdentificationTypeUUID.toString();
        String key = enterpriseId + "|" + systemId + "|" + identTypeName + "|" + token.toString();

        java.util.UUID cachedId = involvedPartyKeyToId.get(key);
        if (cachedId != null) {
            log.trace("🔁 InvolvedParty cache hit for key '{}' → {} — loading by UUID", key, cachedId);
            return getInvolvedPartyById(session, cachedId).flatMap(found -> {
                if (found != null) {
                    return Uni.createFrom()
                              .item(found);
                }
                involvedPartyKeyToId.remove(key);
                return proceedFindByUUIDColdPath(session, token, system, identityToken).invoke(p -> {
                    if (p != null && p.getId() != null) {
                        involvedPartyKeyToId.put(key, p.getId());
                    }
                });
            });
        }

        // Cold path
        return proceedFindByUUIDColdPath(session, token, system, identityToken).invoke(p -> {
            if (p != null && p.getId() != null) {
                involvedPartyKeyToId.put(key, p.getId());
            }
        });
    }

    private Uni<IInvolvedParty<?, ?>> proceedFindByUUIDColdPath(Mutiny.Session session, UUID token, ISystems<?, ?> system, UUID... identityToken)
    {
        var enterprise = system.getEnterprise();
        return findInvolvedPartyIdentificationType(session, IdentificationTypeUUID.toString(), system, identityToken).chain(id -> {
                                                                                                                         InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
                                                                                                                         return idType.builder(session)
                                                                                                                                      .findLink(null, (InvolvedPartyIdentificationType) id, token.toString())
                                                                                                                                      .inActiveRange()
                                                                                                                                      .inDateRange()
                                                                                                                                      .withEnterprise(enterprise)
                                                                                                                                      .canRead(system, identityToken)
                                                                                                                                      .get()
                                                                                                                                      .onFailure(NoResultException.class)
                                                                                                                                      .invoke(e -> log.warn("InvolvedParty by UUID '{}' could not be found", token));
                                                                                                                     })
                                                                                                                     .chain(idxid -> session.fetch(idxid.getInvolvedPartyID()));
    }

    @Override
    public Uni<List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>> findAllByIdentificationType(Mutiny.Session session, String identificationType, String value)
    {
        log.debug("Finding all InvolvedParties by identification type: {}, value: {}", identificationType, value);
        InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder(session);
        builder.inDateRange()
               .where(InvolvedPartyIdentificationType_.name, Equals, identificationType);

        InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder(session);
        if (value != null) {
            ipQb.withValue(value);
        }

        ipQb.inDateRange()
            .orderBy(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, DESC)
            .join(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, builder, JoinType.INNER);

        return ipQb.getAll()
                   .onFailure()
                   .invoke(error -> log.error("Error finding involved parties by identification type: {}", error.getMessage(), error))
                   .map(list -> (List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>) (List<?>) list);

    }

    @Override
    public Uni<List<IInvolvedParty<?, ?>>> findByRulesClassification(Mutiny.Session session, String classification, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedParties by rules classification: {}, value: {}", classification, value);
        return classificationService.find(session, classification, system, identityToken)
                                    .chain(classification1 -> {
                                        return new InvolvedPartyXRules().builder(session)
                                                                        .withClassification(classification1)
                                                                        .withValue(value)
                                                                        .inActiveRange()
                                                                        .inDateRange()
                                                                        .getAll()
                                                                        .onFailure()
                                                                        .invoke(error -> log.error("Error finding involved parties by rules classification: {}", error.getMessage(), error))
                                                                        .map(list -> {
                                                                            List<IInvolvedParty<?, ?>> result = new ArrayList<>();
                                                                            for (InvolvedPartyXRules item : list) {
                                                                                result.add(item.getInvolvedPartyID());
                                                                            }
                                                                            return result;
                                                                        });
                                    });
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByClassification(Mutiny.Session session, String classification, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedParty by classification: {}, value: {}", classification, value);
        return classificationService.find(session, classification, system, identityToken)
                                    .chain(classification1 -> {
                                        return new InvolvedPartyXClassification().builder(session)
                                                                                 .withClassification(classification1)
                                                                                 .withValue(value)
                                                                                 .inActiveRange()
                                                                                 .inDateRange()
                                                                                 .get()
                                                                                 .onFailure()
                                                                                 .invoke(error -> log.error("Error finding involved party by classification: {}", error.getMessage(), error))
                                                                                 .onItem()
                                                                                 .ifNotNull()
                                                                                 .transform(item -> (IInvolvedParty<?, ?>) item.getPrimary())
                                                                                 .onItem()
                                                                                 .ifNull()
                                                                                 .continueWith(() -> null);
                                    });

    }

    // ============================================================================================
    // Stateless finder twins (builder reads; associations resolved via session.fetch).
    // ============================================================================================

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Uni<IInvolvedParty<?, ?>> findByResourceItem(Mutiny.StatelessSession session, IResourceItem<?, ?> idType, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedParty by ResourceItem (stateless): value={}", value);
        return (Uni) new InvolvedPartyXResourceItem().builder(session)
                                                     .canRead(system, identityToken)
                                                     .inActiveRange()
                                                     .inDateRange()
                                                     .findLink(null, (ResourceItem) idType, value)
                                                     .setReturnFirst(true)
                                                     .get()
                                                     .onFailure(NoResultException.class)
                                                     .invoke(e -> log.warn("InvolvedParty by ResourceItem (stateless) with value '{}' could not be found", value))
                                                     .chain(item -> item == null ? Uni.createFrom()
                                                                                      .nullItem() : session.fetch(item.getInvolvedPartyID()));
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByToken(Mutiny.StatelessSession session, ISecurityToken<?, ?> token, UUID... identityToken)
    {
        log.debug("Finding InvolvedParty by token (stateless): {}", token.getSecurityToken());
        var sys = ((SecurityToken) token).getSystemID();
        return findInvolvedPartyIdentificationType(session, IdentificationTypeUUID.toString(), sys, identityToken)
                .chain(id ->
                               new InvolvedPartyXInvolvedPartyIdentificationType()
                                       .builder(session)
                                       .findLink(null,
                                                 (InvolvedPartyIdentificationType) id,
                                                 token.getSecurityToken()
                                       )
                                       .inActiveRange()
                                       .inDateRange()
                                       .canRead(sys,
                                                identityToken
                                       )
                                       .get()
                                       .onFailure(
                                               NoResultException.class)
                                       .invoke(e -> log.warn(
                                               "InvolvedParty by token (stateless) '{}' could not be found",
                                               token.getSecurityToken()
                                       ))
                                       .chain(item -> session.fetch(
                                               item.getInvolvedPartyID())));
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByUUID(Mutiny.StatelessSession session, UUID token, ISystems<?, ?> system, UUID... identityToken)
    {
        log.trace("Finding InvolvedParty by UUID token (stateless): {}", token);
        var enterprise = system.getEnterprise();
        java.util.UUID enterpriseId = null;
        java.util.UUID systemId = null;
        if (enterprise instanceof com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise ent) {
            enterpriseId = ent.getId();
        }
        if (system instanceof com.guicedee.activitymaster.fsdm.db.entities.systems.Systems s) {
            systemId = s.getId();
        }
        String identTypeName = IdentificationTypeUUID.toString();
        String key = enterpriseId + "|" + systemId + "|" + identTypeName + "|" + token;

        java.util.UUID cachedId = involvedPartyKeyToId.get(key);
        if (cachedId != null) {
            log.trace("InvolvedParty stateless UUID-token cache hit for key '{}' -> {}", key, cachedId);
            return getStatelessInvolvedPartyById(session, cachedId);
        }

        return findInvolvedPartyIdentificationType(session, identTypeName, system, identityToken)
                .chain(id -> new InvolvedPartyXInvolvedPartyIdentificationType().builder(session)
                                                                                .findLink(null,
                                                                                          (InvolvedPartyIdentificationType) id,
                                                                                          token.toString())
                                                                                .inActiveRange()
                                                                                .inDateRange()
                                                                                .withEnterprise(enterprise)
                                                                                .canRead(system, identityToken)
                                                                                .get()
                                                                                .onFailure(NoResultException.class)
                                                                                .invoke(e -> log.warn(
                                                                                        "InvolvedParty by UUID (stateless) '{}' could not be found",
                                                                                        token)))
                .chain(idxid -> session.fetch(idxid.getInvolvedPartyID()))
                .invoke(involvedParty -> {
                    if (involvedParty != null && involvedParty.getId() != null) {
                        involvedPartyKeyToId.put(key, (UUID) involvedParty.getId());
                        cacheStatelessInvolvedParty((IInvolvedParty<?, ?>) involvedParty);
                    }
                })
                .map(involvedParty -> (IInvolvedParty<?, ?>) involvedParty);
    }

    @Override
    public Uni<List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>> findAllByIdentificationType(Mutiny.StatelessSession session, String identificationType, String value)
    {
        log.debug("Finding all InvolvedParties by identification type (stateless): {}, value: {}", identificationType, value);
        InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder(session);
        builder.inDateRange()
               .where(InvolvedPartyIdentificationType_.name, Equals, identificationType);

        InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder(session);
        if (value != null) {
            ipQb.withValue(value);
        }
        ipQb.inDateRange()
            .orderBy(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, DESC)
            .join(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, builder, JoinType.INNER);

        return ipQb.getAll()
                   .map(list -> (List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>>) (List<?>) list);
    }

    @Override
    public Uni<List<IInvolvedParty<?, ?>>> findByRulesClassification(Mutiny.StatelessSession session, String classification, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedParties by rules classification (stateless): {}, value: {}", classification, value);
        return classificationService.find(session, classification, system, identityToken)
                                    .chain(classification1 -> new InvolvedPartyXRules().builder(session)
                                                                                       .withClassification(classification1)
                                                                                       .withValue(value)
                                                                                       .inActiveRange()
                                                                                       .inDateRange()
                                                                                       .getAll()
                                                                                       .chain(list -> {
                                                                                           Uni<List<IInvolvedParty<?, ?>>> acc = Uni.createFrom()
                                                                                                                                    .item(new ArrayList<>());
                                                                                           for (InvolvedPartyXRules item : list) {
                                                                                               acc = acc.chain(result -> session.fetch(item.getInvolvedPartyID())
                                                                                                                                .map(p -> {
                                                                                                                                    result.add(p);
                                                                                                                                    return result;
                                                                                                                                }));
                                                                                           }
                                                                                           return acc;
                                                                                       }));
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByClassification(Mutiny.StatelessSession session, String classification, String value, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding InvolvedParty by classification (stateless): {}, value: {}", classification, value);
        return classificationService.find(session, classification, system, identityToken)
                                    .chain(classification1 -> new InvolvedPartyXClassification().builder(session)
                                                                                                .withClassification(classification1)
                                                                                                .withValue(value)
                                                                                                .inActiveRange()
                                                                                                .inDateRange()
                                                                                                .get()
                                                                                                .onFailure(NoResultException.class)
                                                                                                .invoke(e -> log.warn("InvolvedParty by classification value {}  (stateless) '{}' could not be found",
                                                                                                                      classification,
                                                                                                                      value
                                                                                                ))
                                                                                                .chain(item -> item == null ? Uni.createFrom()
                                                                                                                                 .nullItem() : session.fetch(item.getPrimary()))
                                                                                                .map(p -> (IInvolvedParty<?, ?>) p));
    }
}

