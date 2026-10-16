package com.auction.lab;

/**
 * Experiment 3: String vs StringBuilder Performance and Memory Allocation
 *
 * Compares repeated concatenation using immutable String ('+=') against mutable StringBuilder.
 * In Java, Strings are immutable: every 'str += token' creates a new String object and copies
 * existing character content. In a loop of N iterations, this leads to O(N^2) character copying
 * and thousands of intermediate temporary objects discarded on the young generation heap.
 * In contrast, StringBuilder maintains a resizable char/byte array buffer, executing appends
 * in amortized O(1) time with minimal object allocations.
 */
public class StringVsStringBuilderExperiment {

    public static void main(String[] args) {
        int iterations = 15_000;
        if (args.length > 0) {
            try {
                iterations = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        System.out.println("=== Experiment 3: String += vs StringBuilder.append() ===");
        System.out.printf("Running benchmark with %,d iterations...%n%n", iterations);

        // Warm up run with small iteration to trigger JIT compilation
        warmUp(1000);

        // Test 1: String += concatenation
        System.gc(); // Suggest GC before test
        long startStr = System.nanoTime();
        String strResult = "";
        for (int i = 0; i < iterations; i++) {
            strResult += "x";
        }
        long durationStrNs = System.nanoTime() - startStr;
        double durationStrMs = durationStrNs / 1_000_000.0;

        System.out.printf("[String +=]          Iterations: %,d | Final Length: %,d | Time: %.3f ms (%,d ns)%n",
                iterations, strResult.length(), durationStrMs, durationStrNs);

        // Test 2: StringBuilder.append()
        System.gc(); // Suggest GC before test
        long startSb = System.nanoTime();
        StringBuilder sbResult = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sbResult.append("x");
        }
        long durationSbNs = System.nanoTime() - startSb;
        double durationSbMs = durationSbNs / 1_000_000.0;

        System.out.printf("[StringBuilder]      Iterations: %,d | Final Length: %,d | Time: %.3f ms (%,d ns)%n",
                iterations, sbResult.length(), durationSbMs, durationSbNs);

        // Verification of equal outcome
        boolean equalLength = (strResult.length() == sbResult.length());
        System.out.println("\nVerification: Both strings produced identical length: " + equalLength);
        if (durationSbMs > 0) {
            System.out.printf("Speedup Ratio: StringBuilder was ~%.1fx faster than String += in this run.%n",
                    durationStrMs / durationSbMs);
        }
    }

    private static void warmUp(int count) {
        String s = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            s += "w";
            sb.append("w");
        }
    }
}
