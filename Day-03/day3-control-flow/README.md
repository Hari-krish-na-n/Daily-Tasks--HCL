# Day 03 — Control Flow + Maven

Java console application for the Online Auction System project created to practice Java control-flow statements and Maven basics.

## Topics Practiced

1. **Control Flow:** `if`, `if-else`, `else-if`, `switch`, `while`, `do-while`, traditional `for`, and `enhanced for` loops.
2. **Break and Continue:** Used `break` for PIN login attempts and `continue` to skip invalid/rejected auction records during audit.
3. **Labelled Break:** Used `break searchAuction;` to exit the outer loop directly when finding a bidder in nested loops.
4. **Input Validation:** Safely handles non-numeric input (such as `abc`), negative values, out-of-range choices, and empty lines without crashing.
5. **Maven Setup:** Standard directory layout, POM coordinates, Maven lifecycle phases (`validate`, `compile`, `test`, `package`), and `dev`/`prod` profiles.

---

## Project Structure

```text
Day-03/day3-control-flow/
├── pom.xml
├── README.md
├── .gitignore
├── run.bat
│
├── src/
│   ├── main/java/com/auction/
│   │   ├── AuctionConsoleApp.java
│   │   ├── AuctionConstants.java
│   │   ├── Auction.java
│   │   ├── Bid.java
│   │   └── User.java
│   │
│   └── test/java/com/auction/
│       └── AuctionConsoleAppTest.java
│
└── docs/
    ├── CONTROL_FLOW_NOTES.md
    ├── MAVEN_NOTES.md
    └── TEST_CASES.md
```

---

## Control Flow Implementation Details

| Control Flow | Where it is used in the Auction System | Method / Location |
|---|---|---|
| `if / else` | Validating minimum bid increment and reserve price | `handlePlaceBid()`, `handleCloseAuctionAndDeclareWinner()` |
| `else-if` | Checking user roles (Seller, Bidder, Admin) | `handleUserRegistrationAndLogin()` |
| `switch` | Main menu navigation and admin moderation choices | `main()`, `handleAdminModeration()` |
| `while` | Reading validated user input and proxy bidding increments | `readValidatedMenuChoice()`, `handleProxyBidding()` |
| `do-while` | Main console menu loop (runs at least once) | `main()` |
| `for` | Iterating through bid history with an index counter | `handleSettlementAndShipment()` |
| `enhanced for` | Displaying active auctions list and mini-statement | `displayCurrentAuctions()`, `handleAdminModeration()` |
| `break` | Exiting 3-attempt PIN login loop upon success | `handleUserRegistrationAndLogin()` |
| `continue` | Skipping rejected auction records during audit verification | `handleSettlementAndShipment()` |
| `labelled break`| Terminating outer search loop directly upon finding bidder | `handleSettlementAndShipment()` (`searchAuction:`) |
| `input validation`| Preventing crashes on non-numeric or out-of-range input | `readValidatedMenuChoice()`, `readPositiveDouble()` |

---

## Console Menu (8 Functional Requirements)

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

- **FR1 — User Registration / Login:** Select role and verify 4-digit PIN (max 3 attempts).
- **FR2 — Create Auction Listing:** Enter product name, starting price, and reserve price with validation.
- **FR3 — Admin Approve / Reject Listing:** Approve or reject listings using `switch`.
- **FR4 — Place Bid:** Validates that new bid is at least `current bid + ₹500`.
- **FR5 — Proxy / Auto Bidding:** Automatically increments bids in steps of ₹500 up to max budget.
- **FR6 — Anti-Sniping:** Extends auction by 2 minutes if bid is placed within final 2 minutes.
- **FR7 — Close Auction & Declare Winner:** Checks if reserve price is met; declares winner or marks unsold.
- **FR8 — Settlement & Shipment:** Shows settlement status, bid ledger using `for`, audit using `continue`, and bidder search using labelled break.

---

## Maven Commands

### Build and Package
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Run Application
```bash
# Using JAR
java -jar target/day3-control-flow-1.0-SNAPSHOT.jar

# Or using Maven
mvn exec:java

# Or using script
run.bat
```

### Maven Profiles
```bash
# Development profile
mvn clean package -Pdev

# Production profile
mvn clean package -Pprod
```
