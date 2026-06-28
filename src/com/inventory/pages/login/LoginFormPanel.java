package com.inventory.pages.login;

import com.inventory.components.FontLoader;
import com.inventory.components.Theme;
import com.inventory.components.VectorIcons;
import com.inventory.models.User;
import com.inventory.router.Router;
import com.inventory.services.UserService;
import com.inventory.state.AppState;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class LoginFormPanel extends JPanel {
    private final Router router;
    private final UserService userService;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheckbox;
    private JLabel errorLabel;
    private JButton signInButton;

    public LoginFormPanel(Router router) {
        this.router = router;
        this.userService = new UserService();
        initializeUI();
    }

    private void initializeUI() {
        setBackground(Theme.CREAM_SURFACE);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(500, 640));

        // Form Container with Max Width Constraint
        JPanel formContainer = new JPanel();
        formContainer.setOpaque(false);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(new EmptyBorder(0, 48, 0, 48));

        // --- Header ---
        JLabel titleLabel = new JLabel("Welcome back");
        titleLabel.setFont(FontLoader.getMerriweather(28f, Font.BOLD));
        titleLabel.setForeground(Theme.FOREST_DEEP);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("<html>Please enter your details to access the inventory management suite.</html>");
        subtitleLabel.setFont(FontLoader.getInter(13f, Font.PLAIN));
        subtitleLabel.setForeground(Theme.SLATE_MUTED);
        subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);
        subtitleLabel.setMaximumSize(new Dimension(380, 50));

        // --- Form Fields ---
        JLabel usernameLabel = new JLabel("USER NAME");
        usernameLabel.setFont(FontLoader.getInterSemiBold(11f));
        usernameLabel.setForeground(Theme.FOREST_DEEP);
        usernameLabel.setAlignmentX(LEFT_ALIGNMENT);

        usernameField = new JTextField();
        Theme.styleTextField(usernameField);
        usernameField.setMaximumSize(new Dimension(380, 40));
        usernameField.setPreferredSize(new Dimension(380, 40));
        usernameField.setAlignmentX(LEFT_ALIGNMENT);
        // FlatLaf client properties for placeholders and icons
        usernameField.putClientProperty("JTextField.placeholderText", "Enter your username");
        usernameField.putClientProperty("JTextField.leadingIcon", new VectorIcons.PersonIcon(16));

        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(FontLoader.getInterSemiBold(11f));
        passwordLabel.setForeground(Theme.FOREST_DEEP);
        passwordLabel.setAlignmentX(LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(FontLoader.getInter(14f, Font.PLAIN));
        passwordField.setBackground(Theme.WHITE);
        passwordField.setForeground(Theme.SLATE_TEXT);
        passwordField.setCaretColor(Theme.FOREST_DEEP);
        passwordField.setBorder(new Theme.RoundedBorder(4, Theme.BORDER_SUBTLE, 1));
        passwordField.setMaximumSize(new Dimension(380, 40));
        passwordField.setPreferredSize(new Dimension(380, 40));
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
        // FlatLaf client properties for placeholders, icons, and reveal toggle
        passwordField.putClientProperty("JTextField.placeholderText", "••••••••");
        passwordField.putClientProperty("JTextField.leadingIcon", new VectorIcons.LockIcon(16));
        passwordField.putClientProperty("JPasswordField.showRevealButton", true);

        // --- Remember Me ---
        rememberCheckbox = new JCheckBox("Remember me for 30 days");
        rememberCheckbox.setFont(FontLoader.getInter(13f, Font.PLAIN));
        rememberCheckbox.setForeground(Theme.SLATE_TEXT);
        rememberCheckbox.setOpaque(false);
        rememberCheckbox.setFocusPainted(false);
        rememberCheckbox.setAlignmentX(LEFT_ALIGNMENT);

        // --- Error Label ---
        errorLabel = new JLabel(" ");
        errorLabel.setFont(FontLoader.getInter(12f, Font.PLAIN));
        errorLabel.setForeground(Theme.ERROR_FG);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        // --- Sign In Button ---
        signInButton = new JButton("Sign In");
        Theme.stylePrimaryButton(signInButton);
        signInButton.setMaximumSize(new Dimension(380, 46));
        signInButton.setPreferredSize(new Dimension(380, 46));
        signInButton.setAlignmentX(LEFT_ALIGNMENT);
        signInButton.addActionListener(e -> triggerLogin());

        // Bind enter key on username & password fields to login action
        java.awt.event.ActionListener enterAction = e -> triggerLogin();
        usernameField.addActionListener(enterAction);
        passwordField.addActionListener(enterAction);

        // --- Footer ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(LEFT_ALIGNMENT);
        footerPanel.setMaximumSize(new Dimension(380, 30));

        JLabel footerText = new JLabel("Don't have an account?");
        footerText.setFont(FontLoader.getInter(13f, Font.PLAIN));
        footerText.setForeground(Theme.SLATE_MUTED);

        JButton contactAdminBtn = new JButton("Contact Administrator");
        contactAdminBtn.setFont(FontLoader.getInterSemiBold(13f));
        contactAdminBtn.setForeground(Theme.FOREST_DEEP);
        contactAdminBtn.setOpaque(false);
        contactAdminBtn.setContentAreaFilled(false);
        contactAdminBtn.setBorder(null);
        contactAdminBtn.setFocusPainted(false);
        contactAdminBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        contactAdminBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Please contact the Operations Manager or IT Administrator\nto create a new account using the user creation script:\n\n./tools/create_user.sh", 
                "Account Registration", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        footerPanel.add(footerText);
        footerPanel.add(contactAdminBtn);

        // Assemble Form Layout
        formContainer.add(titleLabel);
        formContainer.add(Box.createVerticalStrut(8));
        formContainer.add(subtitleLabel);
        
        formContainer.add(Box.createVerticalStrut(32));
        formContainer.add(usernameLabel);
        formContainer.add(Box.createVerticalStrut(6));
        formContainer.add(usernameField);
        
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(passwordLabel);
        formContainer.add(Box.createVerticalStrut(6));
        formContainer.add(passwordField);
        
        formContainer.add(Box.createVerticalStrut(16));
        formContainer.add(rememberCheckbox);
        
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(errorLabel);
        
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(signInButton);
        
        formContainer.add(Box.createVerticalStrut(32));
        formContainer.add(footerPanel);

        // Center the form container inside the GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);
        add(formContainer, gbc);
    }

    private void triggerLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        signInButton.setEnabled(false);
        errorLabel.setText("Authenticating credentials...");

        // Perform validation on a background thread to prevent UI lockups
        new Thread(() -> {
            User user = userService.authenticate(username, password);
            SwingUtilities.invokeLater(() -> {
                signInButton.setEnabled(true);
                if (user != null) {
                    errorLabel.setText(" ");
                    AppState.getInstance().setCurrentUser(user);
                    router.navigate("/dashboard");
                } else {
                    errorLabel.setText("Invalid username or password.");
                    passwordField.setText("");
                    passwordField.requestFocusInWindow();
                }
            });
        }).start();
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText(" ");
        usernameField.requestFocusInWindow();
    }
}
