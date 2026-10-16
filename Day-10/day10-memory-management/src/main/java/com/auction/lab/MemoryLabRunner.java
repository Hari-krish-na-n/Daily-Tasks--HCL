package com.auction.lab;

import java.util.Scanner;

/**
 * Interactive console runner to launch each memory experiment from one place.
 */
public class MemoryLabRunner {

    public static void main(String[] args) {
        if (args.length > 0) {
            String exp = args[0].trim();
            switch (exp) {
                case "1" -> StackOverflowExperiment.main(new String[0]);
                case "2" -> OutOfMemoryExperiment.main(new String[0]);
                case "3" -> StringVsStringBuilderExperiment.main(new String[0]);
                case "4" -> GarbageCollectionExperiment.main(new String[0]);
                case "5" -> IntegerCacheExperiment.main(new String[0]);
                default -> runAll();
            }
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("=========================================================");
        System.out.println("   ONLINE AUCTION SYSTEM - DAY 10: MEMORY MANAGEMENT LAB ");
        System.out.println("=========================================================");
        System.out.println("1. Run Experiment 1: StackOverflowError Depth");
        System.out.println("2. Run Experiment 2: OutOfMemoryError (Heap Space)");
        System.out.println("3. Run Experiment 3: String += vs StringBuilder Timing");
        System.out.println("4. Run Experiment 4: Garbage Collection & Reachability");
        System.out.println("5. Run Experiment 5: Boxed Integer Cache (== vs equals)");
        System.out.println("6. Run All Experiments (Sequential)");
        System.out.println("0. Exit");
        System.out.print("Select an experiment (0-6): ");

        if (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> StackOverflowExperiment.main(new String[0]);
                case "2" -> OutOfMemoryExperiment.main(new String[0]);
                case "3" -> StringVsStringBuilderExperiment.main(new String[0]);
                case "4" -> GarbageCollectionExperiment.main(new String[0]);
                case "5" -> IntegerCacheExperiment.main(new String[0]);
                case "6" -> runAll();
                default -> System.out.println("Exiting Memory Lab Runner.");
            }
        }
    }

    private static void runAll() {
        System.out.println("\n--- RUNNING ALL EXPERIMENTS ---");
        StackOverflowExperiment.main(new String[0]);
        System.out.println();
        StringVsStringBuilderExperiment.main(new String[0]);
        System.out.println();
        IntegerCacheExperiment.main(new String[0]);
        System.out.println();
        GarbageCollectionExperiment.main(new String[0]);
        System.out.println();
        OutOfMemoryExperiment.main(new String[0]);
    }
}
