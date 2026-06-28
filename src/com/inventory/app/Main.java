package com.inventory.app;

import com.formdev.flatlaf.FlatLightLaf;
import com.inventory.components.AppLayout;
import com.inventory.components.FontLoader;
import com.inventory.components.Sidebar;
import com.inventory.components.TopNav;
import com.inventory.db.DatabaseManager;
import com.inventory.middleware.AuthMiddleware;
import com.inventory.middleware.LoggingMiddleware;
import com.inventory.pages.dashboard.DashboardPage;
import com.inventory.pages.items.ItemsPage;
import com.inventory.pages.items.RawItemsPage;
import com.inventory.pages.login.LoginPage;
import com.inventory.router.Router;
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

            // Root panel (serves the entire app) - swaps between full login and main layout
            CardLayout rootLayout = new CardLayout();
            JPanel rootPanel = new JPanel(rootLayout);
            frame.add(rootPanel);

            // Inner content panel managed by Router (serves as the page outlet)
            JPanel contentContainer = new JPanel();

            // Initialize Router on the inner content panel
            Router router = new Router(contentContainer);

            // Initialize Layout components
            Sidebar sidebar = new Sidebar(router);
            TopNav topNav = new TopNav(router);

            // AppLayout (serves other pages - wraps Sidebar, TopNav, and content outlet)
            AppLayout appLayout = new AppLayout(sidebar, topNav, contentContainer);

            // Initialize LoginPage (mounted ONLY to the root panel to avoid double-parenting)
            LoginPage loginPage = new LoginPage(router);

            // Register top-level layout panels
            rootPanel.add(loginPage, "auth-layout");     // Fullscreen login (no sidebar or topnav)
            rootPanel.add(appLayout, "dashboard-layout"); // Layout with sidebar and topnav

            // Register routes in Router
            router.register("/login", new com.inventory.router.Page() {
                @Override
                public void onPageLoad() {}
            }); 
            router.register("/dashboard", new DashboardPage(router));
            router.register("/items", new ItemsPage(router));
            router.register("/raw-items", new RawItemsPage(router));

            // Register global Middleware chain (order matters)
            router.addGlobalMiddleware(new LoggingMiddleware());
            
            // Layout switcher middleware: toggles top-level layouts and highlights active links
            router.addGlobalMiddleware((current, target, r) -> {
                if ("/login".equals(target)) {
                    // Show fullscreen login screen
                    rootLayout.show(rootPanel, "auth-layout");
                } else {
                    // Show dashboard layout and sync header/sidebar active states
                    rootLayout.show(rootPanel, "dashboard-layout");
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
