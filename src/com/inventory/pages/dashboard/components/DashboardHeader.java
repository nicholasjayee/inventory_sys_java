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
        setBorder(new EmptyBorder(0, 0, 32, 0)); // mb-section-gap (32px or 48px)

        // --- Left Container (Title + Inline Metrics) ---
        JPanel leftContainer = new JPanel();
        leftContainer.setOpaque(false);
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));

        // Title: "Inventory Overview" (headline-lg / 32px)
        JLabel titleLabel = new JLabel("Inventory Overview");
        titleLabel.setFont(FontLoader.getMerriweather(32f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        leftContainer.add(titleLabel);
        leftContainer.add(Box.createVerticalStrut(12));

        // Stats Row (Total Items + Divider + Total Value)
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statsRow.setOpaque(false);
        statsRow.setAlignmentX(LEFT_ALIGNMENT);

        // 1. Total Items Panel
        JPanel itemsPanel = new JPanel();
        itemsPanel.setOpaque(false);
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        JLabel itemsLabel = new JLabel("TOTAL ITEMS");
        itemsLabel.setFont(FontLoader.getInterSemiBold(10f));
        itemsLabel.setForeground(Theme.SLATE_MUTED);
        totalUnitsVal = new JLabel("0 units");
        totalUnitsVal.setFont(FontLoader.getMerriweather(20f, Font.BOLD));
        totalUnitsVal.setForeground(Theme.FOREST_DEEP);
        itemsPanel.add(itemsLabel);
        itemsPanel.add(Box.createVerticalStrut(2));
        itemsPanel.add(totalUnitsVal);
        statsRow.add(itemsPanel);

        // 2. Vertical border divider
        statsRow.add(Box.createHorizontalStrut(32));
        JPanel divider = new JPanel();
        divider.setPreferredSize(new Dimension(1, 40));
        divider.setBackground(Theme.BORDER_SUBTLE);
        statsRow.add(divider);
        statsRow.add(Box.createHorizontalStrut(32));

        // 3. Total Value Panel
        JPanel valuePanel = new JPanel();
        valuePanel.setOpaque(false);
        valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
        JLabel valueLabel = new JLabel("TOTAL VALUE");
        valueLabel.setFont(FontLoader.getInterSemiBold(10f));
        valueLabel.setForeground(Theme.SLATE_MUTED);
        totalValueVal = new JLabel("$0.00");
        totalValueVal.setFont(FontLoader.getMerriweather(20f, Font.BOLD));
        totalValueVal.setForeground(Theme.FOREST_DEEP);
        valuePanel.add(valueLabel);
        valuePanel.add(Box.createVerticalStrut(2));
        valuePanel.add(totalValueVal);
        statsRow.add(valuePanel);

        leftContainer.add(statsRow);
        add(leftContainer, BorderLayout.CENTER);

        // --- Right Container (Create Item Button, baseline aligned) ---
        JPanel rightContainer = new JPanel(new GridBagLayout());
        rightContainer.setOpaque(false);
        
        JButton createBtn = new JButton("Create New Item");
        Theme.stylePrimaryButton(createBtn);
        createBtn.setPreferredSize(new Dimension(160, 38));
        
        try {
            ImageIcon plusIcon = new ImageIcon("assets/icons/plus.png");
            Image scaledPlus = plusIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            createBtn.setIcon(new ImageIcon(scaledPlus));
            createBtn.setIconTextGap(6);
        } catch (Exception e) {}
        
        createBtn.addActionListener(e -> onCreateItemClicked.run());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH; // Baseline/Bottom aligned items-end
        rightContainer.add(createBtn, gbc);

        add(rightContainer, BorderLayout.EAST);
    }

    public void updateMetrics(int totalUnits, double totalValue) {
        NumberFormat usdFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat unitFormat = NumberFormat.getNumberInstance(Locale.US);
        
        // Match HTML suffix styling: bold number + regular slate-muted "units" suffix
        String countFormatted = unitFormat.format(totalUnits);
        totalUnitsVal.setText("<html>" + countFormatted + " <span style='font-family: Inter; font-size: 13px; font-weight: normal; color: #64748B;'>units</span></html>");
        totalValueVal.setText(usdFormat.format(totalValue));
    }
}
