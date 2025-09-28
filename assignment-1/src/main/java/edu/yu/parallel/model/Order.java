package edu.yu.parallel.model;

import edu.yu.parallel.util.IdGenerator;

/**
 * Represents an order placed by a trader.
 * Contains all necessary information about the order, including symbol, type,
 * quantity, price, trader, and status.
 */
public class Order {
    /**
     * Unique integer order ID for this order.
     */
    private final int orderId;

    /**
     * Symbol being traded (e.g., stock ticker).
     */
    private final String symbol;

    /**
     * Type of order: BUY or SELL.
     */
    private final OrderType orderType;

    /**
     * Number of shares/contracts for this order. Mutable.
     */
    private int quantity;

    /**
     * Price per unit for this order.
     */
    private final double price;

    /**
     * Trader who placed this order.
     */
    private final Trader trader;

    /**
     * Status of the order (e.g., NEW, FILLED, CANCELLED). Mutable.
     */
    private OrderStatus status;

    /**
     * Factory method to create an Order with a generated order ID and default
     * status NEW.
     *
     * @param idGen     IdGenerator instance for generating unique order IDs
     * @param symbol    Symbol being traded
     * @param orderType Type of order (BUY or SELL)
     * @param quantity  Number of shares/contracts
     * @param price     Price per unit
     * @param trader    Trader who placed the order
     * @return a new Order instance
     */
    public static Order createOrder(IdGenerator idGen, String symbol, OrderType orderType,
            int quantity, double price, Trader trader) {
        int orderId = idGen.nextOrderId();
        return new Order(orderId, symbol, orderType, quantity, price, trader, OrderStatus.NEW);
    }

    /**
     * Private constructor for Order. Use createOrder to instantiate.
     *
     * @param orderId   Unique integer order ID
     * @param symbol    Symbol being traded
     * @param orderType Type of order (BUY or SELL)
     * @param quantity  Number of shares/contracts
     * @param price     Price per unit
     * @param trader    Trader who placed the order
     * @param status    Initial status of the order
     */
    private Order(int orderId, String symbol, OrderType orderType, int quantity, double price, Trader trader,
            OrderStatus status) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.trader = trader;
        this.status = status;
    }

    /**
     * Gets the unique order ID for this order.
     * 
     * @return orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Gets the symbol being traded.
     * 
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the type of order (BUY or SELL).
     * 
     * @return orderType
     */
    public OrderType getOrderType() {
        return orderType;
    }

    /**
     * Gets the quantity for this order.
     * 
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity for this order.
     * 
     * @param quantity new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the status of this order (e.g., NEW, FILLED, CANCELLED).
     * 
     * @return status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this order.
     * 
     * @param status new status
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Gets the price per unit for this order.
     * 
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the trader who placed this order.
     * 
     * @return trader
     */
    public Trader getTrader() {
        return trader;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", symbol='" + symbol + '\'' +
                ", orderType=" + orderType +
                ", quantity=" + quantity +
                ", price=" + price +
                ", trader=" + trader +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order other = (Order) obj;
        return orderId == other.orderId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderId);
    }
}