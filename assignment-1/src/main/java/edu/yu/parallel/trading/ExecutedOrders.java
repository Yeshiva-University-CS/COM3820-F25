package edu.yu.parallel.trading;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.parallel.model.Execution;

/**
 * Thread-safe manager for all executed orders in the trading system.
 * This class is responsible for recording, storing, and providing access to
 * allorder executions.
 * You must implement proper synchronization to ensure thread safety for
 * concurrent access and updates.
 *
 * Design Instructions:
 * - Choose appropriate data structures for storing executions and for fast
 * lookup by symbol.
 * - Implement thread-safe methods for recording and retrieving executions
 * - Ensure that statistics (count, volume) are accurate under concurrent
 * updates.
 * - Optimize getLastExecution for fast retrieval by symbol.
 */
public class ExecutedOrders {
    private final static Logger logger = LogManager.getLogger(ExecutedOrders.class);

    /**
     * Records a completed order execution.
     * Must be thread-safe for concurrent updates.
     * 
     * @param execution the execution to record
     * @throws IllegalArgumentException if execution is null
     */
    public void recordExecution(Execution execution) {
        // Implementation goes here
    }

    /**
     * Returns the most recent execution for the given symbol, or null if none
     * exist.
     * Should be optimized for fast lookup by symbol.
     * 
     * @param symbol the stock symbol
     * @return last execution for symbol, or null if none
     * @throws IllegalArgumentException if symbol is null
     */
    public Execution getLastExecution(String symbol) {
        return null; // Implementation goes here
    }

    /**
     * Prints statistics for executed orders:
     * 1) Total execution count
     * 2) Total volume
     * 3) For each symbol, execution count and volume
     * 
     * Volume calculation per execution:
     * - Volume = execution.getQuantity() [number of shares/contracts executed]
     * - Total volume: sum of quantities from all executions
     * - Per-symbol volume: sum of quantities for executions of that symbol
     * 
     * Note, you must call outputStatistics to print the results.
     * Results must be consistent even during concurrent updates.
     */
    public void printStatistics() {
        // Call to outputStatistics with dummy data
        outputStatistics(0L, 0L, Map.of(), Map.of());
    }

    /*
     * Helper method to output statistics in a formatted manner.
     * DO NOT MAKE ANY CHANGES TO THIS METHOD
     * YOU MUST CALL THIS METHOD FROM printStatistics()
     */
    private void outputStatistics(
            long totalCount,
            long totalVolume,
            Map<String, Integer> symbolCount,
            Map<String, Integer> symbolVolume) {
        logger.info("Total execution count: " + totalCount);
        logger.info("Total volume: " + totalVolume);
        logger.info("Per-symbol statistics:");
        for (String symbol : symbolCount.keySet()) {
            logger.info("  Symbol: " + symbol + ", Execution count: " + symbolCount.get(symbol)
                    + ", Volume: " + symbolVolume.get(symbol));
        }
    }
}