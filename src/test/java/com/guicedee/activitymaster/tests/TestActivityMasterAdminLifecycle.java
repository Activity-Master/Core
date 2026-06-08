package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IPasswordsService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Admin-identity lifecycle counterpart of {@link TestActivityMasterLifecycle}.
 *
 * <p>Where {@code TestActivityMasterLifecycle} exercises the join/remove/archive lifecycle without a
 * caller identity, this class performs the same kind of lifecycle operations <em>as the logged-in admin
 * user</em> and asserts that the <strong>admin identity security token</strong> drives row-level security
 * correctly: it can <strong>read</strong> and <strong>write</strong> the records and join rows it secures,
 * while an empty identity cannot.</p>
 *
 * <p>The admin user is seeded by {@link IEnterpriseService#startNewEnterprise} with an {@code Identity}
 * {@link SecurityToken} named after the username, created under the {@code Administrators} folder (full
 * CRUD on every default-secured row). Authenticating via
 * {@link IPasswordsService#findByUsernameAndPassword} and resolving that token yields the identity UUID
 * passed to {@link IWarehouseCoreTable#canRead}/{@link IWarehouseCoreTable#canWrite} and the
 * {@code IManageClassifications} capability methods.</p>
 *
 * <ul>
 *     <li><b>AdminIdentity</b> — the admin authenticates, resolves a non-null identity token, and that
 *         token expands (transitively) to include the {@code Administrators} folder.</li>
 *     <li><b>SecuredRecordReadWrite</b> — a default-secured record is readable AND writable by the admin
 *         identity, but neither by an empty identity.</li>
 *     <li><b>AdminClassificationLifecycle</b> — adding a classification join as admin secures the join row
 *         (admin can read/write it and count it), and removing/archiving it as admin retires it (count 0).</li>
 * </ul>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterAdminLifecycle {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "!@adminadmin";
    // Secure-by-default (post-install, security flag ENABLED) per-row matrix for a single-entity create with
    // no explicit scope token: Administrators=CRUD plus Systems/Applications/Plugins=create/update/read, and
    // NO Everyone/Everywhere/Guests grants (→ not world-readable). Records created in these admin-lifecycle
    // flows run with security enabled, so they carry the restricted matrix rather than the public 7-row one.
    private static final int RESTRICTED_SECURITY_ROWS_PER_RECORD = 4;

    protected Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup() {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        log.info("Loading DB Configuration / PersistService from Guice (Admin Lifecycle)");
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Pre-resolve all service singletons on the test main thread BEFORE entering any Vert.x callback,
        // so IGuiceContext.get() inside the async callbacks is CallScope-safe.
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
        IGuiceContext.get(IClassificationService.class);
        IGuiceContext.get(IActiveFlagService.class);
        IGuiceContext.get(ISecurityTokenService.class);
        IGuiceContext.get(IPasswordsService.class);

        // Ensure the enterprise + Activity Master system exist.
        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.getEnterprise(session, TestEnterprise.name())
                                .onFailure().recoverWithUni(t -> {
                                    var ent = enterpriseService.get();
                                    ent.setName(TestEnterprise.name());
                                    ent.setDescription("Enterprise for Admin Lifecycle Testing");
                                    return enterpriseService.createNewEnterprise(session, ent);
                                })
                                .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                        .onFailure().recoverWithUni(t -> systemsService.create(session, (IEnterprise<?, ?>) ent,
                                                ISystemsService.ActivityMasterSystemName, "Activity Master System")))
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));

        // Start the enterprise (idempotent) — seeds the canonical groups/folders AND the admin user/token.
        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.startNewEnterprise(session, TestEnterprise.name(), ADMIN_USER, ADMIN_PASSWORD)
                                .onFailure().recoverWithItem(e -> null)
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    // ──────────────────────────────────────────────────────────────────────────────────────────────
    // Shared reactive helpers (chain them; never block inside a session callback)
    // ──────────────────────────────────────────────────────────────────────────────────────────────

    /** Resolves the enterprise for the test. */
    private Uni<IEnterprise<?, ?>> enterprise(Mutiny.Session session) {
        return IGuiceContext.get(IEnterpriseService.class)
                .getEnterprise(session, TestEnterprise.name())
                .map(e -> (IEnterprise<?, ?>) e);
    }

    /** Resolves the Activity Master system for the given enterprise. */
    private Uni<ISystems<?, ?>> system(Mutiny.Session session, IEnterprise<?, ?> ent) {
        return IGuiceContext.get(ISystemsService.class)
                .getActivityMaster(session, ent)
                .map(s -> (ISystems<?, ?>) s);
    }

    /**
     * Logs in as the admin user and resolves the admin's <em>identity</em> security token (created under
     * the {@code Administrators} folder by the enterprise install) into the UUID identity consumed by the
     * access APIs.
     */
    private Uni<UUID> loginAndResolveAdminIdentity(Mutiny.Session session, IEnterprise<?, ?> ent, ISystems<?, ?> system) {
        IPasswordsService<?> passwords = IGuiceContext.get(IPasswordsService.class);
        return passwords.findByUsernameAndPassword(session, ADMIN_USER, ADMIN_PASSWORD, system, true)
                .invoke(party -> assertNotNull(party, "Admin must authenticate with the correct password"))
                .chain(party -> new SecurityToken().builder(session)
                        .withName(ADMIN_USER)
                        .withEnterprise(ent)
                        .inActiveRange()
                        .inDateRange()
                        .get())
                .map(tok -> UUID.fromString(((ISecurityToken<?, ?>) tok).getSecurityToken()));
    }

    // ──────────────────────────────────────────────────────────────────────────────────────────────
    // Part 1 — the admin authenticates and owns a valid, Administrators-scoped identity token
    // ──────────────────────────────────────────────────────────────────────────────────────────────
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class AdminIdentity {

        @Test
        @Order(1)
        public void testAdminAuthenticatesAndResolvesIdentityToken() {
            UUID adminIdentity = sessionFactory.withTransaction(session ->
                    enterprise(session).chain(ent -> system(session, ent)
                            .chain(sys -> loginAndResolveAdminIdentity(session, ent, sys)))
            ).await().atMost(Duration.ofMinutes(2));
            assertNotNull(adminIdentity, "Logging in as admin must resolve a non-null identity security token");
        }

        @Test
        @Order(2)
        public void testAdminIdentityExpandsToAdministratorsFolder() {
            boolean[] holder = new boolean[1];
            sessionFactory.withTransaction(session ->
                    enterprise(session).chain(ent -> system(session, ent).chain(sys -> {
                        ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                        return sec.getAdministratorsFolder(session, sys).chain(adminFolder ->
                                loginAndResolveAdminIdentity(session, ent, sys).chain(adminIdentity ->
                                        sec.getApplicableSecurityTokenIds(session, sys, adminIdentity)
                                                .invoke(applicable -> {
                                                    assertFalse(applicable.isEmpty(),
                                                            "The admin identity must expand to a non-empty applicable set");
                                                    holder[0] = applicable.contains(((ISecurityToken<?, ?>) adminFolder).getId());
                                                })));
                    }))
            ).await().atMost(Duration.ofMinutes(2));
            assertTrue(holder[0], "The admin identity's applicable set must include the 'Administrators' folder token");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────────────────────────
    // Part 2 — a default-secured record is read/write for the admin, no-access for an empty identity
    // ──────────────────────────────────────────────────────────────────────────────────────────────
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SecuredRecordReadWrite {

        @Test
        @Order(1)
        public void testAdminCanReadAndWriteSecuredRecordWhileEmptyIdentityCannot() {
            final String runId = Long.toHexString(System.nanoTime());
            boolean[] access = sessionFactory.withTransaction(session ->
                    enterprise(session).chain(ent -> system(session, ent).chain(sys -> {
                        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
                        // Creating the record secures it per-row (default security: Administrators full CRUD).
                        return cs.create(session, "AdminLc_Record_" + runId, "admin lifecycle secured record",
                                        EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys)
                                .chain(record -> {
                                    IWarehouseCoreTable<?, ?, ?, ?> rec = (IWarehouseCoreTable<?, ?, ?, ?>) record;
                                    return rec.countDefaultSecurity(session)
                                            .invoke(count -> assertEquals((long) RESTRICTED_SECURITY_ROWS_PER_RECORD, count,
                                                    "The created record must carry the secure-by-default (restricted) security matrix"))
                                            .chain(() -> loginAndResolveAdminIdentity(session, ent, sys))
                                            .chain(adminIdentity -> rec.canRead(session, sys, adminIdentity)
                                                    .chain(adminRead -> rec.canWrite(session, sys, adminIdentity)
                                                            .chain(adminWrite -> rec.canRead(session, sys) // empty identity
                                                                    .chain(anonRead -> rec.canWrite(session, sys)
                                                                            .map(anonWrite -> new boolean[]{adminRead, adminWrite, anonRead, anonWrite})))));
                                });
                    }))
            ).await().atMost(Duration.ofMinutes(2));

            assertTrue(access[0], "The admin identity must be able to READ a default-secured record");
            assertTrue(access[1], "The admin identity must be able to WRITE a default-secured record (Administrators grants CRUD)");
            assertFalse(access[2], "An empty identity must NOT be able to read a default-secured record");
            assertFalse(access[3], "An empty identity must NOT be able to write a default-secured record");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────────────────────────
    // Part 3 — classification join lifecycle performed AS the admin identity
    // ──────────────────────────────────────────────────────────────────────────────────────────────
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class AdminClassificationLifecycle {

        @Test
        @Order(1)
        public void testAdminAddSecuresAndReadsWritesEnterpriseClassificationJoin() {
            final String runId = Long.toHexString(System.nanoTime());
            final String name = "AdminLc_EntClassify_" + runId;
            final String value = "ADM-ENT-1";

            Object[] result = sessionFactory.withTransaction(session ->
                    enterprise(session).chain(ent -> system(session, ent).chain(sys -> {
                        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
                        return loginAndResolveAdminIdentity(session, ent, sys).chain(adminIdentity ->
                                cs.create(session, name, "admin enterprise classification", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys, adminIdentity)
                                        // Add the join AS the admin — the join row is secured per-row on create.
                                        .chain(created -> ent.addClassification(session, name, value, sys, adminIdentity))
                                        .chain(join -> {
                                            IWarehouseCoreTable<?, ?, ?, ?> link = (IWarehouseCoreTable<?, ?, ?, ?>) join;
                                            return link.countDefaultSecurity(session)
                                                    .chain(secCount -> link.canRead(session, sys, adminIdentity)
                                                            .chain(canRead -> link.canWrite(session, sys, adminIdentity)
                                                                    // The admin can also count the join through the security-trimmed read path.
                                                                    .chain(canWrite -> ent.numberOfClassifications(session, name, value, sys, adminIdentity)
                                                                            .map(visibleCount -> new Object[]{secCount, canRead, canWrite, visibleCount}))));
                                        }));
                    }))
            ).await().atMost(Duration.ofMinutes(2));

            assertEquals((long) RESTRICTED_SECURITY_ROWS_PER_RECORD, (long) (Long) result[0],
                    "Adding a classification as admin must secure the join row with the secure-by-default (restricted) matrix");
            assertTrue((Boolean) result[1], "The admin identity must be able to READ the secured join row");
            assertTrue((Boolean) result[2], "The admin identity must be able to WRITE the secured join row");
            assertEquals(1L, (long) (Long) result[3],
                    "The admin identity must see exactly one active classification join via the security-trimmed count");
        }

        @Test
        @Order(2)
        public void testAdminRemoveClassificationOnEnterprise() {
            final String runId = Long.toHexString(System.nanoTime());
            final String name = "AdminLc_EntRemove_" + runId;
            final String value = "ADM-ENT-REM-1";

            Long countAfterRemove = sessionFactory.withTransaction(session ->
                    enterprise(session).chain(ent -> system(session, ent).chain(sys -> {
                        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
                        return loginAndResolveAdminIdentity(session, ent, sys).chain(adminIdentity ->
                                cs.create(session, name, "admin enterprise remove classification", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys, adminIdentity)
                                        .chain(created -> ent.addClassification(session, name, value, sys, adminIdentity))
                                        .chain(join -> ent.removeClassification(session, name, value, sys, adminIdentity))
                                        .chain(ignored -> ent.numberOfClassifications(session, name, value, sys, adminIdentity)));
                    }))
            ).await().atMost(Duration.ofMinutes(2));

            assertEquals(0L, countAfterRemove,
                    "After removeClassification as admin, the admin must see 0 active enterprise joins for the value");
        }

        @Test
        @Order(3)
        public void testAdminArchiveClassificationOnActiveFlag() {
            final String runId = Long.toHexString(System.nanoTime());
            final String name = "AdminLc_ActiveArchive_" + runId;
            final String value = "ADM-AF-ARC-1";

            Long countAfterArchive = sessionFactory.withTransaction(session ->
                    enterprise(session).chain(ent -> system(session, ent).chain(sys -> {
                        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
                        IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);
                        return afs.getActiveFlag(session, ent).chain(activeObj -> {
                            IActiveFlag<?, ?> active = (IActiveFlag<?, ?>) activeObj;
                            return loginAndResolveAdminIdentity(session, ent, sys).chain(adminIdentity ->
                                    cs.create(session, name, "admin activeflag archive classification", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys, adminIdentity)
                                            .chain(created -> active.addClassification(session, name, value, sys, adminIdentity))
                                            .chain(join -> active.archiveClassification(session, name, value, sys, adminIdentity))
                                            .chain(ignored -> active.numberOfClassifications(session, name, value, sys, adminIdentity)));
                        });
                    }))
            ).await().atMost(Duration.ofMinutes(2));

            assertEquals(0L, countAfterArchive,
                    "After archiveClassification as admin, the admin must see 0 active ActiveFlag joins for the value");
        }
    }
}

