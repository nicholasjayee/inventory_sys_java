package com.inventory.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class EmptyStatePanel extends JPanel {
    private final JButton actionButton;

    public EmptyStatePanel(ActionListener actionListener) {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(new Theme.RoundedBorder(8, Theme.BORDER_SUBTLE, 1));
        
        // Centered Content Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // 1. Vector Leaf Graphic Component
        LeafIconPanel iconPanel = new LeafIconPanel();
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Headings
        JLabel titleLabel = new JLabel("No Inventory Registered");
        titleLabel.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descLabel = new JLabel("<html><center>Start tracking your botanical skincare raw materials and processed batches.<br>Register your first item to begin operations.</center></html>");
        descLabel.setFont(FontLoader.getInter(13f, Font.PLAIN));
        descLabel.setForeground(Theme.SLATE_MUTED);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setMaximumSize(new Dimension(480, 50));
        descLabel.setPreferredSize(new Dimension(480, 50));

        // 3. CTA Action Button
        actionButton = new JButton("+ Register First Item");
        Theme.stylePrimaryButton(actionButton);
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionButton.setMaximumSize(new Dimension(180, 38));
        actionButton.setPreferredSize(new Dimension(180, 38));
        actionButton.addActionListener(actionListener);

        // Assemble Content
        centerPanel.add(iconPanel);
        centerPanel.add(Box.createVerticalStrut(16));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(descLabel);
        centerPanel.add(Box.createVerticalStrut(24));
        centerPanel.add(actionButton);

        // Center constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(48, 48, 48, 48);
        add(centerPanel, gbc);
    }

    /**
     * Inner custom class drawing a premium organic leaf vector.
     */
    private static class LeafIconPanel extends JPanel {
        public LeafIconPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(80, 80));
            setMaximumSize(new Dimension(80, 80));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            
            // Draw a circular background container
            g2.setColor(Theme.CREAM_SURFACE);
            g2.fillOval(4, 4, w - 8, h - 8);
            g2.setColor(Theme.BORDER_SUBTLE);
            g2.setStroke(new java.awt.BasicStroke(1f));
            g2.drawOval(4, 4, w - 8, h - 8);

            // Draw organic leaf vector using curves
            g2.setColor(Theme.FOREST_DEEP);
            
            // Left half of leaf
            Path2D.Double leaf = new Path2D.Double();
            leaf.moveTo(w * 0.5, h * 0.25); // Leaf tip
            leaf.curveTo(w * 0.2, h * 0.35, w * 0.25, h * 0.65, w * 0.5, h * 0.75); // Left edge to base
            leaf.curveTo(w * 0.75, h * 0.65, w * 0.8, h * 0.35, w * 0.5, h * 0.25); // Right edge back to tip
            leaf.closePath();
            g2.fill(leaf);

            // Draw leaf center vein (stem line)
            g2.setColor(Theme.CREAM_BASE);
            g2.setStroke(new java.awt.BasicStroke(2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
            g2.drawLine((int)(w * 0.5), (int)(h * 0.28), (int)(w * 0.5), (int)(h * 0.72));

            // Side vein lines
            g2.drawLine((int)(w * 0.5), (int)(h * 0.45), (int)(w * 0.38), (int)(h * 0.38));
            g2.drawLine((int)(w * 0.5), (int)(h * 0.55), (int)(w * 0.35), (int)(h * 0.48));
            g2.drawLine((int)(w * 0.5), (int)(h * 0.45), (int)(w * 0.62), (int)(h * 0.38));
            g2.drawLine((int)(w * 0.5), (int)(h * 0.55), (int)(w * 0.65), (int)(h * 0.48));

            g2.dispose();
        }
    }
}
