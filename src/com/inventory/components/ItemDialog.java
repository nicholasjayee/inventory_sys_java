package com.inventory.components;

import com.inventory.models.Item;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ItemDialog extends JDialog {
    private JTextField nameField;
    private JTextField categoryField;
    private JSpinner quantitySpinner;
    private JTextField priceField;
    
    private boolean succeeded = false;
    private Item item;

    public ItemDialog(Frame owner, String title, Item itemToEdit) {
        super(owner, title, true);
        this.item = itemToEdit;
        initializeUI();
    }

    private void initializeUI() {
        setSize(420, 480);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        getContentPane().setBackground(Theme.CREAM_BASE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(24, 24, 8, 24));
        
        JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        
        JLabel subtitleLabel = new JLabel(item == null ? "Fill in the details to add a new inventory item." : "Modify the existing item properties.");
        subtitleLabel.setFont(FontLoader.getInter(12f, Font.PLAIN));
        subtitleLabel.setForeground(Theme.SLATE_MUTED);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Body Panel (Form Fields)
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        // Name field
        JLabel nameLabel = new JLabel("ITEM NAME");
        nameLabel.setFont(FontLoader.getInterSemiBold(11f));
        nameLabel.setForeground(Theme.FOREST_DEEP);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);
        nameField = new JTextField();
        Theme.styleTextField(nameField);
        nameField.setMaximumSize(new Dimension(380, 36));
        nameField.setAlignmentX(LEFT_ALIGNMENT);

        // Category field
        JLabel categoryLabel = new JLabel("CATEGORY");
        categoryLabel.setFont(FontLoader.getInterSemiBold(11f));
        categoryLabel.setForeground(Theme.FOREST_DEEP);
        categoryLabel.setAlignmentX(LEFT_ALIGNMENT);
        categoryField = new JTextField();
        Theme.styleTextField(categoryField);
        categoryField.setMaximumSize(new Dimension(380, 36));
        categoryField.setAlignmentX(LEFT_ALIGNMENT);

        // Grid for Quantity and Price (side-by-side)
        JPanel gridPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        gridPanel.setOpaque(false);
        gridPanel.setMaximumSize(new Dimension(380, 60));
        gridPanel.setAlignmentX(LEFT_ALIGNMENT);

        // Quantity field
        JPanel qtyPanel = new JPanel();
        qtyPanel.setOpaque(false);
        qtyPanel.setLayout(new BoxLayout(qtyPanel, BoxLayout.Y_AXIS));
        JLabel qtyLabel = new JLabel("QUANTITY");
        qtyLabel.setFont(FontLoader.getInterSemiBold(11f));
        qtyLabel.setForeground(Theme.FOREST_DEEP);
        qtyLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 100000, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setFont(FontLoader.getInter(14f, Font.PLAIN));
        quantitySpinner.setMaximumSize(new Dimension(180, 36));
        quantitySpinner.setAlignmentX(LEFT_ALIGNMENT);
        // Style spinner editor
        JComponent editor = quantitySpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(Color.WHITE);
            tf.setCaretColor(Theme.FOREST_DEEP);
            tf.setBorder(null);
        }
        quantitySpinner.setBorder(BorderFactory.createCompoundBorder(
            new Theme.RoundedBorder(4, Theme.BORDER_SUBTLE, 1),
            new EmptyBorder(2, 4, 2, 4)
        ));

        qtyPanel.add(qtyLabel);
        qtyPanel.add(Box.createVerticalStrut(6));
        qtyPanel.add(quantitySpinner);

        // Price field
        JPanel pricePanel = new JPanel();
        pricePanel.setOpaque(false);
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        JLabel priceLabel = new JLabel("PRICE (USD)");
        priceLabel.setFont(FontLoader.getInterSemiBold(11f));
        priceLabel.setForeground(Theme.FOREST_DEEP);
        priceLabel.setAlignmentX(LEFT_ALIGNMENT);
        priceField = new JTextField();
        Theme.styleTextField(priceField);
        priceField.setMaximumSize(new Dimension(180, 36));
        priceField.setAlignmentX(LEFT_ALIGNMENT);

        pricePanel.add(priceLabel);
        pricePanel.add(Box.createVerticalStrut(6));
        pricePanel.add(priceField);

        gridPanel.add(qtyPanel);
        gridPanel.add(pricePanel);

        // Assemble Form
        bodyPanel.add(nameLabel);
        bodyPanel.add(Box.createVerticalStrut(6));
        bodyPanel.add(nameField);
        bodyPanel.add(Box.createVerticalStrut(16));
        bodyPanel.add(categoryLabel);
        bodyPanel.add(Box.createVerticalStrut(6));
        bodyPanel.add(categoryField);
        bodyPanel.add(Box.createVerticalStrut(16));
        bodyPanel.add(gridPanel);
        bodyPanel.add(Box.createVerticalGlue());

        add(bodyPanel, BorderLayout.CENTER);

        // Footer Panel (Buttons)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footerPanel.setBackground(Theme.CREAM_SURFACE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE));
        
        JButton cancelButton = new JButton("Cancel");
        Theme.styleSecondaryButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());
        
        JButton saveButton = new JButton(item == null ? "Add Item" : "Save Changes");
        Theme.stylePrimaryButton(saveButton);
        saveButton.addActionListener(e -> handleSave());

        footerPanel.add(cancelButton);
        footerPanel.add(saveButton);
        add(footerPanel, BorderLayout.SOUTH);

        // Load existing item data if editing
        if (item != null) {
            nameField.setText(item.getName());
            categoryField.setText(item.getCategory());
            quantitySpinner.setValue(item.getQuantity());
            priceField.setText(String.valueOf(item.getPrice()));
        }
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        int quantity = (Integer) quantitySpinner.getValue();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number for price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determine status based on quantity
        String status = "In Stock";
        if (quantity == 0) {
            status = "Out of Stock";
        } else if (quantity < 10) {
            status = "Low Stock";
        }

        if (item == null) {
            item = new Item(0, name, category, quantity, price, status);
        } else {
            item.setName(name);
            item.setCategory(category);
            item.setQuantity(quantity);
            item.setPrice(price);
            item.setStatus(status);
        }

        succeeded = true;
        dispose();
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public Item getItem() {
        return item;
    }
}
