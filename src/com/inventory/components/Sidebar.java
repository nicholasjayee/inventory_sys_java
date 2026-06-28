package com.inventory.components;

import com.inventory.models.User;
import com.inventory.router.Router;
import com.inventory.state.AppState;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Sidebar extends JPanel {
    private final Router router;
    
    private JButton dashboardBtn;
    private JButton itemsBtn;
    private JButton rawItemsBtn;
    private JLabel userLabel;
    private JLabel roleLabel;

    public Sidebar(Router router) {
        this.router = router;
        initializeUI();
        
        // Listen to state changes (e.g. login/logout)
        AppState.getInstance().addListener(state -> updateUserDetails(state.getCurrentUser()));
    }

    private void initializeUI() {
        setPreferredSize(new Dimension(280, 720));
        setBackground(Theme.CREAM_SURFACE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_SUBTLE));
        setLayout(new BorderLayout());

        // Top Brand Logo Section
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(32, 24, 32, 24));

        JLabel brandLabel = new JLabel("Aramweer");
        brandLabel.setFont(FontLoader.getMerriweather(24f, Font.BOLD));
        brandLabel.setForeground(Theme.FOREST_DEEP);
        brandLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subBrandLabel = new JLabel("BOTANICAL LOGISTICS");
        subBrandLabel.setFont(FontLoader.getInterSemiBold(9f));
        subBrandLabel.setForeground(Theme.SLATE_MUTED);
        subBrandLabel.setAlignmentX(LEFT_ALIGNMENT);

        brandPanel.add(brandLabel);
        brandPanel.add(Box.createVerticalStrut(4));
        brandPanel.add(subBrandLabel);
        add(brandPanel, BorderLayout.NORTH);

        // Center Navigation Links Section
        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(new EmptyBorder(0, 16, 0, 16));

        dashboardBtn = createNavButton("Dashboard");
        dashboardBtn.addActionListener(e -> router.navigate("/dashboard"));

        itemsBtn = createNavButton("Items Library");
        itemsBtn.addActionListener(e -> router.navigate("/items"));

        rawItemsBtn = createNavButton("Raw Items");
        rawItemsBtn.addActionListener(e -> router.navigate("/raw-items"));

        navPanel.add(dashboardBtn);
        navPanel.add(Box.createVerticalStrut(8));
        navPanel.add(itemsBtn);
        navPanel.add(Box.createVerticalStrut(8));
        navPanel.add(rawItemsBtn);
        add(navPanel, BorderLayout.CENTER);

        // Bottom User Session Section
        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        userPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE),
            new EmptyBorder(24, 24, 24, 24)
        ));

        userLabel = new JLabel("Guest User");
        userLabel.setFont(FontLoader.getInterSemiBold(13f));
        userLabel.setForeground(Theme.SLATE_TEXT);

        roleLabel = new JLabel("Role: Guest");
        roleLabel.setFont(FontLoader.getInter(11f, Font.PLAIN));
        roleLabel.setForeground(Theme.SLATE_MUTED);

        JButton signOutBtn = new JButton("Sign Out");
        Theme.styleSecondaryButton(signOutBtn);
        signOutBtn.setForeground(Theme.ERROR_FG);
        signOutBtn.setFont(FontLoader.getInterSemiBold(11f));
        signOutBtn.setMaximumSize(new Dimension(232, 32));
        signOutBtn.setPreferredSize(new Dimension(232, 32));
        signOutBtn.setAlignmentX(LEFT_ALIGNMENT);
        signOutBtn.addActionListener(e -> handleSignOut());

        userPanel.add(userLabel);
        userPanel.add(Box.createVerticalStrut(2));
        userPanel.add(roleLabel);
        userPanel.add(Box.createVerticalStrut(16));
        userPanel.add(signOutBtn);

        add(userPanel, BorderLayout.SOUTH);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FontLoader.getInterMedium(13f));
        button.setForeground(Theme.SLATE_TEXT);
        button.setBackground(Theme.CREAM_SURFACE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            new Theme.RoundedBorder(4, Theme.CREAM_SURFACE, 1),
            new EmptyBorder(10, 16, 10, 16)
        ));
        button.setMaximumSize(new Dimension(248, 40));
        button.setPreferredSize(new Dimension(248, 40));
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.getBackground() != Theme.FOREST_DEEP) {
                    button.setBackground(Theme.CREAM_BASE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        new Theme.RoundedBorder(4, Theme.BORDER_SUBTLE, 1),
                        new EmptyBorder(10, 16, 10, 16)
                    ));
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.getBackground() != Theme.FOREST_DEEP) {
                    button.setBackground(Theme.CREAM_SURFACE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        new Theme.RoundedBorder(4, Theme.CREAM_SURFACE, 1),
                        new EmptyBorder(10, 16, 10, 16)
                    ));
                }
            }
        });
        
        return button;
    }

    public void setActiveRoute(String path) {
        // Reset styles
        resetButtonTheme(dashboardBtn);
        resetButtonTheme(itemsBtn);
        resetButtonTheme(rawItemsBtn);

        if ("/dashboard".equals(path)) {
            highlightButton(dashboardBtn);
        } else if ("/items".equals(path)) {
            highlightButton(itemsBtn);
        } else if ("/raw-items".equals(path)) {
            highlightButton(rawItemsBtn);
        }
    }

    private void resetButtonTheme(JButton button) {
        button.setBackground(Theme.CREAM_SURFACE);
        button.setForeground(Theme.SLATE_TEXT);
        button.setBorder(BorderFactory.createCompoundBorder(
            new Theme.RoundedBorder(4, Theme.CREAM_SURFACE, 1),
            new EmptyBorder(10, 16, 10, 16)
        ));
    }

    private void highlightButton(JButton button) {
        button.setBackground(Theme.FOREST_DEEP);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            new Theme.RoundedBorder(4, Theme.FOREST_DEEP, 1),
            new EmptyBorder(10, 16, 10, 16)
        ));
    }

    private void updateUserDetails(User user) {
        if (user != null) {
            userLabel.setText(user.getDisplayName());
            roleLabel.setText("Role: " + user.getRole());
        } else {
            userLabel.setText("Guest User");
            roleLabel.setText("Role: Guest");
        }
    }

    private void handleSignOut() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to sign out?",
            "Sign Out Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            AppState.getInstance().setCurrentUser(null);
            router.navigate("/login");
        }
    }
}
