package com.inventory.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;

public class Theme {
    // Design system colors
    public static final Color FOREST_DEEP = new Color(0x01, 0x26, 0x1F); // Primary
    public static final Color FOREST_LEAF = new Color(0x2D, 0x5A, 0x4E); // Primary Hover
    public static final Color CREAM_BASE = new Color(0xFD, 0xFB, 0xF7);  // Level 0 Background
    public static final Color CREAM_SURFACE = new Color(0xF5, 0xF2, 0xED); // Level 1 Surface
    public static final Color SLATE_TEXT = new Color(0x33, 0x41, 0x55);   // Body copy
    public static final Color SLATE_MUTED = new Color(0x64, 0x74, 0x8B);  // Captions/Labels
    public static final Color BORDER_SUBTLE = new Color(0xE2, 0xE8, 0xF0); // Soft outline
    public static final Color WHITE = Color.WHITE;
    
    // Status colors
    public static final Color SUCCESS_BG = new Color(0xE6, 0xF4, 0xEA);
    public static final Color SUCCESS_FG = new Color(0x13, 0x73, 0x33);
    public static final Color WARNING_BG = new Color(0xFE, 0xF7, 0xE0);
    public static final Color WARNING_FG = new Color(0xB0, 0x60, 0x00);
    public static final Color ERROR_BG = new Color(0xFC, 0xE8, 0xE6);
    public static final Color ERROR_FG = new Color(0xC5, 0x22, 0x1F);

    // Apply primary button styles
    public static void stylePrimaryButton(JButton button) {
        button.setFont(FontLoader.getInterSemiBold(13f));
        button.setBackground(FOREST_DEEP);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(4, FOREST_DEEP, 1));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(FOREST_LEAF);
                button.setBorder(new RoundedBorder(4, FOREST_LEAF, 1));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(FOREST_DEEP);
                button.setBorder(new RoundedBorder(4, FOREST_DEEP, 1));
            }
        });
    }

    // Apply secondary button styles (Outline)
    public static void styleSecondaryButton(JButton button) {
        button.setFont(FontLoader.getInterSemiBold(13f));
        button.setBackground(WHITE);
        button.setForeground(FOREST_DEEP);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(4, BORDER_SUBTLE, 1));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(CREAM_SURFACE);
                button.setBorder(new RoundedBorder(4, FOREST_DEEP, 1));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WHITE);
                button.setBorder(new RoundedBorder(4, BORDER_SUBTLE, 1));
            }
        });
    }

    // Apply text field styling
    public static void styleTextField(JTextField textField) {
        textField.setFont(FontLoader.getInter(14f, java.awt.Font.PLAIN));
        textField.setBackground(WHITE);
        textField.setForeground(SLATE_TEXT);
        textField.setCaretColor(FOREST_DEEP);
        textField.setBorder(new RoundedBorder(4, BORDER_SUBTLE, 1));
        
        textField.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                textField.setBorder(new RoundedBorder(4, FOREST_DEEP, 1));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                textField.setBorder(new RoundedBorder(4, BORDER_SUBTLE, 1));
            }
        });
    }

    // Custom Rounded Border implementation
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        private final int thickness;

        public RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new java.awt.BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Double(x + (double)thickness/2, y + (double)thickness/2, 
                    width - thickness, height - thickness, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 4, radius + 8, radius + 4, radius + 8);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = radius + 8;
            insets.right = radius + 8;
            insets.top = radius + 4;
            insets.bottom = radius + 4;
            return insets;
        }
    }
}
