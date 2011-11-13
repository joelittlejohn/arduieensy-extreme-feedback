package com.github.joelittlejohn.arduieensy.extremefeedback;

import java.util.HashMap;
import java.util.Map;

public class Orb {

    private static Map<Colour, Pin> PINS = new HashMap<>();
    static {
        PINS.put(Colour.RED, new Pin(12));
        PINS.put(Colour.GREEN, new Pin(15));
        PINS.put(Colour.BLUE, new Pin(14));
    }

    private Colour currentColour;

    public void setColour(Colour newColour) {
        if (this.currentColour != newColour) {
            rampDownFromColour(currentColour);
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
                PINS.get(colour).setValue(i);
            }
        }
    }

    private void rampDownFromColour(Colour newColour) {
        turnOffAllPins();
        jumpToColour(newColour);

        for (int i = Pin.MAX_VALUE; i >= Pin.MIN_VALUE; i--) {
            for (Colour colour : newColour.getComponentColours()) {
                PINS.get(colour).setValue(i);
            }
        }

    }

    private void jumpToColour(Colour newColour) {

        turnOffAllPins();

        for (Colour colour : newColour.getComponentColours()) {
            PINS.get(colour).on();
        }
    }

    private void turnOffAllPins() {
        for (Pin pin : PINS.values()) {
            pin.off();
        }
    }

}
