package edu.yu.cs.primes.strategies;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.CountDownLatch;

import edu.yu.cs.primes.PrimeLab;
import edu.yu.cs.primes.PrimeStrategy;

/**
 * A PrimeStrategy implementation that uses a custom Executor (does not use ExecutorService).
 * The executor runs a fixed set of worker threads that take Runnables from a queue.
 */
public class ExecutorPrimeChecker implements PrimeStrategy {

    @Override
    public int countPrimes(List<Integer> numbers, int numThreads) {
        if (numThreads <= 0) throw new IllegalArgumentException("numThreads must be >= 1");

        final AtomicInteger primeCount = new AtomicInteger(0);

        final Map<String, Integer> workCounts = new ConcurrentHashMap<>();
        final Map<String, Long> workTimes = new ConcurrentHashMap<>();

        // Use a fixed thread pool created via Executors.newFixedThreadPool but only use it as an Executor.
        final var threadIndex = new AtomicInteger(0);
        ThreadFactory factory = r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("exec-worker-" + threadIndex.getAndIncrement());
            return t;
        };
        Executor executor = Executors.newFixedThreadPool(numThreads, factory);

        // CountDownLatch to wait for all submitted tasks to finish
        CountDownLatch done = new CountDownLatch(numbers.size());

        // submit tasks; each task records its own time and attributes it to the worker thread
        for (int n : numbers) {
            final int value = n;
            executor.execute(() -> {
                String workerName = Thread.currentThread().getName();
                int localCount = 0;
                long startTask = System.nanoTime();
                try {
                    if (PrimeLab.isPrime(value)) localCount++;
                } finally {
                    primeCount.addAndGet(localCount);
                    long taskNs = System.nanoTime() - startTask;
                    workCounts.merge(workerName, 1, Integer::sum);
                    workTimes.merge(workerName, taskNs, Long::sum);
                    done.countDown();
                }
            });
        }

        try {
            done.await();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nWorker Summary (EXECUTOR):");
        for (String name : workCounts.keySet()) {
            int count = workCounts.get(name);
            double ms = workTimes.get(name) / 1_000_000.0;
            System.out.printf("  %-15s processed=%5d  timeMs=%8.2f%n", name, count, ms);
        }

        return primeCount.get();
    }
}
