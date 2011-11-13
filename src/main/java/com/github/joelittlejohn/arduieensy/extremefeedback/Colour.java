package com.github.joelittlejohn.arduieensy.extremefeedback;

public enum Colour {

    RED(), GREEN(), BLUE(), YELLOW(RED, GREEN), CYAN(BLUE, GREEN), MAGENTA(RED, BLUE);

    private final Colour[] componentColours;

    private Colour(Colour... componentColours) {
        if (componentColours.length == 0) {
            this.componentColours = new Colour[] { this };
        } else {
            this.componentColours = componentColours;
        }
    }

    public Colour[] getComponentColours() {
        return componentColours;
    }

}
