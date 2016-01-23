package com.toptal.entrance.alexeyz.ui.event;

import com.google.gwt.thirdparty.guava.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionContext;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@Component
public class ApplicationEventBus implements SubscriberExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApplicationEventBus.class);

    private final EventBus eventBus = new EventBus(this);

    public void post(Object event) {
        eventBus.post(event);
    }

    public void register(Object object) {
        eventBus.register(object);
    }

    public void unregister(Object object) {
        eventBus.unregister(object);
    }

    @Override
    public final void handleException(Throwable err, SubscriberExceptionContext context) {
        log.error("Got error on event {} with subscriber {}:{}", context.getEvent(),
                context.getSubscriber(), context.getSubscriberMethod(), err);
    }
}
