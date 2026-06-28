package com.inventory.pages.dashboard;

import com.inventory.components.CardPanel;
import com.inventory.components.EmptyStatePanel;
import com.inventory.components.FontLoader;
import com.inventory.components.ItemDialog;
import com.inventory.components.SkeletonCard;
import com.inventory.components.SkeletonTable;
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

    // Persistent Header Components
    private JLabel welcomeLabel;

    // State Container (CardLayout)
    private CardLayout stateLayout;
    private JPanel stateCardPanel;

    // LOADED State UI Components
    private JLabel totalUnitsVal;
    private JLabel totalValVal;
    private JLabel uniqueTypesVal;
    private JLabel lowStockVal;
    private DefaultTableModel recentItemsModel;
    private JTable recentTable;

    // Skeletons references to pause/start timers
    private SkeletonTable skeletonTable;
    private SkeletonCard skeletonCard1, skeletonCard2, skeletonCard3, skeletonCard4;

    public DashboardPage(Router router) {
        this.router = router;
        this.itemService = new ItemService();
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Theme.CREAM_BASE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(32, 32, 32, 32));

        // 1. Persistent Top Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel titleLabel = new JLabel("Botanical Logistics");
        titleLabel.setFont(FontLoader.getMerriweather(28f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);

        welcomeLabel = new JLabel("Welcome back");
        welcomeLabel.setFont(FontLoader.getInter(14f, Font.PLAIN));
        welcomeLabel.setForeground(Theme.SLATE_MUTED);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(welcomeLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // 2. State-Based Panel Swapper
        stateLayout = new CardLayout();
        stateCardPanel = new JPanel(stateLayout);
        stateCardPanel.setOpaque(false);

        // Define layouts for states
        setupLoadingStatePanel();
        setupLoadedStatePanel();
        setupEmptyStatePanel();
        setupErrorStatePanel();

        add(stateCardPanel, BorderLayout.CENTER);
    }

    // --- Loading State Setup ---
    private void setupLoadingStatePanel() {
        JPanel loadingPanel = new JPanel();
        loadingPanel.setOpaque(false);
        loadingPanel.setLayout(new BoxLayout(loadingPanel, BoxLayout.Y_AXIS));

        // KPI Skeletons
        JPanel kpiSkeletonGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        kpiSkeletonGrid.setOpaque(false);
        kpiSkeletonGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        kpiSkeletonGrid.setPreferredSize(new Dimension(800, 120));

        skeletonCard1 = new SkeletonCard();
        skeletonCard2 = new SkeletonCard();
        skeletonCard3 = new SkeletonCard();
        skeletonCard4 = new SkeletonCard();

        kpiSkeletonGrid.add(skeletonCard1);
        kpiSkeletonGrid.add(skeletonCard2);
        kpiSkeletonGrid.add(skeletonCard3);
        kpiSkeletonGrid.add(skeletonCard4);

        // Table Skeleton Section
        JPanel tableSkeletonPanel = new JPanel(new BorderLayout());
        tableSkeletonPanel.setOpaque(false);
        
        JLabel sectionTitle = new JLabel("Loading inventory records...");
        sectionTitle.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        sectionTitle.setForeground(Theme.FOREST_DEEP);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        tableSkeletonPanel.add(sectionTitle, BorderLayout.NORTH);

        skeletonTable = new SkeletonTable();
        tableSkeletonPanel.add(skeletonTable, BorderLayout.CENTER);

        loadingPanel.add(Box.createVerticalStrut(16));
        loadingPanel.add(kpiSkeletonGrid);
        loadingPanel.add(Box.createVerticalStrut(32));
        loadingPanel.add(tableSkeletonPanel);

        stateCardPanel.add(loadingPanel, "LOADING");
    }

    // --- Loaded State Setup ---
    private void setupLoadedStatePanel() {
        JPanel loadedPanel = new JPanel();
        loadedPanel.setOpaque(false);
        loadedPanel.setLayout(new BoxLayout(loadedPanel, BoxLayout.Y_AXIS));

        // Real KPI Grid
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        kpiPanel.setPreferredSize(new Dimension(800, 120));

        kpiPanel.add(createKPICard("TOTAL UNITS", totalUnitsVal = new JLabel("0 units")));
        kpiPanel.add(createKPICard("TOTAL VALUE (USD)", totalValVal = new JLabel("$0.00")));
        kpiPanel.add(createKPICard("UNIQUE PRODUCTS", uniqueTypesVal = new JLabel("0 items")));
        
        lowStockVal = new JLabel("0 items");
        lowStockVal.setForeground(Theme.WARNING_FG);
        kpiPanel.add(createKPICard("LOW STOCK WARNINGS", lowStockVal));

        // Real Recent Table Section
        JPanel recentSection = new JPanel(new BorderLayout());
        recentSection.setOpaque(false);

        JLabel sectionTitle = new JLabel("Recent Inventory Additions");
        sectionTitle.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        sectionTitle.setForeground(Theme.FOREST_DEEP);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        recentSection.add(sectionTitle, BorderLayout.NORTH);

        String[] columnNames = {"Name", "Category", "Quantity", "Price", "Status"};
        recentItemsModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        recentTable = new JTable(recentItemsModel);
        recentTable.setFont(FontLoader.getInter(13f, Font.PLAIN));
        recentTable.setRowHeight(36);
        recentTable.setGridColor(Theme.BORDER_SUBTLE);
        recentTable.setShowVerticalLines(false);
        recentTable.setSelectionBackground(Theme.CREAM_SURFACE);
        recentTable.setSelectionForeground(Theme.FOREST_DEEP);

        JTableHeader tableHeader = recentTable.getTableHeader();
        tableHeader.setFont(FontLoader.getInterSemiBold(11f));
        tableHeader.setBackground(Theme.CREAM_SURFACE);
        tableHeader.setForeground(Theme.SLATE_MUTED);
        tableHeader.setPreferredSize(new Dimension(800, 32));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE));

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 8, 0, 8));
        recentTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        recentTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

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

        recentTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel pillContainer = new JPanel(new GridBagLayout());
                pillContainer.setOpaque(true);
                pillContainer.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

                String status = (value != null) ? value.toString() : "";
                JLabel statusLabel = new JLabel(status);
                statusLabel.setFont(FontLoader.getInterSemiBold(11f));
                statusLabel.setOpaque(true);

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

        loadedPanel.add(Box.createVerticalStrut(16));
        loadedPanel.add(kpiPanel);
        loadedPanel.add(Box.createVerticalStrut(32));
        loadedPanel.add(recentSection);

        stateCardPanel.add(loadedPanel, "LOADED");
    }

    // --- Empty State Setup ---
    private void setupEmptyStatePanel() {
        EmptyStatePanel emptyStatePanel = new EmptyStatePanel(e -> showAddItemDialog());
        stateCardPanel.add(emptyStatePanel, "EMPTY");
    }

    // --- Error State Setup ---
    private void setupErrorStatePanel() {
        JPanel errorPanel = new JPanel(new GridBagLayout());
        errorPanel.setBackground(Color.WHITE);
        errorPanel.setBorder(new Theme.RoundedBorder(8, Theme.BORDER_SUBTLE, 1));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel errorIcon = new JLabel("⚠️");
        errorIcon.setFont(new Font("Serif", Font.PLAIN, 48));
        errorIcon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel errorTitle = new JLabel("Database Access Failure");
        errorTitle.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        errorTitle.setForeground(Theme.ERROR_FG);
        errorTitle.setAlignmentX(CENTER_ALIGNMENT);

        JLabel errorDesc = new JLabel("<html><center>We encountered a file lock or schema error while loading database metrics.<br>Please ensure the SQLite database is not locked by another process.</center></html>");
        errorDesc.setFont(FontLoader.getInter(13f, Font.PLAIN));
        errorDesc.setForeground(Theme.SLATE_MUTED);
        errorDesc.setAlignmentX(CENTER_ALIGNMENT);
        errorDesc.setHorizontalAlignment(SwingConstants.CENTER);

        JButton retryBtn = new JButton("Retry Connection");
        Theme.stylePrimaryButton(retryBtn);
        retryBtn.setAlignmentX(CENTER_ALIGNMENT);
        retryBtn.setMaximumSize(new Dimension(160, 36));
        retryBtn.setPreferredSize(new Dimension(160, 36));
        retryBtn.addActionListener(e -> onPageLoad());

        inner.add(errorIcon);
        inner.add(Box.createVerticalStrut(12));
        inner.add(errorTitle);
        inner.add(Box.createVerticalStrut(6));
        inner.add(errorDesc);
        inner.add(Box.createVerticalStrut(20));
        inner.add(retryBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        errorPanel.add(inner, gbc);

        stateCardPanel.add(errorPanel, "ERROR");
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

    private void showAddItemDialog() {
        Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        ItemDialog dialog = new ItemDialog(topFrame, "Register New Botanical Item", null);
        dialog.setVisible(true);

        if (dialog.isSucceeded() && dialog.getItem() != null) {
            Item newItem = dialog.getItem();
            new Thread(() -> {
                boolean added = itemService.addItem(newItem);
                if (added) {
                    System.out.println("Registered first item successfully: " + newItem.getName());
                    // Reload data
                    loadDashboardData();
                }
            }).start();
        }
    }

    @Override
    public void onPageLoad() {
        // Update user information
        User user = AppState.getInstance().getCurrentUser();
        if (user != null) {
            welcomeLabel.setText("Welcome back, " + user.getDisplayName() + " | Role: " + user.getRole());
        }

        // Show LOADING state and kick start shimmers
        stateLayout.show(stateCardPanel, "LOADING");
        startSkeletons();

        // Query database in background thread
        new Thread(this::loadDashboardData).start();
    }

    @Override
    public void onPageUnload() {
        stopSkeletons();
    }

    private void startSkeletons() {
        if (skeletonTable != null) skeletonTable.startAnimation();
        if (skeletonCard1 != null) skeletonCard1.startAnimation();
        if (skeletonCard2 != null) skeletonCard2.startAnimation();
        if (skeletonCard3 != null) skeletonCard3.startAnimation();
        if (skeletonCard4 != null) skeletonCard4.startAnimation();
    }

    private void stopSkeletons() {
        if (skeletonTable != null) skeletonTable.stopAnimation();
        if (skeletonCard1 != null) skeletonCard1.stopAnimation();
        if (skeletonCard2 != null) skeletonCard2.stopAnimation();
        if (skeletonCard3 != null) skeletonCard3.stopAnimation();
        if (skeletonCard4 != null) skeletonCard4.stopAnimation();
    }

    private void loadDashboardData() {
        // Mimic a tiny artificial network/disk delay (e.g. 1000ms) to let skeletons shine!
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            // Ignore
        }

        try {
            List<Item> items = itemService.getAllItems();
            
            if (items == null) {
                SwingUtilities.invokeLater(() -> {
                    stopSkeletons();
                    stateLayout.show(stateCardPanel, "ERROR");
                });
                return;
            }

            if (items.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    stopSkeletons();
                    stateLayout.show(stateCardPanel, "EMPTY");
                });
                return;
            }

            // Perform calculations
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
                stopSkeletons();
                
                // Update KPI values
                totalUnitsVal.setText(unitFormat.format(finalTotalUnits) + " units");
                totalValVal.setText(usdFormat.format(finalTotalValue));
                uniqueTypesVal.setText(finalUniqueTypes + " items");
                lowStockVal.setText(finalLowStockAlerts + " items");

                // Populate recent items table (first 5)
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

                // Show LOADED state
                stateLayout.show(stateCardPanel, "LOADED");
            });

        } catch (Exception e) {
            System.err.println("Dashboard loading failed.");
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                stopSkeletons();
                stateLayout.show(stateCardPanel, "ERROR");
            });
        }
    }
}
