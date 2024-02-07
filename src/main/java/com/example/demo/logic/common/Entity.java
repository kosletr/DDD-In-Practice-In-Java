package com.example.demo.logic.common;

import org.hibernate.Hibernate;

import java.util.Objects;

public abstract class Entity {
    protected long id;

    @Override
    public boolean equals(final Object obj) {
        final Entity other = (Entity) obj;
        if (this == other) return true;
        if (other == null || realClass() != other.realClass()) return false;
        if (id == 0 || other.id == 0) return false;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(realClass().toString() + id);
    }

    public long getId() {
        return id;
    }

    private Class<?> realClass() {
        return Hibernate.unproxy(this).getClass();
    }
}
