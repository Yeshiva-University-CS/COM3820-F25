package edu.yu.cs.primes;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import edu.yu.cs.primes.strategies.PartitionedPrimeChecker;
import edu.yu.cs.primes.strategies.ProducerConsumerPrimeChecker;
import edu.yu.cs.primes.strategies.UnboundedPrimeChecker;

public class PrimeLab {

    public static void main(String[] args) {
        if (args.length < 2) {
        System.out.println(
            "Usage:\n" +
            "  java -jar prime-lab-1.jar UNBOUNDED <count>\n" +
            "  java -jar prime-lab-1.jar PARTITIONED <count> <numThreads>\n" +
            "  java -jar prime-lab-1.jar QUEUE <count> <numThreads>\n" +
            "\nExamples:\n" +
            "  java -jar prime-lab-1.jar UNBOUNDED 1000\n" +
            "  java -jar prime-lab-1.jar PARTITIONED 2000 4\n" +
            "  java -jar prime-lab-1.jar QUEUE 2000 4\n"
        );
            System.exit(1);
        }

        String mode = args[0].toUpperCase();
        int count = Integer.parseInt(args[1]);
        int numThreads = (("PARTITIONED".equals(mode) || "QUEUE".equals(mode)) && args.length >= 3)
                ? Integer.parseInt(args[2])
                : -1;

    System.out.printf("Generating %,d random numbers...%n", (long)count);
    long genStartNs = System.nanoTime();
    List<Integer> numbers = generateRandomNumbers(count, 100_000, 1_000_000);
    long genEndNs = System.nanoTime();

    long sortStartNs = System.nanoTime();
    //numbers.sort(Comparator.naturalOrder());
    long sortEndNs = System.nanoTime();

    System.out.printf(Locale.US, "Generated %,d numbers in %.2f ms; sorted in %.2f ms%n",
        (long)numbers.size(), (genEndNs - genStartNs) / 1_000_000.0, (sortEndNs - sortStartNs) / 1_000_000.0);

    PrimeStrategy strategy;
        switch (mode) {
            case "UNBOUNDED":
                strategy = new UnboundedPrimeChecker();
                break;
            case "PARTITIONED":
                strategy = new PartitionedPrimeChecker();
                break;
            case "QUEUE":
                strategy = new ProducerConsumerPrimeChecker();
                break;
            default:
                throw new IllegalArgumentException("Unknown mode: " + mode);
        }

        long start = System.nanoTime();
        int primes = strategy.countPrimes(numbers, numThreads);
        long end = System.nanoTime();

        double ms = (end - start) / 1_000_000.0;
        System.out.printf(Locale.US,
                "Mode=%s, count=%d, threads=%d, primes=%d, timeMs=%.2f%n",
                mode, count, (numThreads < 0 ? numbers.size() : numThreads), primes, ms);
    }

    public static List<Integer> generateRandomNumbers(int size, int minInclusive, int maxInclusive) {
        ArrayList<Integer> list = new ArrayList<>(size);
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) {
            list.add(rng.nextInt(minInclusive, maxInclusive + 1));
        }
        return list;
    }

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        int limit = (int) Math.sqrt(n);
        for (int d = 3; d <= limit; d += 2) {
            if (n % d == 0) return false;
        }
        return true;
    }
}