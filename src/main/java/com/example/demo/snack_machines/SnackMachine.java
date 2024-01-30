package com.example.demo.snack_machines;

import com.example.demo.common.AggregateRoot;
import com.example.demo.shared_kernel.Money;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SnackMachine extends AggregateRoot {
    private Money moneyInside;
    private BigDecimal moneyInTransaction;
    protected List<Slot> slots;

    private static final Set<Money> coinsOrNotes = Set.of(
            Money.Cent, Money.TenCent, Money.Quarter,
            Money.Dollar, Money.FiveDollar, Money.TwentyDollar
    );

    public SnackMachine() {
        moneyInside = Money.None;
        moneyInTransaction = BigDecimal.ZERO;
        slots = List.of(
                new Slot(this,1),
                new Slot(this, 2),
                new Slot(this, 3)
        );
    }

    public SnackPile getSnackPile(final int position) {
        return getSlot(position).getSnackPile();
    }

    public List<SnackPile> retrieveAllSnackPiles() {
        return slots.stream()
                .sorted(Comparator.comparingInt(Slot::getPosition))
                .map(Slot::getSnackPile)
                .collect(Collectors.toUnmodifiableList());
    }

    private Slot getSlot(final int position) {
        return slots.stream()
                .filter(s -> s.getPosition() == position)
                .findFirst()
                .orElseThrow();
    }

    public void insertMoney(final Money money) {
        if (!coinsOrNotes.contains(money))
            throw new IllegalArgumentException();
        moneyInTransaction = moneyInTransaction.add(money.amount());
        moneyInside = moneyInside.add(money);
    }

    public void returnMoney() {
        Money moneyToReturn = moneyInside.allocate(moneyInTransaction);
        moneyInside = moneyInside.subtract(moneyToReturn);
        moneyInTransaction = BigDecimal.ZERO;
    }

    public String canBuySnack(final int position) {
        final SnackPile snackPile = getSnackPile(position);

        if (snackPile.getQuantity() == 0)
            return "The snack pile is empty.";

        if (moneyInTransaction.compareTo(snackPile.getPrice()) < 0)
            return "Not enough money";

        if (!moneyInside.canAllocate(moneyInTransaction.subtract(snackPile.getPrice())))
            return "Not enough change";

        return "";
    }

    public void buySnack(final int position) {
        final String errorMessage = canBuySnack(position);
        if (!errorMessage.isEmpty())
            throw new IllegalStateException(errorMessage);

        final Slot slot = getSlot(position);

        slot.setSnackPile(slot.getSnackPile().subtractOne());
        final Money change = moneyInside.allocate(moneyInTransaction.subtract(slot.getSnackPile().getPrice()));

        moneyInside = moneyInside.subtract(change);
        moneyInTransaction = BigDecimal.ZERO;
    }

    public void loadSnacks(final int position, final SnackPile snackPile) {
        getSlot(position).setSnackPile(snackPile);
    }

    public void loadMoney(final Money money) {
        moneyInside = moneyInside.add(money);
    }

    public Money unloadMoney() {
        if (moneyInTransaction.compareTo(BigDecimal.ZERO) > 0)
            throw new IllegalStateException();

        Money money = moneyInside;
        moneyInside = Money.None;
        return money;
    }

    public List<Slot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public Money getMoneyInside() {
        return moneyInside;
    }

    public BigDecimal getMoneyInTransaction() {
        return moneyInTransaction;
    }
}
