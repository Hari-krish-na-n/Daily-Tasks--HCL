package com.auction.lab;

/**
 * Experiment 1: StackOverflowError Depth Exploration
 *
 * Demonstrates stack memory usage and stack frame allocation.
 * Each recursive invocation pushes a new stack frame containing method parameters,
 * local variables, and return address. When the thread stack reaches its capacity limit
 * (configured via -Xss), the JVM throws java.lang.StackOverflowError.
 */
public class StackOverflowExperiment {

    private static long recursionDepth = 0;

    public static void main(String[] args) {
        System.out.println("=== Experiment 1: StackOverflowError Exploration ===");
        System.out.println("Starting recursion to measure maximum stack depth...");
        try {
            recursiveMethod(1L);
        } catch (StackOverflowError error) {
            System.err.println("Caught StackOverflowError!");
            System.out.println("Max recursion depth reached: " + recursionDepth);
            System.out.println("Thread name: " + Thread.currentThread().getName());
        }
    }

    /**
     * Recursive call with primitive and reference variables in each stack frame.
     */
    public static void recursiveMethod(long depth) {
        recursionDepth = depth;
        // Introduce small local variables in the stack frame
        long dummy1 = depth * 2;
        long dummy2 = depth + 10;
        recursiveMethod(depth + 1);
    }

    public static long getRecursionDepth() {
        return recursionDepth;
    }
}
