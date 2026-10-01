package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.client.Environment;
import com.guicedee.persistence.ConnectionBaseInfo;
import com.guicedee.persistence.implementations.postgres.PostgresConnectionBaseInfo;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.*;
import io.vertx.pgclient.*;
import io.vertx.sqlclient.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/** Immutable connection policy for the one ActivityMaster pool. No connection is opened during resolution. */
public final class ActivityMasterPoolConfiguration {
    private final PgConnectOptions connection;
    private final PoolOptions pool;
    private final NetClientOptions transport;

    private ActivityMasterPoolConfiguration(PgConnectOptions connection, PoolOptions pool, NetClientOptions transport) {
        this.connection=connection;this.pool=pool;this.transport=transport;
    }
    public static ActivityMasterPoolConfiguration configured() {
        return resolve(name -> Environment.getSystemPropertyOrEnvironment(name, null));
    }
    public static ActivityMasterPoolConfiguration resolve(Function<String,String> settings) {
        try {
            String host=value(settings,"FSDM_DBSERVER","localhost");
            String mode=value(settings,"FSDM_SSL_MODE","verify-full");
            int maximum=integer(settings,"FSDM_POOL_MAX_SIZE",16,1,500);
            var connection=new PgConnectOptions().setHost(host)
                    .setPort(integer(settings,"FSDM_DBPORT",5432,1,65535))
                    .setDatabase(value(settings,"FSDM_DBNAME","fsdm"))
                    .setUser(value(settings,"FSDM_USER","fsdm"))
                    .setPassword(value(settings,"FSDM_PASSWORD",""))
                    .setPipeliningLimit(1)
                    .setProperties(Map.of("application_name","activity-master", "options",
                            "-c statement_timeout="+integer(settings,"FSDM_STATEMENT_TIMEOUT_MS",30000,1,600000)
                            +" -c lock_timeout="+integer(settings,"FSDM_LOCK_TIMEOUT_MS",1000,1,60000)));
            if(connection.getPassword().isBlank())throw invalid();
            if("verify-full".equals(mode)) {
                String file=value(settings,"FSDM_SSL_CA_FILE","");
                if(file.isBlank())throw invalid();
                byte[] ca;
                try(var input=Files.newInputStream(Path.of(file))) {ca=input.readNBytes(65537);}
                if(ca.length==0 || ca.length>65536)throw invalid();
                connection.setSslMode(SslMode.VERIFY_FULL).setSslOptions(new ClientSSLOptions()
                        .setTrustAll(false).setHostnameVerificationAlgorithm("HTTPS")
                        .setSslHandshakeTimeout(integer(settings,"FSDM_TLS_TIMEOUT_MS",2000,1,30000))
                        .setSslHandshakeTimeoutUnit(TimeUnit.MILLISECONDS)
                        .setTrustOptions(new PemTrustOptions().addCertValue(Buffer.buffer(ca))));
            } else if("disable".equals(mode)
                    && Set.of("local","development","test").contains(value(settings,"ENVIRONMENT",""))
                    && Set.of("127.0.0.1","::1").contains(host)) {
                connection.setSslMode(SslMode.DISABLE);
            } else throw invalid();
            var pool=new PoolOptions().setShared(false).setName("activity-master-pool")
                    .setMaxSize(maximum).setMaxWaitQueueSize(integer(settings,"FSDM_POOL_MAX_WAIT_QUEUE",64,0,4096))
                    .setConnectionTimeout(integer(settings,"FSDM_POOL_ACQUIRE_TIMEOUT_MS",1000,1,30000))
                    .setConnectionTimeoutUnit(TimeUnit.MILLISECONDS)
                    .setIdleTimeout(300).setIdleTimeoutUnit(TimeUnit.SECONDS)
                    .setMaxLifetime(3).setMaxLifetimeUnit(TimeUnit.MINUTES);
            var transport=new NetClientOptions().setReconnectAttempts(0)
                    .setConnectTimeout(integer(settings,"FSDM_CONNECT_TIMEOUT_MS",2000,1,30000));
            return new ActivityMasterPoolConfiguration(connection,pool,transport);
        } catch(Exception failure) {throw invalid();}
    }
    /** Caller owns the pool; borrowers must receive this exact instance, not a new named-pool handle. */
    public Pool open(Vertx vertx) {
        return PgBuilder.pool().using(Objects.requireNonNull(vertx)).connectingTo(new PgConnectOptions(connection))
                .with(new PoolOptions(pool)).with(new NetClientOptions(transport)).build();
    }
    /** Supplies the same owned pool to Hibernate and every managed SQL-client lookup. */
    public ConnectionBaseInfo attach(Vertx vertx, Properties properties) {
        Objects.requireNonNull(properties);
        Pool owned=open(vertx);
        var info=new PostgresConnectionBaseInfo() {
            @Override public Pool toPooledDatasource() {return owned;}
            @Override public String toString() {return "ActivityMasterConnection[redacted]";}
        };
        info.setServerName(connection.getHost());info.setPort(Integer.toString(connection.getPort()));
        info.setDatabaseName(connection.getDatabase());info.setUsername(connection.getUser());
        info.setPassword(connection.getPassword());info.setDefaultConnection(true);info.setReactive(true);
        info.setMinPoolSize(0);info.setMaxPoolSize(pool.getMaxSize());
        properties.put("hibernate.vertx.pool",owned);
        properties.put("guicedee.persistence.ownedPool",owned);
        return info;
    }
    @Override public String toString() {return "ActivityMasterPoolConfiguration[redacted]";}
    private static String value(Function<String,String> settings,String name,String fallback) {
        String value=settings.apply(name);
        // Environment represents an absent null-default setting as an empty string.
        return value==null || value.isEmpty()?fallback:value;
    }
    private static int integer(Function<String,String> settings,String name,int fallback,int minimum,int maximum) {
        String value=value(settings,name,Integer.toString(fallback));
        if(!value.matches("0|[1-9][0-9]{0,5}"))throw invalid();
        int number=Integer.parseInt(value);if(number<minimum || number>maximum)throw invalid();return number;
    }
    private static IllegalArgumentException invalid() {
        return new IllegalArgumentException("Invalid ActivityMaster pool configuration");
    }
}
