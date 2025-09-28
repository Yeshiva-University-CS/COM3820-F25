package edu.yu.parallel.util;

/**
 * Global singleton for generating unique integer trade IDs and order IDs.
 */
public class IdGenerator {
    private static final IdGenerator INSTANCE = new IdGenerator();
    private int tradeIdCounter = 0;
    private int orderIdCounter = 0;
    private int executionIdCounter = 0;

    private IdGenerator() {}

    /**
     * Gets the singleton instance of IdGenerator.
     * @return IdGenerator instance
     */
    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * Generates the next unique trade ID as an integer.
     * @return next trade ID
     */
    public int nextTradeId() {
        return ++tradeIdCounter;
    }

    /**
     * Generates the next unique execution ID as an integer.
     * @return next execution ID
     */
    public int nextExecutionId() {
        return ++executionIdCounter;
    }

    /**
     * Generates the next unique order ID as an integer.
     * @return next order ID
     */
    public int nextOrderId() {
        return ++orderIdCounter;
    }
}
