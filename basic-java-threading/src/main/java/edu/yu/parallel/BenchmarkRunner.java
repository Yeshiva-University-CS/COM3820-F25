package edu.yu.parallel;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class BenchmarkRunner {

    private static final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    @FunctionalInterface
    interface LongCallable { long call() throws Exception; }

    static class PhaseResult {
        final String label;
        final long total;
        final double timeMs;
        final double jvmCpuPctAllCores;  // normalized to 100% == all cores saturated
        final double jvmCpuPctPerCore;   // 100% == one core saturated

        PhaseResult(String label, long total, double timeMs, double allCores, double perCore) {
            this.label = label; this.total = total; this.timeMs = timeMs;
            this.jvmCpuPctAllCores = allCores; this.jvmCpuPctPerCore = perCore;
        }
    }

    private static PhaseResult measure(String label, LongCallable work) throws Exception {
        final int cores = osBean.getAvailableProcessors();

        final long wall0 = System.nanoTime();
        final long cpu0  = osBean.getProcessCpuTime(); // ns of JVM CPU across all cores

        long total = work.call();

        final long wall1 = System.nanoTime();
        final long cpu1  = osBean.getProcessCpuTime();

        final long wallΔ = wall1 - wall0;
        final long cpuΔ  = (cpu1 >= 0 && cpu0 >= 0) ? (cpu1 - cpu0) : -1;

        double timeMs = wallΔ / 1_000_000.0;
        double pctPerCore = (cpuΔ > 0) ? (cpuΔ * 100.0 / wallΔ) : Double.NaN;
        double pctAllCores = (cpuΔ > 0) ? (pctPerCore / cores) : Double.NaN;

        return new PhaseResult(label, total, timeMs, pctAllCores, pctPerCore);
    }

    private static void print(PhaseResult r) {
        String allCores = Double.isNaN(r.jvmCpuPctAllCores) ? "N/A" : String.format("%.1f%%", r.jvmCpuPctAllCores);
        String perCore  = Double.isNaN(r.jvmCpuPctPerCore)  ? "N/A" : String.format("%.1f%%", r.jvmCpuPctPerCore);
        System.out.printf("%-22s total=%-8d time=%8.2f ms   JVM CPU: %-6s (≈ %s per-core)%n",
                r.label + ":", r.total, r.timeMs, allCores, perCore);
    }

    public static void main(String[] args) throws Exception {
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 200_000;
        int iterations = (args.length > 1) ? Integer.parseInt(args[1]) : 8;
        int threads = (args.length > 2) ? Integer.parseInt(args[2]) :
                Math.max(1, Runtime.getRuntime().availableProcessors());

        System.out.printf("Benchmark: countPrimesUpTo(%d), iterations=%d%n", n, iterations);
        System.out.printf("CPU cores detected: %d%n", Runtime.getRuntime().availableProcessors());
        System.out.printf("Using %d threads%n%n", threads);

        // Small warmup to stabilize JIT
        BasicJavaThreading.runSequential(Math.min(n, 50_000), 1);

        // Measure each phase USING process CPU time (robust & lag-free)
        PhaseResult seq = measure("Sequential", () ->
                BasicJavaThreading.runSequential(n, iterations));

        PhaseResult raw1 = measure(String.format("Parallel (Threads 1,%2d)", threads), () ->
                BasicJavaThreading.runParallelWithThreads1(n, iterations, threads));

        PhaseResult raw2 = measure(String.format("Parallel (Threads 2,%2d)", threads), () ->
                BasicJavaThreading.runParallelWithThreads2(n, iterations, threads));

        PhaseResult raw3= measure(String.format("Parallel (Threads 3,%2d)", threads), () ->
                BasicJavaThreading.runParallelWithThreads3(n, iterations, threads));

        print(seq);
        print(raw1);
        print(raw2);
        print(raw3);

        if (seq.total != raw1.total || seq.total != raw2.total || seq.total != raw3.total) {
            System.out.println("\nWARNING: Results differ! (This should not happen.)");
        }

        System.out.printf("Speedup (RawThreads1): %.2fx%n", seq.timeMs / raw1.timeMs);
        System.out.printf("Speedup (RawThreads2): %.2fx%n", seq.timeMs / raw1.timeMs);
        System.out.printf("Speedup (RawThreads3): %.2fx%n", seq.timeMs / raw1.timeMs);
    }
}
