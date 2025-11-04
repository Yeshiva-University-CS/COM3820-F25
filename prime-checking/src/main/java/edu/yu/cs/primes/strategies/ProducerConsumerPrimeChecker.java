package edu.yu.cs.primes.strategies;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import edu.yu.cs.primes.PrimeLab;
import edu.yu.cs.primes.PrimeStrategy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ProducerConsumerPrimeChecker implements PrimeStrategy {
    
    private static record PrimeTask(int value, boolean poison) {
        private static final PrimeTask POISON = new PrimeTask(-1, true);
    }

    @Override
    public int countPrimes(List<Integer> numbers, int numThreads) {
        if (numThreads <= 0)
            throw new IllegalArgumentException("numThreads must be >= 1");

        final BlockingQueue<PrimeTask> queue = new LinkedBlockingQueue<>();
        final AtomicInteger primeCount = new AtomicInteger(0);

        final Map<String, Integer> workCounts = new ConcurrentHashMap<>();
        final Map<String, Long> workTimes = new ConcurrentHashMap<>();

        List<Thread> workers = new ArrayList<>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            String threadName = "pc-worker-" + i;
            Thread worker = new Thread(() -> {
                int localCount = 0;
                long startTime = System.nanoTime();
                try {
                    while (true) {
                        PrimeTask task = queue.take();
                        if (task.poison()) break;
                        if (PrimeLab.isPrime(task.value()))
                            primeCount.incrementAndGet();
                        localCount++;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    long elapsedNs = System.nanoTime() - startTime;
                    workCounts.put(threadName, localCount);
                    workTimes.put(threadName, elapsedNs);
                }
            }, threadName);
            workers.add(worker);
        }

        Thread producer = new Thread(() -> {
            try {
                for (int n : numbers) queue.put(new PrimeTask(n, false));
                for (int i = 0; i < numThreads; i++) queue.put(PrimeTask.POISON);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "pc-producer");

        producer.start();
        for (Thread w : workers) w.start();

        try { producer.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        for (Thread w : workers) {
            try { w.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        System.out.println("\nWorker Summary (PRODUCER-CONSUMER):");
        for (String name : workCounts.keySet()) {
            int count = workCounts.get(name);
            double ms = workTimes.get(name) / 1_000_000.0;
            System.out.printf("  %-15s processed=%5d  timeMs=%8.2f%n", name, count, ms);
        }

        return primeCount.get();
    }
}
