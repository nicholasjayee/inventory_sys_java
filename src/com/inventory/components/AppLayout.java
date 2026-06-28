package com.inventory.components;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class AppLayout extends JPanel {
    private final Sidebar sidebar;
    private final TopNav topNav;
    private final JPanel contentContainer;

    public AppLayout(Sidebar sidebar, TopNav topNav, JPanel contentContainer) {
        this.sidebar = sidebar;
        this.topNav = topNav;
        this.contentContainer = contentContainer;

        setLayout(new BorderLayout());

        // Sidebar on the Left
        add(sidebar, BorderLayout.WEST);

        // Right side wrapper panel (TopNav + Page Content)
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.add(topNav, BorderLayout.NORTH);
        rightContainer.add(contentContainer, BorderLayout.CENTER);
        add(rightContainer, BorderLayout.CENTER);
    }

    public Sidebar getSidebar() {
        return sidebar;
    }

    public TopNav getTopNav() {
        return topNav;
    }

    public JPanel getContentContainer() {
        return contentContainer;
    }
}
