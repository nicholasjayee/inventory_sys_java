# User & Account Management Guide

Because the **Botanical Logistics** desktop client is designed as a secure operational application, it does not expose a public registration form on the login screen. Instead, new user accounts must be created using a separate utility CLI.

---

## 🛠️ Account Creation CLI Tool

Inside the `tools/` folder, we have provided an interactive CLI tool to securely add new accounts into the SQLite database.

### Prerequisites:
* **Python 3** must be installed.
* **Database migrations** must be applied first (`./migrate.sh` or `migrate.bat`) so the `users` table is created.

---

## 🚀 How to Create a New User

To register a new user:

### macOS / Linux:
```bash
./tools/create_user.sh
```
*(Alternatively, run `python3 tools/create_user.py` directly from the project root).*

### Windows:
Double-click `tools/create_user.bat` or run it in your terminal:
```cmd
tools\create_user.bat
```

---

## 📝 CLI Parameters

When you run the tool, it will prompt you for the following inputs:

1. **Username**: The alphanumeric login name (must be unique).
2. **Display Name**: The human-readable name shown in the application sidebar (e.g., `Jane Doe`, `Shift Lead`).
3. **Password**: The plain-text password for login authentication.
4. **Role**: The access tier. Select from:
   * `Admin` (Full access to all operations)
   * `Manager` (Management suite operations)
   * `Staff` (Default role for stock edits)

Once registered, you can immediately open the main application and log in using the newly created credentials.
