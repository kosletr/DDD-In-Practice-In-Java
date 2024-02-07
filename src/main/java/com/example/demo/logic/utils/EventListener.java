package com.example.demo.logic.utils;

import com.example.demo.logic.common.AggregateRoot;
import com.example.demo.logic.common.DomainEvents;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventListener {

    @PostPersist
    @PostUpdate
    @PostRemove
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dispatchEvents(AggregateRoot aggregateRoot) {
        if (aggregateRoot == null)
            return;

        aggregateRoot.getDomainEvents().forEach(DomainEvents::dispatch);
        aggregateRoot.clearEvents();
    }
}
