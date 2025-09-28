package edu.yu.parallel.threads;

import java.util.List;

import edu.yu.parallel.model.Trader;
import edu.yu.parallel.trading.ExecutedOrders;
import edu.yu.parallel.trading.OrderGenerator;
import edu.yu.parallel.trading.OrderQueue;

public class TradingThreadFactory {

        /**
         * Creates a new trading thread for the given trader.
         *
         * @param trader the trader for this thread (must not be null)
         * @param orderGenerator the order generator to use (must not be null)
         * @param orderQueue the order queue to use (must not be null)
         * @return a new Thread for trading
         * @throws IllegalArgumentException if any argument is null
         */
        public Thread createTradingThread(
                        Trader trader,
                        OrderGenerator orderGenerator,
                        OrderQueue orderQueue) {
                throw new UnsupportedOperationException("not implemented yet");
        }

        /**
         * Creates a new market maker thread for the given stock symbol(s).
         *
         * @param stockSymbols list of stock symbols to make markets for (must not be null or empty)
         * @param orderQueue the order queue to use (must not be null)
         * @param executedOrders the executed orders tracker (must not be null)
         * @return a new Thread for market making
         * @throws IllegalArgumentException if any argument is null or if stockSymbols is empty
         */
        public Thread createMarketMakerThread(
                        List<String> stockSymbols,
                        OrderQueue orderQueue,
                        ExecutedOrders executedOrders) {
                throw new UnsupportedOperationException("not implemented yet");
        }
}
