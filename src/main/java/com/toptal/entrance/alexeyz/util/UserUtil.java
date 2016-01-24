package com.toptal.entrance.alexeyz.util;

import com.toptal.entrance.alexeyz.domain.User;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import org.springframework.util.DigestUtils;

/**
 * Vaadin user session utility class
 *
 * @author alexey.zakharchenko@gmail.com
 */
public final class UserUtil {

    public static boolean isLoggedIn() {
        return currentUser() != null;
    }

    public static void logOut() {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    /**
     * @return Current logged user
     */
    public static User currentUser() {
        return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
    }

    /**
     * @param password Plain password
     * @return MD hash of the given password. Its length always = 32
     */
    public static String hash(String password) {
        if (password == null)
            password = "";

        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
