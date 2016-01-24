package com.toptal.entrance.alexeyz.ui.view;

import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.ui.form.UserForm;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

import static com.toptal.entrance.alexeyz.util.UserUtil.currentUser;

/**
 * Tabs that displays list of users
 *
 * @author alexey.zakharchenko@gmail.com
 */
class UserTab extends MVerticalLayout {
    private MTable<User> usersTable = new MTable<>(User.class)
            .withProperties("id", "login", "role", "createdAt")
            .withColumnHeaders("Id", "Login", "Role", "Created At")
            .setSortableProperties("login", "createdAt")
            .withFullWidth();


    private Button addNewButton = new MButton(FontAwesome.PLUS, this::add);
    private Button editButton = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private Button deleteButton = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete this user?", this::remove);
    private Button refreshButton = new MButton(FontAwesome.REFRESH, (e) -> reloadUsers());


    private final MainView view;

    UserTab(MainView view) {
        this.view = view;
    }

    UserTab init() {
        addComponent(new MHorizontalLayout(addNewButton, editButton, deleteButton, refreshButton));
        addComponent(usersTable);

        reloadUsers();

        usersTable.addMValueChangeListener(e -> adjustActionButtonState());

        return this;
    }

    private void reloadUsers() {
        List<User> users = view.ui.userRepository.findAll();
        users.forEach(u -> u.setPassword(null));
        usersTable.setBeans(users);

        adjustActionButtonState();
    }


    private void adjustActionButtonState() {
        boolean hasSelection = usersTable.getValue() != null;
        boolean isMe = false, isAdmin = false;
        if (hasSelection && currentUser() != null) {
            isMe = currentUser().getId().equals(usersTable.getValue().getId());
            isAdmin = usersTable.getValue().isAdmin();
        }
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection && !isMe && !isAdmin);
    }

    private void add(Button.ClickEvent clickEvent) {
        User user = new User();
        edit(user);
    }

    private void edit(Button.ClickEvent e) {
        edit(usersTable.getValue());
    }

    private void remove(Button.ClickEvent e) {
        if (usersTable.getValue() != null) {
            long userId = usersTable.getValue().getId();
            view.ui.userRepository.delete(userId);
            usersTable.setValue(null);
            reloadUsers();
        }
    }

    private void edit(User entry) {
        if (entry == null)
            return;

        UserForm form = new UserForm(entry);
        form.setSavedHandler(this::saveEntry);
        form.setResetHandler(this::resetEntry);

        form.openInModalPopup();
    }

    private void saveEntry(User entry) {
        view.ui.userRepository.save(entry);

        reloadUsers();
        view.ui.closeWindow();
    }

    private void resetEntry(User entry) {
        reloadUsers();
        view.ui.closeWindow();
    }

}
