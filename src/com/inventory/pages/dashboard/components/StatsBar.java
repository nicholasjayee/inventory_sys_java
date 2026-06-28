package com.inventory.pages.dashboard.components;

import com.inventory.components.CardPanel;
import com.inventory.components.FontLoader;
import com.inventory.components.Theme;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StatsBar extends JPanel {
    private final JLabel rawStockVal;
    private final JLabel processedGoodsVal;
    private final JLabel lowStockAlertsVal;

    public StatsBar() {
        setOpaque(false);
        setLayout(new GridLayout(1, 3, 24, 0));
        setPreferredSize(new Dimension(800, 90));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        add(createStatsCard("RAW STOCK", rawStockVal = new JLabel("0 units"), "assets/icons/box_active.png", Theme.SUCCESS_BG));
        add(createStatsCard("PROCESSED GOODS", processedGoodsVal = new JLabel("0 units"), "assets/icons/science_active.png", Theme.SUCCESS_BG));
        add(createStatsCard("LOW STOCK ALERTS", lowStockAlertsVal = new JLabel("0 items"), "assets/icons/warning.png", Theme.ERROR_BG));
    }

    public void updateMetrics(int rawUnits, int processedUnits, int lowAlerts) {
        NumberFormat unitFormat = NumberFormat.getNumberInstance(Locale.US);
        rawStockVal.setText(unitFormat.format(rawUnits) + " units");
        processedGoodsVal.setText(unitFormat.format(processedUnits) + " units");
        lowStockAlertsVal.setText(lowAlerts + " items");
    }

    private CardPanel createStatsCard(String title, JLabel valueLabel, String iconPath, Color bgCircleColor) {
        CardPanel card = new CardPanel(12);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Circular Icon container
        JPanel circle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgCircleColor);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        circle.setOpaque(false);
        circle.setPreferredSize(new Dimension(42, 42));

        // Load, scale, and center the PNG icon in the circle
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaled));
            circle.add(iconLabel);
        } catch (Exception e) {
            System.err.println("Failed to display card icon: " + iconPath);
        }

        // Right Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontLoader.getInterSemiBold(9f));
        titleLabel.setForeground(Theme.SLATE_MUTED);

        valueLabel.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        valueLabel.setForeground(Theme.FOREST_DEEP);
        if (title.contains("LOW")) {
            valueLabel.setForeground(Theme.ERROR_FG);
        }

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(valueLabel);

        card.add(circle, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        return card;
    }
}
