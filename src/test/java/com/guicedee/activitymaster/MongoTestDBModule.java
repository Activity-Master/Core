package com.guicedee.activitymaster;

import com.guicedee.activitymaster.fsdm.db.ActivityMasterMongoModule;
import com.guicedee.client.services.lifecycle.IGuiceModule;
import com.guicedee.persistence.implementations.mongodb.MongoConnectionInfo;
import com.guicedee.persistence.implementations.mongodb.MongoModule;
import org.testcontainers.containers.MongoDBContainer;

import java.time.Duration;

/**
 * Test {@link MongoModule} that provisions a MongoDB instance with Testcontainers and binds a shared
 * {@code MongoClient} for the core test suite (mirrors {@link PostgreSQLTestDBModule}).
 * <p>
 * It binds under the same {@link ActivityMasterMongoModule#CONNECTION_NAME} logical name and as the default
 * connection, so {@code ResourceItemJsonStore} resolves it transparently. It is configured purely from the
 * container (it deliberately does <strong>not</strong> set {@code MONGO_*} environment properties) so the
 * production {@link ActivityMasterMongoModule} stays disabled during tests and there is exactly one MongoClient
 * binding.
 */
public class MongoTestDBModule extends MongoModule<MongoTestDBModule>
        implements IGuiceModule<MongoTestDBModule>
{
    private static final MongoDBContainer mongoContainer =
            new MongoDBContainer("mongo:7.0").withStartupTimeout(Duration.ofMinutes(2));

    static
    {
        mongoContainer.start();
        System.out.println("✅ MongoDB test container started at: " + mongoContainer.getConnectionString());
    }

    @Override
    protected MongoConnectionInfo getMongoConnectionInfo()
    {
        return new MongoConnectionInfo()
                .setName(ActivityMasterMongoModule.CONNECTION_NAME)
                .setConnectionString(mongoContainer.getConnectionString())
                .setDatabaseName("activitymaster_test")
                .setDefaultConnection(true);
    }

    @Override
    public Integer sortOrder()
    {
        return 20;
    }

    /** Exposes the underlying Testcontainers instance for reuse in other tests. */
    public static MongoDBContainer getMongoContainer()
    {
        return mongoContainer;
    }
}

