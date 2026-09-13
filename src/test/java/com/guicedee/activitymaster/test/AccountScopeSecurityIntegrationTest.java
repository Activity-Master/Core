package com.guicedee.activitymaster.test;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.utils.Pair;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.*;
import java.time.Duration;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/** Full canonical FSDM + Guice bootstrap using the test module's disposable databases. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountScopeSecurityIntegrationTest {
    private static final String ENTERPRISE = "NE1-Onboarding-Security-Test";
    private static final Duration TIMEOUT = Duration.ofMinutes(2);
    private Mutiny.SessionFactory factory;
    private IEnterpriseService<?> enterprises;
    private ISystemsService<?> systems;
    private IInvolvedPartyService<?> parties;
    private ISecurityTokenService<?> security;

    @BeforeAll void start() {
        ActivityMasterConfiguration.get().setApplicationEnterpriseName(ENTERPRISE);
        IGuiceContext.instance();
        factory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
        enterprises = IGuiceContext.get(IEnterpriseService.class); systems = IGuiceContext.get(ISystemsService.class);
        parties = IGuiceContext.get(IInvolvedPartyService.class); security = IGuiceContext.get(ISecurityTokenService.class);
        IGuiceContext.get(IClassificationService.class); IGuiceContext.get(IActiveFlagService.class);
        factory.withStatelessSession(session -> enterprises.getEnterprise(session, ENTERPRISE)
                .onFailure(NoResultException.class).recoverWithUni(failure -> {
                    var enterprise = enterprises.get(); enterprise.setName(ENTERPRISE); enterprise.setDescription("Owned security fixture");
                    return enterprises.createNewEnterprise(session, enterprise);
                }).chain(enterprise -> enterprises.startNewEnterprise(session, ENTERPRISE, "fixture-admin", "Fixture-only-Admin!42")))
                .await().atMost(TIMEOUT); // Bootstrap failure must fail the test, never recover to success.
    }

    private static final class Context {
        ISystems<?, ?> system;
        UUID systemIdentity;
        ISecurityToken<?, ?> parent, scope, sibling;
        IInvolvedParty<?, ?> party;
        Map<String, ISecurityToken<?, ?>> folders = new LinkedHashMap<>();
    }
    private Uni<Context> create(Mutiny.Session session) {
        Context c = new Context(); String id = UUID.randomUUID().toString();
        return enterprises.getEnterprise(session, ENTERPRISE)
                .chain(e -> systems.getActivityMaster(session, e)).invoke(s -> c.system = s)
                .chain(s -> systems.getSecurityIdentityToken(session, s)).invoke(t -> c.systemIdentity = t)
                .chain(() -> security.getRegisteredGuestsFolder(session, c.system, c.systemIdentity)).invoke(p -> c.parent = p)
                .chain(() -> security.create(session, SecurityTokenClassifications.UserGroup.toString(), "ne1-account-" + id,
                        "Account scope", c.system, c.parent, c.systemIdentity)).invoke(s -> c.scope = s)
                .chain(() -> security.create(session, SecurityTokenClassifications.UserGroup.toString(), "ne1-sibling-" + id,
                        "Sibling account scope", c.system, c.parent, c.systemIdentity)).invoke(s -> c.sibling = s)
                .chain(() -> parties.createIdentificationType(session, c.system, "NE1 Onboarding Operation", "Trusted operation ID", c.systemIdentity))
                .chain(() -> parties.createScopeRestricted(session, c.system, UUID.fromString(id),
                        new Pair<>("NE1 Onboarding Operation", id), true, c.scope, c.systemIdentity)).invoke(p -> c.party = p)
                .chain(() -> security.getAdministratorsFolder(session, c.system)).invoke(t -> c.folders.put("administrators", t))
                .chain(() -> security.getSystemsFolder(session, c.system)).invoke(t -> c.folders.put("systems", t))
                .chain(() -> security.getApplicationsFolder(session, c.system)).invoke(t -> c.folders.put("applications", t))
                .chain(() -> security.getPluginsFolder(session, c.system)).invoke(t -> c.folders.put("plugins", t))
                .chain(() -> security.getGuestsFolder(session, c.system)).invoke(t -> c.folders.put("guests", t))
                .chain(() -> security.getEveryoneGroup(session, c.system)).invoke(t -> c.folders.put("everyone", t))
                .chain(() -> security.getEverywhereGroup(session, c.system)).invoke(t -> c.folders.put("everywhere", t))
                .chain(session::flush).replaceWith(c);
    }

    @Test void canonicalPartyAndOrganicSubtypeHaveExactlyRestrictedGrants() {
        Context c = factory.withTransaction((session, tx) -> create(session)).await().atMost(TIMEOUT);
        factory.withTransaction((session, tx) -> session.createNativeQuery("""
                SELECT c.classificationname, d.classificationdataconceptname FROM security.securitytoken t
                JOIN classification.classification c ON c.classificationid=t.securitytokenclassificationid
                JOIN classification.classificationdataconcept d ON d.classificationdataconceptid=c.classificationdataconceptid
                WHERE t.securitytokenid=:scope
                """, Object[].class).setParameter("scope", c.scope.getId()).getSingleResult()
                .invoke(row -> assertArrayEquals(new Object[]{"UserGroup", "SecurityTokenXSecurityToken"}, row))
                .chain(() -> matrix(session, c, "involvedpartysecuritytoken", "involvedpartyid", c.party.getId()))
                .chain(() -> matrix(session, c, "involvedpartyorganicsecuritytoken", "involvedpartyorganicid", c.party.getId())))
                .await().atMost(TIMEOUT);
    }

    private Uni<Void> matrix(Mutiny.Session session, Context c, String table, String column, UUID id) {
        return session.createNativeQuery("SELECT securitytokenid,createallowed,updateallowed,deleteallowed,readallowed FROM party."
                + table + " WHERE " + column + "=:id", Object[].class).setParameter("id", id).getResultList().invoke(rows -> {
            assertEquals(5, rows.size()); Map<UUID, List<Integer>> actual = new HashMap<>();
            rows.forEach(row -> actual.put((UUID) row[0], Arrays.stream(row).skip(1).map(n -> ((Number)n).intValue()).toList()));
            assertEquals(List.of(1,1,1,1), actual.get(c.folders.get("administrators").getId()));
            for (String name : List.of("systems", "applications", "plugins"))
                assertEquals(List.of(1,1,0,1), actual.get(c.folders.get(name).getId()));
            assertEquals(List.of(0,0,0,1), actual.get(c.scope.getId()));
            for (String name : List.of("guests", "everyone", "everywhere")) assertFalse(actual.containsKey(c.folders.get(name).getId()));
        }).replaceWithVoid();
    }

    @Test void realHierarchyAllowsOwnerReadOnlyAndDeniesSiblingAndGuest() {
        Context c = factory.withTransaction((session, tx) -> create(session)).await().atMost(TIMEOUT);
        var record = (IWarehouseCoreTable<?,?,?,?>) c.party;
        factory.withTransaction((session, tx) -> access(session, record, c, UUID.fromString(c.scope.getSecurityToken()), true, false)
                .chain(() -> access(session, record, c, UUID.fromString(c.sibling.getSecurityToken()), false, false))
                .chain(() -> access(session, record, c, UUID.fromString(c.parent.getSecurityToken()), false, false))
                .chain(() -> access(session, record, c, UUID.fromString(c.folders.get("guests").getSecurityToken()), false, false))
                .chain(() -> access(session, record, c, UUID.randomUUID(), false, false))
                .chain(() -> access(session, record, c, c.systemIdentity, true, true)))
                .await().atMost(TIMEOUT);
    }
    private Uni<Void> access(Mutiny.Session session, IWarehouseCoreTable<?,?,?,?> record, Context c, UUID token, boolean read, boolean write) {
        return record.canRead(session, c.system, token).invoke(actual -> assertEquals(read, actual))
                .chain(() -> record.canWrite(session, c.system, token)).invoke(actual -> assertEquals(write, actual)).replaceWithVoid();
    }
}
