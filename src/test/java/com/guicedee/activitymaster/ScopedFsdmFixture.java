package com.guicedee.activitymaster;

import com.guicedee.activitymaster.fsdm.transactions.FsdmBehaviorAuthority;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** Test-only canonical FSDM rows for scoped provider admission. No space schema. */
public final class ScopedFsdmFixture {
    @FunctionalInterface public interface Sql { void run(String statement) throws Exception; }

    private final Sql sql;
    private final UUID enterprise;
    private final UUID credential;

    public ScopedFsdmFixture(Sql sql, UUID enterprise, UUID credential) {
        this.sql = sql;
        this.enterprise = enterprise;
        this.credential = credential;
    }

    public UUID install(UUID system, String realm, UUID owner, String provider) throws Exception {
        UUID event = id(system, realm, owner, provider, "installation", null);
        event(system, event, "Scoped Provider Installation");
        classify(system, event, "ScopedProvider", provider);
        classify(system, event, "ScopedRealm", realm);
        classify(system, event, "ScopedOwner", owner.toString());
        return event;
    }

    public UUID grant(UUID system, String realm, UUID owner, String provider,
                      UUID actor, String action) throws Exception {
        UUID event = grantId(system, realm, owner, provider, actor, action);
        event(system, event, "Scoped Behavior Grant");
        classify(system, event, "ScopedProvider", provider);
        classify(system, event, "ScopedRealm", realm);
        classify(system, event, "ScopedOwner", owner.toString());
        classify(system, event, "ScopedBehavior", system + ":" + action);
        sql.run("""
                insert into event.eventxinvolvedparty
                    (eventxinvolvedpartyid,effectivefromdate,effectivetodate,warehousecreatedtimestamp,
                     warehousefromdate,warehouselastupdatedtimestamp,originalsourcesystemuniqueid,value,
                     activeflagid,enterpriseid,systemid,originalsourcesystemid,classificationid,eventid,involvedpartyid)
                select '%s',statement_timestamp()-interval '1 minute','9999-12-31',statement_timestamp(),current_date,
                       statement_timestamp(),'%s','1',f.activeflagid,'%s','%s','%s',c.classificationid,'%s','%s'
                from classification.classification c
                join classification.classificationdataconcept d on d.classificationdataconceptid=c.classificationdataconceptid
                cross join lateral (select activeflagid from dbo.activeflag where enterpriseid='%s' and allowaccess=1 limit 1) f
                where c.classificationname='%s' and d.classificationdataconceptname='EventXInvolvedParty'
                  and c.enterpriseid='%s' and c.systemid='%s' limit 1
                """.formatted(UUID.randomUUID(), event, enterprise, system, system, event, actor,
                enterprise, FsdmBehaviorAuthority.role(system,"ScopedActor"), enterprise, system));
        return event;
    }

    public static UUID grantId(UUID system, String realm, UUID owner, String provider,
                               UUID actor, String action) {
        return id(system, realm, owner, provider, action, actor);
    }

    public static UUID installationId(UUID system, String realm, UUID owner, String provider) {
        return id(system, realm, owner, provider, "installation", null);
    }

    public void disable(UUID event) throws Exception {
        sql.run("update event.event set effectivetodate=statement_timestamp()-interval '1 second' where eventid='" + event + "'");
    }

    public void enable(UUID event) throws Exception {
        sql.run("update event.event set effectivetodate='9999-12-31' where eventid='" + event + "'");
    }

