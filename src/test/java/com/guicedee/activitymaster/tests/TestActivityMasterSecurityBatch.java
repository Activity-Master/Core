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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Exercises the batched, <strong>stateless-session</strong> default-security creation path
 * ({@link IWarehouseCoreTable#createDefaultSecurity(Mutiny.StatelessSession, ISystems, IEnterprise, IActiveFlag, Map, java.util.UUID...)}).
 *
 * <p>When records are created during an install, each one needs a fan-out of default security rows
 * (one per canonical group/folder). At scale that is an <em>exhaustive</em> number of inserts, so a
 * normal stateful session is a poor fit — its first-level cache and dirty-checking grow with every
 * row. This test demonstrates the recommended approach:</p>
 * <ol>
 *     <li>resolve the shared security context (system, enterprise, active flag, group/folder tokens)
 *         <em>once</em> on a normal session;</li>
 *     <li>create the owning records <strong>raw</strong> (a direct persist, <em>without</em> the per-row
 *         {@code createDefaultSecurity} that {@code ClassificationService.create()} runs) so they enter
 *         this test unsecured — exactly how a bulk loader produces rows;</li>
 *     <li>write all of their default-security rows inside a single {@code withStatelessTransaction}
 *         block, so the persistence context never grows and the inserts can be JDBC-batched;</li>
 *     <li>assert every record ended up with the expected number of security rows.</li>
 * </ol>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterSecurityBatch {

    /** Number of canonical group/folder security rows created per record. */
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
        // record + installs/registers every system via the stateless registerSystem path; startNewEnterprise
        // then seeds the canonical security groups/folders + admin. Idempotent: create only when absent.
        sessionFactory.openStatelessSession()
                .chain(ss -> enterpriseService.getEnterprise(ss, TestEnterprise.name())
                        .onFailure().recoverWithUni(t -> {
                            var ent = enterpriseService.get();
                            ent.setName(TestEnterprise.name());
                            ent.setDescription("Enterprise for Security Batch Testing");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), "admin", "!@adminadmin"))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    /** Mutable holder for the shared install/security context resolved on a normal session. */
    private static final class Ctx {
        IEnterprise<?, ?> enterprise;
        ISystems<?, ?> system;
        IActiveFlag<?, ?> activeFlag;
        final List<IClassification<?, ?>> records = new ArrayList<>();
        final Map<String, ISecurityToken<?, ?>> tokens = new HashMap<>();
    }

    @Test
    @Order(1)
    public void testBatchStatelessDefaultSecurityCreation() {
        final int recordCount = 40;
        final String runId = Long.toHexString(System.nanoTime());
        final Ctx ctx = new Ctx();

        // ── Phase A: resolve the shared security context + create the owning records (stateful) ──
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
                        // Resolve the default data concept once; raw records below reference it by FK.
                        return dcs.find(session, EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, ctx.system);
                    })
                    .chain(dataConcept -> {
                        // Create the owning records RAW — a direct persist that deliberately SKIPS the per-row
                        // createDefaultSecurity that ClassificationService.create() would run. This mirrors a bulk
                        // loader (rows in, no per-row security), leaving Phase B's stateless batch as the SOLE
                        // securer so the 7-rows-per-record assertions are deterministic.
                        final ClassificationDataConcept concept = (ClassificationDataConcept) dataConcept;
                        Uni<Void> chain = Uni.createFrom().voidItem();
                        for (int i = 0; i < recordCount; i++) {
                            final int idx = i;
                            chain = chain.chain(() -> {
                                Classification record = new Classification();
                                record.setName("BatchSec_" + runId + "_" + idx);
                                record.setDescription("batch security test record");
                                record.setClassificationSequenceNumber(1);
                                record.setSystemID(ctx.system);
                                record.setOriginalSourceSystemID(ctx.system.getId());
                                record.setOriginalSourceSystemUniqueID(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                                record.setEnterpriseID(ctx.enterprise);
                                record.setConcept(concept);
                                record.setActiveFlagID(ctx.activeFlag);
                                return record.builder(session).persist(record)
                                        .invoke(ctx.records::add)
                                        .replaceWithVoid();
                            });
                        }
                        return chain;
                    })
                    // Resolve the canonical group/folder tokens once and key them by the SECURITY_* constants.
                    .chain(() -> sec.getAdministratorsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_ADMINISTRATORS, t)))
                    .chain(() -> sec.getEveryoneGroup(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_EVERYONE, t)))
                    .chain(() -> sec.getEverywhereGroup(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_EVERYWHERE, t)))
                    .chain(() -> sec.getSystemsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_SYSTEMS, t)))
                    .chain(() -> sec.getApplicationsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_APPLICATIONS, t)))
                    .chain(() -> sec.getPluginsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_PLUGINS, t)))
                    .chain(() -> sec.getGuestsFolder(session, ctx.system).invoke(t -> ctx.tokens.put(IWarehouseCoreTable.SECURITY_GUESTS, t)))
                    .replaceWithVoid();
        }).await().atMost(Duration.ofMinutes(3));

        assertEquals(recordCount, ctx.records.size(), "All records should have been created");
        assertEquals(SECURITY_ROWS_PER_RECORD, ctx.tokens.size(), "All canonical group/folder tokens should resolve");

        // ── Phase B: write every record's default security in ONE stateless transaction (batched) ──
        long started = System.nanoTime();
        Long inserted = sessionFactory.withStatelessTransaction(session -> {
            Uni<Long> chain = Uni.createFrom().item(0L);
            for (IClassification<?, ?> record : ctx.records) {
                final IWarehouseCoreTable<?, ?, ?, ?> core = (IWarehouseCoreTable<?, ?, ?, ?>) record;
                chain = chain.chain(runningTotal -> core
                        .createDefaultSecurity(session, ctx.system, ctx.enterprise, ctx.activeFlag, ctx.tokens)
                        .map(perRecord -> runningTotal + perRecord));
            }
            return chain;
        }).await().atMost(Duration.ofMinutes(3));
        long elapsedMs = (System.nanoTime() - started) / 1_000_000;
        log.info("⚡ Batch+stateless inserted {} security rows across {} records in {} ms",
                inserted, recordCount, elapsedMs);

        assertEquals((long) recordCount * SECURITY_ROWS_PER_RECORD, inserted,
                "Each record should produce " + SECURITY_ROWS_PER_RECORD + " security rows");

        // ── Phase C: assert the rows were actually persisted for every record (stateful read) ──
        sessionFactory.withTransaction(session -> {
            Uni<Void> chain = Uni.createFrom().voidItem();
            for (IClassification<?, ?> record : ctx.records) {
                final IWarehouseCoreTable<?, ?, ?, ?> core = (IWarehouseCoreTable<?, ?, ?, ?>) record;
                chain = chain.chain(() -> core.countDefaultSecurity(session)
                        .invoke(count -> assertEquals((long) SECURITY_ROWS_PER_RECORD, count,
                                "Each record should have " + SECURITY_ROWS_PER_RECORD + " persisted security rows"))
                        .replaceWithVoid());
            }
            return chain;
        }).await().atMost(Duration.ofMinutes(3));
    }
}


