package com.toptal.entrance.alexeyz.ui.view.jogging;

import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;
import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.repo.JoggingRepository;
import com.toptal.entrance.alexeyz.repo.UserRepository;
import com.toptal.entrance.alexeyz.ui.event.Events;
import com.toptal.entrance.alexeyz.ui.event.JoggingEventBus;
import com.toptal.entrance.alexeyz.ui.view.LoginView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@SpringUI
@Theme("valo")
@Title("Toptal Test Project - AlexeyZ")
public class JoggingUI extends UI {

    private static final long serialVersionUID = 1L;

    @Autowired
    JoggingRepository joggingRepository;

    @Autowired
    UserRepository userRepository;

    private JoggingTab joggingTab;
    private WeekTab weekTab;
    private UsersTab usersTab;

    @Override
    protected void init(VaadinRequest request) {
        TabSheet tabsheet = new TabSheet();

        joggingTab = new JoggingTab(this)
                .init();
        joggingTab.setCaption("My Jogging History");
        tabsheet.addTab(joggingTab);

        weekTab = new WeekTab(this)
                .init();
        weekTab.setCaption("Weekly Reports");
        tabsheet.addTab(weekTab);

        usersTab = new UsersTab(this)
                .init();
        usersTab.setCaption("Users");
        tabsheet.addTab(usersTab);


        Label hello = new Label("<h2>Welcome, " + "%username%" + "!</h2>", ContentMode.HTML);
        Button logout = new MButton("Logout");
        Layout layout = new MVerticalLayout(
                new MHorizontalLayout(hello, logout)
                        .withAlign(hello, Alignment.MIDDLE_LEFT)
                        .withAlign(logout, Alignment.MIDDLE_RIGHT)
                        .withFullWidth(),
                new MHorizontalLayout(tabsheet).expand(tabsheet)
        );

        setContent(layout);
    }

    void onJogChange() {
        joggingTab.reloadData();
        weekTab.reloadData();
    }

    private final JoggingEventBus bus = new JoggingEventBus();

    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());

        if (user != null && user.getRole() == User.Role.admin) {
            // Authenticated user
            //setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final Events.UserLoginRequestedEvent event) {
//        User user = userRepository.authenticate(event.getUserName(), event.getPassword());
        User user = userRepository.findByLogin(event.getUserName());
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final Events.UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final Events.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    /**
     * @return An instance for accessing the (dummy) services layer.
     */
    public static JoggingEventBus getJoggingEventbus() {
        return ((JoggingUI) getCurrent()).bus;
    }

    // @todo Temp
    private User user;
    User currentUser() {
        if (user == null)
            user = userRepository.findAll().get(0);

        return user;
    }

    void closeWindow() {
        getWindows().stream().forEach(w -> removeWindow(w));
    }

}
