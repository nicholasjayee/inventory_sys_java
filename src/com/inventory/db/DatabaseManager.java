package com.inventory.db;

import com.inventory.app.AppConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DatabaseManager {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found in classpath.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        // Ensure database directory exists
        File dbFile = new File(AppConfig.getDbPath());
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        return DriverManager.getConnection(AppConfig.getDbUrl());
    }

    /**
     * Initializes the database and applies all pending migrations from the migrations directory.
     */
    public static void runMigrations() {
        System.out.println("Running database migrations...");
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create schema_migrations table if not exists
            stmt.execute("CREATE TABLE IF NOT EXISTS schema_migrations (version TEXT PRIMARY KEY);");
            
            // Scan migrations directory
            File migrationsDir = new File("migrations");
            if (!migrationsDir.exists() || !migrationsDir.isDirectory()) {
                System.out.println("Migrations directory not found, skipping migrations.");
                return;
            }

            File[] migrationFiles = migrationsDir.listFiles((dir, name) -> name.endsWith(".sql"));
            if (migrationFiles == null || migrationFiles.length == 0) {
                System.out.println("No migration files found.");
                return;
            }

            // Sort files alphabetically to ensure correct sequence
            Arrays.sort(migrationFiles, (f1, f2) -> f1.getName().compareTo(f2.getName()));

            for (File file : migrationFiles) {
                String migrationName = file.getName();
                
                // Check if this migration was already executed
                if (isMigrationApplied(conn, migrationName)) {
                    System.out.println("Migration already applied: " + migrationName);
                    continue;
                }

                System.out.println("Applying migration: " + migrationName + "...");
                try {
                    conn.setAutoCommit(false);
                    
                    // Read file content
                    String sqlContent = Files.readString(file.toPath());
                    
                    // Split SQL by semicolons to execute statements individually (simple parser)
                    String[] statements = sqlContent.split(";");
                    for (String sql : statements) {
                        String trimmedSql = sql.trim();
                        if (!trimmedSql.isEmpty()) {
                            stmt.execute(trimmedSql);
                        }
                    }
                    
                    // Log execution
                    try (PreparedStatement logStmt = conn.prepareStatement("INSERT INTO schema_migrations (version) VALUES (?)")) {
                        logStmt.setString(1, migrationName);
                        logStmt.executeUpdate();
                    }
                    
                    conn.commit();
                    System.out.println("Successfully applied: " + migrationName);
                } catch (Exception e) {
                    conn.rollback();
                    System.err.println("Failed to apply migration: " + migrationName);
                    e.printStackTrace();
                    throw new RuntimeException("Migration failed: " + migrationName, e);
                } finally {
                    conn.setAutoCommit(true);
                }
            }
            System.out.println("Database is up to date.");
        } catch (SQLException e) {
            System.err.println("Database connectivity error during migrations.");
            e.printStackTrace();
        }
    }

    private static boolean isMigrationApplied(Connection conn, String version) throws SQLException {
        String sql = "SELECT 1 FROM schema_migrations WHERE version = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, version);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Entry point for running migrations standalone from the command line.
     */
    public static void main(String[] args) {
        runMigrations();
    }
}
