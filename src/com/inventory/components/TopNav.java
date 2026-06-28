package com.inventory.components;

import com.inventory.models.User;
import com.inventory.router.Router;
import com.inventory.state.AppState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TopNav extends JPanel {
    private final Router router;
    private JLabel titleLabel;
    private JLabel userProfileLabel;
    private JLabel dateLabel;
    private JPanel avatarPanel;

    private static final String AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuBepq3TakdiZCToUb9WGm50y_IVi73Hbvoc4rCYQxGtPE49nL6hlySKReoEc4NCT5qmKQ7syaktBBv9x48mC-O_1tVouNhOJlhobWc4xFF_6TuUIwy5y7jUDz619uAZogZKgN9ucbJxKS5brYwNx4jv0XrSyx2jTpoK9OH30NKs80SjrcaPgHjZ3HHlfe4m_9135rCZr3bJcUNB50UAyP2MsYDXugnizLH2zNgZmQuMrMnJYR-GSnp1QLR10OZDQkNIQN4zQUFQIGe1";

    public TopNav(Router router) {
        this.router = router;
        initializeUI();
        
        // Listen to global app state to update user profiles
        AppState.getInstance().addListener(state -> updateProfile(state.getCurrentUser()));
    }

    private void initializeUI() {
        setPreferredSize(new Dimension(800, 60));
        setBackground(Theme.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            getBorder(),
            new EmptyBorder(10, 24, 10, 24)
        ));

        // Left: Page Title / Breadcrumb
        titleLabel = new JLabel("System Console");
        titleLabel.setFont(FontLoader.getInterSemiBold(15f));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        add(titleLabel, BorderLayout.WEST);

        // Right: Date & User Profile Info
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);

        // Date Display
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d, yyyy");
        dateLabel = new JLabel(sdf.format(new Date()));
        dateLabel.setFont(FontLoader.getInter(12f, Font.PLAIN));
        dateLabel.setForeground(Theme.SLATE_MUTED);

        // Vertical divider
        JLabel divider = new JLabel("|");
        divider.setForeground(Theme.BORDER_SUBTLE);

        // User profile text
        userProfileLabel = new JLabel("Guest");
        userProfileLabel.setFont(FontLoader.getInterMedium(13f));
        userProfileLabel.setForeground(Theme.SLATE_TEXT);

        // Round Avatar Image Panel loaded asynchronously
        avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Draw white background
                g2.setColor(Color.WHITE);
                g2.fill(new Ellipse2D.Double(0, 0, w - 1, h - 1));
                
                // Draw rounded image
                ImageIcon icon = ImageLoader.getOrLoadImage("user_avatar", AVATAR_URL, this, w, h);
                if (icon != null) {
                    g2.setClip(new Ellipse2D.Double(0, 0, w, h));
                    g2.drawImage(icon.getImage(), 0, 0, w, h, null);
                }
                
                // Outline border
                g2.setClip(null);
                g2.setColor(Theme.BORDER_SUBTLE);
                g2.draw(new Ellipse2D.Double(0, 0, w - 1, h - 1));
                g2.dispose();
            }
        };
        avatarPanel.setPreferredSize(new Dimension(32, 32));
        avatarPanel.setOpaque(false);

        rightPanel.add(dateLabel);
        rightPanel.add(divider);
        rightPanel.add(userProfileLabel);
        rightPanel.add(avatarPanel);
        add(rightPanel, BorderLayout.EAST);
    }

    public void setPageTitle(String path) {
        if ("/dashboard".equals(path)) {
            titleLabel.setText("Operations Dashboard");
        } else if ("/items".equals(path)) {
            titleLabel.setText("Items Library & Control");
        } else if ("/raw-items".equals(path)) {
            titleLabel.setText("Raw Ingredients Library");
        } else {
            titleLabel.setText("Management Suite");
        }
    }

    private void updateProfile(User user) {
        if (user != null) {
            userProfileLabel.setText(user.getDisplayName() + " (" + user.getRole() + ")");
            avatarPanel.repaint();
        } else {
            userProfileLabel.setText("Guest");
        }
    }
}
