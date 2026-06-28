package com.inventory.pages.dashboard;

import com.inventory.components.CardPanel;
import com.inventory.components.FontLoader;
import com.inventory.components.Theme;
import com.inventory.models.Item;
import com.inventory.models.User;
import com.inventory.router.Page;
import com.inventory.router.Router;
import com.inventory.services.ItemService;
import com.inventory.state.AppState;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class DashboardPage extends Page {
    private final Router router;
    private final ItemService itemService;
    
    private JLabel welcomeLabel;
    private JLabel totalUnitsVal;
    private JLabel totalValVal;
    private JLabel uniqueTypesVal;
    private JLabel lowStockVal;
    
    private DefaultTableModel recentItemsModel;
    private JTable recentTable;

    public DashboardPage(Router router) {
        this.router = router;
        this.itemService = new ItemService();
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Theme.CREAM_BASE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(32, 32, 32, 32));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Botanical Logistics");
        titleLabel.setFont(FontLoader.getMerriweather(28f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        
        welcomeLabel = new JLabel("Welcome back");
        welcomeLabel.setFont(FontLoader.getInter(14f, Font.PLAIN));
        welcomeLabel.setForeground(Theme.SLATE_MUTED);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(welcomeLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel (Grid of KPIs + Recent Items)
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // KPI Panel (4 columns grid)
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        kpiPanel.setPreferredSize(new Dimension(800, 120));

        // KPI 1: Total Units
        CardPanel kpi1 = createKPICard("TOTAL UNITS", totalUnitsVal = new JLabel("0 units"));
        // KPI 2: Total Value
        CardPanel kpi2 = createKPICard("TOTAL PORTFOLIO VALUE", totalValVal = new JLabel("$0.00"));
        // KPI 3: Unique Products
        CardPanel kpi3 = createKPICard("UNIQUE PRODUCTS", uniqueTypesVal = new JLabel("0 types"));
        // KPI 4: Low Stock Alerts
        CardPanel kpi4 = createKPICard("LOW STOCK WARNINGS", lowStockVal = new JLabel("0 alerts"));
        // Apply status colors to low stock alert
        lowStockVal.setForeground(Theme.WARNING_FG);

        kpiPanel.add(kpi1);
        kpiPanel.add(kpi2);
        kpiPanel.add(kpi3);
        kpiPanel.add(kpi4);

        centerPanel.add(Box.createVerticalStrut(24));
        centerPanel.add(kpiPanel);
        centerPanel.add(Box.createVerticalStrut(32));

        // Recent Items Panel
        JPanel recentSection = new JPanel(new BorderLayout());
        recentSection.setOpaque(false);
        
        JLabel sectionTitle = new JLabel("Recent Inventory Additions");
        sectionTitle.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        sectionTitle.setForeground(Theme.FOREST_DEEP);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        recentSection.add(sectionTitle, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Name", "Category", "Quantity", "Price", "Status"};
        recentItemsModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentTable = new JTable(recentItemsModel);
        recentTable.setFont(FontLoader.getInter(13f, Font.PLAIN));
        recentTable.setRowHeight(36);
        recentTable.setGridColor(Theme.BORDER_SUBTLE);
        recentTable.setShowVerticalLines(false);
        recentTable.setSelectionBackground(Theme.CREAM_SURFACE);
        recentTable.setSelectionForeground(Theme.FOREST_DEEP);
        
        // Header styling
        JTableHeader tableHeader = recentTable.getTableHeader();
        tableHeader.setFont(FontLoader.getInterSemiBold(11f));
        tableHeader.setBackground(Theme.CREAM_SURFACE);
        tableHeader.setForeground(Theme.SLATE_MUTED);
        tableHeader.setPreferredSize(new Dimension(800, 32));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE));

        // Alignments and custom renders
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 8, 0, 8));
        
        recentTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        recentTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        
        // Align numeric columns to right/center
        DefaultTableCellRenderer numericRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(FontLoader.getInter(13f, Font.PLAIN));
                setForeground(Theme.SLATE_TEXT);
                setHorizontalAlignment(JLabel.RIGHT);
                setBorder(new EmptyBorder(0, 8, 0, 16));
                return this;
            }
        };
        recentTable.getColumnModel().getColumn(2).setCellRenderer(numericRenderer);
        recentTable.getColumnModel().getColumn(3).setCellRenderer(numericRenderer);

        // Status pill renderer
        recentTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel pillContainer = new JPanel(new GridBagLayout());
                pillContainer.setOpaque(true);
                pillContainer.setBackground(table.getBackground());
                if (isSelected) {
                    pillContainer.setBackground(table.getSelectionBackground());
                }

                String status = (value != null) ? value.toString() : "";
                JLabel statusLabel = new JLabel(status);
                statusLabel.setFont(FontLoader.getInterSemiBold(11f));
                statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
                statusLabel.setOpaque(true);

                // Style based on status
                if ("In Stock".equalsIgnoreCase(status)) {
                    statusLabel.setBackground(Theme.SUCCESS_BG);
                    statusLabel.setForeground(Theme.SUCCESS_FG);
                } else if ("Low Stock".equalsIgnoreCase(status)) {
                    statusLabel.setBackground(Theme.WARNING_BG);
                    statusLabel.setForeground(Theme.WARNING_FG);
                } else {
                    statusLabel.setBackground(Theme.ERROR_BG);
                    statusLabel.setForeground(Theme.ERROR_FG);
                }

                statusLabel.setBorder(BorderFactory.createCompoundBorder(
                    new Theme.RoundedBorder(4, statusLabel.getBackground(), 1),
                    new EmptyBorder(2, 6, 2, 6)
                ));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.CENTER;
                pillContainer.add(statusLabel, gbc);

                return pillContainer;
            }
        });

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_SUBTLE, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        recentSection.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(recentSection);
        add(centerPanel, BorderLayout.CENTER);
    }

    private CardPanel createKPICard(String title, JLabel valueLabel) {
        CardPanel card = new CardPanel(16);
        card.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontLoader.getInterSemiBold(10f));
        titleLabel.setForeground(Theme.SLATE_MUTED);
        
        valueLabel.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        valueLabel.setForeground(Theme.FOREST_DEEP);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void onPageLoad() {
        // Load active user metadata
        User user = AppState.getInstance().getCurrentUser();
        if (user != null) {
            welcomeLabel.setText("Welcome back, " + user.getDisplayName() + " | Role: " + user.getRole());
        }

        // Fetch data from SQLite in background
        new Thread(this::loadDashboardData).start();
    }

    private void loadDashboardData() {
        List<Item> items = itemService.getAllItems();

        int totalUnits = 0;
        double totalValue = 0.0;
        int uniqueTypes = items.size();
        int lowStockAlerts = 0;

        for (Item item : items) {
            totalUnits += item.getQuantity();
            totalValue += item.getQuantity() * item.getPrice();
            if (item.getQuantity() < 10 || "Low Stock".equalsIgnoreCase(item.getStatus()) || "Out of Stock".equalsIgnoreCase(item.getStatus())) {
                lowStockAlerts++;
            }
        }

        final int finalTotalUnits = totalUnits;
        final double finalTotalValue = totalValue;
        final int finalUniqueTypes = uniqueTypes;
        final int finalLowStockAlerts = lowStockAlerts;

        NumberFormat usdFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat unitFormat = NumberFormat.getNumberInstance(Locale.US);

        SwingUtilities.invokeLater(() -> {
            totalUnitsVal.setText(unitFormat.format(finalTotalUnits) + " units");
            totalValVal.setText(usdFormat.format(finalTotalValue));
            uniqueTypesVal.setText(finalUniqueTypes + " items");
            lowStockVal.setText(finalLowStockAlerts + " items");

            // Load recent additions into table (limit to 5)
            recentItemsModel.setRowCount(0);
            int count = 0;
            for (Item item : items) {
                if (count >= 5) break;
                recentItemsModel.addRow(new Object[]{
                    item.getName(),
                    item.getCategory(),
                    unitFormat.format(item.getQuantity()),
                    usdFormat.format(item.getPrice()),
                    item.getStatus()
                });
                count++;
            }
        });
    }
}
