#!/usr/bin/env python3
import sqlite3
import os
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
    print(" Aramweer - Database Reset Utility (CLI) ")
    print("=========================================")
    
    db_path = get_db_path()
    if not os.path.exists(db_path):
        print(f"Database file does not exist yet: {db_path}")
        print("Nothing to clear. Run migrations or launch the app first.")
        sys.exit(0)

    # Double check confirmation
    confirm = input("Are you sure you want to delete ALL inventory items? (y/N): ").strip().lower()
    if confirm != 'y':
        print("Operation cancelled. No changes were made.")
        sys.exit(0)

    try:
        conn = sqlite3.connect(db_path)
        cursor = conn.cursor()
        
        # Count current items
        cursor.execute("SELECT COUNT(*) FROM items")
        count = cursor.fetchone()[0]
        
        # Clear items
        cursor.execute("DELETE FROM items")
        
        # Reset sqlite auto-increment sequence
        cursor.execute("DELETE FROM sqlite_sequence WHERE name='items'")
        
        conn.commit()
        
        print("\n=========================================")
        print("✔ Database cleared successfully!")
        print(f"  Deleted items count: {count}")
        print("  Items auto-increment sequence reset.")
        print("=========================================")
        
    except sqlite3.Error as e:
        print(f"SQLite error during database reset: {e}", file=sys.stderr)
    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    main()
