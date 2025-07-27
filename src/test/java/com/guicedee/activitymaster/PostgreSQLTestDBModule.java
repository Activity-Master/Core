package com.guicedee.activitymaster;

import com.guicedee.activitymaster.fsdm.db.ActivityMasterDBModule;
import com.guicedee.guicedinjection.interfaces.IGuiceModule;
import com.guicedee.vertxpersistence.ConnectionBaseInfo;
import com.guicedee.vertxpersistence.DatabaseModule;
import com.guicedee.vertxpersistence.annotations.EntityManager;
import com.guicedee.vertxpersistence.implementations.postgres.PostgresConnectionBaseInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@EntityManager(value = "ActivityMaster-Test", defaultEm = true)
public class PostgreSQLTestDBModule
        extends DatabaseModule<PostgreSQLTestDBModule>
        implements IGuiceModule<PostgreSQLTestDBModule>
{
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("fsdm")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        ActivityMasterDBModule.forTests = true;
        postgresContainer.start();
         try {
        // Copy init.sql from classpath to container
        Path sqlPath = Paths.get("src/test/resources/postgres_fsdm.sql");

        postgresContainer.copyFileToContainer(
            MountableFile.forHostPath(sqlPath),
            "/docker-entrypoint-initdb.d/init.sql"
        );

        // Or execute it explicitly with psql:
        postgresContainer.copyFileToContainer(
            MountableFile.forHostPath(sqlPath),
            "/tmp/init.sql"
        );

        Container.ExecResult result = postgresContainer.execInContainer(
            "psql",
            "-U", postgresContainer.getUsername(),
            "-d", postgresContainer.getDatabaseName(),
            "-f", "/tmp/init.sql"
        );

        if (result.getExitCode() != 0) {
            System.err.println("STDERR: " + result.getStderr());
            throw new RuntimeException("psql script execution failed: " + result.getStderr());
        }

        System.out.println("✅ DB Script Executed Successfully:\n" + result.getStdout());

    } catch (Exception e) {
        throw new RuntimeException("Failed to execute init.sql", e);
    }
    }

    @NotNull
    @Override
    protected String getPersistenceUnitName()
    {
        return "ActivityMaster-Test";
    }

    @Override
    @NotNull
    protected ConnectionBaseInfo getConnectionBaseInfo(PersistenceUnitDescriptor unit, Properties filteredProperties)
    {
        PostgresConnectionBaseInfo connectionInfo = new PostgresConnectionBaseInfo();
        connectionInfo.setServerName(postgresContainer.getHost());
        connectionInfo.setPort(String.valueOf(postgresContainer.getFirstMappedPort()));
        connectionInfo.setDatabaseName(postgresContainer.getDatabaseName());
        connectionInfo.setUsername(postgresContainer.getUsername());
        connectionInfo.setPassword(postgresContainer.getPassword());
        connectionInfo.setDefaultConnection(true);
        connectionInfo.setReactive(true);
        return connectionInfo;
    }

    @NotNull
    @Override
    protected String getJndiMapping()
    {
        return "jdbc:activitymaster-test";
    }


    @Override
    public Integer sortOrder()
    {
        return 10;
    }
}
