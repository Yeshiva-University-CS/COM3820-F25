
package edu.yu.parallel.trading;

import java.util.List;

import edu.yu.parallel.model.Order;

/**
 * MarketMaker is responsible for matching incoming orders with resting orders
 * in the order book and producing executions.
 */
public class MarketMaker {

    /**
     * Process an incoming order by attempting to match it with resting orders.
     *
     * @param newOrder the incoming {@link Order} to process
     * 
     * @throws IllegalArgumentException if newOrder is null or not in a NEW state
     * @throws IllegalStateException    if the market maker has been shut down
     */
    public void processOrder(Order newOrder) {
        // Implementation goes here
    }

    /**
     * Shuts down the market maker, stopping all processing and cleaning up resources.
     * 
     * @return a list of all cancelled orders
     */
    public List<Order> shutdown() {
        return List.of(); // Implementation goes here
    }

}
