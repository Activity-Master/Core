package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that the canonical security tokens issued to a <strong>system</strong> are valid and
 * actually translate into row-level access on records that carry default security.
 *
 * <p>The Activity Master system's identity token (resolved via
 * {@link ISystemsService#getSecurityIdentityToken}) is a <em>child</em> of the {@code Systems} folder in
 * the security hierarchy. Expanding it with
 * {@link ISecurityTokenService#getApplicableSecurityTokenIds} therefore yields the system token plus its
 * parent {@code Systems} folder (and the enterprise root). The {@code Systems} folder is granted
 * create/update/read on every default-secured record, so the system can both <strong>read</strong>
 * ({@link IWarehouseCoreTable#canRead}) and <strong>write</strong>
 * ({@link IWarehouseCoreTable#canWrite}) those records.</p>
 *
 * <ul>
 *     <li>the system identity token resolves and is non-null (the token is valid);</li>
 *     <li>its applicable-token expansion includes the {@code Systems} folder token;</li>
 *     <li>a default-secured record is readable AND writable by the system token;</li>
 *     <li>an unsecured record is neither readable nor writable;</li>
 *     <li>an empty identity grants no access.</li>
 * </ul>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterSecurityAccess {

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

        // Provision the enterprise on the stateless pipeline (no bridge). createNewEnterprise creates the
        // record + installs/registers every system via the stateless registerSystem path (so stateless-only
        // systems like TimeSystem work); startNewEnterprise then seeds the canonical security defaults + admin.
        sessionFactory.openStatelessSession()
                .chain(ss -> enterpriseService.getEnterprise(ss, TestEnterprise.name())
                        .onFailure().recoverWithUni(t -> {
                            var ent = enterpriseService.get();
                            ent.setName(TestEnterprise.name());
                            ent.setDescription("Enterprise for Security Access Testing");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), "admin", "!@adminadmin"))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    /** Mutable holder for the resolved access-test context. */
    private static final class Ctx {
        IEnterprise<?, ?> enterprise;
        ISystems<?, ?> system;
        IActiveFlag<?, ?> activeFlag;
        UUID systemToken;
        ISecurityToken<?, ?> systemsFolder;
        IClassification<?, ?> securedRecord;
        IClassification<?, ?> unsecuredRecord;
        final Map<String, ISecurityToken<?, ?>> tokens = new HashMap<>();
    }

    private Ctx provision() {
        final String runId = Long.toHexString(System.nanoTime());
        final Ctx ctx = new Ctx();

        // Phase A — resolve context + the system identity token, create two records (one to secure).
        sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
            IClassificationDataConceptService<?> dcs = IGuiceContext.get(IClassificationDataConceptService.class);
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
                        return ss.getSecurityIdentityToken(session, ctx.system);
                    })
                    .chain(systemToken -> {
                        ctx.systemToken = systemToken;
                        return sec.getSystemsFolder(session, ctx.system).invoke(t -> ctx.systemsFolder = t);
                    })
                    // Resolve the default data concept once; both raw records below reference it by FK.
                    .chain(() -> dcs.find(session, EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, ctx.system))
                    .chain(dataConcept -> {
                        // Create BOTH records RAW — a direct persist that deliberately SKIPS the per-row
                        // createDefaultSecurity that ClassificationService.create() would run. The 'secured'
                        // record is then secured by the stateless batch in Phase B; the 'unsecured' record is
                        // never secured. This makes the no-access assertions deterministic instead of relying on
                        // the per-row path silently being a no-op.
                        final ClassificationDataConcept concept = (ClassificationDataConcept) dataConcept;
                        Classification secured = newRawClassification("SecAccessSecured_" + runId, "secured access record", concept, ctx);
                        Classification unsecured = newRawClassification("SecAccessUnsecured_" + runId, "unsecured access record", concept, ctx);
                        return secured.builder(session).persist(secured)
                                .invoke(c -> ctx.securedRecord = c)
                                .chain(() -> unsecured.builder(session).persist(unsecured)
                                        .invoke(c -> ctx.unsecuredRecord = c))
                                .replaceWithVoid();
                    })
                    .chain(() -> sec.getAdministratorsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_ADMINISTRATORS, t)))
                    .chain(() -> sec.getEveryoneGroup(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_EVERYONE, t)))
                    .chain(() -> sec.getEverywhereGroup(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_EVERYWHERE, t)))
                    .chain(() -> sec.getSystemsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_SYSTEMS, t)))
                    .chain(() -> sec.getApplicationsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_APPLICATIONS, t)))
                    .chain(() -> sec.getPluginsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_PLUGINS, t)))
                    .chain(() -> sec.getGuestsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_GUESTS, t)))
                    .replaceWithVoid();
        }).await().atMost(Duration.ofMinutes(2));

        // Phase B — secure only the first record (stateless batch). The other stays unsecured.
        Long inserted = sessionFactory.withStatelessTransaction(session ->
                ((IWarehouseCoreTable<?, ?, ?, ?>) ctx.securedRecord)
                        .createDefaultSecurity(session, ctx.system, ctx.enterprise, ctx.activeFlag, ctx.tokens)
        ).await().atMost(Duration.ofMinutes(1));
        assertNotNull(inserted);
        assertEquals((long) SECURITY_ROWS_PER_RECORD, inserted,
                "Secured record should have exactly its default security written by the batch pass");

        return ctx;
    }

    /**
     * Builds (but does not persist) a raw {@link Classification} with all required FKs set, deliberately
     * <strong>without</strong> running the per-row {@code createDefaultSecurity}. This mirrors how a bulk
     * loader produces rows, leaving security entirely to the explicit stateless batch pass under test.
     */
    private static Classification newRawClassification(String name, String description,
                                                       ClassificationDataConcept concept, Ctx ctx) {
        Classification record = new Classification();
        record.setName(name);
        record.setDescription(description);
        record.setClassificationSequenceNumber(1);
        record.setSystemID(ctx.system);
        record.setOriginalSourceSystemID(ctx.system.getId());
        record.setOriginalSourceSystemUniqueID(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        record.setEnterpriseID(ctx.enterprise);
        record.setConcept(concept);
        record.setActiveFlagID(ctx.activeFlag);
        return record;
    }

    @Test
    @Order(1)
    public void testSystemIdentityTokenIsValidAndExpands() {
        Ctx ctx = provision();
        assertNotNull(ctx.systemToken, "The system identity token must resolve (be valid)");
        assertNotNull(ctx.systemsFolder, "The Systems folder token must resolve");

        // The system token must expand (transitively) to include its parent Systems folder.
        var applicable = sessionFactory.withTransaction(session -> {
            ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
            return sec.getApplicableSecurityTokenIds(session, ctx.system, ctx.systemToken);
        }).await().atMost(Duration.ofMinutes(1));

        assertFalse(applicable.isEmpty(), "A valid system token must expand to a non-empty applicable set");
        assertTrue(applicable.contains(((ISecurityToken<?, ?>) ctx.systemsFolder).getId()),
                "The system token's applicable set must include its parent 'Systems' folder token");
    }

    @Test
    @Order(2)
    public void testSystemTokenGrantsReadAccess() {
        Ctx ctx = provision();
        Boolean canRead = sessionFactory.withTransaction(session ->
                ((IWarehouseCoreTable<?, ?, ?, ?>) ctx.securedRecord).canRead(session, ctx.system, ctx.systemToken)
        ).await().atMost(Duration.ofMinutes(1));
        assertTrue(canRead, "A default-secured record must be READABLE by the system identity token");
    }

    @Test
    @Order(3)
    public void testSystemTokenGrantsWriteAccess() {
        Ctx ctx = provision();
        Boolean canWrite = sessionFactory.withTransaction(session ->
                ((IWarehouseCoreTable<?, ?, ?, ?>) ctx.securedRecord).canWrite(session, ctx.system, ctx.systemToken)
        ).await().atMost(Duration.ofMinutes(1));
        assertTrue(canWrite, "A default-secured record must be WRITABLE (create/update) by the system identity token");
    }

    @Test
    @Order(4)
    public void testUnsecuredRecordGrantsNoAccess() {
        Ctx ctx = provision();
        boolean[] access = sessionFactory.withTransaction(session -> {
            IWarehouseCoreTable<?, ?, ?, ?> rec = (IWarehouseCoreTable<?, ?, ?, ?>) ctx.unsecuredRecord;
            return rec.canRead(session, ctx.system, ctx.systemToken)
                    .chain(r -> rec.canWrite(session, ctx.system, ctx.systemToken)
                            .map(w -> new boolean[]{r, w}));
        }).await().atMost(Duration.ofMinutes(1));
        assertFalse(access[0], "A record with no security rows must NOT be readable");
        assertFalse(access[1], "A record with no security rows must NOT be writable");
    }

    @Test
    @Order(5)
    public void testEmptyIdentityGrantsNoAccess() {
        Ctx ctx = provision();
        boolean[] access = sessionFactory.withTransaction(session -> {
            IWarehouseCoreTable<?, ?, ?, ?> rec = (IWarehouseCoreTable<?, ?, ?, ?>) ctx.securedRecord;
            return rec.canRead(session, ctx.system)         // no identity token
                    .chain(r -> rec.canWrite(session, ctx.system)
                            .map(w -> new boolean[]{r, w}));
        }).await().atMost(Duration.ofMinutes(1));
        assertFalse(access[0], "An empty identity must not grant read access");
        assertFalse(access[1], "An empty identity must not grant write access");
    }

    /**
     * The canonical <strong>Guests</strong> folder token grants <em>read-only</em> access in the default
     * security policy. Seeding {@link ISecurityTokenService#getApplicableSecurityTokenIds} from the Guests
     * token (and walking up to {@code Everyone}/enterprise-root) therefore yields read but never write.
     */
    @Test
    @Order(6)
    public void testGuestTokenIsReadOnly() {
        Ctx ctx = provision();
        // The Guests folder token, used as an identity, seeds from its SecurityToken (varchar UUID) value.
        UUID guestIdentity = UUID.fromString(ctx.tokens.get(IWarehouseCoreTable.SECURITY_GUESTS).getSecurityToken());

        boolean[] access = sessionFactory.withTransaction(session -> {
            IWarehouseCoreTable<?, ?, ?, ?> rec = (IWarehouseCoreTable<?, ?, ?, ?>) ctx.securedRecord;
            return rec.canRead(session, ctx.system, guestIdentity)
                    .chain(r -> rec.canWrite(session, ctx.system, guestIdentity)
                            .map(w -> new boolean[]{r, w}));
        }).await().atMost(Duration.ofMinutes(1));

        assertTrue(access[0], "A Guests-folder token must be able to READ a default-secured record");
        assertFalse(access[1], "A Guests-folder token must NOT be able to WRITE a default-secured record (read-only)");
    }

    /**
     * End-to-end query-level security trim: resolving the readable ids for the system token and applying
     * them with {@link com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderDefault#canRead(java.util.Collection)}
     * must return the secured (readable) record and exclude the unsecured (unreadable) one.
     */
    @Test
    @Order(7)
    public void testQueryLevelReadTrimExcludesUnreadable() {
        Ctx ctx = provision();
        UUID securedId = (UUID) ctx.securedRecord.getId();
        UUID unsecuredId = (UUID) ctx.unsecuredRecord.getId();

        java.util.Set<UUID> visible = sessionFactory.withTransaction(session -> {
            IWarehouseCoreTable<?, ?, ?, ?> rec = (IWarehouseCoreTable<?, ?, ?, ?>) ctx.securedRecord;
            return rec.readableIds(session, ctx.system, ctx.systemToken)
                    .chain(readable -> new Classification().builder(session)
                            // bound the query to just our two records, then apply the security trim
                            .where("id", com.entityassist.enumerations.Operand.InList,
                                    java.util.List.of(securedId, unsecuredId))
                            .canRead(readable)
                            .getAll()
                            .map(rows -> {
                                java.util.Set<UUID> ids = new java.util.LinkedHashSet<>();
                                for (Object o : rows) {
                                    ids.add((UUID) ((Classification) o).getId());
                                }
                                return ids;
                            }));
        }).await().atMost(Duration.ofMinutes(1));

        assertTrue(visible.contains(securedId),
                "Security-trimmed list query must include the readable (secured) record");
        assertFalse(visible.contains(unsecuredId),
                "Security-trimmed list query must exclude the unreadable (unsecured) record");
    }
}



