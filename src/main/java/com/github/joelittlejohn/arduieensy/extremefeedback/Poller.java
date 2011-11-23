package com.github.joelittlejohn.arduieensy.extremefeedback;

import static com.github.joelittlejohn.arduieensy.extremefeedback.util.ThreadUtils.*;

import java.util.concurrent.TimeUnit;

public class Poller {

    private static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(30);

    private final Job job;
    private final Orb orb;

    public Poller(Job job, Orb orb) {
        this.job = job;
        this.orb = orb;
    }

    public void start() {

        while (true) {

            try {
                JobState jobState = job.getState();

                switch (jobState) {
                    case SUCCESS:
                        orb.setColour(Colour.BLUE);
                        break;
                    case UNSTABLE:
                        orb.setColour(Colour.YELLOW);
                        break;
                    case FAILURE:
                        orb.setColour(Colour.RED);
                        break;
                }
            } catch (Exception e) {
                // log any exception but don't allow polling to cease completely
                e.printStackTrace(System.err);
            }

            sleepQuietly(POLL_INTERVAL);
        }

    }

}
