package edu.yu.parallel.model;

/**
 * Represents the status of an order in the trading system.
 */
public enum OrderStatus {
    /**
     * Order just submitted.
     */
    NEW,
    /**
     * Order partially filled.
     */
    PARTIAL,
    /**
     * Order completely filled.
     */
    FILLED,
    /**
     * Order cancelled.
     */
    CANCELLED
}