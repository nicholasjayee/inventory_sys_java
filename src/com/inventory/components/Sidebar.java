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
    private JButton rawMaterialsBtn;
    private JButton processedGoodsBtn;
    private JLabel userLabel;
    private JLabel roleLabel;

    // Sidebar PNG Icons (Loaded and scaled to 18x18)
    private ImageIcon dbActive;
    private ImageIcon dbMuted;
    private ImageIcon ecoActive;
    private ImageIcon ecoMuted;
    private ImageIcon cogActive;
    private ImageIcon cogMuted;

    public Sidebar(Router router) {
        this.router = router;
        loadIcons();
        initializeUI();
        
        // Listen to state changes (e.g. login/logout)
        AppState.getInstance().addListener(state -> updateUserDetails(state.getCurrentUser()));
    }

    private void loadIcons() {
        dbActive = loadAndScaleIcon("assets/icons/dashboard_active.png");
        dbMuted = loadAndScaleIcon("assets/icons/dashboard_muted.png");
        ecoActive = loadAndScaleIcon("assets/icons/eco_active.png");
        ecoMuted = loadAndScaleIcon("assets/icons/eco_muted.png");
        cogActive = loadAndScaleIcon("assets/icons/cog_active.png");
        cogMuted = loadAndScaleIcon("assets/icons/cog_muted.png");
    }

    private ImageIcon loadAndScaleIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image scaledImg = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("Failed to load icon: " + path);
            return null;
        }
    }

    private void initializeUI() {
        setPreferredSize(new Dimension(280, 720));
        setBackground(Theme.CREAM_SURFACE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_SUBTLE));
        setLayout(new BorderLayout());

        // Top Brand Logo Section (Matches mockup exactly)
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(32, 24, 32, 24));

        JLabel brandLabel = new JLabel("Inventory");
        brandLabel.setFont(FontLoader.getMerriweather(20f, Font.BOLD));
        brandLabel.setForeground(Theme.FOREST_DEEP);
        brandLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subBrandLabel = new JLabel("MANAGEMENT SUITE");
        subBrandLabel.setFont(FontLoader.getInterSemiBold(10f));
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
        navPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        dashboardBtn = createNavButton("Dashboard", dbMuted, dbActive);
        dashboardBtn.addActionListener(e -> router.navigate("/dashboard"));

        rawMaterialsBtn = createNavButton("Raw Materials", ecoMuted, ecoActive);
        rawMaterialsBtn.addActionListener(e -> router.navigate("/raw-items"));

        processedGoodsBtn = createNavButton("Processed Goods", cogMuted, cogActive);
        processedGoodsBtn.addActionListener(e -> router.navigate("/items"));

        navPanel.add(dashboardBtn);
        navPanel.add(Box.createVerticalStrut(4));
        navPanel.add(rawMaterialsBtn);
        navPanel.add(Box.createVerticalStrut(4));
        navPanel.add(processedGoodsBtn);
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

    private JButton createNavButton(String text, ImageIcon mutedIcon, ImageIcon activeIcon) {
        JButton button = new JButton(text);
        button.setIcon(mutedIcon);
        button.setIconTextGap(12); // gap-3 matching Tailwind
        button.setFont(FontLoader.getInterMedium(14f));
        button.setForeground(Theme.SLATE_MUTED);
        button.setBackground(Theme.CREAM_SURFACE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setMaximumSize(new Dimension(280, 44));
        button.setPreferredSize(new Dimension(280, 44));
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.getForeground() != Theme.FOREST_DEEP) {
                    button.setBackground(Theme.CREAM_BASE);
                    button.setForeground(Theme.FOREST_LEAF);
                    if (mutedIcon == dbMuted) button.setIcon(dbActive);
                    if (mutedIcon == ecoMuted) button.setIcon(ecoActive);
                    if (mutedIcon == cogMuted) button.setIcon(cogActive);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.getForeground() != Theme.FOREST_DEEP) {
                    button.setBackground(Theme.CREAM_SURFACE);
                    button.setForeground(Theme.SLATE_MUTED);
                    button.setIcon(mutedIcon);
                }
            }
        });
        
        return button;
    }

    public void setActiveRoute(String path) {
        // Reset styles
        resetButtonTheme(dashboardBtn, dbMuted);
        resetButtonTheme(rawMaterialsBtn, ecoMuted);
        resetButtonTheme(processedGoodsBtn, cogMuted);

        if ("/dashboard".equals(path)) {
            highlightButton(dashboardBtn, dbActive);
        } else if ("/raw-items".equals(path)) {
            highlightButton(rawMaterialsBtn, ecoActive);
        } else if ("/items".equals(path)) {
            highlightButton(processedGoodsBtn, cogActive);
        }
    }

    private void resetButtonTheme(JButton button, ImageIcon mutedIcon) {
        button.setIcon(mutedIcon);
        button.setBackground(Theme.CREAM_SURFACE);
        button.setForeground(Theme.SLATE_MUTED);
        button.setFont(FontLoader.getInterMedium(14f));
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
    }

    private void highlightButton(JButton button, ImageIcon activeIcon) {
        button.setIcon(activeIcon);
        button.setBackground(Theme.CREAM_BASE);
        button.setForeground(Theme.FOREST_DEEP);
        button.setFont(FontLoader.getInter(14f, Font.BOLD));
        // Draw 4px thick green border indicator on the right of the active button
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 4, Theme.FOREST_DEEP),
            new EmptyBorder(12, 24, 12, 20)
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
