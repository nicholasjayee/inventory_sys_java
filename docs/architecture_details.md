# Application Architecture & Design Patterns

This document details the software architecture, modular directory strategy, and design patterns utilized in the **Botanical Logistics** desktop application.

---

## 🏗️ Architecture Design Strategy

The application borrows state-of-the-art architectures from web-based frameworks like React/Next.js and maps them onto native Java Swing desktop programming. It is built to be modular, extensible, and clean.

```
                    ┌────────────────────────┐
                    │       Main Frame       │
                    └───────────┬────────────┘
                                │
                 ┌──────────────┴──────────────┐
                 ▼                             ▼
       ┌──────────────────┐          ┌──────────────────┐
       │     Sidebar      │          │ Content Container│
       │ (Navigation Bar) │          │  (Card Layout)   │
       └─────────┬────────┘          └─────────▲────────┘
                 │                             │ (Routes Pages)
                 │  Toggles/Active Route       │
                 └─────────────────────────────┼────────┐
                                               │        │
                                    ┌──────────┴──┐     │
                                    │   Router    │     │
                                    └─────┬───────┘     │
                                          │             │
                    Runs Middlewares      ▼             ▼
                 ┌────────────────────────────────────────┐
                 │ Logging, Layout, Auth Interceptors     │
                 └────────────────────────────────────────┘
```

---

## 📦 Directory Structure

### 1. `com.inventory.router`
* **`Page.java`**: Abstract class extending `JPanel` representing a view container. Exposes `onPageLoad()` and `onPageUnload()` hook methods which function similarly to React's `useEffect` for page mount/unmount triggers.
* **`Router.java`**: Manages the screen container, registers pages under specific paths (e.g. `/login`, `/dashboard`), handles navigation history (backwards traversal), and runs the global/route-level middleware chain.

### 2. `com.inventory.middleware`
* Intercepts route requests. Returning `false` prevents navigation and initiates a redirect.
* **`AuthMiddleware.java`**: Inspects user sessions. If the user is unauthenticated, they are redirected to `/login`.
* **`LoggingMiddleware.java`**: Traces navigation transitions in standard out.
* **Layout Interceptor (`Main.java`)**: Configures the main application frame context (e.g. hiding the sidebar during auth screens, highlighting buttons).

### 3. `com.inventory.state`
* **`AppState.java`**: Implements a global context provider (singleton). Exposes properties like `currentUser` and notifies registered `StateChangeListener` classes upon modifications (e.g. redrawing sidebar details on user login).

### 4. `com.inventory.services`
* **`UserService.java`**: Handles credentials lookup.
* **`ItemService.java`**: Implements CRUD functions (fetching list, inserting new raw materials, updating values, deleting rows).

### 5. `com.inventory.db`
* **`DatabaseManager.java`**: Establishes connections using the SQLite JDBC driver and scans the `migrations/` folder. Sorts scripts alphabetically to apply unexecuted DDL schema files transactionalized.

---

## 📈 Lifecycle Flow

1. **Bootstrap**:
   - `Main.java` initializes look-and-feel (FlatLaf) and registers branding fonts.
   - `DatabaseManager` runs database migrations.
   - The primary JFrame is instantiated with a persistent Left Sidebar and a Right page container.
2. **Page Navigation**:
   - Routing triggers `router.navigate("/dashboard")`.
   - Middlewares check user session state.
   - If authenticated, the Sidebar is rendered, the active navigation button is highlighted, and the card layout shows `DashboardPage`.
   - `DashboardPage` receives `onPageLoad()`, starts a background worker thread querying SQLite, and populates the table on the Swing Event Dispatch Thread (EDT) using `SwingUtilities.invokeLater()`.
