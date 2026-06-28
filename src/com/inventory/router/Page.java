package com.inventory.router;

import javax.swing.JPanel;

public abstract class Page extends JPanel {
    public Page() {
        // Default layout for pages is typically BorderLayout
        setLayout(new java.awt.BorderLayout());
    }

    /**
     * Called immediately after this page becomes active and visible.
     * Perfect for fetching data or starting animations.
     */
    public abstract void onPageLoad();

    /**
     * Called just before leaving this page.
     * Perfect for cleaning up timers or saving draft states.
     */
    public void onPageUnload() {}
}
