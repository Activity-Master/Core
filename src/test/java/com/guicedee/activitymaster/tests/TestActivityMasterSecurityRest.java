package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.rest.security.*;
import com.guicedee.activitymaster.fsdm.rest.security.SecurityRestService;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.LogUtils;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.ForbiddenException;
import org.apache.logging.log4j.Level;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link SecurityRestService} — the comprehensive security-structure management
 * endpoint. Exercises the resource the same way the Vert.x router does (resolve from
 * {@link IGuiceContext}, invoke the method with enterprise + requesting system + DTO, await the Uni).
 * <p>
 * The headline guarantee verified here: the {@code Systems}, {@code Applications} and {@code Plugins}
 * folders — and {@code System}/{@code Application}/{@code Plugin}-typed tokens — are library-managed and
 * cannot be created, updated, deleted, granted, or used as a membership target through this endpoint.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterSecurityRest
{
    private static final String SYSTEM = ISystemsService.ActivityMasterSystemName;

    private Mutiny.SessionFactory sessionFactory;

    private String enterprise()
    {
        return TestEnterprise.name();
    }

    @BeforeAll
    public void setup()
    {
        LogUtils.addConsoleLogger(Level.INFO);
        ActivityMasterConfiguration.get()
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
                            ent.setDescription("Enterprise for security REST tests");
                            return enterpriseService.createNewEnterprise(ss, ent);
                        })
                        .chain(ent -> enterpriseService.startNewEnterprise(ss, TestEnterprise.name(), "admin", "adminadmin!@"))
                        .onFailure().recoverWithItem(e -> null)
                        .eventually(ss::close))
                .await().atMost(Duration.ofMinutes(2));
    }

    private static <T> T await(Uni<T> uni)
    {
        return uni.await().atMost(Duration.ofMinutes(2));
    }

    private SecurityRestService rest()
    {
        SecurityRestService rest = IGuiceContext.get(SecurityRestService.class);
        assertNotNull(rest, "SecurityRestService should be injectable");
        return rest;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Manageable structures — happy path
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    public void createAndFindGroup()
    {
        SecurityRestService rest = rest();

        SecurityTokenCreateDTO create = new SecurityTokenCreateDTO();
        create.name = "REST_SecGroup_1";
        create.description = "rest security group";
        create.type = "UserGroup";

        SecurityTokenDTO created = await(rest.create(enterprise(), SYSTEM, create));
        assertNotNull(created, "create should echo a DTO");
        assertNotNull(created.securityTokenId, "create should return a generated security token id");
        assertEquals("REST_SecGroup_1", created.name, "create should echo the submitted name");
        assertEquals("UserGroup", created.type, "create should echo the resolved type");
        assertFalse(Boolean.TRUE.equals(created.managed), "a UserGroup is not a library-managed structure");

        SecurityTokenFindDTO find = new SecurityTokenFindDTO();
        find.name = "REST_SecGroup_1";
        find.includes = List.of(SecurityTokenDataIncludes.Members, SecurityTokenDataIncludes.MemberOf);

        SecurityTokenDTO found = await(rest.find(enterprise(), SYSTEM, find));
        assertNotNull(found, "find should return the group");
        assertEquals("REST_SecGroup_1", found.name, "find should return the persisted name");
    }

    @Test
    @Order(2)
    public void addAndListMember()
    {
        SecurityRestService rest = rest();

        SecurityTokenCreateDTO child = new SecurityTokenCreateDTO();
        child.name = "REST_SecGroup_Child";
        child.description = "child group";
        child.type = "UserGroup";
        await(rest.create(enterprise(), SYSTEM, child));

        SecurityTokenMembershipDTO add = new SecurityTokenMembershipDTO();
        add.parentName = "REST_SecGroup_1";
        add.childName = "REST_SecGroup_Child";
        SecurityTokenDTO parent = await(rest.addMember(enterprise(), SYSTEM, add));
        assertNotNull(parent.members, "members should be hydrated after add");
        assertTrue(parent.members.stream().anyMatch(m -> "REST_SecGroup_Child".equals(m.name)),
                "the child should appear among the parent's members");
    }

    @Test
    @Order(3)
    public void resolveApplicableForAdmin()
    {
        SecurityRestService rest = rest();

        SecurityTokenResolveDTO resolve = new SecurityTokenResolveDTO();
        resolve.name = "admin";

        ApplicableTokensDTO applicable = await(rest.resolve(enterprise(), SYSTEM, resolve));
        assertNotNull(applicable, "resolve should return a DTO");
        assertNotNull(applicable.applicableIds, "resolve should return applicable ids");
        assertFalse(applicable.applicableIds.isEmpty(), "admin should expand to at least its own token");
        assertTrue(applicable.applicable.stream().anyMatch(r -> "Administrators".equalsIgnoreCase(r.name)),
                "admin should expand to include the Administrators folder");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Library-managed structures — must be rejected (the headline guarantee)
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    @Order(10)
    public void cannotCreateSystemApplicationOrPluginTypes()
    {
        SecurityRestService rest = rest();
        for (String managedType : List.of("System", "Application", "Plugin"))
        {
            SecurityTokenCreateDTO create = new SecurityTokenCreateDTO();
            create.name = "REST_Illegal_" + managedType;
            create.description = "should be rejected";
            create.type = managedType;
            assertThrows(ForbiddenException.class,
                    () -> await(rest.create(enterprise(), SYSTEM, create)),
                    managedType + "-typed tokens are library-managed and must be rejected (403)");
        }
    }

    @Test
    @Order(11)
    public void cannotManageTheApplicationsFolder()
    {
        SecurityRestService rest = rest();

        SecurityTokenUpdateDTO update = new SecurityTokenUpdateDTO();
        update.name = "Applications";
        update.description = "trying to tamper";
        assertThrows(ForbiddenException.class,
                () -> await(rest.update(enterprise(), SYSTEM, update)),
                "the Applications folder is library-managed and must not be updatable");

        SecurityTokenFindDTO delete = new SecurityTokenFindDTO();
        delete.name = "Plugins";
        assertThrows(ForbiddenException.class,
                () -> await(rest.delete(enterprise(), SYSTEM, delete)),
                "the Plugins folder is library-managed and must not be deletable");
    }

    @Test
    @Order(12)
    public void cannotAddMembersIntoManagedFolders()
    {
        SecurityRestService rest = rest();

        SecurityTokenMembershipDTO add = new SecurityTokenMembershipDTO();
        add.parentName = "System";          // the Systems folder
        add.childName = "REST_SecGroup_1";
        assertThrows(ForbiddenException.class,
                () -> await(rest.addMember(enterprise(), SYSTEM, add)),
                "adding members into the Systems folder must be rejected (403)");
    }
}

