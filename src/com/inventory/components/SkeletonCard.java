package com.inventory.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SkeletonCard extends JPanel {
    private float shimmerOffset = -0.5f;
    private Timer timer;
    private int cornerRadius = 8;

    public SkeletonCard() {
        setOpaque(false);
        setBackground(Theme.CREAM_SURFACE);
        
        // Start shimmer animation at ~60 FPS
        timer = new Timer(16, e -> {
            shimmerOffset += 0.02f;
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
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        // Base rounded card background
        g2.setColor(Theme.CREAM_SURFACE);
        g2.fill(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, cornerRadius, cornerRadius));
        g2.setColor(Theme.BORDER_SUBTLE);
        g2.draw(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, cornerRadius, cornerRadius));
        
        // Draw shimmering block for title (top left)
        drawShimmerBlock(g2, 16, 16, 110, 12);
        
        // Draw shimmering block for value (middle left)
        drawShimmerBlock(g2, 16, 44, 160, 26);
        
        g2.dispose();
    }

    private void drawShimmerBlock(Graphics2D g2, int x, int y, int width, int height) {
        // Base gray placeholder block
        g2.setColor(new Color(230, 225, 218)); // Soft cream-gray base
        g2.fillRoundRect(x, y, width, height, 4, 4);
        
        // Draw linear gradient shimmer overlay moving based on timer offset
        float start = shimmerOffset;
        Point2D startPt = new Point2D.Float(x + width * start, y);
        Point2D endPt = new Point2D.Float(x + width * (start + 0.4f), y);
        
        float[] fractions = {0.0f, 0.5f, 1.0f};
        Color[] colors = {
            new Color(255, 255, 255, 0),
            new Color(255, 255, 255, 140), // Shimmer highlight
            new Color(255, 255, 255, 0)
        };
        
        try {
            LinearGradientPaint lgp = new LinearGradientPaint(startPt, endPt, fractions, colors);
            g2.setPaint(lgp);
            g2.fillRoundRect(x, y, width, height, 4, 4);
        } catch (IllegalArgumentException e) {
            // Catch transient out of bounds parameters during resize events
        }
    }
}
