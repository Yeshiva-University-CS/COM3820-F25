import java.util.*;
import java.util.stream.*;

/**
 * Demonstration of how spliterator characteristics affect parallel stream
 * performance.
 * 
 * This class shows the performance differences between:
 * 1. ArrayList (SIZED + SUBSIZED + ORDERED) - Excellent for parallel
 * 2. LinkedList (SIZED + ORDERED, but NOT SUBSIZED) - Poor for parallel
 * 3. Iterator-based streams (NOT SIZED, NOT SUBSIZED) - Very poor for parallel
 * 
 * Run this in class to see real performance differences on your machine!
 */
public class SpliteratorPerformance {

    private static final int DATA_SIZE = 30_000_000;
    private static final int WARMUP_RUNS = 3;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Spliterator Performance Demonstration");
        System.out.println("========================================\n");

        // Print system information
        printSystemInfo();

        System.out.println("\n========================================");
        System.out.println("Running Tests...");
        System.out.println("========================================\n");

        // Warmup JVM
        System.out.println("Warming up JVM...");
        for (int i = 0; i < WARMUP_RUNS; i++) {
            runAllTests(false);
        }
        System.out.println("Warmup complete!\n");

        // Run actual tests
        System.out.println("Running performance tests...\n");
        long[] results = runAllTests(true);

        // Print results
        printResults(results);

