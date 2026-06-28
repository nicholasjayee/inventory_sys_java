-- Migration 001: Initialize Database Schema with UUID and User-relation support

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT UNIQUE NOT NULL,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    display_name TEXT NOT NULL,
    role TEXT NOT NULL
);

-- Items Table (Tied to the user who created them)
CREATE TABLE IF NOT EXISTS items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT UNIQUE NOT NULL,
    user_uuid TEXT NOT NULL,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    price REAL NOT NULL DEFAULT 0.0,
    status TEXT NOT NULL,
    FOREIGN KEY(user_uuid) REFERENCES users(uuid)
);

-- Insert a default admin user
INSERT OR IGNORE INTO users (id, uuid, username, password_hash, display_name, role)
VALUES (1, '33331b26-f716-4df6-a2cb-13f1c20f41e7', 'admin', 'admin', 'Operations Manager', 'Admin');
