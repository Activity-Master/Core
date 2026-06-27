package com.guicedee.activitymaster.fsdm.db;

import com.guicedee.client.Environment;
import com.guicedee.persistence.implementations.mongodb.MongoConnectionInfo;
import com.guicedee.persistence.implementations.mongodb.MongoModule;
import lombok.extern.log4j.Log4j2;

/**
 * GuicedEE persistence {@link MongoModule} that binds a shared Vert.x MongoClient for ActivityMaster.
 * <p>
 * The client backs {@link com.guicedee.activitymaster.fsdm.ResourceItemJsonStore} — when a resource item's
 * type is a JSON type its payload is stored as a MongoDB document instead of the relational
 * {@code resource.resourceitemdatavalue} column.
 * <p>
 * The module is <strong>opt-in</strong>: it only binds a client when MongoDB is configured (see
 * {@link #isMongoConfigured()}), so deployments that do not use JSON resource items are unaffected and never
 * require a MongoDB instance. Routing JSON resource items to MongoDB is additionally gated by the master
 * opt-in flag {@code RESOURCE_ITEM_JSON_STORE=true} (see {@code ResourceItemJsonStore}); when that flag is not
 * set the store never resolves a client and everything stays on relational storage. Configuration is resolved
 * through {@link Environment}:
 * <ul>
 *   <li>{@code MONGO_ENABLED} — set {@code true} to force-enable (when using discrete host/port settings)</li>
 *   <li>{@code MONGO_URL} — full connection string, e.g. {@code mongodb://localhost:27017} (takes precedence)</li>
 *   <li>{@code MONGO_HOST} / {@code MONGO_PORT} — discrete host/port (default {@code 127.0.0.1:27017})</li>
 *   <li>{@code MONGO_DATABASE} — database name (default {@code activitymaster})</li>
 *   <li>{@code MONGO_USERNAME} / {@code MONGO_PASSWORD} / {@code MONGO_AUTH_SOURCE} — optional credentials</li>
 * </ul>
 */
@Log4j2
public class ActivityMasterMongoModule<J extends ActivityMasterMongoModule<J>> extends MongoModule<J> {
    /**
     * Logical connection name (Guice {@code @Named} qualifier) for the ActivityMaster MongoClient.
     */
    public static final String CONNECTION_NAME = "activityMaster";

    /**
     * Whether MongoDB has been configured for this deployment (via {@code MONGO_ENABLED}, {@code MONGO_URL}
     * or {@code MONGO_HOST}).
     *
     * @return {@code true} when a MongoDB connection should be created
     */
    public static boolean isMongoConfigured() {
        String enabled = Environment.getProperty("MONGO_ENABLED", "");
        if ("true".equalsIgnoreCase(enabled)) {
            return true;
        }
        String url = Environment.getProperty("MONGO_URL", "");
        if (url != null && !url.isBlank()) {
            return true;
        }
        String host = Environment.getProperty("MONGO_HOST", "");
        return host != null && !host.isBlank();
    }

    @Override
    public boolean enabled() {
        return isMongoConfigured();
    }

    @Override
    protected MongoConnectionInfo getMongoConnectionInfo() {
        MongoConnectionInfo info = new MongoConnectionInfo()
                .setName(CONNECTION_NAME)
                .setDatabaseName(Environment.getProperty("MONGO_DATABASE", "activitymaster"))
                .setDefaultConnection(false);

        String url = Environment.getProperty("MONGO_URL", "");
        if (url != null && !url.isBlank()) {
            info.setConnectionString(url);
        } else {
            info.setHost(Environment.getProperty("MONGO_HOST", "127.0.0.1"));
            String port = Environment.getProperty("MONGO_PORT", "27017");
            try {
                info.setPort(Integer.parseInt(port.trim()));
            } catch (NumberFormatException badPort) {
                log.warn("Invalid MONGO_PORT '{}' — defaulting to 27017", port);
                info.setPort(27017);
            }
            String user = Environment.getProperty("MONGO_USERNAME", "");
            if (user != null && !user.isBlank()) {
                info.setUsername(user);
            }
            String pass = Environment.getProperty("MONGO_PASSWORD", "");
            if (pass != null && !pass.isBlank()) {
                info.setPassword(pass);
            }
            String authSource = Environment.getProperty("MONGO_AUTH_SOURCE", "");
            if (authSource != null && !authSource.isBlank()) {
                info.setAuthSource(authSource);
            }
        }
        return info;
    }

    @Override
    public Integer sortOrder() {
        return 25;
    }
}

