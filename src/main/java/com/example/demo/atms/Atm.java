package com.example.demo.atms;

import com.example.demo.common.AggregateRoot;
import com.example.demo.shared_kernel.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Atm extends AggregateRoot {
    private Money moneyInside = Money.None;
    private BigDecimal moneyCharged = BigDecimal.ZERO;
    private static final BigDecimal COMMISSION_RATE = BigDecimal.valueOf(0.01);


    // TODO: Unit tests
    public String canTakeMoney(final BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return "Invalid amount";

        if(moneyInside.amount().compareTo(amount) < 0)
            return "Not enough money";

        if (!moneyInside.canAllocate(amount))
            return "Not enough change";

        return "";
    }

    public void takeMoney(final BigDecimal amount) {
        final String errorMessage = canTakeMoney(amount);
        if (!errorMessage.isEmpty())
            throw new IllegalStateException();

        final Money requestedMoney = moneyInside.allocate(amount);
        moneyInside = moneyInside.subtract(requestedMoney);

        BigDecimal amountWithCommission = calculateAmountWithCommission(amount);
        moneyCharged = moneyCharged.add(amountWithCommission);

        addDomainEvent(new BalanceChangedEvent(amountWithCommission));
    }

    public BigDecimal calculateAmountWithCommission(final BigDecimal amount) {
        BigDecimal commission = amount.multiply(Atm.COMMISSION_RATE);

        BigDecimal lessThanCent = commission.remainder(BigDecimal.valueOf(0.01));
        if(lessThanCent.compareTo(BigDecimal.ZERO) > 0)
            commission = commission.subtract(lessThanCent).add(BigDecimal.valueOf(0.01));

        return amount.add(commission);
    }

    public Money getMoneyInside() {
        return moneyInside;
    }

    public BigDecimal getMoneyCharged() {
        return moneyCharged;
    }

    public void loadMoney(Money money) {
        moneyInside = moneyInside.add(money);
    }
}
