package com.example.demo.tests;

import com.example.demo.logic.atms.Atm;
import com.example.demo.logic.atms.BalanceChangedEvent;
import com.example.demo.logic.shared_kernel.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class AtmTests {
    @Test
    void takeMoneyExchangesMoneyWithCommission() {
        Atm atm = new Atm();
        atm.loadMoney(Money.Dollar);

        atm.takeMoney(BigDecimal.ONE);

        assertThat(atm.getMoneyInside()).isEqualTo(Money.None);
        assertThat(atm.getMoneyCharged()).isEqualByComparingTo(BigDecimal.valueOf(1.01));
    }

    @Test
    void commissionIsAtLeastOneCent(){
        Atm atm = new Atm();
        atm.loadMoney(Money.Cent);

        atm.takeMoney(BigDecimal.valueOf(0.01));

        assertThat(atm.getMoneyCharged()).isEqualByComparingTo(BigDecimal.valueOf(0.02));
    }


    @Test
    void commissionIsRoundedUpToTheNextCent(){
        Atm atm = new Atm();
        atm.loadMoney(Money.Dollar.add(Money.TenCent));

        atm.takeMoney(BigDecimal.valueOf(1.1));

        assertThat(atm.getMoneyCharged()).isEqualByComparingTo(BigDecimal.valueOf(1.12));
    }

    @Test
    void takeMoneyRaisesAnEvent() {
        Atm atm = new Atm();
        atm.loadMoney(Money.Dollar);

        atm.takeMoney(BigDecimal.ONE);

        BalanceChangedEvent balanceChangedEvent = (BalanceChangedEvent) atm.getDomainEvents().get(0);
        assertThat(balanceChangedEvent).isNotNull();
        assertThat(balanceChangedEvent.delta()).isEqualByComparingTo(BigDecimal.valueOf(1.01));
    }
}
