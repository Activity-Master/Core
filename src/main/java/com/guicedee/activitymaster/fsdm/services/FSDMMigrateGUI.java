package com.guicedee.activitymaster.fsdm.services;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

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


    private JTextField backupDirectory;

    private JTextField sourceClusterNameField;

    private JProgressBar progressBar;


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
        frame.setSize(1200, 800);

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

        gbc.gridx = 0;
        gbc.gridy = 9;
        configPanel.add(new JLabel("Backup Directory:"), gbc);

        gbc.gridx = 1;
        backupDirectory = new JTextField(30);
        backupDirectory.setText("c:\\Java");
        configPanel.add(backupDirectory, gbc);


        mainPanel.add(configPanel, BorderLayout.NORTH);

        // Log output area
        logArea = new JTextArea(20, 70);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Migration Log (Latest 200 Messages)"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        JButton addDateColumns = new JButton("Add Date Columns");


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JPanel buttonsContainer = new JPanel(new GridLayout(3, 1));
        buttonsContainer.add(buttonPanel);
        buttonsContainer.add(buttonPanel2);


        mainPanel.add(buttonsContainer, BorderLayout.SOUTH);

        JButton backupDbButton = new JButton("Backup DB");
        JButton backupDataButton = new JButton("Backup Data");
        JButton restoreDataButton = new JButton("Restore Data");

        JButton createDbButton = new JButton("Create DB");

        JButton partitionDbButton = new JButton("Partition DB");
        JButton structureDbButton = new JButton("Structure DB");

        buttonPanel.add(backupDbButton);

        JButton removeConstraintsButton = new JButton("Remove Source Constraints");
        buttonPanel.add(removeConstraintsButton);
        removeConstraintsButton.addActionListener(e -> executeSQLScript("META-INF/scripts/drop_foreign_keys.sql", "Remove Constraints",false));

        JButton deleteIndexes = new JButton("Delete Indexes");
        buttonPanel.add(deleteIndexes);
        deleteIndexes.addActionListener(e -> executeSQLScript("META-INF/scripts/delete_indexes.sql", "Delete Indexes",false));


        JButton updateOriginSystemIDButton = new JButton("Update Origin Source IDs");
        buttonPanel.add(updateOriginSystemIDButton);
        updateOriginSystemIDButton.addActionListener(e -> executeSQLScript("META-INF/scripts/update_orirgin_system_id.sql", "Update Origin System IDs",false));

        JButton updateVarcharToUUIDButton = new JButton("Update VARCHAR to UUID");
        buttonPanel.add(updateVarcharToUUIDButton);
        updateVarcharToUUIDButton.addActionListener(e -> executeSQLScript("META-INF/scripts/update_to_uuid.sql", "Update UUIDs",false));

        buttonPanel2.add(backupDataButton);
        buttonPanel2.add(createDbButton);
        buttonPanel2.add(partitionDbButton);
        buttonPanel2.add(restoreDataButton);
        buttonPanel2.add(structureDbButton);

        backupDbButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backupDb();
            }
        });

        backupDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backupData();
            }
        });

        restoreDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreData();
            }
        });


        createDbButton.addActionListener(e -> executeSQLScript("META-INF/postgres_fsdm.sql", "Create Database",true));
        partitionDbButton.addActionListener(e -> executeSQLScript("META-INF/postgres_partitions.sql", "Partition Database",true));
        structureDbButton.addActionListener(e -> executeSQLScript("META-INF/postgres_structure.sql", "Structure Database",true));

        addDateColumns.addActionListener(e -> executeSQLScript("META-INF/scripts/2026_migration_prep.sql", "Add Date Columns",false));

        buttonPanel.add(addDateColumns);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true); // Shows "Running..." as text on the progress bar
        progressBar.setIndeterminate(false); // Start with no progress
        progressBar.setVisible(false); // Hide initially

        // Add progress bar to your GUI layout (example for BorderLayout)
        buttonsContainer.add(progressBar, BorderLayout.SOUTH);

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

    private void backupDb() {
        // Build the command to execute a full backup
        String command = "pg_dump.exe --file \"" + backupDirectory.getText() + "\\fullbackup.backup\" --host \"localhost\" --port \"5432\" --username \"postgres\" --no-password --format=c --large-objects --verbose \"" + sourceDbNameField.getText() + "\"";

        // Run the task in a SwingWorker to avoid freezing the GUI
        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    publish("Starting full backup...");

                    ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                    processBuilder.environment().put("PGPASSWORD", sourcePasswordField.getText()); // Add the password as an environment variable
                    processBuilder.redirectErrorStream(true);

                    // Start the process
                    Process process = processBuilder.start();

                    // Capture and publish output in real-time
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
                    {
                        String line;
                        while ((line = reader.readLine()) != null)
                        {
                            publish(line); // Safely send log data to the GUI
                        }

                        // Wait for the process to finish
                        int exitCode = process.waitFor();
                        if (exitCode == 0)
                        {
                            publish("Full backup completed successfully.");
                        }
                        else
                        {
                            publish("Full backup failed with exit code: " + exitCode);
                        }
                    }
                } catch (Exception ex) {
                    publish("Error during full backup: " + ex.getMessage());
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // Safely update the log area on the Event Dispatch Thread with published messages
                for (String message : chunks) {
                    appendLog(message);
                }
            }

            @Override
            protected void done() {
                appendLog("Backup process finished.");
            }
        }.execute();
    }

    private void backupData() {
        String command = "pg_dump.exe --file \"" + backupDirectory.getText() + "\\data.backup\" --host \"localhost\" --port \"5432\" --username \"" + sourceUserField.getText()+
                "\" --no-password --format=t --large-objects --data-only --no-owner --no-privileges --no-tablespaces --no-unlogged-table-data --no-comments --no-publications --no-subscriptions --no-security-labels --no-toast-compression --no-table-access-method --inserts --on-conflict-do-nothing --column-inserts --verbose --exclude-schema \"information_schema\" --exclude-schema \"postgres\" --exclude-schema \"pg_toast\" \"" + sourceDbNameField.getText() + "\"";

        // Create SwingWorker that returns exitCode and publishes log messages
        SwingWorker<Integer, String> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                publish("Starting backup...");

                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                processBuilder.redirectErrorStream(true);
                processBuilder.environment().put("PGPASSWORD", sourcePasswordField.getText());
                Process process = processBuilder.start();

                // Capture and publish output
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    publish(line);
                }

                return process.waitFor();
            }

            @Override
            protected void process(List<String> chunks) {
                // Update UI with log messages on EDT
                for (String message : chunks) {
                    appendLog(message);
                }
            }

            @Override
            protected void done() {
                try {
                    int exitCode = get(); // Get the result from doInBackground
                    if (exitCode == 0) {
                        appendLog("Backup completed successfully.");
                    } else {
                        appendLog("Backup failed with exit code: " + exitCode);
                    }
                } catch (Exception ex) {
                    appendLog("Error during backup: " + ex.getMessage());
                }
            }
        };

        // Start the worker
        worker.execute();
    }


    private void restoreData() {
        String command = "pg_restore.exe --host \"localhost\" --port \"5432\" --username \"" + destUserField.getText()+ "\"" +
                " --no-password --dbname \"" + destDbNameField.getText()+ "\" --data-only --verbose \"" + backupDirectory.getText() + "\\data.backup" + "\"";

        SwingWorker<Integer, String> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                publish("Starting restore...");

                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                processBuilder.redirectErrorStream(true);
                processBuilder.environment().put("PGPASSWORD", sourcePasswordField.getText());
                Process process = processBuilder.start();

                // Capture and publish output
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    publish(line);
                }

                return process.waitFor();
            }

            @Override
            protected void process(List<String> chunks) {
                // Update UI with log messages on EDT
                for (String message : chunks) {
                    appendLog(message);
                }
            }

            @Override
            protected void done() {
                try {
                    int exitCode = get();
                    if (exitCode == 0) {
                        appendLog("Restore completed successfully.");
                    } else {
                        appendLog("Restore failed with exit code: " + exitCode);
                    }
                } catch (Exception ex) {
                    appendLog("Error during restore: " + ex.getMessage());
                }
            }
        };

        // Start the worker
        worker.execute();
    }




    private void executeSQLScript(String filePath, String actionName, boolean destination) {
        // Use SwingWorker to handle the execution asynchronously
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Adjust this if `psql` is not in the system's PATH.
                String psqlPath = "psql.exe";
                String dbName = destination ? destDbNameField.getText() : sourceDbNameField.getText();
                String host = "localhost";
                String port = "5432";
                String username = destination ? destUserField.getText() : sourceUserField.getText();
                String password =  destination ? destPasswordField.getText() : sourcePasswordField.getText();

                try {
                    publish("Starting " + actionName + "...");
                    setProgress(0); // Reset progress to 0

                    // Locate the SQL file (it might be in the classpath or filesystem)
                    InputStream sqlFileInputStream = getClass().getClassLoader().getResourceAsStream(filePath);
                    File sqlFile;

                    if (sqlFileInputStream != null) {
                        // File is in the JAR or classpath. Extract to a temporary file.
                        publish("Extracting SQL file from classpath...");
                        sqlFile = File.createTempFile("temp-", ".sql");
                        sqlFile.deleteOnExit();

                        try (FileOutputStream outputStream = new FileOutputStream(sqlFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = sqlFileInputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        }
                        publish("SQL file extracted to: " + sqlFile.getAbsolutePath());
                    } else {
                        // File is expected to be on the filesystem.
                        sqlFile = new File(filePath);

                        if (!sqlFile.exists()) {
                            publish("Error: File not found: " + filePath);
                            return null;
                        }
                    }

                    // Run the `psql` command
                    String command = String.format(
                            "%s --host=%s --port=%s --username=%s --dbname=%s --file=\"%s\"",
                            psqlPath, host, port, username, dbName, sqlFile.getAbsolutePath()
                    );

                    ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                    // Set environment variable for password (if needed)
                    processBuilder.environment().put("PGPASSWORD", password);

                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();

                    // Capture and log the process output
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
                    {
                        String line;

                        // Assign an arbitrary progress interval while reading the output
                        int totalLines = 0; // Tracks line count while reading output
                        while ((line = reader.readLine()) != null)
                        {
                            totalLines++;
                            publish(line);
                            if (totalLines % 10 == 0)
                            { // Update progress every 10 lines
                                setProgress(Math.min(totalLines, 100)); // Smoothly update progress to a max of 100
                            }
                        }

                        int exitCode = process.waitFor();
                        if (exitCode == 0)
                        {
                            publish(actionName + " completed successfully.");
                        }
                        else
                        {
                            publish(actionName + " failed with exit code: " + exitCode);
                        }
                    }
                    setProgress(100); // Complete progress on success

                } catch (Exception ex) {
                    publish("Error during " + actionName + ": " + ex.getMessage());
                }

                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // This runs on the EDT to update the GUI with messages
                for (String message : chunks) {
                    appendLog(message); // Append messages to the log text area
                }
            }

            @Override
            protected void done() {
                // This runs on the EDT after the background task completes
                progressBar.setVisible(false); // Hide the progress bar
                progressBar.setIndeterminate(false); // Reset indeterminate state
                setProgress(0); // Reset progress
                appendLog(actionName + " process complete.");
            }
        };

        // Initialize progress bar before starting the worker
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true); // Start with an indeterminate state while initializing
        worker.execute(); // Start the SwingWorker
    }

}