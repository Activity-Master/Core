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

    // Controls global enable/disable state of all buttons during background tasks
    private final Object uiStateLock = new Object();
    private int runningTasks = 0; // number of active background tasks

    private void markTaskStart() {
        synchronized (uiStateLock) {
            boolean wasIdle = (runningTasks == 0);
            runningTasks++;
            if (wasIdle) {
                setAllButtonsEnabled(false);
            }
        }
    }

    private void markTaskEnd() {
        synchronized (uiStateLock) {
            if (runningTasks > 0) {
                runningTasks--;
            }
            if (runningTasks == 0) {
                setAllButtonsEnabled(true);
            }
        }
    }

    private void setAllButtonsEnabled(boolean enabled) {
        // Ensure this runs on the EDT
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> setAllButtonsEnabled(enabled));
            return;
        }
        Window window = SwingUtilities.getWindowAncestor(progressBar != null ? progressBar : logArea);
        if (window == null) {
            // Fallback to any known component
            JRootPane rootPane = SwingUtilities.getRootPane(logArea);
            if (rootPane != null) {
                toggleButtonsRecursive(rootPane.getContentPane(), enabled);
            }
            return;
        }
        if (window instanceof JFrame frame) {
            toggleButtonsRecursive(frame.getContentPane(), enabled);
        } else if (window instanceof JDialog dialog) {
            toggleButtonsRecursive(dialog.getContentPane(), enabled);
        }
    }

    private void toggleButtonsRecursive(Container container, boolean enabled) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof AbstractButton) {
                comp.setEnabled(enabled);
            }
            if (comp instanceof Container) {
                toggleButtonsRecursive((Container) comp, enabled);
            }
        }
    }

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
        // Set application icon
        try {
            java.net.URL iconUrl = FSDMMigrateGUI.class.getResource("/logo_activitymaster.png");
            if (iconUrl != null) {
                ImageIcon appIcon = new ImageIcon(iconUrl);
                frame.setIconImage(appIcon.getImage());
            }
        } catch (Exception ignore) {
            // If the icon cannot be loaded, continue without setting it
        }

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
        sourceClusterNameField.setText("UWEv9");
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


        JButton removeDuplicateIndexes = new JButton("Remove Indexes");
        JButton fixDateColumns = new JButton("Add Dates 2026");
        JButton addDateColumns = new JButton("Configure for 2026");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Create a new panel for the "Run All" button
        JPanel runAllPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton runAllButton = new JButton("Run All");
        runAllButton.setFont(new Font(runAllButton.getFont().getName(), Font.BOLD, 14));
        runAllButton.setBackground(new Color(0, 153, 0)); // Green background
        runAllButton.setForeground(Color.WHITE); // White text
        runAllButton.setPreferredSize(new Dimension(150, 40));
        runAllPanel.add(runAllButton);

        // Add action listener for the "Run All" button
        runAllButton.addActionListener(e -> runAllFunctions());

        JPanel buttonsContainer = new JPanel(new GridLayout(4, 1));
        buttonsContainer.add(runAllPanel);
        buttonsContainer.add(buttonPanel);
        buttonsContainer.add(buttonPanel2);


        mainPanel.add(buttonsContainer, BorderLayout.SOUTH);

        JButton backupDbButton = new JButton("Backup DB");
        JButton backupDataButton = new JButton("Backup Data");
        JButton restoreDataButton = new JButton("Restore Data");

        JButton createDbButton = new JButton("Create DB");

        JButton partitionDbButton = new JButton("Partition DB");
        partitionDbButton.setEnabled(false);
        JButton structureDbButton = new JButton("Structure DB");
        JButton structureFsdmDbButton = new JButton("Structure FSDM DB");

        buttonPanel.add(backupDbButton);

        JButton removeConstraintsButton = new JButton("Remove Constraints");
        buttonPanel.add(removeConstraintsButton);
        removeConstraintsButton.addActionListener(e -> executeSQLScript("META-INF/scripts/drop_foreign_keys.sql", "Remove Constraints",false));

        JButton deleteIndexes = new JButton("Delete Indexes");
        deleteIndexes.setEnabled(true);
        buttonPanel.add(deleteIndexes);
        deleteIndexes.addActionListener(e -> executeSQLScript("META-INF/scripts/delete_indexes.sql", "Delete Indexes",false));


        JButton updateOriginSystemIDButton = new JButton("Update Origin");
        updateOriginSystemIDButton.setEnabled(true);
        buttonPanel.add(updateOriginSystemIDButton);
        updateOriginSystemIDButton.addActionListener(e -> executeSQLScript("META-INF/scripts/update_orirgin_system_id.sql", "Update Origin System IDs",false));

        JButton updateVarcharToUUIDButton = new JButton("Update VARCHAR");
        buttonPanel.add(updateVarcharToUUIDButton);
        updateVarcharToUUIDButton.addActionListener(e -> executeSQLScript("META-INF/scripts/update_to_uuid.sql", "Update UUIDs",false));

        buttonPanel2.add(backupDataButton);
        buttonPanel2.add(createDbButton);
        buttonPanel2.add(partitionDbButton);
        buttonPanel2.add(restoreDataButton);
        buttonPanel2.add(structureDbButton);
        buttonPanel2.add(structureFsdmDbButton);

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
        structureFsdmDbButton.addActionListener(e -> executeSQLScript("META-INF/postgres_fsdm_structure.sql", "Structure Database",true));

        removeDuplicateIndexes.addActionListener(e -> executeSQLScript("META-INF/scripts/2026_remove_duplicate_indexes.sql", "Remove Duplicate Indexes",false));
        addDateColumns.addActionListener(e -> executeSQLScript("META-INF/scripts/2026_migration_prep.sql", "Add Date Columns",false));
        fixDateColumns.addActionListener(e -> executeSQLScript("META-INF/scripts/add_warehousecreatedate_column.sql", "Fix Date Columns",false));

        buttonPanel.add(removeDuplicateIndexes);
        buttonPanel.add(addDateColumns);
        buttonPanel.add(fixDateColumns);

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
            markTaskStart();
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
                try {
                    appendLog("Backup process finished.");
                } finally {
                    markTaskEnd();
                }
            }
        }.execute();
    }

    private void backupData() {
            markTaskStart();
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
                } finally {
                    markTaskEnd();
                }
            }
        };

        // Start the worker
        worker.execute();
    }


    private void restoreData() {
            markTaskStart();
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
                } finally {
                    markTaskEnd();
                }
            }
        };

        // Start the worker
        worker.execute();
    }




    private void executeSQLScript(String filePath, String actionName, boolean destination) {
            markTaskStart();
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
                try {
                    progressBar.setVisible(false); // Hide the progress bar
                    progressBar.setIndeterminate(false); // Reset indeterminate state
                    setProgress(0); // Reset progress
                    appendLog(actionName + " process complete.");
                } finally {
                    markTaskEnd();
                }
            }
        };

        // Initialize progress bar before starting the worker
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true); // Start with an indeterminate state while initializing
        worker.execute(); // Start the SwingWorker
    }

    /**
     * Executes all migration functions in the specified order
     */
    private void runAllFunctions() {
            markTaskStart();
        appendLog("Starting Run All process...");

        // Create a sequential executor for the tasks
        executeSequentialTasks(0, () -> {
            try {
                appendLog("Run All process finished.");
                progressBar.setVisible(false);
                progressBar.setIndeterminate(false);
            } finally {
                markTaskEnd();
            }
        });
    }

    /**
     * Executes tasks sequentially with proper handling of asynchronous operations
     * @param taskIndex The index of the current task
     * @param onComplete Callback to run when all tasks are complete
     */
    private void executeSequentialTasks(int taskIndex, Runnable onComplete) {
        // Define all tasks in the sequence
        Runnable[] tasks = new Runnable[] {
            // Task 1: Backup DB
            () -> {
                appendLog("Step 1/12: Backing up database...");
                executeWithCallback(() -> {
                    String command = "pg_dump.exe --file \"" + backupDirectory.getText() + "\\fullbackup.backup\" --host \"localhost\" --port \"5432\" --username \"postgres\" --no-password --format=c --large-objects --verbose \"" + sourceDbNameField.getText() + "\"";

                    try {
                        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                        processBuilder.environment().put("PGPASSWORD", sourcePasswordField.getText());
                        processBuilder.redirectErrorStream(true);
                        Process process = processBuilder.start();

                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                final String logLine = line;
                                SwingUtilities.invokeLater(() -> appendLog(logLine));
                            }

                            int exitCode = process.waitFor();
                            final String resultMessage = exitCode == 0 ? 
                                "Full backup completed successfully." : 
                                "Full backup failed with exit code: " + exitCode;

                            SwingUtilities.invokeLater(() -> appendLog(resultMessage));
                        }
                    } catch (Exception ex) {
                        final String errorMsg = "Error during full backup: " + ex.getMessage();
                        SwingUtilities.invokeLater(() -> appendLog(errorMsg));
                    }
                }, () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 2: Remove Source Constraints
            () -> {
                appendLog("Step 2/12: Removing source constraints...");
                executeSQLScriptWithCallback("META-INF/scripts/drop_foreign_keys.sql", "Remove Constraints", false,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 3: Remove Indexes
            () -> {
                appendLog("Step 3/12: Removing indexes...");
                executeSQLScriptWithCallback("META-INF/scripts/2026_remove_duplicate_indexes.sql", "Remove Duplicate Indexes", false,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 4: Delete Indexes
            () -> {
                appendLog("Step 4/12: Deleting indexes...");
                executeSQLScriptWithCallback("META-INF/scripts/delete_indexes.sql", "Delete Indexes", false,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 5: Update Origin Source IDs
            () -> {
                appendLog("Step 5/12: Updating origin source IDs...");
                executeSQLScriptWithCallback("META-INF/scripts/update_orirgin_system_id.sql", "Update Origin System IDs", false,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 6: Update VARCHAR to UUID
            () -> {
                appendLog("Step 6/12: Updating VARCHAR to UUID...");
                executeSQLScriptWithCallback("META-INF/scripts/update_to_uuid.sql", "Update UUIDs", false,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 7: Configure for 2026
            () -> {
                appendLog("Step 7/12: Configuring for 2026...");
                executeSQLScriptWithCallback("META-INF/scripts/2026_migration_prep.sql", "Fix Date Columns", false,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 7.1: Configure for 2026
            () -> {
                appendLog("Step 7/12: Configuring for 2026...");
                executeSQLScriptWithCallback("META-INF/scripts/add_warehousecreatedate_column.sql", "Add Date Columns", false,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },
            // Task 7.2: Configure for 2026
            () -> {
                appendLog("Step 7/12: Structure FSDM...");
                executeSQLScriptWithCallback("META-INF/scripts/postgres_fsdm_structure.sql", "Structure FSDM", false,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 8: Backup Data
            () -> {
                appendLog("Step 8/12: Backing up data...");
                executeWithCallback(() -> {
                    String command = "pg_dump.exe --file \"" + backupDirectory.getText() + "\\data.backup\" --host \"localhost\" --port \"5432\" --username \"" + sourceUserField.getText()+
                        "\" --no-password --format=t --large-objects --data-only --no-owner --no-privileges --no-tablespaces --no-unlogged-table-data --no-comments --no-publications --no-subscriptions --no-security-labels --no-toast-compression --no-table-access-method --inserts --on-conflict-do-nothing --column-inserts --verbose --exclude-schema \"information_schema\" --exclude-schema \"postgres\" --exclude-schema \"pg_toast\" \"" + sourceDbNameField.getText() + "\"";

                    try {
                        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                        processBuilder.redirectErrorStream(true);
                        processBuilder.environment().put("PGPASSWORD", sourcePasswordField.getText());
                        Process process = processBuilder.start();

                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                final String logLine = line;
                                SwingUtilities.invokeLater(() -> appendLog(logLine));
                            }

                            int exitCode = process.waitFor();
                            final String resultMessage = exitCode == 0 ? 
                                "Backup completed successfully." : 
                                "Backup failed with exit code: " + exitCode;

                            SwingUtilities.invokeLater(() -> appendLog(resultMessage));
                        }
                    } catch (Exception ex) {
                        final String errorMsg = "Error during backup: " + ex.getMessage();
                        SwingUtilities.invokeLater(() -> appendLog(errorMsg));
                    }
                }, () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 9: Create DB
            () -> {
                appendLog("Step 9/12: Creating database...");
                executeSQLScriptWithCallback("META-INF/postgres_fsdm.sql", "Create Database", true,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 10: Partition DB
            () -> {
                appendLog("Step 10/12: Partitioning database...");
                executeSQLScriptWithCallback("META-INF/postgres_partitions.sql", "Partition Database", true,
                    () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 11: Restore Data
            () -> {
                appendLog("Step 11/12: Restoring data...");
                executeWithCallback(() -> {
                    String command = "pg_restore.exe --host \"localhost\" --port \"5432\" --username \"" + destUserField.getText()+ "\"" +
                        " --no-password --dbname \"" + destDbNameField.getText()+ "\" --data-only --verbose \"" + backupDirectory.getText() + "\\data.backup" + "\"";

                    try {
                        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
                        processBuilder.redirectErrorStream(true);
                        processBuilder.environment().put("PGPASSWORD", sourcePasswordField.getText());
                        Process process = processBuilder.start();

                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                final String logLine = line;
                                SwingUtilities.invokeLater(() -> appendLog(logLine));
                            }

                            int exitCode = process.waitFor();
                            final String resultMessage = exitCode == 0 ? 
                                "Restore completed successfully." : 
                                "Restore failed with exit code: " + exitCode;

                            SwingUtilities.invokeLater(() -> appendLog(resultMessage));
                        }
                    } catch (Exception ex) {
                        final String errorMsg = "Error during restore: " + ex.getMessage();
                        SwingUtilities.invokeLater(() -> appendLog(errorMsg));
                    }
                }, () -> executeSequentialTasks(taskIndex + 1, onComplete));
            },

            // Task 12: Structure DB
            () -> {
                appendLog("Step 12/12: Structuring database...");
                executeSQLScriptWithCallback("META-INF/postgres_structure.sql", "Structure Database", true,
                    () -> {
                        appendLog("Run All process completed successfully!");
                        onComplete.run();
                    });
            }
        };

        // Execute the current task if within bounds
        if (taskIndex < tasks.length) {
            tasks[taskIndex].run();
        } else {
            // All tasks completed
            onComplete.run();
        }
    }

    /**
     * Helper method to execute a task with a callback when complete
     */
    private void executeWithCallback(Runnable task, Runnable callback) {
            markTaskStart();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                task.run();
                return null;
            }

            @Override
            protected void done() {
                try {
                    callback.run();
                } finally {
                    markTaskEnd();
                }
            }
        };
        worker.execute();
    }

    /**
     * Helper method to execute an SQL script with a callback when complete
     */
    private void executeSQLScriptWithCallback(String filePath, String actionName, boolean destination, Runnable callback) {
            markTaskStart();
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
                // Call the callback when done
                try {
                    callback.run();
                } finally {
                    markTaskEnd();
                }
            }
        };

        // Start the worker
        worker.execute();
    }
}
