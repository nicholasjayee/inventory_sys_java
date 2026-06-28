package com.inventory.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class SkeletonPanel extends JPanel implements ActionListener {
    private Timer timer;
    private float animationPhase = 0f;
    private Color baseColor = new Color(230, 230, 230);
    private Color shimmerColor = new Color(245, 245, 245);
    private int cornerRadius = 8;
    
    public SkeletonPanel(int width, int height) {
        this(width, height, 8);
    }
    
    public SkeletonPanel(int width, int height, int cornerRadius) {
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setOpaque(false);
        this.cornerRadius = cornerRadius;
        
        // Timer fires every 16ms (approx 60fps)
        timer = new Timer(16, this);
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        g2.setColor(baseColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, w, h, cornerRadius, cornerRadius));
        
        // Calculate shimmer bounds
        int shimmerWidth = Math.max(1, w / 2);
        int shimmerX = (int) (-shimmerWidth + (w + shimmerWidth * 2) * animationPhase);
        
        LinearGradientPaint paint = new LinearGradientPaint(
            shimmerX, 0, shimmerX + shimmerWidth, 0,
            new float[]{0f, 0.5f, 1f},
            new Color[]{
                new Color(shimmerColor.getRed(), shimmerColor.getGreen(), shimmerColor.getBlue(), 0),
                shimmerColor,
                new Color(shimmerColor.getRed(), shimmerColor.getGreen(), shimmerColor.getBlue(), 0)
            }
        );
        
        g2.setPaint(paint);
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.fill(new RoundRectangle2D.Double(0, 0, w, h, cornerRadius, cornerRadius));
        
        g2.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        animationPhase += 0.02f; // adjust speed
        if (animationPhase > 1.2f) {
            animationPhase = -0.2f;
        }
        repaint();
    }
    
    public void stop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}
