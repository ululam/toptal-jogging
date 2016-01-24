package com.toptal.entrance.alexeyz.ui.view;

import com.toptal.entrance.alexeyz.db.JoggingRepository;
import com.toptal.entrance.alexeyz.db.UserRepository;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import static com.toptal.entrance.alexeyz.util.UserUtil.isLoggedIn;

/**
 * "Main" UI access point
 *
 * @author alexey.zakharchenko@gmail.com
 */
@SpringUI
@Theme("valo")
@Title("Toptal Test Project - AlexeyZ")
@PreserveOnRefresh
public class MainUI extends UI {
    private static final long serialVersionUID = 1L;

    @Autowired
    JoggingRepository joggingRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void init(VaadinRequest request) {
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();
    }

    void updateContent() {
//        if (user != null && user.getRole() == User.Role.admin) {
        if (isLoggedIn()) {
            // Authenticated user
            setContent(new MainView(this));
            //getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView(this));
        }
    }

    void closeWindow() {
        getWindows().forEach(this::removeWindow);
    }

}
