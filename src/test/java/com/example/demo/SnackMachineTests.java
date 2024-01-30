package com.example.demo;

import com.example.demo.shared_kernel.Money;
import com.example.demo.snack_machines.Snack;
import com.example.demo.snack_machines.SnackMachine;
import com.example.demo.snack_machines.SnackPile;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class SnackMachineTests {
    @Test
    void returnMoneyEmptiesMoneyInTransaction() {
        SnackMachine snackMachine = new SnackMachine();
        snackMachine.insertMoney(Money.Dollar);

        snackMachine.returnMoney();

        assertThat(snackMachine.getMoneyInTransaction()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void insertedMoneyGoesToMoneyInTransaction() {
        SnackMachine snackMachine = new SnackMachine();

        snackMachine.insertMoney(Money.Cent);
        snackMachine.insertMoney(Money.Dollar);

        assertThat(snackMachine.getMoneyInTransaction()).isEqualByComparingTo(BigDecimal.valueOf(1.01));
    }

    @Test
    void cannotInsertMoreThanOneNoteOrCoinAtATime() {
        SnackMachine snackMachine = new SnackMachine();
        Money twoCents = Money.Cent.add(Money.Cent);

        Throwable throwable = catchThrowable(() -> snackMachine.insertMoney(twoCents));

        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void buySnackTradesInsertedMoneyForASnack() {
        SnackMachine snackMachine = new SnackMachine();
        snackMachine.loadSnacks(1, new SnackPile(Snack.Chocolate, 10, BigDecimal.ONE));
        snackMachine.insertMoney(Money.Dollar);

        snackMachine.buySnack(1);

        assertThat(snackMachine.getMoneyInTransaction()).isEqualTo(BigDecimal.ZERO);
        assertThat(snackMachine.getMoneyInside().amount()).isEqualByComparingTo(BigDecimal.valueOf(1));
        assertThat(snackMachine.getSnackPile(1).getQuantity()).isEqualTo(9);
    }

    @Test
    void cannotMakePurchaseWhenThereIsNoSnacks() {
        SnackMachine snackMachine = new SnackMachine();

        Throwable throwable = catchThrowable(() -> snackMachine.buySnack(1));

        assertThat(throwable).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void cannotMakePurchaseWhenThereIsNotEnoughMoneyInserted() {
        SnackMachine snackMachine = new SnackMachine();
        snackMachine.loadSnacks(1, new SnackPile(Snack.Chocolate, 1, BigDecimal.valueOf(2)));
        snackMachine.insertMoney(Money.Dollar);

        Throwable throwable = catchThrowable(() -> snackMachine.buySnack(1));

        assertThat(throwable).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void snackMachineReturnsMoneyWithHighestDenominationFirst() {
        SnackMachine snackMachine = new SnackMachine();
        snackMachine.loadMoney(Money.Dollar);

        snackMachine.insertMoney(Money.Quarter);
        snackMachine.insertMoney(Money.Quarter);
        snackMachine.insertMoney(Money.Quarter);
        snackMachine.insertMoney(Money.Quarter);
        snackMachine.returnMoney();

        assertThat(snackMachine.getMoneyInside().getQuarterCount()).isEqualTo(4);
        assertThat(snackMachine.getMoneyInside().getOneDollarCount()).isZero();
    }

    @Test
    void snackMachineReturnsChangeAfterPurchase() {
        SnackMachine snackMachine = new SnackMachine();
        snackMachine.loadSnacks(1, new SnackPile(Snack.Chocolate, 1, BigDecimal.valueOf(0.5)));
        snackMachine.loadMoney(Money.TenCent.multiply(10));

        snackMachine.insertMoney(Money.Dollar);
        snackMachine.buySnack(1);

        assertThat(snackMachine.getMoneyInside().amount()).isEqualByComparingTo(BigDecimal.valueOf(1.5));
        assertThat(snackMachine.getMoneyInTransaction()).isZero();
    }

    @Test
    void cannotBuySnackIfNotEnoughChangeInTheSnackMachine() {
        SnackMachine snackMachine = new SnackMachine();
        snackMachine.loadSnacks(1, new SnackPile(Snack.Chocolate, 1, BigDecimal.valueOf(0.5)));
        snackMachine.loadMoney(Money.Dollar);

        snackMachine.insertMoney(Money.Dollar);
        Throwable throwable = catchThrowable(() -> snackMachine.buySnack(1));

        assertThat(throwable).isInstanceOf(IllegalStateException.class);
    }
}
