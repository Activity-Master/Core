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
    private Uni<Context> create(Mutiny.StatelessSession session) {
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
                .replaceWith(c);
    }

    @Test void actorEventAndBothRelationshipsArePrivateAndRollBackWithTheDomainTransaction() {
        IEventService<?> eventService = IGuiceContext.get(IEventService.class);
        UUID eventId = factory.withStatelessTransaction((session, tx) -> create(session)
                .chain(c -> eventService.createActorEvent(session, "Profile updated: Private field " + UUID.randomUUID(),
                        c.party, c.scope, c.system, c.systemIdentity)
                        .chain(event -> session.createNativeQuery("select (extract(epoch from warehousecreatedtimestamp)*1000000)::bigint from event.event where eventid=:event", Long.class)
                                .setParameter("event", event.getId()).getSingleResult()
                                .invoke(micros -> assertTrue(micros > 0L))
                                .chain(() -> session.createNativeQuery("""
                                select count(*) from (
                                  select s.securitytokenid from event.eventsecuritytoken s where s.eventsid=:event
                                  union all
                                  select s.securitytokenid from event.eventxeventtypesecuritytoken s
                                    join event.eventxeventtype x on x.eventxeventtypeid=s.eventxeventtypeid where x.eventid=:event
                                  union all
                                  select s.securitytokenid from event.eventxinvolvedpartysecuritytoken s
                                    join event.eventxinvolvedparty x on x.eventxinvolvedpartyid=s.eventxinvolvedpartyid where x.eventid=:event
                                ) grants join security.securitytoken k on k.securitytokenid=grants.securitytokenid
                                where k.securitytokenfriendlyname in ('Everyone','Everywhere','Guests')
                                """, Long.class).setParameter("event", event.getId()).getSingleResult()
                                .invoke(count -> assertEquals(0L, count))
                                .chain(() -> ((IWarehouseCoreTable<?,?,?,?>) event).canRead(session, c.system, UUID.fromString(c.scope.getSecurityToken())))
                                .invoke(allowed -> assertTrue(allowed))
                                .chain(() -> ((IWarehouseCoreTable<?,?,?,?>) event).canRead(session, c.system, UUID.fromString(c.sibling.getSecurityToken())))
                                .invoke(allowed -> assertFalse(allowed)).replaceWith((UUID) event.getId())))))
                .await().atMost(TIMEOUT);
        assertNotNull(eventId);
        var rolledBack = new java.util.concurrent.atomic.AtomicReference<UUID>();
        assertThrows(IllegalStateException.class, () -> factory.withStatelessTransaction((session, tx) -> create(session)
                .chain(c -> eventService.createActorEvent(session, "Failed profile update " + UUID.randomUUID(),
                        c.party, c.scope, c.system, c.systemIdentity))
                .invoke(event -> rolledBack.set((UUID) event.getId()))
                .chain(() -> Uni.createFrom().failure(new IllegalStateException("Reject domain write"))))
                .await().atMost(TIMEOUT));
        assertNotNull(rolledBack.get());
        Long remaining = factory.withStatelessSession(session -> session.createNativeQuery(
                "select count(*) from event.event where eventid=:event", Long.class)
                .setParameter("event", rolledBack.get()).getSingleResult()).await().atMost(TIMEOUT);
        assertEquals(0L, remaining);
    }

    @Test void canonicalPartyAndOrganicSubtypeHaveExactlyRestrictedGrants() {
        Context c = factory.withStatelessTransaction((session, tx) -> create(session)).await().atMost(TIMEOUT);
        factory.withStatelessTransaction((session, tx) -> session.createNativeQuery("""
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

    private Uni<Void> matrix(Mutiny.StatelessSession session, Context c, String table, String column, UUID id) {
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
        Context c = factory.withStatelessTransaction((session, tx) -> create(session)).await().atMost(TIMEOUT);
        var record = (IWarehouseCoreTable<?,?,?,?>) c.party;
        factory.withStatelessTransaction((session, tx) -> access(session, record, c, UUID.fromString(c.scope.getSecurityToken()), true, false)
                .chain(() -> access(session, record, c, UUID.fromString(c.sibling.getSecurityToken()), false, false))
                .chain(() -> access(session, record, c, UUID.fromString(c.parent.getSecurityToken()), false, false))
                .chain(() -> access(session, record, c, UUID.fromString(c.folders.get("guests").getSecurityToken()), false, false))
                .chain(() -> access(session, record, c, UUID.randomUUID(), false, false))
                .chain(() -> access(session, record, c, c.systemIdentity, true, true)))
                .await().atMost(TIMEOUT);
    }
    private Uni<Void> access(Mutiny.StatelessSession session, IWarehouseCoreTable<?,?,?,?> record, Context c, UUID token, boolean read, boolean write) {
        return record.canRead(session, c.system, token).invoke(actual -> assertEquals(read, actual))
                .chain(() -> record.canWrite(session, c.system, token)).invoke(actual -> assertEquals(write, actual)).replaceWithVoid();
    }
}
