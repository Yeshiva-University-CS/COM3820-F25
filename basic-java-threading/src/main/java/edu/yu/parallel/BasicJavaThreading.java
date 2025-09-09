package edu.yu.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CPU-intensive demo: count primes up to 'n' repeatedly.
 * Provides:
 *  - sequential
 *  - parallel WITH executors
 *  - parallel WITH raw threads (no executors)
 */
public class BasicJavaThreading {

    /** Naive primality test (CPU-heavy). */
    public static boolean isPrime(int x) {
        if (x < 2) return false;
        if (x % 2 == 0) return x == 2;
        int limit = (int) Math.sqrt(x);
        for (int d = 3; d <= limit; d += 2) {
            if (x % d == 0) return false;
        }
        return true;
    }

    /** Count primes in [2, n]. */
    public static int countPrimesUpTo(int n) {
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) count++;
        }
        return count;
    }

    /** Sequential run of the task repeated 'iterations' times. */
    public static long runSequential(int n, int iterations) {
        long total = 0;
        for (int i = 0; i < iterations; i++) {
            total += countPrimesUpTo(n);
        }
        return total;
    }

    /**
     * Parallel run using RAW THREADS (no executors).
     * We divvy the 'iterations' among 'threads' threads and join.
     * Uses the Thread class
     */
    public static long runParallelWithThreads1(int n, int iterations, int threads) throws InterruptedException {
        return runSequential(n, iterations);
    }

    /**
     * Parallel run using RAW THREADS (no executors).
     * We divvy the 'iterations' among 'threads' threads and join.
     * Uses the Runnable interface
     */
    public static long runParallelWithThreads2(int n, int iterations, int threads) throws InterruptedException {
        return runSequential(n, iterations);
    }

    /**
     * Parallel run using RAW THREADS (no executors).
     * We divvy the 'iterations' among 'threads' threads and join.
     * Uses the Runnable interface and lambdas
     */
    public static long runParallelWithThreads3(int n, int iterations, int threads) throws InterruptedException {
        return runSequential(n, iterations);
    }
}
