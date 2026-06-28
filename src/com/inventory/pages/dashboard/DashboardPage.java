package com.inventory.pages.dashboard;

import com.inventory.components.EmptyStatePanel;
import com.inventory.components.ItemDialog;
import com.inventory.components.Theme;
import com.inventory.models.Item;
import com.inventory.pages.dashboard.components.DashboardHeader;
import com.inventory.pages.dashboard.components.ProductBentoCard;
import com.inventory.pages.dashboard.components.StatsBar;
import com.inventory.router.Page;
import com.inventory.router.Router;
import com.inventory.services.ItemService;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardPage extends Page {
    private final Router router;
    private final ItemService itemService;

    // Modularized Components
    private DashboardHeader headerPanel;
    private StatsBar statsBar;

    // Grid Layout and Scrollpane
    private JPanel gridContainer;
    private JScrollPane scrollPane;
    private CardLayout contentSwitcher;
    private JPanel mainContentPanel;
    private EmptyStatePanel emptyStatePanel;

    private List<Item> loadedItems;

    public DashboardPage(Router router) {
        this.router = router;
        this.itemService = new ItemService();
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Theme.CREAM_BASE);
        setLayout(new BorderLayout());
        setBorder(null);

        // Wrapper that tracks viewport width to prevent horizontal scrolling
        JPanel scrollContent = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                if (getParent() instanceof JViewport) {
                    d.width = getParent().getWidth();
                }
                return d;
            }
        };
        scrollContent.setOpaque(false);
        scrollContent.setBorder(new EmptyBorder(32, 32, 32, 32));

        // 1. Header component (with Create Item callback)
        headerPanel = new DashboardHeader(this::showAddItemDialog);

        // 2. Stats Bar component (RAW, PROCESSED, LOW STOCK circular KPIs)
        statsBar = new StatsBar();

        // Group Header & Stats Bar vertically
        JPanel northPanel = new JPanel();
        northPanel.setOpaque(false);
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(headerPanel);
        northPanel.add(statsBar);
        northPanel.add(Box.createVerticalStrut(32));
        scrollContent.add(northPanel, BorderLayout.NORTH);

        // 3. Grid Content Container (Empty vs Populated Bento list layout)
        contentSwitcher = new CardLayout();
        mainContentPanel = new JPanel(contentSwitcher);
        mainContentPanel.setOpaque(false);

        gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new GridLayout(0, 3, 24, 24)); // Auto cols calculated on resize

        // Empty state trigger callback
        emptyStatePanel = new EmptyStatePanel(e -> showAddItemDialog());

        mainContentPanel.add(gridContainer, "GRID_VIEW");
        mainContentPanel.add(emptyStatePanel, "EMPTY_VIEW");
        scrollContent.add(mainContentPanel, BorderLayout.CENTER);

        scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.CREAM_BASE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        // Grid Resize columns auto adjuster
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustGridColumns();
            }
        });
    }

    private void adjustGridColumns() {
        int width = getWidth();
        if (width <= 0) return;

        int cols = Math.max(1, (width - 64) / 340);
        GridLayout layout = (GridLayout) gridContainer.getLayout();
        if (layout.getColumns() != cols) {
            layout.setColumns(cols);
            gridContainer.revalidate();
        }
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
                    loadDashboardData();
                }
            }).start();
        }
    }

    private void showEditItemDialog(Item item) {
        Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        ItemDialog dialog = new ItemDialog(topFrame, "Update Item Detail", item);
        dialog.setVisible(true);

        if (dialog.isSucceeded()) {
            new Thread(() -> {
                boolean updated = itemService.updateItem(item);
                if (updated) {
                    loadDashboardData();
                }
            }).start();
        }
    }

    private void handleDeleteItem(Item item) {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete " + item.getName() + "?\nThis operation cannot be undone.",
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                boolean deleted = itemService.deleteItemByUuid(item.getUuid());
                if (deleted) {
                    loadDashboardData();
                }
            }).start();
        }
    }

    @Override
    public void onPageLoad() {
        new Thread(this::loadDashboardData).start();
    }

    private void loadDashboardData() {
        try {
            loadedItems = itemService.getAllItems();
            
            if (loadedItems.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    headerPanel.updateMetrics(0, 0.0);
                    statsBar.updateMetrics(0, 0, 0);
                    contentSwitcher.show(mainContentPanel, "EMPTY_VIEW");
                });
                return;
            }

            // Calculations
            int totalUnits = 0;
            double totalValue = 0.0;
            int rawStock = 0;
            int processedStock = 0;
            int lowStockCount = 0;

            for (Item item : loadedItems) {
                totalUnits += item.getQuantity();
                totalValue += item.getQuantity() * item.getPrice();
                
                if (item.isRaw()) {
                    rawStock += item.getQuantity();
                } else {
                    processedStock += item.getQuantity();
                }

                if ("Low Stock".equalsIgnoreCase(item.getStatus()) || "Out of Stock".equalsIgnoreCase(item.getStatus())) {
                    lowStockCount++;
                }
            }

            final int finalUnits = totalUnits;
            final double finalVal = totalValue;
            final int finalRaw = rawStock;
            final int finalProcessed = processedStock;
            final int finalLow = lowStockCount;

            SwingUtilities.invokeLater(() -> {
                // Update modularized Header and Stats components
                headerPanel.updateMetrics(finalUnits, finalVal);
                statsBar.updateMetrics(finalRaw, finalProcessed, finalLow);

                // Populate grid bento cards using ProductBentoCard component
                gridContainer.removeAll();
                for (Item item : loadedItems) {
                    gridContainer.add(new ProductBentoCard(item, this::showEditItemDialog, this::handleDeleteItem));
                }
                gridContainer.revalidate();
                gridContainer.repaint();

                adjustGridColumns();
                contentSwitcher.show(mainContentPanel, "GRID_VIEW");
            });

        } catch (Exception e) {
            System.err.println("Database load error on DashboardPage.");
            e.printStackTrace();
        }
    }
}
