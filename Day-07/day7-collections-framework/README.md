# Day 7: Collections Framework (Part 1) — Online Auction System

This project is part of my Java training tasks for **Day 7**, continuing directly from the Online Auction System developed during Days 1 to 6.

---

## 1. Objectives & Overview

In this module, I implemented functional requirements FR1 through FR3 using the Java Collections Framework (JCF), created focused collection demonstrations, built a standalone Library Waitlist exercise, and measured the performance characteristics of `ArrayList` versus `LinkedList`:

1. **FR1 — User Authentication:** User registration and login using `ArrayList<User>` (master registry) and `HashSet<String>` (instant $\mathcal{O}(1)$ duplicate username detection). Passwords are kept confidential and never printed.
2. **FR2 — Auction Listing Creation:** Sellers create auction listings with validation on start price, reserve price, and duration. Stored in `ArrayList<Auction>` and start in a `PENDING` state.
3. **FR3 — Admin Moderation:** Admins moderate pending listings using an `ArrayDeque<Auction>` FIFO queue. Listings transition to `APPROVED` or `REJECTED`, and repeated moderation is prevented.
4. **Collections Framework Showcase:**
   - `ArrayList` vs `LinkedList` for insertion and access patterns.
   - `HashSet` (unordered), `LinkedHashSet` (insertion-ordered), and `TreeSet` (naturally sorted).
   - `ArrayDeque` (standard FIFO) vs `PriorityQueue` (comparator-based priority).
   - Safe removal via `Iterator.remove()`, bi-directional traversal with `ListIterator`, and reproduction/recovery from `ConcurrentModificationException`.
5. **Library Waitlist Exercise:** Independent simulation featuring book catalogue (`ArrayList`), request ID deduplication (`LinkedHashSet`), FIFO borrowing (`ArrayDeque`), faculty-first queue (`PriorityQueue`), and overdue removal (`Iterator.remove()`).
6. **Performance Benchmarking:** Real nanosecond timing comparing `ArrayList` and `LinkedList` for head insertion and indexed `get(i)` lookups.

---

## 2. Project Structure

```text
Day-07/day7-collections-framework/
├── pom.xml
├── README.md
├── docs/
│   ├── COLLECTIONS_NOTES.md
│   ├── COLLECTION_TIMING.md
│   └── FR1_FR3_IMPLEMENTATION.md
└── src/
    ├── main/java/com/auction/
    │   ├── app/
    │   │   └── AuctionApp.java
    │   ├── demo/
    │   │   ├── CollectionsShowcase.java
    │   │   ├── LibraryWaitlistDemo.java
    │   │   └── ListPerformanceBenchmark.java
    │   ├── exception/
    │   │   ├── InsufficientStockException.java
    │   │   ├── InvalidBidException.java
    │   │   ├── InvalidQuantityException.java
    │   │   └── OrderProcessingException.java
    │   ├── learning/payment/
    │   │   ├── CardPayment.java
    │   │   ├── CashPayment.java
    │   │   ├── Payment.java
    │   │   ├── Refundable.java
    │   │   └── UpiPayment.java
    │   ├── model/
    │   │   ├── Admin.java
    │   │   ├── Auction.java
    │   │   ├── AuctionItem.java
    │   │   ├── AuctionRole.java
    │   │   ├── BaseEntity.java
    │   │   ├── Bidder.java
    │   │   ├── ListingStatus.java
    │   │   ├── Seller.java
    │   │   └── User.java
    │   ├── service/
    │   │   ├── AuditService.java
    │   │   ├── AuctionManager.java
    │   │   ├── AuctionService.java
    │   │   ├── AuthService.java
    │   │   └── OrderProcessor.java
    │   └── strategy/
    │       ├── BidValidationStrategy.java
    │       ├── PremiumBidValidationStrategy.java
    │       └── StandardBidValidationStrategy.java
    └── test/java/com/auction/
        ├── learning/payment/PaymentTest.java
        ├── model/AuctionRoleTest.java
        ├── model/InheritanceTest.java
        ├── service/CollectionsServiceTest.java
        ├── service/ExceptionHandlingTest.java
        ├── strategy/PremiumBidValidationStrategyTest.java
        └── strategy/StandardBidValidationStrategyTest.java
```

---

## 3. Collections Used & Justifications

| Feature / Class | Collection Used | Justification |
|---|---|---|
| User Registry (`AuthService`) | `ArrayList<User>` | Preserves chronological registration order, supports indexed traversal and fast reading. |
| Username Uniqueness (`AuthService`) | `HashSet<String>` | High-speed $\mathcal{O}(1)$ average membership check to reject duplicate usernames instantly. |
| Auction Catalogue (`AuctionManager`) | `ArrayList<Auction>` | Master catalogue supporting random access by index and clean tabular output in console. |
| Pending Moderation (`AuctionManager`) | `ArrayDeque<Auction>` | FIFO queue ensuring fairness (first submitted, first moderated) without node allocation overhead. |
| Library Catalogue (`LibraryWaitlistDemo`) | `ArrayList<Book>` | Ordered book inventory supporting indexed slot lookups. |
| Library Deduplication (`LibraryWaitlistDemo`) | `LinkedHashSet<String>` | Prevents duplicate requests while preserving arrival order. |
| Library Standard Waitlist | `ArrayDeque<BorrowRequest>` | Normal FIFO borrower queue. |
| Library Faculty Waitlist | `PriorityQueue<BorrowRequest>` | Prioritizes requests using an explicit comparator so faculty members are served first. |

---

## 4. How to Build and Run

### Prerequisites
- Java 21 or higher
- Apache Maven 3.8+

### Commands

1. **Run Unit Tests:**
   ```bash
   cd Day-07/day7-collections-framework
   mvn test
   ```
   *(Executes all 54 JUnit 5 tests, including FR1–FR3 tests and inherited domain tests)*

2. **Package the Executable JAR:**
   ```bash
   mvn package -DskipTests
   ```

3. **Run the Automated Demonstration Mode:**
   ```bash
   java -jar target/day7-collections-framework-1.0-SNAPSHOT.jar --demo
   ```

4. **Run the Interactive Console Application:**
   ```bash
   java -jar target/day7-collections-framework-1.0-SNAPSHOT.jar
   ```

5. **Run the Benchmark Standalone:**
   ```bash
   java -cp target/day7-collections-framework-1.0-SNAPSHOT.jar com.auction.demo.ListPerformanceBenchmark
   ```

---

## 5. Summary of Test Verification

- **Total Tests:** 54 passed (0 failures, 0 errors, 0 skipped).
- **FR1 Verification:** Confirmed bidder/seller registration, duplicate username rejection, login credential validation, and password confidentiality.
- **FR2 Verification:** Confirmed that newly created listings start in `PENDING` state and that invalid starting/reserve prices are rejected.
- **FR3 Verification:** Confirmed admin approval and rejection workflows, removal from the pending queue, and prohibition of repeated moderation.
- **Legacy Verification:** Confirmed that Days 1–6 components (inheritance, role hierarchy, custom exceptions, audit logging, payment polymorphism) remain fully functional.
