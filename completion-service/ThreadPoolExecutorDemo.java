import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo {
    
    public static void main(String[] args) {
        // Start timer
        long startTime = System.currentTimeMillis();
        
        // Get number of threads from command line, default to 4
        int numThreads = 4;
        if (args.length > 0) {
            try {
                numThreads = Integer.parseInt(args[0]);
                if (numThreads < 1) {
                    System.err.println("Number of threads must be at least 1. Using default: 4");
                    numThreads = 4;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format. Using default: 4 threads");
                numThreads = 4;
            }
        }
        
        System.out.println("Using thread pool with " + numThreads + " threads\n");
        
        // Create a thread pool with specified number of threads
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        
        // Generate random task durations (1-10 seconds, shuffled)
        List<Integer> taskDurations = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            taskDurations.add(i);
        }
        Collections.shuffle(taskDurations);
        
        // List to store Future objects
        List<Future<Integer>> futures = new ArrayList<>();
        
        // Submit 10 tasks
        for (int i = 0; i < 10; i++) {
            final int taskNumber = i + 1;
            final int sleepDuration = taskDurations.get(i);
            
            Callable<Integer> task = () -> {
                System.out.println("I am task #" + taskNumber + " doing work for " + sleepDuration + " seconds");
                try {
                    Thread.sleep(sleepDuration * 1000);
                    System.out.println("Task #" + taskNumber + " completed after " + sleepDuration + " seconds");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Task #" + taskNumber + " was interrupted");
                }
                return sleepDuration;
            };
            
            futures.add(executorService.submit(task));
        }
        
        // Shutdown the executor service
        executorService.shutdown();
        
        try {
            System.out.println("\nWaiting for all tasks to complete...");
            // Wait for all tasks to finish (with a timeout of 2 minutes)
            if (executorService.awaitTermination(2, TimeUnit.MINUTES)) {
                System.out.println("All tasks completed successfully!");
            } else {
                System.err.println("Timeout occurred before all tasks completed");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted while waiting for tasks");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Collect results from all tasks
        List<Integer> results = new ArrayList<>();
        for (Future<Integer> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error retrieving task result: " + e.getMessage());
            }
        }
        
        // Print the results
        System.out.println("\nResults (sleep durations returned by tasks): " + results);
        
        // End timer and print elapsed time
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Total execution time: " + elapsedTime + " ms (" + String.format("%.2f", elapsedTime / 1000.0) + " seconds)");
    }
}
