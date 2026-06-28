package com.inventory.middleware;

import com.inventory.router.Middleware;
import com.inventory.router.Router;

public class LoggingMiddleware implements Middleware {
    @Override
    public boolean handle(String currentPath, String targetPath, Router router) {
        System.out.println("[Router Log] Route transition: " + 
                           (currentPath == null ? "START" : currentPath) + " -> " + targetPath);
        return true;
    }
}
