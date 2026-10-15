package com.auction.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Performance benchmarking program comparing ArrayList and LinkedList.
 *
 * Tests:
 * 1. Inserting at index 0 (beginning of the list)
 *    - ArrayList: requires shifting all existing elements to index + 1 on every insert. O(n) per insert.
 *    - LinkedList: allocates a new Node and updates head/first pointer. O(1) per insert.
 *
 * 2. Accessing elements by index using get(i)
 *    - ArrayList: direct array indexing (base_address + i * reference_size). O(1).
 *    - LinkedList: node pointer traversal from either head or tail until index i is reached. O(n) on average.
 *
 * Important Notes:
 * - Uses warmup passes to stabilize JIT compilation.
 * - Measures elapsed time using System.nanoTime().
 * - Results depend on machine CPU, JVM tier compilation, garbage collection, and data size.
 */
public class ListPerformanceBenchmark {

    public static class BenchmarkResult {
        public final String operation;
        public final int elementCount;
        public final long arrayListTimeNanos;
        public final long linkedListTimeNanos;

        public BenchmarkResult(String operation, int elementCount, long arrayListTimeNanos, long linkedListTimeNanos) {
            this.operation = operation;
            this.elementCount = elementCount;
            this.arrayListTimeNanos = arrayListTimeNanos;
            this.linkedListTimeNanos = linkedListTimeNanos;
        }

        public double getArrayListMs() {
            return arrayListTimeNanos / 1_000_000.0;
        }

        public double getLinkedListMs() {
            return linkedListTimeNanos / 1_000_000.0;
        }
    }

    public static List<BenchmarkResult> runBenchmark(int elementCount) {
        System.out.println("=================================================");
        System.out.println("   ARRAYLIST VS LINKEDLIST PERFORMANCE TIMING");
        System.out.println("=================================================");
        System.out.printf("Dataset Size: %,d elements%n", elementCount);
        System.out.println("JVM Warmup in progress...");

        // Warm up JIT
        warmup(5_000);

        List<BenchmarkResult> results = new ArrayList<>();

        // Test 1: Insertion at beginning (index 0)
        System.out.println("\n[Test 1] Measuring Insertion at beginning (add(0, value))...");
        long alInsertTime = timeInsertAtBeginning(new ArrayList<>(), elementCount);
        long llInsertTime = timeInsertAtBeginning(new LinkedList<>(), elementCount);
        BenchmarkResult insertResult = new BenchmarkResult("Insert at index 0", elementCount, alInsertTime, llInsertTime);
        results.add(insertResult);

        System.out.printf("  * ArrayList:  %,12d ns (%.3f ms)%n", alInsertTime, insertResult.getArrayListMs());
        System.out.printf("  * LinkedList: %,12d ns (%.3f ms)%n", llInsertTime, insertResult.getLinkedListMs());
        if (llInsertTime < alInsertTime) {
            System.out.printf("  -> LinkedList was %.2fx faster for head insertion.%n", (double) alInsertTime / llInsertTime);
        } else {
            System.out.printf("  -> ArrayList was %.2fx faster (small size or node allocation overhead).%n", (double) llInsertTime / alInsertTime);
        }

        // Test 2: Random indexed access get(i)
        // Note: For LinkedList random access, testing full elementCount could take seconds due to O(n^2) total traversal.
        // We test a representative number of random accesses across the populated list.
        int accessOperations = Math.min(elementCount, 20_000);
        System.out.printf("%n[Test 2] Measuring Random Indexed Access get(i) (%d lookups)...%n", accessOperations);

        List<Integer> prePopulatedAL = new ArrayList<>(elementCount);
        List<Integer> prePopulatedLL = new LinkedList<>();
        for (int i = 0; i < elementCount; i++) {
            prePopulatedAL.add(i);
            prePopulatedLL.add(i);
        }

        int[] randomIndices = new int[accessOperations];
        Random random = new Random(42); // fixed seed for reproducibility
        for (int i = 0; i < accessOperations; i++) {
            randomIndices[i] = random.nextInt(elementCount);
        }

        long alAccessTime = timeRandomAccess(prePopulatedAL, randomIndices);
        long llAccessTime = timeRandomAccess(prePopulatedLL, randomIndices);
        BenchmarkResult accessResult = new BenchmarkResult("Random Access get(i)", accessOperations, alAccessTime, llAccessTime);
        results.add(accessResult);

        System.out.printf("  * ArrayList:  %,12d ns (%.3f ms)%n", alAccessTime, accessResult.getArrayListMs());
        System.out.printf("  * LinkedList: %,12d ns (%.3f ms)%n", llAccessTime, accessResult.getLinkedListMs());
        if (alAccessTime < llAccessTime) {
            System.out.printf("  -> ArrayList was %.2fx faster for random indexed access.%n", (double) llAccessTime / Math.max(1, alAccessTime));
        }

        return results;
    }

    private static void warmup(int count) {
        List<Integer> al = new ArrayList<>();
        List<Integer> ll = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            al.add(0, i);
            ll.add(0, i);
        }
        for (int i = 0; i < count; i++) {
            int dummy = al.get(i) + ll.get(i);
        }
    }

    private static long timeInsertAtBeginning(List<Integer> list, int count) {
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            list.add(0, i);
        }
        return System.nanoTime() - start;
    }

    private static long timeRandomAccess(List<Integer> list, int[] indices) {
        long start = System.nanoTime();
        long blackhole = 0;
        for (int idx : indices) {
            blackhole += list.get(idx);
        }
        long elapsed = System.nanoTime() - start;
        if (blackhole == -1) System.out.print(""); // prevent dead-code elimination
        return elapsed;
    }

    public static void main(String[] args) {
        runBenchmark(50_000);
    }
}
