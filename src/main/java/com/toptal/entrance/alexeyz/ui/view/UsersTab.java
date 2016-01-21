package com.toptal.entrance.alexeyz.ui.view;

import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.repo.UserRepository;
import com.toptal.entrance.alexeyz.ui.JoggingUI;
import com.toptal.entrance.alexeyz.ui.form.UserForm;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class UsersTab extends MVerticalLayout {
    private MTable<User> usersTable = new MTable<>(User.class)
            .withProperties("id", "login", "role", "createdAt")
            .withColumnHeaders("Id", "Login", "Role", "Created At")
            .setSortableProperties("week")
            .withFullWidth();


    private Button addNew = new MButton(FontAwesome.PLUS, this::add);
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete this user?", this::remove);


    @Autowired
    private final UserRepository userRepository;

    private final JoggingUI ui;


    public UsersTab(JoggingUI ui) {
        this.ui = ui;
        this.userRepository = ui.userRepository;
    }

    public UsersTab init() {
        addComponent(new MHorizontalLayout(addNew, edit, delete));
        addComponent(usersTable);

        listUsers();

        usersTable.addMValueChangeListener(e -> adjustActionButtonState());

        return this;
    }

    private void listUsers() {
        usersTable.setBeans(userRepository.findAll());

        adjustActionButtonState();
    }


    protected void adjustActionButtonState() {
        boolean hasSelection = usersTable.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    public void add(Button.ClickEvent clickEvent) {
        User user = new User();
        edit(user);
    }

    public void edit(Button.ClickEvent e) {
        edit(usersTable.getValue());
    }

    public void remove(Button.ClickEvent e) {
        if (usersTable.getValue() != null) {
            userRepository.delete(usersTable.getValue().getId());
            usersTable.setValue(null);
            listUsers();
        }
    }

    protected void edit(User entry) {
        UserForm form = new UserForm(entry);
        form.openInModalPopup();
        form.setSavedHandler(this::saveEntry);
        form.setResetHandler(this::resetEntry);
    }

    public void saveEntry(User entry) {
        userRepository.save(entry);

        listUsers();
        closeWindow();
    }

    public void resetEntry(User entry) {
        listUsers();
        closeWindow();
    }

    protected void closeWindow() {
        ui.getWindows().stream().forEach(w -> ui.removeWindow(w));
    }

}
