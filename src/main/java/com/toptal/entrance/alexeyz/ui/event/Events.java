package com.toptal.entrance.alexeyz.ui.event;


import com.toptal.entrance.alexeyz.domain.Jog;

/**
 * System custom event classes
 *
 * @author alexey.zakharchenko@gmail.com
 */
public interface Events {

    class UserLoginRequestedEvent {
        public final String userName, password;

        public UserLoginRequestedEvent(final String userName,
                                       final String password) {
            this.userName = userName;
            this.password = password;
        }
    }

    class UserRegisterRequestedEvent {

    }

    class JogChangedEvent {
        public final Jog jog;

        public JogChangedEvent(Jog jog) {
            this.jog = jog;
        }
    }

    class UserLoggedOutEvent {

    }

    public static class CloseOpenWindowsEvent {
    }

}