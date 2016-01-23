package com.toptal.entrance.alexeyz.ui.view;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.toptal.entrance.alexeyz.util.UserUtil.*;
/**
 * @author alexey.zakharchenko@gmail.com
 */
public class MainView extends MVerticalLayout {

    private JoggingTab joggingTab;
    private WeekTab weekTab;
    private AdminJoggingTab allJoggingTab;

    final MainUI ui;

    public MainView(MainUI ui) {
        this.ui = ui;

        TabSheet tabsheet = new TabSheet();

        joggingTab = new JoggingTab(this)
                .init();
        joggingTab.setCaption("My Jogging History");
        tabsheet.addTab(joggingTab);

        weekTab = new WeekTab(this)
                .init();
        weekTab.setCaption("My Weekly Reports");
        tabsheet.addTab(weekTab);

        if (currentUser().isManager()) {
            UserTab userTab = new UserTab(this)
                    .init();
            userTab.setCaption("Users");
            tabsheet.addTab(userTab);
        }

        if (currentUser().isAdmin()) {
            allJoggingTab = new AdminJoggingTab(this)
                    .init();
            allJoggingTab.setCaption("All users' jogging history");
            tabsheet.addTab(allJoggingTab);
        }

        Label hello = new Label("<h2>Welcome, " + currentUser().getLogin() + "!</h2>", ContentMode.HTML);
        Button logout = new MButton("Logout", (e) -> logOut());

        addComponent(
                new MHorizontalLayout(hello, logout)
                        .withAlign(hello, Alignment.MIDDLE_LEFT)
                        .withAlign(logout, Alignment.MIDDLE_RIGHT)
                        .withFullWidth()
        );
        addComponent(new MHorizontalLayout(tabsheet).expand(tabsheet));

    }

    // @todo Refactor to JogChangedEvent +listeners
    void onJogChange() {
        joggingTab.reloadData();
        weekTab.reloadData();
        if (allJoggingTab != null)
            allJoggingTab.reloadData();
    }

}
