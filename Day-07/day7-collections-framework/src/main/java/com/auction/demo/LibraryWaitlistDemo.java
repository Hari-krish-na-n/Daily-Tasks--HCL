package com.auction.demo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Self-contained Library Waitlist training exercise.
 *
 * Demonstrates:
 * 1. ArrayList as a book catalogue (ordered catalogue, indexed access)
 * 2. LinkedHashSet to prevent duplicate request IDs while preserving request insertion order
 * 3. ArrayDeque as a standard FIFO borrower waitlist
 * 4. PriorityQueue to serve faculty-first requests ahead of student requests
 * 5. Iterator.remove() to safely remove overdue book reservations during iteration
 */
public class LibraryWaitlistDemo {

    public static class Book {
        private final String isbn;
        private final String title;
        private final String author;
        private boolean available;

        public Book(String isbn, String title, String author, boolean available) {
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.available = available;
        }

        public String getIsbn() { return isbn; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }

        @Override
        public String toString() {
            return String.format("[%s] '%s' by %s (Available: %s)", isbn, title, author, available);
        }
    }

    public enum PatronType {
        FACULTY(1),
        STUDENT(2),
        COMMUNITY(3);

        private final int priorityTier;

        PatronType(int priorityTier) {
            this.priorityTier = priorityTier;
        }

        public int getPriorityTier() {
            return priorityTier;
        }
    }

    public static class BorrowRequest {
        private final String requestId;
        private final String patronName;
        private final PatronType patronType;
        private final String bookIsbn;
        private int daysOverdue; // 0 if not overdue

        public BorrowRequest(String requestId, String patronName, PatronType patronType, String bookIsbn, int daysOverdue) {
            this.requestId = requestId;
            this.patronName = patronName;
            this.patronType = patronType;
            this.bookIsbn = bookIsbn;
            this.daysOverdue = daysOverdue;
        }

        public String getRequestId() { return requestId; }
        public String getPatronName() { return patronName; }
        public PatronType getPatronType() { return patronType; }
        public String getBookIsbn() { return bookIsbn; }
        public int getDaysOverdue() { return daysOverdue; }

        @Override
        public String toString() {
            return String.format("Req#%s | %s (%s) | Book: %s | OverdueDays: %d",
                    requestId, patronName, patronType, bookIsbn, daysOverdue);
        }
    }

