# Assignment #1: Multi-threaded Trading System

## COM 3820 - Parallel Programming

### Overview

In this assignment, you will implement a high-throughput, multi-threaded trading system simulation. The system simulates multiple traders placing orders concurrently while one or more market maker process these orders to generate trades. Your goal is to maximize system throughput while ensuring thread safety and data consistency.

### Learning Objectives

By completing this assignment, you will:
- Implement thread-safe data structures using fundamental Java concurrency utilities
- Design a multi-producer, single- or mulit-consumer system with optimal throughput
- Coordinate concurrent access to shared resources (market data, statistics, trade records)
- Apply synchronization mechanisms to prevent race conditions
- Understand the performance implications of different synchronization strategies
- Practice proper resource management and graceful shutdown procedures

### System Architecture

The trading system consists of several key components:

#### Core Components
- **TradingSystem**: Main orchestrator that manages all threads and components
- **OrderQueue**: Thread-safe queue that stores pending orders from traders
- **MarketMaker**: Processes orders from the queue and generates executions
- **Trader**: Individual trading entities (data models for traders)
- **OrderGenerator**: Utility that creates realistic trading orders for traders
- **ExecutedOrders**: Thread-safe storage for completed executions
- **MarketData**: Utility object that provides current market prices and generates realistic order prices

#### Threading Model
- **Trader Threads**: Multiple threads (configurable via `NUM_TRADERS` environment variable, default: 2) created by `TradingThreadFactory` that use `OrderGenerator` to continuously generate and submit orders on behalf of `Trader` objects
- **Market Maker Thread(s)**: One or more threads that process orders from the queue and execute trades

### Implementation Requirements

#### 1. TradingSystem Implementation

Implement the `start()` and `stop()` methods in `TradingSystem.java`:

**Core Requirements:**
- Create and start trader threads using the provided `TradingThreadFactory`
- Create and start the market maker thread
- Implement proper lifecycle management (prevent double-start, validate system state)
- Handle thread creation failures gracefully
- Coordinate graceful shutdown by calling shutdown methods on OrderQueue and MarketMaker
- Wait for all threads to complete with reasonable timeouts
- Return all canceled orders from the shutdown process

**Implementation Notes:**
- All method specifications are detailed in the JavaDoc comments
- Consider thread lifecycle management and proper resource cleanup
- Ensure system can be restarted after stopping

#### 2. OrderQueue Implementation

Implement all methods in `OrderQueue.java` to create a thread-safe queue:

**Core Requirements:**
- Support multiple concurrent producers (trader threads) and at least one consumer (market maker)
- Implement proper blocking behavior for capacity and availability constraints
- Maintain FIFO ordering and handle interruption gracefully
- Support shutdown functionality that prevents new orders and cancels pending ones
- Provide thread-safe statistics reporting

**Implementation Notes:**
- All method specifications are detailed in the JavaDoc comments
- Focus on choosing appropriate synchronization mechanisms
- Consider performance implications of your locking strategy
- Use efficient data structures for high-throughput scenarios

#### 3. MarketMaker Implementation

Implement methods in `MarketMaker.java`:

**Core Requirements:**
- Process incoming orders by matching them against existing orders
- Create `Execution` objects for successful matches and update order statuses appropriately
- Generate `Trade` objects for each execution and add them to traders' trade lists
- Store executions in the `ExecutedOrders` component
- Handle both full and partial order fills
- Support shutdown functionality that cancels remaining orders

**Implementation Notes:**
- All method specifications are detailed in the JavaDoc comments
- Use a simple matching algorithms
- Focus on thread safety and proper integration with other components

#### 4. ExecutedOrders Implementation

Implement all methods in `ExecutedOrders.java`:

**Core Requirements:**
- Store and retrieve execution records in a thread-safe manner
- Optimize lookups by symbol for performance
- Provide thread-safe statistics reporting with consistent views during concurrent updates

**Implementation Notes:**
- All method specifications are detailed in the JavaDoc comments
- Choose appropriate data structures for fast symbol-based lookups
- Consider synchronization granularity for optimal performance

#### 5. Supporting Components

