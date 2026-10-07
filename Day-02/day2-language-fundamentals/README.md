# Day 02 — Java Language Fundamentals
**Domain:** Online Auction System  
**Project:** `online-auction-system`  
**Training:** HCL Day 2

---

## 1. Overview

This module demonstrates core Java language fundamentals using the **Online Auction System** domain:
- 8 Primitive Data Types
- 1-D & 2-D Arrays
- Business Rule Constants (`AuctionConstants`)
- Operators (Arithmetic, Relational, Logical, Assignment, Increment/Decrement, Ternary)
- Type Casting (Widening & Narrowing)
- Integer Overflow and fix using `long`
- Floating-Point Precision limitation with `double` and safe comparison

---

## 2. Project Structure

```text
online-auction-system/
├── pom.xml
├── .gitignore
├── README.md
├── run.bat
└── src/
    └── main/
        └── java/
            └── com/
                └── auction/
                    ├── AuctionConstants.java   ← Business rule constants
                    └── AuctionDataDemo.java    ← Comprehensive fundamentals demo
```

---

## 3. How to Compile and Run

### Using Maven:
```bash
# Compile
mvn compile

# Run the demo
mvn exec:java
```

### Or using `run.bat`:
Double-click `run.bat` or run in terminal:
```cmd
run.bat
```

### Or using plain `java`:
```bash
javac -d target/classes src/main/java/com/auction/*.java
java -cp target/classes com.auction.AuctionDataDemo
```

---

## 4. Key Concepts Demonstrated

### 1. Variables & 8 Primitive Data Types
- `byte` (1 byte, -128 to 127): `auctionStatus = 1` (Active)
- `short` (2 bytes, -32,768 to 32,767): `totalAuctions = 1250`
- `int` (4 bytes, ~-2.1B to 2.1B): `auctionId = 100001`, `registeredUsers = 45230`
- `long` (8 bytes, ~-9.2 quintillion to 9.2 quintillion): `totalTransactionValue = 98765432100L`
- `float` (4 bytes, ~7 digits precision): `commissionRate = 2.5f`
- `double` (8 bytes, ~15-16 digits precision): `startingPrice = 50000.00`, `reservePrice = 75000.00`
- `char` (2 bytes, Unicode): `auctionGrade = 'B'`
- `boolean` (true/false): `isApproved = false`, `isReserveMet = false`

### 2. Arrays
- **1-D Array (`weeklyBids`):** Bid history calculation of Total, Average, Max, and Min bids.
- **2-D Array (`auctionBids`):** Multi-auction bids matrix comparing starting, median, and winning bids.

### 3. Constants (`AuctionConstants`)
- `MINIMUM_BID_INCREMENT = 500.0`
- `ANTI_SNIPE_MINUTES = 2`
- `ANTI_SNIPE_EXTENSION_MINUTES = 5`
- `PLATFORM_FEE_PERCENTAGE = 5.0`
- `GRADE_A_THRESHOLD = 100000.0`

### 4. Operators
- **Arithmetic:** `+`, `-`, `*`, `/`, `%` (bid increment, platform fee, split calculation)
- **Relational:** `==`, `!=`, `>`, `<`, `>=`, `<=` (reserve price met, valid bid check)
- **Logical:** `&&`, `||`, `!` (bidding permissions and moderation flags)
- **Assignment & Increment:** `+=`, `-=`, `*=`, `++`, `--`
- **Ternary Operator:** `auctionGrade = (winningBid >= GRADE_A_THRESHOLD) ? 'A' : 'B'`

### 5. Type Casting
- **Widening (Automatic):** `int -> long`, `int -> double` (no data loss)
- **Narrowing (Manual):** `(int) averageBid` (truncates decimal digits)
- Safe average calculation: `(double) sum / count`

### 6. Integer Overflow
- Multiplying large integers exceeds `Integer.MAX_VALUE` (2,147,483,647) silently wrapping to negative/incorrect value.
- Fix: using `long` literals (`50_000L`) to prevent overflow.

### 7. Floating-Point Precision
- `0.1 + 0.2 != 0.3` due to binary IEEE-754 representation.
- Safe comparison with epsilon tolerance: `Math.abs(a - b) < 0.000001`.
