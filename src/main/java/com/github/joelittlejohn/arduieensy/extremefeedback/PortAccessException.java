package com.github.joelittlejohn.arduieensy.extremefeedback;

public class PortAccessException extends RuntimeException {

    public PortAccessException(String message) {
        super(message);
    }

    public PortAccessException(Throwable cause) {
        super(cause);
    }

    public PortAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
