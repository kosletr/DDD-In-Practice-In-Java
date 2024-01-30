package com.example.demo.management;

import com.example.demo.atms.Atm;
import com.example.demo.common.AggregateRoot;
import com.example.demo.shared_kernel.Money;
import com.example.demo.snack_machines.SnackMachine;

import java.math.BigDecimal;

public class HeadOffice extends AggregateRoot {
    private BigDecimal balance;
    private Money cash = Money.None;

    public void changeBalance(BigDecimal delta) {
        balance = balance.add(delta);
    }

    public void unloadCashFromSnackMachine(SnackMachine snackMachine) {
        Money money = snackMachine.unloadMoney();
        cash = cash.add(money);
    }

    public void loadCashToAtm(Atm atm) {
        atm.loadMoney(cash);
        cash = Money.None;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Money getCash() {
        return cash;
    }
}
