package util.breaking;

import util.KonzyRandom;

public class Break {
    double minDuration; // time of the break in minutes
    double maxDuration;
    double minInterval; // time between breaks in minutes
    double maxInterval;
    boolean shouldLogout;

    long breakStart = System.currentTimeMillis();
    long breakEnd;

    public Break(double minDuration, double maxDuration, double minInterval, double maxInterval, boolean shouldLogout) {
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.shouldLogout = shouldLogout;
        calculateNextBreak();
    }

    public void calculateNextBreak(){
        if (breakEnd > System.currentTimeMillis()) return;
        breakStart = System.currentTimeMillis() + nextDoubleInRange(minInterval, maxInterval);
        breakEnd = breakStart + nextDoubleInRange(minDuration, maxDuration);
    }

    private long nextDoubleInRange(double minVal, double maxVal) {
        return minutesToMillis(KonzyRandom.nextSkewedBoundedDouble(minVal, maxVal));
    }

    private long minutesToMillis(double minutes) {
        return (long)(minutes * 60 * 1000);
    }


}
