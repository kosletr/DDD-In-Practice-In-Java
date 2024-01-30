package com.example.demo.atms;

import com.example.demo.common.DomainEvent;

import java.math.BigDecimal;


public record BalanceChangedEvent(BigDecimal delta) implements DomainEvent {
}
