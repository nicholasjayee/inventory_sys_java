package com.inventory.app;

import com.formdev.flatlaf.FlatLightLaf;
import com.inventory.components.FontLoader;
import com.inventory.components.Sidebar;
import com.inventory.db.DatabaseManager;
import com.inventory.middleware.AuthMiddleware;
import com.inventory.middleware.LoggingMiddleware;
import com.inventory.pages.dashboard.DashboardPage;
import com.inventory.pages.items.ItemsPage;
import com.inventory.pages.login.LoginPage;
import com.inventory.router.Router;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize Modern Flat Look and Feel
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("FlatLightLaf failed to initialize. Falling back to default.");
        }

        // 2. Load custom branding fonts (Inter, Merriweather)
        FontLoader.loadFonts();

        // 3. Execute SQLite database migrations
        DatabaseManager.runMigrations();

        // 4. Start Desktop GUI Application
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(AppConfig.getAppName() + " - v" + AppConfig.getAppVersion());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 750);
            frame.setMinimumSize(new Dimension(1000, 650));
            frame.setLocationRelativeTo(null);

            // Root panel layout wrapper
            JPanel rootPanel = new JPanel(new BorderLayout());
            frame.add(rootPanel);

            // Container panel managed by CardLayout routing
            JPanel contentContainer = new JPanel();

            // Initialize Router
            Router router = new Router(contentContainer);

            // Initialize persistent Sidebar
            Sidebar sidebar = new Sidebar(router);

            // Assemble main layout
            rootPanel.add(sidebar, BorderLayout.WEST);
            rootPanel.add(contentContainer, BorderLayout.CENTER);

            // Register routes (Pages)
            router.register("/login", new LoginPage(router));
            router.register("/dashboard", new DashboardPage(router));
            router.register("/items", new ItemsPage(router));

            // Register global Middleware chain (order matters)
            router.addGlobalMiddleware(new LoggingMiddleware());
            
            // Layout layout middleware: hides sidebar on login, highlights sidebar nav items
            router.addGlobalMiddleware((current, target, r) -> {
                if ("/login".equals(target)) {
                    sidebar.setVisible(false);
                } else {
                    sidebar.setVisible(true);
                    sidebar.setActiveRoute(target);
                }
                return true;
            });
            
            // Auth check middleware
            router.addGlobalMiddleware(new AuthMiddleware());

            // Navigate to default route (AuthMiddleware automatically intercepts and redirects to /login)
            router.navigate("/dashboard");

            frame.setVisible(true);
        });
    }
}
