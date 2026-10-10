# Day 6: Exception Handling + AI-Assisted Debugging — Online Auction System

This project is part of my Java training tasks for **Day 6**, continuing directly from the Online Auction System developed during Days 1 to 5.

---

## 1. Objectives & Overview

In this project, I implemented professional exception handling mechanisms, in-memory inventory order processing, and audit logging into the Online Auction System:
1. **Checked Exception (`InsufficientStockException`):** Represents business rule failures when requested quantities exceed available inventory.
2. **Unchecked Exception (`InvalidQuantityException`):** Thrown when quantities requested are non-positive (zero or negative).
3. **Unchecked Business Exception (`InvalidBidException`):** Thrown when bid amounts submitted are zero or negative.
4. **OrderProcessor Service:** Manages in-memory inventory with atomic state changes (stock is never partially deducted when an operation fails).
5. **Exception Chaining:** Wrapping lower-level domain exceptions into `OrderProcessingException` while preserving original causes accessible via `getCause()`.
6. **Auditing with `finally`:** `AuditService` logs every operation attempt (success or failure) via `finally` blocks.
7. **Graceful Console Menu Recovery:** Safe input parsing and error recovery so that invalid inputs or business exceptions never terminate the console menu prematurely.
8. **AI-Assisted Debugging Analysis:** Documentation of two real JVM stack traces, AI analysis, root cause inspection, and fixes.

---

## 2. Project Structure

```text
Day-06/day6-exception-handling/
├── pom.xml
├── README.md
├── docs/
│   ├── EXCEPTION_HANDLING_NOTES.md
│   └── AI_DEBUGGING_NOTES.md
└── src/
    ├── main/java/com/auction/
    │   ├── app/
    │   │   └── AuctionApp.java
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
    │   │   ├── AuctionItem.java
    │   │   ├── AuctionRole.java
    │   │   ├── BaseEntity.java
    │   │   ├── Bidder.java
    │   │   ├── Seller.java
    │   │   └── User.java
    │   ├── service/
    │   │   ├── AuditService.java
    │   │   ├── AuctionService.java
    │   │   └── OrderProcessor.java
    │   └── strategy/
    │       ├── BidValidationStrategy.java
    │       ├── PremiumBidValidationStrategy.java
    │       └── StandardBidValidationStrategy.java
    └── test/java/com/auction/
        ├── learning/payment/PaymentTest.java
        ├── model/AuctionRoleTest.java
        ├── model/InheritanceTest.java
        ├── service/ExceptionHandlingTest.java
        ├── strategy/PremiumBidValidationStrategyTest.java
        └── strategy/StandardBidValidationStrategyTest.java
```

---

## 3. Business Rules Implemented

### Rule 1: Invalid Bid Amount (Unchecked Exception)
- A bid must be strictly positive (`bidAmount > 0`).
- If an amount of `0.0` or negative is passed, an `InvalidBidException` is thrown.
- The item's `currentBid` is **not modified**, ensuring state integrity.

### Rule 2: Order Quantity & Stock Fulfillment (Checked & Unchecked)
- Quantity requested must be strictly positive (`requestedQty > 0`), otherwise `InvalidQuantityException` is thrown.
- Requested quantity cannot exceed available stock. If it does, a checked `InsufficientStockException` is thrown.
- Inventory is updated **only upon valid fulfillment**. If an exception occurs, stock remains intact.

### Rule 3: Audit Trail Execution via `finally`
- Every operation (`PLACE_BID`, `PROCESS_ORDER`) records an audit entry through a `finally` block in `AuctionService` and `OrderProcessor`.
- Guarantees an audit trail whether the operation succeeds or throws an exception.

---

## 4. How to Build and Run

### Prerequisites
- Java 21 or higher
- Apache Maven 3.8+

### Commands

1. **Run Unit Tests:**
   ```bash
   cd Day-06/day6-exception-handling
   mvn clean test
   ```
   *(Runs all 45 automated JUnit 5 tests)*

2. **Package the Executable JAR:**
   ```bash
   mvn clean package
   ```

3. **Run the Automated Demonstration Mode:**
   ```bash
   java -jar target/day6-exception-handling-1.0-SNAPSHOT.jar --demo
   ```

4. **Run the Interactive Console Menu:**
   ```bash
   java -jar target/day6-exception-handling-1.0-SNAPSHOT.jar
   ```

---

## 5. Test Suite Verification

The test suite contains 45 JUnit 5 tests covering:
- Valid stock reduction and atomic rollback on failure.
- Zero/negative quantity validation (`InvalidQuantityException`).
- Insufficient stock validation (`InsufficientStockException`).
- Zero/negative bid validation (`InvalidBidException`).
- Exception cause preservation (`OrderProcessingException.getCause()`).
- Unconditional `finally` audit execution.
- Day 5 inherited payment and strategy patterns.
