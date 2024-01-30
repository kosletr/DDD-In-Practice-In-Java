package com.example.demo.snack_machines;

import com.example.demo.common.ValueObject;

import java.math.BigDecimal;

public final class SnackPile extends ValueObject<SnackPile> {
    private Snack snack;
    private int quantity;
    private BigDecimal price;

    public static final SnackPile Empty = new SnackPile(Snack.None, 0, BigDecimal.ZERO);

    private SnackPile() {
    }

    public SnackPile(final Snack snack, final int quantity, final BigDecimal price) {
        if (quantity < 0)
            throw new IllegalArgumentException();
        if (price.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException();
        if (price.remainder(BigDecimal.valueOf(0.01)).compareTo(BigDecimal.ZERO) > 0)
            throw new IllegalArgumentException();

        this.snack = snack;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    protected boolean equalsCore(final SnackPile other) {
        return snack.equals(other.snack) &&
                quantity == other.quantity &&
                price.equals(other.price);
    }

    @Override
    protected int hashCodeCore() {
        int hashCode = snack.hashCode();
        hashCode = (hashCode * 397) ^ quantity;
        hashCode = (hashCode * 397) ^ price.hashCode();
        return hashCode;
    }

    public Snack getSnack() {
        return snack;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public SnackPile subtractOne() {
        return new SnackPile(snack, quantity - 1, price);
    }
}
