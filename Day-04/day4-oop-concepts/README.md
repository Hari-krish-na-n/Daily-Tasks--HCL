# Day 04 - OOP Concepts + Debugging

Worked on Java OOP concepts using the Online Auction System.

## Topics

- Classes and objects
- Constructors
- Constructor overloading
- Constructor chaining
- this and super
- Encapsulation
- Inheritance
- Access modifiers
- static and instance members
- equals and hashCode
- Packages
- Debugging

## Project Structure

```text
Day-04/day4-oop-concepts/
├── pom.xml
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── auction/
│                   ├── model/
│                   │   ├── User.java
│                   │   ├── Seller.java
│                   │   ├── Bidder.java
│                   │   ├── Auction.java
│                   │   └── Bid.java
│                   ├── service/
│                   │   └── AuctionService.java
│                   └── app/
│                       └── AuctionApp.java
└── docs/
    ├── OOP_NOTES.md
    └── DEBUGGING_NOTES.md
```

## What I Implemented

- Built base `User` entity and extended it with `Seller` and `Bidder` subclasses using `extends` and `super()`.
- Implemented `Auction` with constructor overloading and chaining via `this()`.
- Added a static counter `auctionCount` in `Auction` to contrast static vs instance members.
- Created `Bid` with strict encapsulation (amount > 0) and overridden `equals()` and `hashCode()` using `bidId`.
- Maintained encapsulation across entities with private fields and validated accessors.
- Organized code across packages: `model`, `service`, and `app`.
- Created in-memory `AuctionService` to manage auctions and validate incoming bids.
- Console application `AuctionApp` to demonstrate all OOP features and bidding flow.

## Debugging

- Planted a deliberate logic error in `AuctionService.validateBid` where `newBid > (currentBid - minimumIncrement)` incorrectly accepted an underbid of ₹49,000 when the current price was ₹50,000.
- Used the IDE debugger (regular and conditional breakpoints, watches, step over/into/out, call stack, logpoint, and hot code replace) to trace and diagnose the issue without console print statements.
- Fixed the logic to require `newBid >= (currentBid + minimumIncrement)` so invalid bids (₹49,000) are rejected while valid bids (₹50,500+) are accepted.
- Documented full debugging steps and recommended screenshot guidelines in `docs/DEBUGGING_NOTES.md`.

## Run

To compile and package the project:

```bash
mvn clean package
```

To run the application:

```bash
java -jar target/day4-oop-concepts-1.0-SNAPSHOT.jar
```

Or execute directly with Maven Exec plugin:

```bash
mvn compile exec:java -Dexec.mainClass="com.auction.app.AuctionApp"
```