**Trader Statistics Implementation:**
- Implement `addTrade(Trade trade)` and `printStatistics()` methods in `Trader.java`
- Thread-safely store trades and calculate statistics (total count, PnL, per-symbol breakdowns)
- Ensure statistics are consistent during concurrent updates

**TradingThreadFactory Implementation:**
- Implement `createTradingThread()` to create threads that continuously generate and submit orders
- Implement `createMarketMakerThread()` to create the market maker processing thread
- Ensure proper thread lifecycle management and graceful shutdown handling

**Implementation Notes:**
- All method specifications are detailed in the JavaDoc comments
- Focus on proper thread creation patterns and exception handling
- Consider how threads should respond to interruption

#### 6. Thread Safety Analysis and Resolution

**Critical Requirement**: You must identify and resolve all existing multithreading issues in the provided codebase. Some components may contain race conditions or other concurrency bugs that you must fix to ensure correct operation in a multi-threaded environment.

#### 7. Statistics and Consistency Requirements

**Internal Component Consistency:**
- Each component (Trader, OrderQueue, ExecutedOrders) must provide internally consistent statistics
- When a component prints statistics, all values should represent a coherent snapshot of that component's state
- Use appropriate synchronization to ensure concurrent updates don't corrupt individual component statistics

**Cross-Component Consistency:**
- Perfect synchronization across different components is not required or expected
- It's normal for OrderQueue to show 100 total orders while ExecutedOrders shows 95 executions due to natural system processing delays
- Focus on making each component's internal operations thread-safe rather than achieving system-wide atomic consistency

### Technical Constraints (Important !!)

#### Concurrency Restrictions
You may ONLY use the following `java.util.concurrent` utilities:
- `java.util.concurrent.BlockingQueue` implementations

#### Prohibited Tools
- `java.util.concurrent.ConcurrentHashMap`
- `java.util.concurrent.atomic.*` (AtomicInteger, AtomicLong, AtomicReference, etc.)
- `ExecutorService`, `ThreadPoolExecutor`, `CompletableFuture`
- `java.util.concurrent.locks.*` (ReentrantLock, ReadWriteLock, etc.)
- `CountDownLatch`, `CyclicBarrier`, `Semaphore`
- Any third-party concurrency libraries

#### Code Modification Constraints
- **Main.java**: You are NOT allowed to modify `Main.java`. Your implementation must be compatible with the existing Main class as provided
- **API Extensions**: You are free to add new methods, fields, or constructors to any class, but existing method signatures must remain unchanged
- **Compatibility**: Any modifications must maintain backward compatibility with the existing `Main.java`

#### Other Constraints
- **Java Version**: Must compile and run with Java 21
- **Thread Safety**: All shared data structures must be thread-safe using only the allowed concurrency utilities
- **Logging**: Use the provided Log4j2 logger for output (do not use `System.out`)

### Performance Requirements

Your implementation will be evaluated on:
- **Correctness**: Thread safety, data consistency, proper synchronization
- **Throughput**: Orders processed per second during the simulation
- **Scalability**: Performance with varying numbers of trader threads
- **Resource Usage**: Efficient memory usage and CPU utilization

### Testing and Evaluation

Your solution will be tested with:
- Various numbers of trader threads (2, 5, 10, 20)
- Different queue capacities (100, 1000, unlimited)
- Extended simulation durations (30+ seconds)
- Stress testing for race conditions and deadlocks


### Important Notes

- Focus on correctness first, then optimize for performance
- Test your implementation with multiple thread counts to verify scalability
- Pay special attention to edge cases in concurrent scenarios
- Document any assumptions or design decisions in your code comments
- The system should handle graceful shutdown without data loss or resource leaks
- Remember that `Main.java` (or similar) will be used exactly as provided for testing - ensure compatibility
- Without atomic variables and ConcurrentHashMap, you'll need to think carefully about synchronization strategies

Remember that the key to success is balancing correctness with performance while maintaining clean, readable, and well-documented code. This assignment mirrors real-world concurrent system design challenges you'll encounter in industry, where understanding fundamental synchronization mechanisms is crucial.