package edu.yu.parallel.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Provides market data and price generation utilities for the trading system.
 * Maintains closing prices and generates realistic buy/sell prices based on
 * current market conditions.
 */
public class MarketData {
    /**
     * Map of stock symbols to their closing prices.
     */
    private final Map<String, Double> closingPrices;
    /**
     * Array of available stock symbols in the market.
     */
    private final String[] availableSymbols;
    /**
     * Random number generator for price simulation.
     */
    private final Random random;

    /**
     * Constructs a MarketData instance with predefined closing prices.
     */
    public MarketData() {
        this.random = new Random();

        // Initialize with realistic closing prices
        this.closingPrices = new HashMap<>();
        closingPrices.put("META", 334.50);
        closingPrices.put("AAPL", 189.25);
        closingPrices.put("AMZN", 145.75);
        closingPrices.put("NFLX", 485.30);
        closingPrices.put("MSFT", 378.90);
        closingPrices.put("GOOGL", 2875.40);

        this.availableSymbols = closingPrices.keySet().toArray(new String[0]);
    }

    /**
     * Returns a copy of all available stock symbols.
     * 
     * @return array of available symbols
     */
    public String[] getAvailableSymbols() {
        return availableSymbols.clone();
    }

    /**
     * Returns a random stock symbol from the available symbols.
     * 
     * @return random symbol
     */
    public String getRandomSymbol() {
        return availableSymbols[random.nextInt(availableSymbols.length)];
    }

    /**
     * Gets the closing price for a given symbol.
     * 
     * @param symbol the stock symbol
     * @return closing price
     * @throws IllegalArgumentException if symbol is unknown
     */
    public double getClosingPrice(String symbol) {
        Double price = closingPrices.get(symbol);
        if (price == null) {
            throw new IllegalArgumentException("Unknown symbol: " + symbol);
        }
        return price;
    }

    /**
     * Generates a realistic buy price based on a current market price.
     * Buyers are willing to pay at or slightly above the current market price.
     * 
     * @param symbol       the stock symbol (for validation)
     * @param currentPrice the current market price to base the buy price on
     * @return generated buy price
     * @throws IllegalArgumentException if symbol is unknown
     */
    public double generateBuyPrice(String symbol, double currentPrice) {
        validateSymbol(symbol);
        // Buyers willing to pay at or slightly above current market
        return currentPrice * (1.0 + random.nextDouble() * 0.02); // 0-2% above
    }

    /**
     * Generates a realistic sell price based on a current market price.
     * Sellers want to sell at or slightly below the current market price.
     * 
     * @param symbol       the stock symbol (for validation)
     * @param currentPrice the current market price to base the sell price on
     * @return generated sell price
     * @throws IllegalArgumentException if symbol is unknown
     */
    public double generateSellPrice(String symbol, double currentPrice) {
        validateSymbol(symbol);
        // Sellers want to sell at or slightly below current market
        return currentPrice * (0.98 + random.nextDouble() * 0.02); // -2% to 0%
    }

    /**
     * Validates that a symbol is known to this market data provider.
     * 
     * @param symbol the symbol to validate
     * @throws IllegalArgumentException if symbol is unknown
     */
    private void validateSymbol(String symbol) {
        if (!closingPrices.containsKey(symbol)) {
            throw new IllegalArgumentException("Unknown symbol: " + symbol);
        }
    }
}