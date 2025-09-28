package edu.yu.parallel.trading;

import java.util.Random;

import edu.yu.parallel.model.Execution;
import edu.yu.parallel.model.MarketData;
import edu.yu.parallel.model.Order;
import edu.yu.parallel.model.OrderType;
import edu.yu.parallel.model.Trader;
import edu.yu.parallel.util.IdGenerator;

/**
 * Class responsible for generating orders based on current market data and
 * execution history.
 */
public class OrderGenerator {
    /**
     * Market data utility for accessing symbols and prices.
     */
    private final MarketData marketData;

    /**
     * Tracks executed orders to determine current market prices.
     */
    private final ExecutedOrders executedOrders;

    /**
     * Minimum quantity allowed for generated orders.
     */
    private final int minQuantity;

    /**
     * Maximum quantity allowed for generated orders.
     */
    private final int maxQuantity;

    /**
     * Random number generator for randomizing order attributes.
     */
    private final Random random = new Random();

    /**
     * Constructs an OrderGenerator with the specified market data, executed orders,
     * and quantity bounds.
     * 
     * @param marketData     the market data utility
     * @param executedOrders the executed orders tracker
     * @param minQuantity    minimum quantity for orders
     * @param maxQuantity    maximum quantity for orders
     */
    public OrderGenerator(MarketData marketData, ExecutedOrders executedOrders,
            int minQuantity, int maxQuantity) {
        this.marketData = marketData;
        this.executedOrders = executedOrders;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    /**
     * Generates a new order for the given trader using random attributes and
     * current market prices.
     * 
     * @param trader the trader placing the order
     * @return a newly created Order object
     */
    public Order generateOrder(Trader trader) {
        String symbol = marketData.getRandomSymbol();
        int quantity = random.nextInt(maxQuantity - minQuantity + 1) + minQuantity;
        OrderType orderType = random.nextBoolean() ? OrderType.BUY : OrderType.SELL;
        double price;
        if (orderType == OrderType.BUY) {
            price = generateBidPrice(symbol);
        } else {
            price = generateAskPrice(symbol);
        }
        return Order.createOrder(IdGenerator.getInstance(), symbol, orderType, quantity, price, trader);
    }

    /**
     * Gets the current market price for the specified symbol.
     * Uses the last execution price if available, otherwise falls back to the
     * closing price.
     * 
     * @param symbol the symbol to look up
     * @return the current market price
     */
    private double getCurrentMarketPrice(String symbol) {
        Execution lastExecution = executedOrders.getLastExecution(symbol);
        if (lastExecution != null) {
            return lastExecution.getPrice();
        }
        return marketData.getClosingPrice(symbol);
    }

    /**
     * Generates a realistic bid price for the specified symbol based on current
     * market price.
     * 
     * @param symbol the symbol to generate a bid price for
     * @return a bid price
     */
    private double generateBidPrice(String symbol) {
        double currentPrice = getCurrentMarketPrice(symbol);
        return marketData.generateBuyPrice(symbol, currentPrice);
    }

    /**
     * Generates a realistic ask price for the specified symbol based on current
     * market price.
     * 
     * @param symbol the symbol to generate an ask price for
     * @return an ask price
     */
    private double generateAskPrice(String symbol) {
        double currentPrice = getCurrentMarketPrice(symbol);
        return marketData.generateSellPrice(symbol, currentPrice);
    }
}