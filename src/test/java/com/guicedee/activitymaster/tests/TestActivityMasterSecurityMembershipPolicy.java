package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SecurityAccessException;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the canonical security-hierarchy <strong>membership policy</strong> enforced by
 * {@link ISecurityTokenService#link}.
 *
 * <p>Once the base hierarchy is built the type folders are locked down:</p>
 * <ul>
 *     <li>The <strong>Systems</strong> folder accepts only {@code System}-typed tokens — a group/user
 *         cannot be added to it (so "groups can add groups and users <em>except</em> for systems").</li>
 *     <li>The <strong>Applications</strong> folder accepts only {@code Application}-typed tokens
 *         (always involved parties).</li>
 *     <li>A {@code System}-typed token may not be parented under a generic group.</li>
 *     <li>A generic group may freely add further groups/users.</li>
 * </ul>
 */
@Log4j2
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestActivityMasterSecurityMembershipPolicy {

    protected Mutiny.SessionFactory sessionFactory;

    @BeforeAll
    public void setup() {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(TestEnterprise.name());
        IGuiceContext.instance();
        sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        assertNotNull(sessionFactory, "SessionFactory should not be null");

        IEnterpriseService<?> enterpriseService = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> systemsService = IGuiceContext.get(ISystemsService.class);
        IGuiceContext.get(IClassificationService.class);
        IGuiceContext.get(IActiveFlagService.class);
        IGuiceContext.get(ISecurityTokenService.class);

        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.getEnterprise(session, TestEnterprise.name())
                                .onFailure().recoverWithUni(t -> {
                                    var ent = enterpriseService.get();
                                    ent.setName(TestEnterprise.name());
                                    ent.setDescription("Enterprise for Membership Policy Testing");
                                    return enterpriseService.createNewEnterprise(session, ent);
                                })
                                .chain(ent -> systemsService.getActivityMaster(session, (IEnterprise<?, ?>) ent)
                                        .onFailure().recoverWithUni(t -> systemsService.create(session, (IEnterprise<?, ?>) ent,
                                                ISystemsService.ActivityMasterSystemName, "Activity Master System")))
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));

        sessionFactory.withSession(session ->
                session.withTransaction(tx ->
                        enterpriseService.startNewEnterprise(session, TestEnterprise.name(), "admin", "!@adminadmin")
                                .onFailure().recoverWithItem(e -> null)
                                .replaceWith(Uni.createFrom().voidItem())
                )
        ).await().atMost(Duration.of(2, ChronoUnit.MINUTES));
    }

    /** Resolved context for a single link attempt. */
    private static final class Ctx {
        ISystems<?, ?> system;
        ISecurityToken<?, ?> systemsFolder;
        ISecurityToken<?, ?> applicationsFolder;
        ISecurityToken<?, ?> everyone;
        IClassification<?, ?> userGroupClass;
        IClassification<?, ?> systemClass;
        ISecurityToken<?, ?> childGroup;   // a fresh UserGroup-typed token
        ISecurityToken<?, ?> childSystem;  // a fresh System-typed token
    }

    private Uni<Ctx> resolveCtx(Mutiny.Session session, String runId) {
        IEnterpriseService<?> es = IGuiceContext.get(IEnterpriseService.class);
        ISystemsService<?> ss = IGuiceContext.get(ISystemsService.class);
        IClassificationService<?> cs = IGuiceContext.get(IClassificationService.class);
        ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
        Ctx ctx = new Ctx();

        return es.getEnterprise(session, TestEnterprise.name())
                .chain(ent -> ss.getActivityMaster(session, (IEnterprise<?, ?>) ent))
                .chain(sys -> {
                    ctx.system = (ISystems<?, ?>) sys;
                    return sec.getSystemsFolder(session, ctx.system).invoke(t -> ctx.systemsFolder = t);
                })
                .chain(() -> sec.getApplicationsFolder(session, ctx.system).invoke(t -> ctx.applicationsFolder = t))
                .chain(() -> sec.getEveryoneGroup(session, ctx.system).invoke(t -> ctx.everyone = t))
                .chain(() -> cs.find(session, SecurityTokenClassifications.UserGroup, ctx.system)
                        .invoke(c -> ctx.userGroupClass = (IClassification<?, ?>) c))
                .chain(() -> cs.find(session, UserGroupSecurityTokenClassifications.System, ctx.system)
                        .invoke(c -> ctx.systemClass = (IClassification<?, ?>) c))
                .chain(() -> sec.create(session, SecurityTokenClassifications.UserGroup.toString(),
                                "PolicyGroup_" + runId, "test membership group", ctx.system)
                        .invoke(t -> ctx.childGroup = t))
                .chain(() -> sec.create(session, UserGroupSecurityTokenClassifications.System.toString(),
                                "PolicySystem_" + runId, "test system token", ctx.system)
                        .invoke(t -> ctx.childSystem = t))
                .replaceWith(ctx);
    }

    /** Reactively runs the given link and emits {@code true} when the membership policy rejects it. */
    private Uni<Boolean> linkRejected(Mutiny.Session session, ISecurityTokenService<?> sec,
                                      ISecurityToken<?, ?> parent, ISecurityToken<?, ?> child, IClassification<?, ?> cls) {
        return sec.link(session, parent, child, cls)
                .replaceWith(Boolean.FALSE)
                .onFailure(SecurityAccessException.class).recoverWithItem(Boolean.TRUE);
    }

    @Test
    @Order(1)
    public void testGroupCanAddGroupsAndUsers() {
        final String runId = Long.toHexString(System.nanoTime());
        Boolean ok = sessionFactory.withTransaction(session ->
                resolveCtx(session, runId).chain(ctx -> {
                    ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                    // group -> group with the UserGroup type is permitted
                    return sec.link(session, ctx.everyone, ctx.childGroup, ctx.userGroupClass)
                            .replaceWith(Boolean.TRUE);
                })
        ).await().atMost(Duration.ofMinutes(2));
        assertTrue(ok, "A generic group must accept further group/user members");
    }

    @Test
    @Order(2)
    public void testSystemsFolderRejectsNonSystemTokens() {
        final String runId = Long.toHexString(System.nanoTime());
        Boolean rejected = sessionFactory.withTransaction(session ->
                resolveCtx(session, runId).chain(ctx -> {
                    ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                    return linkRejected(session, sec, ctx.systemsFolder, ctx.childGroup, ctx.userGroupClass);
                })
        ).await().atMost(Duration.ofMinutes(2));
        assertTrue(rejected, "The Systems folder must reject a group/user token (groups add everywhere except systems)");
    }

    @Test
    @Order(3)
    public void testApplicationsFolderRejectsNonApplicationTokens() {
        final String runId = Long.toHexString(System.nanoTime());
        Boolean rejected = sessionFactory.withTransaction(session ->
                resolveCtx(session, runId).chain(ctx -> {
                    ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                    return linkRejected(session, sec, ctx.applicationsFolder, ctx.childGroup, ctx.userGroupClass);
                })
        ).await().atMost(Duration.ofMinutes(2));
        assertTrue(rejected, "The Applications folder must reject non-Application (group/user) tokens");
    }

    @Test
    @Order(4)
    public void testSystemsFolderAcceptsSystemTokens() {
        final String runId = Long.toHexString(System.nanoTime());
        Boolean ok = sessionFactory.withTransaction(session ->
                resolveCtx(session, runId).chain(ctx -> {
                    ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                    return sec.link(session, ctx.systemsFolder, ctx.childSystem, ctx.systemClass)
                            .replaceWith(Boolean.TRUE);
                })
        ).await().atMost(Duration.ofMinutes(2));
        assertTrue(ok, "The Systems folder must accept a System-typed token");
    }

    @Test
    @Order(5)
    public void testSystemTokenRejectedUnderGenericGroup() {
        final String runId = Long.toHexString(System.nanoTime());
        Boolean rejected = sessionFactory.withTransaction(session ->
                resolveCtx(session, runId).chain(ctx -> {
                    ISecurityTokenService<?> sec = IGuiceContext.get(ISecurityTokenService.class);
                    return linkRejected(session, sec, ctx.everyone, ctx.childSystem, ctx.systemClass);
                })
        ).await().atMost(Duration.ofMinutes(2));
        assertTrue(rejected, "A System-typed token may only be parented under the Systems folder (or root), not a generic group");
    }
}


