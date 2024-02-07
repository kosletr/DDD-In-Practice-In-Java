package com.example.demo.logic.atms;

import com.example.demo.logic.common.DomainEvent;

import java.math.BigDecimal;


public record BalanceChangedEvent(BigDecimal delta) implements DomainEvent {
}
