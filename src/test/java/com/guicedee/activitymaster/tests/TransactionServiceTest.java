package com.guicedee.activitymaster.tests;

import com.guicedee.activitymaster.fsdm.transactions.ActivityScope.*;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.*;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {
    static PostgreSQLContainer<?> postgres;
    static Vertx vertx;
    static Pool pool;
    UUID party, enterprise, system, event, debitType, creditType, first, second;
    TransactionSqlFixture service;
    TransactionSqlFixture.Call call;

    @BeforeAll static void database() throws Exception {
        postgres = new PostgreSQLContainer<>("postgres:17-alpine"); postgres.start();
        vertx = Vertx.vertx();
        pool = PgBuilder.pool().using(vertx).connectingTo(new PgConnectOptions()
                .setHost(postgres.getHost()).setPort(postgres.getMappedPort(5432))
                .setDatabase(postgres.getDatabaseName()).setUser(postgres.getUsername())
                .setPassword(postgres.getPassword())).with(new PoolOptions().setMaxSize(4)).build();
        sql("""
                CREATE SCHEMA party; CREATE SCHEMA dbo; CREATE SCHEMA arrangement; CREATE SCHEMA event;
                CREATE SCHEMA classification;
                CREATE TABLE classification.classification(classificationid uuid PRIMARY KEY, enterpriseid uuid NOT NULL);
                CREATE SCHEMA security;
                CREATE TABLE security.securitytoken(securitytokenid uuid PRIMARY KEY);
                CREATE TABLE party.involvedparty(involvedpartyid uuid PRIMARY KEY, enterpriseid uuid NOT NULL);
                CREATE TABLE party.involvedpartyorganic(involvedpartyorganicid uuid PRIMARY KEY);
                CREATE TABLE dbo.enterprise(enterpriseid uuid PRIMARY KEY);
                CREATE TABLE dbo.systems(systemid uuid PRIMARY KEY);
                CREATE TABLE dbo.activeflag(activeflagid uuid PRIMARY KEY);
                CREATE TABLE arrangement.arrangement(arrangementid uuid PRIMARY KEY,enterpriseid uuid NOT NULL,
                    effectivefromdate timestamptz NOT NULL,effectivetodate timestamptz NOT NULL);
                CREATE TABLE arrangement.arrangementtype(arrangementtypeid uuid PRIMARY KEY,enterpriseid uuid NOT NULL,
                    arrangementtypename text NOT NULL);
                CREATE TABLE arrangement.arrangementxarrangementtype(arrangementid uuid NOT NULL,
                    arrangementtypeid uuid NOT NULL,enterpriseid uuid NOT NULL,
                    effectivefromdate timestamptz NOT NULL,effectivetodate timestamptz NOT NULL);
                CREATE TABLE arrangement.arrangementxinvolvedparty(arrangementid uuid NOT NULL,
                    involvedpartyid uuid NOT NULL,enterpriseid uuid NOT NULL,
                    effectivefromdate timestamptz NOT NULL,effectivetodate timestamptz NOT NULL);
                CREATE TABLE event.event(eventid uuid PRIMARY KEY,enterpriseid uuid NOT NULL,
                    effectivefromdate timestamptz NOT NULL,effectivetodate timestamptz NOT NULL);
                CREATE TABLE event.eventtype(eventtypeid uuid PRIMARY KEY,enterpriseid uuid NOT NULL,
                    eventtypename text NOT NULL);
                CREATE TABLE event.eventxeventtype(eventid uuid NOT NULL,eventtypeid uuid NOT NULL,
                    enterpriseid uuid NOT NULL,effectivefromdate timestamptz NOT NULL,effectivetodate timestamptz NOT NULL);
                CREATE TABLE event.eventxarrangement(eventid uuid NOT NULL,arrangementid uuid NOT NULL,
                    enterpriseid uuid NOT NULL,effectivefromdate timestamptz NOT NULL,effectivetodate timestamptz NOT NULL);
                """);
        sql("""
                CREATE SCHEMA resource; CREATE SCHEMA product; CREATE SCHEMA address;
                CREATE SCHEMA geography; CREATE SCHEMA rules;
                CREATE TABLE resource.resourceitem(resourceitemid uuid PRIMARY KEY, enterpriseid uuid NOT NULL);
                CREATE TABLE product.product(productid uuid PRIMARY KEY, enterpriseid uuid NOT NULL);
                CREATE TABLE address.address(addressid uuid PRIMARY KEY, enterpriseid uuid NOT NULL);
                CREATE TABLE geography.geography(geographyid uuid PRIMARY KEY, enterpriseid uuid NOT NULL);
                CREATE TABLE rules.rules(rulesid uuid PRIMARY KEY, enterpriseid uuid NOT NULL);
                """);
        sql(Files.readString(Path.of("src/main/resources/db/transactions.sql")));
    }
    @AfterAll static void close() {
        if (pool != null) await(pool.close());
        if (vertx != null) await(vertx.close());
        if (postgres != null) postgres.stop();
    }
    @BeforeEach void seed() {
        sql("TRUNCATE event.event,arrangement.arrangement,transactions.transaction_type CASCADE");
        sql("TRUNCATE event.eventtype,arrangement.arrangementtype CASCADE");
        party=UUID.randomUUID(); enterprise=UUID.randomUUID(); system=UUID.randomUUID();
        event=UUID.randomUUID(); debitType=UUID.randomUUID(); creditType=UUID.randomUUID();
        first=UUID.randomUUID(); second=UUID.randomUUID();
        query("INSERT INTO party.involvedparty VALUES ($1,$2)", party,enterprise);
        query("INSERT INTO party.involvedpartyorganic VALUES ($1)", party);
        query("INSERT INTO dbo.enterprise VALUES ($1)", enterprise);
        query("INSERT INTO dbo.systems VALUES ($1)", system);
        query("INSERT INTO event.event VALUES ($1,$2,now()-interval '1 day',now()+interval '1 day')",event,enterprise);
        UUID eventType=UUID.randomUUID();
        query("INSERT INTO event.eventtype VALUES ($1,$2,'Transaction Event')",eventType,enterprise);
        query("INSERT INTO event.eventxeventtype VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                event,eventType,enterprise);
        UUID walletType=UUID.randomUUID();
        query("INSERT INTO arrangement.arrangementtype VALUES ($1,$2,'Wallet')",walletType,enterprise);
        for (UUID arrangement : List.of(first,second)) {
            query("INSERT INTO arrangement.arrangement VALUES ($1,$2,now()-interval '1 day',now()+interval '1 day')",arrangement,enterprise);
            query("INSERT INTO arrangement.arrangementxarrangementtype VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                    arrangement,walletType,enterprise);
            query("INSERT INTO arrangement.arrangementxinvolvedparty VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                    arrangement,party,enterprise);
            query("INSERT INTO event.eventxarrangement VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                    event,arrangement,enterprise);
        }
        query("INSERT INTO transactions.transaction_type(transaction_type_id,enterprise_id,code,direction,active) "
                        + "VALUES ($1,$2,'debit',-1,true),($3,$2,'credit',1,true)",
                debitType,enterprise,creditType);
        service=new TransactionSqlFixture(pool,system);
        call=new TransactionSqlFixture.Call(new Actor(party,true),new Context(Realm.PERSONAL,party),
                new TransactionSqlFixture.Authority() {
                    public Future<Void> currentActor(SqlConnection connection,Actor actor,Context context) { return Future.succeededFuture(); }
                    public Future<Void> event(SqlConnection connection,Actor actor,Context context,UUID id) { return Future.succeededFuture(); }
                    public Future<Void> arrangement(SqlConnection connection,Actor actor,Context context,UUID id,int direction) { return Future.succeededFuture(); }
                });
    }
    List<TransactionSqlFixture.Line> balanced() {
        return List.of(new TransactionSqlFixture.Line(first,debitType,new BigDecimal("12.50"),"POINTS"),
                new TransactionSqlFixture.Line(second,creditType,new BigDecimal("12.50"),"POINTS"));
    }
    void fundFirst() {
        UUID clearing=UUID.randomUUID(), clearingType=UUID.randomUUID(), depositEvent=UUID.randomUUID();
        query("INSERT INTO arrangement.arrangementtype VALUES ($1,$2,'Wallet Clearing')",clearingType,enterprise);
        query("INSERT INTO arrangement.arrangement VALUES ($1,$2,now()-interval '1 day',now()+interval '1 day')",clearing,enterprise);
        query("INSERT INTO arrangement.arrangementxarrangementtype VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                clearing,clearingType,enterprise);
        query("INSERT INTO arrangement.arrangementxinvolvedparty VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                clearing,party,enterprise);
        query("INSERT INTO event.event VALUES ($1,$2,now()-interval '1 day',now()+interval '1 day')",depositEvent,enterprise);
        UUID eventType=UUID.randomUUID();
        query("INSERT INTO event.eventtype VALUES ($1,$2,'Transaction Event')",eventType,enterprise);
        query("INSERT INTO event.eventxeventtype VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                depositEvent,eventType,enterprise);
        for (UUID arrangement : List.of(clearing,first))
            query("INSERT INTO event.eventxarrangement VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                    depositEvent,arrangement,enterprise);
        done(service.post(call,depositEvent,UUID.randomUUID(),List.of(
                new TransactionSqlFixture.Line(clearing,debitType,new BigDecimal("100"),"POINTS"),
                new TransactionSqlFixture.Line(first,creditType,new BigDecimal("100"),"POINTS"))));
    }
    @Test void oneEventMovesValueBetweenTwoArrangementsAndRetriesIdempotently() {
        fundFirst();
        UUID key=UUID.randomUUID();
        var receipt=done(service.post(call,event,key,balanced()));
        assertEquals(receipt,done(service.post(call,event,key,balanced())));
        assertEquals(new BigDecimal("87.50000000"),done(service.balance(call,first,"POINTS")));
        assertEquals(new BigDecimal("12.50000000"),done(service.balance(call,second,"POINTS")));
        assertEquals(4L,count("SELECT count(*) AS n FROM transactions.entry"));
        assertThrows(IllegalStateException.class,()->done(service.post(call,event,UUID.randomUUID(),balanced())));
    }
    @Test void unbalancedEventAndFailedAuthorityRollBack() {
        var uneven=List.of(new TransactionSqlFixture.Line(first,debitType,new BigDecimal("12"),"POINTS"),
                new TransactionSqlFixture.Line(second,creditType,new BigDecimal("11"),"POINTS"));
        assertThrows(RuntimeException.class,()->done(service.post(call,event,UUID.randomUUID(),uneven)));
        assertEquals(0L,count("SELECT count(*) AS n FROM transactions.entry"));
        AtomicBoolean reached=new AtomicBoolean();
        var denied=new TransactionSqlFixture.Call(call.actor(),call.context(),new TransactionSqlFixture.Authority() {
            public Future<Void> currentActor(SqlConnection connection,Actor actor,Context context) {return Future.succeededFuture();}
            public Future<Void> event(SqlConnection connection,Actor actor,Context context,UUID id) {return Future.succeededFuture();}
            public Future<Void> arrangement(SqlConnection connection,Actor actor,Context context,UUID id,int direction) {
                reached.set(true);return Future.failedFuture(new SecurityException("FSDM row denied"));
            }
        });
        assertThrows(SecurityException.class,()->done(service.post(denied,event,UUID.randomUUID(),balanced())));
        assertTrue(reached.get());
        assertEquals(0L,count("SELECT count(*) AS n FROM transactions.entry"));
    }
    @Test void missingRelationshipDeniesPosting() {
        query("DELETE FROM event.eventxarrangement WHERE arrangementid=$1",second);
        assertThrows(SecurityException.class,()->done(service.post(call,event,UUID.randomUUID(),balanced())));
        assertEquals(0L,count("SELECT count(*) AS n FROM transactions.entry"));
    }
    @Test void postedRowsCannotBeChangedDirectly() {
        fundFirst();
        done(service.post(call,event,UUID.randomUUID(),balanced()));
        assertThrows(RuntimeException.class,()->query("UPDATE transactions.entry SET amount=1 WHERE event_id=$1",event));
        assertThrows(RuntimeException.class,()->query("DELETE FROM transactions.entry WHERE event_id=$1",event));
    }
    @Test void databaseRejectsPostingUnderAnotherEnterprise() {
        UUID other=UUID.randomUUID();
        query("INSERT INTO dbo.enterprise VALUES ($1)",other);
        assertThrows(RuntimeException.class,()->query("""
                INSERT INTO transactions.entry(entry_id,event_id,enterprise_id,operation_key,line_no,
                    arrangement_id,transaction_type_id,direction,amount,unit)
                VALUES ($1,$2,$3,$4,1,$5,$6,-1,1,'POINTS')
                """,UUID.randomUUID(),event,other,UUID.randomUUID(),first,debitType));
        assertEquals(0L,count("SELECT count(*) AS n FROM transactions.entry"));
    }
    @Test void classifiedRelationshipsAndTheirSecurityUseExistingFsdmObjects() {
        fundFirst();
        done(service.post(call,event,UUID.randomUUID(),balanced()));
        UUID entry = await(pool.preparedQuery("SELECT entry_id FROM transactions.entry WHERE event_id=$1 AND line_no=1")
                .execute(Tuple.of(event))).iterator().next().getUUID("entry_id");
        UUID role=UUID.randomUUID(), flag=UUID.randomUUID(), device=UUID.randomUUID(), token=UUID.randomUUID();
        query("INSERT INTO classification.classification VALUES ($1,$2)",role,enterprise);
        query("INSERT INTO dbo.activeflag VALUES ($1)",flag);
        query("INSERT INTO resource.resourceitem VALUES ($1,$2)",device,enterprise);
        query("INSERT INTO security.securitytoken VALUES ($1)",token);
        for (String target : List.of("involved_party", "resource_item", "arrangement")) {
            UUID related = target.equals("involved_party") ? party : target.equals("resource_item") ? device : first;
            UUID link=UUID.randomUUID();
            String table="transaction_x_"+target;
            query("INSERT INTO transactions."+table+" ("+table+"_id,entry_id,"+target+"_id,classificationid,"
                    +"enterprise_id,activeflagid,systemid) VALUES ($1,$2,$3,$4,$5,$6,$7)",
                    link,entry,related,role,enterprise,flag,system);
            query("INSERT INTO transactions."+table+"_security_token ("+table+"_security_token_id,"+table+"_id,"
                    +"enterprise_id,securitytokenid,createallowed,updateallowed,deleteallowed,readallowed,activeflagid,systemid)"
                    +" VALUES ($1,$2,$3,$4,0,0,0,1,$5,$6)",UUID.randomUUID(),link,enterprise,token,flag,system);
            assertEquals(1L,count("SELECT count(*) AS n FROM transactions."+table+"_security_token"));
        }
        // The same party can have a second role, without duplicating the accounting amount.
        UUID cashierRole=UUID.randomUUID();
        query("INSERT INTO classification.classification VALUES ($1,$2)",cashierRole,enterprise);
        query("INSERT INTO transactions.transaction_x_involved_party "
                        +"(transaction_x_involved_party_id,entry_id,involved_party_id,classificationid,enterprise_id,activeflagid,systemid)"
                        +" VALUES ($1,$2,$3,$4,$5,$6,$7)",UUID.randomUUID(),entry,party,cashierRole,enterprise,flag,system);
        assertEquals(2L,count("SELECT count(*) AS n FROM transactions.transaction_x_involved_party"));
        assertEquals(new BigDecimal("87.50000000"),done(service.balance(call,first,"POINTS")));
        UUID foreignDevice=UUID.randomUUID();
        query("INSERT INTO resource.resourceitem VALUES ($1,$2)",foreignDevice,UUID.randomUUID());
        assertThrows(RuntimeException.class,()->query("INSERT INTO transactions.transaction_x_resource_item "
                        +"(transaction_x_resource_item_id,entry_id,resource_item_id,classificationid,enterprise_id,activeflagid,systemid)"
                        +" VALUES ($1,$2,$3,$4,$5,$6,$7)",UUID.randomUUID(),entry,foreignDevice,role,enterprise,flag,system));
    }

    @Test void unbalancedDirectWritesRollBackWithoutAPostingAggregate() {
        assertThrows(RuntimeException.class,()->query("""
                INSERT INTO transactions.entry(entry_id,event_id,enterprise_id,operation_key,line_no,
                    arrangement_id,transaction_type_id,direction,amount,unit)
                VALUES ($1,$2,$3,$4,1,$5,$6,1,1,'POINTS')
                """,UUID.randomUUID(),event,enterprise,UUID.randomUUID(),second,creditType));
        assertEquals(0L,count("SELECT count(*) AS n FROM transactions.entry"));
    }

    @Test void committedEventsRejectAdditionalLines() {
        fundFirst();
        UUID key=UUID.randomUUID();
        done(service.post(call,event,key,balanced()));
        assertThrows(RuntimeException.class,()->query("""
                INSERT INTO transactions.entry(entry_id,event_id,enterprise_id,operation_key,line_no,
                    arrangement_id,transaction_type_id,direction,amount,unit)
                VALUES ($1,$2,$3,$4,3,$5,$6,1,1,'POINTS')
                """,UUID.randomUUID(),event,enterprise,key,second,creditType));
        assertEquals(4L,count("SELECT count(*) AS n FROM transactions.entry"));
    }

    @Test void partyMustStillBelongToEveryAffectedArrangement() {
        fundFirst();
        query("DELETE FROM arrangement.arrangementxinvolvedparty WHERE arrangementid=$1",second);
        assertThrows(SecurityException.class,()->done(service.post(call,event,UUID.randomUUID(),balanced())));
        assertEquals(2L,count("SELECT count(*) AS n FROM transactions.entry"));
    }
    @Test void walletCannotOverdrawAndConcurrentDebitsSerialize() {
        fundFirst();
        var excessive=List.of(new TransactionSqlFixture.Line(first,debitType,new BigDecimal("101"),"POINTS"),
                new TransactionSqlFixture.Line(second,creditType,new BigDecimal("101"),"POINTS"));
        assertThrows(IllegalStateException.class,()->done(service.post(call,event,UUID.randomUUID(),excessive)));
        assertEquals(new BigDecimal("100.00000000"),done(service.balance(call,first,"POINTS")));
        var success=service.post(call,event,UUID.randomUUID(),balanced()).subscribeAsCompletionStage();
        var conflict=service.post(call,event,UUID.randomUUID(),balanced()).subscribeAsCompletionStage();
        int passed=0;
        try { Uni.createFrom().completionStage(success).await().atMost(Duration.ofSeconds(15)); passed++; } catch (RuntimeException ignored) {}
        try { Uni.createFrom().completionStage(conflict).await().atMost(Duration.ofSeconds(15)); passed++; } catch (RuntimeException ignored) {}
        assertEquals(1,passed);
        assertEquals(new BigDecimal("87.50000000"),done(service.balance(call,first,"POINTS")));
    }
    @Test void separateEventsCannotBothSpendTheSameFunds() {
        fundFirst();
        UUID otherEvent=UUID.randomUUID();
        query("INSERT INTO event.event VALUES ($1,$2,now()-interval '1 day',now()+interval '1 day')",otherEvent,enterprise);
        query("""
                INSERT INTO event.eventxeventtype
                SELECT $1,eventtypeid,$2,now()-interval '1 day',now()+interval '1 day'
                FROM event.eventtype WHERE eventtypename='Transaction Event' LIMIT 1
                """,otherEvent,enterprise);
        for(UUID arrangement:List.of(first,second))
            query("INSERT INTO event.eventxarrangement VALUES ($1,$2,$3,now()-interval '1 day',now()+interval '1 day')",
                    otherEvent,arrangement,enterprise);
        var spend=List.of(new TransactionSqlFixture.Line(first,debitType,new BigDecimal("70"),"POINTS"),
                new TransactionSqlFixture.Line(second,creditType,new BigDecimal("70"),"POINTS"));
        var one=service.post(call,event,UUID.randomUUID(),spend).subscribeAsCompletionStage();
        var two=service.post(call,otherEvent,UUID.randomUUID(),spend).subscribeAsCompletionStage();
        int passed=0;
        try { Uni.createFrom().completionStage(one).await().atMost(Duration.ofSeconds(15)); passed++; } catch(RuntimeException ignored) {}
        try { Uni.createFrom().completionStage(two).await().atMost(Duration.ofSeconds(15)); passed++; } catch(RuntimeException ignored) {}
        assertEquals(1,passed);
        assertEquals(new BigDecimal("30.00000000"),done(service.balance(call,first,"POINTS")));
    }
    static <T> T done(Uni<T> uni) {return uni.await().atMost(Duration.ofSeconds(20));}
    static <T> T await(Future<T> future) {return done(Uni.createFrom().completionStage(future.toCompletionStage()));}
    static void sql(String sql) {await(pool.query(sql).execute());}
    static void query(String sql,Object... values) {await(pool.preparedQuery(sql).execute(Tuple.tuple(Arrays.asList(values))));}
    static long count(String sql) {return await(pool.query(sql).execute()).iterator().next().getLong("n");}
}
