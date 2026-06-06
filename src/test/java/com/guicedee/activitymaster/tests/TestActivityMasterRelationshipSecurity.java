package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IResourceItemService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the <strong>relationship-link default-security</strong> mechanism: when an entity links to a
 * secondary via one of the {@code IManageX} capability methods (e.g.
 * {@link com.guicedee.activitymaster.fsdm.client.services.capabilities.IManageClassifications#addClassification},
 * {@link com.guicedee.activitymaster.fsdm.client.services.capabilities.IManageResourceItems#addResourceItem}),
 * the produced join/link row must itself receive the canonical seven default-security rows and be readable
 * by the owning system identity token.
 *
 * <p>This is a regression guard for the previously-latent bug where the per-row
 * {@code link.createDefaultSecurity(...)} call inside those {@code addX} methods was issued as a bare
 * statement and its returned {@link Uni} was never subscribed — so a {@code Uni} that does nothing until
 * subscribed silently left the link <em>unsecured</em>. The {@code addX} methods now properly chain the
 * security creation, which these tests assert end-to-end.</p>
 *
 * <ul>
 *     <li>{@code addClassification} secures the {@code ClassificationXClassification} link (7 rows, readable);</li>
 *     <li>{@code addResourceItem} secures the {@code ClassificationXResourceItem} link (7 rows, readable) —
 *         one of the capability methods that was previously a no-op.</li>
 * </ul>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterRelationshipSecurity {

    /** Canonical number of default-security rows created per secured row (group/folder fan-out). */
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
        IGuiceContext.get(IResourceItemService.class);
        IGuiceContext.get(IActiveFlagService.class);
        IGuiceContext.get(ISecurityTokenService.class);

        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.getEnterprise(session, TestEnterprise.name())
                                .onFailure().recoverWithUni(t -> {
                                    var ent = enterpriseService.get();
                                    ent.setName(TestEnterprise.name());
                                    ent.setDescription("Enterprise for Relationship Security Testing");
                                    return enterpriseService.createNewEnterprise(session, ent);
                                })
                                .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                        .onFailure().recoverWithUni(t -> systemsService.create(session, (IEnterprise<?, ?>) ent,
                                                ISystemsService.ActivityMasterSystemName, "Activity Master System")))
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));

        // Start the enterprise (idempotent) — seeds the canonical security groups/folders + default security.
        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.startNewEnterprise(session, TestEnterprise.name(), "admin", "!@adminadmin")
                                .onFailure().recoverWithItem(e -> null)
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    /** Resolved shared context for a single test. */
    private static final class Ctx {
        IEnterprise<?, ?> enterprise;
        ISystems<?, ?> system;
        IActiveFlag<?, ?> activeFlag;
        UUID systemToken;
    }

    private Ctx provision() {
        final Ctx ctx = new Ctx();
        sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
            IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);
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
                    .invoke(token -> ctx.systemToken = token)
                    .replaceWithVoid();
        }).await().atMost(Duration.ofMinutes(2));
        assertNotNull(ctx.systemToken, "The system identity token must resolve");
        return ctx;
    }

    /** Asserts the supplied link row carries the canonical default security and is readable by the system token. */
    private void assertLinkSecured(Ctx ctx, IWarehouseCoreTable<?, ?, ?, ?> link, String what) {
        assertNotNull(link, what + " link must have been created");

        Long count = sessionFactory.withTransaction(link::countDefaultSecurity
        ).await().atMost(Duration.ofMinutes(1));
        assertEquals((long) SECURITY_ROWS_PER_RECORD, count,
                what + " link must carry exactly " + SECURITY_ROWS_PER_RECORD + " default-security rows");

        Boolean canRead = sessionFactory.withTransaction(session ->
                link.canRead(session, ctx.system, ctx.systemToken)
        ).await().atMost(Duration.ofMinutes(1));
        assertTrue(canRead, what + " link must be READABLE by the system identity token once secured");
    }

    @Test
    @Order(1)
    public void testAddClassificationSecuresLink() {
        Ctx ctx = provision();
        final String runId = Long.toHexString(System.nanoTime());
        final Object[] linkHolder = new Object[1];

        sessionFactory.withTransaction(session -> {
            IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
            // primary owns the capability; secondary is the classification it links to (by name).
            return cs.create(session, "RelSecPrimary_" + runId, "relationship primary",
                            EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, ctx.system)
                    .chain(primary -> cs.create(session, "RelSecSecondary_" + runId, "relationship secondary",
                                    EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, ctx.system)
                            .chain(secondary -> ((Classification) primary)
                                    .addClassification(session, secondary.getName(), "link-value", ctx.system, ctx.systemToken)))
                    .invoke(link -> linkHolder[0] = link)
                    .replaceWithVoid();
        }).await().atMost(Duration.ofMinutes(2));

        assertLinkSecured(ctx, (IWarehouseCoreTable<?, ?, ?, ?>) linkHolder[0], "ClassificationXClassification");
    }

    @Test
    @Order(2)
    public void testAddResourceItemSecuresLink() {
        Ctx ctx = provision();
        final String runId = Long.toHexString(System.nanoTime());
        final Object[] refs = new Object[2]; // [0] = primary classification, [1] = resource item
        final Object[] linkHolder = new Object[1];

        // Tx1: create the primary classification and a fully-persisted resource item (with its type).
        // Done in its own transaction so the link operation below starts from already-committed rows —
        // exactly how addResourceItem is used in practice (the secondary already exists).
        sessionFactory.withTransaction(session -> {
            IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
            IResourceItemService<?> ris = IGuiceContext.get(IResourceItemService.class);
            final String resTypeName = "RelSecResType_" + runId;
            return cs.create(session, "RelSecRiPrimary_" + runId, "resource-item link primary",
                            EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, ctx.system)
                    .invoke(primary -> refs[0] = primary)
                    .chain(primary -> ris.createType(session, resTypeName, "relationship security resource type", ctx.system)
                            .chain(type -> ris.create(session, resTypeName, "resource-data-" + runId, ctx.system)))
                    .invoke(resourceItem -> refs[1] = resourceItem)
                    .replaceWithVoid();
        }).await().atMost(Duration.ofMinutes(2));
        assertNotNull(refs[0], "primary classification must be created");
        assertNotNull(refs[1], "resource item must be created");

        // Tx2: link the (committed) resource item via the previously-broken IManageResourceItems capability.
        sessionFactory.withTransaction(session -> {
            Classification primary = (Classification) refs[0];
            var resourceItem = (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem<?, ?>) refs[1];
            return primary.addResourceItem(session, null, resourceItem, "ri-link-value", ctx.system, ctx.systemToken)
                    .invoke(link -> linkHolder[0] = link)
                    .replaceWithVoid();
        }).await().atMost(Duration.ofMinutes(2));

        assertLinkSecured(ctx, (IWarehouseCoreTable<?, ?, ?, ?>) linkHolder[0], "ClassificationXResourceItem");
    }
}



