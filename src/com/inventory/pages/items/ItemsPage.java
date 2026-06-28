package com.inventory.pages.items;

import com.inventory.components.FontLoader;
import com.inventory.components.ImageLoader;
import com.inventory.components.ItemDialog;
import com.inventory.components.Theme;
import com.inventory.models.Item;
import com.inventory.router.Page;
import com.inventory.router.Router;
import com.inventory.services.ItemService;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;

public class ItemsPage extends Page {
    private final Router router;
    private final ItemService itemService;

    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable itemsTable;
    
    // Segment controller buttons
    private JButton tabAllBtn;
    private JButton tabRawBtn;
    private JButton tabProcessedBtn;
    private String activeTab = "ALL"; // "ALL", "RAW", "PROCESSED"

    private List<Item> loadedItems = new ArrayList<>();

    public ItemsPage(Router router) {
        this.router = router;
        this.itemService = new ItemService();
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Theme.CREAM_BASE);
        setLayout(new BorderLayout());

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

        // --- 1. Hero Banner Panel ---
        HeroBannerPanel heroPanel = new HeroBannerPanel();
        scrollContent.add(heroPanel, BorderLayout.NORTH);

        // --- 2. Main Body Container ---
        JPanel bodyContainer = new JPanel(new BorderLayout());
        bodyContainer.setOpaque(false);
        bodyContainer.setBorder(new EmptyBorder(24, 32, 32, 32));

        // Controls bar panel (Filters segment + Actions)
        JPanel controlsBar = new JPanel(new BorderLayout());
        controlsBar.setOpaque(false);
        controlsBar.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Segment Tabs (Left)
        JPanel tabsWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabsWrapper.setOpaque(true);
        tabsWrapper.setBackground(Theme.CREAM_SURFACE);
        tabsWrapper.setBorder(BorderFactory.createCompoundBorder(
            new Theme.RoundedBorder(6, Theme.BORDER_SUBTLE, 1),
            new EmptyBorder(2, 2, 2, 2)
        ));

        tabAllBtn = createTabButton("View All", "ALL");
        tabRawBtn = createTabButton("View Raw", "RAW");
        tabProcessedBtn = createTabButton("View Processed", "PROCESSED");

        tabsWrapper.add(tabAllBtn);
        tabsWrapper.add(tabRawBtn);
        tabsWrapper.add(tabProcessedBtn);

        // Search & Add Button (Right)
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightActions.setOpaque(false);

