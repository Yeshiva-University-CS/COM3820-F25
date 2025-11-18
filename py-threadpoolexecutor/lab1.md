## Lab Exercise 1: Your First ThreadPoolExecutor

### Learning Objectives
- Create a ThreadPoolExecutor with context manager
- Submit tasks using submit()
- Retrieve results from Futures
- Compare concurrent vs sequential execution

### The Problem
You need to fetch stock prices from a simulated API. Each API call takes 2 seconds. 
You have 5 stocks to check. Sequential execution would take 10 seconds. 
Can you do it faster with ThreadPoolExecutor?

### Your Tasks
1. Implement `fetch_sequential()` - call `fetch_stock_price()` for each stock
2. Implement `fetch_concurrent()` - use ThreadPoolExecutor
3. Run the program and observe the timing difference

### ~Expected Output
```
=== Sequential Execution ===
Fetching AAPL...
Got AAPL: $234.56
Fetching GOOGL...
Got GOOGL: $345.67
...
Sequential time: 10.02 seconds

=== Concurrent Execution ===
Fetching AAPL...
Fetching GOOGL...
Fetching MSFT...
Fetching AMZN...
Fetching TSLA...
Got MSFT: $156.78
Got AAPL: $234.56
...
Concurrent time: 2.01 seconds

Speedup: 4.98x faster!
```

### Questions to Think About
- What happens if one stock API call fails?
- How would you handle a timeout (e.g., if API takes too long)?
- What's the optimal number of workers?
- Why isn't it exactly 5x faster?
