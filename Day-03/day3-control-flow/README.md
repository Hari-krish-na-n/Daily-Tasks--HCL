# Day 03 — Control Flow + Maven

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Maven](https://img.shields.io/badge/Build-Maven-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Project:** Online Auction System — Day 3  
**Module:** `day3-control-flow`  
**Package:** `com.auction`  

---

## 📌 Overview

Day 3 focuses on implementing all foundational **Java Control Flow structures** alongside **Apache Maven build management** using the **Online Auction System** domain:

1. **Decision Making & Branching:** `if`, `if-else`, `else-if`, `switch`
2. **Looping & Iteration:** `while`, `do-while`, traditional `for`, enhanced `for`
3. **Jump & Control Transfer:** `break`, `continue`, `labelled break`
4. **Resilience & Quality:** Safe input validation (never crashing on non-numeric or out-of-bounds input)
5. **Maven Engineering:** Standard directory layout, POM coordinates, Maven build lifecycle phases, dependency management, and `dev`/`prod` profiles.

---

## 📂 Project Structure

```text
Day-03/day3-control-flow/
├── pom.xml                                ← Java 21 Maven build file with profiles
├── README.md                              ← Day 3 documentation & execution guide
├── .gitignore                             ← Ignores target/ and IDE files
├── run.bat                                ← Convenience batch runner script
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── auction/
│   │               ├── AuctionConsoleApp.java  ← Main console application with all 8 FRs
│   │               ├── AuctionConstants.java   ← Business rule constants (no magic numbers)
│   │               ├── Auction.java            ← Domain model for auction listings
│   │               ├── Bid.java                ← Domain model for bids
│   │               └── User.java               ← Domain model for users and authentication
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── auction/
│                   └── AuctionConsoleAppTest.java ← Automated JUnit 5 tests
│
└── docs/
    ├── CONTROL_FLOW_NOTES.md              ← Comprehensive control-flow concepts guide
    ├── MAVEN_NOTES.md                     ← Maven lifecycle, layout & profiles guide
    └── TEST_CASES.md                      ← Test case matrix and verification logs
```

---

## ⚙️ Control Flow Concepts Demonstrated

| Control Flow | Auction System Application | Location / Method |
| :--- | :--- | :--- |
| **`if / else`** | Validates bids (`bid >= current + increment`), reserve prices, positive amounts | `handlePlaceBid()`, `validateBid()`, `isReservePriceMet()` |
| **`else-if`** | Multi-role authorization checks (`SELLER`, `BIDDER`, `ADMIN`) | `handleUserRegistrationAndLogin()` |
| **`switch`** | Routes main menu selections (0-8) and admin moderation actions | `main()`, `handleAdminModeration()` |
| **`while`** | Input validation loops, automated proxy counter-bidding increments | `readValidatedMenuChoice()`, `handleProxyBidding()` |
| **`do-while`** | Main interactive console menu (guaranteed to render at least once) | `main()` |
| **`for` (traditional)** | Indexed iteration over auction bid ledgers (`i = 0; i < bids.size(); i++`) | `handleSettlementAndShipment()` |
| **`enhanced for`** | Iterating over in-memory auction listings and mini-statement display | `displayCurrentAuctions()`, `handleAdminModeration()` |
| **`break`** | Exits 3-attempt PIN security loop immediately upon correct PIN | `handleUserRegistrationAndLogin()` |
| **`continue`** | Skips non-approved/rejected auction records during audit verification | `handleSettlementAndShipment()` |
| **`labelled break`** | Terminating outer loop directly during nested multi-auction bidder search | `handleSettlementAndShipment()` (`searchAuction:`) |
| **`input validation`**| Prevents crashes on `abc`, negative numbers, empty input, or bounds errors | `readValidatedMenuChoice()`, `readPositiveDouble()` |

---

## 📋 8 Functional Requirements (Console Menu)

```text
========================================
       ONLINE AUCTION SYSTEM
========================================
1. User Registration / Login
2. Create Auction Listing
3. Admin Approve / Reject Listing
4. Place Bid
5. Proxy / Auto Bidding
6. Anti-Sniping
7. Close Auction & Declare Winner
8. Settlement & Shipment
0. Exit
========================================
Enter your choice:
```

- **FR1 — User Registration / Login:** Role assignment (`SELLER`, `BIDDER`, `ADMIN`) + 3-attempt PIN security with `break`.
- **FR2 — Auction Listing:** Listing creation with positive starting price and reserve price validation.
- **FR3 — Admin Moderation:** Review pending listings and Approve/Reject via `switch`.
- **FR4 — Bidding:** Minimum increment enforcement (`currentBid + MINIMUM_BID_INCREMENT`) via `if/else`.
- **FR5 — Proxy / Auto Bidding:** Automated simulated bidding steps up to ceiling budget using `while`.
- **FR6 — Anti-Sniping:** Automatic auction extension if bid arrives within final `ANTI_SNIPE_MINUTES`.
- **FR7 — Closing & Winner Declaration:** Evaluates highest bid against reserve price; marks `SOLD` or `UNSOLD`.
- **FR8 — Settlement & Shipment:** Escrow financials, traditional `for` bid ledgers, `continue` audit filter, and `labelled break` search.

---

## 🛠️ Maven Configuration & Lifecycle

### Standard Coordinates
```xml
<groupId>com.auction</groupId>
<artifactId>day3-control-flow</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>jar</packaging>
```

### Build Lifecycle Flow
```text
validate  ──>  compile  ──>  test  ──>  package  ──>  install
```

### Profiles
- **`dev`** (Default): Injects `environment=development` property.
- **`prod`**: Injects `environment=production` property.

---

## 🚀 How to Run

### 1. Build and Package
```bash
cd Day-03/day3-control-flow

# Clean and package with default development profile
mvn clean package

# Or package explicitly with production profile
mvn clean package -Pprod
```

### 2. Run the Application

#### Option A: Run JAR Directly
```bash
java -jar target/day3-control-flow-1.0-SNAPSHOT.jar
```

#### Option B: Run via Maven Exec Plugin
```bash
mvn exec:java
```

#### Option C: Run using Convenience Script
```bash
run.bat
```

### 3. Run Automated Unit Tests
```bash
mvn test
```

---

## ✅ Quality Checklist

- [x] Java 21 configured and enforced
- [x] All 10 control-flow statements implemented with auction domain logic
- [x] Application handles invalid user input (`abc`, `-1`, `99`, empty) gracefully without crashing
- [x] 8 Functional Requirements mapped to menu
- [x] Automated JUnit 5 tests passing
- [x] `mvn clean package`, `mvn clean package -Pdev`, and `mvn clean package -Pprod` succeed
- [x] Clean separation of daily folders
