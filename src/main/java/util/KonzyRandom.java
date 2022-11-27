package util;

import java.util.Random;

public class KonzyRandom {
    private static final Random random = new Random();
    private static final double d = random.nextDouble();

    public static void main(String[] args) {
        int samples = 100;
        double accum = 0;
        for (int i = 0; i < samples; i++) {
            double val = randomWait();
            accum += val;
            System.out.println(val);
        }
        System.out.println(accum / samples);
    }

    static public double nextSkewedBoundedDouble(double min, double max) {
        return nextSkewedBoundedDouble(min, max, 1, d);
    }

    static public double nextSkewedBoundedDouble(double min, double max, double skew, double bias) {
        double range = max - min;
        double mid = min + range / 2.0;
        double unitGaussian = random.nextGaussian();
        double biasFactor = Math.exp(bias);
        return mid + (range * (biasFactor / (biasFactor + Math.exp(-unitGaussian / skew)) - 0.5));
    }

    static public double randomWait() {
        double max = 60 * 1000;
        double clamp = max * 0.9;
        double temp;
        do {
            temp = nextSkewedBoundedDouble(0,max, 0.05, -2.5);

        } while (temp > clamp);

        return temp;
    }

}
