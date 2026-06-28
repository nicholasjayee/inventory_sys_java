package com.inventory.pages.dashboard.components;

import com.inventory.components.CardPanel;
import com.inventory.components.FontLoader;
import com.inventory.components.ImageLoader;
import com.inventory.components.Theme;
import com.inventory.models.Item;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductBentoCard extends CardPanel {
    private static final Map<String, String> IMAGE_URLS = new HashMap<>();
    static {
        IMAGE_URLS.put("shea butter", "assets/images/shea_butter.jpg");
        IMAGE_URLS.put("lavender", "assets/images/lavender.jpg");
        IMAGE_URLS.put("rosehip", "assets/images/rosehip.jpg");
        IMAGE_URLS.put("argan", "assets/images/argan.jpg");
        IMAGE_URLS.put("kelp", "assets/images/kelp.jpg");
        IMAGE_URLS.put("beeswax", "assets/images/beeswax.jpg");
        IMAGE_URLS.put("serum", "assets/images/serum.jpg");
        IMAGE_URLS.put("cream", "assets/images/cream.jpg");
        IMAGE_URLS.put("macadamia", "assets/images/macadamia.jpg");
        IMAGE_URLS.put("eucalyptus", "assets/images/eucalyptus.jpg");
        IMAGE_URLS.put("baobab", "assets/images/baobab.jpg");
        IMAGE_URLS.put("moringa", "assets/images/moringa.jpg");
    }

    public ProductBentoCard(Item item, Consumer<Item> onEdit, Consumer<Item> onDeleteConfirm) {
        super(12);
        setLayout(new BorderLayout());
        setBackground(Theme.CREAM_SURFACE);
        setBorder(null);

        // Match image based on name matching
        String imageUrl = "";
        String lowercaseName = item.getName().toLowerCase();
        for (Map.Entry<String, String> entry : IMAGE_URLS.entrySet()) {
            if (lowercaseName.contains(entry.getKey())) {
                imageUrl = entry.getValue();
                break;
            }
        }

        final String finalUrl = imageUrl;
        boolean isRaw = item.getCategory().toLowerCase().contains("raw") || item.getCategory().toLowerCase().contains("ingredient");

        // Image Section with overlaid badge
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
                    // Fallback gradient
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(isRaw ? Theme.FOREST_LEAF : Theme.SLATE_MUTED);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }

                // Overlay Badge in Top-Right
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                String badgeText = isRaw ? "Raw" : "Processed";
                Color bg = isRaw ? Theme.FOREST_DEEP : Theme.FOREST_LEAF;
                
                g2.setColor(bg);
                int badgeW = 70;
                int badgeH = 20;
                int badgeX = getWidth() - badgeW - 12;
                int badgeY = 12;
                g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 4, 4);

                g2.setColor(Color.WHITE);
                g2.setFont(FontLoader.getInterSemiBold(9f));
                FontMetrics fm = g2.getFontMetrics();
                int textX = badgeX + (badgeW - fm.stringWidth(badgeText)) / 2;
                int textY = badgeY + (badgeH - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(badgeText, textX, textY);
                g2.dispose();
            }
        };
        imagePanel.setPreferredSize(new Dimension(320, 140));

        // Details Panel
        JPanel bodyPanel = new JPanel();
        bodyPanel.setOpaque(false);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(FontLoader.getMerriweather(16f, Font.BOLD));
        nameLabel.setForeground(Theme.FOREST_DEEP);
        nameLabel.setBorder(new EmptyBorder(0, 0, 12, 0));

        // Separator
        JPanel line = new JPanel();
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(100, 1));
        line.setBackground(Theme.BORDER_SUBTLE);

        // Stats
        JPanel statsRow = new JPanel(new GridLayout(1, 2, 8, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(new EmptyBorder(12, 0, 12, 0));

        JPanel qtyPanel = new JPanel();
        qtyPanel.setOpaque(false);
        qtyPanel.setLayout(new BoxLayout(qtyPanel, BoxLayout.Y_AXIS));
        JLabel qtyTitle = new JLabel("QUANTITY");
        qtyTitle.setFont(FontLoader.getInterSemiBold(9f));
        qtyTitle.setForeground(Theme.SLATE_MUTED);
        JLabel qtyVal = new JLabel(item.getQuantity() + (isRaw ? " kg" : " Units"));
        qtyVal.setFont(FontLoader.getInterSemiBold(13f));
        qtyVal.setForeground(Theme.FOREST_DEEP);
        qtyPanel.add(qtyTitle);
        qtyPanel.add(qtyVal);

        JPanel pricePanel = new JPanel();
        pricePanel.setOpaque(false);
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        JLabel priceTitle = new JLabel("PRICE");
        priceTitle.setFont(FontLoader.getInterSemiBold(9f));
        priceTitle.setForeground(Theme.SLATE_MUTED);
        JLabel priceVal = new JLabel(com.inventory.app.AppConfig.getCurrencyFormat().format(item.getPrice()) + (isRaw ? " / kg" : " / u"));
        priceVal.setFont(FontLoader.getInterSemiBold(13f));
        priceVal.setForeground(Theme.FOREST_DEEP);
        pricePanel.add(priceTitle);
        pricePanel.add(priceVal);

        statsRow.add(qtyPanel);
        statsRow.add(pricePanel);

        // Footer Actions (Edit / Delete buttons with high-res PNG icons)
        JPanel footerRow = new JPanel(new BorderLayout());
        footerRow.setOpaque(false);
        footerRow.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton editBtn = new JButton("EDIT");
        Theme.styleSecondaryButton(editBtn);
        editBtn.setPreferredSize(new Dimension(76, 28));
        editBtn.setFont(FontLoader.getInterSemiBold(9f));
        editBtn.addActionListener(e -> onEdit.accept(item));

        // Load and scale pencil edit icon
        try {
            ImageIcon editIcon = new ImageIcon("assets/icons/edit.png");
            Image scaledEdit = editIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            editBtn.setIcon(new ImageIcon(scaledEdit));
            editBtn.setIconTextGap(6);
        } catch (Exception e) {
            System.err.println("Failed to load edit icon");
        }

        JButton removeBtn = new JButton("REMOVE");
        Theme.styleSecondaryButton(removeBtn);
        removeBtn.setForeground(Theme.ERROR_FG);
        removeBtn.setPreferredSize(new Dimension(92, 28));
        removeBtn.setFont(FontLoader.getInterSemiBold(9f));
        removeBtn.addActionListener(e -> onDeleteConfirm.accept(item));

        // Load and scale trash delete icon
        try {
            ImageIcon deleteIcon = new ImageIcon("assets/icons/delete.png");
            Image scaledDelete = deleteIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            removeBtn.setIcon(new ImageIcon(scaledDelete));
            removeBtn.setIconTextGap(6);
        } catch (Exception e) {
            System.err.println("Failed to load delete icon");
        }

        footerRow.add(editBtn, BorderLayout.WEST);
        footerRow.add(removeBtn, BorderLayout.EAST);

        bodyPanel.add(nameLabel);
        bodyPanel.add(line);
        bodyPanel.add(statsRow);
        bodyPanel.add(footerRow);

        add(imagePanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
    }
}
