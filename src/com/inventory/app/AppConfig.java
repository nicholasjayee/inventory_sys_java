package com.inventory.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = new FileInputStream("metadata/app.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Warning: Could not load metadata/app.properties. Using default settings.");
            properties.setProperty("app.name", "Botanical Logistics");
            properties.setProperty("app.version", "1.0.0");
            properties.setProperty("db.path", "db/inventory.db");
            properties.setProperty("app.main.class", "com.inventory.app.Main");
        }
    }

    public static String getAppName() {
        return properties.getProperty("app.name", "Botanical Logistics");
    }

    public static String getAppVersion() {
        return properties.getProperty("app.version", "1.0.0");
    }

    public static String getDbPath() {
        return properties.getProperty("db.path", "db/inventory.db");
    }

    public static String getDbUrl() {
        return "jdbc:sqlite:" + getDbPath();
    }
}
