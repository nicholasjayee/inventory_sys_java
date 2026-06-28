#!/usr/bin/env python3
import sqlite3
import os
import uuid
import sys

def get_db_path():
    properties_path = "metadata/app.properties"
    db_path = "db/inventory.db"
    
    if os.path.exists(properties_path):
        try:
            with open(properties_path, "r") as f:
                for line in f:
                    if line.strip().startswith("db.path="):
                        db_path = line.strip().split("=", 1)[1].strip()
                        break
        except Exception as e:
            print(f"Warning: Could not read metadata/app.properties ({e}). Using default: {db_path}", file=sys.stderr)
    return db_path

def main():
    print("=========================================")
    print(" Aramweer - Database Seeder Utility (CLI) ")
    print("=========================================")
    
    db_path = get_db_path()
    if not os.path.exists(db_path):
        print(f"Database file does not exist yet: {db_path}")
        print("Please launch the application or run migrate.sh first.")
        sys.exit(1)

    try:
        conn = sqlite3.connect(db_path)
        cursor = conn.cursor()
        
        # 1. Fetch first user in the database to tie the items to
        cursor.execute("SELECT uuid, username FROM users LIMIT 1")
        row = cursor.fetchone()
        if not row:
            # Seed default admin user first if database is entirely wiped
            admin_uuid = "33331b26-f716-4df6-a2cb-13f1c20f41e7"
            cursor.execute("""
                INSERT OR IGNORE INTO users (uuid, username, password_hash, display_name, role)
                VALUES (?, 'admin', 'admin', 'Operations Manager', 'Admin')
            """, (admin_uuid,))
            user_uuid = admin_uuid
            print("No users found. Seeding default admin user.")
        else:
            user_uuid = row[0]
            print(f"Linking seeded items to user: {row[1]} ({user_uuid})")

        # 2. Mockup raw items list
        mock_items = [
            ("Organic Macadamia Nut Oil", "Raw Items", "RAW-MO-001", 50, "Kenya", 250, 450.00, "In Stock"),
            ("Pure Eucalyptus Essential Oil", "Raw Items", "RAW-EO-012", 15, "Kenya", 8, 2500.00, "Low Stock"),
            ("Raw Baobab Fruit Powder", "Raw Items", "RAW-BP-044", 20, "Kenya", 85, 1250.00, "In Stock"),
            ("Moringa Oleifera Seeds", "Raw Items", "RAW-MS-089", 15, "Kenya", 120, 800.00, "In Stock"),
            ("Unrefined Shea Butter", "Raw Items", "RAW-SB-023", 30, "Uganda", 45, 950.00, "In Stock"),
            ("Yellow Beeswax Pellets", "Raw Items", "RAW-BP-007", 25, "Tanzania", 300, 600.00, "In Stock")
        ]

        # 3. Seed items
        seeded_count = 0
        for name, category, sku, reorder, origin, qty, price, status in mock_items:
            item_uuid = str(uuid.uuid4())
            # Double check existence to prevent duplicates on multiple runs
            cursor.execute("SELECT COUNT(*) FROM items WHERE name = ?", (name,))
            if cursor.fetchone()[0] == 0:
                cursor.execute("""
                    INSERT INTO items (uuid, user_uuid, name, category, sku, reorder_point, origin, quantity, price, status)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, (item_uuid, user_uuid, name, category, sku, reorder, origin, qty, price, status))
                seeded_count += 1

        conn.commit()
        print("\n=========================================")
        print("✔ Database seeding complete!")
        print(f"  Successfully seeded items count: {seeded_count}")
        print("=========================================")

    except sqlite3.Error as e:
        print(f"SQLite error during seeding: {e}", file=sys.stderr)
    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    main()
