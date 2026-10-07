# Day 3 — Java Control Flow Notes

This document provides a comprehensive, beginner-friendly guide to Java Control Flow concepts demonstrated in the **Online Auction System** training project.

---

## 1. Decision Making: `if`, `if-else`, and `else-if`

Conditional statements allow the program to evaluate boolean expressions and branch execution dynamically.

### `if` Statement
Executes a block only when a boolean condition evaluates to `true`.

```java
if (startingPrice < AuctionConstants.MINIMUM_STARTING_PRICE) {
    startingPrice = AuctionConstants.MINIMUM_STARTING_PRICE;
}
```

### `if-else` Statement
Provides two distinct paths: one when the condition is `true`, and another when `false`.

```java
// Bid Validation Rule: new bid must exceed current bid by at least minimum increment
if (newBid >= currentBid + AuctionConstants.MINIMUM_BID_INCREMENT) {
    System.out.println("Bid accepted.");
} else {
    System.out.println("Bid rejected. Bid must be at least ₹" + (currentBid + AuctionConstants.MINIMUM_BID_INCREMENT));
}
```

### `else-if` Ladder
Tests multiple conditions in sequence until one matches.

```java
// User Role Authorization
if (roleChoice == 1) {
    roleName = "SELLER";
} else if (roleChoice == 2) {
    roleName = "BIDDER";
} else if (roleChoice == 3) {
    roleName = "ADMIN";
} else {
    roleName = "BIDDER (Guest)";
}
```

---

## 2. Selection: `switch` Statement

The `switch` statement selects one of many code blocks to execute based on the value of a variable or expression (primitives like `int`, `char`, `String`, or `enum`).

In Day 3, we use standard `switch` syntax with explicit `break` statements:

```java
switch (choice) {
    case 1:
        handleUserRegistrationAndLogin(scanner);
        break;
    case 2:
        handleCreateAuctionListing(scanner);
        break;
    case 3:
        handleAdminModeration(scanner);
        break;
    case 4:
        handlePlaceBid(scanner);
        break;
    case 5:
        handleProxyBidding(scanner);
        break;
    case 6:
        handleAntiSniping(scanner);
        break;
    case 7:
        handleCloseAuctionAndDeclareWinner(scanner);
        break;
    case 8:
        handleSettlementAndShipment(scanner);
        break;
    case 0:
        System.out.println("Exiting...");
        break;
    default:
        System.out.println("Invalid choice. Enter 0-8.");
        break;
}
```

---

## 3. Repetition: `do-while` Loop

A `do-while` loop evaluates its condition **after** executing the block. This guarantees that the loop body executes **at least once**.

### Why use `do-while` for the Console Menu?
The user must see the console menu at least once before deciding whether to perform an action or exit (`0`).

```java
int choice;
do {
    displayMenu();
    choice = readValidatedMenuChoice(scanner);
    switch (choice) {
        // handle options...
    }
} while (choice != 0);
```

---

## 4. Repetition: `while` Loop

A `while` loop checks its condition **before** entering the body. If the condition is false initially, the body does not execute.

### Input Validation with `while`
Prevents application crashes when invalid inputs (out of bounds, non-numeric tokens) are encountered:

```java
while (true) {
    String input = scanner.nextLine().trim();
    try {
        int choice = Integer.parseInt(input);
        if (choice >= 0 && choice <= 8) {
            return choice; // Valid, exit loop
        }
        System.out.print("Invalid choice. Enter 0-8: ");
    } catch (NumberFormatException e) {
        System.out.print("Invalid input. Please enter a number: ");
    }
}
```

---

## 5. Repetition: Traditional `for` Loop

A traditional `for` loop is used when the number of iterations is known or an index counter is required:

```java
// Iterating indexed bid records in an auction ledger
for (int i = 0; i < bids.size(); i++) {
    System.out.printf("Bid [%d]: %s%n", i, bids.get(i));
}
```

Structure:
1. **Initialization:** `int i = 0;` (executes once)
2. **Condition:** `i < bids.size();` (evaluated before each iteration)
3. **Update:** `i++` (executes after each iteration)

---

## 6. Repetition: Enhanced `for` Loop (for-each)

The enhanced `for` loop provides a clean, concise syntax for iterating over arrays and `Iterable` collections without managing manual index variables.

```java
// Displaying all active auction listings
for (Auction auction : auctions) {
    System.out.printf("Auction %d - %s - ₹%,.0f%n",
            auction.getId(), auction.getProductName(), auction.getCurrentBid());
}
```

---

## 7. Jump Statement: `break`

The `break` statement terminates the innermost loop or switch statement immediately and transfers control to the statement following the loop.

### Security PIN Verification Example
Allow a maximum of 3 login attempts. If the user enters the correct PIN, `break` immediately exits the loop:

```java
boolean authenticated = false;
for (int attempt = 1; attempt <= 3; attempt++) {
    System.out.print("Enter PIN: ");
    int pin = readPin(scanner);
    if (pin == 1234) {
        System.out.println("Login successful!");
        authenticated = true;
        break; // Exits loop early upon successful login
    } else {
        System.out.println("Incorrect PIN.");
    }
}
if (!authenticated) {
    System.out.println("Maximum attempts reached. Access denied.");
}
```

---

## 8. Jump Statement: `continue`

The `continue` statement skips the remainder of the current loop iteration and moves directly to the next iteration.

### Auction Audit Filter Example
When auditing auction records, if an auction is in `REJECTED` status, skip further processing and continue to the next record:

```java
for (int i = 0; i < auctions.size(); i++) {
    Auction a = auctions.get(i);
    if ("REJECTED".equalsIgnoreCase(a.getStatus())) {
        System.out.println("Skipping rejected auction record ID: " + a.getId());
        continue; // Skips current iteration, proceeds to next auction
    }
    System.out.println("Processing valid auction: " + a.getProductName());
}
```

---

## 9. Jump Statement: `labelled break`

A standard `break` only exits the innermost enclosing loop. When dealing with nested loops (e.g. searching across multiple auctions and their respective bids), a **labelled break** allows exiting the outer loop directly.

### Nested Bidder Search Example

```java
// 'searchAuction:' is the label naming the outer loop
searchAuction:
for (Auction auction : auctions) {
    for (Bid bid : auction.getBids()) {
        if (bid.getBidderId().equalsIgnoreCase(targetBidderId)) {
            System.out.println("Bidder found in Auction " + auction.getId());
            found = true;
            break searchAuction; // Directly terminates the outer 'searchAuction' loop!
        }
    }
}
```

> **Why Labelled Break?**
> Without a labelled break, an inner `break` only breaks out of the `bids` loop. You would need extra boolean flags (`if (found) break;`) in the outer loop. Labelled break eliminates redundant checks and cleanly terminates outer execution.

---

## 10. Robust Input Validation

A production-grade console application must never crash when unexpected input is typed by the user:

| Input Scenario | Vulnerable Code (`scanner.nextInt()`) | Robust Solution |
| :--- | :--- | :--- |
| `abc` (text) | Throws `InputMismatchException` and crashes | Catches `NumberFormatException` / uses `scanner.nextLine()` |
| `-1` (negative) | Accepted silently, corrupts state | Validates bounds (`val > 0`) |
| `999` (out of range) | Invalid menu index triggers crash | Bounds checking (`choice >= 0 && choice <= 8`) |
| Empty Enter key | Throws parsing error | Checks `input.isEmpty()` and re-prompts |
| Floating point in integer field | Crash | Graceful re-prompt |
