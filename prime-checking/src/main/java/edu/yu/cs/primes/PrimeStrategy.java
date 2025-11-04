package edu.yu.cs.primes;
import java.util.List;

public interface PrimeStrategy {
    int countPrimes(List<Integer> numbers, int numThreads);
}
