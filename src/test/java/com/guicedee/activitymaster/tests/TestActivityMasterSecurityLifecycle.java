package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.IQueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.entityassist.RootEntity;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Lifecycle test focused on the <em>security</em> structures created when an enterprise is installed
 * and started.
 *
 * <p>When {@code startNewEnterprise(...)} runs, the
 * {@code SecurityTokenSystem.createGroupsAndFolders(...)} routine seeds the canonical security
 * taxonomy for the enterprise's Activity Master system. This test boots the reactive stack against a
 * Testcontainers PostgreSQL instance, installs and starts the test enterprise, and then asserts that
 * every expected security <strong>group</strong> and <strong>folder</strong> token exists and is
 * resolvable through {@link ISecurityTokenService}.</p>
 *
 * <h3>Groups asserted</h3>
 * <ul>
 *     <li>{@code Everyone}</li>
 *     <li>{@code Everywhere}</li>
 * </ul>
 *
 * <h3>Folders asserted</h3>
 * <ul>
 *     <li>{@code Guests}</li>
 *     <li>{@code Registered Guests}</li>
 *     <li>{@code Visitors Guests}</li>
 *     <li>{@code Administrators}</li>
 *     <li>{@code Systems}</li>
 *     <li>{@code Plugins}</li>
 *     <li>{@code Applications}</li>
 * </ul>
 *
 * <p>The {@link FutureSecurity} nested block holds {@link Disabled} placeholders for security
 * behaviours we will build out next (membership, inheritance, guest access scoping, etc.).</p>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterSecurityLifecycle {

    protected Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup() {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        log.info("Loading DB Configuration / PersistService from Guice (Security Lifecycle)");
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        // Pre-resolve all service singletons on the test main thread BEFORE entering any Vert.x callback.
        // GuicedEE's CallScope is only active on the test thread; resolving inside withSession/withTransaction
        // would trigger OutOfScopeException for CallScopeProperties on the first (construction) call.
        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
        IGuiceContext.get(IClassificationService.class);
        IGuiceContext.get(IActiveFlagService.class);
        IGuiceContext.get(ISecurityTokenService.class);

        // Ensure the enterprise + Activity Master system exist for this test class context.
        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.getEnterprise(session, TestEnterprise.name())
                                .onFailure().recoverWithUni(t -> {
                                    var ent = enterpriseService.get();
                                    ent.setName(TestEnterprise.name());
                                    ent.setDescription("Enterprise Entity for Security Lifecycle Testing");
                                    return enterpriseService.createNewEnterprise(session, ent);
                                })
                                .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                        .onFailure().recoverWithUni(t -> systemsService.create(session, (IEnterprise<?, ?>) ent,
                                                ISystemsService.ActivityMasterSystemName, "Activity Master System"))
                                )
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));

        // Start the enterprise (idempotent) — this is what creates the security groups and folders.
        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.startNewEnterprise(session, TestEnterprise.name(), "admin", "!@adminadmin")
                                .onFailure().recoverWithItem(e -> null)
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    /**
     * Resolves the test enterprise + its Activity Master system inside a single transaction and applies
     * the supplied reactive function, awaiting the result. Keeps the individual tests focused purely on
     * the security assertions.
     */
    private <T> T withActivityMasterSystem(BiFunction<Mutiny.Session, ISystems<?, ?>, Uni<T>> fn) {
        return sessionFactory.withTransaction(session -> {
            IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
            ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
            return enterpriseService.getEnterprise(session, TestEnterprise.name())
                    .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                    .chain(sys -> fn.apply(session, (ISystems<?, ?>) sys));
        }).await().atMost(Duration.ofMinutes(2));
    }

    /**
     * Returns {@code true} when an <em>active, in-range</em> hierarchy edge
     * {@code parent -> child} exists in {@code security.securitytokenxsecuritytoken} for the enterprise.
     * Mirrors the edge semantics walked by
     * {@link ISecurityTokenService#getApplicableSecurityTokenIds(Mutiny.Session, ISystems, UUID...)}.
     */
    private Uni<Boolean> edgeExists(Mutiny.Session session, UUID enterpriseId, OffsetDateTime now, UUID parentId, UUID childId) {
        String sql = "select x.childsecuritytokenid from security.securitytokenxsecuritytoken x " +
                "where x.parentsecuritytokenid = :parent and x.childsecuritytokenid = :child " +
                "and x.enterpriseid = :ent and x.effectivefromdate <= :now and x.effectivetodate > :now";
        return session.createNativeQuery(sql, UUID.class)
                .setParameter("parent", parentId)
                .setParameter("child", childId)
                .setParameter("ent", enterpriseId)
                .setParameter("now", now)
                .getResultList()
                .map(rows -> !rows.isEmpty());
    }

    /** Returns the distinct set of parent ids linked above {@code childId} (its immediate parents). */
    private Uni<java.util.List<UUID>> parentIdsOf(Mutiny.Session session, UUID enterpriseId, OffsetDateTime now, UUID childId) {
        String sql = "select distinct x.parentsecuritytokenid from security.securitytokenxsecuritytoken x " +
                "where x.childsecuritytokenid = :child and x.enterpriseid = :ent " +
                "and x.effectivefromdate <= :now and x.effectivetodate > :now";
        return session.createNativeQuery(sql, UUID.class)
                .setParameter("child", childId)
                .setParameter("ent", enterpriseId)
                .setParameter("now", now)
                .getResultList();
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GroupsAndFolders {

        @Test
        @Order(1)
        public void testSecurityGroupsExistAfterInstall() {
            withActivityMasterSystem((session, sys) -> {
                ISecurityTokenService<?> securityService = IGuiceContext.get(ISecurityTokenService.class);
                return securityService.getEveryoneGroup(session, sys)
                        .invoke(tok -> assertNotNull(tok, "'Everyone' group should exist after install"))
                        .chain(ignored -> securityService.getEverywhereGroup(session, sys)
                                .invoke(tok -> assertNotNull(tok, "'Everywhere' group should exist after install")));
            });
        }

        @Test
        @Order(2)
        public void testSecurityFoldersExistAfterInstall() {
            withActivityMasterSystem((session, sys) -> {
                ISecurityTokenService<?> securityService = IGuiceContext.get(ISecurityTokenService.class);
                return securityService.getGuestsFolder(session, sys)
                        .invoke(tok -> assertNotNull(tok, "'Guests' folder should exist after install"))
                        .chain(ignored -> securityService.getRegisteredGuestsFolder(session, sys)
                                .invoke(tok -> assertNotNull(tok, "'Registered Guests' folder should exist after install")))
                        .chain(ignored -> securityService.getVisitorsGuestsFolder(session, sys)
                                .invoke(tok -> assertNotNull(tok, "'Visitors Guests' folder should exist after install")))
                        .chain(ignored -> securityService.getAdministratorsFolder(session, sys)
                                .invoke(tok -> assertNotNull(tok, "'Administrators' folder should exist after install")))
                        .chain(ignored -> securityService.getSystemsFolder(session, sys)
                                .invoke(tok -> assertNotNull(tok, "'Systems' folder should exist after install")))
                        .chain(ignored -> securityService.getPluginsFolder(session, sys)
                                .invoke(tok -> assertNotNull(tok, "'Plugins' folder should exist after install")))
                        .chain(ignored -> securityService.getApplicationsFolder(session, sys)
                                .invoke(tok -> assertNotNull(tok, "'Applications' folder should exist after install")));
            });
        }

        @Test
        @Order(3)
        public void testSecurityTokensAreUnique() {
            withActivityMasterSystem((session, sys) -> {
                ISecurityTokenService<?> securityService = IGuiceContext.get(ISecurityTokenService.class);
                // The canonical groups/folders must resolve to distinct security tokens.
                return securityService.getEveryoneGroup(session, sys)
                        .chain(everyone -> securityService.getAdministratorsFolder(session, sys)
                                .invoke(admins -> {
                                    String everyoneTok = ((ISecurityToken<?, ?>) everyone).getSecurityToken();
                                    String adminTok = ((ISecurityToken<?, ?>) admins).getSecurityToken();
                                    assertNotNull(everyoneTok, "'Everyone' group must carry a security token");
                                    assertNotNull(adminTok, "'Administrators' folder must carry a security token");
                                    Assertions.assertNotEquals(everyoneTok, adminTok,
                                            "'Everyone' and 'Administrators' must be distinct security tokens");
                                }));
            });
        }

        /**
         * Proves the <strong>default capability</strong>: when an enterprise is installed, the
         * {@code SecurityTokenSystem} now applies default security to its records via the batched,
         * stateless-session path. The Active Flag record is one of the tables the install secures
         * ({@code applyDefaultsToNewEnterprise}), so after install it must carry the full fan-out of
         * canonical group/folder security rows.
         */
        @Test
        @Order(4)
        public void testInstallAppliesDefaultSecurityToRecords() {
            withActivityMasterSystem((session, sys) -> {
                IActiveFlagService<?> activeFlagService = IGuiceContext.get(IActiveFlagService.class);
                return activeFlagService.getActiveFlag(session, (IEnterprise<?, ?>) sys.getEnterprise())
                        .invoke(flag -> assertNotNull(flag, "Active flag should exist after install"))
                        .chain(flag -> ((IWarehouseCoreTable<?, ?, ?, ?>) flag).countDefaultSecurity(session)
                                .invoke(count -> assertTrue(count != null && count >= 7L,
                                        "Install should have applied the canonical default security rows "
                                                + "(>=7) to the Active Flag record, found: " + count)));
            });
        }

        /**
         * Confirms the canonical <strong>user-group hierarchy</strong> that the install links via
         * {@code SecurityTokenSystem.createGroupsAndFolders(...)}. The membership tree (walked
         * child&rarr;parent by {@code getApplicableSecurityTokenIds}) is:
         * <pre>
         *   (enterprise root)
         *     ├── Everyone
         *     │     ├── Administrators
         *     │     └── Guests
         *     │           ├── Registered Guests
         *     │           └── Visitors Guests
         *     └── Everywhere
         * </pre>
         * Each expected {@code parent -> child} edge must exist (and be active/in-range), the reverse
         * edges must <em>not</em> exist (directionality), and both {@code Everyone} and {@code Everywhere}
         * must hang off the same single enterprise-root parent.
         */
        @Test
        @Order(5)
        public void testUserGroupHierarchyIsCorrect() {
            withActivityMasterSystem((session, sys) -> {
                ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                UUID enterpriseId = ((IEnterprise<?, ?>) sys.getEnterprise()).getId();
                OffsetDateTime now = IQueryBuilderSCD.convertToUTCDateTime(RootEntity.getNow());

                Map<String, ISecurityToken<?, ?>> t = new LinkedHashMap<>();
                return sec.getEveryoneGroup(session, sys).invoke(x -> t.put("everyone", x))
                        .chain(() -> sec.getEverywhereGroup(session, sys).invoke(x -> t.put("everywhere", x)))
                        .chain(() -> sec.getAdministratorsFolder(session, sys).invoke(x -> t.put("administrators", x)))
                        .chain(() -> sec.getGuestsFolder(session, sys).invoke(x -> t.put("guests", x)))
                        .chain(() -> sec.getRegisteredGuestsFolder(session, sys).invoke(x -> t.put("registered", x)))
                        .chain(() -> sec.getVisitorsGuestsFolder(session, sys).invoke(x -> t.put("visitors", x)))
                        .chain(() -> {
                            UUID everyone = t.get("everyone").getId();
                            UUID everywhere = t.get("everywhere").getId();
                            UUID administrators = t.get("administrators").getId();
                            UUID guests = t.get("guests").getId();
                            UUID registered = t.get("registered").getId();
                            UUID visitors = t.get("visitors").getId();

                            // ── Expected membership edges (parent -> child) ──
                            return edgeExists(session, enterpriseId, now, everyone, administrators)
                                    .invoke(ok -> assertTrue(ok, "Everyone -> Administrators edge should exist"))
                                    .chain(() -> edgeExists(session, enterpriseId, now, everyone, guests)
                                            .invoke(ok -> assertTrue(ok, "Everyone -> Guests edge should exist")))
                                    .chain(() -> edgeExists(session, enterpriseId, now, guests, registered)
                                            .invoke(ok -> assertTrue(ok, "Guests -> Registered Guests edge should exist")))
                                    .chain(() -> edgeExists(session, enterpriseId, now, guests, visitors)
                                            .invoke(ok -> assertTrue(ok, "Guests -> Visitors Guests edge should exist")))
                                    // ── Directionality: the reverse edges must NOT exist ──
                                    .chain(() -> edgeExists(session, enterpriseId, now, administrators, everyone)
                                            .invoke(rev -> assertFalse(rev, "Administrators must NOT be a parent of Everyone")))
                                    .chain(() -> edgeExists(session, enterpriseId, now, registered, guests)
                                            .invoke(rev -> assertFalse(rev, "Registered Guests must NOT be a parent of Guests")))
                                    // ── Everyone & Everywhere both hang off the single enterprise root ──
                                    .chain(() -> parentIdsOf(session, enterpriseId, now, everyone))
                                    .chain(everyoneParents -> parentIdsOf(session, enterpriseId, now, everywhere)
                                            .invoke(everywhereParents -> {
                                                assertEquals(1, everyoneParents.size(),
                                                        "'Everyone' should have exactly one parent (enterprise root)");
                                                assertEquals(1, everywhereParents.size(),
                                                        "'Everywhere' should have exactly one parent (enterprise root)");
                                                assertEquals(everyoneParents.get(0), everywhereParents.get(0),
                                                        "'Everyone' and 'Everywhere' must share the same enterprise-root parent");
                                            }))
                                    .replaceWithVoid();
                        });
            });
        }
    }

    /**
     * Placeholders for the next round of security lifecycle coverage. These are intentionally
     * {@link Disabled} until the corresponding behaviour and assertions are finalised — they describe
     * the intended scope so the work is discoverable and ready to flesh out.
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class FutureSecurity {

        @Test
        @Order(1)
        @Disabled("Planned: assert the seeded 'admin' user is a member of the Administrators folder/group.")
        public void testAdminUserBelongsToAdministratorsGroup() {
            // TODO: resolve the admin InvolvedParty and assert its security membership resolves to Administrators.
        }

        @Test
        @Order(2)
        public void testGuestAccessIsReadOnly() {
            final String runId = Long.toHexString(System.nanoTime());
            // Holders: [0]=enterprise, [1]=system, [2]=activeFlag, [3]=record, [4]=guests token
            final Object[] h = new Object[5];
            final Map<String, ISecurityToken<?, ?>> tokens = new LinkedHashMap<>();

            // Phase A — resolve context + the canonical group/folder tokens, create a record to secure.
            sessionFactory.withTransaction(session -> {
                IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
                ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
                IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
                IActiveFlagService<?> afs = IGuiceContext.get(IActiveFlagService.class);
                ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                return es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> {
                            h[0] = ent;
                            return ss.getActivityMaster(session, (IEnterprise<?, ?>) ent);
                        })
                        .chain(sys -> {
                            h[1] = sys;
                            return afs.getActiveFlag(session, (IEnterprise<?, ?>) h[0]);
                        })
                        .chain(af -> {
                            h[2] = af;
                            return cs.create(session, "GuestRO_" + runId, "guest read-only record",
                                    EnterpriseClassificationDataConcepts.NoClassificationDataConceptName, (ISystems<?, ?>) h[1]);
                        })
                        .chain(rec -> {
                            h[3] = rec;
                            return sec.getAdministratorsFolder(session, (ISystems<?, ?>) h[1]).invoke(t -> tokens.put(IWarehouseCoreTable.SECURITY_ADMINISTRATORS, t));
                        })
                        .chain(() -> sec.getEveryoneGroup(session, (ISystems<?, ?>) h[1]).invoke(t -> tokens.put(IWarehouseCoreTable.SECURITY_EVERYONE, t)))
                        .chain(() -> sec.getEverywhereGroup(session, (ISystems<?, ?>) h[1]).invoke(t -> tokens.put(IWarehouseCoreTable.SECURITY_EVERYWHERE, t)))
                        .chain(() -> sec.getSystemsFolder(session, (ISystems<?, ?>) h[1]).invoke(t -> tokens.put(IWarehouseCoreTable.SECURITY_SYSTEMS, t)))
                        .chain(() -> sec.getApplicationsFolder(session, (ISystems<?, ?>) h[1]).invoke(t -> tokens.put(IWarehouseCoreTable.SECURITY_APPLICATIONS, t)))
                        .chain(() -> sec.getPluginsFolder(session, (ISystems<?, ?>) h[1]).invoke(t -> tokens.put(IWarehouseCoreTable.SECURITY_PLUGINS, t)))
                        .chain(() -> sec.getGuestsFolder(session, (ISystems<?, ?>) h[1]).invoke(t -> {
                            tokens.put(IWarehouseCoreTable.SECURITY_GUESTS, t);
                            h[4] = t;
                        }))
                        .replaceWithVoid();
            }).await().atMost(Duration.ofMinutes(2));

            // Phase B — apply the canonical default security to the record (stateless batch).
            Long inserted = sessionFactory.withStatelessTransaction(session ->
                    ((IWarehouseCoreTable<?, ?, ?, ?>) h[3]).createDefaultSecurity(session,
                            (ISystems<?, ?>) h[1], (IEnterprise<?, ?>) h[0], (IActiveFlag<?, ?>) h[2], tokens)
            ).await().atMost(Duration.ofMinutes(1));
            assertNotNull(inserted);
            assertTrue(inserted >= 7L, "Record should have its canonical default security written");

            // Phase C — a Guests-folder identity must be read-only: canRead true, canWrite false.
            UUID guestIdentity = UUID.fromString(((ISecurityToken<?, ?>) h[4]).getSecurityToken());
            boolean[] access = sessionFactory.withTransaction(session -> {
                IWarehouseCoreTable<?, ?, ?, ?> rec = (IWarehouseCoreTable<?, ?, ?, ?>) h[3];
                return rec.canRead(session, (ISystems<?, ?>) h[1], guestIdentity)
                        .chain(r -> rec.canWrite(session, (ISystems<?, ?>) h[1], guestIdentity)
                                .map(w -> new boolean[]{r, w}));
            }).await().atMost(Duration.ofMinutes(1));

            assertTrue(access[0], "A Guests-folder token must be able to READ a default-secured record");
            assertFalse(access[1], "A Guests-folder token must NOT be able to WRITE a default-secured record (read-only)");
        }

        @Test
        @Order(3)
        @Disabled("Planned: assert that re-running the install is idempotent and does not duplicate groups/folders.")
        public void testInstallIsIdempotentForSecurityTokens() {
            // TODO: run startNewEnterprise twice and assert each group/folder still resolves to exactly one token.
        }
    }
}




