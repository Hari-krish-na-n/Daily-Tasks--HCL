package com.auction.lab;

/**
 * Experiment 5: Boxed Integer Cache vs Object References
 *
 * Demonstrates:
 * 1. Java Language Specification (JLS §5.1.7) requires the JVM to cache boxed Integer objects
 *    in the range [-128, 127] when autoboxing or calling Integer.valueOf(int).
 * 2. Within this cached range (e.g. 100), two separate autoboxed references point to the exact
 *    same shared instance in the heap cache, so '==' evaluates to true.
 * 3. Outside this range (e.g. 1000), autoboxing creates distinct new Integer heap objects
 *    (unless modified by JVM flag -XX:AutoBoxCacheMax), so '==' evaluates to false because
 *    '==' tests reference identity (memory addresses), NOT numeric value.
 * 4. In contrast, 'equals()' compares the actual underlying primitive values in all cases,
 *    returning true whenever the numeric contents match.
 * 5. Best Practice in business entities: Always use '.equals()' when comparing boxed wrapper types
 *    (Integer, Long) to prevent elusive identity bugs.
 */
public class IntegerCacheExperiment {

    public static void main(String[] args) {
        System.out.println("=== Experiment 5: Boxed Integer Cache and '==' vs '.equals()' ===");

        // Test Case A: Inside the guaranteed cache range [-128, 127]
        System.out.println("\n--- Test A: Comparing boxed integers with value 100 (inside cache range) ---");
        Integer a1 = 100; // Integer.valueOf(100)
        Integer a2 = 100; // Returns cached object
        System.out.println("Integer a1 = 100; Integer a2 = 100;");
        System.out.println("a1 == a2 :      " + (a1 == a2) + "  (Identical object reference from cache)");
        System.out.println("a1.equals(a2) : " + a1.equals(a2) + "  (Values are numerically equal)");
        System.out.printf("Identity hashCodes -> a1: %d, a2: %d%n",
                System.identityHashCode(a1), System.identityHashCode(a2));

        // Test Case B: Outside the default cache range
        System.out.println("\n--- Test B: Comparing boxed integers with value 1000 (outside cache range) ---");
        Integer b1 = 1000; // Integer.valueOf(1000) -> new Integer instance
        Integer b2 = 1000; // new Integer instance
        System.out.println("Integer b1 = 1000; Integer b2 = 1000;");
        System.out.println("b1 == b2 :      " + (b1 == b2) + " (Different heap object references!)");
        System.out.println("b1.equals(b2) : " + b1.equals(b2) + "  (Values are numerically equal)");
        System.out.printf("Identity hashCodes -> b1: %d, b2: %d%n",
                System.identityHashCode(b1), System.identityHashCode(b2));

        // Test Case C: Explicit new Integer vs valueOf / Autoboxing
        System.out.println("\n--- Test C: Boundary values [-128, 127] ---");
        Integer low1 = -128;
        Integer low2 = -128;
        System.out.println("-128 == -128: " + (low1 == low2));

        Integer high1 = 127;
        Integer high2 = 127;
        System.out.println("127 == 127:   " + (high1 == high2));

        Integer above1 = 128;
        Integer above2 = 128;
        System.out.println("128 == 128:   " + (above1 == above2));

        System.out.println("\nSummary:");
        System.out.println("For wrapper classes in Java, always use .equals() or unbox to primitive before comparison.");
    }
}
