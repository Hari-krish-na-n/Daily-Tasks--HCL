# Day 11: AI Integration (Core Java) & FR7–FR8 Implementation

**Author:** Hari Krishnan  
**Training Program:** HCL Core Java Training — Online Auction System  
**Module:** Day-11 / day11-ai-integration-core-java  

---

## Overview

Day 11 focuses on **AI Integration in Core Java Development**, demonstrating how to critically evaluate AI-generated code, conduct prompt engineering comparisons, fix real architectural and domain defects, and implement functional requirements **FR7 (Auction Closing & Winner Declaration)** and **FR8 (Settlement & Shipment)** within our Online Auction System.

---

## Key Modules & Deliverables

### 1. Student POJO Prompt Engineering (Vague vs Specific)
- **Vague Prompt:** `"Create a Student class in Java."`
- **Specific Prompt:** `"Create a Java Student POJO with private fields for studentId, name, and email. Include appropriate constructors, getters, setters, equals(), hashCode(), and toString(). Validate required fields and avoid exposing mutable internal collections. Follow the Java version and coding conventions used in this repository."`
- **Implementation:** [Student.java](src/main/java/com/auction/model/Student.java) featuring private fields, compiled regex email validation, positive ID guard clauses, and `equals()` / `hashCode()` based on `studentId`.

### 2. StudentService Implementation & Critical Code Review
- **Implementation:** [StudentService.java](src/main/java/com/auction/service/StudentService.java)
- **6 Genuine Issues Found and Fixed:** Documented in [AI-CODE-REVIEW.md](AI-CODE-REVIEW.md):
  1. Internal mutable collection leakage fixed via `Collections.unmodifiableList()`.
  2. Duplicate ID collisions resolved with `LinkedHashMap` and rejection checks.
  3. Slow $\mathcal{O}(n)$ scan replaced by $\mathcal{O}(1)$ key lookup.
  4. Missing boundary and blank-string validation fixed.
  5. Swallowed errors and nulls replaced with `Optional<Student>`.
  6. Silent update failures replaced with boolean status confirmation.

### 3. Entity & Generic Repository Reuse
- **Entity:** Reused existing `Bid` and `Auction` domain models, introduced `Settlement` (extending `BaseEntity`).
- **Repository:** Reused Day 9 generic `CrudRepository<T, ID>` and `InMemoryRepository<T, ID>`:
  - [BidRepository.java](src/main/java/com/auction/repository/BidRepository.java)
  - [InMemoryBidRepository.java](src/main/java/com/auction/repository/InMemoryBidRepository.java) providing specialized queries: `findByAuctionId()`, `findByBidderId()`, and `findHighestBid()`.

### 4. Functional Requirements (FR7 & FR8)
- **FR7 — Auction Closing & Winner Declaration:**
  - Evaluates auction closing at end time.
  - If highest bid $\ge$ reserve price: marks status `SOLD`, declares winner, and initializes settlement escrow.
  - If highest bid $<$ reserve price: marks status `UNSOLD` (reserve not met).
- **FR8 — Settlement & Shipment:**
  - Simulates winner payment into platform escrow.
  - Deducts 5% platform fee (`AuctionConstants.PLATFORM_FEE_PERCENTAGE = 5.0`).
  - Records courier name and tracking number upon seller shipment.
  - Releases net payout to seller only after buyer payment and shipment confirmation.
- **Service:** [AuctionFulfillmentService.java](src/main/java/com/auction/service/AuctionFulfillmentService.java)

---

## Build, Test, and Run Instructions

### 1. Run All Automated Unit Tests
```bash
mvn test
```

### 2. Run Interactive Console Application
```bash
mvn compile exec:java -Dexec.mainClass="com.auction.app.Day11App"
```
Or directly using java:
```bash
java -cp target/classes com.auction.app.Day11App
```

### 3. Run Automated Demonstration Mode
```bash
java -cp target/classes com.auction.app.Day11App --demo
```

### Sample Automated Output
```text
=================================================
       ONLINE AUCTION SYSTEM - DAY 11
   AI Integration (Core Java) & FR7-FR8
=================================================

--- RUNNING AUTOMATED DEMONSTRATION OF DAY 11 ---

1. Student POJO & StudentService verification:
   Student[ID=1, Name='Hari Krishnan', Email='hari@training.hcl.com']
   Student[ID=2, Name='Ravi Kumar', Email='ravi.kumar@example.com']

2. Specialized BidRepository verification:
   Highest bid on Auction 1001: INR 145,000.00 by BidderKarthik

3. FR7 - Auction Closing & Winner Declaration (Reserve Met):
   Auction #1001 CLOSED [SOLD]: Winner=BidderKarthik (ID: 202), Winning Bid=INR 145,000.00 (Reserve: INR 140,000.00)

4. FR7 - Auction Closing (Reserve NOT Met):
   Auction #1002 CLOSED [UNSOLD]: Final Bid=INR 18,000.00, Reserve=INR 25,000.00. Message: Reserve price of INR 25,000.00 not met (Highest bid: INR 18,000.00). Item marked UNSOLD.

5. FR8 - Settlement & Shipment Workflow for Auction 1001:
   Created Settlement: Gross=INR 145,000.00 | Platform Fee (5%)=INR 7,250.00 | Net Payout=INR 137,750.00
   Step A: Buyer payment simulation...
   Buyer payment received! Escrow locked.
   Step B: Seller shipment submission...
   Shipment confirmed! Payout released to seller: INR 137,750.00
   Final Settlement Status: Settlement #8001 [Auction #1001] Gross: INR 145,000.00 | Fee: INR 7,250.00 | Net Payout: INR 137,750.00 | Paid: true | Shipped: true (Courier: BlueDart Express, Track: BD-98234-IN) | Payout Released: true

--- DEMONSTRATION COMPLETE ---
```
