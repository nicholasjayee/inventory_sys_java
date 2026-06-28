package com.inventory.components;

import com.inventory.models.User;
import com.inventory.router.Router;
import com.inventory.state.AppState;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TopNav extends JPanel {
    private final Router router;
    private JLabel titleLabel;
    private JLabel userProfileLabel;
    private JLabel dateLabel;

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
            new EmptyBorder(12, 24, 12, 24)
        ));

        // Left: Page Title / Breadcrumb
        titleLabel = new JLabel("System Console");
        titleLabel.setFont(FontLoader.getInterSemiBold(15f));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        add(titleLabel, BorderLayout.WEST);

        // Right: Date & User Profile Info
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
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

        rightPanel.add(dateLabel);
        rightPanel.add(divider);
        rightPanel.add(userProfileLabel);
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
        } else {
            userProfileLabel.setText("Guest");
        }
    }
}
