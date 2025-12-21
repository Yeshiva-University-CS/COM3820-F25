package edu.yu.parallel.query;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.parallel.model.PlayerAnalytics;
import edu.yu.parallel.model.PlayerStats;

/**
 * QueryEngine Specification:
 *
 * All WAR calculations MUST treat seasons with fewer than 400 Plate Appearances
 * (PA < 400) as contributing ZERO WAR. That is:
 *
 * - If a PlayerAnalytics object represents a season with PA >= 400,
 *   include its SimpleWAR value.
 *
 * - If PA < 400, treat its SimpleWAR as 0 and do NOT count it toward
 *   career totals, decade totals, or 5-year window totals.
 *
 * Each query prints only the required output (no ranking numbers, no headers
 * except where specified for decades).
 */
public abstract class QueryEngine {
    protected final static Logger logger = LogManager.getLogger("main");
    protected final static double WAR_PA_THRESHOLD = 400.0;
    protected final static int MAX_SEASONS = 20;

    /** Maps playerID -> "FirstName LastName" */
    protected final Map<String, String> playerMap;

    /**
     * Constructs a QueryEngine with the raw player stats and a player name map.
     *
     * @param playerMap   mapping from playerID to "FirstName LastName"
     * @param playerStats list of per-season PlayerStats records
     */
    public QueryEngine(Map<String, String> playerMap) {
        this.playerMap = playerMap;
    }

    /**
     * Computes advanced metrics (wOBA, wRAA, SimpleWAR, etc.) for each
     * PlayerStats entry and returns a corresponding list of PlayerAnalytics.
     *
     * Implementations (sequential, ForkJoin, streams) may parallelize
     * this computation as they see fit.
     */
    public List<PlayerAnalytics> calculatePlayerAnalytics(List<PlayerStats> playerStats) {
        return List.of();
    }

    /**
     * Prints the top 10 career WAR leaders.
     * Input: list of PlayerAnalytics with advanced metrics already computed.
     *
     * Output format per line (no header, no numbering):
     * "%.2f FirstName LastName"
     */
    public void printTopTenCareerWAR(List<PlayerAnalytics> playerAnalytics) {
        logger.info(String.format("%.2f %s", 123.45, "Akiva Sacknovitz"));
    }

    /**
     * Prints the top 5 WAR players for each decade starting from 1900.
     *
     * Input: list of PlayerAnalytics with advanced metrics already computed.
     *
     * Output format:
     *
     * 1900-1909
     * "%.2f FirstName LastName"
     *
     * 1910-1919
     * "%.2f FirstName LastName"
     *
     * Output a blank line between decades.
     */
    public void printTopFiveWARByDecade(List<PlayerAnalytics> playerAnalytics) {
        logger.info(String.format("%d-%d", 1900, 1909));
        logger.info(String.format("%.2f %s", 123.45, "Akiva Sacknovitz"));
    }

    /**
    * Prints the top 10 highest consecutive 7-year WAR windows.
    * Each player may appear at most once: use that player's single best
    * 7-year window (highest WAR) when ranking.
     *
     * Input: list of PlayerAnalytics with advanced metrics already computed.
     *
     * Output format per line:
     * "%.2f FirstName LastName (startYear-endYear)"
     */
    public void printTopTenConsecutiveSevenYearWarWindow(List<PlayerAnalytics> playerAnalytics) {
        logger.info(String.format("%.2f %s (%d-%d)", 123.45, "Akiva Sacknovitz", 2000, 2006));
    }


    /**
     * Prints the top 10 best 7-year WAR windows.
     * This is the total WAR of the best seven years for each player.
     *
     * Input: list of PlayerAnalytics with advanced metrics already computed.
     *
     * Output format per line:
     * "%.2f FirstName LastName (firstYear-lastYear)"
     */
    public void printTopTenBestSevenYearWar(List<PlayerAnalytics> playerAnalytics) {
        logger.info(String.format("%.2f %s (%d-%d)", 123.45, "Akiva Sacknovitz", 2000, 2006));
    }


    /**
     * Prints the top 10 players with total WAR with those who are most similar to them 
     * baseed on based on cosine similarity of their WAR "career-shape" vectors.
     *
     * Implementation details:
     * - Restrict to the top 1000 players by career WAR
     * - Restrict to players with at least 10 seasons played
     * - Restrict to players who played after 1901
     
     *   (using the PA >= WAR_PA_THRESHOLD rule when summing).
     * - For each of those players, build a fixed-length career vector:
     *     double[] vec = buildCareerVector(playerId, allSeasons, MAX_SEASONS, WAR_THRESHOLD);
     *   where:
     *     - MAX_SEASONS is a fixed constant (e.g., 20),
     *     - WAR_PA_THRESHOLD is the minimum PA for a season for its WAR to be included
     *       in the vector; seasons below the threshold are ignored when
     *       filling, and the rest of the vector is padded with 0.0.
     * - Compute cosine similarity for every unordered pair of these players.
     * - Rank pairs by similarity (descending) and print the top 10.
     *
     * Input: list of PlayerAnalytics with advanced metrics already computed.
     *
     * Output format per line:
     *   "%.4f FirstNameA LastNameA (First YR) <-> FirstNameB LastNameB (First YR" 
     *
     * Example:
     *   0.9734 Babe Ruth (1914) <-> Ted Williams (1941)
     */
    public void printTopTenMostSimilarCareers(List<PlayerAnalytics> playerAnalytics) {
        logger.info(String.format("%.4f %s (%d) <-> %s (%d)", 0.9734, "Babe Ruth", 1914, "Ted Williams", 1941));
    }

}