        // Print analysis
        printAnalysis(results);
    }

    /**
     * Print information about the system and thread pool
     */
    private static void printSystemInfo() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int parallelism = java.util.concurrent.ForkJoinPool.commonPool().getParallelism();

        System.out.println("System Information:");
        System.out.println("-------------------");
        System.out.println("Available Processors: " + availableProcessors);
        System.out.println("Common ForkJoinPool Parallelism: " + parallelism);
        System.out.println("Common ForkJoinPool Size: " +
                java.util.concurrent.ForkJoinPool.commonPool().getPoolSize());
        System.out.println("\nNote: The common pool typically uses (processors - 1) threads");
        System.out.println("      This allows the main thread to participate in work.");
    }

    /**
     * Run all three test scenarios
     * 
     * @param printProgress Whether to print progress messages
     * @return Array of execution times [arrayListTime, linkedListTime,
     *         iteratorTime]
     */
    private static long[] runAllTests(boolean printProgress) {
        long[] times = new long[3];

        // Test 1: ArrayList (SIZED + SUBSIZED + ORDERED)
        if (printProgress)
            System.out.println("Test 1: ArrayList (SIZED + SUBSIZED + ORDERED)");
        List<Integer> arrayList = IntStream.range(0, DATA_SIZE)
                .boxed()
                .collect(Collectors.toList());

        times[0] = runTestMultipleTimes(() -> arrayList.parallelStream()
                .filter(n -> n % 2 == 0)
                .count(), printProgress);

        if (printProgress)
            System.out.println("  ✓ Average time (dropping extremes): " + times[0] + "ms\n");

        // Test 2: LinkedList (SIZED + ORDERED, but NOT SUBSIZED)
        if (printProgress)
            System.out.println("Test 2: LinkedList (SIZED + ORDERED, but NOT SUBSIZED)");
        List<Integer> linkedList = new LinkedList<>(arrayList);

        times[1] = runTestMultipleTimes(() -> linkedList.parallelStream()
                .filter(n -> n % 2 == 0)
                .count(), printProgress);

        if (printProgress)
            System.out.println("  ✓ Average time (dropping extremes): " + times[1] + "ms\n");

        // Test 3: Iterator-based (NOT SIZED, NOT SUBSIZED)
        if (printProgress)
            System.out.println("Test 3: Iterator-based Stream (NOT SIZED, NOT SUBSIZED)");
        
        times[2] = runTestMultipleTimes(() -> {
            Iterator<Integer> iter = arrayList.iterator();
            Stream<Integer> iterStream = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(iter, 0), true);
            iterStream.filter(n -> n % 2 == 0).count();
        }, printProgress);

        if (printProgress)
            System.out.println("  ✓ Average time (dropping extremes): " + times[2] + "ms\n");

        return times;
    }

    /**
     * Run a test 5 times, drop the min and max, and average the remaining 3
     * 
     * @param task The task to measure
     * @param printProgress Whether to print individual run times
     * @return Average execution time in milliseconds (excluding extremes)
     */
    private static long runTestMultipleTimes(Runnable task, boolean printProgress) {
        long[] runTimes = new long[5];
        
        for (int i = 0; i < 5; i++) {
            runTimes[i] = measureTime(task);
            if (printProgress) {
                System.out.println("    Run " + (i + 1) + ": " + runTimes[i] + "ms");
            }
        }
        
        // Sort to find min and max
        Arrays.sort(runTimes);
        
        // Average the middle 3 values (indices 1, 2, 3)
        long sum = runTimes[1] + runTimes[2] + runTimes[3];
        long average = sum / 3;
        
        if (printProgress) {
            System.out.println("    Dropped: " + runTimes[0] + "ms (min), " + runTimes[4] + "ms (max)");
            System.out.println("    Average of remaining 3: " + average + "ms");
        }
        
        return average;
    }

    /**
     * Measure execution time of a task
     * 
     * @param task The task to measure
     * @return Execution time in milliseconds
     */
    private static long measureTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000; // Convert to milliseconds
    }

    /**
     * Print formatted results
     */
    private static void printResults(long[] times) {
        System.out.println("========================================");
        System.out.println("Results Summary");
        System.out.println("========================================\n");

        System.out.printf("ArrayList:  %6d ms  (SIZED + SUBSIZED + ORDERED)%n", times[0]);
        System.out.printf("LinkedList: %6d ms  (SIZED + ORDERED, NOT SUBSIZED)%n", times[1]);
        System.out.printf("Iterator:   %6d ms  (NOT SIZED, NOT SUBSIZED)%n", times[2]);
    }

    /**
     * Print analysis of the results
     */
    private static void printAnalysis(long[] times) {
        System.out.println("\n========================================");
        System.out.println("Analysis");
        System.out.println("========================================\n");

        double linkedListSlowdown = (double) times[1] / times[0];
        double iteratorSlowdown = (double) times[2] / times[0];

        System.out.printf("LinkedList is %.1fx slower than ArrayList%n", linkedListSlowdown);
        System.out.printf("Iterator is %.1fx slower than ArrayList%n", iteratorSlowdown);

        System.out.println("\nWhy the difference?");
        System.out.println("-------------------");
        System.out.println("ArrayList: O(1) splitting via index arithmetic");
        System.out.println("           Can instantly split [0-999] into [0-499] and [500-999]");
        System.out.println();
        System.out.println("LinkedList: O(n) splitting via traversal");
        System.out.println("            Must walk node→node→node to find middle");
        System.out.println("            Every split requires expensive traversal");
        System.out.println();
        System.out.println("Iterator: Cannot effectively split at all");
        System.out.println("          Doesn't know size, can't jump to positions");
        System.out.println("          Falls back to mostly sequential execution");

        System.out.println("\n========================================");
        System.out.println("Key Takeaway");
        System.out.println("========================================");
        System.out.println("Choose ArrayList (or arrays) for parallel streams!");
        System.out.println("If you have a LinkedList, convert it first:");
        System.out.println("  List<T> arrayList = new ArrayList<>(linkedList);");
        System.out.println("Even with conversion cost, you'll be faster.");
    }

    /**
     * Optional: Run this to see spliterator characteristics for any collection
     */
    public static void printSpliteratorInfo(Collection<?> collection) {
        Spliterator<?> spliterator = collection.spliterator();

        System.out.println("\nSpliterator Characteristics for " +
                collection.getClass().getSimpleName() + ":");
        System.out.println("  SIZED:     " +
                spliterator.hasCharacteristics(Spliterator.SIZED));
        System.out.println("  SUBSIZED:  " +
                spliterator.hasCharacteristics(Spliterator.SUBSIZED));
        System.out.println("  ORDERED:   " +
                spliterator.hasCharacteristics(Spliterator.ORDERED));
        System.out.println("  DISTINCT:  " +
                spliterator.hasCharacteristics(Spliterator.DISTINCT));
        System.out.println("  SORTED:    " +
                spliterator.hasCharacteristics(Spliterator.SORTED));
        System.out.println("  NONNULL:   " +
                spliterator.hasCharacteristics(Spliterator.NONNULL));
        System.out.println("  IMMUTABLE: " +
                spliterator.hasCharacteristics(Spliterator.IMMUTABLE));
    }
}