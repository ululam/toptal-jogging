package com.toptal.entrance.alexeyz.ui.form;

import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.User;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class UserForm extends AbstractForm<User> {

    private final TextField login = new MTextField("Login");
    private final TextField password = new MTextField("Password");
    private final ComboBox role = new ComboBox("Role", Arrays.asList(User.Role.values()));

    public UserForm(User user) {
        setSizeUndefined();
        setEntity(user);
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

}
