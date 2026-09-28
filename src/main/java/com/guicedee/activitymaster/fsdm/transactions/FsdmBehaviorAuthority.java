package com.guicedee.activitymaster.fsdm.transactions;

import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.events.Event;
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
                .chain(installations -> installations.size() == 1
                        ? readableAny(session, system, enterprise, credential, installations) : denied())
                .chain(() -> find(session, system, enterprise, actor, context, provider, behavior, GRANT))
                .chain(grants -> readableAny(session, system, enterprise, credential, grants));
    }

    private Uni<List<UUID>> find(Mutiny.StatelessSession session, UUID system, UUID enterprise,
                           Actor actor, Context context, String provider, String behavior, String type) {
        boolean grant = behavior != null;
        String extra = grant ? """
                join event.eventxclassification b on b.eventid=e.eventid and b.enterpriseid=e.enterpriseid and b.systemid=e.systemid
                join dbo.activeflag fb on fb.activeflagid=b.activeflagid and fb.allowaccess=1
                join classification.classification cb on cb.classificationid=b.classificationid and cb.enterpriseid=e.enterpriseid
                    and cb.systemid=e.systemid and cb.classificationname=:behaviorRole
                join dbo.activeflag fcb on fcb.activeflagid=cb.activeflagid and fcb.allowaccess=1
                join classification.classificationdataconcept db on db.classificationdataconceptid=cb.classificationdataconceptid
                    and db.classificationdataconceptname='EventXClassification'
                join event.eventxinvolvedparty a on a.eventid=e.eventid and a.enterpriseid=e.enterpriseid
                    and a.systemid=e.systemid and a.involvedpartyid=:actor
                join dbo.activeflag fa on fa.activeflagid=a.activeflagid and fa.allowaccess=1
                join classification.classification ca on ca.classificationid=a.classificationid and ca.enterpriseid=e.enterpriseid
                    and ca.systemid=e.systemid and ca.classificationname=:actorRole
                join dbo.activeflag fca on fca.activeflagid=ca.activeflagid and fca.allowaccess=1
                join classification.classificationdataconcept da on da.classificationdataconceptid=ca.classificationdataconceptid
                    and da.classificationdataconceptname='EventXInvolvedParty'
                """ : "";
        String grantCondition = grant ? """
                and b.value=:behavior and b.effectivefromdate<=statement_timestamp() and b.effectivetodate>statement_timestamp()
                and cb.effectivefromdate<=statement_timestamp() and cb.effectivetodate>statement_timestamp()
                and a.effectivefromdate<=statement_timestamp() and a.effectivetodate>statement_timestamp()
                and ca.effectivefromdate<=statement_timestamp() and ca.effectivetodate>statement_timestamp()
                """ : "";
        String locks = grant ? "for share of e,f,xt,t,p,r,o,b,a" : "for share of e,f,xt,t,p,r,o";
        var query = session.createNativeQuery("""
                select e.eventid from event.event e
                join dbo.activeflag f on f.activeflagid=e.activeflagid and f.allowaccess=1
                join event.eventxeventtype xt on xt.eventid=e.eventid and xt.enterpriseid=e.enterpriseid and xt.systemid=e.systemid
                join dbo.activeflag fxt on fxt.activeflagid=xt.activeflagid and fxt.allowaccess=1
                join event.eventtype t on t.eventtypeid=xt.eventtypeid and t.enterpriseid=e.enterpriseid
                    and t.systemid=e.systemid and t.eventtypename=:type
                join dbo.activeflag ft on ft.activeflagid=t.activeflagid and ft.allowaccess=1
                join event.eventxclassification p on p.eventid=e.eventid and p.enterpriseid=e.enterpriseid and p.systemid=e.systemid
                join dbo.activeflag fp on fp.activeflagid=p.activeflagid and fp.allowaccess=1
                join classification.classification cp on cp.classificationid=p.classificationid and cp.enterpriseid=e.enterpriseid
                    and cp.systemid=e.systemid and cp.classificationname=:providerRole
                join dbo.activeflag fcp on fcp.activeflagid=cp.activeflagid and fcp.allowaccess=1
                join classification.classificationdataconcept dp on dp.classificationdataconceptid=cp.classificationdataconceptid
                    and dp.classificationdataconceptname='EventXClassification'
                join event.eventxclassification r on r.eventid=e.eventid and r.enterpriseid=e.enterpriseid and r.systemid=e.systemid
                join dbo.activeflag fr on fr.activeflagid=r.activeflagid and fr.allowaccess=1
                join classification.classification cr on cr.classificationid=r.classificationid and cr.enterpriseid=e.enterpriseid
                    and cr.systemid=e.systemid and cr.classificationname=:realmRole
                join dbo.activeflag fcr on fcr.activeflagid=cr.activeflagid and fcr.allowaccess=1
                join classification.classificationdataconcept dr on dr.classificationdataconceptid=cr.classificationdataconceptid
                    and dr.classificationdataconceptname='EventXClassification'
                join event.eventxclassification o on o.eventid=e.eventid and o.enterpriseid=e.enterpriseid and o.systemid=e.systemid
                join dbo.activeflag fo on fo.activeflagid=o.activeflagid and fo.allowaccess=1
                join classification.classification co on co.classificationid=o.classificationid and co.enterpriseid=e.enterpriseid
                    and co.systemid=e.systemid and co.classificationname=:ownerRole
                join dbo.activeflag fco on fco.activeflagid=co.activeflagid and fco.allowaccess=1
                join classification.classificationdataconcept do_ on do_.classificationdataconceptid=co.classificationdataconceptid
                    and do_.classificationdataconceptname='EventXClassification'
                %s
                where e.enterpriseid=:enterprise and e.systemid=:system and p.value=:provider
                  and r.value=:realm and o.value=:owner
                  and e.effectivefromdate<=statement_timestamp() and e.effectivetodate>statement_timestamp()
                  and xt.effectivefromdate<=statement_timestamp() and xt.effectivetodate>statement_timestamp()
                  and t.effectivefromdate<=statement_timestamp() and t.effectivetodate>statement_timestamp()
                  and p.effectivefromdate<=statement_timestamp() and p.effectivetodate>statement_timestamp()
                  and cp.effectivefromdate<=statement_timestamp() and cp.effectivetodate>statement_timestamp()
                  and r.effectivefromdate<=statement_timestamp() and r.effectivetodate>statement_timestamp()
                  and cr.effectivefromdate<=statement_timestamp() and cr.effectivetodate>statement_timestamp()
                  and o.effectivefromdate<=statement_timestamp() and o.effectivetodate>statement_timestamp()
                  and co.effectivefromdate<=statement_timestamp() and co.effectivetodate>statement_timestamp()
                %s
                %s
                """.formatted(extra, grantCondition, locks), UUID.class)
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

    private Uni<Void> readableAny(Mutiny.StatelessSession session, UUID system, UUID enterprise,
                                  UUID credential, List<UUID> eventIds) {
        Systems owner = new Systems().setId(system).setEnterpriseID(new Enterprise().setId(enterprise));
        Uni<Boolean> allowed = Uni.createFrom().item(false);
        for (UUID eventId : eventIds) {
            allowed = allowed.chain(previous -> previous ? Uni.createFrom().item(true)
                    : new Event().setId(eventId).canRead(session, owner, credential));
        }
        return allowed
                .chain(permitted -> permitted ? Uni.createFrom().voidItem() : denied());
    }

    private static <T> Uni<T> denied() {
        return Uni.createFrom().failure(new SecurityException("Scoped behavior unavailable"));
    }
}
