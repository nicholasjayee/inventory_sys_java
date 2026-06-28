package com.inventory.components;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class FontLoader {
    public static Font INTER_REGULAR;
    public static Font INTER_MEDIUM;
    public static Font INTER_SEMIBOLD;
    public static Font INTER_BOLD;
    public static Font MERRIWEATHER_REGULAR;
    public static Font MERRIWEATHER_BOLD;

    public static void loadFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            INTER_REGULAR = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Inter-Regular.ttf"));
            ge.registerFont(INTER_REGULAR);

            INTER_MEDIUM = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Inter-Medium.ttf"));
            ge.registerFont(INTER_MEDIUM);

            INTER_SEMIBOLD = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Inter-SemiBold.ttf"));
            ge.registerFont(INTER_SEMIBOLD);

            INTER_BOLD = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Inter-Bold.ttf"));
            ge.registerFont(INTER_BOLD);

            MERRIWEATHER_REGULAR = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Merriweather-Regular.ttf"));
            ge.registerFont(MERRIWEATHER_REGULAR);

            MERRIWEATHER_BOLD = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Merriweather-Bold.ttf"));
            ge.registerFont(MERRIWEATHER_BOLD);

            System.out.println("Custom fonts loaded successfully.");
        } catch (Exception e) {
            System.err.println("Could not load custom fonts. Falling back to system fonts.");
            e.printStackTrace();
            
            // Fallback font instances
            INTER_REGULAR = new Font("SansSerif", Font.PLAIN, 14);
            INTER_MEDIUM = new Font("SansSerif", Font.PLAIN, 14);
            INTER_SEMIBOLD = new Font("SansSerif", Font.BOLD, 14);
            INTER_BOLD = new Font("SansSerif", Font.BOLD, 14);
            MERRIWEATHER_REGULAR = new Font("Serif", Font.PLAIN, 14);
            MERRIWEATHER_BOLD = new Font("Serif", Font.BOLD, 14);
        }
    }

    public static Font getInter(float size, int style) {
        if (style == Font.BOLD) return INTER_BOLD.deriveFont(size);
        if (style == Font.ITALIC) return INTER_REGULAR.deriveFont(style, size);
        return INTER_REGULAR.deriveFont(size);
    }

    public static Font getInterMedium(float size) {
        return INTER_MEDIUM.deriveFont(size);
    }

    public static Font getInterSemiBold(float size) {
        return INTER_SEMIBOLD.deriveFont(size);
    }

    public static Font getMerriweather(float size, int style) {
        if (style == Font.BOLD) return MERRIWEATHER_BOLD.deriveFont(size);
        return MERRIWEATHER_REGULAR.deriveFont(size);
    }
}
