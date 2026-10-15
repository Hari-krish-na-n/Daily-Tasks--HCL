package com.auction.demo;

import com.auction.model.Auction;
import com.auction.model.Seller;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Focused demonstrations of Java Collections Framework interfaces and classes:
 * 1. List: ArrayList vs LinkedList (preserves insertion order, indexed access, duplicate allowance)
 * 2. Set: HashSet vs LinkedHashSet vs TreeSet (uniqueness, iteration order, sorting)
 * 3. Queue: ArrayDeque (FIFO waitlist) vs PriorityQueue (comparator-based priority)
 * 4. Iteration Patterns:
 *    - Enhanced for-loop
 *    - Iterator.remove() for safe element removal
 *    - ListIterator for bi-directional traversal
 *    - Safe reproduction & recovery from ConcurrentModificationException
 */
public class CollectionsShowcase {

    public static void runAllDemonstrations() {
        System.out.println("=================================================");
        System.out.println("    JAVA COLLECTIONS FRAMEWORK DEMONSTRATION");
        System.out.println("=================================================");

        demoListImplementations();
        demoSetImplementations();
        demoQueueImplementations();
        demoIterationPatterns();
        demoConcurrentModificationException();
    }

    /**
     * 1. List Demonstrations: ArrayList and LinkedList
     */
    public static void demoListImplementations() {
        System.out.println("\n--- 1. LIST DEMONSTRATION: ArrayList vs LinkedList ---");

        // ArrayList: backed by a dynamic resizable array
        // - Fast random access O(1) by index
        // - Preserves insertion order
        // - Allows duplicates
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Vintage Camera");
        arrayList.add("Gaming Laptop");
        arrayList.add("Smart Watch");
        arrayList.add("Vintage Camera"); // duplicate allowed

        System.out.println("ArrayList content (allows duplicates, preserves order):");
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println("  Index " + i + " -> " + arrayList.get(i));
        }

        // LinkedList: doubly linked list
        // - Fast insertion/deletion at both ends O(1)
        // - Slower indexed access O(n) because nodes must be traversed
        List<String> linkedList = new LinkedList<>(arrayList);
        linkedList.add(0, "Collector Coin (Inserted at Head)");

