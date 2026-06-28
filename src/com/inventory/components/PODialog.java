package com.inventory.components;

import com.inventory.models.Item;
import com.inventory.models.PurchaseOrder;
import com.inventory.services.ItemService;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PODialog extends JDialog {
    private JComboBox<ItemComboItem> itemCombo;
    private JTextField supplierField;
    private JSpinner quantitySpinner;
    private boolean succeeded;
    private PurchaseOrder purchaseOrder;

    // Helper class for rendering items in JComboBox
    private static class ItemComboItem {
        Item item;
        ItemComboItem(Item item) { this.item = item; }
        @Override
        public String toString() { return item.getName() + " (" + item.getSku() + ")"; }
    }

    public PODialog(Frame parent) {
        super(parent, "Create Purchase Order", true);
        setSize(480, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.CREAM_BASE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(24, 24, 16, 24));
        JLabel titleLabel = new JLabel("Log Pending Arrival");
        titleLabel.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        JLabel subtitleLabel = new JLabel("Record a new purchase order from a supplier.");
        subtitleLabel.setFont(FontLoader.getInter(12f, Font.PLAIN));
        subtitleLabel.setForeground(Theme.SLATE_MUTED);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subtitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Body
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(12, 24, 12, 24));

        JLabel itemLabel = new JLabel("SELECT ITEM");
        itemLabel.setFont(FontLoader.getInterSemiBold(10f));
        itemLabel.setForeground(Theme.FOREST_DEEP);
        
        itemCombo = new JComboBox<>();
        itemCombo.setFont(FontLoader.getInter(13f, Font.PLAIN));
        itemCombo.setBackground(Color.WHITE);
        itemCombo.setMaximumSize(new Dimension(432, 36));
        itemCombo.setAlignmentX(LEFT_ALIGNMENT);
        
        // Populate items
        ItemService is = new ItemService();
        List<Item> items = is.getAllItems();
        for (Item it : items) {
            itemCombo.addItem(new ItemComboItem(it));
        }

        JLabel supplierLabel = new JLabel("SUPPLIER NAME");
        supplierLabel.setFont(FontLoader.getInterSemiBold(10f));
        supplierLabel.setForeground(Theme.FOREST_DEEP);
        
        supplierField = new JTextField();
        Theme.styleTextField(supplierField);
        supplierField.setMaximumSize(new Dimension(432, 36));
        supplierField.setAlignmentX(LEFT_ALIGNMENT);

        JLabel qtyLabel = new JLabel("ORDER QUANTITY");
        qtyLabel.setFont(FontLoader.getInterSemiBold(10f));
        qtyLabel.setForeground(Theme.FOREST_DEEP);
        
        quantitySpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000000, 10));
        quantitySpinner.setMaximumSize(new Dimension(200, 36));
        quantitySpinner.setAlignmentX(LEFT_ALIGNMENT);
        quantitySpinner.setFont(FontLoader.getInter(13f, Font.PLAIN));
        
        bodyPanel.add(itemLabel);
        bodyPanel.add(Box.createVerticalStrut(4));
        bodyPanel.add(itemCombo);
        bodyPanel.add(Box.createVerticalStrut(16));
        bodyPanel.add(supplierLabel);
        bodyPanel.add(Box.createVerticalStrut(4));
        bodyPanel.add(supplierField);
        bodyPanel.add(Box.createVerticalStrut(16));
        bodyPanel.add(qtyLabel);
        bodyPanel.add(Box.createVerticalStrut(4));
        bodyPanel.add(quantitySpinner);
        
        add(bodyPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footerPanel.setBackground(Theme.CREAM_SURFACE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE));
        
        JButton cancelBtn = new JButton("Cancel");
        Theme.styleSecondaryButton(cancelBtn);
        cancelBtn.addActionListener(e -> dispose());
        
        JButton saveBtn = new JButton("Create Order");
        Theme.stylePrimaryButton(saveBtn);
        saveBtn.addActionListener(e -> {
            if (supplierField.getText().trim().isEmpty() || itemCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select an item and provide a supplier name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ItemComboItem selected = (ItemComboItem) itemCombo.getSelectedItem();
            purchaseOrder = new PurchaseOrder(
                selected.item.getUuid(),
                supplierField.getText().trim(),
                (Integer) quantitySpinner.getValue(),
                "Pending"
            );
            succeeded = true;
            dispose();
        });
        
        footerPanel.add(cancelBtn);
        footerPanel.add(saveBtn);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    public boolean isSucceeded() { return succeeded; }
    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
}
