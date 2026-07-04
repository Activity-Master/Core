package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.ResourceItemClassifications;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for stateless enterprise update and management operations.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestActivityMasterEnterpriseUpdatesStateless {

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
                            ent.setDescription("Enterprise for stateless update tests");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), "admin", "adminadmin!@"))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.ofMinutes(3));
    }

    @Test
    @DisplayName("Test loadUpdates in Stateless Session")
    public void testLoadUpdates_Stateless() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        
        Integer updatesProcessed = sessionFactory.withStatelessSession(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> es.loadUpdates(session, (IEnterprise<?, ?>) ent))
        ).await().atMost(Duration.ofMinutes(2));
        
        assertNotNull(updatesProcessed);
        assertTrue(updatesProcessed >= 0, "Updates processed should be non-negative");
    }
    
    @Test
    @DisplayName("Test getUpdates in Stateless Session")
    public void testGetUpdates_Stateless() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        
        var updates = sessionFactory.withStatelessSession(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> es.getUpdates(session, (IEnterprise<?, ?>) ent))
        ).await().atMost(Duration.ofMinutes(1));
        
        assertNotNull(updates, "Available updates map should not be null");
    }

    @Test
    @DisplayName("Test getEnterpriseAppliedUpdates in Stateless Session")
    public void testGetEnterpriseAppliedUpdates_Stateless() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        
        var appliedUpdates = sessionFactory.withStatelessSession(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> es.getEnterpriseAppliedUpdates(session, (IEnterprise<?, ?>) ent))
        ).await().atMost(Duration.ofMinutes(1));
        
        assertNotNull(appliedUpdates, "Applied updates set should not be null");
    }

    @Test
    @DisplayName("Test isEnterpriseReady in Stateless Session")
    public void testIsEnterpriseReady_Stateless() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        
        IEnterprise<?, ?> ready = sessionFactory.withStatelessSession(session ->
                es.isEnterpriseReady(session)
        ).await().atMost(Duration.ofMinutes(1));
        
        assertNotNull(ready, "Enterprise should be ready and returned");
        assertEquals(TestEnterprise.name(), ready.getName());
    }

    @Test
    @DisplayName("Test findEnterprisesWithClassification in Stateless Session")
    public void testFindEnterprisesWithClassification_Stateless() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        com.guicedee.activitymaster.fsdm.client.services.IClassificationService<?> cs = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.IClassificationService.class);
        com.guicedee.activitymaster.fsdm.client.services.ISystemsService<?> ss = IGuiceContext.get(com.guicedee.activitymaster.fsdm.client.services.ISystemsService.class);

        List<IEnterprise<?, ?>> list = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> es.loadUpdates(session, (IEnterprise<?, ?>) ent)
                                .chain(count -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                        .chain(sys -> cs.find(session, com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassifications.LastUpdateDate.toString(), (com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems<?, ?>) sys)
                                                .chain(cls -> es.findEnterprisesWithClassification(session, cls)))))
        ).await().atMost(Duration.ofMinutes(1));

        assertNotNull(list, "Enterprise list should not be null");
        assertTrue(list.stream().anyMatch(e -> e.getName().equals(TestEnterprise.name())), "Test Enterprise should be found by classification");
    }

    /**
     * Regression for the original defect: on the stateless path {@code ResourceItemsBaseSetup} only overrode
     * the managed {@code update(Mutiny.Session,…)}, so its stateless twin never ran and the {@code Icon}
     * classification was never seeded — the first stateless lookup of "Icon" then threw {@code NoResultException}.
     * After running the stateless {@code loadUpdates}, the "Icon" classification must resolve.
     */
    @Test
    @DisplayName("Stateless install seeds the 'Icon' resource-item classification")
    public void statelessInstall_seedsIconClassification() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);

        IClassification<?, ?> icon = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> es.loadUpdates(session, (IEnterprise<?, ?>) ent)
                                .chain(count -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                                .chain(sys -> cs.find(session, ResourceItemClassifications.Icon.name(), (ISystems<?, ?>) sys)))
        ).await().atMost(Duration.ofMinutes(2));

        assertNotNull(icon, "Stateless install (ResourceItemsBaseSetup twin) must seed the 'Icon' classification");
        assertEquals(ResourceItemClassifications.Icon.name(), icon.getName(), "Resolved classification must be 'Icon'");
    }

    /**
     * End-to-end reproduction of the exact failing sequence from the diagnosis: create a product type on a
     * {@link Mutiny.StatelessSession}, then immediately link the {@code Icon} classification via
     * {@code addOrReuseClassification}. Before the fix this threw {@code NoResultException} because "Icon"
     * was unseeded on the stateless path; now it must complete.
     */
    @Test
    @DisplayName("Stateless createProductType + addOrReuseClassification(Icon) completes (NoResultException regression)")
    public void createProductType_thenAddIconClassification_stateless_completes() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IProductService<?> ps = IGuiceContext.get(IProductService.class);

        final String hex = Long.toHexString(System.nanoTime());
        final String typeName = "SlIconProdType_" + hex;

        Boolean ok = sessionFactory.withStatelessTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> es.loadUpdates(session, (IEnterprise<?, ?>) ent)
                                .chain(count -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                                .chain(sys -> ps.createProductType(session, typeName, "stateless product type", (ISystems<?, ?>) sys)
                                        .chain(pt -> pt.addOrReuseClassification(session, ResourceItemClassifications.Icon, "icon-" + hex + ".png", (ISystems<?, ?>) sys)
                                                .replaceWith(Boolean.TRUE))))
        ).await().atMost(Duration.ofMinutes(2));

        assertTrue(ok, "createProductType then addOrReuseClassification(Icon) must complete on the stateless path");
    }

    /**
     * Broader safety net for the stateless-installer sweep: each core {@code @SortedUpdate} seeder's stateless
     * twin must seed its base classification. Covers ProductsBaseSetup (Products), AddressBaseSetup (Address),
     * ClassificationBaseSetup (Languages), EventsBaseSetup (InvolvedPartyEvents) and ArrangementsBaseSetup
     * (InvolvedPartyArrangements).
     */
    @Test
    @DisplayName("Stateless install seeds core taxonomy base classifications (installer sweep)")
    public void statelessInstall_seedsCoreBaseClassifications() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);

        List<String> baseNames = List.of("Products", "Address", "Languages", "InvolvedPartyEvents", "InvolvedPartyArrangements");

        for (String name : baseNames) {
            IClassification<?, ?> found = sessionFactory.withStatelessTransaction(session ->
                    es.getEnterprise(session, TestEnterprise.name())
                            .chain(ent -> es.loadUpdates(session, (IEnterprise<?, ?>) ent)
                                    .chain(count -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                                    .chain(sys -> cs.find(session, name, (ISystems<?, ?>) sys)))
            ).await().atMost(Duration.ofMinutes(2));

            assertNotNull(found, "Stateless install must seed base classification: " + name);
            assertEquals(name, found.getName(), "Resolved classification must be: " + name);
        }
    }
}
