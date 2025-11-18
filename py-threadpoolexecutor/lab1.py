import time
import random
from concurrent.futures import ThreadPoolExecutor

# Simulated API call - DO NOT MODIFY
def fetch_stock_price(symbol):
    """Simulate API call with 2-second delay"""
    print(f"Fetching {symbol}...")
    time.sleep(2)  # Simulate network delay
    price = random.uniform(100, 500)
    print(f"Got {symbol}: ${price:.2f}")
    return (symbol, price)

# Stock symbols to fetch
stocks = ["AAPL", "GOOGL", "MSFT", "AMZN", "TSLA"]

# TODO: Implement sequential version first (for comparison)
def fetch_sequential(stocks):
    """Fetch all stock prices sequentially"""
    results = []
    # YOUR CODE HERE
    pass

# TODO: Implement concurrent version with ThreadPoolExecutor
def fetch_concurrent(stocks):
    """Fetch all stock prices concurrently"""
    results = []
    # YOUR CODE HERE
    # 1. Create ThreadPoolExecutor with max_workers=5
    # 2. Use submit() to submit each stock
    # 3. Collect futures in a list
    # 4. Call result() on each future
    pass

if __name__ == "__main__":
    # Test sequential version
    print("=== Sequential Execution ===")
    start = time.time()
    seq_results = fetch_sequential(stocks)
    seq_time = time.time() - start
    print(f"Sequential time: {seq_time:.2f} seconds\n")
    
    # Test concurrent version
    print("=== Concurrent Execution ===")
    start = time.time()
    conc_results = fetch_concurrent(stocks)
    conc_time = time.time() - start
    print(f"Concurrent time: {conc_time:.2f} seconds")
    
    # Compare
    speedup = seq_time / conc_time
    print(f"\nSpeedup: {speedup:.2f}x faster!")
