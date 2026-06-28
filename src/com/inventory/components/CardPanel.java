package com.inventory.components;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class CardPanel extends JPanel {
    private int cornerRadius = 8;

    public CardPanel() {
        super(new BorderLayout());
        setOpaque(false);
        setBackground(Theme.CREAM_SURFACE);
        setBorder(new EmptyBorder(16, 16, 16, 16));
    }

    public CardPanel(int padding) {
        super(new BorderLayout());
        setOpaque(false);
        setBackground(Theme.CREAM_SURFACE);
        setBorder(new EmptyBorder(padding, padding, padding, padding));
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
        
        // Draw outline
        g2.setColor(Theme.BORDER_SUBTLE);
        g2.setStroke(new java.awt.BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
        
        g2.dispose();
    }
}
