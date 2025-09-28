# Assignment 1: Multithreaded Trading System

## COM 3820 - Parallel Programming

### Overview

In this assignment, you will implement a high-throughput, multi-threaded trading system simulation. The system simulates multiple traders placing orders concurrently while one or more market makers process these orders to generate trades. Your goal is to maximize system throughput while ensuring thread safety and data consistency.

### Learning Objectives

By completing this assignment, you will:
- Implement thread-safe data structures using fundamental Java concurrency utilities
- Design a multi-producer, single- or multi-consumer system with optimal throughput
- Coordinate concurrent access to shared resources (market data, statistics, trade records)
- Apply synchronization mechanisms to prevent race conditions
- Understand the performance implications of different synchronization strategies
- Practice proper resource management and graceful shutdown procedures

### System Architecture

#### Core Components
- **TradingSystem**: Main orchestrator that manages all threads and components
- **OrderQueue**: Thread-safe queue that stores pending orders from traders
- **MarketMaker**: Processes orders from the queue and generates executions
- **Trader**: Individual trading entities (data models for traders)
- **OrderGenerator**: Utility that creates realistic trading orders for traders
- **ExecutedOrders**: Thread-safe storage for completed executions
- **MarketData**: Utility object that provides current market prices and generates realistic order prices

#### Threading Model
- **Trader Threads**: Multiple threads generating and submitting orders
- **Market Maker Thread(s)**: One or more threads processing orders and executing trades

**Note:** All detailed method specifications are in the JavaDoc comments.

### Implementation Requirements

#### 1. TradingSystem Implementation

**Core Requirements:**
- Create and start trader threads using the provided `TradingThreadFactory`
- Create and start the market maker thread
- Implement proper lifecycle management (prevent double-start, validate system state)
- Handle thread creation failures gracefully
- Coordinate graceful shutdown by calling shutdown methods on OrderQueue and MarketMaker
- Wait for all threads to complete with reasonable timeouts
- Return all canceled orders from the shutdown process

**Implementation Notes:**
- Consider thread lifecycle management and proper resource cleanup
- Ensure system can be restarted after stopping

#### 2. OrderQueue Implementation

**Core Requirements:**
- Support multiple concurrent producers (trader threads) and at least one consumer (market maker)
- Implement proper blocking behavior for capacity and availability constraints
- Maintain FIFO ordering and handle interruption gracefully
- Support shutdown functionality that prevents new orders and cancels pending ones
- Provide thread-safe statistics reporting

**Implementation Notes:**
- Focus on choosing appropriate synchronization mechanisms
- Consider performance implications of your locking strategy
- Use efficient data structures for high-throughput scenarios

#### 3. MarketMaker Implementation

**Core Requirements:**
- Process incoming orders by matching them against existing orders
- Create `Execution` objects for successful matches and update order statuses appropriately
- Generate `Trade` objects for each execution and add them to traders' trade lists
- Store executions in the `ExecutedOrders` component
- Handle both full and partial order fills
- Support shutdown functionality that cancels remaining orders

**Implementation Notes:**
- Use simple matching algorithms
- Focus on thread safety and proper integration with other components

#### 4. ExecutedOrders Implementation

**Core Requirements:**
- Store and retrieve execution records in a thread-safe manner
- Optimize lookups by symbol for performance
- Provide thread-safe statistics reporting with consistent views during concurrent updates

**Implementation Notes:**
- Choose appropriate data structures for fast symbol-based lookups
- Consider synchronization granularity for optimal performance

#### 5. Supporting Components

**Trader Statistics Implementation:**
- Implement thread-safe trade storage and statistics calculation
- Ensure statistics are consistent during concurrent updates

**TradingThreadFactory Implementation:**
- Create threads for continuous order generation and market making
- Ensure proper thread lifecycle management and graceful shutdown handling

**Implementation Notes:**
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

### Technical Constraints

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
- **POM file**: You are NOT allowed to modify the `.pom` file
- **Main.java**: You are NOT allowed to modify `Main.java`. Your implementation must be compatible with the existing Main class as provided
- **API Extensions**: You are free to add new methods, fields, or constructors to any class, but existing method signatures must remain unchanged
- **Compatibility**: Any modifications must maintain backward compatibility with the existing `Main.java`

#### Other Constraints
- **Java Version**: Must compile and run with Java 21
- **Thread Safety**: All shared data structures must be thread-safe using only the allowed concurrency utilities
- **Logging**: Use the provided Log4j2 logger for output (do not use `System.out`)

### Evaluation

Your implementation will be evaluated on correctness, throughput, scalability, and resource usage. Solutions will be stress tested with varying thread counts (2, 5, 10, 20), queue capacities (100, 1000, unlimited), and extended simulation durations (30+ seconds).

### Tips

- **Focus on correctness first**, then optimize for performance
- **Test thoroughly** with multiple thread counts to verify scalability
- **Handle edge cases** in concurrent scenarios carefully
- **Ensure clean shutdown** without data loss or resource leaks
- **Think carefully about synchronization strategies** without atomic variables and ConcurrentHashMap

This assignment mirrors real-world concurrent system design challenges where understanding fundamental synchronization mechanisms is crucial.