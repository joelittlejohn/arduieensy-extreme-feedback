package com.github.joelittlejohn.arduieensy.extremefeedback.util;

public class ThreadUtils {

    public static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

}
