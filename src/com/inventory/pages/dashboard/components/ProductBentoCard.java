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
        IMAGE_URLS.put("shea butter", "https://lh3.googleusercontent.com/aida-public/AB6AXuCHBRoYKc0SBAInofThMZBrgxb3UuDv6K95qV0D1viI6QHybia87hjg4rEQE1-BgiKpZbQko0CiTlCw_A6gzGFfB4L-zVO6mmNURRvMV3DaNC3wTLQhUG4HBrOJvBzcSjHZD4b1V9dc_RdSgsNRBldu7Ex2gEpTOMk6dT2L7YE-TLtdJDrEXF_9PJ2DqWdRLhopM3CU57mVopw6bWP105s6oh5pCEirmmRmsAps_7lw9FQVSWvBW3Kdcmqq8ScOZ-P7J-l1Gg7icn0t");
        IMAGE_URLS.put("lavender", "https://lh3.googleusercontent.com/aida-public/AB6AXuAs-hZR63qnNR0cpgMedujl0AXcYB4iZX3cvXS2GtPbv2leWUVpUDFmnwKV-7VY1pQpBj8KLv73WX80qT1ewl1rmzwI7_8zUc53-bGHnvJ7TNgeXoTw-KU7yt6HOS22AWmlvPkeHm72Lhyk9NPu9mx4Or9pfTmR2UmJN5P8Au1MRJPZWzt1sYgFj4hE8chadoKOODcLySU9PB7mfF-DW24mmg39U9AoqVZvFOG6qsBXklj6T4npfA8qZjj90KltuJi4P8efFarbwX9b");
        IMAGE_URLS.put("rosehip", "https://lh3.googleusercontent.com/aida-public/AB6AXuD5wkhJQP4AqhFADaZgNyUugQ6MQUy7rAYxEG0j-z5yUvyG8DwwxPQq33ZjkIOUchsalKUbth2UZ1gWxF5SlI5wANd8GPIDg-tzaJv9odrxKfGnS35-lLGnuT9Z-fNdKyEeDzvspRhhnt1DSHCeFSDtRe6Eg4n2m7ylusTBwPxom5Wqvql7GEwGpRCglWeD-BXghwEMXQa821Jx1-nX-vpPbfZW5NIXj3fEtatCVuRQXxKsBUOEvxsge6HDycjfsuWMSqR9n_vfG80O");
        IMAGE_URLS.put("argan", "https://lh3.googleusercontent.com/aida-public/AB6AXuC4C357I8sYtX7Qb-AyNdClKIy0yEefjgk5_xVHD6OdtJSH19MYZGSGWQ-fmfEwMnS57Fs2TX-90GxEqSrW9jib3BE3gAFlDAOYCcuthZdcJoyH9zEZlp3S8E2dsZjzQX3xUb5NAWGxhlrGq2Ehm1At9gmOaKT0pvWSVwKDIKtMwu-qt5_p4e4ZoxqDiZYlUZGZtlwmPVOUglCZAStDT2W3-dO8KZSoztPsH8gU1rF26QiopzDadN-OQ4t6ZY57qUFcRan8H67Os5eD");
        IMAGE_URLS.put("kelp", "https://lh3.googleusercontent.com/aida-public/AB6AXuBqzkFG1Qz30wB8eMX539RardS9h17zRfZY-BKz0UUoR5TkpXhAkLVg9CNWZXWyL5aos3_P73OHYt_WrIXj8WzrOO1AtK4VSgZuL5Yv5ZslWO1GISqKl99GTqtJ0AZlLROAtwO0rpLjxcLeHxQA45lRnZIlh1P1sU3_udyjTsFROY4mh-wdDMv1cibdDhHJe656RVpMBvw7UzvSnKTyQlKBIwQxhiVOG2gwZnT-czcx1rJ7lhaJCVmYHYzUrd5T0wWkBY4cmXj30E8E");
        IMAGE_URLS.put("beeswax", "https://lh3.googleusercontent.com/aida-public/AB6AXuDks8tbSGxskWGYmmd0eIJHdh6_HZbGBgVVDax1ns93fZfSslpLqS4AOUkwmuFHSDcK2p_2mzdxQbyWO5kn_2tmTOAlRn0VpGdSOb-FBqgdkDVFvZ1B8a39DLfzxUZ1_Rx1OsJnDeh47tE8YNrws9qANYxmrxK2BFima-E0AnCvnRelSk0nCdiPryAOhmq-I66Y9OUxxRcOAiBGQnb8ilMxHAFuKQ1taALhAlWS4s-H_kl38FxFqFlPhlVBhR6MVHxGlF5jyYGDlfMA");
        IMAGE_URLS.put("serum", "https://lh3.googleusercontent.com/aida-public/AB6AXuAsbOwKjbPU6hRJ-FDfvcQAtCRN25zeueRafHO7MyFvwRhGHdOrUZDLFHZjdVUZLkYQnlBdXcgK6C78r1ObSk-pfL3RVBJETLThmq89mHGFnCIKILHK0mgn673mDAZaz-JQ6TKGOthdkjMjF78lpNxXhsU_AiDsjI6YaoNaOhOpikPVfGiEGrTbG0_oXUfxco7UcnII87oni53ObJ_-iWMvA94lEym32SlM_ztuHxm2dJkLtY-10k9BRNf-dmgWHo7mPRl1JOHtu3RQ");
        IMAGE_URLS.put("cream", "https://lh3.googleusercontent.com/aida-public/AB6AXuAH2W0HmgKCdcgjZ3TBguiGy2QJThteqAZ5bMWToTC-BkKMjC7cepybTWUR2MHStw0A2Q3MIAWM8OJ5oWybKDcegL38zj4T19_-RK2BDxFBjTRSso60_31xqElN-Ovc_g_gPP6f3drS3LBRko2ef5zVTBPyPpMS8qTLbU9Lkj2zwFUi02HrsBKTWmR1WzLy3udgqw-HVixlf6YZYWUIpISK7l3WStni1es4hfw8k244GuumvgRoaf-ipN2glkm98576XD_pwPmPTd09");
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
        JLabel priceVal = new JLabel(NumberFormat.getCurrencyInstance(Locale.US).format(item.getPrice()) + (isRaw ? " / kg" : " / u"));
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
