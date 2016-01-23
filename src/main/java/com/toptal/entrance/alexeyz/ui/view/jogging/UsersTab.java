package com.toptal.entrance.alexeyz.ui.view.jogging;

import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.ui.form.UserForm;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author alexey.zakharchenko@gmail.com
 */
class UsersTab extends MVerticalLayout {
    private MTable<User> usersTable = new MTable<>(User.class)
            .withProperties("id", "login", "role", "createdAt")
            .withColumnHeaders("Id", "Login", "Role", "Created At")
            .setSortableProperties("login")
            .withFullWidth();


    private Button addNew = new MButton(FontAwesome.PLUS, this::add);
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete this user?", this::remove);


    private final JoggingUI ui;

    UsersTab(JoggingUI ui) {
        this.ui = ui;
    }

    UsersTab init() {
        addComponent(new MHorizontalLayout(addNew, edit, delete));
        addComponent(usersTable);

        reloadUsers();

        usersTable.addMValueChangeListener(e -> adjustActionButtonState());

//        usersTable.addItemClickListener((e) -> {
//            if (e.isDoubleClick())
//                edit(usersTable.getValue());
//        });

        return this;
    }

    private void reloadUsers() {
        usersTable.setBeans(ui.userRepository.findAll());

        adjustActionButtonState();
    }


    private void adjustActionButtonState() {
        boolean hasSelection = usersTable.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
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
            ui.userRepository.delete(usersTable.getValue().getId());
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
        ui.userRepository.save(entry);

        reloadUsers();
        ui.closeWindow();
    }

    private void resetEntry(User entry) {
        reloadUsers();
        ui.closeWindow();
    }

}
