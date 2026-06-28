package com.inventory.pages.login;

import com.inventory.components.CardPanel;
import com.inventory.components.FontLoader;
import com.inventory.components.Theme;
import com.inventory.models.User;
import com.inventory.router.Page;
import com.inventory.router.Router;
import com.inventory.services.UserService;
import com.inventory.state.AppState;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPage extends Page {
    private final Router router;
    private final UserService userService;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JButton loginButton;

    public LoginPage(Router router) {
        this.router = router;
        this.userService = new UserService();
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Theme.CREAM_BASE);
        setLayout(new GridBagLayout());

        // Center card container
        CardPanel card = new CardPanel(32);
        card.setPreferredSize(new Dimension(420, 500));
        card.setCornerRadius(12);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Logo / Title
        JLabel brandLabel = new JLabel("Aramweer");
        brandLabel.setFont(FontLoader.getMerriweather(32f, java.awt.Font.BOLD));
        brandLabel.setForeground(Theme.FOREST_DEEP);
        brandLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subBrandLabel = new JLabel("ORGANIC SKIN CARE");
        subBrandLabel.setFont(FontLoader.getInter(11f, java.awt.Font.PLAIN));
        subBrandLabel.setForeground(Theme.SLATE_MUTED);
        subBrandLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Botanical Logistics");
        titleLabel.setFont(FontLoader.getInterSemiBold(18f));
        titleLabel.setForeground(Theme.SLATE_TEXT);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("Welcome back. Please enter your credentials.");
        descLabel.setFont(FontLoader.getInter(13f, java.awt.Font.PLAIN));
        descLabel.setForeground(Theme.SLATE_MUTED);
        descLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Form Fields
        JLabel usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(FontLoader.getInterSemiBold(11f));
        usernameLabel.setForeground(Theme.FOREST_DEEP);
        usernameLabel.setAlignmentX(LEFT_ALIGNMENT);

        usernameField = new JTextField();
        Theme.styleTextField(usernameField);
        usernameField.setMaximumSize(new Dimension(360, 40));
        usernameField.setPreferredSize(new Dimension(360, 40));
        usernameField.setAlignmentX(LEFT_ALIGNMENT);

        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(FontLoader.getInterSemiBold(11f));
        passwordLabel.setForeground(Theme.FOREST_DEEP);
        passwordLabel.setAlignmentX(LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        // Style password field similarly to text fields
        passwordField.setFont(FontLoader.getInter(14f, java.awt.Font.PLAIN));
        passwordField.setBackground(Theme.WHITE);
        passwordField.setForeground(Theme.SLATE_TEXT);
        passwordField.setCaretColor(Theme.FOREST_DEEP);
        passwordField.setBorder(new Theme.RoundedBorder(4, Theme.BORDER_SUBTLE, 1));
        passwordField.setMaximumSize(new Dimension(360, 40));
        passwordField.setPreferredSize(new Dimension(360, 40));
        passwordField.setAlignmentX(LEFT_ALIGNMENT);
        passwordField.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                passwordField.setBorder(new Theme.RoundedBorder(4, Theme.FOREST_DEEP, 1));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                passwordField.setBorder(new Theme.RoundedBorder(4, Theme.BORDER_SUBTLE, 1));
            }
        });

        // Error message label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(FontLoader.getInter(12f, java.awt.Font.PLAIN));
        errorLabel.setForeground(Theme.ERROR_FG);
        errorLabel.setAlignmentX(CENTER_ALIGNMENT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Login Button
        loginButton = new JButton("Sign In");
        Theme.stylePrimaryButton(loginButton);
        loginButton.setMaximumSize(new Dimension(360, 44));
        loginButton.setPreferredSize(new Dimension(360, 44));
        loginButton.setAlignmentX(LEFT_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());

        // Assemble card
        card.add(Box.createVerticalStrut(20));
        card.add(brandLabel);
        card.add(subBrandLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(30));

        // Form alignment panel
        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(16));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(16));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(16));
        card.add(loginButton);
        card.add(Box.createVerticalGlue());

        // Add card to center grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        add(card, gbc);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        loginButton.setEnabled(false);
        errorLabel.setText("Authenticating...");

        // Run authentication in a background worker thread to keep UI responsive
        new Thread(() -> {
            User user = userService.authenticate(username, password);
            javax.swing.SwingUtilities.invokeLater(() -> {
                loginButton.setEnabled(true);
                if (user != null) {
                    errorLabel.setText(" ");
                    AppState.getInstance().setCurrentUser(user);
                    System.out.println("Login successful for user: " + user.getDisplayName());
                    router.navigate("/dashboard");
                } else {
                    errorLabel.setText("Invalid username or password.");
                    passwordField.setText("");
                }
            });
        }).start();
    }

    @Override
    public void onPageLoad() {
        // Clear previous state on page load
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText(" ");
        usernameField.requestFocusInWindow();
    }
}
