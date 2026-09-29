package com.guicedee.activitymaster;

import com.guicedee.activitymaster.fsdm.db.ActivityMasterDBModule;
import com.guicedee.client.services.lifecycle.IGuiceModule;
import com.guicedee.persistence.ConnectionBaseInfo;
import com.guicedee.persistence.DatabaseModule;
import com.guicedee.persistence.annotations.EntityManager;
import com.guicedee.persistence.implementations.postgres.PostgresConnectionBaseInfo;
import jakarta.validation.constraints.NotNull;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import java.util.Properties;

@EntityManager(value = "ActivityMaster-Test", defaultEm = true)
public class PostgreSQLTestDBModule
        extends DatabaseModule<PostgreSQLTestDBModule>
        implements IGuiceModule<PostgreSQLTestDBModule>
{
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("fsdm")
            .withUsername("postgres")
            .withPassword("postgres")
            .withCommand("postgres", "-c", "shared_preload_libraries=pg_stat_statements");

    static {
        postgresContainer.start();
        try {
            // Prefer classpath resources so scripts work when consumed via test-jar
            //String fsdmResource = "postgres_fsdm.sql";
            System.setProperty("FSDM_DBSERVER", postgresContainer.getHost());
            System.setProperty("FSDM_DBPORT", String.valueOf(postgresContainer.getFirstMappedPort()));
            System.setProperty("FSDM_DBNAME", postgresContainer.getDatabaseName());
            System.setProperty("FSDM_USER", postgresContainer.getUsername());
            System.setProperty("FSDM_PASSWORD", postgresContainer.getPassword());

            String[] fsdmScripts = {
                        "00.1.setup.sql",
                        "01.enterprise.sql",
                        "02.activeflag.sql",
                        "03.systems.sql",
                        "04.classification-data-concept.sql",
                        "05.classification.sql",
                        "06.address.sql",
                        "07.arrangement.sql",
                        "08.product.sql",
                        "09.resourceitem.sql",
                        "10.party.sql",
                        "11.rules.sql",
                        "12.securitytoken.sql",
                        "13.event.sql",
                        "14.geography.sql",
                        "15.time.sql",
                        "16.transactions.sql",
                        "20.1.setup.sql"
                };

            // Copy and execute FSDM scripts in order
            Container.ExecResult fsdmResult = null;
                for (String script : fsdmScripts) {
                    postgresContainer.copyFileToContainer(
                            MountableFile.forClasspathResource("db/"+script),
                            "/tmp/" + script
                    );

                    fsdmResult = postgresContainer.execInContainer(
                            "psql",
                            "-v", "ON_ERROR_STOP=1",
                            "-U", postgresContainer.getUsername(),
                            "-d", postgresContainer.getDatabaseName(),
                            "-f", "/tmp/" + script
                    );

                    if (fsdmResult.getExitCode() != 0) {
                        System.err.println("[FSDM STDERR] " + fsdmResult.getStderr());
                        System.err.println("[FSDM STDOUT] " + fsdmResult.getStdout());
                        throw new RuntimeException("psql fsdm script execution failed: " + script);
                    }
                    System.out.println("✅ DB Script Executed Successfully: " + script);
                }
          //  String structureResource = "postgres_structure.sql";

            // Copy FSDM script from classpath into container and execute with ON_ERROR_STOP
          /*  postgresContainer.copyFileToContainer(
                    MountableFile.forClasspathResource(fsdmResource),
                    "/tmp/init_fsdm.sql"
            );*/

      /*      Container.ExecResult fsdmResult = postgresContainer.execInContainer(
                    "psql",
                    "-v", "ON_ERROR_STOP=1",
                    "-U", postgresContainer.getUsername(),
                    "-d", postgresContainer.getDatabaseName(),
                    "-f", "/tmp/init_fsdm.sql"
            );*/

          /*  if (fsdmResult.getExitCode() != 0) {
                System.err.println("[FSDM STDERR] " + fsdmResult.getStderr());
                System.err.println("[FSDM STDOUT] " + fsdmResult.getStdout());
                throw new RuntimeException("psql fsdm script execution failed: " + fsdmResult.getStderr());
            }
*/
            System.out.println("✅ DB FSDM Script Executed Successfully");

            // Copy Structure script from classpath into container and execute with ON_ERROR_STOP
          /*  postgresContainer.copyFileToContainer(
                    MountableFile.forClasspathResource(structureResource),
                    "/tmp/init_structure.sql"
            );

            Container.ExecResult structureResult = postgresContainer.execInContainer(
                    "psql",
                    "-v", "ON_ERROR_STOP=1",
                    "-U", postgresContainer.getUsername(),
                    "-d", postgresContainer.getDatabaseName(),
                    "-f", "/tmp/init_structure.sql"
            );

            if (structureResult.getExitCode() != 0) {
                System.err.println("[STRUCTURE STDERR] " + structureResult.getStderr());
                System.err.println("[STRUCTURE STDOUT] " + structureResult.getStdout());
                throw new RuntimeException("psql structure script execution failed: " + structureResult.getStderr());
            }*/

            //System.out.println("✅ DB Structure Script Executed Successfully:\n" + structureResult.getStdout());

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute SQL initialization scripts", e);
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

    // Expose the underlying Testcontainers instance and connection details for reuse in other modules/tests
    public static PostgreSQLContainer<?> getPostgresContainer() {
        return postgresContainer;
    }

    public static String getJdbcUrl() {
        return postgresContainer.getJdbcUrl();
    }

    public static String getUsername() {
        return postgresContainer.getUsername();
    }

    public static String getPassword() {
        return postgresContainer.getPassword();
    }

    public static String getDatabaseName() {
        return postgresContainer.getDatabaseName();
    }

    public static String getHost() {
        return postgresContainer.getHost();
    }

    public static int getPort() {
        return postgresContainer.getFirstMappedPort();
    }
}