    private void event(UUID system, UUID id, String type) throws Exception {
        sql.run("""
                insert into event.event
                    (eventid,effectivefromdate,effectivetodate,warehousecreatedtimestamp,warehousefromdate,
                     warehouselastupdatedtimestamp,originalsourcesystemuniqueid,dayid,hourid,minuteid,
                     activeflagid,enterpriseid,systemid,originalsourcesystemid)
                select '%s',statement_timestamp()-interval '1 minute','9999-12-31',statement_timestamp(),current_date,
                       statement_timestamp(),'%s',0,0,0,f.activeflagid,'%s','%s','%s'
                from dbo.activeflag f where f.enterpriseid='%s' and f.allowaccess=1 limit 1
                """.formatted(id, id, enterprise, system, system, enterprise));
        sql.run("""
                insert into event.eventxeventtype
                    (eventxeventtypeid,effectivefromdate,effectivetodate,warehousecreatedtimestamp,warehousefromdate,
                     warehouselastupdatedtimestamp,originalsourcesystemuniqueid,value,activeflagid,enterpriseid,
                     systemid,originalsourcesystemid,classificationid,eventid,eventtypeid)
                select '%s',statement_timestamp()-interval '1 minute','9999-12-31',statement_timestamp(),current_date,
                       statement_timestamp(),'%s','1',f.activeflagid,'%s','%s','%s',c.classificationid,'%s',t.eventtypeid
                from event.eventtype t
                join classification.classification c on c.classificationname='%s'
                    and c.enterpriseid=t.enterpriseid and c.systemid=t.systemid
                join classification.classificationdataconcept d on d.classificationdataconceptid=c.classificationdataconceptid
                    and d.classificationdataconceptname='EventXEventType'
                cross join lateral (select activeflagid from dbo.activeflag where enterpriseid='%s' and allowaccess=1 limit 1) f
                where t.eventtypename='%s' and t.enterpriseid='%s' and t.systemid='%s' limit 1
                """.formatted(UUID.randomUUID(), id, enterprise, system, system,
                id, FsdmBehaviorAuthority.role(system,"ScopedEventType"),
                enterprise, FsdmBehaviorAuthority.type(system,type), enterprise, system));
        sql.run("""
                insert into event.eventsecuritytoken
                    (eventssecuritytokenid,effectivefromdate,effectivetodate,warehousecreatedtimestamp,warehousefromdate,
                     warehouselastupdatedtimestamp,createallowed,deleteallowed,originalsourcesystemuniqueid,
                     readallowed,updateallowed,activeflagid,enterpriseid,originalsourcesystemid,
                     securitytokenid,systemid,eventsid)
                select '%s',statement_timestamp()-interval '1 minute','9999-12-31',statement_timestamp(),current_date,
                       statement_timestamp(),0,0,'%s',1,0,f.activeflagid,'%s','%s',st.securitytokenid,'%s','%s'
                from security.securitytoken st
                cross join lateral (select activeflagid from dbo.activeflag where enterpriseid='%s' and allowaccess=1 limit 1) f
                where st.securitytoken='%s' and st.enterpriseid='%s' limit 1
                """.formatted(UUID.randomUUID(), id, enterprise, system, system, id,
                enterprise, credential, enterprise));
    }

    private void classify(UUID system, UUID event, String name, String value) throws Exception {
        if (!value.matches("[A-Za-z0-9._:-]{1,200}")) throw new IllegalArgumentException("Unsafe fixture value");
        sql.run("""
                insert into event.eventxclassification
                    (eventxclassificationid,effectivefromdate,effectivetodate,warehousecreatedtimestamp,warehousefromdate,
                     warehouselastupdatedtimestamp,originalsourcesystemuniqueid,value,activeflagid,enterpriseid,
                     systemid,originalsourcesystemid,classificationid,eventid)
                select '%s',statement_timestamp()-interval '1 minute','9999-12-31',statement_timestamp(),current_date,
                       statement_timestamp(),'%s','%s',f.activeflagid,'%s','%s','%s',c.classificationid,'%s'
                from classification.classification c
                join classification.classificationdataconcept d on d.classificationdataconceptid=c.classificationdataconceptid
                    and d.classificationdataconceptname='EventXClassification'
                cross join lateral (select activeflagid from dbo.activeflag where enterpriseid='%s' and allowaccess=1 limit 1) f
                where c.classificationname='%s' and c.enterpriseid='%s' and c.systemid='%s' limit 1
                """.formatted(UUID.randomUUID(), event, value, enterprise, system, system, event,
                enterprise, FsdmBehaviorAuthority.role(system,name), enterprise, system));
    }

    private static UUID id(UUID system, String realm, UUID owner, String provider, String action, UUID actor) {
        return UUID.nameUUIDFromBytes(("scoped-fixture:" + system + ':' + realm + ':' + owner + ':' + provider
                + ':' + action + ':' + actor).getBytes(StandardCharsets.UTF_8));
    }
}
