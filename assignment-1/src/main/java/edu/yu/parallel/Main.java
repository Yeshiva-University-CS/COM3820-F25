package edu.yu.parallel;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.parallel.model.MarketData;
import edu.yu.parallel.model.Order;
import edu.yu.parallel.model.Trader;
import edu.yu.parallel.threads.TradingThreadFactory;
import edu.yu.parallel.trading.ExecutedOrders;
import edu.yu.parallel.trading.OrderGenerator;
import edu.yu.parallel.trading.OrderQueue;

/**
 * Main class to run the trading simulation.
 * Creates a TradingSystem with 5 traders and one MarketMaker for all stocks.
 * Runs the system for 20 seconds, printing execution statistics every 5
 * seconds.
 * After stopping, prints final trade info and count of canceled orders.
 */
public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        int numTraders = getNumberOfTraders("NUM_TRADERS", 2);

        List<Trader> traders = new ArrayList<>();
        for (int i = 1; i <= numTraders; i++) {
            traders.add(new Trader("Trader" + i));
        }

        int maxQueueCapacity = Integer.MAX_VALUE;
        int minOrderQuantity = 10;
        int maxOrderQuantity = 100;

        MarketData marketData = new MarketData();
        OrderQueue orderQueue = new OrderQueue(maxQueueCapacity);
        ExecutedOrders executedOrders = new ExecutedOrders();
        OrderGenerator orderGenerator = new OrderGenerator(marketData, executedOrders,
                minOrderQuantity, maxOrderQuantity);

        // Create and start the trading system
        TradingSystem system = new TradingSystem(
                traders,
                marketData,
                orderGenerator,
                orderQueue,
                executedOrders,
                new TradingThreadFactory());

        long startTime = System.currentTimeMillis();

        logger.info("Starting trading system...");
        system.start();
        logger.info("Trading system started in " + (System.currentTimeMillis() - startTime) + " ms");

        long duration = 20_000; // 21 seconds
        long interval = 5_000; // 5 seconds

        List<Order> canceledOrders = new ArrayList<>();
        Thread stopperThread = new Thread(() -> {
            try {
                Thread.sleep(duration);
                if (system.isRunning()) {
                    logger.info("Stopping trading system after " + (duration / 1000) + " seconds...");
                    long now = System.currentTimeMillis();
                    var orders = system.stop();
                    logger.info("Trading system stopped in " + (System.currentTimeMillis() - now) + " ms");
                    canceledOrders.addAll(orders);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread statsThread = new Thread(() -> {
            try {
                while (system.isRunning()) {
                    Thread.sleep(interval);
                    logger.info("=== Execution Status ===");
                    orderQueue.printStatistics();
                    executedOrders.printStatistics();

                    logger.info("=== Trader Details ===");
                    for (Trader trader : traders) {
                        logger.info("=== Trader: " + trader.getTraderId());
                        trader.printStatistics();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        statsThread.start();
        stopperThread.start();

        stopperThread.join();
        statsThread.join();

        logger.info("=== Execution Status ===");
        orderQueue.printStatistics();
        logger.info("Canceled orders: " + canceledOrders.size());
        executedOrders.printStatistics();

        logger.info("=== Trader Details ===");

        for (Trader trader : traders) {
            logger.info("=== Trader: " + trader.getTraderId());
            trader.printStatistics();
        }

        logger.info("Simulation complete.");
    }

    /**
     * Gets the number of traders from an environment variable, or returns the
     * default if not set or invalid.
     * 
     * @param envVarName   the environment variable name
     * @param defaultValue the default value to use if not set or invalid
     * @return the number of traders
     */
    private static int getNumberOfTraders(String envVarName, int defaultValue) {
        String envValue = System.getenv(envVarName);
        if (envValue != null) {
            try {
                return Integer.parseInt(envValue);
            } catch (NumberFormatException e) {
                logger.warn("Invalid " + envVarName + " value: " + envValue + ". Using default: " + defaultValue);
            }
        }
        return defaultValue;
    }
}
