package com.example.demo.logic.management;

import com.example.demo.logic.atms.BalanceChangedEvent;
import com.example.demo.logic.common.DomainEventHandler;


public class BalanceChangedEventHandler implements DomainEventHandler<BalanceChangedEvent> {
    private final HeadOfficeRepository headOfficeRepository;

    public BalanceChangedEventHandler(HeadOfficeRepository headOfficeRepository) {
        this.headOfficeRepository = headOfficeRepository;
    }

    @Override
    public Class<BalanceChangedEvent> getEventClass() {
        return BalanceChangedEvent.class;
    }

    @Override
    public void handle(BalanceChangedEvent domainEvent) {
        HeadOffice headOffice = HeadOfficeInstance.getInstance();
        headOffice.changeBalance(domainEvent.delta());
        headOfficeRepository.save(headOffice);
    }
}
