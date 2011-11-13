package com.github.joelittlejohn.arduieensy.extremefeedback;

public class Pin {

    public static final short MAX_VALUE = 255;
    public static final short MIN_VALUE = 0;

    private final int number;

    public Pin(int number) {
        this.number = number;
    }

    public void on() {
        this.setValue(MAX_VALUE);
    }

    public void off() {
        this.setValue(MIN_VALUE);
    }

    public void setValue(int value) {

    }

}
