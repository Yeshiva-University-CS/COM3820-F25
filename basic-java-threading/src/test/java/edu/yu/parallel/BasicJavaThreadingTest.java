package edu.yu.parallel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicJavaThreadingTest {

    @Test
    void allApproachesProduceSameResult() throws Exception {
        int n = 100_000;
        int iterations = 4;
        int threads = Math.max(1, Runtime.getRuntime().availableProcessors());

        long seq  = BasicJavaThreading.runSequential(n, iterations);
        long raw1   = BasicJavaThreading.runParallelWithThreads1(n, iterations, threads);
        long raw2  = BasicJavaThreading.runParallelWithThreads2(n, iterations, threads);
        long raw3  = BasicJavaThreading.runParallelWithThreads3(n, iterations, threads);

        assertEquals(seq, raw1,  "Result must match sequential");
        assertEquals(seq, raw2,  "Result must match sequential");
        assertEquals(seq, raw3,  "Result must match sequential");
    }

    @Test
    void primeCounterSanity() {
        assertEquals(0, BasicJavaThreading.countPrimesUpTo(1));
        assertEquals(1, BasicJavaThreading.countPrimesUpTo(2));
        assertEquals(4, BasicJavaThreading.countPrimesUpTo(10));   // 2,3,5,7
        assertEquals(25, BasicJavaThreading.countPrimesUpTo(100)); // quick sanity
    }
}
