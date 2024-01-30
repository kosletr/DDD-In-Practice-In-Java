package com.example.demo.shared_kernel;

import com.example.demo.common.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money extends ValueObject<Money> {
    private int oneCentCount;
    private int tenCentCount;
    private int quarterCount;
    private int oneDollarCount;
    private int fiveDollarCount;
    private int twentyDollarCount;

    private Money() {}
    private Money(int oneCentCount, int tenCentCount, int quarterCount, int oneDollarCount, int fiveDollarCount, int twentyDollarCount) {
        if (oneCentCount < 0 ||
                tenCentCount < 0 ||
                quarterCount < 0 ||
                oneDollarCount < 0 ||
                fiveDollarCount < 0 ||
                twentyDollarCount < 0
        )
            throw new IllegalArgumentException();

        this.oneCentCount = oneCentCount;
        this.tenCentCount = tenCentCount;
        this.quarterCount = quarterCount;
        this.oneDollarCount = oneDollarCount;
        this.fiveDollarCount = fiveDollarCount;
        this.twentyDollarCount = twentyDollarCount;
    }

    public static Money of(final int oneCentCount, final int tenCentCount, final int quarterCount, final int oneDollarCount, final int fiveDollarCount, final int twentyDollarCount) {
        return new Money(oneCentCount, tenCentCount, quarterCount, oneDollarCount, fiveDollarCount, twentyDollarCount);
    }

    public static final Money None = Money.of(0, 0, 0, 0, 0, 0);
    public static final Money Cent = Money.of(1, 0, 0, 0, 0, 0);
    public static final Money TenCent = Money.of(0, 1, 0, 0, 0, 0);
    public static final Money Quarter = Money.of(0, 0, 1, 0, 0, 0);
    public static final Money Dollar = Money.of(0, 0, 0, 1, 0, 0);
    public static final Money FiveDollar = Money.of(0, 0, 0, 0, 1, 0);
    public static final Money TwentyDollar = Money.of(0, 0, 0, 0, 0, 1);

    public Money add(final Money money) {
        return Money.of(
                oneCentCount + money.oneCentCount,
                tenCentCount + money.tenCentCount,
                quarterCount + money.quarterCount,
                oneDollarCount + money.oneDollarCount,
                fiveDollarCount + money.fiveDollarCount,
                twentyDollarCount + money.twentyDollarCount
        );
    }

    public Money subtract(final Money money) {
        return Money.of(
                oneCentCount - money.oneCentCount,
                tenCentCount - money.tenCentCount,
                quarterCount - money.quarterCount,
                oneDollarCount - money.oneDollarCount,
                fiveDollarCount - money.fiveDollarCount,
                twentyDollarCount - money.twentyDollarCount
        );
    }


    public Money multiply(final int multiplier) {
        return Money.of(
                oneCentCount * multiplier,
                tenCentCount * multiplier,
                quarterCount * multiplier,
                oneDollarCount * multiplier,
                fiveDollarCount * multiplier,
                twentyDollarCount * multiplier
        );
    }

    public BigDecimal amount() {
        return BigDecimal.valueOf(oneCentCount).multiply(BigDecimal.valueOf(0.01))
                .add(BigDecimal.valueOf(tenCentCount).multiply(BigDecimal.valueOf(0.10)))
                .add(BigDecimal.valueOf(quarterCount).multiply(BigDecimal.valueOf(0.25)))
                .add(BigDecimal.valueOf(oneDollarCount).multiply(BigDecimal.valueOf(1)))
                .add(BigDecimal.valueOf(fiveDollarCount).multiply(BigDecimal.valueOf(5)))
                .add(BigDecimal.valueOf(twentyDollarCount).multiply(BigDecimal.valueOf(20)));
    }

    public boolean canAllocate(final BigDecimal amount) {
        Money money = allocateCore(amount);
        return money.amount().compareTo(amount) == 0;
    }

    public Money allocate(final BigDecimal amount) {
        if (!canAllocate(amount))
            throw new IllegalStateException();
        return allocateCore(amount);
    }

    private Money allocateCore(final BigDecimal amount) {
        BigDecimal total = new BigDecimal(amount.toString());

        final int resultTwentyDollarCount = Math.min((int)(total.doubleValue()) / 20, twentyDollarCount);
        total = total.subtract(BigDecimal.valueOf(resultTwentyDollarCount * 20.0));

        final int resultFiveDollarCount = Math.min((int)(total.doubleValue()) / 5, fiveDollarCount);
        total = total.subtract(BigDecimal.valueOf(resultFiveDollarCount * 5.0));

        final int resultOneDollarCount = Math.min((int)(total.doubleValue()), oneDollarCount);
        total = total.subtract(BigDecimal.valueOf(resultOneDollarCount));

        final int resultQuarterCount = Math.min((int)(total.doubleValue() / 0.25), quarterCount);
        total = total.subtract(BigDecimal.valueOf(resultQuarterCount * 0.25));

        final int resultTenCentCount = Math.min((int)(total.doubleValue() / 0.10), tenCentCount);
        total = total.subtract(BigDecimal.valueOf(resultTenCentCount * 0.10));

        final int resultOneCentCount = Math.min((int)(total.doubleValue() / 0.01), oneCentCount);

        return Money.of(
                resultOneCentCount, resultTenCentCount, resultQuarterCount,
                resultOneDollarCount, resultFiveDollarCount, resultTwentyDollarCount
        );
    }

    @Override
    protected boolean equalsCore(final Money other) {
        return oneCentCount == other.oneCentCount &&
                tenCentCount == other.tenCentCount &&
                quarterCount == other.quarterCount &&
                oneDollarCount == other.oneDollarCount &&
                fiveDollarCount == other.fiveDollarCount &&
                twentyDollarCount == other.twentyDollarCount;
    }

    @Override
    protected int hashCodeCore() {
        int hashCode = oneCentCount;
        hashCode = (hashCode * 397) ^ tenCentCount;
        hashCode = (hashCode * 397) ^ quarterCount;
        hashCode = (hashCode * 397) ^ oneDollarCount;
        hashCode = (hashCode * 397) ^ fiveDollarCount;
        hashCode = (hashCode * 397) ^ twentyDollarCount;
        return hashCode;
    }

    @Override
    public String toString() {
        if (amount().compareTo(BigDecimal.ONE) < 0)
            return "¢" + amount().multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_EVEN);
        return "$" + amount().setScale(2, RoundingMode.HALF_EVEN);
    }

    public int getOneCentCount() {
        return oneCentCount;
    }

    public int getTenCentCount() {
        return tenCentCount;
    }

    public int getQuarterCount() {
        return quarterCount;
    }

    public int getOneDollarCount() {
        return oneDollarCount;
    }

    public int getFiveDollarCount() {
        return fiveDollarCount;
    }

    public int getTwentyDollarCount() {
        return twentyDollarCount;
    }
}
