package edu.yu.parallel.model;

import java.util.Objects;

import edu.yu.parallel.Utils;

/**
 * Wraps a PlayerStats with advanced sabermetric metrics:
 * - wOBA (weighted On-Base Average)
 * - wRAA (weighted Runs Above Average)
 * - simpleWAR (simplified WAR = wRAA / 10)
 *
 * This class is immutable and created via the static factory method.
 * The raw and derived batting totals remain inside PlayerStats.
 */
public final class PlayerAnalytics {

    // Hardcoded weights used for calculating advanced offensive metrics such as wOBA
    private static final double W_BB = 0.69; // Weight for unintentional walks (BB)
    private static final double W_HBP = 0.72; // Weight for hit-by-pitch (HBP)
    private static final double W_1B = 0.89; // Weight for singles (1B)
    private static final double W_2B = 1.27; // Weight for doubles (2B)
    private static final double W_3B = 1.62; // Weight for triples (3B)
    private static final double W_HR = 2.10; // Weight for home runs (HR)

    // Underlying player-season statistics
    private final PlayerStats seasonStats;

    // Advanced metrics
    private final double wOBA;
    private final double wRAA;
    private final double simpleWAR;

    // Private constructor
    private PlayerAnalytics(
            PlayerStats seasonStats,
            double wOBA,
            double wRAA,
            double simpleWAR) {
        this.seasonStats = seasonStats;
        this.wOBA = wOBA;
        this.wRAA = wRAA;
        this.simpleWAR = simpleWAR;
    }

    /**
     * Factory method to compute PlayerAnalytics from a PlayerStats.
     */
    public static PlayerAnalytics of(PlayerStats playerStats,
            double seasonWeightedOBA) {

        Objects.requireNonNull(playerStats, "playerStats cannot be null");

        double woba = calcWeightedOBA(playerStats);
        double wraa = calcWRAA(playerStats, woba, seasonWeightedOBA);
        double war = calcSimpleWAR(wraa);

        return new PlayerAnalytics(playerStats, woba, wraa, war);
    }

    // -------- Private static calculation methods --------

    /**
     * Computes weighted OBA (On-Base Average) for a player-season.
     *
     * Formula:
     * wOBA = (wBB*BB + wHBP*HBP + w1B*1B + w2B*2B + w3B*3B + wHR*HR) / PA
     *
     * Returns 0 if PA <= 0.
     */
    private static double calcWeightedOBA(PlayerStats stats) {
        if (stats.getPA() <= 0) {
            return 0.0;
        }

        double numerator = W_BB * stats.getBB() +
                W_HBP * stats.getHBP() +
                W_1B * stats.getSingles() +
                W_2B * stats.get2B() +
                W_3B * stats.get3B() +
                W_HR * stats.getHR();

        return numerator / stats.getPA();
    }

    /**
     * Computes wRAA (Weighted Runs Above Average) for a player-season.
     *
     * Formula:
     * wRAA = ((wOBA - seasonWeightedOBA) / scale) * PA
     *
     * Uses the standard wOBA scale constant of 1.15.
     */
    private static double calcWRAA(PlayerStats stats, double playerWeightedOBA, double seasonWeightedOBA) {
        double wobaDiff = playerWeightedOBA - seasonWeightedOBA;

        // If no PA, wRAA is 0
        if (stats.getPA() <= 0) {
            return 0.0;
        }

        final double WOBA_SCALE = 1.15;

        return (wobaDiff / WOBA_SCALE) * stats.getPA();
    }

    /**
     * Computes simplified WAR from wRAA.
     * Note: This is a simplified version of WAR that only considers offensive contributions.
     * This will be skewered for longevity.
     *
     * Formula:
     * WAR = wRAA / 10
     */
    private static double calcSimpleWAR(double wraa) {
        return wraa / 10.0;
    }

    // ----------------------------------------------------------------------
    // Accessors
    // ----------------------------------------------------------------------

    public PlayerStats getPlayerStats() {
        return seasonStats;
    }

    public String getPlayerID() {
        return seasonStats.getPlayerID();
    }

    public int getYear() {
        return seasonStats.getyear();
    }

    public int getPA() {
        return seasonStats.getPA();
    }

    public double getWOBA() {
        return wOBA;
    }

    public double getWRAA() {
        return wRAA;
    }

    public double getSimpleWAR() {
        return simpleWAR;
    }

    @Override
    public String toString() {
        return String.format(
                "%s %d wOBA=%s wRAA=%.2f WAR=%.2f",
                getPlayerID(), getYear(), Utils.statFmt(wOBA), wRAA, simpleWAR);
    }
}
