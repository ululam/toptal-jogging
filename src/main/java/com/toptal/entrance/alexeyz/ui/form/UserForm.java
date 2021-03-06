package com.toptal.entrance.alexeyz.ui.form;

import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.util.UserUtil;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

/**
 * Add/edit User
 *
 * @author alexey.zakharchenko@gmail.com
 */
public class UserForm extends AbstractForm<User> {

    private final TextField login = new MTextField("Login");
    private final TextField password = new MTextField("Password");
    private final ComboBox role = new ComboBox("Role", Arrays.asList(User.Role.values()));

    public UserForm(User user, Validator loginValidator) {
        setSizeUndefined();
        setEntity(user);
        // Only admin may create admins
        if (!UserUtil.currentUser().isAdmin())
            role.removeItem(User.Role.admin);

        role.setNullSelectionAllowed(false);

        login.addValidator(new StringLengthValidator("Username should be at least 3 chars long",
                User.MIN_LOGIN_LENGTH, 1024, false));
        login.addValidator(loginValidator);

        password.addValidator(new StringLengthValidator("Password should be bettween" + User.MIN_PASSWORD_LENGTH
                + " and " + User.MAX_PASSWORD_LENGTH + " char length",
                User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH, false));
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        login,
                        password,
                        role
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
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
