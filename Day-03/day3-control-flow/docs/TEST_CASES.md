# Day 3 — Test Cases & Quality Assurance Matrix

This document outlines the test scenarios and execution results for the **Day 3 Control Flow + Maven** module in the **Online Auction System**.

---

## 1. Automated Test Cases (JUnit 5)

| Test ID | Method Name | Description | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- |
| **TC-AUTO-01** | `testBidValidation_Accept` | Verifies bid equal to `current + MIN_INCREMENT` (₹50,500) and higher (₹55,000) are accepted. | `validateBid(...) == true` | **PASS** |
| **TC-AUTO-02** | `testBidValidation_Reject` | Verifies bids below minimum increment (₹50,400) or below current bid (₹49,000) are rejected. | `validateBid(...) == false` | **PASS** |
| **TC-AUTO-03** | `testReservePrice_Met` | Tests closing auction when highest bid (₹62,000) meets or exceeds reserve price (₹60,000). | `isReservePriceMet(...) == true` | **PASS** |
| **TC-AUTO-04** | `testReservePrice_NotMet` | Tests closing auction when highest bid (₹55,000) is below reserve price (₹60,000). | `isReservePriceMet(...) == false` | **PASS** |
| **TC-AUTO-05** | `testAntiSnipingTrigger` | Validates trigger condition at 1 min (threshold <= 2) and 2 min, vs 5 min (no trigger). | Triggered at 1 & 2 mins; not at 5 mins | **PASS** |
| **TC-AUTO-06** | `testSafeInputValidation_Recovers` | Simulates inputs `abc`, `-1`, `99`, empty line, followed by `4`. | Safely recovers without crashing, returns 4 | **PASS** |
| **TC-AUTO-07** | `testInitialSampleAuctions` | Asserts initial in-memory sample auctions (101, 102, 103) are populated. | Auctions 101, 102, 103 non-null | **PASS** |
| **TC-AUTO-08** | `testUserPinVerification` | Tests correct PIN (1234) vs invalid PIN (9999). | Correct returns `true`; incorrect returns `false` | **PASS** |

---

## 2. Interactive Console Input Validation Scenarios

### Scenario A: Menu Input Boundaries
- **Input:** `abc`
  - **Output:** `Invalid input. Please enter a number: `
  - **Result:** Application stays running.
- **Input:** `-1`
  - **Output:** `Invalid choice. Enter 0-8: `
  - **Result:** Application rejects negative index.
- **Input:** `99`
  - **Output:** `Invalid choice. Enter 0-8: `
  - **Result:** Application rejects out-of-range index.
- **Input:** ` ` (empty enter key)
  - **Output:** `Input cannot be empty. Enter choice (0-8): `
  - **Result:** Handled cleanly.
- **Input:** `4`
  - **Output:** Opens FR4 Bidding menu.
  - **Result:** Accepted.

### Scenario B: Bid Increment Validation (FR4)
- **Current State:** Auction 101 current bid = ₹50,000. Minimum increment = ₹500.
- **Input:** ₹49,000
  - **Output:** `Bid rejected. Bid must be at least ₹50,500.`
- **Input:** ₹50,400
  - **Output:** `Bid rejected. Bid must be at least ₹50,500.`
- **Input:** ₹50,500
  - **Output:** `Bid accepted. Current bid is now ₹50,500 by Krishna.`

### Scenario C: PIN Authentication Security Attempts (FR1)
- **Attempt 1:** `0000` → `Incorrect PIN.`
- **Attempt 2:** `1111` → `Incorrect PIN.`
- **Attempt 3:** `1234` → `Login successful!` (Exits loop early via `break`).
- **Alternative (3 Failed Attempts):**
  - Attempt 1: `9999`
  - Attempt 2: `8888`
  - Attempt 3: `7777`
  - Output: `Maximum attempts reached. Access denied.`

### Scenario D: Reserve Price Verification (FR7)
- **Auction 101:** Highest bid = ₹62,000, Reserve price = ₹60,000
  - Output: `Highest bid: ₹62,000 | Reserve price: ₹60,000 | Winner: Krishna | Auction status: SOLD`
- **Auction 103:** Highest bid = ₹40,000, Reserve price = ₹45,000
  - Output: `Highest bid: ₹40,000 | Reserve price: ₹45,000 | Reserve price not met. Auction status: UNSOLD`

### Scenario E: Anti-Sniping Trigger (FR6)
- **Input:** `1` (minute remaining)
  - Output: `Bid occurred within final 2 minutes. Auction extended by 2 minutes. New end time: 3 minutes remaining.`
- **Input:** `5` (minutes remaining)
  - Output: `Bid occurred with 5 minutes remaining. No extension needed.`

### Scenario F: Labelled Break Search (FR8)
- **Target Bidder:** `Krishna`
  - **Trace:**
    - Outer loop enters Auction 101.
    - Inner loop inspects Bid 1 (`Krishna`, ₹50,000).
    - Match found! Prints message and triggers `break searchAuction;`.
    - Directly terminates outer loop without checking remaining auctions.

---

## 3. Maven Build Verification Checklist

| Command | Expected Result | Status |
| :--- | :--- | :--- |
| `mvn validate` | Validates POM and project model | **SUCCESS** |
| `mvn compile` | Compiles source files into `target/classes` | **SUCCESS** |
| `mvn test` | Executes all 8 JUnit tests | **SUCCESS** |
| `mvn package` | Creates `target/day3-control-flow-1.0-SNAPSHOT.jar` | **SUCCESS** |
| `mvn clean package` | Full clean build and jar packaging | **SUCCESS** |
| `mvn clean package -Pdev` | Builds successfully with `development` profile | **SUCCESS** |
| `mvn clean package -Pprod` | Builds successfully with `production` profile | **SUCCESS** |
