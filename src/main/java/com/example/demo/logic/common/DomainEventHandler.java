package com.example.demo.logic.common;

public interface DomainEventHandler<T extends DomainEvent> {
    Class<T> getEventClass();
    void handle(T domainEvent);
}
