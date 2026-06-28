package com.inventory.router;

public interface Middleware {
    /**
     * Intercepts navigation from currentPath to targetPath.
     * 
     * @param currentPath The route the user is currently on (might be null initially).
     * @param targetPath The route the user is attempting to navigate to.
     * @param router The Router instance.
     * @return true if navigation should proceed; false if navigation is aborted/redirected.
     */
    boolean handle(String currentPath, String targetPath, Router router);
}
