package com.inventory.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SkeletonTable extends JPanel {
    private float shimmerOffset = -0.5f;
    private Timer timer;

    public SkeletonTable() {
        setOpaque(true);
        setBackground(Color.WHITE);
        
        timer = new Timer(16, e -> {
            shimmerOffset += 0.015f;
            if (shimmerOffset > 1.5f) {
                shimmerOffset = -0.5f;
            }
            repaint();
        });
        timer.start();
    }

    public void stopAnimation() {
        if (timer != null) {
            timer.stop();
        }
    }

    public void startAnimation() {
        if (timer != null && !timer.isRunning()) {
            timer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        
        // 1. Render Mock Table Header
        g2.setColor(Theme.CREAM_SURFACE);
        g2.fillRect(0, 0, w, 36);
        g2.setColor(Theme.BORDER_SUBTLE);
        g2.drawLine(0, 36, w, 36);

        // 2. Render 5 Shimmering Rows
        int rowHeight = 44;
        for (int i = 0; i < 5; i++) {
            int yOffset = 36 + (i * rowHeight);
            
            // Row divider line
            g2.setColor(Theme.BORDER_SUBTLE);
            g2.drawLine(0, yOffset + rowHeight, w, yOffset + rowHeight);
            
            // Name Column (shimmer)
            drawShimmerBlock(g2, 16, yOffset + 14, 180, 16);
            
            // Category Column (shimmer)
            drawShimmerBlock(g2, 220, yOffset + 14, 90, 16);
            
            // Quantity Column (shimmer)
            drawShimmerBlock(g2, w - 320, yOffset + 14, 50, 16);
            
            // Price Column (shimmer)
            drawShimmerBlock(g2, w - 220, yOffset + 14, 70, 16);
            
            // Status Column (shimmer)
            drawShimmerBlock(g2, w - 100, yOffset + 12, 80, 20);
        }

        g2.dispose();
    }

    private void drawShimmerBlock(Graphics2D g2, int x, int y, int width, int height) {
        // Base placeholder block color
        g2.setColor(new Color(241, 245, 249)); // Soft light slate gray
        g2.fillRoundRect(x, y, width, height, 4, 4);

        // Draw animating shimmer gradient
        float start = shimmerOffset;
        Point2D startPt = new Point2D.Float(x + width * start, y);
        Point2D endPt = new Point2D.Float(x + width * (start + 0.4f), y);

        float[] fractions = {0.0f, 0.5f, 1.0f};
        Color[] colors = {
            new Color(255, 255, 255, 0),
            new Color(255, 255, 255, 180), // Shimmer highlight
            new Color(255, 255, 255, 0)
        };

        try {
            LinearGradientPaint lgp = new LinearGradientPaint(startPt, endPt, fractions, colors);
            g2.setPaint(lgp);
            g2.fillRoundRect(x, y, width, height, 4, 4);
        } catch (IllegalArgumentException e) {
            // Ignore boundary exceptions during layout transitions
        }
    }
}
