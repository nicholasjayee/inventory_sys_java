package com.inventory.pages.login;

import com.inventory.components.FontLoader;
import com.inventory.components.Theme;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class BrandingPanel extends JPanel {
    private BufferedImage backgroundImage;

    public BrandingPanel() {
        setPreferredSize(new Dimension(500, 640));
        setLayout(new GridBagLayout());
        setBackground(Theme.FOREST_DEEP);
        
        // Load the background image asynchronously
        new Thread(this::loadBrandingImage).start();

        initializeUI();
    }

    private void loadBrandingImage() {
        try {
            File imgFile = new File("assets/login_branding.jpg");
            if (imgFile.exists()) {
                backgroundImage = ImageIO.read(imgFile);
                repaint();
            }
        } catch (Exception e) {
            System.err.println("Could not load login branding image. Falling back to solid forest deep background.");
        }
    }

    private void initializeUI() {
        // We use GridBagLayout to place a top section and a bottom section
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // --- Top Logo Section ---
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(48, 48, 0, 48));

        JLabel brandLabel = new JLabel("Aramweer");
        brandLabel.setFont(FontLoader.getMerriweather(32f, Font.BOLD));
        brandLabel.setForeground(Theme.CREAM_BASE);
        brandLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subBrandLabel = new JLabel("ORGANIC SKIN CARE");
        subBrandLabel.setFont(FontLoader.getInterSemiBold(10f));
        subBrandLabel.setForeground(Theme.CREAM_BASE);
        subBrandLabel.setOpaque(false);
        subBrandLabel.setAlignmentX(LEFT_ALIGNMENT);

        logoPanel.add(brandLabel);
        logoPanel.add(Box.createVerticalStrut(4));
        logoPanel.add(subBrandLabel);

        gbc.gridy = 0;
        gbc.weighty = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(logoPanel, gbc);

        // --- Bottom Quote Section ---
        JPanel quotePanel = new JPanel();
        quotePanel.setOpaque(false);
        quotePanel.setLayout(new BoxLayout(quotePanel, BoxLayout.Y_AXIS));
        quotePanel.setBorder(new EmptyBorder(0, 48, 48, 48));

        JLabel quoteLabel = new JLabel("<html><i>\"Rooted in nature, refined by science.\"</i></html>");
        quoteLabel.setFont(FontLoader.getMerriweather(20f, Font.ITALIC));
        quoteLabel.setForeground(Theme.CREAM_BASE);
        quoteLabel.setAlignmentX(LEFT_ALIGNMENT);

        // Est. 1984 Panel with horizontal line
        JPanel estPanel = new JPanel();
        estPanel.setOpaque(false);
        estPanel.setLayout(new BoxLayout(estPanel, BoxLayout.X_AXIS));
        estPanel.setAlignmentX(LEFT_ALIGNMENT);

        // Small divider line
        JPanel line = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(253, 251, 247, 180)); // Semi-transparent Cream Base
                g.fillRect(0, getHeight() / 2 - 1, getWidth(), 1);
            }
        };
        line.setOpaque(false);
        line.setMaximumSize(new Dimension(48, 12));
        line.setPreferredSize(new Dimension(48, 12));

        JLabel estLabel = new JLabel("Est. 1984");
        estLabel.setFont(FontLoader.getInterSemiBold(10f));
        estLabel.setForeground(new Color(253, 251, 247, 180));

        estPanel.add(line);
        estPanel.add(Box.createHorizontalStrut(12));
        estPanel.add(estLabel);

        quotePanel.add(quoteLabel);
        quotePanel.add(Box.createVerticalStrut(16));
        quotePanel.add(estPanel);

        gbc.gridy = 1;
        gbc.weighty = 0.7;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(quotePanel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        // 1. Draw branding background image (aspect ratio fill - equivalent to object-cover)
        if (backgroundImage != null) {
            int imgW = backgroundImage.getWidth();
            int imgH = backgroundImage.getHeight();

            double sx = (double) w / imgW;
            double sy = (double) h / imgH;
            double scale = Math.max(sx, sy);

            int targetW = (int) (imgW * scale);
            int targetH = (int) (imgH * scale);

            int x = (w - targetW) / 2;
            int y = (h - targetH) / 2;

            g2.drawImage(backgroundImage, x, y, targetW, targetH, null);
        }

        // 2. Draw Forest Deep color overlay with 60% opacity (0.6f alpha)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2.setColor(Theme.FOREST_DEEP);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }
}
