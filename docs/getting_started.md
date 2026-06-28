# Getting Started Guide

Welcome to the **Botanical Logistics** desktop inventory management application. This guide will walk you through the prerequisites, installation steps, database setup, compilation, and launch commands.

---

## 📋 Prerequisites

Before running the application, ensure your environment has the following software installed:

1. **Java JDK 21 (or newer)**
   * Verify using: `java -version` and `javac -version`
2. **SQLite 3**
   * Pre-packaged on macOS and most Linux distributions. Verify using: `sqlite3 --version`
3. **Git**
   * To pull updates and manage changes. Verify using: `git --version`

---

## 📥 Setup & Dependencies

The project is structured to run out-of-the-box without complex configuration. All libraries and resources are bundled within the repository:
* **Libraries (`libs/`)**: Contains FlatLaf look-and-feel, SVG Salamander, SQLite JDBC, and SLF4J logger jars.
* **Fonts (`fonts/`)**: Contains the corporate typography files (Inter and Merriweather).
* **Metadata (`metadata/`)**: Contains centralized parameters like application metadata and database paths in `app.properties`.

---

## 🗄️ Database Initialization

The database schema and initial seed data are managed using localized migrations in the `migrations/` folder. Run the following command to initialize or upgrade the database:

### macOS / Linux:
```bash
./migrate.sh
```

### Windows:
```cmd
migrate.bat
```

This will compile the Java compiler targets and run the [DatabaseManager](file:///home/code8/Desktop/inventory/src/com/inventory/db/DatabaseManager.java) class standalone. A tracking table `schema_migrations` keeps record of already executed scripts in the local SQLite database (`db/inventory.db`).

---

## 🌍 Localization & Seeding Data

By default, the application is configured for East African operations (Kenyan Shillings - KES). 

You can alter the global currency and branding instantly by editing:
`metadata/app.properties`

If you are initializing the database for the first time, you can auto-populate it with sample East African botanical ingredients (e.g., Macadamia Nut Oil, Baobab powder) by running the Python seeder utility:
```bash
python3 tools/seed_items.py
```

---

## 🚀 Running the Application

To compile all files and launch the graphical interface:

### macOS / Linux:
```bash
./run.sh
```

### Windows:
```cmd
run.bat
```

The scripts automatically detect your operating system's classpath requirements and compiler settings.

---

## 🔐 Default Credentials

To sign in upon launching the application:
* **Username**: `admin`
* **Password**: `admin`
