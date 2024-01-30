package com.example.demo.snack_machines;

import com.example.demo.common.Entity;

public class Slot extends Entity {
    private SnackPile snackPile;
    private SnackMachine snackMachine;
    private int position;

    protected Slot() {
    }

    public Slot(final SnackMachine snackMachine, final int position) {
        this.snackMachine = snackMachine;
        this.position = position;
        this.snackPile = SnackPile.Empty;
    }

    public SnackMachine getSnackMachine() {
        return snackMachine;
    }

    public SnackPile getSnackPile() {
        return snackPile;
    }

    public void setSnackPile(final SnackPile snackPile) {
        this.snackPile = snackPile;
    }

    public int getPosition() {
        return position;
    }
}
