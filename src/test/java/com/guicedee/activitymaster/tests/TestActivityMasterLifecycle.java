package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SecurityAccessException;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.SecurityPassword;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.SecurityPasswordSalt;
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

        // Pre-resolve all service singletons on the test main thread BEFORE entering any Vert.x callback.
        // GuicedEE's CallScope is only active on the test thread; resolving inside withSession/withTransaction
        // would trigger OutOfScopeException for CallScopeProperties on the first (construction) call.
        // Once the singletons are cached here, IGuiceContext.get() calls inside async callbacks are safe.
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
        IGuiceContext.get(IClassificationService.class);
        IGuiceContext.get(IActiveFlagService.class);

        // Ensure Enterprise is installed and started for this test class context
        sessionFactory.withSession(session ->
                session.withTransaction(tx -> {
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

    /**
     * Exercises the modern password mechanism and its migration path:
     * <ol>
     *   <li>The admin user (seeded in {@link #setup()}) authenticates and its credential is stored
     *       in the modern self-describing PHC format ({@code $pbkdf2-sha256$...}).</li>
     *   <li>An incorrect password is rejected.</li>
     *   <li>A credential downgraded to the legacy PBKDF2-HMAC-SHA1 format (with a separately stored
     *       salt) still authenticates and is transparently re-hashed to the modern format on
     *       successful login — proving the upgrade/migration path.</li>
     * </ol>
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PasswordLifecycle {

        private static final String ADMIN_USER = "admin";
        private static final String ADMIN_PASSWORD = "!@adminadmin";

        @Test
        @Order(1)
        public void testModernPasswordLoginAndFormat() {
            var uni = sessionFactory.withTransaction(session -> {
                IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                IPasswordsService<?> passwordsService = IGuiceContext.get(IPasswordsService.class);

                return enterpriseService.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                .chain(sysObj -> {
                                    ISystems<?, ?> sys = (ISystems<?, ?>) sysObj;
                                    return passwordsService.findByUsernameAndPassword(session, ADMIN_USER, ADMIN_PASSWORD, sys, true)
                                            .invoke(party -> assertNotNull(party, "Admin should authenticate with the correct password"))
                                            .chain(party -> ((IInvolvedParty<?, ?>) party).findClassification(session, SecurityPassword, sys)
                                                    .invoke(rel -> {
                                                        assertNotNull(rel, "Stored password credential should exist");
                                                        Assertions.assertTrue(rel.getValue().startsWith("$pbkdf2-sha256$"),
                                                                "Password must be stored in the modern PHC format, was: " + rel.getValue());
                                                    }));
                                })
                        );
            });
            uni.await().atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(2)
        public void testIncorrectPasswordRejected() {
            var uni = sessionFactory.withTransaction(session -> {
                IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                IPasswordsService<?> passwordsService = IGuiceContext.get(IPasswordsService.class);

                return enterpriseService.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                .chain(sysObj -> {
                                    ISystems<?, ?> sys = (ISystems<?, ?>) sysObj;
                                    return passwordsService.findByUsernameAndPassword(session, ADMIN_USER, "definitely-the-wrong-password", sys, true)
                                            .onItem().transform(p -> Boolean.FALSE)
                                            .onFailure(SecurityAccessException.class).recoverWithItem(Boolean.TRUE)
                                            .invoke(rejected -> Assertions.assertTrue(rejected,
                                                    "An incorrect password must be rejected with a SecurityAccessException"));
                                })
                        );
            });
            uni.await().atMost(Duration.ofMinutes(2));
        }

        @Test
        @Order(3)
        public void testLegacyPasswordMigratedOnLogin() {
            var uni = sessionFactory.withTransaction(session -> {
                IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
                ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
                IPasswordsService<?> passwordsService = IGuiceContext.get(IPasswordsService.class);

                return enterpriseService.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                .chain(sysObj -> {
                                    ISystems<?, ?> sys = (ISystems<?, ?>) sysObj;
                                    return passwordsService.findByUsername(session, ADMIN_USER, sys)
                                            .chain(partyObj -> {
                                                IInvolvedParty<?, ?> party = (IInvolvedParty<?, ?>) partyObj;

                                                // Seed a LEGACY credential (PBKDF2-HMAC-SHA1 + integer-encoded salt) for the same password
                                                Passwords pw = new Passwords();
                                                byte[] salt = pw.getNextSalt();
                                                byte[] hash = pw.hash(ADMIN_PASSWORD.toCharArray(), salt);
                                                String legacyPass = pw.integerEncrypt(hash);
                                                String legacySalt = pw.integerEncrypt(salt);

                                                return party.addOrUpdateClassification(session, SecurityPassword, null, legacyPass, sys)
                                                        .chain(() -> party.addOrUpdateClassification(session, SecurityPasswordSalt, null, legacySalt, sys))
                                                        // Confirm the stored credential is now in the legacy (non-PHC) format
                                                        .chain(() -> party.findClassification(session, SecurityPassword, sys)
                                                                .invoke(rel -> Assertions.assertFalse(rel.getValue().startsWith("$"),
                                                                        "Seeded legacy hash should not be in PHC format")))
                                                        // Authenticate — this should succeed AND upgrade the stored hash
                                                        .chain(() -> passwordsService.findByUsernameAndPassword(session, ADMIN_USER, ADMIN_PASSWORD, sys, true)
                                                                .invoke(p -> assertNotNull(p, "Legacy credential should still authenticate")))
                                                        // Verify the credential was migrated to the modern PHC format
                                                        .chain(() -> party.findClassification(session, SecurityPassword, sys)
                                                                .invoke(rel -> Assertions.assertTrue(rel.getValue().startsWith("$pbkdf2-sha256$"),
                                                                        "After login the legacy hash must be migrated to the modern PHC format, was: " + rel.getValue())));
                                            });
                                })
                        );
            });
            uni.await().atMost(Duration.ofMinutes(2));
        }
    }

    /**
     * Verifies that row-level security is <em>opt-in</em>: it only activates when the
     * {@link ActivityMasterConfiguration#isSecurityEnabled() security flag} is set.
     * <p>
     * The flag is stored in the {@code CallScopeProperties} of the active call scope, which is
     * backed by a Vert.x context. The plain JUnit test thread has no Vert.x context, so the toggling
     * assertions are executed inside a freshly entered call scope on a Vert.x context.
     * <ul>
     *   <li>Security is enabled by default (secure-by-default) inside a fresh call scope.</li>
     *   <li>Clearing the flag deactivates security for the current call scope.</li>
     *   <li>Re-setting the flag re-activates it.</li>
     *   <li>{@link ActivityMasterConfiguration#configureThread} propagates the flag onto the scope.</li>
     *   <li>With no active call scope the flag falls back to the secure default (enabled).</li>
     * </ul>
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SecurityFlagLifecycle {

        /**
         * Executes {@code action} inside a started call scope on a Vert.x context and returns its result.
         */
        private <T> T inCallScope(java.util.function.Supplier<T> action) {
            io.vertx.core.Vertx vertx = IGuiceContext.get(io.vertx.core.Vertx.class);
            com.guicedee.client.scopes.CallScoper scoper = IGuiceContext.get(com.guicedee.client.scopes.CallScoper.class);
            return Uni.createFrom().<T>emitter(em -> vertx.runOnContext(v -> {
                        boolean entered = false;
                        try {
                            if (!scoper.isStartedScope()) {
                                scoper.enterQuietly();
                                entered = true;
                            }
                            em.complete(action.get());
                        } catch (Throwable t) {
                            em.fail(t);
                        } finally {
                            if (entered) {
                                scoper.exitQuietly();
                            }
                        }
                    }))
                    .await().atMost(Duration.ofSeconds(30));
        }

        @Test
        @Order(1)
        public void testSecurityEnabledByDefaultWithinScope() {
            Boolean enabled = inCallScope(() -> ActivityMasterConfiguration.get().isSecurityEnabled());
            Assertions.assertTrue(enabled,
                    "Security must default to enabled (secure-by-default) inside a fresh call scope");
        }

        @Test
        @Order(2)
        public void testSecurityDeactivatesWhenFlagCleared() {
            boolean[] state = inCallScope(() -> {
                ActivityMasterConfiguration config = ActivityMasterConfiguration.get();
                boolean def = config.isSecurityEnabled();
                config.setSecurityEnabled(false);
                boolean off = config.isSecurityEnabled();
                config.setSecurityEnabled(true);
                boolean on = config.isSecurityEnabled();
                return new boolean[]{def, off, on};
            });
            Assertions.assertTrue(state[0], "Security must start enabled by default");
            Assertions.assertFalse(state[1], "Security must be inactive once the flag is explicitly cleared");
            Assertions.assertTrue(state[2], "Security must re-activate once the flag is set again");
        }

        @Test
        @Order(3)
        public void testConfigureThreadHonoursSecurityFlag() {
            boolean[] state = inCallScope(() -> {
                ActivityMasterConfiguration config = ActivityMasterConfiguration.get();
                config.configureThread(new ActivityMasterConfiguration.ActivityMasterConfigurationDTO()
                        .setEnterpriseName(TestEnterprise.name())
                        .setSecurities(false));
                boolean off = config.isSecurityEnabled();
                config.configureThread(new ActivityMasterConfiguration.ActivityMasterConfigurationDTO()
                        .setEnterpriseName(TestEnterprise.name())
                        .setSecurities(true));
                boolean on = config.isSecurityEnabled();
                return new boolean[]{off, on};
            });
            Assertions.assertFalse(state[0], "configureThread must deactivate security when the DTO flag is false");
            Assertions.assertTrue(state[1], "configureThread must activate security when the DTO flag is true");
        }

        @Test
        @Order(4)
        public void testSecurityFallsBackToSecureDefaultWithoutScope() {
            // Outside any active call scope (plain test thread) the flag must fall back to the
            // secure default — security is ON — so it can never be silently disabled.
            Assertions.assertTrue(ActivityMasterConfiguration.get().isSecurityEnabled(),
                    "With no active call scope, security must fall back to the secure default (enabled)");
        }
    }
}
