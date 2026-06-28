package com.inventory.components;

import com.inventory.models.Item;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ItemDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> categoryCombo;
    private JTextField skuField;
    private JSpinner quantitySpinner;
    private JSpinner reorderSpinner;
    private JTextField priceField;
    private JTextField originField;

    // Unit suffix labels
    private JLabel qtyUnitLabel;
    private JLabel reorderUnitLabel;
    
    private boolean succeeded = false;
    private Item item;

    public ItemDialog(Frame owner, String title, Item itemToEdit) {
        super(owner, title, true);
        this.item = itemToEdit;
        initializeUI();
    }

    private void initializeUI() {
        setSize(480, 680);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        getContentPane().setBackground(Theme.CREAM_BASE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(24, 24, 8, 24));
        
        JLabel titleLabel = new JLabel(item == null ? "Create item" : "Update item");
        titleLabel.setFont(FontLoader.getMerriweather(18f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        
        JLabel subtitleLabel = new JLabel(item == null ? "Add a new item to your inventory." : "Modify the existing item properties.");
        subtitleLabel.setFont(FontLoader.getInter(12f, Font.PLAIN));
        subtitleLabel.setForeground(Theme.SLATE_MUTED);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Body Panel (Form Fields)
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(12, 24, 12, 24));

        // Name field (Full Width)
        JLabel nameLabel = new JLabel("ITEM NAME");
        nameLabel.setFont(FontLoader.getInterSemiBold(10f));
        nameLabel.setForeground(Theme.FOREST_DEEP);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        nameField = new JTextField();
        Theme.styleTextField(nameField);
        nameField.setToolTipText("e.g. Organic Lavender Extract");
        nameField.setMaximumSize(new Dimension(432, 36));
        nameField.setAlignmentX(LEFT_ALIGNMENT);

        // Category dropdown (Full Width)
        JLabel categoryLabel = new JLabel("CATEGORY");
        categoryLabel.setFont(FontLoader.getInterSemiBold(10f));
        categoryLabel.setForeground(Theme.FOREST_DEEP);
        categoryLabel.setAlignmentX(LEFT_ALIGNMENT);

        categoryCombo = new JComboBox<>(new String[]{"Raw Items", "Processed Goods"});
        categoryCombo.setFont(FontLoader.getInter(13f, Font.PLAIN));
        categoryCombo.setBackground(Color.WHITE);
        categoryCombo.setMaximumSize(new Dimension(432, 36));
        categoryCombo.setAlignmentX(LEFT_ALIGNMENT);
        categoryCombo.setBorder(BorderFactory.createCompoundBorder(
            new Theme.RoundedBorder(4, Theme.BORDER_SUBTLE, 1),
            new EmptyBorder(2, 4, 2, 4)
        ));

        // Dynamically update units when category is changed
        categoryCombo.addActionListener(e -> updateUnitSuffixes());

        // SKU field (Full Width)
        JLabel skuLabel = new JLabel("SKU (STOCK KEEPING UNIT)");
        skuLabel.setFont(FontLoader.getInterSemiBold(10f));
        skuLabel.setForeground(Theme.FOREST_DEEP);
        skuLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        skuField = new JTextField();
        Theme.styleTextField(skuField);
        skuField.setMaximumSize(new Dimension(432, 36));
        skuField.setAlignmentX(LEFT_ALIGNMENT);

        // Row 3: Quantity and Reorder Point (Side-by-Side)
        JPanel row3Grid = new JPanel(new GridLayout(1, 2, 16, 0));
        row3Grid.setOpaque(false);
        row3Grid.setMaximumSize(new Dimension(432, 60));
        row3Grid.setAlignmentX(LEFT_ALIGNMENT);

        JPanel qtyPanel = new JPanel();
        qtyPanel.setOpaque(false);
        qtyPanel.setLayout(new BoxLayout(qtyPanel, BoxLayout.Y_AXIS));
        JLabel qtyLabel = new JLabel("QUANTITY");
        qtyLabel.setFont(FontLoader.getInterSemiBold(10f));
        qtyLabel.setForeground(Theme.FOREST_DEEP);
        qtyLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        JPanel qtySpinnerWrapper = new JPanel(new BorderLayout(4, 0));
        qtySpinnerWrapper.setOpaque(false);
        SpinnerNumberModel qtyModel = new SpinnerNumberModel(0, 0, 100000, 1);
        quantitySpinner = new JSpinner(qtyModel);
        styleSpinner(quantitySpinner);
        qtyUnitLabel = new JLabel("kg");
        qtyUnitLabel.setFont(FontLoader.getInter(11f, Font.PLAIN));
        qtyUnitLabel.setForeground(Theme.SLATE_MUTED);
        qtySpinnerWrapper.add(quantitySpinner, BorderLayout.CENTER);
        qtySpinnerWrapper.add(qtyUnitLabel, BorderLayout.EAST);
        qtySpinnerWrapper.setMaximumSize(new Dimension(200, 36));
        qtySpinnerWrapper.setAlignmentX(LEFT_ALIGNMENT);
        qtyPanel.add(qtyLabel);
        qtyPanel.add(Box.createVerticalStrut(4));
        qtyPanel.add(qtySpinnerWrapper);

        JPanel reorderPanel = new JPanel();
        reorderPanel.setOpaque(false);
        reorderPanel.setLayout(new BoxLayout(reorderPanel, BoxLayout.Y_AXIS));
        JLabel reorderLabel = new JLabel("REORDER POINT");
        reorderLabel.setFont(FontLoader.getInterSemiBold(10f));
        reorderLabel.setForeground(Theme.FOREST_DEEP);
        reorderLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        JPanel reorderSpinnerWrapper = new JPanel(new BorderLayout(4, 0));
        reorderSpinnerWrapper.setOpaque(false);
        SpinnerNumberModel reorderModel = new SpinnerNumberModel(10, 0, 100000, 1);
        reorderSpinner = new JSpinner(reorderModel);
        styleSpinner(reorderSpinner);
        reorderUnitLabel = new JLabel("kg");
        reorderUnitLabel.setFont(FontLoader.getInter(11f, Font.PLAIN));
        reorderUnitLabel.setForeground(Theme.SLATE_MUTED);
        reorderSpinnerWrapper.add(reorderSpinner, BorderLayout.CENTER);
        reorderSpinnerWrapper.add(reorderUnitLabel, BorderLayout.EAST);
        reorderSpinnerWrapper.setMaximumSize(new Dimension(200, 36));
        reorderSpinnerWrapper.setAlignmentX(LEFT_ALIGNMENT);
        reorderPanel.add(reorderLabel);
        reorderPanel.add(Box.createVerticalStrut(4));
        reorderPanel.add(reorderSpinnerWrapper);

        row3Grid.add(qtyPanel);
        row3Grid.add(reorderPanel);

        // Row 4: Price and Origin (Side-by-Side)
        JPanel row4Grid = new JPanel(new GridLayout(1, 2, 16, 0));
        row4Grid.setOpaque(false);
        row4Grid.setMaximumSize(new Dimension(432, 60));
        row4Grid.setAlignmentX(LEFT_ALIGNMENT);

        JPanel pricePanel = new JPanel();
        pricePanel.setOpaque(false);
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        JLabel priceLabel = new JLabel("PRICE (USD)");
        priceLabel.setFont(FontLoader.getInterSemiBold(10f));
        priceLabel.setForeground(Theme.FOREST_DEEP);
        priceLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        JPanel priceFieldWrapper = new JPanel(new BorderLayout(4, 0));
        priceFieldWrapper.setOpaque(false);
        JLabel dollarSign = new JLabel("$");
        dollarSign.setFont(FontLoader.getInter(13f, Font.PLAIN));
        dollarSign.setForeground(Theme.SLATE_MUTED);
        priceField = new JTextField();
        Theme.styleTextField(priceField);
        priceFieldWrapper.add(dollarSign, BorderLayout.WEST);
        priceFieldWrapper.add(priceField, BorderLayout.CENTER);
        priceFieldWrapper.setMaximumSize(new Dimension(200, 36));
        priceFieldWrapper.setAlignmentX(LEFT_ALIGNMENT);
        pricePanel.add(priceLabel);
        pricePanel.add(Box.createVerticalStrut(4));
        pricePanel.add(priceFieldWrapper);

        JPanel originPanel = new JPanel();
        originPanel.setOpaque(false);
        originPanel.setLayout(new BoxLayout(originPanel, BoxLayout.Y_AXIS));
        JLabel originLabel = new JLabel("COUNTRY OF ORIGIN");
        originLabel.setFont(FontLoader.getInterSemiBold(10f));
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

        // --- Aesthetic Supply-Chain Compliance Panel ---
        JPanel compliancePanel = new JPanel(new BorderLayout(12, 0));
        compliancePanel.setBackground(Theme.CREAM_SURFACE);
        compliancePanel.setBorder(BorderFactory.createCompoundBorder(
            new Theme.RoundedBorder(6, Theme.BORDER_SUBTLE, 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        compliancePanel.setMaximumSize(new Dimension(432, 64));
        compliancePanel.setAlignmentX(LEFT_ALIGNMENT);

        // A small 36x36 rounded image container loading botanical art
        String thumbUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAHqhT80QwBiOawV-B_lzTiEwW4XE5V6CMDvfMeJ-d0E-CIvTKVj0JeKNp8lcI6tSclrAfCOuR-ASbh-gQw0hb5ge49xmKsgR-uxBav6_QTx3-APaZcJMNOgIdrNVImdn-am5pnjJrRVNHh7fQXc7w7G8zi0TxsjSh42H_T-2xmR0gqMW_kiiumh0l2joPqpSBODX_nm239QW57ImIzdqe9MNfXaZIcbzSk5GRdgRyvH4cMZQDkpbdzlSUa7XpECpGB6GBUhrKno2uQ";
        JPanel imgThumb = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = ImageLoader.getOrLoadImage("dialog_compliance_thumb", thumbUrl, this, getWidth(), getHeight());
                if (icon != null) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
                }
            }
        };
        imgThumb.setPreferredSize(new Dimension(40, 40));
        imgThumb.setBorder(BorderFactory.createLineBorder(Theme.BORDER_SUBTLE, 1));
        imgThumb.setOpaque(false);

        JLabel quoteLabel = new JLabel("<html><body style='width: 320px;'><i>\"Each new item record ensures traceability and batch quality compliance across our organic supply chain.\"</i></body></html>");
        quoteLabel.setFont(FontLoader.getInter(10f, Font.PLAIN));
        quoteLabel.setForeground(Theme.SLATE_MUTED);

        compliancePanel.add(imgThumb, BorderLayout.WEST);
        compliancePanel.add(quoteLabel, BorderLayout.CENTER);

        // Assemble Fields to Body
        bodyPanel.add(nameLabel);
        bodyPanel.add(Box.createVerticalStrut(4));
        bodyPanel.add(nameField);
        bodyPanel.add(Box.createVerticalStrut(12));
        bodyPanel.add(categoryLabel);
        bodyPanel.add(Box.createVerticalStrut(4));
        bodyPanel.add(categoryCombo);
        bodyPanel.add(Box.createVerticalStrut(12));
        bodyPanel.add(skuLabel);
        bodyPanel.add(Box.createVerticalStrut(4));
        bodyPanel.add(skuField);
        bodyPanel.add(Box.createVerticalStrut(12));
        bodyPanel.add(row3Grid);
        bodyPanel.add(Box.createVerticalStrut(12));
        bodyPanel.add(row4Grid);
        bodyPanel.add(Box.createVerticalStrut(16));
        bodyPanel.add(compliancePanel);
        bodyPanel.add(Box.createVerticalGlue());

        add(bodyPanel, BorderLayout.CENTER);

        // Footer Panel (Save / Cancel Buttons)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 16));
        footerPanel.setBackground(Theme.CREAM_SURFACE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_SUBTLE));
        
        JButton cancelButton = new JButton("Cancel");
        Theme.styleSecondaryButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());
        
        JButton saveButton = new JButton(item == null ? "Create" : "Save Changes");
        Theme.stylePrimaryButton(saveButton);
        saveButton.addActionListener(e -> handleSave());

        footerPanel.add(cancelButton);
        footerPanel.add(saveButton);
        add(footerPanel, BorderLayout.SOUTH);

        // Load existing item data if editing
        if (item != null) {
            nameField.setText(item.getName());
            categoryCombo.setSelectedItem("Raw Items".equalsIgnoreCase(item.getCategory()) ? "Raw Items" : "Processed Goods");
            skuField.setText(item.getSku());
            quantitySpinner.setValue(item.getQuantity());
            reorderSpinner.setValue(item.getReorderPoint());
            priceField.setText(String.valueOf(item.getPrice()));
            originField.setText(item.getOrigin());
        }
        
        updateUnitSuffixes();
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(FontLoader.getInter(14f, Font.PLAIN));
        spinner.setPreferredSize(new Dimension(140, 36));
        spinner.setMaximumSize(new Dimension(140, 36));
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

    private void updateUnitSuffixes() {
        String selected = (String) categoryCombo.getSelectedItem();
        if ("Raw Items".equalsIgnoreCase(selected)) {
            qtyUnitLabel.setText("kg");
            reorderUnitLabel.setText("kg");
        } else {
            qtyUnitLabel.setText("units");
            reorderUnitLabel.setText("units");
        }
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        String sku = skuField.getText().trim();
        int quantity = (Integer) quantitySpinner.getValue();
        int reorderPoint = (Integer) reorderSpinner.getValue();
        String priceText = priceField.getText().trim();
        String origin = originField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields (Name, Price).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive decimal number for price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
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