        searchField = new JTextField();
        Theme.styleTextField(searchField);
        searchField.setPreferredSize(new Dimension(220, 36));
        searchField.setToolTipText("Search items...");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }
            
            private void filter() {
                loadItemsData(searchField.getText().trim());
            }
        });

        JButton addItemBtn = new JButton("+ New Item");
        Theme.stylePrimaryButton(addItemBtn);
        addItemBtn.setPreferredSize(new Dimension(120, 36));
        addItemBtn.addActionListener(e -> showAddItemDialog());

        rightActions.add(new JLabel("Search: "));
        rightActions.add(searchField);
        rightActions.add(addItemBtn);

        controlsBar.add(tabsWrapper, BorderLayout.WEST);
        controlsBar.add(rightActions, BorderLayout.EAST);
        bodyContainer.add(controlsBar, BorderLayout.NORTH);

        // --- 3. Inventory Table ---
        String[] columnNames = {"Name", "Category", "Quantity", "Price", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only actions column is editable
            }
        };

        itemsTable = new JTable(tableModel);
        itemsTable.setFont(FontLoader.getInter(13f, Font.PLAIN));
        itemsTable.setRowHeight(56); // High rows to fit Name + SKU stack
        itemsTable.setGridColor(Theme.BORDER_SUBTLE);
        itemsTable.setShowVerticalLines(false);
        itemsTable.setSelectionBackground(Theme.CREAM_SURFACE);
        itemsTable.setSelectionForeground(Theme.FOREST_DEEP);

        // Header Styling
        JTableHeader tableHeader = itemsTable.getTableHeader();
        tableHeader.setFont(FontLoader.getInterSemiBold(11f));
        tableHeader.setBackground(Theme.CREAM_SURFACE);
        tableHeader.setForeground(Theme.SLATE_MUTED);
        tableHeader.setPreferredSize(new Dimension(800, 38));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE));

        // Custom Cell Renderers
        itemsTable.getColumnModel().getColumn(0).setCellRenderer(new ItemNameCellRenderer()); // Custom SKU stack + Icon
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 8, 0, 8));
        itemsTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer); // Category

        // Numeric fields right align
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
        itemsTable.getColumnModel().getColumn(2).setCellRenderer(numericRenderer); // Qty
        itemsTable.getColumnModel().getColumn(3).setCellRenderer(numericRenderer); // Price

        // Status pill renderer
        itemsTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel pillContainer = new JPanel(new GridBagLayout());
                pillContainer.setOpaque(true);
                pillContainer.setBackground(table.getBackground());
                if (isSelected) pillContainer.setBackground(table.getSelectionBackground());

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

        // Actions Column Custom Renderer & Editor
        ActionButtonsRendererEditor actionRendererEditor = new ActionButtonsRendererEditor();
        itemsTable.getColumnModel().getColumn(5).setCellRenderer(actionRendererEditor);
        itemsTable.getColumnModel().getColumn(5).setCellEditor(actionRendererEditor);
        itemsTable.getColumnModel().getColumn(5).setPreferredWidth(160);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createLineBorder(Theme.BORDER_SUBTLE, 1));
        tableContainer.add(itemsTable.getTableHeader(), BorderLayout.NORTH);
        tableContainer.add(itemsTable, BorderLayout.CENTER);
        
        bodyContainer.add(tableContainer, BorderLayout.CENTER);
        scrollContent.add(bodyContainer, BorderLayout.CENTER);

        JScrollPane mainScrollPane = new JScrollPane(scrollContent);
        mainScrollPane.setBorder(null);
        mainScrollPane.getViewport().setBackground(Theme.CREAM_BASE);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(mainScrollPane, BorderLayout.CENTER);
    }

    private JButton createTabButton(String text, String tabCode) {
        JButton btn = new JButton(text);
        btn.setFont(FontLoader.getInterSemiBold(11f));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(6, 16, 6, 16));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        
        // Initial Theme styles
        updateTabButtonVisuals(btn, tabCode.equals(activeTab));

        btn.addActionListener(e -> {
            activeTab = tabCode;
            updateTabButtonVisuals(tabAllBtn, "ALL".equals(activeTab));
            updateTabButtonVisuals(tabRawBtn, "RAW".equals(activeTab));
            updateTabButtonVisuals(tabProcessedBtn, "PROCESSED".equals(activeTab));
            loadItemsData(searchField.getText().trim());
        });
        return btn;
    }

    private void updateTabButtonVisuals(JButton btn, boolean isActive) {
        if (isActive) {
            btn.setBackground(Theme.FOREST_DEEP);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                new Theme.RoundedBorder(4, Theme.FOREST_DEEP, 1),
                new EmptyBorder(6, 16, 6, 16)
            ));
        } else {
            btn.setBackground(Theme.CREAM_SURFACE);
            btn.setForeground(Theme.FOREST_LEAF);
            btn.setBorder(BorderFactory.createCompoundBorder(
                new Theme.RoundedBorder(4, Theme.CREAM_SURFACE, 1),
                new EmptyBorder(6, 16, 6, 16)
            ));
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
                    loadItemsData("");
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
                    loadItemsData("");
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
                    loadItemsData("");
                }
            }).start();
        }
    }

    @Override
    public void onPageLoad() {
        String globalQ = com.inventory.state.AppState.getInstance().getGlobalSearchQuery();
        final String initialSearch;
        if (globalQ != null && !globalQ.isEmpty()) {
            searchField.setText(globalQ);
            initialSearch = globalQ;
            com.inventory.state.AppState.getInstance().setGlobalSearchQuery(""); // consume it
        } else {
            searchField.setText("");
            initialSearch = "";
        }
        new Thread(() -> loadItemsData(initialSearch)).start();
    }

    private void loadItemsData(String query) {
        List<Item> allItems;
        if (query == null || query.isEmpty()) {
            allItems = itemService.getAllItems();
        } else {
            allItems = itemService.searchItems(query);
        }

        // Apply Tab Filter locally
        loadedItems.clear();
        for (Item item : allItems) {
            if ("RAW".equals(activeTab)) {
                if (item.isRaw()) {
                    loadedItems.add(item);
                }
            } else if ("PROCESSED".equals(activeTab)) {
                if (item.isProcessed()) {
                    loadedItems.add(item);
                }
            } else {
                // ALL
                loadedItems.add(item);
            }
        }

        NumberFormat usdFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat unitFormat = NumberFormat.getNumberInstance(Locale.US);

        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (Item item : loadedItems) {
                tableModel.addRow(new Object[]{
                    item, // Name cell takes the whole item object for the Custom SKU Renderer
                    item.getCategory(),
                    unitFormat.format(item.getQuantity()) + ("Raw Items".equalsIgnoreCase(item.getCategory()) ? " kg" : " Units"),
                    usdFormat.format(item.getPrice()) + ("Raw Items".equalsIgnoreCase(item.getCategory()) ? " / kg" : " / ea"),
                    item.getStatus(),
                    item // Actions Cell Editor takes the item object
                });
            }
        });
    }

    // --- Custom Name + SKU + Icon Cell Renderer ---
    private static class ItemNameCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (!(value instanceof Item)) {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }

            Item item = (Item) value;

            JPanel container = new JPanel(new BorderLayout(12, 0));
            container.setOpaque(true);
            container.setBorder(new EmptyBorder(8, 8, 8, 8));
            container.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            // 1. Left Rounded Icon Box (leaf vs cog)
            boolean isRaw = item.getCategory().toLowerCase().contains("raw") || item.getCategory().toLowerCase().contains("ingredient");
            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    int w = getWidth();
                    int h = getHeight();
                    
                    // Rounded border background
                    g2.setColor(Theme.CREAM_SURFACE);
                    g2.fill(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 6, 6));
                    g2.setColor(Theme.BORDER_SUBTLE);
                    g2.draw(new RoundRectangle2D.Double(0, 0, w - 1, h - 1, 6, 6));

                    // Paint Eco Leaf or Cog vector
                    g2.setStroke(new BasicStroke(1.5f));
                    if (isRaw) {
                        g2.setColor(Theme.FOREST_LEAF);
                        // Simple Leaf curve
                        Path2D.Double leaf = new Path2D.Double();
                        leaf.moveTo(w * 0.5, h * 0.25);
                        leaf.curveTo(w * 0.25, h * 0.35, w * 0.3, h * 0.65, w * 0.5, h * 0.75);
                        leaf.curveTo(w * 0.7, h * 0.65, w * 0.75, h * 0.35, w * 0.5, h * 0.25);
                        g2.draw(leaf);
                        g2.drawLine((int)(w*0.5), (int)(h*0.3), (int)(w*0.5), (int)(h*0.7));
                    } else {
                        g2.setColor(Theme.SLATE_MUTED);
                        // Simple Cog vector (circle + notches)
                        g2.drawOval(w/2 - 7, h/2 - 7, 14, 14);
                        g2.drawOval(w/2 - 3, h/2 - 3, 6, 6);
                        for (int angle = 0; angle < 360; angle += 45) {
                            double rad = Math.toRadians(angle);
                            int x1 = (int)(w/2 + 7 * Math.cos(rad));
                            int y1 = (int)(h/2 + 7 * Math.sin(rad));
                            int x2 = (int)(w/2 + 10 * Math.cos(rad));
                            int y2 = (int)(h/2 + 10 * Math.sin(rad));
                            g2.drawLine(x1, y1, x2, y2);
                        }
                    }
                    g2.dispose();
                }
            };
            iconPanel.setPreferredSize(new Dimension(36, 36));
            iconPanel.setOpaque(false);

            // 2. Right Text panel (Stacked Name + SKU)
            JPanel textPanel = new JPanel();
            textPanel.setOpaque(false);
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel(item.getName());
            nameLabel.setFont(FontLoader.getInterSemiBold(13f));
            nameLabel.setForeground(Theme.FOREST_DEEP);

            String sku = item.getSku() != null ? item.getSku() : "RAW-ITM-" + item.getId();
            JLabel skuLabel = new JLabel("SKU: " + sku.toUpperCase());
            skuLabel.setFont(FontLoader.getInter(10f, Font.PLAIN));
            skuLabel.setForeground(Theme.SLATE_MUTED);

            textPanel.add(nameLabel);
            textPanel.add(Box.createVerticalStrut(2));
            textPanel.add(skuLabel);

            container.add(iconPanel, BorderLayout.WEST);
            container.add(textPanel, BorderLayout.CENTER);

            return container;
        }
    }

    // --- Hero Banner Class ---
    private static class HeroBannerPanel extends JPanel {
        private final String imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAH2W0HmgKCdcgjZ3TBguiGy2QJThteqAZ5bMWToTC-BkKMjC7cepybTWUR2MHStw0A2Q3MIAWM8OJ5oWybKDcegL38zj4T19_-RK2BDxFBjTRSso60_31xqElN-Ovc_g_gPP6f3drS3LBRko2ef5zVTBPyPpMS8qTLbU9Lkj2zwFUi02HrsBKTWmR1WzLy3udgqw-HVixlf6YZYWUIpISK7l3WStni1es4hfw8k244GuumvgRoaf-ipN2glkm98576XD_pwPmPTd09";
        
        public HeroBannerPanel() {
            setPreferredSize(new Dimension(800, 220));
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(32, 40, 32, 40));
            
            JPanel textPanel = new JPanel();
            textPanel.setOpaque(false);
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            
            JLabel breadcrumb = new JLabel("DASHBOARD  >  ITEMS LIBRARY");
            breadcrumb.setFont(FontLoader.getInterSemiBold(10f));
            breadcrumb.setForeground(new Color(255, 255, 255, 160));
            
            JLabel title = new JLabel("Items Library");
            title.setFont(FontLoader.getMerriweather(28f, Font.BOLD));
            title.setForeground(Color.WHITE);
            title.setBorder(new EmptyBorder(6, 0, 6, 0));
            
            JLabel subtitle = new JLabel("<html><body style='width: 540px;'>Manage your comprehensive catalog of raw botanical ingredients and processed organic formulas. Maintain precision in your inventory levels to ensure production excellence.</body></html>");
            subtitle.setFont(FontLoader.getInter(13f, Font.PLAIN));
            subtitle.setForeground(new Color(255, 255, 255, 210));
            
            textPanel.add(breadcrumb);
            textPanel.add(title);
            textPanel.add(subtitle);
            
            add(textPanel, BorderLayout.WEST);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon icon = ImageLoader.getOrLoadImage("items_hero_banner", imageUrl, this, getWidth(), getHeight());
            if (icon != null) {
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
            
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Linear gradient overlay from forest-deep green (85% opacity) to light alpha
            Color startColor = new Color(26, 60, 52, 220); // forest-deep green
            Color endColor = new Color(26, 60, 52, 70);
            
            LinearGradientPaint lgp = new LinearGradientPaint(
                new Point2D.Float(0, 0),
                new Point2D.Float(getWidth() * 0.75f, 0),
                new float[]{0.0f, 1.0f},
                new Color[]{startColor, endColor}
            );
            g2.setPaint(lgp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    // --- Helper Action Buttons Renderer and Editor class ---
    private class ActionButtonsRendererEditor extends AbstractCellEditor implements TableCellEditor, javax.swing.table.TableCellRenderer {
        private final ActionPanel panel = new ActionPanel();
        private Item currentItem;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return panel;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentItem = (Item) value;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentItem;
        }

        private class ActionPanel extends JPanel {
            public ActionPanel() {
                setLayout(new FlowLayout(FlowLayout.CENTER, 8, 14));
                setOpaque(true);

                JButton editBtn = new JButton("Edit");
                Theme.styleSecondaryButton(editBtn);
                editBtn.setPreferredSize(new Dimension(76, 26));
                editBtn.setFont(FontLoader.getInterSemiBold(11f));
                try {
                    ImageIcon editIcon = new ImageIcon("assets/icons/edit.png");
                    Image scaled = editIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
                    editBtn.setIcon(new ImageIcon(scaled));
                    editBtn.setIconTextGap(4);
                } catch (Exception e) {}
                editBtn.addActionListener(e -> {
                    fireEditingStopped();
                    showEditItemDialog(currentItem);
                });

                JButton deleteBtn = new JButton("Delete");
                Theme.styleSecondaryButton(deleteBtn);
                deleteBtn.setForeground(Theme.ERROR_FG);
                deleteBtn.setPreferredSize(new Dimension(84, 26));
                deleteBtn.setFont(FontLoader.getInterSemiBold(11f));
                try {
                    ImageIcon deleteIcon = new ImageIcon("assets/icons/delete.png");
                    Image scaled = deleteIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
                    deleteBtn.setIcon(new ImageIcon(scaled));
                    deleteBtn.setIconTextGap(4);
                } catch (Exception e) {}
                deleteBtn.addActionListener(e -> {
                    fireEditingStopped();
                    handleDeleteItem(currentItem);
                });

                add(editBtn);
                add(deleteBtn);
            }
        }
    }
}
