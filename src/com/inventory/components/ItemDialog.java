package com.inventory.components;

import com.inventory.models.Item;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ItemDialog extends JDialog {
    private JTextField nameField;
    private JTextField categoryField;
    private JTextField skuField;
    private JSpinner quantitySpinner;
    private JSpinner reorderSpinner;
    private JTextField priceField;
    private JTextField originField;
    
    private boolean succeeded = false;
    private Item item;

    public ItemDialog(Frame owner, String title, Item itemToEdit) {
        super(owner, title, true);
        this.item = itemToEdit;
        initializeUI();
    }

    private void initializeUI() {
        setSize(460, 580);
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
        
        JLabel subtitleLabel = new JLabel(item == null ? "Fill in the details to add a new botanical item." : "Modify the existing item properties.");
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

        // Name field (Full Width)
        JLabel nameLabel = new JLabel("ITEM NAME");
        nameLabel.setFont(FontLoader.getInterSemiBold(11f));
        nameLabel.setForeground(Theme.FOREST_DEEP);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);
        nameField = new JTextField();
        Theme.styleTextField(nameField);
        nameField.setMaximumSize(new Dimension(410, 36));
        nameField.setAlignmentX(LEFT_ALIGNMENT);

        // Row 2: Category and SKU (Side-by-Side)
        JPanel row2Grid = new JPanel(new GridLayout(1, 2, 16, 0));
        row2Grid.setOpaque(false);
        row2Grid.setMaximumSize(new Dimension(410, 60));
        row2Grid.setAlignmentX(LEFT_ALIGNMENT);

        JPanel catPanel = new JPanel();
        catPanel.setOpaque(false);
        catPanel.setLayout(new BoxLayout(catPanel, BoxLayout.Y_AXIS));
        JLabel categoryLabel = new JLabel("CATEGORY");
        categoryLabel.setFont(FontLoader.getInterSemiBold(11f));
        categoryLabel.setForeground(Theme.FOREST_DEEP);
        categoryLabel.setAlignmentX(LEFT_ALIGNMENT);
        categoryField = new JTextField();
        Theme.styleTextField(categoryField);
        categoryField.setMaximumSize(new Dimension(200, 36));
        categoryField.setAlignmentX(LEFT_ALIGNMENT);
        catPanel.add(categoryLabel);
        catPanel.add(Box.createVerticalStrut(4));
        catPanel.add(categoryField);

        JPanel skuPanel = new JPanel();
        skuPanel.setOpaque(false);
        skuPanel.setLayout(new BoxLayout(skuPanel, BoxLayout.Y_AXIS));
        JLabel skuLabel = new JLabel("SKU (STOCK KEEPING UNIT)");
        skuLabel.setFont(FontLoader.getInterSemiBold(11f));
        skuLabel.setForeground(Theme.FOREST_DEEP);
        skuLabel.setAlignmentX(LEFT_ALIGNMENT);
        skuField = new JTextField();
        Theme.styleTextField(skuField);
        skuField.setMaximumSize(new Dimension(200, 36));
        skuField.setAlignmentX(LEFT_ALIGNMENT);
        skuPanel.add(skuLabel);
        skuPanel.add(Box.createVerticalStrut(4));
        skuPanel.add(skuField);

        row2Grid.add(catPanel);
        row2Grid.add(skuPanel);

        // Row 3: Quantity and Reorder Point (Side-by-Side)
        JPanel row3Grid = new JPanel(new GridLayout(1, 2, 16, 0));
        row3Grid.setOpaque(false);
        row3Grid.setMaximumSize(new Dimension(410, 60));
        row3Grid.setAlignmentX(LEFT_ALIGNMENT);

        JPanel qtyPanel = new JPanel();
        qtyPanel.setOpaque(false);
        qtyPanel.setLayout(new BoxLayout(qtyPanel, BoxLayout.Y_AXIS));
        JLabel qtyLabel = new JLabel("CURRENT QUANTITY");
        qtyLabel.setFont(FontLoader.getInterSemiBold(11f));
        qtyLabel.setForeground(Theme.FOREST_DEEP);
        qtyLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        SpinnerNumberModel qtyModel = new SpinnerNumberModel(0, 0, 100000, 1);
        quantitySpinner = new JSpinner(qtyModel);
        styleSpinner(quantitySpinner);
        qtyPanel.add(qtyLabel);
        qtyPanel.add(Box.createVerticalStrut(4));
        qtyPanel.add(quantitySpinner);

        JPanel reorderPanel = new JPanel();
        reorderPanel.setOpaque(false);
        reorderPanel.setLayout(new BoxLayout(reorderPanel, BoxLayout.Y_AXIS));
        JLabel reorderLabel = new JLabel("REORDER POINT");
        reorderLabel.setFont(FontLoader.getInterSemiBold(11f));
        reorderLabel.setForeground(Theme.FOREST_DEEP);
        reorderLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        SpinnerNumberModel reorderModel = new SpinnerNumberModel(10, 0, 100000, 1);
        reorderSpinner = new JSpinner(reorderModel);
        styleSpinner(reorderSpinner);
        reorderPanel.add(reorderLabel);
        reorderPanel.add(Box.createVerticalStrut(4));
        reorderPanel.add(reorderSpinner);

        row3Grid.add(qtyPanel);
        row3Grid.add(reorderPanel);

        // Row 4: Price and Origin (Side-by-Side)
        JPanel row4Grid = new JPanel(new GridLayout(1, 2, 16, 0));
        row4Grid.setOpaque(false);
        row4Grid.setMaximumSize(new Dimension(410, 60));
        row4Grid.setAlignmentX(LEFT_ALIGNMENT);

        JPanel pricePanel = new JPanel();
        pricePanel.setOpaque(false);
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        JLabel priceLabel = new JLabel("UNIT PRICE (USD)");
        priceLabel.setFont(FontLoader.getInterSemiBold(11f));
        priceLabel.setForeground(Theme.FOREST_DEEP);
        priceLabel.setAlignmentX(LEFT_ALIGNMENT);
        priceField = new JTextField();
        Theme.styleTextField(priceField);
        priceField.setMaximumSize(new Dimension(200, 36));
        priceField.setAlignmentX(LEFT_ALIGNMENT);
        pricePanel.add(priceLabel);
        pricePanel.add(Box.createVerticalStrut(4));
        pricePanel.add(priceField);

        JPanel originPanel = new JPanel();
        originPanel.setOpaque(false);
        originPanel.setLayout(new BoxLayout(originPanel, BoxLayout.Y_AXIS));
        JLabel originLabel = new JLabel("COUNTRY OF ORIGIN");
        originLabel.setFont(FontLoader.getInterSemiBold(11f));
        originLabel.setForeground(Theme.FOREST_DEEP);
        originLabel.setAlignmentX(LEFT_ALIGNMENT);
        originField = new JTextField();
        Theme.styleTextField(originField);
        originField.setMaximumSize(new Dimension(200, 36));
        originField.setAlignmentX(LEFT_ALIGNMENT);
        originPanel.add(originLabel);
        originPanel.add(Box.createVerticalStrut(4));
        originPanel.add(originField);

        row4Grid.add(pricePanel);
        row4Grid.add(originPanel);

        // Assemble Fields to Body
        bodyPanel.add(nameLabel);
        bodyPanel.add(Box.createVerticalStrut(4));
        bodyPanel.add(nameField);
        bodyPanel.add(Box.createVerticalStrut(12));
        bodyPanel.add(row2Grid);
        bodyPanel.add(Box.createVerticalStrut(12));
        bodyPanel.add(row3Grid);
        bodyPanel.add(Box.createVerticalStrut(12));
        bodyPanel.add(row4Grid);
        bodyPanel.add(Box.createVerticalGlue());

        add(bodyPanel, BorderLayout.CENTER);

        // Footer Panel (Save / Cancel Buttons)
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
            skuField.setText(item.getSku());
            quantitySpinner.setValue(item.getQuantity());
            reorderSpinner.setValue(item.getReorderPoint());
            priceField.setText(String.valueOf(item.getPrice()));
            originField.setText(item.getOrigin());
        }
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(FontLoader.getInter(14f, Font.PLAIN));
        spinner.setMaximumSize(new Dimension(200, 36));
        spinner.setAlignmentX(LEFT_ALIGNMENT);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(Color.WHITE);
            tf.setCaretColor(Theme.FOREST_DEEP);
            tf.setBorder(null);
        }
        spinner.setBorder(BorderFactory.createCompoundBorder(
            new Theme.RoundedBorder(4, Theme.BORDER_SUBTLE, 1),
            new EmptyBorder(2, 4, 2, 4)
        ));
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String sku = skuField.getText().trim();
        int quantity = (Integer) quantitySpinner.getValue();
        int reorderPoint = (Integer) reorderSpinner.getValue();
        String priceText = priceField.getText().trim();
        String origin = originField.getText().trim();

        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields (Name, Category, Price).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive decimal number for unit price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determine stock status based on quantity vs reorder point
        String status = "In Stock";
        if (quantity == 0) {
            status = "Out of Stock";
        } else if (quantity < reorderPoint) {
            status = "Low Stock";
        }

        if (item == null) {
            String userUuid = "33331b26-f716-4df6-a2cb-13f1c20f41e7"; // Default fallback
            if (com.inventory.state.AppState.getInstance().isLoggedIn()) {
                userUuid = com.inventory.state.AppState.getInstance().getCurrentUser().getUuid();
            }
            item = new Item(userUuid, name, category, sku, reorderPoint, origin, quantity, price, status);
        } else {
            item.setName(name);
            item.setCategory(category);
            item.setSku(sku);
            item.setReorderPoint(reorderPoint);
            item.setOrigin(origin);
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
