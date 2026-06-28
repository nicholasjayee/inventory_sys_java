-- Migration 001: Initialize Database Schema with UUID support

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT UNIQUE NOT NULL,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    display_name TEXT NOT NULL,
    role TEXT NOT NULL
);

-- Items Table
CREATE TABLE IF NOT EXISTS items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid TEXT UNIQUE NOT NULL,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    price REAL NOT NULL DEFAULT 0.0,
    status TEXT NOT NULL
);

-- Insert a default admin user with a generated UUID
INSERT OR IGNORE INTO users (id, uuid, username, password_hash, display_name, role)
VALUES (1, '33331b26-f716-4df6-a2cb-13f1c20f41e7', 'admin', 'admin', 'Operations Manager', 'Admin');

-- Insert initial sample botanical inventory items with unique UUIDs
INSERT INTO items (uuid, name, category, quantity, price, status) VALUES
('fa8d4073-61ab-40a2-ad21-dc2b04ea9101', 'Rosehip Seed Facial Oil', 'Serums', 48, 42.00, 'In Stock'),
('fa8d4073-61ab-40a2-ad21-dc2b04ea9102', 'Chamomile Calming Mist', 'Toners', 8, 28.00, 'Low Stock'),
('fa8d4073-61ab-40a2-ad21-dc2b04ea9103', 'Jasmine Absolute Body Butter', 'Creams', 15, 65.00, 'In Stock'),
('fa8d4073-61ab-40a2-ad21-dc2b04ea9104', 'Eucalyptus Mint Scrub', 'Exfoliators', 0, 32.00, 'Out of Stock'),
('fa8d4073-61ab-40a2-ad21-dc2b04ea9105', 'Neroli Replenishing Cream', 'Creams', 24, 85.00, 'In Stock');
