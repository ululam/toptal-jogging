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

import static com.toptal.entrance.alexeyz.util.UserUtil.*;

/**
 * @author alexey.zakharchenko@gmail.com
 */
class UserTab extends MVerticalLayout {
    private MTable<User> usersTable = new MTable<>(User.class)
            .withProperties("id", "login", "role", "createdAt")
            .withColumnHeaders("Id", "Login", "Role", "Created At")
            .setSortableProperties("login", "createdAt")
            .withFullWidth();


    private Button addNew = new MButton(FontAwesome.PLUS, this::add);
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete this user?", this::remove);


    private final MainView view;

    UserTab(MainView view) {
        this.view = view;
    }

    UserTab init() {
        addComponent(new MHorizontalLayout(addNew, edit, delete));
        addComponent(usersTable);

        reloadUsers();

        usersTable.addMValueChangeListener(e -> adjustActionButtonState());

        return this;
    }

    private void reloadUsers() {
        usersTable.setBeans(view.ui.userRepository.findAll());

        adjustActionButtonState();
    }


    private void adjustActionButtonState() {
        boolean hasSelection = usersTable.getValue() != null;
        boolean isMe = false, isAdmin = false;
        if (hasSelection && currentUser() != null) {
            isMe = currentUser().getId().equals(usersTable.getValue().getId());
            isAdmin = usersTable.getValue().isAdmin();
        }
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection && !isMe && !isAdmin);
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
            // Delete associated joggings (curently I don't rely upon many-to-one cascading)
            view.ui.userRepository.delete(userId);
//            view.ui.joggingRepository.deleteByUserId(userId);
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
