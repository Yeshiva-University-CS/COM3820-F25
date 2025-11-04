package edu.yu.cs.primes.strategies;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import edu.yu.cs.primes.PrimeLab;
import edu.yu.cs.primes.PrimeStrategy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class PartitionedPrimeChecker implements PrimeStrategy {

    @Override
    public int countPrimes(List<Integer> numbers, int numThreads) {
        if (numThreads <= 0) throw new IllegalArgumentException("numThreads must be >= 1");

        AtomicInteger primeCount = new AtomicInteger(0);
        List<Thread> workers = new ArrayList<>(numThreads);

        Map<String, Integer> workCounts = new ConcurrentHashMap<>();
        Map<String, Long> workTimes = new ConcurrentHashMap<>();

        int n = numbers.size();
        int base = n / numThreads;
        int rem = n % numThreads;

        int startIdx = 0;
        for (int i = 0; i < numThreads; i++) {
            int size = base + (i < rem ? 1 : 0);
            int from = startIdx;
            int to = Math.min(from + size, n);
            startIdx = to;

            final List<Integer> slice = (from < to) ? numbers.subList(from, to) : List.of();
            final String threadName = "part-worker-" + i;

            Thread worker = new Thread(() -> {
                int localProcessed = 0;
                long startNs = System.nanoTime();
                try {
                    for (int val : slice) {
                        if (PrimeLab.isPrime(val)) {
                            primeCount.incrementAndGet();
                        }
                        localProcessed++;
                    }
                } finally {
                    long elapsed = System.nanoTime() - startNs;
                    workCounts.put(threadName, localProcessed);
                    workTimes.put(threadName, elapsed);
                }
            }, threadName);

            workers.add(worker);
        }

        for (Thread t : workers) t.start();
        for (Thread t : workers) {
            try { t.join(); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
        }

        System.out.println("\nWorker Summary (PARTITIONED):");
        for (String name : workCounts.keySet()) {
            int count = workCounts.get(name);
            double ms = workTimes.get(name) / 1_000_000.0;
            System.out.printf("  %-15s processed=%5d  timeMs=%8.2f%n", name, count, ms);
        }

        return primeCount.get();
    }
}
