package com.github.joelittlejohn.arduieensy.extremefeedback;

public class Poller {

    private static final long FIVE_MINUTES = 1000 * 60 * 5;

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

                Thread.sleep(FIVE_MINUTES);
            } catch (Exception e) {
                // log any exception but don't allow polling to cease completely
                e.printStackTrace(System.err);
            }
        }

    }

}
