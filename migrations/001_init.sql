-- Migration 001: Initialize Database Schema

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    display_name TEXT NOT NULL,
    role TEXT NOT NULL
);

-- Items Table
CREATE TABLE IF NOT EXISTS items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    price REAL NOT NULL DEFAULT 0.0,
    status TEXT NOT NULL
);

-- Insert a default admin user if it doesn't exist
INSERT OR IGNORE INTO users (id, username, password_hash, display_name, role)
VALUES (1, 'admin', 'admin', 'Operations Manager', 'Admin');

-- Insert initial sample botanical inventory items
INSERT INTO items (name, category, quantity, price, status) VALUES
('Rosehip Seed Facial Oil', 'Serums', 48, 42.00, 'In Stock'),
('Chamomile Calming Mist', 'Toners', 8, 28.00, 'Low Stock'),
('Jasmine Absolute Body Butter', 'Creams', 15, 65.00, 'In Stock'),
('Eucalyptus Mint Scrub', 'Exfoliators', 0, 32.00, 'Out of Stock'),
('Neroli Replenishing Cream', 'Creams', 24, 85.00, 'In Stock');
