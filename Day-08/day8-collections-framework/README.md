# Day 8: Collections Framework (Part 2) — Employee Directory & Auction Bidding Engine

This project forms part of the Java training tasks for **Day 8**, continuing directly from the Online Auction System developed across Days 1 through 7.

---

## 1. Overview & Objectives

Day 8 focuses on advanced aspects of the Java Collections Framework (JCF):
1. **Map Implementations & Operations:** Comparing `HashMap`, `LinkedHashMap`, and `TreeMap`, using core Map APIs (`get`, `put`, `containsKey`, `remove`, `getOrDefault`), and department frequency counting via `Map.merge()`.
2. **Comparable vs. Comparator:** Implementing natural ordering via `Comparable<Employee>` (by employee ID) and custom ordering via `Comparator` (salary descending with name ascending tie-breaking), generating a top-five salary report.
3. **equals() and hashCode() Contract:** Demonstrating why equal objects must produce equal hash codes, showing a deterministic, reproducible missing-`hashCode()` bug in `HashSet`/`HashMap`, followed by the corrected implementation.
4. **Online Auction System Bidding Engine (FR4–FR6):**
   - **FR4 — Bidding Validation:** Validating minimum bid increments (`currentHighestBid + minIncrement`), starting price rules, and auction time boundaries.
   - **FR5 — Proxy/Auto-Bidding:** Maintaining maximum proxy bid limits per bidder in nested Maps and automatically generating counter-bids up to the registered threshold.
   - **FR6 — Anti-Sniping:** Extending auction deadlines by 5 minutes whenever a valid bid is placed in the final 2 minutes.

---

## 2. Project Structure

```text
Day-08/day8-collections-framework/
├── pom.xml
├── README.md
├── docs/
│   ├── MAPS_AND_COMPARATORS.md
│   ├── EQUALS_HASHCODE_CONTRACT.md
│   └── FR4_FR6_SPECIFICATION.md
└── src/
    ├── main/java/
    │   ├── EmployeeDirectory.java              <-- Main demo entry point
    │   └── com/auction/
    │       ├── model/
    │       │   ├── Employee.java              <-- Comparable<Employee> + equals/hashCode
    │       │   ├── BuggyEmployee.java         <-- Missing hashCode() contract bug
    │       │   ├── Bid.java                   <-- Bid entity with Comparable<Bid>
    │       │   ├── BidResult.java             <-- Outcome value object
    │       │   └── ... (Auction, User, etc.)
    │       └── service/
    │           └── BiddingService.java        <-- FR4, FR5, FR6 engine
    └── test/java/
        ├── EmployeeDirectoryTest.java          <-- Map, sorting, equals/hashCode tests
        └── com/auction/service/
            └── BiddingServiceTest.java         <-- FR4, FR5, FR6 unit tests
```

---

## 3. Map Implementations & Ordering

| Map Implementation | Underlying Data Structure | Key Ordering Guarantee | Lookup / Insert Complexity | Best Use Case |
|---|---|---|---|---|
| `HashMap` | Hash table with buckets (array of linked nodes / red-black trees) | **None** (Unordered, depends on hash codes) | $\mathcal{O}(1)$ average | Fast key lookup by ID or username where order does not matter |
| `LinkedHashMap` | Hash table + doubly-linked list through entries | **Insertion order** (or access order) | $\mathcal{O}(1)$ average | Caches (LRU), maintaining chronological input order |
| `TreeMap` | Red-Black Tree (Self-balancing binary search tree) | **Sorted order** (Natural or custom Comparator) | $\mathcal{O}(\log N)$ | Range queries, sorted reports, alphabetical listings |

---

## 4. Comparable vs. Comparator

- **`Comparable<T>` (Natural Ordering):**
  - Defines the single inherent default ordering of an object.
  - Implemented by the class itself via `public int compareTo(T other)`.
  - In `Employee.java`, `compareTo()` sorts by `employeeId` ascending.
- **`Comparator<T>` (Custom Ordering):**
  - Defines alternate or multiple external sorting strategies without modifying the entity class.
  - Chaining supported with `Comparator.comparing(...).reversed().thenComparing(...)`.
  - In `EmployeeDirectory.java`, employees are sorted by salary descending, and tied salaries are broken alphabetically by employee name ascending.
- **Top-5 Report Safety:**
  - Safely computes `Math.min(5, list.size())` to avoid `IndexOutOfBoundsException` when fewer than 5 records exist.

---

## 5. equals() and hashCode() Contract

### Contract Rules
1. **Consistency:** If `o1.equals(o2) == true`, then `o1.hashCode() == o2.hashCode()` **MUST ALWAYS** be true.
2. **Reverse:** If `o1.hashCode() == o2.hashCode()`, `o1.equals(o2)` is *not required* to be true (hash collision).

### The Reproducible Bug (`BuggyEmployee`)
- When `equals()` is overridden on `employeeId` but `hashCode()` is omitted, the class inherits `Object.hashCode()` (based on memory/identity).
- Two distinct instances `b1` and `b2` with ID `1001` are equal according to `b1.equals(b2)`.
- However, `b1.hashCode() != b2.hashCode()`.
- **Consequences:**
  - `HashSet` adds both `b1` and `b2`, storing duplicate equal elements (size = 2).
  - `HashMap.put(b1, val)` places the entry in bucket $H_1$. `HashMap.get(b2)` checks bucket $H_2$ and returns `null`!

---

## 6. Functional Requirements (FR4 – FR6)

- **FR4 — Bidding Validation:** Validates that new bids strictly exceed the current highest bid by at least `DEFAULT_MIN_INCREMENT` (₹500.00). Bids below the increment or arriving past the auction end time are rejected.
- **FR5 — Proxy/Auto-Bidding:** Stores maximum proxy bid limits in `Map<auctionId, Map<bidderId, maxProxy>>`. When competing bids arrive, the system automatically counters with `competingBid + ₹500` up to each bidder's configured limit.
- **FR6 — Anti-Sniping:** If a valid bid arrives within the final 2 minutes (120 seconds) before `endTime`, the auction deadline is automatically extended by 5 minutes (`endTime.plusMinutes(5)`).

---

## 7. How to Build and Run

```bash
# Navigate to Day 8 directory
cd Day-08/day8-collections-framework

# Compile and run unit tests (14 tests)
mvn test

# Package executable JAR
mvn package

# Run the console demo application
java -jar target/day8-collections-framework-1.0-SNAPSHOT.jar
```
