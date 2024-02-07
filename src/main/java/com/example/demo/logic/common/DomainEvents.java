package com.example.demo.logic.common;

import java.util.ArrayList;
import java.util.List;


public class DomainEvents {
    private static final List<DomainEventHandler<?>> handlers = new ArrayList<>();

    private DomainEvents() {
    }

    public static <T extends DomainEvent> void init(List<DomainEventHandler<T>> handlers) {
         DomainEvents.handlers.addAll(handlers);
    }

    public static void dispatch(DomainEvent domainEvent) {
        for (DomainEventHandler<?> handler : handlers) {
            if (handler.getEventClass().isAssignableFrom(domainEvent.getClass())) {
                //noinspection unchecked
                ((DomainEventHandler<DomainEvent>) handler).handle(domainEvent);
            }
        }
    }
}
