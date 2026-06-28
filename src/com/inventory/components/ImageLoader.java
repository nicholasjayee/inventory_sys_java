package com.inventory.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class ImageLoader {
    private static final Map<String, ImageIcon> cache = new HashMap<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * Loads an image from a URL asynchronously and calls repaint on the target component when finished.
     */
    public static ImageIcon getOrLoadImage(String name, String urlString, JComponent targetComponent, int width, int height) {
        String cacheKey = name + "_" + width + "x" + height;
        
        synchronized (cache) {
            if (cache.containsKey(cacheKey)) {
                return cache.get(cacheKey);
            }
        }

        // Set placeholder while loading
        ImageIcon placeholder = createPlaceholder(width, height);
        synchronized (cache) {
            cache.put(cacheKey, placeholder);
        }

        // Fetch image in background
        executor.submit(() -> {
            try {
                URL url = new URL(urlString);
                // Set User-Agent headers if necessary to prevent HTTP 403
                java.net.URLConnection conn = url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedImage rawImage = ImageIO.read(conn.getInputStream());
                
                if (rawImage != null) {
                    // Resize to fit aspect-fill scaling (object-fit: cover equivalent)
                    Image scaledImage = getScaledImage(rawImage, width, height);
                    ImageIcon finalIcon = new ImageIcon(scaledImage);
                    
                    synchronized (cache) {
                        cache.put(cacheKey, finalIcon);
                    }
                    
                    // Repaint the component on the Event Dispatch Thread
                    SwingUtilities.invokeLater(targetComponent::repaint);
                }
            } catch (Exception e) {
                System.err.println("Failed to load image for: " + name + " (" + e.getMessage() + ")");
                // Keep the placeholder as fallback
            }
        });

        return placeholder;
    }

    /**
     * Scale image to aspect-fill the bounds (CSS object-fit: cover equivalent)
     */
    private static Image getScaledImage(BufferedImage src, int targetWidth, int targetHeight) {
        double srcWidth = src.getWidth();
        double srcHeight = src.getHeight();
        
        double scale = Math.max(targetWidth / srcWidth, targetHeight / srcHeight);
        int finalWidth = (int) (srcWidth * scale);
        int finalHeight = (int) (srcHeight * scale);
        
        // Center alignment cropping coordinates
        int x = (targetWidth - finalWidth) / 2;
        int y = (targetHeight - finalHeight) / 2;
        
        BufferedImage dest = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = dest.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the scaled and cropped image
        g2.drawImage(src, x, y, finalWidth, finalHeight, null);
        g2.dispose();
        
        return dest;
    }

    /**
     * Generates a beautiful vector placeholder leaf card when loading or as fallback
     */
    private static ImageIcon createPlaceholder(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background gradient
        g2.setColor(Theme.CREAM_SURFACE);
        g2.fillRect(0, 0, w, h);
        
        // Draw centered leaf outline with soft alpha
        g2.setColor(new Color(45, 90, 78, 40)); // Forest leaf with low opacity
        int cx = w / 2;
        int cy = h / 2;
        
        Path2D.Double leaf = new Path2D.Double();
        leaf.moveTo(cx, cy - 24);
        leaf.curveTo(cx - 24, cy - 16, cx - 20, cy + 12, cx, cy + 24);
        leaf.curveTo(cx + 20, cy + 12, cx + 24, cy - 16, cx, cy - 24);
        leaf.closePath();
        
        g2.fill(leaf);
        
        // Subtle center line
        g2.setColor(new Color(255, 255, 255, 80));
        g2.drawLine(cx, cy - 20, cx, cy + 20);
        
        g2.dispose();
        return new ImageIcon(img);
    }
}
