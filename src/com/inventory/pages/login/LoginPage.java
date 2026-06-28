package com.inventory.pages.login;

import com.inventory.components.Theme;
import com.inventory.router.Page;
import com.inventory.router.Router;
import java.awt.GridLayout;

public class LoginPage extends Page {
    private final LoginFormPanel loginFormPanel;
    private final BrandingPanel brandingPanel;

    public LoginPage(Router router) {
        // Set layout to a 1x2 grid (50% left, 50% right split)
        setLayout(new GridLayout(1, 2));
        setBackground(Theme.CREAM_BASE);

        // Instantiate modular components
        brandingPanel = new BrandingPanel();
        loginFormPanel = new LoginFormPanel(router);

        // Assemble page
        add(brandingPanel);
        add(loginFormPanel);
    }

    @Override
    public void onPageLoad() {
        // Delegate focus and state cleanup to the login form component
        loginFormPanel.clearFields();
    }
}