        System.out.println("\nLinkedList content after inserting at head:");
        for (String item : linkedList) {
            System.out.println("  * " + item);
        }
        System.out.println("Comparison Summary:");
        System.out.println("  ArrayList is optimal for reading/iterating with get(index).");
        System.out.println("  LinkedList is preferred when frequently inserting/deleting elements at the ends.");
    }

    /**
     * 2. Set Demonstrations: HashSet, LinkedHashSet, TreeSet
     */
    public static void demoSetImplementations() {
        System.out.println("\n--- 2. SET DEMONSTRATION: HashSet vs LinkedHashSet vs TreeSet ---");

        String[] usernames = {"krishna", "meera", "ravi", "arjun", "krishna", "deepak"};

        // A. HashSet:
        // - Hash table backed (HashMap internally)
        // - No guaranteed order (order may change across JVM runs)
        // - O(1) average lookup, add, contains
        Set<String> hashSet = new HashSet<>();
        for (String u : usernames) {
            hashSet.add(u);
        }
        System.out.println("HashSet (duplicates discarded, iteration order unpredictable):");
        System.out.println("  Elements: " + hashSet);

        // B. LinkedHashSet:
        // - Hash table + doubly-linked list
        // - Guarantees insertion order during iteration
        // - Slightly more memory overhead than HashSet
        Set<String> linkedHashSet = new LinkedHashSet<>();
        for (String u : usernames) {
            linkedHashSet.add(u);
        }
        System.out.println("LinkedHashSet (duplicates discarded, strict insertion order preserved):");
        System.out.println("  Elements: " + linkedHashSet);

        // C. TreeSet:
        // - Red-Black tree backed (NavigableSet)
        // - Elements maintained in sorted natural order (or custom Comparator)
        // - O(log n) time complexity for basic operations
        Set<String> treeSet = new TreeSet<>();
        for (String u : usernames) {
            treeSet.add(u);
        }
        System.out.println("TreeSet (duplicates discarded, sorted natural alphabetical order):");
        System.out.println("  Elements: " + treeSet);
    }

    /**
     * 3. Queue Demonstrations: ArrayDeque vs PriorityQueue
     */
    public static void demoQueueImplementations() {
        System.out.println("\n--- 3. QUEUE DEMONSTRATION: ArrayDeque (FIFO) vs PriorityQueue (Priority) ---");

        // A. ArrayDeque as standard FIFO Queue
        Queue<String> fifoQueue = new ArrayDeque<>();
        fifoQueue.offer("Bidder 1: Alice");
        fifoQueue.offer("Bidder 2: Bob");
        fifoQueue.offer("Bidder 3: Charlie");

        System.out.println("ArrayDeque FIFO Polling Order (First In, First Out):");
        while (!fifoQueue.isEmpty()) {
            System.out.println("  Served -> " + fifoQueue.poll());
        }

        // B. PriorityQueue with explicit priority
        // NOTE: PriorityQueue orders by comparator, not insertion order!
        class PriorityRequest {
            final String name;
            final int priorityRank; // 1 = Highest (Faculty/VIP), 2 = Standard

            PriorityRequest(String name, int priorityRank) {
                this.name = name;
                this.priorityRank = priorityRank;
            }

            @Override
            public String toString() {
                return name + " [Priority Tier: " + (priorityRank == 1 ? "HIGH/FACULTY" : "STANDARD") + "]";
            }
        }

        Queue<PriorityRequest> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(r -> r.priorityRank)
        );

        // Adding standard users first, then high-priority faculty
        priorityQueue.offer(new PriorityRequest("Student User", 2));
        priorityQueue.offer(new PriorityRequest("Guest Bidder", 2));
        priorityQueue.offer(new PriorityRequest("Faculty Member Dr. Rao", 1));
        priorityQueue.offer(new PriorityRequest("Dean Prof. Sharma", 1));

        System.out.println("\nPriorityQueue Polling Order (Priority 1 served before Priority 2):");
        while (!priorityQueue.isEmpty()) {
            System.out.println("  Processed -> " + priorityQueue.poll());
        }
    }

    /**
     * 4. Iteration Patterns: Enhanced For-Loop, Iterator, ListIterator
     */
    public static void demoIterationPatterns() {
        System.out.println("\n--- 4. ITERATION PATTERNS: For-Each, Iterator, and ListIterator ---");

        List<String> items = new ArrayList<>(List.of("Laptop", "Mouse", "Keyboard", "Monitor"));

        // A. Enhanced For-Loop: Clean reading
        System.out.println("A. Enhanced For-Loop (Read-only traversal):");
        for (String item : items) {
            System.out.println("  Item: " + item);
        }

        // B. Iterator with safe remove()
        System.out.println("\nB. Iterator.remove() (Safe element removal during iteration):");
        Iterator<String> iterator = items.iterator();
        while (iterator.hasNext()) {
            String current = iterator.next();
            if (current.equals("Mouse")) {
                System.out.println("  Safely removing: " + current);
                iterator.remove(); // safe!
            }
        }
        System.out.println("  Remaining list after Iterator.remove: " + items);

        // C. ListIterator: forward and backward traversal and modification
        System.out.println("\nC. ListIterator (Bi-directional traversal & inline replacement):");
        ListIterator<String> listIterator = items.listIterator();
        System.out.println("  Forward traversal:");
        while (listIterator.hasNext()) {
            int index = listIterator.nextIndex();
            String val = listIterator.next();
            System.out.println("    Index " + index + ": " + val);
            if (val.equals("Keyboard")) {
                listIterator.set("Mechanical Keyboard (Upgraded)");
            }
        }

        System.out.println("  Backward traversal from end:");
        while (listIterator.hasPrevious()) {
            int prevIndex = listIterator.previousIndex();
            String prevVal = listIterator.previous();
            System.out.println("    Index " + prevIndex + ": " + prevVal);
        }
    }

    /**
     * 5. ConcurrentModificationException demonstration (Isolated & Caught)
     */
    public static void demoConcurrentModificationException() {
        System.out.println("\n--- 5. ConcurrentModificationException DEMONSTRATION ---");
        System.out.println("Note: This occurs when structurally modifying a collection (add/remove)");
        System.out.println("directly while an enhanced-for loop or Iterator is iterating through it.");
        System.out.println("It does NOT require multi-threading; fail-fast iterators detect modCount mismatch.\n");

        List<String> items = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Date"));

        // Unsafe pattern: structurally modifying during for-each
        try {
            System.out.println("[UNSAFE ATTEMPT] Modifying list directly inside for-each loop...");
            for (String fruit : items) {
                if (fruit.equals("Banana")) {
                    items.remove(fruit); // Direct modification triggers fail-fast check!
                }
            }
        } catch (java.util.ConcurrentModificationException cme) {
            System.out.println(">>> EXPECTED EXCEPTION CAUGHT: " + cme.getClass().getSimpleName());
            System.out.println("    Explanation: modCount changed from underneath the Iterator without its knowledge.");
        }

        // Correct approach: using Iterator.remove()
        System.out.println("\n[SAFE APPROACH] Using iterator.remove() to avoid ConcurrentModificationException:");
        List<String> safeList = new ArrayList<>(List.of("Apple", "Banana", "Cherry", "Date"));
        Iterator<String> safeIterator = safeList.iterator();
        while (safeIterator.hasNext()) {
            String fruit = safeIterator.next();
            if (fruit.equals("Banana")) {
                safeIterator.remove(); // Safely updates both modCount and expectedModCount!
            }
        }
        System.out.println("    Resulting list: " + safeList + " (Cleanly removed 'Banana')");
    }
}
