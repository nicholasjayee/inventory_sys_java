package com.inventory.components;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;

public class VectorIcons {
    
    /**
     * Vector icon that draws a clean user profile symbol.
     */
    public static class PersonIcon implements Icon {
        private final int size;
        
        public PersonIcon(int size) {
            this.size = size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.SLATE_MUTED);
            
            // Head circle
            double headSize = size * 0.36;
            double headX = x + (size - headSize) / 2.0;
            double headY = y + size * 0.15;
            g2.fill(new Ellipse2D.Double(headX, headY, headSize, headSize));
            
            // Shoulders shape
            Path2D.Double shoulders = new Path2D.Double();
            double startX = x + size * 0.18;
            double startY = y + size * 0.85;
            double endX = x + size * 0.82;
            
            shoulders.moveTo(startX, startY);
            shoulders.curveTo(startX, y + size * 0.58, endX, y + size * 0.58, endX, startY);
            shoulders.closePath();
            g2.fill(shoulders);
            
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }

        @Override
        public int getIconHeight() { return size; }
    }

    /**
     * Vector icon that draws a lock symbol.
     */
    public static class LockIcon implements Icon {
        private final int size;
        
        public LockIcon(int size) {
            this.size = size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Theme.SLATE_MUTED);
            
            // Body square
            double bodyW = size * 0.56;
            double bodyH = size * 0.44;
            double bodyX = x + (size - bodyW) / 2.0;
            double bodyY = y + size * 0.46;
            g2.fill(new RoundRectangle2D.Double(bodyX, bodyY, bodyW, bodyH, 6, 6));
            
            // Shackle arc
            g2.setStroke(new BasicStroke((float)(size * 0.08), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            double shackleW = size * 0.36;
            double shackleH = size * 0.28;
            double shackleX = x + (size - shackleW) / 2.0;
            double shackleY = y + size * 0.18;
            g2.draw(new Arc2D.Double(shackleX, shackleY, shackleW, shackleH * 2.0, 0, 180, Arc2D.OPEN));
            
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return size; }

        @Override
        public int getIconHeight() { return size; }
    }
}
