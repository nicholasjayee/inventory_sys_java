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
import com.inventory.components.AppLayout;
import com.inventory.components.TopNav;
import java.awt.CardLayout;
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

            // Root panel managed by CardLayout for swapping layout wrappers
            CardLayout rootLayout = new CardLayout();
            JPanel rootPanel = new JPanel(rootLayout);
            frame.add(rootPanel);

            // Inner content panel managed by CardLayout router (the page outlet)
            JPanel contentContainer = new JPanel();

            // Initialize Router on the inner content panel
            Router router = new Router(contentContainer);

            // Initialize Layout components
            Sidebar sidebar = new Sidebar(router);
            TopNav topNav = new TopNav(router);

            // Wrap them into AppLayout
            AppLayout appLayout = new AppLayout(sidebar, topNav, contentContainer);

            // Register main screen wrappers in root CardPanel
            LoginPage loginPage = new LoginPage(router);
            rootPanel.add(loginPage, "/login-screen");
            rootPanel.add(appLayout, "/app-screen");

            // Register routes (Pages) in Router
            router.register("/login", loginPage);
            router.register("/dashboard", new DashboardPage(router));
            router.register("/items", new ItemsPage(router));

            // Register global Middleware chain (order matters)
            router.addGlobalMiddleware(new LoggingMiddleware());
            
            // Layout toggler middleware: swaps screens and updates sidebar/top-nav
            router.addGlobalMiddleware((current, target, r) -> {
                if ("/login".equals(target)) {
                    rootLayout.show(rootPanel, "/login-screen");
                } else {
                    rootLayout.show(rootPanel, "/app-screen");
                    topNav.setPageTitle(target);
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
