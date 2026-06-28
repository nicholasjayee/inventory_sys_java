package com.inventory.middleware;

import com.inventory.router.Middleware;
import com.inventory.router.Router;
import com.inventory.state.AppState;

public class AuthMiddleware implements Middleware {
    @Override
    public boolean handle(String currentPath, String targetPath, Router router) {
        boolean loggedIn = AppState.getInstance().isLoggedIn();

        if (!loggedIn) {
            // Guard protected pages: if not logged in, redirect to "/login"
            if (!"/login".equals(targetPath)) {
                System.out.println("[AuthMiddleware] Unauthenticated access to " + targetPath + ". Redirecting to /login");
                // Swing routing uses invokeLater to prevent re-entrant layout locking
                javax.swing.SwingUtilities.invokeLater(() -> router.navigate("/login"));
                return false;
            }
        } else {
            // If already logged in, block "/login" and send to dashboard
            if ("/login".equals(targetPath)) {
                System.out.println("[AuthMiddleware] User already logged in. Redirecting from /login to /dashboard");
                javax.swing.SwingUtilities.invokeLater(() -> router.navigate("/dashboard"));
                return false;
            }
        }
        return true;
    }
}
