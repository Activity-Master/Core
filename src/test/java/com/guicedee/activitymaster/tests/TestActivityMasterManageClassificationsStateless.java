package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.events.IEvent;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.products.IProduct;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRules;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Stateless sibling of {@link TestActivityMasterManageClassifications}. Exercises the
 * <strong>{@code IManage<xxx>}</strong> classification capability mixin on a
 * {@link Mutiny.StatelessSession} across <em>every entity domain</em> — the
 * {@code addClassification} / {@code numberOfClassifications} / {@code findClassification} family — proving
 * the relationship-classification {@code add*}/{@code find*}/{@code numberOf*} stateless twins behave
 * exactly like their managed counterparts.
 *
 * <p>Each test runs entirely on a single {@code withStatelessTransaction} unit (read-your-writes): create
 * the domain primary statelessly, ensure a classification exists (stateless create), tag it via the
 * stateless {@code addClassification}, then assert the stateless {@code numberOfClassifications} == 1 and
 * the stateless {@code findClassification} returns the tagged value. The empty identity token resolves to
 * the system's own identity (the system reading the data it just created), so the {@code canRead}-gated
 * {@code findClassification} succeeds — identical to the managed sibling.</p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestActivityMasterManageClassificationsStateless {

    protected Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup() {
        com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.get()
                .setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Provision the enterprise on the stateless pipeline (no bridge). createNewEnterprise creates the
        // record + installs/registers every system via the stateless registerSystem path; startNewEnterprise
        // then seeds the admin + post-startups. Idempotent: create only when absent, always start.
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        sessionFactory.openStatelessSession()
                .chain(ss -> enterpriseService.getEnterprise(ss, TestEnterprise.name())
                        .onFailure().recoverWithUni(t -> {
                            var ent = enterpriseService.get();
                            ent.setName(TestEnterprise.name());
                            ent.setDescription("Enterprise for stateless IManage tests");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), "admin", "adminadmin!@"))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.ofMinutes(3));
    }

    /** Stateless classification create helper (idempotent find-or-create). */
    private Uni<Void> ensureClassification(Mutiny.StatelessSession session, ISystems<?, ?> sys, String name) {
        IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
        return classificationService.create(session, name, "mc", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys)
                .replaceWithVoid();
    }

    private Uni<ISystems<?, ?>> activityMaster(Mutiny.StatelessSession session) {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        return es.getEnterprise(session, TestEnterprise.name())
                .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                .map(sys -> (ISystems<?, ?>) sys);
    }

    @Test
    public void testArrangements_AddClassification_Stateless() {
        IArrangementsService<?> arrangementsService = IGuiceContext.get(IArrangementsService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String classyName = "SlMC_Classy_Arrangements_" + hex;
        final String value = "ARR-VAL-CL-" + hex;
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> arrangementsService.createArrangementType(session, "SlMC_OrderType_" + hex, sys)
                        .chain(type -> arrangementsService.create(session, "SlMC_OrderType_" + hex, null,
                                DefaultClassifications.NoClassification.name(), "ARR-CL-" + hex, sys))
                        .chain(arr -> ensureClassification(session, sys, classyName)
                                .chain(() -> ((IArrangement<?, ?>) arr).addClassification(session, classyName, value, sys))
                                .chain(() -> ((IArrangement<?, ?>) arr).numberOfClassifications(session, classyName, value, sys)
                                        .invoke(cnt -> Assertions.assertEquals(1L, cnt, "numberOfClassifications stateless == 1")))
                                .chain(() -> ((IArrangement<?, ?>) arr).findClassification(session, classyName, sys)
                                        .invoke(found -> {
                                            Assertions.assertNotNull(found, "findClassification stateless must resolve");
                                            Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                                        }))
                                .replaceWithVoid())))
                .await().atMost(Duration.ofMinutes(2));
    }

    @Test
    public void testEvents_AddClassification_Stateless() {
        IEventService<?> eventService = IGuiceContext.get(IEventService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String classyName = "SlMC_Classy_Events_" + hex;
        final String value = "EVT-VAL-CL-" + hex;
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> eventService.createEventType(session, "SlMC_EventType_" + hex, sys)
                        .chain(et -> eventService.createEvent(session, "SlMC_EventType_" + hex, sys))
                        .chain(evt -> ensureClassification(session, sys, classyName)
                                .chain(() -> ((IEvent<?, ?>) evt).addClassification(session, classyName, value, sys))
                                .chain(() -> ((IEvent<?, ?>) evt).numberOfClassifications(session, classyName, value, sys)
                                        .invoke(cnt -> Assertions.assertEquals(1L, cnt, "numberOfClassifications stateless == 1")))
                                .chain(() -> ((IEvent<?, ?>) evt).findClassification(session, classyName, sys)
                                        .invoke(found -> {
                                            Assertions.assertNotNull(found, "findClassification stateless must resolve");
                                            Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                                        }))
                                .replaceWithVoid())))
                .await().atMost(Duration.ofMinutes(2));
    }

    @Test
    public void testProducts_AddClassification_Stateless() {
        IProductService<?> productService = IGuiceContext.get(IProductService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String classyName = "SlMC_Classy_Product_" + hex;
        final String value = "PROD-VAL-CL-" + hex;
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> productService.createProductType(session, "SlMC_GadgetType_" + hex, "Gadget Type", sys)
                        .chain(pt -> productService.createProduct(session, "SlMC_GadgetType_" + hex, "SlMC_Widget_" + hex, "Widget Desc", "WID-CL-" + hex, sys))
                        .chain(prod -> ensureClassification(session, sys, classyName)
                                .chain(() -> ((IProduct<?, ?>) prod).addClassification(session, classyName, value, sys))
                                .chain(() -> ((IProduct<?, ?>) prod).numberOfClassifications(session, classyName, value, sys)
                                        .invoke(cnt -> Assertions.assertEquals(1L, cnt, "numberOfClassifications stateless == 1")))
                                .chain(() -> ((IProduct<?, ?>) prod).findClassification(session, classyName, sys)
                                        .invoke(found -> {
                                            Assertions.assertNotNull(found, "findClassification stateless must resolve");
                                            Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                                        }))
                                .replaceWithVoid())))
                .await().atMost(Duration.ofMinutes(2));
    }

    @Test
    public void testParty_AddClassification_Stateless() {
        IInvolvedPartyService<?> partyService = IGuiceContext.get(IInvolvedPartyService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String idType = "SlMC_NationalID_" + hex;
        final String idValue = "SlMC-PTY-ID-" + hex;
        final String classyName = "SlMC_Classy_Party_" + hex;
        final String value = "IP-VAL-CL-" + hex;
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> partyService.createIdentificationType(session, sys, idType, "National Identification")
                        .chain(() -> partyService.createType(session, sys, "Person", "Person Type"))
                        .chain(() -> partyService.create(session, sys, new Pair<>(idType, idValue), true))
                        .chain(party -> ensureClassification(session, sys, classyName)
                                .chain(() -> ((IInvolvedParty<?, ?>) party).addClassification(session, classyName, value, sys))
                                .chain(() -> ((IInvolvedParty<?, ?>) party).numberOfClassifications(session, classyName, value, sys)
                                        .invoke(cnt -> Assertions.assertEquals(1L, cnt, "numberOfClassifications stateless == 1")))
                                .chain(() -> ((IInvolvedParty<?, ?>) party).findClassification(session, classyName, sys)
                                        .invoke(found -> {
                                            Assertions.assertNotNull(found, "findClassification stateless must resolve");
                                            Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                                        }))
                                .replaceWithVoid())))
                .await().atMost(Duration.ofMinutes(3));
    }

    @Test
    public void testRules_AddClassification_Stateless() {
        IRulesService<?> rulesService = IGuiceContext.get(IRulesService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String classyName = "SlMC_Classy_Rules_" + hex;
        final String value = "R-VAL-CL-" + hex;
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> rulesService.createRules(session, "SlMC_RuleType_" + hex, "SlMC_Rule_" + hex, "desc", sys)
                        .chain(rule -> ensureClassification(session, sys, classyName)
                                .chain(() -> ((IRules<?, ?>) rule).addClassification(session, classyName, value, sys))
                                .chain(() -> ((IRules<?, ?>) rule).numberOfClassifications(session, classyName, value, sys)
                                        .invoke(cnt -> Assertions.assertEquals(1L, cnt, "numberOfClassifications stateless == 1")))
                                .chain(() -> ((IRules<?, ?>) rule).findClassification(session, classyName, sys)
                                        .invoke(found -> {
                                            Assertions.assertNotNull(found, "findClassification stateless must resolve");
                                            Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                                        }))
                                .replaceWithVoid())))
                .await().atMost(Duration.ofMinutes(2));
    }

    @Test
    public void testActiveFlag_AddClassification_Stateless() {
        IActiveFlagService<?> activeFlagService = IGuiceContext.get(IActiveFlagService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String classyName = "SlMC_Classy_ActiveFlag_" + hex;
        final String value = "AF-VAL-CL-" + hex;
        sessionFactory.withStatelessTransaction(session -> {
            IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
            return es.getEnterprise(session, TestEnterprise.name())
                    .chain(ent -> activityMaster(session)
                            .chain(sys -> activeFlagService.getActiveFlag(session, (IEnterprise<?, ?>) ent)
                                    .chain(active -> ensureClassification(session, sys, classyName)
                                            .chain(() -> ((IActiveFlag<?, ?>) active).addClassification(session, classyName, value, sys))
                                            .chain(() -> ((IActiveFlag<?, ?>) active).numberOfClassifications(session, classyName, value, sys)
                                                    .invoke(cnt -> Assertions.assertEquals(1L, cnt, "numberOfClassifications stateless == 1")))
                                            .chain(() -> ((IActiveFlag<?, ?>) active).findClassification(session, classyName, sys)
                                                    .invoke(found -> {
                                                        Assertions.assertNotNull(found, "findClassification stateless must resolve");
                                                        Assertions.assertEquals(value, ((IRelationshipValue<?, ?, ?>) found).getValue());
                                                    }))
                                            .replaceWithVoid())));
        }).await().atMost(Duration.ofMinutes(2));
    }

    @Test
    public void testClassification_CreateWithParent_AddsChildLink_Stateless() {
        IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String parentName = "SlMC_ClassParent_" + hex;
        final String childName = "SlMC_ClassChild_" + hex;
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> classificationService.create(session, parentName, "descP", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys)
                        .chain(parent -> classificationService.create(session, childName, "descC", EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, sys, 1,
                                        (IClassification<?, ?>) parent)
                                .replaceWith(parent))
                        // Stateless findChildren returns the hierarchy link rows. The link's secondary
                        // @ManyToOne is lazy and cannot be initialised on a detached stateless entity, so we
                        // assert the link EXISTS (fresh parent had no children) rather than navigating
                        // secondary.getName() — the link presence proves the stateless addChild ran.
                        .chain(parent -> ((IClassification<?, ?>) parent)
                                .findChildren(session, DefaultClassifications.NoClassification.name(), null, sys)
                                .invoke(children -> {
                                    Assertions.assertNotNull(children, "Children list should not be null");
                                    Assertions.assertFalse(children.isEmpty(), "Stateless findChildren must return the freshly-linked child");
                                }))
                        .replaceWithVoid()))
                .await().atMost(Duration.ofMinutes(2));
    }

    /**
     * Multi-classification add + stateless per-field read. Tags one arrangement with three classifications
     * via the stateless {@code addClassification}, then asserts each is independently resolvable via the
     * stateless {@code numberOfClassifications} (==1) and {@code findClassification} (value matches). This
     * exercises the {@code add*}/{@code find*}/{@code numberOf*} stateless mixin family for several links on
     * a single entity.
     *
     * <p>Note: the batched {@code findClassificationValues} aggregator navigates each link's lazy
     * {@code @ManyToOne} secondary classification name, which is not initialisable on a detached stateless
     * entity — so the equivalent coverage here uses the per-field {@code findClassification} read (the value
     * lives on the link row itself, not the lazy secondary).</p>
     */
    @Test
    public void testMultipleClassifications_PerFieldRead_Stateless() {
        IArrangementsService<?> arrangementsService = IGuiceContext.get(IArrangementsService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String n1 = "SlMC_FCV_Alpha_" + hex; final String v1 = "alpha-" + hex;
        final String n2 = "SlMC_FCV_Beta_" + hex;  final String v2 = "beta-" + hex;
        final String n3 = "SlMC_FCV_Gamma_" + hex; final String v3 = "gamma-" + hex;
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> arrangementsService.createArrangementType(session, "SlMC_OrderType_FCV_" + hex, sys)
                        .chain(type -> arrangementsService.create(session, "SlMC_OrderType_FCV_" + hex, null,
                                DefaultClassifications.NoClassification.name(), "ARR-FCV-" + hex, sys))
                        .chain(arr -> {
                            IArrangement<?, ?> a = (IArrangement<?, ?>) arr;
                            return ensureClassification(session, sys, n1)
                                    .chain(() -> ensureClassification(session, sys, n2))
                                    .chain(() -> ensureClassification(session, sys, n3))
                                    .chain(() -> a.addClassification(session, n1, v1, sys))
                                    .chain(() -> a.addClassification(session, n2, v2, sys))
                                    .chain(() -> a.addClassification(session, n3, v3, sys))
                                    .chain(() -> a.numberOfClassifications(session, n1, v1, sys)
                                            .invoke(c -> Assertions.assertEquals(1L, c, "Alpha link count")))
                                    .chain(() -> a.numberOfClassifications(session, n2, v2, sys)
                                            .invoke(c -> Assertions.assertEquals(1L, c, "Beta link count")))
                                    .chain(() -> a.numberOfClassifications(session, n3, v3, sys)
                                            .invoke(c -> Assertions.assertEquals(1L, c, "Gamma link count")))
                                    .chain(() -> a.findClassification(session, n2, sys)
                                            .invoke(rel -> Assertions.assertEquals(v2, ((IRelationshipValue<?, ?, ?>) rel).getValue(),
                                                    "Stateless per-field read must return the tagged value")))
                                    .replaceWithVoid();
                        })))
                .await().atMost(Duration.ofMinutes(2));
    }

    /**
     * Parallel addClassifications test. Exercises multiple concurrent {@code addClassification} calls
     * across <em>independent</em> stateless transactions/sessions. This is the only appropriate way
     * to run parallel mutations in Hibernate Reactive (one session = one op at a time).
     *
     * <p>The test creates the primary entity and classifications sequentially (setup), then triggers
     * three parallel {@code withStatelessTransaction} units via {@code Multi.merge}. Each branch
     * opens its own session to add a different classification to the same entity. Finally, it
     * joins and asserts all three were persisted.</p>
     */
    @Test
    public void testParallelAddClassifications_Stateless() {
        IArrangementsService<?> arrangementsService = IGuiceContext.get(IArrangementsService.class);
        final String hex = Long.toHexString(System.nanoTime());
        final String n1 = "SlMC_Para_A_" + hex; final String v1 = "val-a-" + hex;
        final String n2 = "SlMC_Para_B_" + hex; final String v2 = "val-b-" + hex;
        final String n3 = "SlMC_Para_C_" + hex; final String v3 = "val-c-" + hex;

        // 1. Setup: Create arrangement and classifications sequentially.
        IArrangement<?, ?> arrangement = sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> arrangementsService.createArrangementType(session, "SlMC_ParaType_" + hex, sys)
                        .chain(type -> arrangementsService.create(session, "SlMC_ParaType_" + hex, null,
                                DefaultClassifications.NoClassification.name(), "ARR-PARA-" + hex, sys))
                        .chain(arr -> ensureClassification(session, sys, n1)
                                .chain(() -> ensureClassification(session, sys, n2))
                                .chain(() -> ensureClassification(session, sys, n3))
                                .replaceWith((IArrangement<?, ?>) arr))
                )).await().atMost(Duration.ofMinutes(2));

        // 2. Parallel add: Each branch runs in its own stateless transaction/session.
        Multi.createFrom().items(new Pair<>(n1, v1), new Pair<>(n2, v2), new Pair<>(n3, v3))
                .onItem().transformToUniAndMerge(pair -> sessionFactory.withStatelessTransaction(session ->
                        activityMaster(session).chain(sys -> arrangement.addClassification(session, pair.getKey(), pair.getValue(), sys))))
                .collect().asList()
                .await().atMost(Duration.ofMinutes(2));

        // 3. Verify: Join and check expected counts for each link.
        sessionFactory.withStatelessTransaction(session -> activityMaster(session)
                .chain(sys -> arrangement.numberOfClassifications(session, n1, v1, sys)
                        .invoke(cnt -> Assertions.assertEquals(1L, cnt, "Parallel add n1 failed"))
                        .chain(() -> arrangement.numberOfClassifications(session, n2, v2, sys))
                        .invoke(cnt -> Assertions.assertEquals(1L, cnt, "Parallel add n2 failed"))
                        .chain(() -> arrangement.numberOfClassifications(session, n3, v3, sys))
                        .invoke(cnt -> Assertions.assertEquals(1L, cnt, "Parallel add n3 failed"))
                        .replaceWithVoid()
                )).await().atMost(Duration.ofMinutes(1));
    }
}


