package com.guicedee.activitymaster.fsdm;

import com.guicedee.client.Environment;
import com.guicedee.client.IGuiceContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class FSDMMigrateGUI {

    // Text fields for user inputs
    private JTextField sourceUrlField;
    private JTextField sourceUserField;
    private JTextField sourcePasswordField;
    private JTextField sourceDbNameField;

    private JTextField destUrlField;
    private JTextField destUserField;
    private JTextField destPasswordField;
    private JTextField destDbNameField;

    private JTextField sourceClusterNameField;


    private JTextArea logArea; // Log output area
    private final List<String> logMessages = new LinkedList<>();


    public static void main(String[] args) {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4j2LogDelegateFactory");
        System.setProperty("log4j.level", "INFO");
        System.setProperty("hazelcast.metrics.enabled", "false");

        SwingUtilities.invokeLater(() -> {
            FSDMMigrateGUI gui = new FSDMMigrateGUI();
            gui.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        // Main application frame
        JFrame frame = new JFrame("FSDM Data Migration Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Input configuration panel for the database properties
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder("Database Configuration"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Source Database Configuration
        gbc.gridx = 0;
        gbc.gridy = 0;
        configPanel.add(new JLabel("Source URL:"), gbc);

        gbc.gridx = 1;
        sourceUrlField = new JTextField(30);
        sourceUrlField.setText("localhost:5432");
        configPanel.add(sourceUrlField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        configPanel.add(new JLabel("Source User:"), gbc);

        gbc.gridx = 1;
        sourceUserField = new JTextField(30);
        sourceUserField.setText("postgres");
        configPanel.add(sourceUserField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        configPanel.add(new JLabel("Source Password:"), gbc);

        gbc.gridx = 1;
        sourcePasswordField = new JPasswordField(30);
        configPanel.add(sourcePasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        configPanel.add(new JLabel("Source Database Name:"), gbc);

        gbc.gridx = 1;
        sourceDbNameField = new JTextField(30);
        sourceDbNameField.setText("fsdm");
        configPanel.add(sourceDbNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        configPanel.add(new JLabel("Source Cluster Name:"), gbc); // New label for source cluster name

        gbc.gridx = 1;
        sourceClusterNameField = new JTextField(30); // New text field for source cluster name
        sourceClusterNameField.setText("UWEv8");
        configPanel.add(sourceClusterNameField, gbc);


        // Destination Database Configuration
        gbc.gridx = 0;
        gbc.gridy = 5;
        configPanel.add(new JLabel("Destination URL:"), gbc);

        gbc.gridx = 1;
        destUrlField = new JTextField(30);
        destUrlField.setText("localhost:5432");
        configPanel.add(destUrlField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        configPanel.add(new JLabel("Destination User:"), gbc);

        gbc.gridx = 1;
        destUserField = new JTextField(30);
        destUserField.setText("postgres");
        configPanel.add(destUserField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        configPanel.add(new JLabel("Destination Password:"), gbc);

        gbc.gridx = 1;
        destPasswordField = new JPasswordField(30);
        configPanel.add(destPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        configPanel.add(new JLabel("Destination Database Name:"), gbc);

        gbc.gridx = 1;
        destDbNameField = new JTextField(30);
        destDbNameField.setText("uweassist");
        configPanel.add(destDbNameField, gbc);

        mainPanel.add(configPanel, BorderLayout.NORTH);

        // Log output area
        logArea = new JTextArea(20, 70);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Migration Log (Latest 200 Messages)"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        // Migrate button
        JButton migrateButton = new JButton("Migrate");
        migrateButton.addActionListener(new MigrateActionListener());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(migrateButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JButton removeConstraintsButton = new JButton("Remove Source Constraints");
        buttonPanel.add(removeConstraintsButton);
        removeConstraintsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    executeRemoveConstraints();
                } catch (Exception ex) {
                    appendLog("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        JButton updateVarcharToUUIDButton = new JButton("Update VARCHAR to UUID");
        buttonPanel.add(updateVarcharToUUIDButton);
        updateVarcharToUUIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    executeUpdateVarcharToUUID();
                } catch (Exception ex) {
                    appendLog("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });



        // Finalizing frame
        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void appendLog(String message) {
        SwingUtilities.invokeLater(() -> {
            // Add to logMessages list
            synchronized (logMessages) {
                logMessages.add(message);
                if (logMessages.size() > 200) {
                    logMessages.remove(0); // Remove the oldest message to maintain the limit
                }
            }

            // Update logArea with the latest messages
            logArea.setText(String.join("\n", logMessages));
        });
    }

    private void executeRemoveConstraints() throws Exception {
        // Get database connection information from input fields
        String sourceUrl = "jdbc:postgresql://" + sourceUrlField.getText() + "/" + sourceDbNameField.getText();
        String sourceUser = sourceUserField.getText();
        String sourcePassword = sourcePasswordField.getText();

        appendLog("Connecting to source database: " + sourceUrl);

        // Connect to the source database
        try (Connection connection = DriverManager.getConnection(sourceUrl, sourceUser, sourcePassword);
             Statement statement = connection.createStatement()) {

            appendLog("Connected to source database successfully.");

            // Execute the function to drop and execute foreign keys
            String createFunctionSql = """
                    CREATE OR REPLACE FUNCTION drop_and_execute_foreign_keys_for_specific_schemas()
                        RETURNS void AS $$
                    DECLARE
                        fk_table_name TEXT;
                        fk_schema_name TEXT;
                        foreign_key_name TEXT;
                        fk_drop_query TEXT;
                    BEGIN
                        FOR fk_schema_name, fk_table_name, foreign_key_name IN
                            SELECT
                                tc.table_schema,
                                tc.table_name,
                                tc.constraint_name
                            FROM information_schema.table_constraints tc
                            WHERE tc.constraint_type = 'FOREIGN KEY'
                              AND tc.table_schema IN (
                                                      'address', 'arrangement', 'classification', 'dbo', 'event',
                                                      'geography', 'party', 'product', 'resource', 'rules',
                                                      'security', 'time'
                                )
                        LOOP
                            fk_drop_query := format('ALTER TABLE %I.%I DROP CONSTRAINT %I;',
                                                    fk_schema_name, fk_table_name, foreign_key_name);
                            RAISE NOTICE '%', fk_drop_query;
                            EXECUTE fk_drop_query;
                        END LOOP;
                    END;
                    $$ LANGUAGE plpgsql;
                    """;

            statement.execute(createFunctionSql);
            appendLog("Function 'drop_and_execute_foreign_keys_for_specific_schemas' created successfully.");

            // Call the function
            String callFunctionSql = "SELECT drop_and_execute_foreign_keys_for_specific_schemas();";
            statement.execute(callFunctionSql);
            appendLog("Foreign key constraints dropped successfully from specified schemas.");
        } catch (Exception e) {
            appendLog("Error executing constraint removal: " + e.getMessage());
            throw e;
        }
    }

    private void executeUpdateVarcharToUUID() throws Exception {
        // Get database connection information from input fields
        String sourceUrl = "jdbc:postgresql://" + sourceUrlField.getText() + "/" + sourceDbNameField.getText();
        String sourceUser = sourceUserField.getText();
        String sourcePassword = sourcePasswordField.getText();

        appendLog("Connecting to source database: " + sourceUrl);

        // Connect to the source database
        try (Connection connection = DriverManager.getConnection(sourceUrl, sourceUser, sourcePassword);
             Statement statement = connection.createStatement()) {

            appendLog("Connected to source database successfully.");

            // Function to convert VARCHAR(36) to UUID
            String createFunctionSql = """
                    CREATE OR REPLACE FUNCTION update_varchar_to_uuid()
                        RETURNS void AS $$
                    DECLARE
                        v_table_name TEXT;
                        v_column_name TEXT;
                        v_schema_name TEXT;
                        fk_table_name TEXT;
                        fk_schema_name TEXT;
                        fk_column_name TEXT;
                        foreign_key_name TEXT;
                        fk_drop_query TEXT;
                        alter_column_query TEXT;
                    BEGIN
                        FOR v_schema_name, v_table_name, v_column_name IN
                            SELECT
                                c.table_schema,
                                c.table_name,
                                c.column_name
                            FROM information_schema.columns c
                            WHERE c.data_type = 'character varying'
                              AND c.character_maximum_length = 36
                              AND c.table_schema NOT IN ('postgres', 'public', 'pg_catalog', 'information_schema')
                              
                        LOOP
                            FOR foreign_key_name, fk_table_name, fk_schema_name, fk_column_name IN
                                SELECT
                                    tc.constraint_name,
                                    kcu.table_name AS fk_table,
                                    kcu.table_schema AS fk_schema,
                                    kcu.column_name AS fk_column
                                FROM information_schema.table_constraints tc
                                         JOIN information_schema.key_column_usage kcu
                                              ON tc.constraint_name = kcu.constraint_name
                                                  AND tc.table_schema = kcu.table_schema
                                WHERE tc.constraint_type = 'FOREIGN KEY'
                                  AND kcu.table_name = v_table_name
                                  AND kcu.column_name = v_column_name
                                  AND kcu.table_schema = v_schema_name
                            LOOP
                                fk_drop_query := format('ALTER TABLE %I.%I DROP CONSTRAINT IF EXISTS %I;',
                                                        fk_schema_name, fk_table_name, foreign_key_name);
                                RAISE NOTICE '%', fk_drop_query;
                                EXECUTE fk_drop_query;
                            END LOOP;

                            alter_column_query := format('ALTER TABLE %I.%I ALTER COLUMN %I TYPE UUID USING %I::UUID;',
                                                         v_schema_name, v_table_name, v_column_name, v_column_name);

                            RAISE NOTICE '%', alter_column_query;
                            EXECUTE alter_column_query;
                        END LOOP;
                    END;
                    $$ LANGUAGE plpgsql;
                    """;

            statement.execute(createFunctionSql);
            appendLog("Function 'update_varchar_to_uuid' created successfully.");

            String callFunctionSql = "SELECT update_varchar_to_uuid();";
            statement.execute(callFunctionSql);
            appendLog("VARCHAR(36) columns converted to UUID successfully.");

        } catch (Exception e) {
            appendLog("Error executing 'Update VARCHAR to UUID': " + e.getMessage());
            throw e;
        }
    }


    private class MigrateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Collect input values
            String sourceUrl = sourceUrlField.getText().trim();
            String sourceUser = sourceUserField.getText().trim();
            String sourcePassword = sourcePasswordField.getText().trim();
            String sourceDbName = sourceDbNameField.getText().trim();

            String sourceClusterName = sourceClusterNameField.getText().trim();

            String destUrl = destUrlField.getText().trim();
            String destUser = destUserField.getText().trim();
            String destPassword = destPasswordField.getText().trim();
            String destDbName = destDbNameField.getText().trim();

            // Set system properties
            System.setProperty("FSDM_DBSERVER", sourceUrl);
            System.setProperty("FSDM_USER", sourceUser);
            System.setProperty("PG_PASSWORD", sourcePassword);
            System.setProperty("FSDM_DBNAME", sourceDbName);

            System.setProperty("FSDM_DBSERVER_2", destUrl);
            System.setProperty("FSDM_USER_2", destUser);
            System.setProperty("PG_PASSWORD_2", destPassword);
            System.setProperty("FSDM_DBNAME_2", destDbName);

            System.setProperty("GROUP_NAME", sourceClusterName);
            System.setProperty("HAZELCAST", "true");
            System.setProperty("CLIENT_ADDRESS", "127.0.0.1:5701");

            appendLog("System properties set:");
            appendLog("Source - URL: " + sourceUrl + ", Database: " + sourceDbName);
            appendLog("Destination - URL: " + destUrl + ", Database: " + destDbName);

            // Run migration in a separate thread to keep the UI responsive
            Thread migrationThread = new Thread(() -> {
                try {
                    appendLog("Starting migration...");

                    // Set up source properties
                    Properties sourceProperties = new Properties();

                    sourceProperties.setProperty("hibernate.hbm2ddl.auto", "validate");


                    sourceProperties.setProperty("jakarta.persistence.jdbc.url", sourceUrl);
                    sourceProperties.setProperty("jakarta.persistence.jdbc.user", sourceUser);
                    sourceProperties.setProperty("jakarta.persistence.jdbc.password", sourcePassword);
                    sourceProperties.setProperty("jakarta.persistence.jdbc.url",
                            "jdbc:postgresql://" + sourceUrl + "/" + sourceDbName);
                    sourceProperties.setProperty("jakarta.persistence.jdbc.user", sourceUser);
                    sourceProperties.setProperty("jakarta.persistence.jdbc.password", sourcePassword);
                    sourceProperties.setProperty("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");

                    // Hibernate-specific properties for better configuration
                    sourceProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                    sourceProperties.setProperty("hibernate.hbm2ddl.auto", "none"); // Disable schema auto-generation for safety
                    sourceProperties.setProperty("hibernate.show_sql", "true");
                    sourceProperties.setProperty("hibernate.format_sql", "false");
                    sourceProperties.setProperty("hibernate.use_sql_comments", "false");


                    appendLog("Overriding JTA with resource-local (JPA)...");
                    sourceProperties.setProperty("jakarta.persistence.transactionType", "RESOURCE_LOCAL");
                    sourceProperties.setProperty("hibernate.transaction.coordinator_class", "jdbc");
                    sourceProperties.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");

                    sourceProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
                    sourceProperties.setProperty("hibernate.cache.use_query_cache", "false");
                    sourceProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.internal.NoCachingRegionFactory");


                    // Build source EntityManager
                    appendLog("Connecting to source database...");
                    jakarta.persistence.EntityManager sourceEntityManager =
                            jakarta.persistence.Persistence.createEntityManagerFactory("ActivityMaster", sourceProperties)
                                    .createEntityManager();
                    appendLog("Source connected successfully!");

                    // Pass sourceEntityManager to the migrateAll method
                    FSDMMigrate.migrateAll(sourceEntityManager,(message)->{
                        appendLog(message);
                    });

                    appendLog("Migration completed successfully!");
                } catch (Exception ex) {
                    appendLog("Error during migration: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            migrationThread.start();
        }
    }
}