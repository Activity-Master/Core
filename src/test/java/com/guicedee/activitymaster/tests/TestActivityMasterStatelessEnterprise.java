package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IInvolvedPartyService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SystemsClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IPTypes;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.NameTypes;
import com.guicedee.activitymaster.fsdm.ActiveFlagService;
import com.guicedee.activitymaster.fsdm.ClassificationsDataConceptService;
import com.guicedee.activitymaster.fsdm.systems.ActiveFlagSystem;
import com.guicedee.activitymaster.fsdm.systems.ClassificationsDataConceptSystem;
import com.guicedee.activitymaster.fsdm.systems.ClassificationsSystem;
import com.guicedee.activitymaster.fsdm.systems.EventsSystem;
import com.guicedee.activitymaster.fsdm.systems.InvolvedPartySystem;
import com.guicedee.activitymaster.fsdm.systems.ProductsSystem;
import com.guicedee.activitymaster.fsdm.systems.RulesSystem;
import com.guicedee.activitymaster.fsdm.systems.SecurityTokenSystem;
import com.guicedee.activitymaster.fsdm.systems.SystemsSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 3 safety net for the <em>stateless-session enterprise lifecycle</em> work
 * (see {@code stateless-enterprise-creation-prompt.md}).
 *
 * <p>The enterprise is provisioned once through the proven <strong>stateful</strong>
 * {@link IEnterpriseService#startNewEnterprise(Mutiny.Session, String, String, String)} path, then every
 * genuinely-stateless lookup/leaf added in Phases 1-2 is asserted to return results that match the
 * stateful path:</p>
 *
 * <ul>
 *     <li>{@link IEnterpriseService#getEnterprise(Mutiny.StatelessSession, String)} /
 *         {@link IEnterpriseService#getEnterprise(Mutiny.StatelessSession, UUID)} resolve the same row;</li>
 *     <li>{@link IEnterpriseService#create(Mutiny.StatelessSession, String, String)} (impl) is idempotent —
 *         find-or-create returns the existing enterprise, never a duplicate;</li>
 *     <li>{@link ISystemsService#getActivityMaster(Mutiny.StatelessSession, IEnterprise, UUID...)},
 *         {@link ISystemsService#findSystem(Mutiny.StatelessSession, IEnterprise, String, UUID...)} and
 *         {@link ISystemsService#doesSystemExist(Mutiny.StatelessSession, IEnterprise, String, UUID...)}
 *         match their {@code Mutiny.Session} counterparts.</li>
 * </ul>
 *
 * <p>This protects the bridge/lookup surface so later phases can push statelessness deeper without
 * silently regressing the stateless entry points.</p>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterStatelessEnterprise {

    protected Mutiny.SessionFactory sessionFactory;

    /** Resolved once in {@link #setup()} from the stateful provisioning, used as the parity baseline. */
    private UUID enterpriseId;
    private UUID activityMasterSystemId;

    @BeforeAll
    public void setup() {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Pre-resolve singletons on the test thread before entering any Vert.x callback (CallScope).
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);

        // Ensure the enterprise + Activity Master system exist (proven provisioning sequence).
        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.getEnterprise(session, TestEnterprise.name())
                                .onFailure().recoverWithUni(t -> {
                                    var ent = enterpriseService.get();
                                    ent.setName(TestEnterprise.name());
                                    ent.setDescription("Enterprise for Stateless Lifecycle Testing");
                                    return enterpriseService.createNewEnterprise(session, ent);
                                })
                                .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                        .onFailure().recoverWithUni(t -> systemsService.create(session, (IEnterprise<?, ?>) ent,
                                                ISystemsService.ActivityMasterSystemName, "Activity Master System")))
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.ofMinutes(2));

        // Start the enterprise (idempotent) — seeds canonical security groups/folders + default security.
        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.startNewEnterprise(session, TestEnterprise.name(), "admin", "!@adminadmin")
                                .onFailure().recoverWithItem(e -> null)
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.ofMinutes(2));

        // Capture the stateful baseline (enterprise id + Activity Master system id).
        Object[] baseline = sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.getEnterprise(session, TestEnterprise.name())
                                .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                        .map(sys -> new Object[]{ent.getId(), ((ISystems<?, ?>) sys).getId()}))
                )
        ).await().atMost(Duration.ofMinutes(1));

        enterpriseId = (UUID) baseline[0];
        activityMasterSystemId = (UUID) baseline[1];
        assertNotNull(enterpriseId, "Stateful baseline enterprise id must resolve");
        assertNotNull(activityMasterSystemId, "Stateful baseline Activity Master system id must resolve");
    }

    @Test
    @Order(1)
    public void getEnterpriseByName_stateless_matchesStateful() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        IEnterprise<?, ?> ent = sessionFactory.withStatelessSession(session ->
                es.getEnterprise(session, TestEnterprise.name())
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(ent, "Stateless getEnterprise(name) must resolve the enterprise");
        assertEquals(enterpriseId, ent.getId(),
                "Stateless getEnterprise(name) must return the same id as the stateful path");
    }

    @Test
    @Order(2)
    public void getEnterpriseByUuid_stateless_matchesStateful() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        IEnterprise<?, ?> ent = sessionFactory.withStatelessSession(session ->
                es.getEnterprise(session, enterpriseId)
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(ent, "Stateless getEnterprise(uuid) must resolve the enterprise");
        assertEquals(enterpriseId, ent.getId(), "Stateless getEnterprise(uuid) must return the requested row");
        assertEquals(TestEnterprise.name(), ent.getName(), "Resolved enterprise name must match");
    }

    @Test
    @Order(3)
    public void create_stateless_isIdempotent() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);

        UUID firstId = sessionFactory.withStatelessTransaction(session ->
                es.create(session, TestEnterprise.name(), TestEnterprise.name()).map(e -> (UUID) e.getId())
        ).await().atMost(Duration.ofMinutes(1));

        UUID secondId = sessionFactory.withStatelessTransaction(session ->
                es.create(session, TestEnterprise.name(), TestEnterprise.name()).map(e -> (UUID) e.getId())
        ).await().atMost(Duration.ofMinutes(1));

        assertEquals(enterpriseId, firstId,
                "Stateless create must find-or-return the already-provisioned enterprise, not a new row");
        assertEquals(firstId, secondId, "Stateless create must be idempotent across calls (no duplicate row)");
    }

    @Test
    @Order(4)
    public void doesSystemExist_stateless_matchesStateful() {
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);

        Boolean[] result = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.doesSystemExist(session, (IEnterprise<?, ?>) ent, ISystemsService.ActivityMasterSystemName)
                                .chain(exists -> ss.doesSystemExist(session, (IEnterprise<?, ?>) ent,
                                                "No Such System " + UUID.randomUUID())
                                        .map(missing -> new Boolean[]{exists, missing})))
        ).await().atMost(Duration.ofMinutes(1));

        assertTrue(result[0], "Stateless doesSystemExist must report the Activity Master system as installed");
        assertFalse(result[1], "Stateless doesSystemExist must report a non-existent system as absent");
    }

    @Test
    @Order(5)
    public void getActivityMasterId_stateless_matchesStateful() {
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);

        UUID statelessAmId = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMasterId(session, (IEnterprise<?, ?>) ent))
        ).await().atMost(Duration.ofMinutes(1));

        assertEquals(activityMasterSystemId, statelessAmId,
                "Stateless getActivityMasterId must resolve the same system id as the stateful path");
    }

    @Test
    @Order(6)
    public void findSystemId_stateless_matchesStateful() {
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);

        UUID statelessFoundId = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.findSystemId(session, (IEnterprise<?, ?>) ent,
                                ISystemsService.ActivityMasterSystemName))
        ).await().atMost(Duration.ofMinutes(1));

        assertEquals(activityMasterSystemId, statelessFoundId,
                "Stateless findSystemId must resolve the same Activity Master system id as the stateful path");
    }

    /**
     * End-to-end: drive the <strong>entire</strong> start/create-enterprise sequence from a
     * {@link Mutiny.StatelessSession} entry point. The stateless overload bridges the managed-entity
     * install onto internally-managed stateful sessions (required because the install creates/reads
     * {@code @Cacheable} entities with eager associations, which a stateless session cannot hydrate).
     * The whole process must complete and resolve the provisioned enterprise (idempotent re-run).
     */
    @Test
    @Order(7)
    public void startNewEnterprise_statelessEntry_completesFullProcess() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);

        IEnterprise<?, ?> result = sessionFactory.openStatelessSession()
                .chain(ss -> es.startNewEnterprise(ss, TestEnterprise.name(), "admin", "!@adminadmin")
                        .eventually(ss::close))
                .await().atMost(Duration.ofMinutes(3));

        assertNotNull(result, "Stateless-entry startNewEnterprise must complete and return the enterprise");
        assertEquals(enterpriseId, result.getId(),
                "Stateless-entry startNewEnterprise must resolve the provisioned enterprise");
    }

    /**
     * Stateless "fetch ids/scalars + prep": {@link ISystemsService#findSystem(Mutiny.StatelessSession,
     * IEnterprise, String, UUID...)} / {@link ISystemsService#getActivityMaster(Mutiny.StatelessSession,
     * IEnterprise, UUID...)} return a <em>detached, prepped</em> {@code Systems} built from a scalar
     * constructor projection (never an eager-association hydration). The prepped entity must carry the
     * same id + name as the stateful baseline and have its enterprise reference wired from the parameter.
     */
    @Test
    @Order(8)
    public void findSystem_stateless_returnsPreppedDetachedEntity() {
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);

        ISystems<?, ?> prepped = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.findSystem(session, (IEnterprise<?, ?>) ent,
                                ISystemsService.ActivityMasterSystemName))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(prepped, "Stateless findSystem must return a prepped detached Systems");
        assertEquals(activityMasterSystemId, prepped.getId(),
                "Prepped Systems id must match the stateful baseline");
        assertEquals(ISystemsService.ActivityMasterSystemName, prepped.getName(),
                "Prepped Systems must carry its own name from the scalar projection");
        assertNotNull(prepped.getEnterprise(), "Prepped Systems must have its enterprise reference wired");
        assertEquals(enterpriseId, prepped.getEnterprise().getId(),
                "Prepped Systems enterprise reference must be the supplied parameter");
    }

    /**
     * Stateless prepped {@code Classification}: {@link IClassificationService#getIdentityType(
     * Mutiny.StatelessSession, ISystems, UUID...)} returns a detached classification built from a scalar
     * projection (the eager {@code concept} association is never hydrated). It must resolve the same id +
     * name as the managed {@code Mutiny.Session} path, composing on the prepped stateless system.
     */
    @Test
    @Order(9)
    public void getIdentityType_stateless_matchesStateful() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);

        // Stateful baseline: managed session resolve of the identity-type classification.
        IClassification<?, ?> stateful = sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        es.getEnterprise(session, TestEnterprise.name())
                                .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                                .chain(sys -> cs.getIdentityType(session, (ISystems<?, ?>) sys)))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(stateful, "Stateful identity-type classification must resolve (baseline)");

        // Stateless prepped path: prepped system → prepped classification, all on one stateless unit.
        IClassification<?, ?> prepped = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.getIdentityType(session, (ISystems<?, ?>) sys))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(prepped, "Stateless getIdentityType must return a prepped detached Classification");
        assertEquals(stateful.getId(), prepped.getId(),
                "Stateless prepped classification id must match the stateful baseline");
        assertEquals(stateful.getName(), prepped.getName(),
                "Stateless prepped classification must carry its own name from the scalar projection");
    }

    /**
     * Stateless prepped {@code SecurityToken}: {@link ISecurityTokenService#getAdministratorsFolder(
     * Mutiny.StatelessSession, ISystems, UUID...)} returns a detached folder token built from a scalar
     * projection (id, securityToken, name, description) through the same folder/name/enterprise filters.
     * It must resolve the same id + name as the managed {@code Mutiny.Session} path, composing on the
     * prepped stateless system.
     */
    @Test
    @Order(10)
    public void getAdministratorsFolder_stateless_matchesStateful() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);

        // Stateful baseline: managed session resolve of the Administrators folder token.
        ISecurityToken<?, ?> stateful = sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        es.getEnterprise(session, TestEnterprise.name())
                                .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                                .chain(sys -> sts.getAdministratorsFolder(session, (ISystems<?, ?>) sys)))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(stateful, "Stateful Administrators folder token must resolve (baseline)");

        // Stateless prepped path: prepped system → prepped folder token, all on one stateless unit.
        ISecurityToken<?, ?> prepped = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> sts.getAdministratorsFolder(session, (ISystems<?, ?>) sys))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(prepped, "Stateless getAdministratorsFolder must return a prepped detached SecurityToken");
        assertEquals(stateful.getId(), prepped.getId(),
                "Stateless prepped Administrators token id must match the stateful baseline");
        assertEquals(stateful.getName(), prepped.getName(),
                "Stateless prepped Administrators token must carry its own name from the scalar projection");
    }

    /**
     * End-to-end fully-stateless write: resolve the seven canonical group/folder tokens on a
     * {@link Mutiny.StatelessSession} (via {@link ISecurityTokenService#resolveDefaultGroupFolderTokens})
     * composing on the prepped stateless system + stateless active flag, then write the entity's default
     * security through the existing stateless insert API
     * ({@link IWarehouseCoreTable#createDefaultSecurity(Mutiny.StatelessSession, ISystems, IEnterprise,
     * IActiveFlag, java.util.Map, UUID...)}) — all on a single stateless unit of work. Must insert exactly
     * the seven canonical grant rows. The Activity Master system is used as the owning record; the insert
     * count is deterministic regardless of any pre-existing rows.
     */
    @Test
    @Order(11)
    public void createDefaultSecurity_statelessEndToEnd_insertsSevenGrants() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);
        IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);

        Long inserted = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                .chain(sys -> afs.getActiveFlag(session, (IEnterprise<?, ?>) ent)
                                        .chain(af -> sts.resolveDefaultGroupFolderTokens(session, (ISystems<?, ?>) sys)
                                                .chain(tokens -> {
                                                    IWarehouseCoreTable<?, ?, ?, ?> core = (IWarehouseCoreTable<?, ?, ?, ?>) sys;
                                                    return core.createDefaultSecurity(session, (ISystems<?, ?>) sys,
                                                            (IEnterprise<?, ?>) ent, (IActiveFlag<?, ?>) af, tokens);
                                                }))))
        ).await().atMost(Duration.ofMinutes(2));

        assertEquals(7L, inserted,
                "Fully stateless createDefaultSecurity must insert the seven canonical grant rows on one stateless unit");
    }

    /**
     * End-to-end stateless classification <strong>write</strong>: {@link IClassificationService#create(
     * Mutiny.StatelessSession, String, String, ISystems, UUID...)} inserts the lean classification row and
     * provisions its default security — entirely on a {@link Mutiny.StatelessSession}, composing the prepped
     * data-concept/active-flag references + the stateless security path. Must assign an id, be idempotent
     * (second call returns the same id, no duplicate), and be findable afterwards via the prepped read.
     */
    @Test
    @Order(12)
    public void createClassification_statelessEndToEnd_persistsAndIsIdempotent() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);

        final String name = "StatelessCls_" + Long.toHexString(System.nanoTime());

        IClassification<?, ?> created = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.create(session, name, "stateless created classification", (ISystems<?, ?>) sys))
        ).await().atMost(Duration.ofMinutes(2));

        assertNotNull(created, "Stateless create must return the classification");
        assertNotNull(created.getId(), "Stateless-created classification must have an id assigned on insert");
        assertEquals(name, created.getName(), "Stateless-created classification name must match");

        // Idempotent: a second create returns the already-existing row (same id), never a duplicate.
        UUID secondId = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.create(session, name, "stateless created classification", (ISystems<?, ?>) sys))
                        .map(c -> (UUID) c.getId())
        ).await().atMost(Duration.ofMinutes(1));

        assertEquals(created.getId(), secondId,
                "Stateless create must be idempotent — second call returns the same id, not a duplicate");

        // Findable afterwards via the prepped stateless read.
        UUID foundId = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.find(session, name, (ISystems<?, ?>) sys))
                        .map(c -> (UUID) c.getId())
        ).await().atMost(Duration.ofMinutes(1));

        assertEquals(created.getId(), foundId,
                "Stateless-created classification must be findable with the same id via the prepped read");
    }

    /**
     * Whole-system slice: run {@link RulesSystem#createDefaults(Mutiny.StatelessSession, IEnterprise)}
     * entirely on a {@link Mutiny.StatelessSession}. This exercises the full per-system createDefaults
     * orchestration statelessly — prepped {@code findSystem}, the new stateless {@code getSystemToken}
     * (a scalar projection of the {@code SystemIdentity} relationship-classification value), and the
     * stateless {@code IClassificationService.create} — and must leave the {@code Rules} and
     * {@code RulesType} classifications resolvable (idempotent: they may already exist from install).
     */
    @Test
    @Order(13)
    public void rulesSystemCreateDefaults_stateless_runsEndToEnd() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
        RulesSystem rules = IGuiceContext.get(RulesSystem.class);

        // Run the system's defaults end-to-end on a single stateless transaction.
        sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> rules.createDefaults(session, (IEnterprise<?, ?>) ent)
                                .replaceWith(Uni.createFrom().voidItem()))
        ).await().atMost(Duration.ofMinutes(2));

        // Verify both rule classifications resolve afterwards via the prepped stateless read.
        Object[] ids = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.find(session, "Rules", (ISystems<?, ?>) sys)
                                .chain(rulesCl -> cs.find(session, "RulesType", (ISystems<?, ?>) sys)
                                        .map(rulesType -> new Object[]{rulesCl.getId(), rulesType.getId()})))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(ids[0], "Rules classification must resolve after stateless createDefaults");
        assertNotNull(ids[1], "RulesType classification must resolve after stateless createDefaults");
    }

    /**
     * Stateless hierarchy write: the parent-aware {@link IClassificationService#create(Mutiny.StatelessSession,
     * String, String, ISystems, IClassification, UUID...)} creates a fresh parent + child and links them via
     * the stateless {@code addChild} (a {@code SystemsXClassification}-style hierarchy link insert) — entirely
     * on a {@link Mutiny.StatelessSession}. Fresh names guarantee the actual insert + link path runs.
     */
    @Test
    @Order(14)
    public void createHierarchy_stateless_linksFreshChildToParent() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);

        final String hex = Long.toHexString(System.nanoTime());
        final String parentName = "SlParent_" + hex;
        final String childName = "SlChild_" + hex;

        UUID[] ids = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.create(session, parentName, parentName, (ISystems<?, ?>) sys)
                                .chain(parent -> cs.create(session, childName, childName, (ISystems<?, ?>) sys, parent)
                                        .map(child -> new UUID[]{(UUID) parent.getId(), (UUID) child.getId()})))
        ).await().atMost(Duration.ofMinutes(2));

        assertNotNull(ids[0], "Fresh parent classification must be created");
        assertNotNull(ids[1], "Fresh child classification must be created");
        assertNotEquals(ids[0], ids[1], "Parent and child must be distinct classifications");
    }

    /**
     * Whole-system slice with hierarchy: {@link ProductsSystem#createDefaults(Mutiny.StatelessSession,
     * IEnterprise)} provisions the Products → ProductGroup → {ProductTypeName, ProductPremiumType,
     * ProductBaseCost} hierarchy entirely on a stateless session (enum + parent stateless creates). Must
     * complete and leave the classifications resolvable (idempotent: they may already exist from install).
     */
    @Test
    @Order(15)
    public void productsSystemCreateDefaults_stateless_runsEndToEnd() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
        ProductsSystem products = IGuiceContext.get(ProductsSystem.class);

        sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> products.createDefaults(session, (IEnterprise<?, ?>) ent)
                                .replaceWith(Uni.createFrom().voidItem()))
        ).await().atMost(Duration.ofMinutes(2));

        Object[] ids = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.find(session, "Products", (ISystems<?, ?>) sys)
                                .chain(p -> cs.find(session, "ProductGroup", (ISystems<?, ?>) sys)
                                        .chain(pg -> cs.find(session, "ProductBaseCost", (ISystems<?, ?>) sys)
                                                .map(pbc -> new Object[]{p.getId(), pg.getId(), pbc.getId()}))))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(ids[0], "Products classification must resolve after stateless createDefaults");
        assertNotNull(ids[1], "ProductGroup classification must resolve after stateless createDefaults");
        assertNotNull(ids[2], "ProductBaseCost classification must resolve after stateless createDefaults");
    }

    /**
     * Foundational system: {@link ClassificationsSystem#createDefaults(Mutiny.StatelessSession, IEnterprise)}
     * provisions the core classification set (hierarchy type, NoClassification, Security, SystemIdentity, …)
     * entirely on a stateless session using the concept-/parent-aware stateless creates. Must complete and
     * leave the Security and SystemIdentity classifications resolvable (idempotent with install).
     */
    @Test
    @Order(16)
    public void classificationsSystemCreateDefaults_stateless_runsEndToEnd() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
        ClassificationsSystem classifications = IGuiceContext.get(ClassificationsSystem.class);

        sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> classifications.createDefaults(session, (IEnterprise<?, ?>) ent)
                                .replaceWith(Uni.createFrom().voidItem()))
        ).await().atMost(Duration.ofMinutes(2));

        Object[] ids = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.find(session, DefaultClassifications.Security.toString(), (ISystems<?, ?>) sys)
                                .chain(sec -> cs.find(session, SystemsClassifications.SystemIdentity.toString(), (ISystems<?, ?>) sys)
                                        .map(ident -> new Object[]{sec.getId(), ident.getId()})))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(ids[0], "Security classification must resolve after stateless createDefaults");
        assertNotNull(ids[1], "SystemIdentity classification must resolve after stateless createDefaults");
    }

    /**
     * Entity-create system: {@link InvolvedPartySystem#createDefaults(Mutiny.StatelessSession, IEnterprise)}
     * provisions the 15 identification types, 12 name types and 7 involved-party types entirely on a
     * stateless session (each via the prepped find-or-create + stateless default-security path). Must
     * complete and leave a representative type / name-type resolvable (idempotent re-create returns the
     * existing row with an id).
     */
    @Test
    @Order(17)
    public void involvedPartySystemCreateDefaults_stateless_runsEndToEnd() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IInvolvedPartyService<?> ips = IGuiceContext.get(IInvolvedPartyService.class);
        InvolvedPartySystem ipSystem = IGuiceContext.get(InvolvedPartySystem.class);

        sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ipSystem.createDefaults(session, (IEnterprise<?, ?>) ent)
                                .replaceWith(Uni.createFrom().voidItem()))
        ).await().atMost(Duration.ofMinutes(3));

        Object[] ids = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> ips.createType(session, (ISystems<?, ?>) sys, IPTypes.TypeIndividual, "x")
                                .chain(t -> ips.createNameType(session, NameTypes.FirstNameType, "x", (ISystems<?, ?>) sys)
                                        .map(n -> new Object[]{t.getId(), n.getId()})))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(ids[0], "InvolvedParty Type must resolve after stateless createDefaults");
        assertNotNull(ids[1], "InvolvedParty NameType must resolve after stateless createDefaults");
    }

    /**
     * Mixed system: {@link EventsSystem#createDefaults(Mutiny.StatelessSession, IEnterprise)} provisions the
     * LogItemTypes / EventStatus concept classifications, the per-value LogItemTypes children, and the
     * LogItem <em>resource-item type</em> — entirely on a stateless session (exercising the stateless
     * concept create, the enum+String-parent create, and the stateless resource-item-type create).
     */
    @Test
    @Order(18)
    public void eventsSystemCreateDefaults_stateless_runsEndToEnd() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
        IResourceItemService<?> ris = IGuiceContext.get(IResourceItemService.class);
        EventsSystem events = IGuiceContext.get(EventsSystem.class);

        sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> events.createDefaults(session, (IEnterprise<?, ?>) ent)
                                .replaceWith(Uni.createFrom().voidItem()))
        ).await().atMost(Duration.ofMinutes(3));

        Object[] ids = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> cs.find(session, "LogItemTypes", (ISystems<?, ?>) sys)
                                .chain(lit -> cs.find(session, "EventStatus", (ISystems<?, ?>) sys)
                                        .chain(esCl -> ris.createType(session, "LogItem", "An attached log item", (ISystems<?, ?>) sys)
                                                .map(rt -> new Object[]{lit.getId(), esCl.getId(), rt.getId()}))))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(ids[0], "LogItemTypes classification must resolve after stateless createDefaults");
        assertNotNull(ids[1], "EventStatus classification must resolve after stateless createDefaults");
        assertNotNull(ids[2], "LogItem resource-item type must resolve after stateless createDefaults");
    }

    /**
     * Infrastructure system: {@link SystemsSystem#createDefaults(Mutiny.StatelessSession, IEnterprise)}
     * find-or-creates the Enterprise / Active Flag / Activity Master {@code Systems} rows on a stateless
     * session. All three must resolve afterwards (idempotent with install).
     */
    @Test
    @Order(19)
    public void systemsSystemCreateDefaults_stateless_runsEndToEnd() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        SystemsSystem sys = IGuiceContext.get(SystemsSystem.class);

        sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> sys.createDefaults(session, (IEnterprise<?, ?>) ent)
                                .replaceWith(Uni.createFrom().voidItem()))
        ).await().atMost(Duration.ofMinutes(2));

        UUID amId = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.findSystemId(session, (IEnterprise<?, ?>) ent, ISystemsService.ActivityMasterSystemName))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(amId, "Activity Master system must resolve after stateless SystemsSystem.createDefaults");
    }

    /**
     * Infrastructure system: {@link ActiveFlagSystem#createDefaults(Mutiny.StatelessSession, IEnterprise)}
     * creates every active-flag reference row statelessly. The 'Active' flag must resolve afterwards.
     */
    @Test
    @Order(20)
    public void activeFlagSystemCreateDefaults_stateless_runsEndToEnd() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);
        ActiveFlagSystem afSystem = IGuiceContext.get(ActiveFlagSystem.class);

        sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> afSystem.createDefaults(session, (IEnterprise<?, ?>) ent)
                                .replaceWith(Uni.createFrom().voidItem()))
        ).await().atMost(Duration.ofMinutes(2));

        IActiveFlag<?, ?> active = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> afs.getActiveFlag(session, (IEnterprise<?, ?>) ent))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(active, "Active flag must resolve after stateless ActiveFlagSystem.createDefaults");
        assertNotNull(active.getId(), "Resolved Active flag must have an id");
    }

    /**
     * Infrastructure system: {@link ClassificationsDataConceptSystem#createDefaults(Mutiny.StatelessSession,
     * IEnterprise)} provisions every {@code EnterpriseClassificationDataConcepts} value as a data concept on a
     * stateless session. A representative concept must resolve afterwards.
     */
    @Test
    @Order(21)
    public void dataConceptSystemCreateDefaults_stateless_runsEndToEnd() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        ClassificationsDataConceptService dcs = IGuiceContext.get(ClassificationsDataConceptService.class);
        ClassificationsDataConceptSystem dcSystem = IGuiceContext.get(ClassificationsDataConceptSystem.class);

        sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> dcSystem.createDefaults(session, (IEnterprise<?, ?>) ent)
                                .replaceWith(Uni.createFrom().voidItem()))
        ).await().atMost(Duration.ofMinutes(3));

        Object concept = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> dcs.find(session,
                                EnterpriseClassificationDataConcepts.NoClassificationDataConceptName.classificationValue(),
                                (ISystems<?, ?>) sys))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(concept, "A data concept must resolve after stateless ClassificationsDataConceptSystem.createDefaults");
    }

    /**
     * Infrastructure system (bridge): {@link SecurityTokenSystem#createDefaults(Mutiny.StatelessSession,
     * IEnterprise)} is the security bootstrap and genuinely requires a managed context (it reads ~30
     * {@code @Cacheable}+eager tables and builds the token hierarchy), so its stateless overload bridges to
     * the proven stateful bootstrap. Invoked top-level via {@code openStatelessSession()} (not nested in a
     * transaction); afterwards the canonical Administrators folder token must resolve.
     */
    @Test
    @Order(22)
    public void securityTokenSystemCreateDefaults_statelessBridge_completes() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        ISecurityTokenService<?> sts = IGuiceContext.get(ISecurityTokenService.class);
        SecurityTokenSystem secSystem = IGuiceContext.get(SecurityTokenSystem.class);

        sessionFactory.openStatelessSession()
                .chain(stateless -> es.getEnterprise(stateless, TestEnterprise.name())
                        .chain(ent -> secSystem.createDefaults(stateless, (IEnterprise<?, ?>) ent))
                        .eventually(stateless::close))
                .await().atMost(Duration.ofMinutes(3));

        ISecurityToken<?, ?> admin = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> sts.getAdministratorsFolder(session, (ISystems<?, ?>) sys))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(admin, "Administrators folder token must resolve after stateless-bridge SecurityTokenSystem.createDefaults");
    }

    /**
     * Stateless relationship-classification write: tag the Activity Master {@code Systems} row with a freshly
     * created classification using {@link com.guicedee.activitymaster.fsdm.client.services.capabilities.IManageClassifications#addOrReuseClassification(Mutiny.StatelessSession, String, String, ISystems, UUID...)}
     * — entirely on a {@link Mutiny.StatelessSession}. The link row is inserted with {@code session.insert}, its
     * default security provisioned via the stateless path, and the operation is find-or-insert (idempotent: a
     * second tag must not create a duplicate link). This is the primitive the Security Token bootstrap uses for
     * {@code enterprise.addOrUpdateClassification(EnterpriseIdentity, …)} and {@code system.addOrReuseClassification(SystemIdentity, …)}.
     */
    @Test
    @Order(23)
    public void addOrReuseClassification_stateless_tagsSystemIdempotently() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);

        final String clsName = "SlTag_" + Long.toHexString(System.nanoTime());

        // Create the classification + tag the Activity Master system with it, twice (idempotency), each on a
        // single stateless unit of work.
        for (int i = 0; i < 2; i++) {
            sessionFactory.withStatelessTransaction(session ->
                    es.getEnterprise(session, TestEnterprise.name())
                            .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                            .chain(sys -> cs.create(session, clsName, clsName, (ISystems<?, ?>) sys)
                                    .chain(cl -> sys.addOrReuseClassification(session, clsName, sys.getId().toString(), (ISystems<?, ?>) sys)))
            ).await().atMost(Duration.ofMinutes(2));
        }

        // Exactly one active link must exist (find-or-insert is idempotent) — counted on a stateless session
        // via the stateless numberOfClassifications mixin (scalar getCount, no canRead gating).
        Long count = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> sys.numberOfClassifications(session, clsName, sys.getId().toString(), (ISystems<?, ?>) sys))
        ).await().atMost(Duration.ofMinutes(1));

        assertEquals(1L, count,
                "Stateless addOrReuseClassification must insert exactly one link and be idempotent (no duplicate on re-tag)");
    }
}





