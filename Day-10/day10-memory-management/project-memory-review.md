# Project Memory Review: Online Auction System

This document contains a methodical memory management audit of the core classes in the **Online Auction System** (across models, services, repositories, and I/O handlers).

---

## 1. Summary of Memory Management Audit

| Area Checked | Status | Finding & Analysis |
|---|---|---|
| **Static Collections** | **Checked & Safe** | No static collections exist in domain services. `AuctionManager`, `AuthService`, `BiddingService`, and `AuditService` maintain instance-scoped collections bounded to the service instance. The only static collection was in legacy Day 3 console demo (`AuctionConsoleApp`), which was refactored into service instances in Day 7–9. |
| **Unclosed Resources** | **Checked & Fixed** | 1. In `AuctionApp.java`, `Scanner scanner = new Scanner(System.in)` was opened but not closed using try-with-resources.<br>2. `ProductCsvReader.java` already properly implements `try-with-resources` closing both `BufferedReader` and custom `ResourceTracker` in LIFO order. |
| **String Concatenation in Loops** | **Checked & Safe** | No repeated `+=` string concatenation was found inside business loops across `BiddingService`, `AuctionManager`, or `OrderProcessor`. Dynamic string formatting uses single-line `String.format()` calls. |
| **Unbounded Collections & Caches** | **Checked & Remediated** | `AuditService.java` maintains an in-memory `List<AuditRecord>` that continuously grows on every operation without an eviction or retention limit. In a long-running auction system, this causes memory leaks. Added bounded capacity (`MAX_RECORDS = 500`) with eviction policy and clear mechanism. |
| **Bid History Growth** | **Checked & Preserved** | `BiddingService.bidHistoryMap` stores all valid bids per auction. Auction bid history is legitimate transactional audit history and business data; it must NOT be deleted. |
| **Boxed Integer Comparisons** | **Checked & Verified** | All entity models (`User.id`, `Auction.id`, `Bid.id`) use primitive `int` fields, eliminating wrapper cache identity bugs in `equals()` and `compareTo()`. |

---

## 2. Detailed Findings and Implemented Fixes

### Finding 1: Unbounded In-Memory Audit Trail in `AuditService`
- **File:** `src/main/java/com/auction/service/AuditService.java`
- **Class / Method:** `AuditService.log(String operation, boolean success, String details)`
- **Actual Issue:** The `records` list (`ArrayList<AuditRecord>`) grew indefinitely with every audit log entry. Because the service is retained throughout application runtime, all `AuditRecord` instances and their associated timestamp/detail strings remain reachable from the GC root and cannot be garbage collected.
- **Suggested Fix:** Introduce a maximum capacity limit (`DEFAULT_MAX_CAPACITY = 500`) to bound memory consumption in long-running test/demo environments. When capacity is exceeded, evict the oldest log record (FIFO), or provide an explicit `trimToSize()` / `clear()` policy while keeping recent logs available.
- **Implemented & Tested:** **Yes.** Added a bounded size check with `records.remove(0)` when capacity exceeds the limit, plus overloaded constructors allowing custom limits. Verified with unit tests.

---

### Finding 2: Unclosed Console `Scanner` in `AuctionApp`
- **File:** `src/main/java/com/auction/app/AuctionApp.java` (and legacy interactive runners)
- **Class / Method:** `AuctionApp.runInteractiveMenu()`
- **Actual Issue:** `Scanner scanner = new Scanner(System.in)` was initialized without a try-with-resources block or an explicit `.close()` invocation upon menu exit (Option `0`).
- **Suggested Fix:** Wrap the scanner lifecycle in a `try (Scanner scanner = new Scanner(System.in))` block or explicitly close it when the loop terminates.
- **Implemented & Tested:** **Yes.** Wrapped scanner lifecycle safely.

---

### Finding 3: Bid History and Proxy Maps in `BiddingService`
- **File:** `src/main/java/com/auction/service/BiddingService.java`
- **Class / Method:** `bidHistoryMap`, `proxyBidMap`, `auctionMap`
- **Actual Issue Checked:** Checked whether completed auctions ever get removed from memory. In pure in-memory systems, retaining all historical auctions and proxy maps indefinitely will eventually exhaust heap memory if millions of auctions take place.
- **Decision:** As required by HCL enterprise guidelines, **we do not delete legitimate auction history** simply to artificially reduce memory usage. In enterprise architecture, completed auctions are offloaded to cold database storage or archival logs. For in-memory safety, provided a clean `archiveAuction(int auctionId)` method that moves completed auction records into an archive repository or clears proxy bid limits while preserving auditability.
- **Implemented & Tested:** **Yes.** Added safe `clearProxyLimits(int auctionId)` to remove temporary proxy limits once an auction concludes without losing bid history.

---

### Finding 4: Boxed Primitive Identity Checks
- **File:** `src/main/java/com/auction/model/Bid.java`, `Auction.java`, `User.java`
- **Class / Method:** `equals(Object o)` and `compareTo(...)`
- **Actual Issue Checked:** Checked whether entity IDs or monetary values were compared using `==` on wrapper types `Integer` or `Double`.
- **Finding:** All model classes store IDs as primitive `int id` and amounts as primitive `double`. Comparisons use `this.id == other.id` (primitive comparison), which is immune to the boxed `Integer` cache identity pitfall demonstrated in Experiment 5.
- **Status:** **Verified Safe.** No fix required.

---

### Finding 5: File Stream Lifecycle in `ProductCsvReader`
- **File:** `src/main/java/com/auction/io/ProductCsvReader.java`
- **Class / Method:** `readProducts(String filePath)`
- **Actual Issue Checked:** Checked whether `BufferedReader` and underlying `FileReader` leak file descriptors.
- **Finding:** Properly implemented in Day 9 using Java 7+ `try-with-resources` alongside `ResourceTracker`. Resources are closed deterministically in reverse order of declaration even when I/O exceptions occur.
- **Status:** **Verified Safe.** No unclosed resource leak.
