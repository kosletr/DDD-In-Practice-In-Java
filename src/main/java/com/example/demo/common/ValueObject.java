package com.example.demo.common;

public abstract class ValueObject<T extends ValueObject<T>> {
    @Override
    public boolean equals(final Object obj) {
        final T valueObject = (T) obj;
        if (valueObject == null) return false;
        return equalsCore(valueObject);
    }

    protected abstract boolean equalsCore(final T other);

    @Override
    public int hashCode() {
        return hashCodeCore();
    }

    protected abstract int hashCodeCore();
}
