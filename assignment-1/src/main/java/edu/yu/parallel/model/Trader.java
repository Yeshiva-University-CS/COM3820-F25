package edu.yu.parallel.model;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a trader in the trading system.
 * Each trader has a unique identifier and can have trades associated with them.
 */
public class Trader {
    private final static Logger logger = LogManager.getLogger(Trader.class);

    /**
     * Unique identifier for this trader.
     */
    private final String traderId;

    /**
     * Constructs a Trader with the specified trader ID.
     * 
     * @param traderId the unique identifier for this trader
     */
    public Trader(String traderId) {
        this.traderId = traderId;
    }

    /**
     * Returns the unique identifier for this trader.
     * 
     * @return the trader ID
     */
    public String getTraderId() {
        return traderId;
    }

    /**
     * Adds a trade to this trader's trade list.
     * Not yet implemented.
     * 
     * @param trade the trade to add
     * @throws UnsupportedOperationException always, since not implemented
     */
    public void addTrade(Trade trade) {
        throw new UnsupportedOperationException("addTrade not implemented");
    }

    /**
     * Prints statistics for trade:
     * 1) Total trade count
     * 2) Total cash made/lost
     * 3) For each symbol, trade count, cash made/lost, position
     * 
     * Cash calculation per trade:
     * - BUY trades: cash = -(quantity * price) [money spent, negative]
     * - SELL trades: cash = +(quantity * price) [money received, positive]
     * - Total cash: sum of all individual trade cash flows
     * - Per-symbol cash: sum of cash flows for that symbol
     * 
     * Position calculation per trade:
     * - BUY trades: position += quantity [acquire shares]
     * - SELL trades: position -= quantity [sell shares, can go negative for short
     * positions]
     * 
     * Note, you must call outputStatistics to print the results.
     * Results must be consistent even during concurrent updates.
     */
    public void printStatistics() {
        // Call to outputStatistics with dummy data
        outputStatistics(0L, 0D, Map.of(), Map.of(), Map.of());
    }

    /*
     * Helper method to output statistics in a formatted manner.
     * DO NOT MAKE ANY CHANGES TO THIS METHOD
     * YOU MUST CALL THIS METHOD FROM printStatistics()
     */
    private void outputStatistics(
            long totalCount,
            double totalCash,
            Map<String, Integer> symbolCount,
            Map<String, Double> symbolCash,
            Map<String, Integer> symbolPosition) {
        logger.info("Total trade count: " + totalCount);
        logger.info("Total cash: " + totalCash);
        logger.info("Per-symbol statistics:");
        for (String symbol : symbolCount.keySet()) {
            logger.info("  Symbol: " + symbol + ", Trade count: " + symbolCount.get(symbol)
                    + ", Cash: " + symbolCash.get(symbol)
                    + ", Position: " + symbolPosition.get(symbol));
        }
    }

    @Override
    public String toString() {
        return "Trader{" +
                "traderId='" + traderId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Trader other = (Trader) obj;
        return traderId != null ? traderId.equals(other.traderId) : other.traderId == null;
    }

    @Override
    public int hashCode() {
        return traderId != null ? traderId.hashCode() : 0;
    }
}