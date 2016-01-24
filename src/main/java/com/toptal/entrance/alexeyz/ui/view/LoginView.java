package com.toptal.entrance.alexeyz.ui.view;

import com.toptal.entrance.alexeyz.Application;
import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.util.UserUtil;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author alexey.zakharchenko@gmail.com
 */

public class LoginView extends VerticalLayout {

    private TextField usernameField;
    private PasswordField passwordField;

    private final MainUI ui;

    public LoginView(MainUI ui) {
        this.ui = ui;
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        usernameField = new TextField("Username");
        usernameField.setIcon(FontAwesome.USER);
        usernameField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        passwordField = new PasswordField("Password");
        passwordField.setIcon(FontAwesome.LOCK);
        passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button logInButton = new Button("Log In");
        logInButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        logInButton.setClickShortcut(KeyCode.ENTER);
        logInButton.focus();

        final Button signupButton = new Button("Sign Up");
        signupButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        fields.addComponents(usernameField, passwordField, logInButton, signupButton);
        fields.setComponentAlignment(logInButton, Alignment.BOTTOM_LEFT);
        fields.setComponentAlignment(signupButton, Alignment.BOTTOM_LEFT);

        logInButton.addClickListener((ClickListener) event ->
                userLoginRequested(usernameField.getValue(), passwordField.getValue())
        );
        signupButton.addClickListener((ClickListener) event ->
                userSignupRequested(usernameField.getValue(), passwordField.getValue())
        );

        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome to AlexeyZ's Toptal Test Project");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        return labels;
    }

    public void userLoginRequested(String login, String password) {
        if (!validate()) return;

        String pwd = Application.PWD_HASH ? UserUtil.hash(password) : password;

        User user = ui.userRepository.findByLoginAndPassword(login, pwd);

        if (user != null) {
            VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
            ui.updateContent();
        } else {
            passwordField.clear();
            showError("Invalid login or password");
        }
    }

    public void userSignupRequested(String login, String password) {
        if (!validate()) return;

        User user = ui.userRepository.findByLogin(login);
        if (user != null) {
            showError("User with login '" + login + "' already exists");
            return;
        }

        ui.userRepository.save(new User(login, password));

        userLoginRequested(login, password);
    }

    private boolean validate() {
        usernameField.addValidator(new StringLengthValidator("Username should be at least " + User.MIN_LOGIN_LENGTH
                + " chars long",
                User.MIN_LOGIN_LENGTH, 1024, false));

        passwordField.addValidator(new StringLengthValidator("Password should be between " + User.MIN_PASSWORD_LENGTH
                + " and " + User.MAX_PASSWORD_LENGTH + " char length",
                User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH, false));
        try {
            usernameField.validate();
            passwordField.validate();
        } catch (Validator.InvalidValueException e) {
            if (e.getCauses().length > 1)
                showError(e.getCauses()[0].getMessage());
            else
                showError(e.getMessage());

            return false;
        }

        return true;
    }

    private void showError(String message) {
        Notification notification = new Notification(message, Notification.Type.WARNING_MESSAGE);
        notification.setHtmlContentAllowed(true);
//        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
//        notification.setIcon(FontAwesome.WARNING);
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }

}