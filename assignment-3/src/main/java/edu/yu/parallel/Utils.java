package edu.yu.parallel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Utils {
    private final static Logger logger = LogManager.getLogger("main");

    public static String statFmt(double value) {
        String formatted = String.format("%.3f", value); // "0.359"
        return formatted.replaceFirst("^0", ""); // ".359"
    }

    public static Level captureLoggerLevel() {
        return logger.getLevel();
    }

    public static void disableMainLogger() {
        Configurator.setLevel("main", Level.OFF);
    }

    public static void setMainLoggerLevel(Level level) {
        Configurator.setLevel("main", level);
    }

    public static long benchmarkRunnable(Runnable task, boolean warmup) {
        Level originalLevel = captureLoggerLevel();

        try {
            // Suppress logging during benchmark
            setMainLoggerLevel(Level.OFF);
            if (warmup) {
                task.run(); // Warm-up run
            }

            // Collect nanosecond timings
            List<Long> runTimesNs = new ArrayList<>();

            for (int i = 0; i < 6; i++) {
                long start = System.nanoTime();
                task.run();
                long elapsedNs = System.nanoTime() - start;
                runTimesNs.add(elapsedNs);
            }

            // Sort and drop fastest 1 and slowest 2
            Collections.sort(runTimesNs);
            List<Long> middleRunTimesNs = runTimesNs.subList(1, 4); // indices 1, 2, 3

            // Average in nanoseconds
            long avgNs = (long) middleRunTimesNs.stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);

            return avgNs / 1_000_000; // Convert to milliseconds

        } finally {
            setMainLoggerLevel(originalLevel); // Restore original log level
        }
    }

    public static double cosineSimilarity(double[] vecA, double[] vecB) {
        if (vecA.length != vecB.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        double dot = 0.0;
        double sumSqA = 0.0;
        double sumSqB = 0.0;

        for (int i = 0; i < vecA.length; i++) {
            double a = vecA[i];
            double b = vecB[i];
            dot += a * b;
            sumSqA += a * a;
            sumSqB += b * b;
        }

        if (sumSqA == 0.0 || sumSqB == 0.0) {
            return 0.0;
        }

        return dot / (Math.sqrt(sumSqA) * Math.sqrt(sumSqB));
    }
}
