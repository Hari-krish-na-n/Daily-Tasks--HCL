package com.auction.lab;

import java.util.ArrayList;
import java.util.List;

/**
 * Experiment 2: OutOfMemoryError (Java heap space)
 *
 * Demonstrates heap memory exhaustion caused by retained references.
 * When objects are added to a growing collection, they remain reachable from the GC roots
 * and cannot be reclaimed by the garbage collector. When the allocated heap limit
 * (configured via -Xmx, e.g. -Xmx64m) is reached and GC cannot free sufficient memory,
 * the JVM throws java.lang.OutOfMemoryError: Java heap space.
 */
public class OutOfMemoryExperiment {

    public static void main(String[] args) {
        System.out.println("=== Experiment 2: OutOfMemoryError (Java Heap Space) ===");
        Runtime runtime = Runtime.getRuntime();
        System.out.printf("Max Heap (-Xmx): %,d bytes (%.2f MB)%n",
                runtime.maxMemory(), runtime.maxMemory() / (1024.0 * 1024.0));
        System.out.printf("Initial Heap (-Xms): %,d bytes (%.2f MB)%n",
                runtime.totalMemory(), runtime.totalMemory() / (1024.0 * 1024.0));

        // Collection holding strong references to prevent garbage collection
        List<byte[]> retainedMemoryChunks = new ArrayList<>();
        int chunkSize = 1024 * 1024; // 1 MB chunk
        int allocationCount = 0;
        long totalAllocatedBytes = 0;

        try {
            while (true) {
                // Allocate 1 MB and retain reference
                byte[] chunk = new byte[chunkSize];
                // Touch the array to ensure memory pages are committed
                chunk[0] = (byte) (allocationCount % 128);
                retainedMemoryChunks.add(chunk);

                allocationCount++;
                totalAllocatedBytes += chunkSize;

                long freeMemory = runtime.freeMemory();
                long totalMemory = runtime.totalMemory();

                if (allocationCount % 10 == 0 || allocationCount > 50) {
                    System.out.printf("Iteration %d: Allocated %,d MB | Total Heap: %,d MB | Free: %,d MB%n",
                            allocationCount,
                            totalAllocatedBytes / (1024 * 1024),
                            totalMemory / (1024 * 1024),
                            freeMemory / (1024 * 1024));
                }
            }
        } catch (OutOfMemoryError oom) {
            // Free the retained collection immediately so stdout/stderr formatting has heap space
            int count = allocationCount;
            long mb = totalAllocatedBytes / (1024 * 1024);
            retainedMemoryChunks.clear();
            System.err.println("Successfully caught OutOfMemoryError: " + oom.getMessage());
            System.out.printf("Heap exhausted after allocating %d chunks (~%d MB retained)%n", count, mb);
            System.out.println("Cleaned retained references. Heap recovered successfully.");
        }
    }
}
