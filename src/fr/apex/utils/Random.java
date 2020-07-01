package fr.apex.utils;

public class Random {
    public static double randD(double min, double max) {
        java.util.Random rand = new java.util.Random();
        return (rand.nextDouble()*(max - min)) + min;
    }

    public static double randD(double max) {
        java.util.Random rand = new java.util.Random();
        return (rand.nextDouble()*(max - 0)) + 0;
    }
}
