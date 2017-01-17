package com.zenika.ylegat.workshop.domain.common;

import java.util.List;

public interface EventBus {

    static EventBus noEventBus() {
        return new EventBus() {

            @Override
            public void register(EventListener eventListener) {

            }

            @Override
            public void unregister(EventListener listener) {

            }

            @Override
            public void push(List<Event> events) {

            }

            @Override
            public void clear() {

            }
        };
    }

    void register(EventListener eventListener);

    void unregister(EventListener listener);

    void push(List<Event> events);

    void clear();
}
