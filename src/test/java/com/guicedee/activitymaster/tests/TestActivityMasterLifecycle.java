package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterLifecycle {

    protected Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup() {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        log.info("Loading DB Configuration / PersistService from Guice (Lifecycle)");
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Ensure Enterprise is installed and started for this test class context
        sessionFactory.withSession(session ->
                session.withTransaction(tx -> {
                    IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                    ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                    // Create or get enterprise
                    return enterpriseService.getEnterprise(session, TestEnterprise.name())
                            .onFailure().recoverWithUni(t -> {
                                var ent = enterpriseService.get();
                                ent.setName(TestEnterprise.name());
                                ent.setDescription("Enterprise Entity for Lifecycle Testing");
                                return enterpriseService.createNewEnterprise(session, ent);
                            })
                            .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                    .onFailure().recoverWithUni(t -> systemsService.create(session, (IEnterprise<?, ?>) ent,
                                            ISystemsService.ActivityMasterSystemName, "Activity Master System"))
                            )
                            .replaceWith(Uni.createFrom().voidItem());
                })
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));

        // Start the enterprise (idempotent)
        sessionFactory.withSession(session ->
                session.withTransaction(tx -> {
                    IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                    return enterpriseService.startNewEnterprise(session, TestEnterprise.name(), "admin", "!@adminadmin")
                            .onFailure().recoverWithItem(e -> null)
                            .replaceWith(Uni.createFrom().voidItem());
                })
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class EnterpriseLifecycle {
        @Test
        @Order(1)
        public void testRemoveClassificationFromEnterprise() {
            var uni = sessionFactory.withTransaction(session -> {
                IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);

                return enterpriseService.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                .chain(sys -> {
                                    String name = "JoinTest_Classy_Enterprise_Remove_1";
                                    String value = "ENT-REM-1";
                                    var concept = EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                    return classificationService.create(session, name, "enterprise remove classification", concept, sys)
                                            .chain(created -> ((IEnterprise<?, ?>) ent).addClassification(session, name, value, sys))
                                            .chain(rel -> ((IEnterprise<?, ?>) ent).removeClassification(session, name, value, sys))
                                            .chain(ignored -> ((IEnterprise<?, ?>) ent).numberOfClassifications(session, name, value, sys)
                                                    .invoke(count -> Assertions.assertEquals(0L, count,
                                                            "After removeClassification, there should be 0 active enterprise joins for the given value"))
                                            );
                                })
                        );
            });
            uni.await().atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testArchiveClassificationOnEnterprise() {
            var uni = sessionFactory.withTransaction(session -> {
                IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);

                return enterpriseService.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                .chain(sys -> {
                                    String name = "JoinTest_Classy_Enterprise_Archive_1";
                                    String value = "ENT-ARC-1";
                                    var concept = EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                    return classificationService.create(session, name, "enterprise archive classification", concept, sys)
                                            .chain(created -> ((IEnterprise<?, ?>) ent).addClassification(session, name, value, sys))
                                            .chain(rel -> ((IEnterprise<?, ?>) ent).archiveClassification(session, name, value, sys))
                                            .chain(ignored -> ((IEnterprise<?, ?>) ent).numberOfClassifications(session, name, value, sys)
                                                    .invoke(count -> Assertions.assertEquals(0L, count,
                                                            "After archiveClassification, there should be 0 active enterprise joins for the given value"))
                                            );
                                })
                        );
            });
            uni.await().atMost(Duration.ofMinutes(2));
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ActiveFlagLifecycle {
        @Test
        @Order(1)
        public void testRemoveClassificationFromActiveFlag() {
            var uni = sessionFactory.withTransaction(session -> {
                IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
                IActiveFlagService<?> activeFlagService = IGuiceContext.get(IActiveFlagService.class);

                return enterpriseService.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                .chain(sys -> activeFlagService.getActiveFlag(session, (IEnterprise<?, ?>) ent)
                                        .chain(active -> {
                                            String name = "JoinTest_Classy_Active_Remove_1";
                                            String value = "AF-REM-1";
                                            var concept = EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                            return classificationService.create(session, name, "activeflag remove classification", concept, sys)
                                                    .chain(created -> ((IActiveFlag<?, ?>) active).addClassification(session, name, value, sys))
                                                    .chain(rel -> ((IActiveFlag<?, ?>) active).removeClassification(session, name, value, sys))
                                                    .chain(ignored -> ((IActiveFlag<?, ?>) active).numberOfClassifications(session, name, value, sys)
                                                            .invoke(count -> Assertions.assertEquals(0L, count,
                                                                    "After removeClassification, there should be 0 active ActiveFlag joins for the given value"))
                                                    );
                                        })
                                )
                        );
            });
            uni.await().atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testArchiveClassificationOnActiveFlag() {
            var uni = sessionFactory.withTransaction(session -> {
                IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                IClassificationService<?> classificationService = IGuiceContext.get(IClassificationService.class);
                IActiveFlagService<?> activeFlagService = IGuiceContext.get(IActiveFlagService.class);

                return enterpriseService.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                .chain(sys -> activeFlagService.getActiveFlag(session, (IEnterprise<?, ?>) ent)
                                        .chain(active -> {
                                            String name = "JoinTest_Classy_Active_Archive_1";
                                            String value = "AF-ARC-1";
                                            var concept = EnterpriseClassificationDataConcepts.NoClassificationDataConceptName;

                                            return classificationService.create(session, name, "activeflag archive classification", concept, sys)
                                                    .chain(created -> ((IActiveFlag<?, ?>) active).addClassification(session, name, value, sys))
                                                    .chain(rel -> ((IActiveFlag<?, ?>) active).archiveClassification(session, name, value, sys))
                                                    .chain(ignored -> ((IActiveFlag<?, ?>) active).numberOfClassifications(session, name, value, sys)
                                                            .invoke(count -> Assertions.assertEquals(0L, count,
                                                                    "After archiveClassification, there should be 0 active ActiveFlag joins for the given value"))
                                                    );
                                        })
                                )
                        );
            });
            uni.await().atMost(Duration.ofMinutes(2));
        }
    }
}
