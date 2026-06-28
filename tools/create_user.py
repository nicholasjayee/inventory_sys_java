#!/usr/bin/env python3
import sqlite3
import os
import sys
import uuid

def get_db_path():
    # Read the db.path from metadata/app.properties if it exists
    properties_path = "metadata/app.properties"
    db_path = "db/inventory.db" # Default fallback
    
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
    print(" Aramweer - Botanical Logistics User CLI ")
    print("=========================================")
    
    db_path = get_db_path()
    db_dir = os.path.dirname(db_path)
    
    # Ensure database directory exists
    if db_dir and not os.path.exists(db_dir):
        os.makedirs(db_dir)

    try:
        conn = sqlite3.connect(db_path)
        cursor = conn.cursor()
        
        # Verify schema is ready (users table must exist)
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name='users'")
        if not cursor.fetchone():
            print("Error: The 'users' table does not exist in the database yet.", file=sys.stderr)
            print("Please run database migrations first using './migrate.sh' or 'migrate.bat'.", file=sys.stderr)
            conn.close()
            sys.exit(1)
            
        print(f"Connected to database: {db_path}\n")

        # Get inputs
        username = input("Enter Username: ").strip()
        if not username:
            print("Error: Username cannot be empty.", file=sys.stderr)
            sys.exit(1)

        # Check if username exists
        cursor.execute("SELECT 1 FROM users WHERE username = ?", (username,))
        if cursor.fetchone():
            print(f"Error: Username '{username}' already exists.", file=sys.stderr)
            sys.exit(1)

        display_name = input("Enter User's Display Name (e.g. Jane Doe): ").strip()
        if not display_name:
            print("Error: Display name cannot be empty.", file=sys.stderr)
            sys.exit(1)

        password = input("Enter Password: ").strip()
        if not password:
            print("Error: Password cannot be empty.", file=sys.stderr)
            sys.exit(1)

        role = input("Enter User Role (Admin, Manager, Staff) [Staff]: ").strip()
        if not role:
            role = "Staff"
        if role.lower() not in ["admin", "manager", "staff"]:
            role = role.capitalize()

        # Generate a unique UUID for the new user
        user_uuid = str(uuid.uuid4())

        # Insert user
        cursor.execute(
            "INSERT INTO users (uuid, username, password_hash, display_name, role) VALUES (?, ?, ?, ?, ?)",
            (user_uuid, username, password, display_name, role)
        )
        conn.commit()
        
        print("\n=========================================")
        print("✔ User successfully registered!")
        print(f"  Username:     {username}")
        print(f"  Display Name: {display_name}")
        print(f"  Role:         {role}")
        print(f"  User UUID:    {user_uuid}")
        print("=========================================")
        
    except sqlite3.Error as e:
        print(f"SQLite database error: {e}", file=sys.stderr)
    finally:
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    main()
