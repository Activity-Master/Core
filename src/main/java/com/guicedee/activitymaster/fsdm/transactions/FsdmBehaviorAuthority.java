package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Actor;
import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Context;
import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.Realm;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;
import java.util.List;

/** Current provider installation and behavior grants stored as secured FSDM Events.
 * The authenticated host supplies the actor, scope and identifying credential.
 * Provisioning these Events is an authorized host operation; discovery grants nothing.
 */
public final class FsdmBehaviorAuthority {
    public static final String INSTALLATION = "Scoped Provider Installation";
    public static final String GRANT = "Scoped Behavior Grant";
    public static String type(UUID system, String name) { return system + ":" + name; }
    public static String role(UUID system, String name) { return system + ":" + name; }

    public Uni<Void> check(Mutiny.StatelessSession session, UUID system, UUID enterprise,
                           Actor actor, Context context, UUID credential, String provider, String action) {
        if (session == null || system == null || enterprise == null || actor == null || context == null
                || credential == null || provider == null || provider.isBlank() || action == null || action.isBlank())
            return denied();
        if (context.realm() == Realm.WORK ? !enterprise.equals(context.ownerId())
                : !actor.partyId().equals(context.ownerId())) return denied();
        String behavior = system + ":" + action;
        return find(session, system, enterprise, actor, context, provider, null, INSTALLATION)
                .chain(installations -> installations.size()!=1 ? denied()
                        : find(session,system,enterprise,actor,context,provider,behavior,GRANT)
                                .chain(grants -> readableAuthority(session,system,enterprise,credential,installations.getFirst(),grants)));
    }

    private Uni<List<UUID>> find(Mutiny.StatelessSession session, UUID system, UUID enterprise,
                           Actor actor, Context context, String provider, String behavior, String type) {
        boolean grant = behavior != null;
        String extra = grant ? """
                join event.eventxclassification b on b.eventid=e.eventid and b.enterpriseid=e.enterpriseid and b.systemid=e.systemid
                join event.eventxinvolvedparty a on a.eventid=e.eventid and a.enterpriseid=e.enterpriseid
                    and a.systemid=e.systemid and a.involvedpartyid=:actor
                """ : "";
        String grantCondition = grant ? " and b.value=:behavior and " + current("b") + " and " + current("a")
                + " and b.classificationid=" + roleIds("behaviorRole","EventXClassification")
                + " and a.classificationid=" + roleIds("actorRole","EventXInvolvedParty") : "";
        // Preserve exactly the original row-lock set. Metadata/flag IDs only constrain
        // those rows; they do not replace either lock read or the fresh token read below.
        String locks = grant ? "for share of e,f,xt,t,p,r,o,b,a" : "for share of e,f,xt,t,p,r,o";
        var query = session.createNativeQuery("""
                with roles as materialized (
                  select c.classificationid,c.classificationname,d.classificationdataconceptname
                  from classification.classification c
                  join classification.classificationdataconcept d on d.classificationdataconceptid=c.classificationdataconceptid
                  where c.enterpriseid=:enterprise and c.systemid=:system
                    and c.classificationname in (:providerRole,:realmRole,:ownerRole%s)
                    and %s
                )
                select e.eventid from event.event e
                join dbo.activeflag f on f.activeflagid=e.activeflagid and f.allowaccess=1
                join event.eventxeventtype xt on xt.eventid=e.eventid and xt.enterpriseid=e.enterpriseid and xt.systemid=e.systemid
                join event.eventtype t on t.eventtypeid=xt.eventtypeid and t.enterpriseid=e.enterpriseid
                    and t.systemid=e.systemid and t.eventtypename=:type
                join event.eventxclassification p on p.eventid=e.eventid and p.enterpriseid=e.enterpriseid and p.systemid=e.systemid
                join event.eventxclassification r on r.eventid=e.eventid and r.enterpriseid=e.enterpriseid and r.systemid=e.systemid
                join event.eventxclassification o on o.eventid=e.eventid and o.enterpriseid=e.enterpriseid and o.systemid=e.systemid
                %s
                where e.enterpriseid=:enterprise and e.systemid=:system and p.value=:provider
                  and r.value=:realm and o.value=:owner
                  and e.effectivefromdate<=statement_timestamp() and e.effectivetodate>statement_timestamp()
                  and %s and %s and %s and %s and %s
                  and p.classificationid=%s and r.classificationid=%s and o.classificationid=%s
                %s
                %s
                """.formatted(grant ? ",:behaviorRole,:actorRole" : "", current("c"), extra,
                        current("xt"),current("t"),current("p"),current("r"),current("o"),
                        roleIds("providerRole","EventXClassification"),roleIds("realmRole","EventXClassification"),
                        roleIds("ownerRole","EventXClassification"),grantCondition,locks), UUID.class)
                .setParameter("type", type(system,type)).setParameter("enterprise", enterprise).setParameter("system", system)
                .setParameter("providerRole", role(system,"ScopedProvider"))
                .setParameter("realmRole", role(system,"ScopedRealm"))
                .setParameter("ownerRole", role(system,"ScopedOwner"))
                .setParameter("provider", provider).setParameter("realm", context.realm().name())
                .setParameter("owner", context.ownerId().toString());
        if (grant) query.setParameter("behavior", behavior).setParameter("actor", actor.partyId())
                .setParameter("behaviorRole", role(system,"ScopedBehavior"))
                .setParameter("actorRole", role(system,"ScopedActor"));
        return query.getResultList().chain(rows -> rows.isEmpty()
                ? denied() : Uni.createFrom().item(rows.stream().distinct().toList()));
    }

    private static String roleIds(String parameter, String concept) {
        return "any(array(select classificationid from roles where classificationname=:" + parameter
                + " and classificationdataconceptname='" + concept + "'))";
    }
    private static String current(String alias) {
        // Match the existing flag predicate exactly; the event's f row remains explicitly locked.
        return alias + ".effectivefromdate<=statement_timestamp() and " + alias + ".effectivetodate>statement_timestamp()"
                + " and " + alias + ".activeflagid=any(array(select activeflagid from dbo.activeflag where allowaccess=1))";
    }

    private Uni<Void> readableAuthority(Mutiny.StatelessSession session, UUID system, UUID enterprise,
                                         UUID credential, UUID installation, List<UUID> grants) {
        Systems owner = new Systems().setId(system).setEnterpriseID(new Enterprise().setId(enterprise));
        // Both event-lock reads completed: expand current tokens now, then read only
        // their row grants. No entity hydration or per-event token expansion.
        return com.guicedee.activitymaster.fsdm.client.services.ApplicableTokenQuery.prepare(session,owner,credential)
                .chain(tokens -> {
                    var events = new java.util.HashSet<>(grants); events.add(installation);
                    var query = session.createNativeQuery(tokens.cte() + "select distinct g.eventsid from event.eventsecuritytoken g "
                                    + "where g.eventsid in (:events) and g.enterpriseid=:enterprise "
                                    + "and g.securitytokenid in (select securitytokenid from applicable) "
                                    + "and g.readallowed=1 and g.effectivefromdate<=statement_timestamp() and g.effectivetodate>statement_timestamp()",UUID.class)
                            .setParameter("events",events).setParameter("enterprise",enterprise);
                    return tokens.bind(query).getResultList().chain(readable -> readable.contains(installation) && grants.stream().anyMatch(readable::contains)
                                    ? Uni.createFrom().voidItem() : denied());
                });
    }

    private static <T> Uni<T> denied() {
        return Uni.createFrom().failure(new SecurityException("Scoped behavior unavailable"));
    }
}
