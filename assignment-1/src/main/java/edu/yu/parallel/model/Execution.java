package edu.yu.parallel.model;

import edu.yu.parallel.util.IdGenerator;

/**
 * Represents a single execution of an order in the trading system.
 * Provides details about the executed quantity, price, involved orders, and
 * counterparties.
 */
public class Execution {
    /**
     * Unique identifier for this execution.
     */
    private final int executionId;

    /**
     * The buy order that was matched in this execution.
     */
    private final Order buyOrder;

    /**
     * The sell order that was matched in this execution.
     */
    private final Order sellOrder;

    /**
     * Symbol being traded (derived from orders).
     */
    private final String symbol;

    /**
     * Quantity executed (may be partial fill).
     */
    private final int quantity;

    /**
     * Price at which the execution occurred.
     */
    private final double price;

    /**
     * Timestamp when this execution occurred.
     */
    private final long timestamp;

    /**
     * Factory method to create an Execution with a generated execution ID.
     *
     * @param idGen     IdGenerator instance for generating unique execution IDs
     * @param buyOrder  The buy order being matched
     * @param sellOrder The sell order being matched
     * @param quantity  The quantity being executed (must be positive)
     * @param price     The execution price
     * @return a new Execution instance
     * @throws IllegalArgumentException if orders have different symbols, quantity
     *                                  is non-positive,
     *                                  buyOrder is not BUY type, or sellOrder is
     *                                  not SELL type
     */
    public static Execution createExecution(IdGenerator idGen, Order buyOrder, Order sellOrder,
            int quantity, double price) {
        // Validation
        if (!buyOrder.getSymbol().equals(sellOrder.getSymbol())) {
            throw new IllegalArgumentException("Buy and sell orders must have the same symbol");
        }
        if (buyOrder.getOrderType() != OrderType.BUY) {
            throw new IllegalArgumentException("Buy order must be BUY type");
        }
        if (sellOrder.getOrderType() != OrderType.SELL) {
            throw new IllegalArgumentException("Sell order must be SELL type");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        int executionId = idGen.nextExecutionId();
        long timestamp = System.currentTimeMillis();

        return new Execution(executionId, buyOrder, sellOrder, buyOrder.getSymbol(),
                quantity, price, timestamp);
    }

    /**
     * Private constructor for Execution. Use createExecution to instantiate.
     *
     * @param executionId Unique execution ID
     * @param buyOrder    The buy order
     * @param sellOrder   The sell order
     * @param symbol      The symbol being traded
     * @param quantity    The executed quantity
     * @param price       The execution price
     * @param timestamp   The execution timestamp
     */
    private Execution(int executionId, Order buyOrder, Order sellOrder, String symbol,
            int quantity, double price, long timestamp) {
        this.executionId = executionId;
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    /**
     * Gets the unique execution ID for this order execution.
     * 
     * @return execution ID
     */
    public int getExecutionId() {
        return executionId;
    }

    /**
     * Gets the buy order associated with this execution.
     * 
     * @return buy order
     */
    public Order getBuyOrder() {
        return buyOrder;
    }

    /**
     * Gets the sell order associated with this execution.
     * 
     * @return sell order
     */
    public Order getSellOrder() {
        return sellOrder;
    }

    /**
     * Gets the symbol for the executed order (e.g., stock ticker).
     * 
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the quantity executed in this execution.
     * 
     * @return executed quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the price at which this execution occurred.
     * 
     * @return execution price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the timestamp when this execution occurred.
     * 
     * @return execution timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string representation of this execution.
     * 
     * @return formatted execution details
     */
    @Override
    public String toString() {
        return String.format("Execution[id=%d, symbol=%s, qty=%d, price=%.2f, buyer=%s, seller=%s]",
                executionId, symbol, quantity, price,
                buyOrder.getTrader().getTraderId(),
                sellOrder.getTrader().getTraderId());
    }

    /**
     * Checks equality based on execution ID.
     * 
     * @param obj the object to compare
     * @return true if equal execution IDs
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Execution execution = (Execution) obj;
        return executionId == execution.executionId;
    }

    /**
     * Returns hash code based on execution ID.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(executionId);
    }
}