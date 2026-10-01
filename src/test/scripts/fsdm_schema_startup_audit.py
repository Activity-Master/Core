"""Exercise default startup SQL updates through the actual Vert.x pool.

Run from core: python src/test/scripts/fsdm_schema_startup_audit.py
Requires Maven, a JDK and Docker. Uses a disposable loopback-only PostgreSQL port.
"""
import pathlib
import subprocess
import tempfile
import time
import uuid

CORE = pathlib.Path(__file__).resolve().parents[3]


def run(*command):
    result = subprocess.run(command, text=True, encoding="utf-8", capture_output=True)
    if result.returncode:
        raise RuntimeError(result.stdout[-4000:] + result.stderr[-4000:])
    return result.stdout.strip()


JAVA = r'''
import com.guicedee.activitymaster.fsdm.db.*;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Pool;
import io.smallrye.mutiny.Uni;
import com.guicedee.client.IGuiceContext;
import com.guicedee.persistence.bind.JtaPersistService;
import com.guicedee.persistence.implementations.VertxPersistenceModule;
import com.google.inject.Key;
import org.mockito.Mockito;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FsdmSchemaStartupAudit {
    static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
    static <T> T await(io.vertx.core.Future<T> future) throws Exception {
        return future.toCompletionStage().toCompletableFuture().get(120, TimeUnit.SECONDS);
    }
    static String scalar(Pool pool, String sql) throws Exception {
        return await(pool.query(sql).execute()).iterator().next().getValue(0).toString();
    }
    public static void main(String[] args) throws Exception {
        String key = FsdmSchemaUpdates.INSTALL_UPDATES;
        System.clearProperty(key);
        check(FsdmSchemaUpdates.enabled(), "Missing setting must default to enabled");
        System.setProperty(key, "");
        check(FsdmSchemaUpdates.enabled(), "Empty setting must default to enabled");
        System.setProperty(key, "false");
        check(!FsdmSchemaUpdates.enabled(), "Explicit false must opt out");
        System.setProperty(key, "TRUE");
        check(FsdmSchemaUpdates.enabled(), "Explicit true must enable updates");
        System.setProperty(key, "typo");
        try {
            FsdmSchemaUpdates.enabled();
            throw new AssertionError("Malformed setting must fail");
        } catch (IllegalArgumentException expected) { }
        System.clearProperty(key);
        System.out.println("PASS: default enabled, explicit opt-out, invalid configuration rejection");
        Vertx vertx = Vertx.vertx();
        Pool pool = null;
        try {
            Map<String,String> settings = Map.of("ENVIRONMENT", "test", "FSDM_SSL_MODE", "disable",
                    "FSDM_DBSERVER", "127.0.0.1", "FSDM_DBPORT", args[0], "FSDM_DBNAME", "postgres",
                    "FSDM_USER", "postgres", "FSDM_PASSWORD", "fixture-password", "FSDM_POOL_MAX_SIZE", "1",
                    "FSDM_STATEMENT_TIMEOUT_MS", "4321");
            Pool administrative = ActivityMasterPoolConfiguration.resolve(settings::get).open(vertx);
            try {
                await(administrative.query("CREATE ROLE fsdm_install_owner LOGIN NOSUPERUSER NOCREATEROLE NOCREATEDB PASSWORD 'fixture-password'").execute());
                await(administrative.query("CREATE DATABASE fsdm_owner_audit OWNER fsdm_install_owner").execute());
            } finally { await(administrative.close()); }
            settings = new java.util.HashMap<>(settings);
            settings.put("FSDM_USER", "fsdm_install_owner");
            settings.put("FSDM_DBNAME", "fsdm_owner_audit");
            var properties = new java.util.Properties();
            var connection = ActivityMasterPoolConfiguration.resolve(settings::get).attach(vertx, properties);
            connection.setPersistenceUnitName(ActivityMasterDBModule.persistenceUnitName);
            pool = (Pool) properties.get("guicedee.persistence.ownedPool");
            VertxPersistenceModule.getConnectionModules().put(connection, null);
            await(FsdmSchemaUpdates.installUpdates(pool));
            check(scalar(pool, "SELECT count(*) FROM dbo.fsdmschemaupdate").equals(
                    Integer.toString(FsdmSchema.orderedScripts().size())), "All pending updates must be recorded");
            check(scalar(pool, "SELECT count(*) FROM pg_constraint WHERE contype='f'").equals("0"),
                    "Fresh runtime schema must have no foreign keys");
            String snapshotSql = "SELECT string_agg(scriptname||checksum||appliedat::text, ',' ORDER BY scriptsequence) FROM dbo.fsdmschemaupdate";
            String snapshot = scalar(pool, snapshotSql);
            await(FsdmSchemaUpdates.installUpdates(pool));
            check(snapshot.equals(scalar(pool, snapshotSql)), "Repeat startup must leave history unchanged");
            check(scalar(pool, "SHOW statement_timeout").equals("4321ms"), "Migration settings must not leak into runtime");
            System.out.println("PASS: actual reactive pool installs and skips completed SQL; session settings restored");
            String last = FsdmSchema.orderedScripts().getLast();
            String checksum = scalar(pool, "SELECT checksum FROM dbo.fsdmschemaupdate WHERE scriptname='"+last+"'");
            await(pool.query("UPDATE dbo.fsdmschemaupdate SET checksum=repeat('0',64) WHERE scriptname='"+last+"'").execute());
            try {
                await(FsdmSchemaUpdates.installUpdates(pool));
                throw new AssertionError("Checksum error must propagate to startup");
            } catch (java.util.concurrent.ExecutionException expected) {
                check(expected.getCause().getMessage().contains(last), "Failure must identify the script");
            }
            // With maxSize=1 this would fail/time out if the updater leaked its connection
            // or returned it while the transaction was still aborted.
            check(scalar(pool, "SELECT 1").equals("1"), "Pool must remain usable after migration failure");
            check(scalar(pool, "SHOW statement_timeout").equals("4321ms"), "Failure must restore settings too");
            await(pool.query("UPDATE dbo.fsdmschemaupdate SET checksum='"+checksum+"' WHERE scriptname='"+last+"'").execute());
            await(FsdmSchemaUpdates.installUpdates(pool));
            check(snapshot.equals(scalar(pool, snapshotSql)), "Retry must preserve completed history");
            System.out.println("PASS: startup failure propagates; rollback, connection release and retry work");

            // Verify the actual module hook with only Hibernate's startup boundary mocked.
            // Delete the final history entry, leaving a real pending, replayable update.
            Pool actualPool = pool;
            await(pool.query("DELETE FROM dbo.fsdmschemaupdate WHERE scriptname='"+last+"'").execute());
            var started = new java.util.concurrent.atomic.AtomicInteger();
            JtaPersistService persistence = Mockito.mock(JtaPersistService.class);
            Mockito.when(persistence.start()).thenReturn(Uni.createFrom().completionStage(() ->
                    actualPool.query("SELECT count(*) FROM dbo.fsdmschemaupdate").execute().toCompletionStage())
                    .invoke(rows -> {
                        check(rows.iterator().next().getLong(0) == FsdmSchema.orderedScripts().size(),
                                "Pending schema SQL must complete before Hibernate starts");
                        started.incrementAndGet();
                    }).replaceWithVoid());
            try (var context = Mockito.mockStatic(IGuiceContext.class)) {
                context.when(() -> IGuiceContext.get(Mockito.any(Key.class))).thenReturn(persistence);
                ActivityMasterDBModule module = new ActivityMasterDBModule();
                // Startup is a different instance from Guice module configuration.
                // It must resolve the configured named pool rather than an instance field.
                var startup = module.postLoad().getFirst();
                check(started.get() == 0, "Startup must remain lazy until subscription");
                check(scalar(pool, "SELECT count(*) FROM dbo.fsdmschemaupdate").equals(
                        Integer.toString(FsdmSchema.orderedScripts().size()-1)), "Updates must remain lazy too");
                check(startup.subscribeAsCompletionStage().toCompletableFuture().get(120, TimeUnit.SECONDS),
                        "Persistence startup must succeed after updates");
                check(started.get() == 1, "Hibernate starts exactly once");
                await(pool.query("UPDATE dbo.fsdmschemaupdate SET checksum=repeat('0',64) WHERE scriptname='"+last+"'").execute());
                try {
                    module.postLoad().getFirst().subscribeAsCompletionStage().toCompletableFuture().get(120, TimeUnit.SECONDS);
                    throw new AssertionError("Module must stop on a migration failure");
                } catch (java.util.concurrent.ExecutionException expected) { }
                check(started.get() == 1, "Hibernate must not start after failed SQL");
                System.setProperty(key, "false");
                VertxPersistenceModule.reset(); // Opt-out must not need a migration pool.
                check(module.postLoad().getFirst().subscribeAsCompletionStage().toCompletableFuture().get(120, TimeUnit.SECONDS),
                        "Explicit opt-out must permit separately managed startup");
                check(started.get() == 2, "Opt-out starts persistence without applying SQL");
                System.clearProperty(key);
            }
            System.out.println("PASS: actual module checks SQL by default before Hibernate; failure gates startup; opt-out bypasses checks");
        } finally {
            if (pool != null) await(pool.close());
            await(vertx.close());
        }
    }
}
'''