    public static void runDemo() {
        System.out.println("=================================================");
        System.out.println("          LIBRARY WAITLIST EXERCISE");
        System.out.println("=================================================");

        // 1. ArrayList as a book catalogue
        // ArrayList chosen: supports indexed browsing, preserves catalogue order, fast iteration
        List<Book> catalogue = new ArrayList<>();
        catalogue.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch", true));
        catalogue.add(new Book("978-0132350884", "Clean Code", "Robert C. Martin", false));
        catalogue.add(new Book("978-0321356680", "Java Concurrency in Practice", "Brian Goetz", true));
        catalogue.add(new Book("978-0201633610", "Design Patterns", "Gang of Four", false));

        System.out.println("\n--- 1. Book Catalogue (ArrayList) ---");
        for (int i = 0; i < catalogue.size(); i++) {
            System.out.println("  Shelf Slot " + i + ": " + catalogue.get(i));
        }

        // 2. LinkedHashSet for deduplicating request IDs
        // LinkedHashSet chosen: prevents duplicate submissions while preserving arrival order
        System.out.println("\n--- 2. Request ID De-duplication (LinkedHashSet) ---");
        Set<String> requestIds = new LinkedHashSet<>();
        String[] incomingIds = {"REQ-101", "REQ-102", "REQ-101", "REQ-103", "REQ-102", "REQ-104"};

        for (String id : incomingIds) {
            boolean added = requestIds.add(id);
            System.out.println("  Submitting " + id + " -> Added: " + added);
        }
        System.out.println("  Final Unique Request IDs (Order preserved): " + requestIds);

        // 3. ArrayDeque as standard FIFO waitlist
        // ArrayDeque chosen: FIFO waitlist queue with efficient head and tail operations
        System.out.println("\n--- 3. Standard FIFO Waitlist (ArrayDeque) ---");
        Queue<BorrowRequest> fifoWaitlist = new ArrayDeque<>();
        fifoWaitlist.offer(new BorrowRequest("REQ-101", "Aakash (Student)", PatronType.STUDENT, "978-0132350884", 0));
        fifoWaitlist.offer(new BorrowRequest("REQ-102", "Pooja (Student)", PatronType.STUDENT, "978-0132350884", 0));
        fifoWaitlist.offer(new BorrowRequest("REQ-103", "Dr. Varma (Faculty)", PatronType.FACULTY, "978-0132350884", 0));

        System.out.println("  Processing FIFO waitlist in arrival order:");
        while (!fifoWaitlist.isEmpty()) {
            System.out.println("    Serving -> " + fifoWaitlist.poll());
        }

        // 4. PriorityQueue for Faculty-First requests
        // PriorityQueue chosen: orders by explicit comparator so FACULTY (tier 1) is served before STUDENT (tier 2)
        System.out.println("\n--- 4. Faculty-First Priority Waitlist (PriorityQueue) ---");
        Queue<BorrowRequest> priorityWaitlist = new PriorityQueue<>(
                Comparator.comparingInt(r -> r.getPatronType().getPriorityTier())
        );

        // Re-enqueuing requests to the priority queue
        priorityWaitlist.offer(new BorrowRequest("REQ-101", "Aakash (Student)", PatronType.STUDENT, "978-0132350884", 0));
        priorityWaitlist.offer(new BorrowRequest("REQ-102", "Pooja (Student)", PatronType.STUDENT, "978-0132350884", 0));
        priorityWaitlist.offer(new BorrowRequest("REQ-103", "Dr. Varma (Faculty)", PatronType.FACULTY, "978-0132350884", 0));
        priorityWaitlist.offer(new BorrowRequest("REQ-104", "Prof. Nambiar (Faculty)", PatronType.FACULTY, "978-0132350884", 0));

        System.out.println("  Processing Priority waitlist (Faculty served before students):");
        while (!priorityWaitlist.isEmpty()) {
            System.out.println("    Served -> " + priorityWaitlist.poll());
        }

        // 5. Iterator.remove() for safely removing overdue book reservations
        System.out.println("\n--- 5. Safe Removal of Overdue Items (Iterator.remove()) ---");
        List<BorrowRequest> activeReservations = new ArrayList<>();
        activeReservations.add(new BorrowRequest("REQ-201", "Rohan", PatronType.STUDENT, "978-0134685991", 0));
        activeReservations.add(new BorrowRequest("REQ-202", "Sneha", PatronType.STUDENT, "978-0132350884", 12)); // overdue!
        activeReservations.add(new BorrowRequest("REQ-203", "Kiran", PatronType.STUDENT, "978-0321356680", 0));
        activeReservations.add(new BorrowRequest("REQ-204", "Vikram", PatronType.COMMUNITY, "978-0201633610", 7)); // overdue!

        System.out.println("  Reservations before purge (" + activeReservations.size() + " total):");
        for (BorrowRequest req : activeReservations) {
            System.out.println("    * " + req);
        }

        // Safe iterator removal
        Iterator<BorrowRequest> it = activeReservations.iterator();
        while (it.hasNext()) {
            BorrowRequest req = it.next();
            if (req.getDaysOverdue() > 0) {
                System.out.println("  [CANCELLED] Removing overdue reservation: " + req.getRequestId()
                        + " (" + req.getDaysOverdue() + " days overdue)");
                it.remove(); // safe removal prevents ConcurrentModificationException
            }
        }

        System.out.println("  Reservations after purge (" + activeReservations.size() + " active remaining):");
        for (BorrowRequest req : activeReservations) {
            System.out.println("    * " + req);
        }
    }
}
