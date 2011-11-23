package com.github.joelittlejohn.arduieensy.extremefeedback;

import java.net.MalformedURLException;
import java.net.URL;

public class FeedbackDaemon {

    public static void main(String[] args) {

        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: FeedbackDaemon <job url>");
        }

        String url = args[0];

        try {
            Poller poller = new Poller(new Job(new URL(url)), new Orb(new Teensy()));
            poller.start();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Couldn't parse poll url: " + url, e);
        }
    }

}
