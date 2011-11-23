package com.github.joelittlejohn.arduieensy.extremefeedback;

public class Pin {

    public static final short MAX_VALUE = 255;
    public static final short LOW_VALUE = 20;
    public static final short MIN_VALUE = 0;

    private final int number;

    public Pin(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

}
