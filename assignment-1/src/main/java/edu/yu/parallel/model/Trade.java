package edu.yu.parallel.model;

import edu.yu.parallel.util.IdGenerator;

/**
 * Represents a trade from the point of view of a trader.
 * Contains details about the executed order, counterparty, and trade direction.
 */
public class Trade {
    private final int tradeId;
    private final Execution orderExecution;
    private final String symbol;

    /**
     * Gets the unique trade ID for this trade.
     * 
     * @return tradeId
     */
    public int getTradeId() {
        return tradeId;
    }

    private final int quantity;
    private final double price;
    private final Trader trader;
    private final Trader counterparty;
    private final TradeDirection direction;
    private final long timestamp;

    public enum TradeDirection {
        BUY, SELL
    }

    /**
     * Factory method to create a BUY trade from an OrderExecution and an
     * IdGenerator.
     * 
     * @param execution the executed order pair
     * @param idGen     the global IdGenerator instance
     * @return a new Trade instance representing a BUY
     */
    public static Trade createBuyTrade(Execution execution, IdGenerator idGen) {
        Order buyOrder = execution.getBuyOrder();
        Order sellOrder = execution.getSellOrder();
        return new Trade(
                idGen.nextTradeId(),
                execution,
                execution.getSymbol(),
                execution.getQuantity(),
                execution.getPrice(),
                buyOrder.getTrader(),
                sellOrder.getTrader(),
                TradeDirection.BUY,
                execution.getTimestamp());
    }

    /**
     * Factory method to create a SELL trade from an OrderExecution and an
     * IdGenerator.
     * 
     * @param execution the executed order pair
     * @param idGen     the global IdGenerator instance
     * @return a new Trade instance representing a SELL
     */
    public static Trade createSellTrade(Execution execution, IdGenerator idGen) {
        Order buyOrder = execution.getBuyOrder();
        Order sellOrder = execution.getSellOrder();
        return new Trade(
                idGen.nextTradeId(),
                execution,
                execution.getSymbol(),
                execution.getQuantity(),
                execution.getPrice(),
                sellOrder.getTrader(),
                buyOrder.getTrader(),
                TradeDirection.SELL,
                execution.getTimestamp());
    }

    /**
     * Private constructor for Trade. Use factory methods to create instances.
     */
    private Trade(int tradeId, Execution orderExecution, String symbol, int quantity, double price,
            Trader trader, Trader counterparty, TradeDirection direction, long timestamp) {
        this.tradeId = tradeId;
        this.orderExecution = orderExecution;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.trader = trader;
        this.counterparty = counterparty;
        this.direction = direction;
        this.timestamp = timestamp;
    }

    /**
     * Gets the order execution ID associated with this trade.
     * 
     * @return order execution ID
     *         /**
     *         Gets the order execution for this trade.
     * @return orderExecution object
     */
    public Execution getOrderExecution() {
        return orderExecution;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Gets the trader for this trade.
     * 
     * @return trader
     */
    public Trader getTrader() {
        return trader;
    }

    /**
     * Gets the counterparty trader for this trade.
     * 
     * @return counterparty trader
     */
    public Trader getCounterparty() {
        return counterparty;
    }

    public TradeDirection getDirection() {
        return direction;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId=" + tradeId +
                ", orderExecution=" + orderExecution +
                ", symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", trader=" + trader +
                ", counterparty=" + counterparty +
                ", direction=" + direction +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trade other = (Trade) obj;
        return tradeId == other.tradeId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(tradeId);
    }
}
