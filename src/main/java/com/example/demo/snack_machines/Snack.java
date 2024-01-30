package com.example.demo.snack_machines;

import com.example.demo.common.AggregateRoot;

public class Snack extends AggregateRoot {
    private String name;

    public static final Snack None = new Snack(0, "None");
    public static final Snack Chocolate = new Snack(1, "Chocolate");
    public static final Snack Soda = new Snack(2, "Soda");
    public static final Snack Gum = new Snack(3, "Gum");

    protected Snack() {}
    private Snack(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
