package com.inventory.router;

import java.awt.CardLayout;
import java.util.*;
import javax.swing.JPanel;

public class Router {
    private final JPanel container;
    private final CardLayout cardLayout;
    private final Map<String, Page> routes = new HashMap<>();
    private final Map<String, List<Middleware>> routeMiddlewares = new HashMap<>();
    private final List<Middleware> globalMiddlewares = new ArrayList<>();
    private final Stack<String> history = new Stack<>();
    private String currentPath = null;

    public Router(JPanel container) {
        this.container = container;
        this.cardLayout = new CardLayout();
        this.container.setLayout(cardLayout);
    }

    /**
     * Registers a page under a specific route path.
     */
    public void register(String path, Page page) {
        routes.put(path, page);
        container.add(page, path);
    }

    /**
     * Registers a page under a specific route path with route-specific middleware.
     */
    public void register(String path, Page page, Middleware... middlewares) {
        register(path, page);
        routeMiddlewares.put(path, Arrays.asList(middlewares));
    }

    /**
     * Adds a global middleware that runs on every routing request.
     */
    public void addGlobalMiddleware(Middleware middleware) {
        globalMiddlewares.add(middleware);
    }

    /**
     * Navigates to the target route path.
     */
    public boolean navigate(String path) {
        // Run global middleware
        for (Middleware middleware : globalMiddlewares) {
            if (!middleware.handle(currentPath, path, this)) {
                return false;
            }
        }

        // Run route-specific middleware
        List<Middleware> middlewares = routeMiddlewares.get(path);
        if (middlewares != null) {
            for (Middleware middleware : middlewares) {
                if (!middleware.handle(currentPath, path, this)) {
                    return false;
                }
            }
        }

        // Verify target page existence
        Page targetPage = routes.get(path);
        if (targetPage == null) {
            System.err.println("404: Route not found: " + path);
            return false;
        }

        // Unload current page and add to history stack
        if (currentPath != null) {
            Page current = routes.get(currentPath);
            if (current != null) {
                current.onPageUnload();
            }
            history.push(currentPath);
        }

        // Perform the route transition
        cardLayout.show(container, path);
        currentPath = path;

        // Trigger onPageLoad lifecycle hook
        targetPage.onPageLoad();

        // Refresh container view
        container.revalidate();
        container.repaint();
        return true;
    }

    /**
     * Navigates back to the previous route in history.
     */
    public boolean back() {
        if (!history.isEmpty()) {
            String prevPath = history.pop();
            // Bypass adding the current state to history when navigating backward
            String current = currentPath;
            currentPath = null;
            if (navigate(prevPath)) {
                if (!history.isEmpty()) {
                    history.pop(); // Remove the extra duplicate added by navigate
                }
                return true;
            }
            currentPath = current;
        }
        return false;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public Map<String, Page> getRoutes() {
        return routes;
    }
}
