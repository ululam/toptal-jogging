package com.toptal.entrance.alexeyz.ui;

import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;
import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.domain.Week;
import com.toptal.entrance.alexeyz.repo.UserRepository;
import com.toptal.entrance.alexeyz.rest.JoggingController;
import com.toptal.entrance.alexeyz.ui.event.Events;
import com.toptal.entrance.alexeyz.ui.event.JoggingEventBus;
import com.toptal.entrance.alexeyz.ui.form.JoggingForm;
import com.toptal.entrance.alexeyz.ui.view.LoginView;
import com.toptal.entrance.alexeyz.ui.view.UsersTab;
import com.toptal.entrance.alexeyz.util.Utils;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToFloatConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@SpringUI
@Theme("valo")
@Title("Toptal Test Project - AlexeyZ")
public class JoggingUI extends UI {
    @Autowired
    private JoggingController controller;

    @Autowired
    public UserRepository userRepository;

    private static final long serialVersionUID = 1L;

    private MTable<Jog> joggingTable = new MTable<>(Jog.class)
            .withProperties("id", "date", "distance", "time", "averageSpeed")
            .withColumnHeaders("id", "Date", "Distance, km", "Time", "Average Speed, km/h")
            .setSortableProperties("date")
            .withFullWidth();

    private MTable<Week> weeksTable = new MTable<>(Week.class)
            .withProperties("week", "start", "end", "runs", "distance", "time", "averageSpeed")
            .withColumnHeaders("Week Number", "Start Date", "End Date", "Runs", "Total Distance, km", "Total Time", "Average Speed, km/h")
            .setSortableProperties("week")
            .withFullWidth();

    private Button addNew = new MButton(FontAwesome.PLUS, this::add);
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete this entry?", this::remove);

    @Override
    protected void init(VaadinRequest request) {
        TabSheet tabsheet = new TabSheet();

        Layout joggingTab = new MVerticalLayout(
                new MHorizontalLayout(addNew, edit, delete),
                joggingTable
        ).expand(joggingTable);

        joggingTab.setCaption("My Jogging History");
        tabsheet.addTab(joggingTab);
        weeksTable.setCaption("Weekly Reports");
        tabsheet.addTab(weeksTable);
        UsersTab usersTab = new UsersTab(this).init();
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

        setConverters();

        listJoggings();

        joggingTable.addMValueChangeListener(e -> adjustActionButtonState());


        // Configure the error handler for the UI
//        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
//            @Override
//            public void error(com.vaadin.server.ErrorEvent event) {
//                // Find the final cause
//                String cause = "<b>The click failed because:</b><br/>";
//                for (Throwable t = event.getThrowable(); t != null;
//                     t = t.getCause())
//                    if (t.getCause() == null) // We're at final cause
//                        cause += t.getClass().getName() + "<br/>";
//
//                // Display the error message in a custom fashion
//                layout.addComponent(new Label(cause, ContentMode.HTML));
//
//                // Do the default error handling (optional)
//                doDefault(event);
//            }
//        });

    }

    private void setConverters() {
        joggingTable.setConverter("date", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance();
            }
        });

        joggingTable.setConverter("time", new StringToIntegerConverter() {
            @Override
            public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return Utils.formatHMS(value);
            }
        });

        joggingTable.setConverter("averageSpeed", new StringToFloatConverter() {
            @Override
            public String convertToPresentation(Float value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return Utils.format(value);
            }
        });


        weeksTable.setConverter("start", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance();
            }
        });
        weeksTable.setConverter("end", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(Locale locale) {
                return DateFormat.getDateInstance();
            }
        });

        weeksTable.setConverter("time", new StringToIntegerConverter() {
            @Override
            public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return Utils.formatHMS(value);
            }
        });

        weeksTable.setConverter("averageSpeed", new StringToFloatConverter() {
            @Override
            public String convertToPresentation(Float value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return Utils.format(value);
            }
        });

    }

    protected void adjustActionButtonState() {
        boolean hasSelection = joggingTable.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    static final int PAGESIZE = 50;

    private void listJoggings() {
        // A dead simple in memory listing would be:
        // joggingTable.setBeans(repo.findAll());

        // Lazy binding with SortableLazyList: memory and query efficient
        // connection from Vaadin Table to Spring Repository
        // Note that fetching strategies can be given to MTable constructor as well.
        // Use this approach if you expect you'll have lots of data in your
        // joggingTable.

        joggingTable.setBeans(new SortableLazyList<>(
                // entity fetching strategy
                (firstRow, asc, sortProperty) -> controller.list(),
                // count fetching strategy
                () -> controller.list().size(),
                PAGESIZE
        ));


        weeksTable.setBeans(controller.weeks());

        adjustActionButtonState();
    }

    public void add(Button.ClickEvent clickEvent) {
        Jog jog = new Jog();
        jog.setDate(new Date());
        edit(jog);
    }

    public void edit(Button.ClickEvent e) {
        edit(joggingTable.getValue());
    }

    public void remove(Button.ClickEvent e) {
        if (joggingTable.getValue() != null) {
            controller.delete(joggingTable.getValue().getId());
            joggingTable.setValue(null);
            listJoggings();
        }
    }

    protected void edit(Jog entry) {
        JoggingForm form = new JoggingForm(entry);
        form.openInModalPopup();
        form.setSavedHandler(this::saveEntry);
        form.setResetHandler(this::resetEntry);
    }

    public void saveEntry(Jog entry) {
        if (entry.getId() == null)
            controller.create(entry);
        else
            controller.update(entry.getId(), entry);

        listJoggings();
        closeWindow();
    }

    public void resetEntry(Jog entry) {
        listJoggings();
        closeWindow();
    }

    protected void closeWindow() {
        getWindows().stream().forEach(w -> removeWindow(w));
    }

    private final JoggingEventBus bus = new JoggingEventBus();

    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());

        if (user != null && "admin".equals(user.getRole())) {
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
}
