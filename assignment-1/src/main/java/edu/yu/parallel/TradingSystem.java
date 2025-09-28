package edu.yu.parallel;

import java.util.List;

import edu.yu.parallel.model.MarketData;
import edu.yu.parallel.model.Order;
import edu.yu.parallel.model.Trader;
import edu.yu.parallel.threads.TradingThreadFactory;
import edu.yu.parallel.trading.ExecutedOrders;
import edu.yu.parallel.trading.OrderGenerator;
import edu.yu.parallel.trading.OrderQueue;

/**
 * TradingSystem coordinates traders, market data, order generation, and
 * execution.
 */
public class TradingSystem {
    private boolean running = false;

    /**
     * Constructs a TradingSystem with all required dependencies.
     *
     * @param traders        List of trader objects to manage (must not be null)
     * @param marketData     Market data for the trading system (must not be null)
     * @param orderGenerator Generates orders for traders (must not be null)
     * @param orderQueue     The order queue for order processing (must not be null)
     * @param executedOrders Storage for completed executions (must not be null)
     * @param threadFactory  Factory to create trader and market maker threads (must not be null)
     * @throws IllegalArgumentException if any argument is null or if stockSymbols is empty
     */
    public TradingSystem(
            List<Trader> traders,
            MarketData marketData,
            OrderGenerator orderGenerator,
            OrderQueue orderQueue,
            ExecutedOrders executedOrders,
            TradingThreadFactory threadFactory) {
    }

    /**
     * Starts the trading system.
     * Creates and starts all trader threads and market data thread(s).
     * This method must be called before trading begins.
     * 
     * @throws IllegalArgumentException if the system is already started
     */
    public void start() {
        this.running = true;
    }

    /**
     * Stops the trading system gracefully.
     * Interrupts all threads and waits for them to complete.
     * After stopping, no new trades or orders will be processed.
     * 
     * @return List of all orders that were canceled due to the stop operation
     * 
     * @throws IllegalArgumentException if the system is not started
     */
    public List<Order> stop() {
        this.running = false;
        return List.of();
    }

    /**
     * Checks if the trading system is currently running.
     * 
     * @return true if the system is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }
}
