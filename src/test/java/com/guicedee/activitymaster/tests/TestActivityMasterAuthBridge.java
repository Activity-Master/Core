package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.auth.ActivityMasterAuthBridge;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.IPasswordsService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.rest.security.SecurityRestService;
import com.guicedee.client.IGuiceContext;
import com.guicedee.rest.pathing.SecurityHandler;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.RoleBasedAuthorization;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Confirms the {@link ActivityMasterAuthBridge} populates a Vert.x {@link User} with the caller's
 * <em>details</em> (subject, username, display name) and <em>security roles</em> resolved from the
 * ActivityMaster security model when an admin authenticates via username/password.
 *
 * <p>The enterprise install mints the admin with an {@code Identity} security token named after the
 * username ({@code admin}), created under the canonical {@code Administrators} folder, and a
 * {@code Preferred} name of {@code Enterprise Creator}. The bridge therefore must surface:
 * <ul>
 *   <li>principal {@code username}/{@code preferred_username} = {@code admin};</li>
 *   <li>a non-null {@code sub} (the identity token UUID);</li>
 *   <li>a display {@code name} = {@code Enterprise Creator};</li>
 *   <li>roles including the admin's own token name ({@code admin}) and the {@code Administrators}
 *       folder, also exposed as a Vert.x {@link RoleBasedAuthorization}.</li>
 * </ul>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterAuthBridge {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "!@adminadmin";

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
        IGuiceContext.get(ActivityMasterAuthBridge.class);

        // Provision the enterprise on the stateless pipeline (no bridge). createNewEnterprise creates the
        // record + installs/registers every system via the stateless registerSystem path; startNewEnterprise
        // then seeds the admin user + canonical groups/folders. Idempotent: create only when absent.
        sessionFactory.openStatelessSession()
                .chain(ss -> enterpriseService.getEnterprise(ss, TestEnterprise.name())
                        .onFailure().recoverWithUni(t -> {
                            var ent = enterpriseService.get();
                            ent.setName(TestEnterprise.name());
                            ent.setDescription("Enterprise for Auth Bridge Testing");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), ADMIN_USER, ADMIN_PASSWORD))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    /**
     * Authenticates the admin and asks the bridge to build the Vert.x {@link User}, so the resolved
     * details and roles can be asserted off the reactive chain.
     */
    private User loginAndBuildUser() {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IPasswordsService<?> passwords = IGuiceContext.get(IPasswordsService.class);
        ActivityMasterAuthBridge bridge = IGuiceContext.get(ActivityMasterAuthBridge.class);

        return sessionFactory.withTransaction(session ->
                es.getEnterprise(session, TestEnterprise.name())
                        .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                        .chain(sys -> passwords
                                .findByUsernameAndPassword(session, ADMIN_USER, ADMIN_PASSWORD, (ISystems<?, ?>) sys, true)
                                .chain(party -> bridge.buildUser(session, (IInvolvedParty<?, ?>) party, ADMIN_USER, (ISystems<?, ?>) sys)))
        ).await().atMost(Duration.ofMinutes(2));
    }

    @Test
    @Order(1)
    public void testAdminUserDetailsArePopulated() {
        User user = loginAndBuildUser();
        assertNotNull(user, "The auth bridge must build a Vert.x User for the authenticated admin");

        assertEquals(ADMIN_USER, user.principal().getString("username"), "username must be carried on the principal");
        assertEquals(ADMIN_USER, user.principal().getString("preferred_username"), "preferred_username must be carried on the principal");

        String sub = user.principal().getString("sub");
        assertNotNull(sub, "subject (identity token) must be present");
        assertDoesNotThrow(() -> UUID.fromString(sub), "subject must be the identity token UUID");

        String name = user.principal().getString("name");
        assertNotNull(name, "a display name must be resolved from the DB");
        assertFalse(name.isBlank(), "the display name must not be blank");
        assertEquals("Enterprise Creator", name, "display name must come from the admin's Preferred name type");

        log.info("Auth bridge user principal: {}", user.principal().encodePrettily());
    }

    @Test
    @Order(2)
    public void testAdminSecurityRolesArePopulated() {
        User user = loginAndBuildUser();
        assertNotNull(user);

        JsonArray roles = user.principal().getJsonArray("roles");
        assertNotNull(roles, "roles array must be present on the principal");
        assertFalse(roles.isEmpty(), "the admin must resolve at least one role");

        assertTrue(roles.contains("Administrators"),
                "roles must include the 'Administrators' folder the admin identity expands to, was: " + roles);
        assertTrue(roles.contains(ADMIN_USER),
                "roles must include the admin's own identity token name ('" + ADMIN_USER + "'), was: " + roles);

        // groups mirror the roles
        JsonArray groups = user.principal().getJsonArray("groups");
        assertNotNull(groups, "groups array must be present");
        assertTrue(groups.contains("Administrators"), "groups must include 'Administrators'");

        log.info("Auth bridge resolved roles: {}", roles);
    }

    @Test
    @Order(3)
    public void testAdminRolesAreExposedAsVertxAuthorizations() {
        User user = loginAndBuildUser();
        assertNotNull(user);

        assertTrue(user.authorizations().verify(RoleBasedAuthorization.create("Administrators")),
                "the 'Administrators' role must be verifiable as a Vert.x RoleBasedAuthorization");
        assertTrue(user.authorizations().verify(RoleBasedAuthorization.create(ADMIN_USER)),
                "the admin's own identity role must be verifiable as a Vert.x RoleBasedAuthorization");
    }

    /**
     * The security management endpoint is annotated {@code @RolesAllowed("Administrators")}. This
     * verifies the now-production {@link SecurityHandler} actually authorizes the admin user built by the
     * auth bridge against that secured resource — i.e. the endpoint is genuinely admin-restricted.
     */
    @Test
    @Order(4)
    public void testAdminIsAuthorizedForSecurityEndpoint() {
        User admin = loginAndBuildUser();
        assertNotNull(admin);

        Method securedMethod = anyMethod(SecurityRestService.class, "list");
        assertTrue(SecurityHandler.isAuthorized(admin, SecurityRestService.class, securedMethod),
                "the admin (holding the 'Administrators' role) must be authorized for the admin-only security endpoint");
    }

    /**
     * Conversely, a caller without the {@code Administrators} role — and an unauthenticated caller — must
     * be denied the admin-only security endpoint.
     */
    @Test
    @Order(5)
    public void testNonAdminIsDeniedForSecurityEndpoint() {
        Method securedMethod = anyMethod(SecurityRestService.class, "create");

        User nonAdmin = User.create(new JsonObject()
                .put("username", "bob")
                .put("roles", new JsonArray().add("user")));
        assertFalse(SecurityHandler.isAuthorized(nonAdmin, SecurityRestService.class, securedMethod),
                "a non-administrator must be denied the admin-only security endpoint (403)");

        assertFalse(SecurityHandler.isAuthorized((User) null, SecurityRestService.class, securedMethod),
                "an unauthenticated caller must be denied the admin-only security endpoint (401)");
    }

    private static Method anyMethod(Class<?> type, String name) {
        return Arrays.stream(type.getMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No method '" + name + "' on " + type.getSimpleName()));
    }
}

