package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.client.Environment;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import java.util.Objects;

/** Applies tracked SQL before Hibernate or enterprise services use the database. */
public final class FsdmSchemaUpdates {
    public static final String INSTALL_UPDATES = "FSDM_INSTALL_UPDATES";

    private FsdmSchemaUpdates() { }

    /** Pending schema updates run by default; separately managed deployments may opt out. */
    public static boolean enabled() {
        String setting = Environment.getSystemPropertyOrEnvironment(INSTALL_UPDATES, "true");
        if (setting == null || setting.isBlank()) return true;
        if ("true".equalsIgnoreCase(setting)) return true;
        if ("false".equalsIgnoreCase(setting)) return false;
        throw new IllegalArgumentException(INSTALL_UPDATES + " must be true or false");
    }

    /** Uses one borrowed connection, with each script managing its own transaction. */
    public static Future<Void> installUpdates(Pool pool) {
        Objects.requireNonNull(pool, "ActivityMaster SQL pool is not initialized");
        return pool.getConnection().compose(connection -> {
            Future<Void> updates = Future.succeededFuture();
            for (String script : FsdmSchema.orderedScripts()) {
                updates = updates.compose(ignored -> connection.query(FsdmSchema.updateSql(script)).execute()
                        .<Void>mapEmpty().recover(failure -> Future.failedFuture(
                                new IllegalStateException("Failed applying FSDM schema script " + script, failure))));
            }
            // A failed multi-statement query can leave its transaction open. Roll it back
            // before returning this connection, preserving the original migration failure.
            return updates.recover(failure -> connection.query("ROLLBACK").execute()
                    .recover(rollbackFailure -> {
                        failure.addSuppressed(rollbackFailure);
                        return Future.succeededFuture();
                    }).compose(ignored -> Future.<Void>failedFuture(failure)))
                    // Setup scripts change session settings; do not leak them to runtime borrowers.
                    .eventually(() -> connection.query("RESET SESSION AUTHORIZATION; RESET ALL").execute().mapEmpty())
                    .eventually(connection::close);
        });
    }
}
