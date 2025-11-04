package edu.yu.cs.primes.strategies;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import edu.yu.cs.primes.PrimeLab;
import edu.yu.cs.primes.PrimeStrategy;

public class UnboundedPrimeChecker implements PrimeStrategy {

    @Override
    public int countPrimes(List<Integer> numbers, int ignored) {
        AtomicInteger primeCount = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<>(numbers.size());

        for (int n : numbers) {
            Thread t = new Thread(() -> {
                if (PrimeLab.isPrime(n)) primeCount.incrementAndGet();
            });
            threads.add(t);
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        return primeCount.get();
    }
}
