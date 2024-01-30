package com.example.demo.common;

public interface DomainEventHandler<T extends DomainEvent> {
    Class<T> getEventClass();
    void handle(T domainEvent);
}