def main():
    name = "am-fsdm-startup-audit-" + uuid.uuid4().hex[:12]
    with tempfile.TemporaryDirectory(prefix="fsdm-startup-audit-") as directory:
        scratch = pathlib.Path(directory)
        classpath_file = scratch / "classpath.txt"
        run("mvn.cmd", "-q", "-DskipTests", "compile", "dependency:build-classpath",
            "-Dmdep.outputFile=" + str(classpath_file))
        classpath = str(CORE / "target/classes") + ";" + classpath_file.read_text().strip()
        source = scratch / "FsdmSchemaStartupAudit.java"
        source.write_text(JAVA, encoding="utf-8")
        run("javac", "-proc:none", "-cp", classpath, "-d", str(scratch), str(source))
        run("docker", "run", "--rm", "-d", "--name", name,
            "-p", "127.0.0.1::5432", "-e", "POSTGRES_HOST_AUTH_METHOD=trust", "postgres:17")
        try:
            for _ in range(60):
                try:
                    run("docker", "exec", name, "pg_isready", "-U", "postgres")
                    break
                except RuntimeError:
                    time.sleep(0.5)
            else:
                raise RuntimeError("PostgreSQL did not start")
            port = run("docker", "port", name, "5432/tcp").rsplit(":", 1)[1]
            print(run("java", "-cp", str(scratch) + ";" + classpath, "FsdmSchemaStartupAudit", port))
        finally:
            run("docker", "stop", name)


if __name__ == "__main__":
    main()
