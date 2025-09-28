package edu.yu.parallel.trading;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.parallel.model.Order;

/**
 * Thread-safe queue for managing orders in the trading system.
 * Stores, retrieves, and monitors orders submitted by traders.
 * Ensures thread safety for concurrent submissions and retrievals.
 * Maintains FIFO order within the queue.
 * Provides statistics for monitoring.
 * 
 * Requirements:
 * - The queue must support a maximum capacity.
 * - Orders must be in the NEW state when added.
 * - If the queue is full, addOrder must not accept new orders until space is available.
 * - If the queue is empty, getNextOrder must not return until an order is available.
 * - Cancelling all pending orders must set their state to CANCELLED and return them.
 * - Statistics must be consistent during concurrent updates.
 */
public class OrderQueue {
        private final static Logger logger = LogManager.getLogger(OrderQueue.class);

    /**
     * The maximum capacity of the queue. If not set, defaults to Integer.MAX_VALUE.
     */
    private final int capacity;

    /**
     * Constructs an OrderQueue with unlimited capacity (Integer.MAX_VALUE).
     */
    public OrderQueue() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Constructs an OrderQueue with the specified capacity.
     * 
     * @param capacity the maximum number of orders the queue can hold
     * @throws IllegalArgumentException if capacity is not positive
     */
    public OrderQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    /**
     * Returns the maximum capacity of the queue.
     * 
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Adds a new order to the queue.
     * The order must be in the NEW state when added.
     * If the queue is full, this method must block until space is available.
     * 
     * @param order the order to add
     * @throws IllegalArgumentException if the order is not in the NEW state
     * @throws InterruptedException     if the thread is interrupted while waiting for space
     * @throws IllegalStateException    if the queue has been shut down
     */
    public void addOrder(Order order) {
        // Must implement
    }

    /**
     * Retrieves and removes the next order to process from the queue.
     * If the queue is empty, this method must not return until an order is available.
     * 
     * @return the next order to process
     * @throws InterruptedException if the thread is interrupted while waiting for
     *                              an order
     */
    public Order getNextOrder() {
        return null; // Must implement
    }

    /**
     * Shuts down the queue so that no new orders can be added.
     * Cancels all pending orders in the queue and returns them as a list.
     * Sets the state of each cancelled order to CANCELLED.
     * 
     * @return a list of all cancelled orders
     */
    public List<Order> shutdown() {
        return List.of(); // Must implement
    }

    /**
     * Prints statistics for the queue:
     * 1) Total order count
     * 2) Pending order count
     * 
     * You must call outputStatistics to print the results.
     * Results must be consistent during concurrent updates.
     */
    public void printStatistics() {
        // Call to outputStatistics with dummy data
        outputStatistics(0L, 0L);
    }

    /*
     * Helper method to output statistics in a formatted manner.
     * DO NOT MAKE ANY CHANGES TO THIS METHOD
     * YOU MUST CALL THIS METHOD FROM printStatistics()
     */
    private void outputStatistics(long totalCount, long totalPending) {
        logger.info("Total order count: " + totalCount);
        logger.info("Total pending count: " + totalPending);
    }
}