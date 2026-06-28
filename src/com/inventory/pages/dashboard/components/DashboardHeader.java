package com.inventory.pages.dashboard.components;

import com.inventory.components.FontLoader;
import com.inventory.components.Theme;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardHeader extends JPanel {
    private final JLabel totalUnitsVal;
    private final JLabel totalValueVal;

    public DashboardHeader(Runnable onCreateItemClicked) {
        setOpaque(false);
        setLayout(new BorderLayout(24, 0));
        setBorder(new EmptyBorder(0, 0, 24, 0));

        // Left Header Titles
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Inventory Dashboard");
        titleLabel.setFont(FontLoader.getMerriweather(28f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);

        JLabel subtitleLabel = new JLabel("Overview of raw materials and processed goods.");
        subtitleLabel.setFont(FontLoader.getInter(14f, Font.PLAIN));
        subtitleLabel.setForeground(Theme.SLATE_MUTED);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);
        add(titlePanel, BorderLayout.WEST);

        // Right Header Stats Grid & Button
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 0));
        rightActions.setOpaque(false);

        JPanel headerStats = new JPanel(new GridLayout(1, 2, 24, 0));
        headerStats.setOpaque(false);

        // Total SKU Stat
        JPanel unitsPanel = new JPanel();
        unitsPanel.setOpaque(false);
        unitsPanel.setLayout(new BoxLayout(unitsPanel, BoxLayout.Y_AXIS));
        JLabel unitsTitle = new JLabel("TOTAL SKU");
        unitsTitle.setFont(FontLoader.getInterSemiBold(9f));
        unitsTitle.setForeground(Theme.SLATE_MUTED);
        totalUnitsVal = new JLabel("0 units");
        totalUnitsVal.setFont(FontLoader.getMerriweather(16f, Font.BOLD));
        totalUnitsVal.setForeground(Theme.FOREST_DEEP);
        unitsPanel.add(unitsTitle);
        unitsPanel.add(Box.createVerticalStrut(2));
        unitsPanel.add(totalUnitsVal);

        // Total Value Stat
        JPanel valuePanel = new JPanel();
        valuePanel.setOpaque(false);
        valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
        JLabel valueTitle = new JLabel("TOTAL VALUE");
        valueTitle.setFont(FontLoader.getInterSemiBold(9f));
        valueTitle.setForeground(Theme.SLATE_MUTED);
        totalValueVal = new JLabel("$0.00");
        totalValueVal.setFont(FontLoader.getMerriweather(16f, Font.BOLD));
        totalValueVal.setForeground(Theme.FOREST_DEEP);
        valuePanel.add(valueTitle);
        valuePanel.add(Box.createVerticalStrut(2));
        valuePanel.add(totalValueVal);

        // Border divider line inside header
        unitsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_SUBTLE),
            new EmptyBorder(0, 0, 0, 16)
        ));

        headerStats.add(unitsPanel);
        headerStats.add(valuePanel);

        JButton createBtn = new JButton("+ Create New Item");
        Theme.stylePrimaryButton(createBtn);
        createBtn.setPreferredSize(new Dimension(160, 38));
        createBtn.addActionListener(e -> onCreateItemClicked.run());

        rightActions.add(headerStats);
        rightActions.add(createBtn);
        add(rightActions, BorderLayout.EAST);
    }

    public void updateMetrics(int totalUnits, double totalValue) {
        NumberFormat usdFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat unitFormat = NumberFormat.getNumberInstance(Locale.US);
        totalUnitsVal.setText(unitFormat.format(totalUnits) + " units");
        totalValueVal.setText(usdFormat.format(totalValue));
    }
}
