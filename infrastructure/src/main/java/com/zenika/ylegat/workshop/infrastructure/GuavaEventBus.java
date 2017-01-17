package com.zenika.ylegat.workshop.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.common.eventbus.Subscribe;
import com.zenika.ylegat.workshop.domain.common.Event;
import com.zenika.ylegat.workshop.domain.common.EventBus;
import com.zenika.ylegat.workshop.domain.common.EventListener;

public class GuavaEventBus implements EventBus {

    private static class GuavaEventListener {

        private final EventListener eventListener;

        public GuavaEventListener(EventListener eventListener) {
            this.eventListener = eventListener;
        }

        @Subscribe
        public void onEvent(Event event) {
            eventListener.on(event);
        }
    }

    private com.google.common.eventbus.EventBus guavaEventBus;

    private final Map<EventListener, GuavaEventListener> eventListenerWrappers = new HashMap<>();

    public GuavaEventBus() {
        clear();
    }

    @Override
    public void register(EventListener eventListener) {
        GuavaEventListener guavaEventListener = eventListenerWrappers.computeIfAbsent(eventListener, GuavaEventListener::new);
        guavaEventBus.register(guavaEventListener);
    }

    @Override
    public void unregister(EventListener listener) {
        Optional.ofNullable(eventListenerWrappers.get(listener))
                .ifPresent(guavaEventBus::unregister);
    }

    @Override
    public void push(List<Event> events) {
        events.forEach(guavaEventBus::post);
    }

    @Override
    public void clear() {
        guavaEventBus = new com.google.common.eventbus.EventBus();
        eventListenerWrappers.clear();
    }
}
