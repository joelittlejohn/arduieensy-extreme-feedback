package com.github.joelittlejohn.arduieensy.extremefeedback;

import java.util.HashMap;
import java.util.Map;

public class Orb {

    private static final int PIN_NUMBER_BLUE = 12;
    private static final int PIN_NUMBER_GREEN = 14;
    private static final int PIN_NUMBER_RED = 15;
    
    private final Teensy teensy;
    private final Map<Colour, Pin> pins = new HashMap<>();
    
    public Orb(Teensy teensy) {
        this.teensy = teensy;
        
        this.pins.put(Colour.RED, teensy.getPin(PIN_NUMBER_RED));
        this.pins.put(Colour.GREEN, teensy.getPin(PIN_NUMBER_GREEN));
        this.pins.put(Colour.BLUE, teensy.getPin(PIN_NUMBER_BLUE));
    }

    private Colour currentColour;

    public void setColour(Colour newColour) {
        if (this.currentColour != newColour) {
            if (this.currentColour != null ) {
                rampDownFromColour(currentColour);
            }
            throbToColour(newColour);
        } else {
            // set the current colour again anyway to help keep device sync'd with software state
            jumpToColour(newColour);
        }

        this.currentColour = newColour;
    }

    private void throbToColour(Colour newColour) {
        rampUpToColour(newColour);
        rampDownFromColour(newColour);
        rampUpToColour(newColour);
        rampDownFromColour(newColour);
        rampUpToColour(newColour);
    }

    private void rampUpToColour(Colour newColour) {
        turnOffAllPins();

        for (int i = Pin.MIN_VALUE; i <= Pin.MAX_VALUE; i++) {
            for (Colour colour : newColour.getComponentColours()) {
                teensy.write(pins.get(colour), i);
            }
        }
    }

    private void rampDownFromColour(Colour currentColour) {

        for (int i = Pin.MAX_VALUE; i >= Pin.MIN_VALUE; i--) {
            for (Colour colour : currentColour.getComponentColours()) {
                teensy.write(pins.get(colour), i);
            }
        }

    }

    private void jumpToColour(Colour newColour) {

        turnOffAllPins();

        for (Colour colour : newColour.getComponentColours()) {
            teensy.write(pins.get(colour), Pin.MAX_VALUE);
        }
    }

    private void turnOffAllPins() {
        for (Pin pin : pins.values()) {
            teensy.write(pin, Pin.MIN_VALUE);
        }
    }

    public static void main(String[] args) {
        
        Orb orb = new Orb(new Teensy());
        orb.setColour(Colour.RED);
        orb.setColour(Colour.GREEN);
        orb.setColour(Colour.BLUE);

        System.exit(0);
    }
    
}
