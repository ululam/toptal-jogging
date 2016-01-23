package com.toptal.entrance.alexeyz.ui.event;

import com.google.gwt.thirdparty.guava.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionContext;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionHandler;
import com.toptal.entrance.alexeyz.ui.view.jogging.JoggingUI;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class JoggingEventBus implements SubscriberExceptionHandler {
    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        JoggingUI.getJoggingEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        JoggingUI.getJoggingEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        JoggingUI.getJoggingEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
                                      final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
