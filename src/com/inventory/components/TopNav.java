package com.inventory.components;

import com.inventory.router.Router;
import com.inventory.state.AppState;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TopNav extends JPanel {
    private final Router router;
    
    // Center Links
    private JButton homeBtn;
    private JButton itemsBtn;
    private JButton inventoryBtn;

    // Right profile
    private JPanel avatarPanel;
    private JButton signOutBtn;

    private static final String AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuBepq3TakdiZCToUb9WGm50y_IVi73Hbvoc4rCYQxGtPE49nL6hlySKReoEc4NCT5qmKQ7syaktBBv9x48mC-O_1tVouNhOJlhobWc4xFF_6TuUIwy5y7jUDz619uAZogZKgN9ucbJxKS5brYwNx4jv0XrSyx2jTpoK9OH30NKs80SjrcaPgHjZ3HHlfe4m_9135rCZr3bJcUNB50UAyP2MsYDXugnizLH2zNgZmQuMrMnJYR-GSnp1QLR10OZDQkNIQN4zQUFQIGe1";

    public TopNav(Router router) {
        this.router = router;
        initializeUI();
    }

    private void initializeUI() {
        setPreferredSize(new Dimension(800, 64)); // h-16
        setBackground(Theme.CREAM_BASE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE));
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE),
            new EmptyBorder(0, 24, 0, 24)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Default weighty is 0.0, which naturally centers the row vertically in the TopNav's 64px height.

        // --- 1. Left branding ---
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        JLabel brandLabel = new JLabel(com.inventory.app.AppConfig.getBrandName());
        brandLabel.setFont(FontLoader.getMerriweather(16f, Font.BOLD));
        brandLabel.setForeground(Theme.FOREST_DEEP);
        leftPanel.add(brandLabel);
        
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        add(leftPanel, gbc);

        // --- 2. Center Nav Links (Home, Items, Inventory) ---
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 32, 0));
        centerPanel.setOpaque(false);

        homeBtn = createNavLink("Home", "/dashboard");
        itemsBtn = createNavLink("Items", "/items");
        inventoryBtn = createNavLink("Inventory", "/raw-items");

        centerPanel.add(homeBtn);
        centerPanel.add(itemsBtn);
        centerPanel.add(inventoryBtn);
        
        gbc.gridx = 1; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.CENTER;
        add(centerPanel, gbc);

        // --- 3. Right Actions (Search Pill + Sign out + Avatar) ---
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        rightPanel.setOpaque(false);

        // Search Wrapper (rounded-lg equivalent)
        JPanel searchPill = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CREAM_SURFACE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8); // rounded-lg
                g2.setColor(Theme.BORDER_SUBTLE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8); // rounded-lg
                g2.dispose();
            }
        };
        searchPill.setOpaque(false);
        searchPill.setPreferredSize(new Dimension(220, 34)); // Match mockup height
        searchPill.setBorder(new EmptyBorder(4, 12, 4, 12));

        // Load the downloaded search icon
        ImageIcon searchIcon = new ImageIcon("assets/icons/search_muted.png");
        // Scale to 16x16 to fit beautifully in the search pill
        Image scaledSearch = searchIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        JLabel searchIconLabel = new JLabel(new ImageIcon(scaledSearch));
        
        JTextField searchInput = new JTextField("Search items...");
        searchInput.setFont(FontLoader.getInter(12f, Font.PLAIN));
        searchInput.setForeground(Theme.SLATE_MUTED);
        searchInput.setBorder(null);
        searchInput.setOpaque(false);
        
        searchInput.addActionListener(e -> {
            String q = searchInput.getText().trim();
            if (q.equals("Search items...")) q = "";
            AppState.getInstance().setGlobalSearchQuery(q);
            router.navigate("/items");
        });

        searchPill.add(searchIconLabel, BorderLayout.WEST);
        searchPill.add(searchInput, BorderLayout.CENTER);

        // Sign Out text link
        signOutBtn = new JButton("SIGN OUT");
        signOutBtn.setFont(FontLoader.getInterSemiBold(11f));
        signOutBtn.setForeground(Theme.SLATE_MUTED);
        signOutBtn.setBorder(null);
        signOutBtn.setOpaque(false);
        signOutBtn.setContentAreaFilled(false);
        signOutBtn.setFocusPainted(false);
        signOutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signOutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                signOutBtn.setForeground(Theme.ERROR_FG);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                signOutBtn.setForeground(Theme.SLATE_MUTED);
            }
        });
        signOutBtn.addActionListener(e -> handleSignOut());

        // Profile Avatar
        avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Draw circle background
                g2.setColor(Color.WHITE);
                g2.fill(new Ellipse2D.Double(0, 0, w - 1, h - 1));
                
                // Load avatar image
                ImageIcon icon = ImageLoader.getOrLoadImage("topnav_user_avatar", AVATAR_URL, this, w, h);
                if (icon != null) {
                    g2.setClip(new Ellipse2D.Double(0, 0, w, h));
                    g2.drawImage(icon.getImage(), 0, 0, w, h, null);
                }
                
                g2.setClip(null);
                g2.setColor(Theme.BORDER_SUBTLE);
                g2.draw(new Ellipse2D.Double(0, 0, w - 1, h - 1));
                g2.dispose();
            }
        };
        avatarPanel.setPreferredSize(new Dimension(32, 32));
        avatarPanel.setOpaque(false);

        rightPanel.add(searchPill);
        rightPanel.add(signOutBtn);
        rightPanel.add(avatarPanel);
        
        gbc.gridx = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.EAST;
        add(rightPanel, gbc);
    }

    private JButton createNavLink(String text, String route) {
        JButton btn = new JButton(text);
        btn.setFont(FontLoader.getInterMedium(13f));
        btn.setForeground(Theme.SLATE_MUTED);
        btn.setBorder(new EmptyBorder(0, 0, 4, 0));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.getForeground() != Theme.FOREST_DEEP) {
                    btn.setForeground(Theme.FOREST_LEAF);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn.getForeground() != Theme.FOREST_DEEP) {
                    btn.setForeground(Theme.SLATE_MUTED);
                }
            }
        });

        btn.addActionListener(e -> router.navigate(route));
        return btn;
    }

    public void setPageTitle(String path) {
        // Reset link selections
        resetNavLink(homeBtn);
        resetNavLink(itemsBtn);
        resetNavLink(inventoryBtn);

        if ("/dashboard".equals(path)) {
            highlightNavLink(homeBtn);
        } else if ("/items".equals(path)) {
            highlightNavLink(itemsBtn);
        } else if ("/raw-items".equals(path)) {
            highlightNavLink(inventoryBtn);
        }
    }

    private void resetNavLink(JButton btn) {
        btn.setForeground(Theme.SLATE_MUTED);
        btn.setFont(FontLoader.getInterMedium(13f));
        btn.setBorder(new EmptyBorder(0, 0, 4, 0));
    }

    private void highlightNavLink(JButton btn) {
        btn.setForeground(Theme.FOREST_DEEP);
        btn.setFont(FontLoader.getInterSemiBold(13f));
        // Underline matching HTML border-b-2
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.FOREST_DEEP));
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
