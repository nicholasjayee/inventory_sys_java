package com.inventory.pages.items;

import com.inventory.components.FontLoader;
import com.inventory.components.ItemDialog;
import com.inventory.components.Theme;
import com.inventory.models.Item;
import com.inventory.router.Page;
import com.inventory.router.Router;
import com.inventory.services.ItemService;
import java.awt.*;
import java.text.NumberFormat;
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
    private List<Item> loadedItems;

    public ItemsPage(Router router) {
        this.router = router;
        this.itemService = new ItemService();
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Theme.CREAM_BASE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(32, 32, 32, 32));

        // Top Header Section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(0, 0, 24, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel pageTitle = new JLabel("Items Library");
        pageTitle.setFont(FontLoader.getMerriweather(28f, Font.BOLD));
        pageTitle.setForeground(Theme.FOREST_DEEP);
        JLabel pageSubtitle = new JLabel("Manage organic skincare raw materials and processed inventories.");
        pageSubtitle.setFont(FontLoader.getInter(14f, Font.PLAIN));
        pageSubtitle.setForeground(Theme.SLATE_MUTED);
        titlePanel.add(pageTitle, BorderLayout.NORTH);
        titlePanel.add(pageSubtitle, BorderLayout.SOUTH);

        // Actions Panel: Search + Add Button
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionsPanel.setOpaque(false);

        searchField = new JTextField();
        Theme.styleTextField(searchField);
        searchField.setPreferredSize(new Dimension(240, 36));
        // Add placeholder text (tip)
        searchField.setToolTipText("Search by item name or category...");
        
        // Add live search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }
            
            private void filter() {
                String query = searchField.getText().trim();
                new Thread(() -> loadItemsData(query)).start();
            }
        });

        JButton addItemBtn = new JButton("+ Add New Item");
        Theme.stylePrimaryButton(addItemBtn);
        addItemBtn.setPreferredSize(new Dimension(140, 36));
        addItemBtn.addActionListener(e -> showAddItemDialog());

        actionsPanel.add(new JLabel("Search: "));
        actionsPanel.add(searchField);
        actionsPanel.add(addItemBtn);

        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(actionsPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Data Table
        String[] columnNames = {"Name", "Category", "Quantity", "Price", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the actions column (column 5) is editable (so buttons can click)
                return column == 5;
            }
        };

        itemsTable = new JTable(tableModel);
        itemsTable.setFont(FontLoader.getInter(13f, Font.PLAIN));
        itemsTable.setRowHeight(44);
        itemsTable.setGridColor(Theme.BORDER_SUBTLE);
        itemsTable.setShowVerticalLines(false);
        itemsTable.setSelectionBackground(Theme.CREAM_SURFACE);
        itemsTable.setSelectionForeground(Theme.FOREST_DEEP);

        // Styling table header
        JTableHeader tableHeader = itemsTable.getTableHeader();
        tableHeader.setFont(FontLoader.getInterSemiBold(11f));
        tableHeader.setBackground(Theme.CREAM_SURFACE);
        tableHeader.setForeground(Theme.SLATE_MUTED);
        tableHeader.setPreferredSize(new Dimension(800, 36));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SUBTLE));

        // Alignments and Cell Renderers
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(new EmptyBorder(0, 8, 0, 8));
        
        itemsTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        itemsTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

        // Numeric renderer
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
        itemsTable.getColumnModel().getColumn(2).setCellRenderer(numericRenderer);
        itemsTable.getColumnModel().getColumn(3).setCellRenderer(numericRenderer);

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

        JScrollPane scrollPane = new JScrollPane(itemsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_SUBTLE, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
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
                    System.out.println("Updated item successfully: " + item.getName());
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
                boolean deleted = itemService.deleteItem(item.getId());
                if (deleted) {
                    System.out.println("Deleted item ID: " + item.getId());
                    loadItemsData("");
                }
            }).start();
        }
    }

    @Override
    public void onPageLoad() {
        searchField.setText("");
        new Thread(() -> loadItemsData("")).start();
    }

    private void loadItemsData(String query) {
        if (query == null || query.isEmpty()) {
            loadedItems = itemService.getAllItems();
        } else {
            loadedItems = itemService.searchItems(query);
        }

        NumberFormat usdFormat = NumberFormat.getCurrencyInstance(Locale.US);
        NumberFormat unitFormat = NumberFormat.getNumberInstance(Locale.US);

        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (Item item : loadedItems) {
                tableModel.addRow(new Object[]{
                    item.getName(),
                    item.getCategory(),
                    unitFormat.format(item.getQuantity()),
                    usdFormat.format(item.getPrice()),
                    item.getStatus(),
                    item // Put the item object directly in the Actions cell so the editor can access it
                });
            }
        });
    }

    // Helper Action Buttons Renderer and Editor class
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
                setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));
                setOpaque(true);

                JButton editBtn = new JButton("Edit");
                Theme.styleSecondaryButton(editBtn);
                editBtn.setPreferredSize(new Dimension(64, 26));
                editBtn.setFont(FontLoader.getInterSemiBold(11f));
                editBtn.addActionListener(e -> {
                    fireEditingStopped();
                    showEditItemDialog(currentItem);
                });

                JButton deleteBtn = new JButton("Delete");
                Theme.styleSecondaryButton(deleteBtn);
                deleteBtn.setForeground(Theme.ERROR_FG);
                deleteBtn.setPreferredSize(new Dimension(72, 26));
                deleteBtn.setFont(FontLoader.getInterSemiBold(11f));
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
