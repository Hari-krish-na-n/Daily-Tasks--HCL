package com.auction.lab;

import java.util.ArrayList;
import java.util.List;

/**
 * Experiment 4: Garbage Collection Basics and Reachability
 *
 * Demonstrates:
 * 1. Object Reachability: An object is reachable if it can be accessed through a chain
 *    of references starting from GC Roots (local variables on active thread stacks,
 *    static fields, JNI handles). Once references are cleared or overwritten (e.g. set to null),
 *    the objects become unreachable and eligible for garbage collection.
 * 2. Generational GC: The JVM heap is divided into Young Generation (Eden and Survivor spaces)
 *    and Old/Tenured Generation. Short-lived objects are quickly reclaimed by Minor GC (Young GC).
 *    Objects that survive multiple collection cycles get promoted to the Old Generation,
 *    where Major GC / Full GC runs less frequently.
 * 3. Non-deterministic timing: System.gc() only requests or hints that the JVM perform
 *    collection; it does not guarantee immediate execution.
 */
public class GarbageCollectionExperiment {

    static class AuctionItemToken {
        private final int id;
        private final byte[] payload;

        public AuctionItemToken(int id) {
            this.id = id;
            // 64 KB payload per item to generate measurable allocation pressure
            this.payload = new byte[64 * 1024];
        }

        public int getId() {
            return id;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Experiment 4: Garbage Collection & Reachability Demonstration ===");

        // Phase 1: Allocate temporary objects that become unreachable immediately
        System.out.println("\n--- Phase 1: Allocating short-lived temporary objects ---");
        for (int i = 0; i < 500; i++) {
            // These objects lose their reference at the end of each loop iteration
            AuctionItemToken temp = new AuctionItemToken(i);
            if (i % 100 == 0) {
                System.out.println("Allocated temporary token ID: " + temp.getId());
            }
        }
        System.out.println("Phase 1 complete: 500 temporary objects created and dereferenced.");

        // Phase 2: Retain objects in a list, then clear the reference chain
        System.out.println("\n--- Phase 2: Retained objects transitioning to unreachable ---");
        List<AuctionItemToken> retainedList = new ArrayList<>();
        for (int i = 1000; i < 1400; i++) {
            retainedList.add(new AuctionItemToken(i));
        }
        System.out.printf("Retained %d tokens in list. Approximate retained payload: %,d KB%n",
                retainedList.size(), (retainedList.size() * 64));

        // Dereference the entire list: breaking reachability from GC root
        retainedList.clear();
        retainedList = null;
        System.out.println("Cleared list reference. All 400 objects are now unreachable!");

        // Phase 3: Suggest Garbage Collection to the JVM
        System.out.println("\n--- Phase 3: Suggesting Garbage Collection via System.gc() ---");
        long beforeFree = Runtime.getRuntime().freeMemory();
        System.out.printf("Free heap before System.gc(): %,d bytes (%.2f MB)%n",
                beforeFree, beforeFree / (1024.0 * 1024.0));

        System.gc();

        // Brief sleep to allow GC thread activity to reflect in logs
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}

        long afterFree = Runtime.getRuntime().freeMemory();
        System.out.printf("Free heap after System.gc():  %,d bytes (%.2f MB)%n",
                afterFree, afterFree / (1024.0 * 1024.0));
        System.out.println("Garbage collection observation completed successfully.");
    }
}
