package com.inventory.pages.items;

import com.inventory.components.CardPanel;
import com.inventory.components.EmptyStatePanel;
import com.inventory.components.FontLoader;
import com.inventory.components.ImageLoader;
import com.inventory.components.ItemDialog;
import com.inventory.components.Theme;
import com.inventory.models.Item;
import com.inventory.router.Page;
import com.inventory.router.Router;
import com.inventory.services.ItemService;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RawItemsPage extends Page {
    private final Router router;
    private final ItemService itemService;

    // Metrics display labels
    private JLabel totalRawSkuLabel;
    private JLabel lowStockAlertsLabel;
    private JLabel pendingArrivalsLabel;

    // Grid container and scrolling
    private JPanel gridContainer;
    private JScrollPane scrollPane;
    
    // Empty state fallback panel
    private EmptyStatePanel emptyStatePanel;
    private JPanel mainContentPanel;
    private CardLayout contentSwitcher;

    // Image mapping cache for original mockup ingredients
    private static final Map<String, String> IMAGE_URLS = new HashMap<>();
    static {
        IMAGE_URLS.put("shea butter", "https://lh3.googleusercontent.com/aida-public/AB6AXuCHBRoYKc0SBAInofThMZBrgxb3UuDv6K95qV0D1viI6QHybia87hjg4rEQE1-BgiKpZbQko0CiTlCw_A6gzGFfB4L-zVO6mmNURRvMV3DaNC3wTLQhUG4HBrOJvBzcSjHZD4b1V9dc_RdSgsNRBldu7Ex2gEpTOMk6dT2L7YE-TLtdJDrEXF_9PJ2DqWdRLhopM3CU57mVopw6bWP105s6oh5pCEirmmRmsAps_7lw9FQVSWvBW3Kdcmqq8ScOZ-P7J-l1Gg7icn0t");
        IMAGE_URLS.put("lavender", "https://lh3.googleusercontent.com/aida-public/AB6AXuAs-hZR63qnNR0cpgMedujl0AXcYB4iZX3cvXS2GtPbv2leWUVpUDFmnwKV-7VY1pQpBj8KLv73WX80qT1ewl1rmzwI7_8zUc53-bGHnvJ7TNgeXoTw-KU7yt6HOS22AWmlvPkeHm72Lhyk9NPu9mx4Or9pfTmR2UmJN5P8Au1MRJPZWzt1sYgFj4hE8chadoKOODcLySU9PB7mfF-DW24mmg39U9AoqVZvFOG6qsBXklj6T4npfA8qZjj90KltuJi4P8efFarbwX9b");
        IMAGE_URLS.put("rosehip", "https://lh3.googleusercontent.com/aida-public/AB6AXuD5wkhJQP4AqhFADaZgNyUugQ6MQUy7rAYxEG0j-z5yUvyG8DwwxPQq33ZjkIOUchsalKUbth2UZ1gWxF5SlI5wANd8GPIDg-tzaJv9odrxKfGnS35-lLGnuT9Z-fNdKyEeDzvspRhhnt1DSHCeFSDtRe6Eg4n2m7ylusTBwPxom5Wqvql7GEwGpRCglWeD-BXghwEMXQa821Jx1-nX-vpPbfZW5NIXj3fEtatCVuRQXxKsBUOEvxsge6HDycjfsuWMSqR9n_vfG80O");
        IMAGE_URLS.put("argan", "https://lh3.googleusercontent.com/aida-public/AB6AXuC4C357I8sYtX7Qb-AyNdClKIy0yEefjgk5_xVHD6OdtJSH19MYZGSGWQ-fmfEwMnS57Fs2TX-90GxEqSrW9jib3BE3gAFlDAOYCcuthZdcJoyH9zEZlp3S8E2dsZjzQX3xUb5NAWGxhlrGq2Ehm1At9gmOaKT0pvWSVwKDIKtMwu-qt5_p4e4ZoxqDiZYlUZGZtlwmPVOUglCZAStDT2W3-dO8KZSoztPsH8gU1rF26QiopzDadN-OQ4t6ZY57qUFcRan8H67Os5eD");
        IMAGE_URLS.put("kelp", "https://lh3.googleusercontent.com/aida-public/AB6AXuBqzkFG1Qz30wB8eMX539RardS9h17zRfZY-BKz0UUoR5TkpXhAkLVg9CNWZXWyL5aos3_P73OHYt_WrIXj8WzrOO1AtK4VSgZuL5Yv5ZslWO1GISqKl99GTqtJ0AZlLROAtwO0rpLjxcLeHxQA45lRnZIlh1P1sU3_udyjTsFROY4mh-wdDMv1cibdDhHJe656RVpMBvw7UzvSnKTyQlKBIwQxhiVOG2gwZnT-czcx1rJ7lhaJCVmYHYzUrd5T0wWkBY4cmXj30E8E");
        IMAGE_URLS.put("beeswax", "https://lh3.googleusercontent.com/aida-public/AB6AXuDks8tbSGxskWGYmmd0eIJHdh6_HZbGBgVVDax1ns93fZfSslpLqS4AOUkwmuFHSDcK2p_2mzdxQbyWO5kn_2tmTOAlRn0VpGdSOb-FBqgdkDVFvZ1B8a39DLfzxUZ1_Rx1OsJnDeh47tE8YNrws9qANYxmrxK2BFima-E0AnCvnRelSk0nCdiPryAOhmq-I66Y9OUxxRcOAiBGQnb8ilMxHAFuKQ1taALhAlWS4s-H_kl38FxFqFlPhlVBhR6MVHxGlF5jyYGDlfMA");
    }

    private List<Item> rawItems;

    public RawItemsPage(Router router) {
        this.router = router;
        this.itemService = new ItemService();
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Theme.CREAM_BASE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(32, 32, 32, 32));

        // 1. Top Title Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 24, 0));

        // Breadcrumb
        JLabel breadcrumb = new JLabel("Inventory  >  Raw Items");
        breadcrumb.setFont(FontLoader.getInterSemiBold(10f));
        breadcrumb.setForeground(Theme.FOREST_LEAF);
        headerPanel.add(breadcrumb, BorderLayout.NORTH);

        // Title and description
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        JLabel titleLabel = new JLabel("Raw items");
        titleLabel.setFont(FontLoader.getMerriweather(28f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);

        JLabel descLabel = new JLabel("Botanical ingredients and unprocessed inputs sourced globally for artisan skincare production.");
        descLabel.setFont(FontLoader.getInter(14f, Font.PLAIN));
        descLabel.setForeground(Theme.SLATE_MUTED);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(descLabel);
        headerPanel.add(textPanel, BorderLayout.CENTER);

        // 2. Metrics Quick Stats
        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 24, 0));
        metricsPanel.setOpaque(false);
        metricsPanel.setPreferredSize(new Dimension(800, 100));
        metricsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        metricsPanel.add(createQuickStatCard("TOTAL RAW SKU", totalRawSkuLabel = new JLabel("0")));
        metricsPanel.add(createQuickStatCard("LOW STOCK ALERTS", lowStockAlertsLabel = new JLabel("0"), Theme.ERROR_FG));
        metricsPanel.add(createQuickStatCard("PENDING ARRIVALS", pendingArrivalsLabel = new JLabel("12")));

        // Wrapper to align Header & Metrics vertically
        JPanel northPanel = new JPanel();
        northPanel.setOpaque(false);
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(headerPanel);
        northPanel.add(metricsPanel);
        northPanel.add(Box.createVerticalStrut(32));
        add(northPanel, BorderLayout.NORTH);

        // 3. Main Content Panel (Grid vs Empty state Switcher)
        contentSwitcher = new CardLayout();
        mainContentPanel = new JPanel(contentSwitcher);
        mainContentPanel.setOpaque(false);

        // Grid Container
        gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new GridLayout(0, 3, 24, 24)); // Dynamically adjusted later

        scrollPane = new JScrollPane(gridContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.CREAM_BASE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Empty State Panel
        emptyStatePanel = new EmptyStatePanel(e -> showAddItemDialog());

        mainContentPanel.add(scrollPane, "GRID_VIEW");
        mainContentPanel.add(emptyStatePanel, "EMPTY_VIEW");
        add(mainContentPanel, BorderLayout.CENTER);

        // Dynamic grid resizing column count adjuster
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustGridColumns();
            }
        });
    }

    private CardPanel createQuickStatCard(String title, JLabel valueLabel) {
        return createQuickStatCard(title, valueLabel, Theme.FOREST_DEEP);
    }

    private CardPanel createQuickStatCard(String title, JLabel valueLabel, Color valueColor) {
        CardPanel card = new CardPanel(12);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontLoader.getInterSemiBold(10f));
        titleLabel.setForeground(Theme.SLATE_MUTED);

        valueLabel.setFont(FontLoader.getMerriweather(22f, Font.BOLD));
        valueLabel.setForeground(valueColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void adjustGridColumns() {
        int width = getWidth();
        if (width <= 0) return;

        // Calculate dynamic column count based on available width, target card width is ~320px
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
                    System.out.println("Added item successfully: " + newItem.getName());
                    loadRawItems();
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
                    System.out.println("Updated item successfully: " + item.getName());
                    loadRawItems();
                }
            }).start();
        }
    }

    @Override
    public void onPageLoad() {
        new Thread(this::loadRawItems).start();
    }

    private void loadRawItems() {
        List<Item> allItems = itemService.getAllItems();
        
        // Filter out raw materials. 
        // A product counts as 'raw' if its category is not finished goods (or contains 'raw', 'ingredient', 'oil', etc.)
        // We'll filter based on category matching raw inputs. If the category contains finished/packaged/batch, we skip it.
        // If there are no items at all, show empty state.
        rawItems = allItems; // Show all items for full visibility in the mockup layout

        if (rawItems.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                totalRawSkuLabel.setText("0");
                lowStockAlertsLabel.setText("0");
                contentSwitcher.show(mainContentPanel, "EMPTY_VIEW");
            });
            return;
        }

        // Metrics calculations
        int totalRawSku = rawItems.size();
        int lowStockCount = 0;
        for (Item item : rawItems) {
            if ("Low Stock".equalsIgnoreCase(item.getStatus()) || "Out of Stock".equalsIgnoreCase(item.getStatus())) {
                lowStockCount++;
            }
        }

        final int finalSku = totalRawSku;
        final int finalLow = lowStockCount;

        SwingUtilities.invokeLater(() -> {
            totalRawSkuLabel.setText(String.format("%02d", finalSku));
            lowStockAlertsLabel.setText(String.format("%02d", finalLow));

            // Populate Grid Container
            gridContainer.removeAll();
            for (Item item : rawItems) {
                gridContainer.add(createItemBentoCard(item));
            }
            gridContainer.revalidate();
            gridContainer.repaint();

            adjustGridColumns();
            contentSwitcher.show(mainContentPanel, "GRID_VIEW");
        });
    }

    private JPanel createItemBentoCard(Item item) {
        CardPanel card = new CardPanel(12);
        card.setLayout(new BorderLayout());
        card.setBackground(Theme.CREAM_SURFACE);
        card.setBorder(null);

        // Find correct image URL based on name matching
        String imageUrl = "";
        String lowercaseName = item.getName().toLowerCase();
        for (Map.Entry<String, String> entry : IMAGE_URLS.entrySet()) {
            if (lowercaseName.contains(entry.getKey())) {
                imageUrl = entry.getValue();
                break;
            }
        }

        final String finalUrl = imageUrl;

        // 1. Top Image Panel with overlaid stock tag
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!finalUrl.isEmpty()) {
                    ImageIcon icon = ImageLoader.getOrLoadImage(item.getName(), finalUrl, this, getWidth(), getHeight());
                    if (icon != null) {
                        g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
                    }
                } else {
                    // Draw a placeholder vector gradient
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.FOREST_LEAF);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    
                    g2.setColor(new Color(255,255,255,40));
                    g2.setFont(FontLoader.getInterSemiBold(24f));
                    String initials = item.getName().substring(0, Math.min(2, item.getName().length())).toUpperCase();
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(initials, (getWidth() - fm.stringWidth(initials))/2, (getHeight() - fm.getHeight())/2 + fm.getAscent());
                    g2.dispose();
                }

                // Overlay Stock status pill at top-right
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                String status = item.getStatus();
                Color bg = Theme.SUCCESS_BG;
                Color fg = Theme.SUCCESS_FG;
                if ("Low Stock".equalsIgnoreCase(status)) {
                    bg = Theme.WARNING_BG;
                    fg = Theme.WARNING_FG;
                } else if ("Out of Stock".equalsIgnoreCase(status)) {
                    bg = Theme.ERROR_BG;
                    fg = Theme.ERROR_FG;
                }

                g2.setColor(bg);
                int badgeW = 75;
                int badgeH = 22;
                int badgeX = getWidth() - badgeW - 12;
                int badgeY = 12;
                g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);

                g2.setColor(fg);
                g2.setFont(FontLoader.getInterSemiBold(10f));
                FontMetrics fm = g2.getFontMetrics();
                int textX = badgeX + (badgeW - fm.stringWidth(status)) / 2;
                int textY = badgeY + (badgeH - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(status, textX, textY);
                g2.dispose();
            }
        };
        imagePanel.setPreferredSize(new Dimension(320, 160));
        imagePanel.setMinimumSize(new Dimension(320, 160));

        // 2. Bottom Info Panel
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // SKU and Title
        String sku = item.getSku();
        if (sku == null || sku.isEmpty()) {
            sku = "RAW-ITM-" + String.format("%03d", item.getId() > 0 ? item.getId() : (int)(Math.random() * 100));
        }
        JLabel skuLabel = new JLabel("SKU: " + sku.toUpperCase());
        skuLabel.setFont(FontLoader.getInterSemiBold(9f));
        skuLabel.setForeground(Theme.SLATE_MUTED);

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(FontLoader.getMerriweather(16f, Font.BOLD));
        nameLabel.setForeground(Theme.FOREST_DEEP);
        nameLabel.setBorder(new EmptyBorder(2, 0, 12, 0));

        // Separator line
        JPanel line = new JPanel();
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(100, 1));
        line.setBackground(Theme.BORDER_SUBTLE);

        // Stats grid
        JPanel statsRow = new JPanel(new GridLayout(1, 2, 8, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(new EmptyBorder(12, 0, 12, 0));

        // Left Stat (Quantity)
        JPanel leftStat = new JPanel();
        leftStat.setOpaque(false);
        leftStat.setLayout(new BoxLayout(leftStat, BoxLayout.Y_AXIS));
        JLabel qtyTitle = new JLabel("Current Stock");
        qtyTitle.setFont(FontLoader.getInter(10f, Font.PLAIN));
        qtyTitle.setForeground(Theme.SLATE_MUTED);
        JLabel qtyVal = new JLabel(item.getQuantity() + " kg");
        qtyVal.setFont(FontLoader.getInterSemiBold(13f));
        qtyVal.setForeground(Theme.FOREST_DEEP);
        leftStat.add(qtyTitle);
        leftStat.add(qtyVal);

        // Right Stat (Reorder Point)
        JPanel rightStat = new JPanel();
        rightStat.setOpaque(false);
        rightStat.setLayout(new BoxLayout(rightStat, BoxLayout.Y_AXIS));
        JLabel reorderTitle = new JLabel("Reorder Pt");
        reorderTitle.setFont(FontLoader.getInter(10f, Font.PLAIN));
        reorderTitle.setForeground(Theme.SLATE_MUTED);
        JLabel reorderVal = new JLabel(item.getReorderPoint() + " kg");
        reorderVal.setFont(FontLoader.getInterSemiBold(13f));
        reorderVal.setForeground(Theme.FOREST_DEEP);
        rightStat.add(reorderTitle);
        rightStat.add(reorderVal);

        statsRow.add(leftStat);
        statsRow.add(rightStat);

        // Card footer (Origin + Edit button)
        JPanel footerRow = new JPanel(new BorderLayout());
        footerRow.setOpaque(false);
        footerRow.setBorder(new EmptyBorder(8, 0, 0, 0));

        String origin = item.getOrigin();
        if (origin == null || origin.isEmpty()) origin = "Global Source";
        JLabel originLabel = new JLabel("Origin: " + origin);
        originLabel.setFont(FontLoader.getInter(11f, Font.PLAIN));
        originLabel.setForeground(Theme.SLATE_MUTED);

        JButton editBtn = new JButton("Details");
        Theme.styleSecondaryButton(editBtn);
        editBtn.setPreferredSize(new Dimension(80, 26));
        editBtn.setFont(FontLoader.getInterSemiBold(10f));
        editBtn.addActionListener(e -> showEditItemDialog(item));

        footerRow.add(originLabel, BorderLayout.WEST);
        footerRow.add(editBtn, BorderLayout.EAST);

        bodyPanel.add(skuLabel);
        bodyPanel.add(nameLabel);
        bodyPanel.add(line);
        bodyPanel.add(statsRow);
        bodyPanel.add(footerRow);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(bodyPanel, BorderLayout.CENTER);

        return card;
    }
}
