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
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Part 4 — <strong>Identity via the user security token</strong>.
 *
 * <p>Caller identity in Activity Master is carried by the authenticated user (in production a Vert.x 5
 * {@code io.vertx.ext.auth.User}); for headless/test flows the equivalent is a username/password login
 * via {@link IPasswordsService#findByUsernameAndPassword}. Once the enterprise install has created the
 * admin/creator user, this test <em>logs in as that admin</em> and then runs the row-level access
 * assertions <em>from the admin identity</em>.</p>
 *
 * <p>The admin user is minted with an {@code Identity} {@link com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken} named after the username,
 * created as a child of the {@code Administrators} folder. That folder is granted full CRUD on every
 * default-secured record, so the logged-in admin identity must be able to both
 * {@link IWarehouseCoreTable#canRead read} and {@link IWarehouseCoreTable#canWrite write} secured
 * records, and its {@link ISecurityTokenService#getApplicableSecurityTokenIds applicable-token
 * expansion} must include the {@code Administrators} folder token.</p>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterSecurityAdminLogin {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "!@adminadmin";
    private static final int SECURITY_ROWS_PER_RECORD = 7;

    protected Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup() {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Pre-resolve singletons on the test thread before entering any Vert.x callback (CallScope).
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
        IGuiceContext.get(IClassificationService.class);
        IGuiceContext.get(IActiveFlagService.class);
        IGuiceContext.get(ISecurityTokenService.class);
        IGuiceContext.get(IPasswordsService.class);

        // Provision the enterprise on the stateless pipeline (no bridge). createNewEnterprise creates the
        // record + installs/registers every system via the stateless registerSystem path; startNewEnterprise
        // then seeds the admin user + canonical groups/folders. Idempotent: create only when absent.
        sessionFactory.openStatelessSession()
                .chain(ss -> enterpriseService.getEnterprise(ss, TestEnterprise.name())
                        .onFailure().recoverWithUni(t -> {
                            var ent = enterpriseService.get();
                            ent.setName(TestEnterprise.name());
                            ent.setDescription("Enterprise for Admin Login Security Testing");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), ADMIN_USER, ADMIN_PASSWORD))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    /** Mutable holder for the resolved admin-login test context. */
    private static final class Ctx {
        IEnterprise<?, ?> enterprise;
        ISystems<?, ?> system;
        IActiveFlag<?, ?> activeFlag;
        ISecurityToken<?, ?> administratorsFolder;
        IClassification<?, ?> securedRecord;
        final Map<String, ISecurityToken<?, ?>> tokens = new HashMap<>();
    }

    private Ctx provision() {
        final String runId = Long.toHexString(System.nanoTime());
        final Ctx ctx = new Ctx();

        // Phase A — resolve context, create a record to secure, gather the canonical group/folder tokens.
        sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
            IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
            IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);
            ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);

            return es.getEnterprise(session, TestEnterprise.name())
                    .chain(ent -> {
                        ctx.enterprise = (IEnterprise<?, ?>) ent;
                        return ss.getActivityMaster(session, (IEnterprise<?, ?>) ent);
                    })
                    .chain(sys -> {
                        ctx.system = (ISystems<?, ?>) sys;
                        return afs.getActiveFlag(session, ctx.enterprise);
                    })
                    .chain(af -> {
                        ctx.activeFlag = (IActiveFlag<?, ?>) af;
                        return cs.create(session, "SecAdminSecured_" + runId, "secured admin-login record",
                                EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, ctx.system)
                                .invoke(c -> ctx.securedRecord = c);
                    })
                    .chain(() -> sec.getAdministratorsFolder(session, ctx.system).invoke(t -> {
                        ctx.administratorsFolder = t;
                        ctx.tokens.put(IWarehouseCoreTable.SECURITY_ADMINISTRATORS, t);
                    }))
                    .chain(() -> sec.getEveryoneGroup(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_EVERYONE, t)))
                    .chain(() -> sec.getEverywhereGroup(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_EVERYWHERE, t)))
                    .chain(() -> sec.getSystemsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_SYSTEMS, t)))
                    .chain(() -> sec.getApplicationsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_APPLICATIONS, t)))
                    .chain(() -> sec.getPluginsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_PLUGINS, t)))
                    .chain(() -> sec.getGuestsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_GUESTS, t)))
                    .replaceWithVoid();
        }).await().atMost(Duration.ofMinutes(2));

        // Phase B — secure the record (stateless batch).
        Long inserted = sessionFactory.withStatelessTransaction(session ->
                ((IWarehouseCoreTable<?, ?, ?, ?>) ctx.securedRecord)
                        .createDefaultSecurity(session, ctx.system, ctx.enterprise, ctx.activeFlag, ctx.tokens)
        ).await().atMost(Duration.ofMinutes(1));
        assertNotNull(inserted);
        assertTrue(inserted >= SECURITY_ROWS_PER_RECORD, "Secured record should have its default security written");

        return ctx;
    }

    /**
     * Logs in as the admin user and resolves the admin's <em>identity</em> security token (created under
     * the {@code Administrators} folder by the enterprise install) into the UUID identity used by the
     * access APIs. Reactive — chain it; do not block inside a session callback.
     */
    private Uni<UUID> loginAndResolveAdminIdentity(Mutiny.Session session, Ctx ctx) {
        IPasswordsService<?> passwords = IGuiceContext.get(IPasswordsService.class);
        return passwords
                .findByUsernameAndPassword(session, ADMIN_USER, ADMIN_PASSWORD, ctx.system, true)
                .invoke(party -> assertNotNull(party, "Admin must authenticate with the correct password"))
                .chain(party -> new SecurityToken().builder(session)
                        .withName(ADMIN_USER)
                        .withEnterprise(ctx.enterprise)
                        .inActiveRange()
                        .inDateRange()
                        .get())
                .map(tok -> UUID.fromString(((ISecurityToken<?, ?>) tok).getSecurityToken()));
    }

    @Test
    @Order(1)
    public void testAdminAuthenticatesAndHasIdentityToken() {
        Ctx ctx = provision();
        UUID adminIdentity = sessionFactory.withTransaction(session ->
                loginAndResolveAdminIdentity(session, ctx)
        ).await().atMost(Duration.ofMinutes(2));
        assertNotNull(adminIdentity, "Logging in as admin must resolve a non-null identity security token");
    }

    @Test
    @Order(2)
    public void testAdminIdentityExpandsToAdministratorsFolder() {
        Ctx ctx = provision();
        var applicable = sessionFactory.withTransaction(session ->
                loginAndResolveAdminIdentity(session, ctx)
                        .chain(adminIdentity -> {
                            ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                            return sec.getApplicableSecurityTokenIds(session, ctx.system, adminIdentity);
                        })
        ).await().atMost(Duration.ofMinutes(2));

        assertFalse(applicable.isEmpty(), "The admin identity must expand to a non-empty applicable set");
        assertTrue(applicable.contains(((ISecurityToken<?, ?>) ctx.administratorsFolder).getId()),
                "The admin identity's applicable set must include the 'Administrators' folder token");
    }

    @Test
    @Order(3)
    public void testAdminCanReadAndWriteAsLoggedInUser() {
        Ctx ctx = provision();
        boolean[] access = sessionFactory.withTransaction(session ->
                loginAndResolveAdminIdentity(session, ctx)
                        .chain(adminIdentity -> {
                            IWarehouseCoreTable<?, ?, ?, ?> rec = (IWarehouseCoreTable<?, ?, ?, ?>) ctx.securedRecord;
                            return rec.canRead(session, ctx.system, adminIdentity)
                                    .chain(r -> rec.canWrite(session, ctx.system, adminIdentity)
                                            .map(w -> new boolean[]{r, w}));
                        })
        ).await().atMost(Duration.ofMinutes(2));

        assertTrue(access[0], "The logged-in admin identity must be able to READ a default-secured record");
        assertTrue(access[1], "The logged-in admin identity must be able to WRITE (Administrators grants CRUD)");
    }
}


