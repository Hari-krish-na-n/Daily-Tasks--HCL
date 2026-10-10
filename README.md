# Daily Tasks – HCL Training

Repository to track daily learning, assessments, and tasks during the HCL training program.

## Daily Progress Tracker

| Day | Topic / Task | Project / Code | Status |
|---|---|---|---|
| Day 01 | Java Platform Basics + Agile/Scrum | Day-01/day1-java-platform-info | Completed |
| Day 02 | Language Fundamentals + Git | Day-02/day2-language-fundamentals | Completed |
| Day 03 | Control Flow + Maven | Day-03/day3-control-flow | Completed |
| Day 04 | OOP Concepts + IDE & Debugging | Day-04/day4-oop-concepts | Completed |
| Day 05 | Inheritance & Polymorphism + Git Branching & Merging | Day-05/day5-inheritance-polymorphism | Completed |
| Day 06 | Exception Handling + AI-Assisted Debugging | Day-06/day6-exception-handling | Completed |

---

## Day 06 Overview: Exception Handling + AI-Assisted Debugging

A Core Java application based on the Online Auction System domain demonstrating checked and unchecked custom exception handling, multi-catch, exception chaining, audit logging via `finally`, and AI-assisted debugging practices.

- **Custom Exceptions:** Checked `InsufficientStockException` and unchecked `InvalidQuantityException` and `InvalidBidException`.
- **OrderProcessor Service:** Manages in-memory inventory with atomic updates and error recovery.
- **Exception Chaining:** `OrderProcessingException` wraps lower-level exceptions preserving original causes accessible via `getCause()`.
- **Auditing with `finally`:** `AuditService` logs every operation attempt unconditionally in `finally` blocks.
- **Graceful Error Recovery:** Console menu recovers from input parsing errors (`NumberFormatException`) and domain rule rejections without crashing.
- **Comprehensive Testing:** 45 automated JUnit 5 tests covering all edge cases and legacy OOP behaviors.
- **AI-Assisted Debugging:** Root-cause analysis, reproduction, and verification for application-level checked exceptions and runtime input errors.

---

## Day 05 Overview: Inheritance & Polymorphism + Git Branching & Merging

A Core Java application based on the Online Auction System domain demonstrating advanced OOP concepts (abstract classes, interfaces, method overriding/overloading, runtime polymorphism, and Strategy Pattern) alongside professional Git branching, merging, conflict resolution, and rebasing.

- **Abstract Base Entity:** `BaseEntity` providing `id`, `createdAt`, `updatedAt`, `markUpdated()`, and abstract `getSummary()`.
- **Inheritance Hierarchy:** `BaseEntity` extended by `User` (subclassed by `Bidder` and `Seller`) and `AuctionItem`.
- **Polymorphic Role Hierarchy:** `AuctionRole` enum implementing Spring Security-compatible authorities with role-specific permissions and descriptions.
- **Strategy Pattern (Dynamic Polymorphism):** `BidValidationStrategy` interface with `StandardBidValidationStrategy` (₹500 min increment + budget check) and `PremiumBidValidationStrategy` (5% increment + floor + verified bidder check).
- **Payment Hierarchy Learning Exercise:** Abstract `Payment` class (`pay()`, `pay(note)`, `printReceipt()`), `Refundable` interface, and concrete classes `CardPayment`, `UpiPayment`, and `CashPayment`.
- **Git Branching & Merging Practice:** Feature branch workflow (`feature/day-5-inheritance-polymorphism`), fast-forward vs three-way merges, simulated merge conflict resolution (`practice/conflict-branch-a` & `b`), and interactive rebase demonstration (`practice/rebase-demo`).
- **Comprehensive Testing:** 36 automated JUnit 5 tests covering all hierarchies, validations, and edge cases.

---

A Core Java console application based on the Online Auction System domain demonstrating Object-Oriented Programming (OOP) concepts and IDE debugging techniques.

- **Classes & Inheritance:** `User` base class with specialized `Seller` and `Bidder` subclasses demonstrating `extends` and `super()`.
- **Constructors & Chaining:** Multiple overloaded constructors in `Auction` chained using `this(...)`.
- **Static vs Instance Members:** `private static int auctionCount` counter incremented upon object creation and accessed via `Auction.getAuctionCount()`.
- **Encapsulation:** Private entity fields protected by validated getters and setters.
- **equals() & hashCode():** Overridden in `Bid` comparing unique `bidId` values.
- **Service Layer & Packages:** Clean separation across `com.auction.model`, `com.auction.service`, and `com.auction.app`.
- **IDE Debugging:** Planted logic bug in bid increment validation diagnosed using breakpoints, conditional breakpoints (`newBid < currentBid`), watch variables, call stack, logpoint, and hot code replace.

---

## Day 03 Overview: Control Flow + Maven

A Java console application based on the Online Auction System project. This task is mainly used to practice Java control flow and Maven.

- **Control Flow:** Used `if`, `if/else`, `else-if`, `switch`, `while`, `do-while`, traditional `for`, and `enhanced for` loops.
- **Break and Continue:** Used `break` for a 3-attempt PIN login check, and `continue` to skip invalid/rejected auction records during audits.
- **Labelled Break:** Used `break searchAuction;` to exit an outer loop directly while searching for a bidder inside nested loops.
- **Input Validation:** Safe scanner methods that handle invalid menu options, negative numbers, empty input, and text (such as `abc`) without crashing.
- **Auction Menu:** Interactive console menu with options corresponding to the 8 functional requirements.
- **Maven:** Standard Maven project layout, Java 21 POM configuration, build lifecycle phases (`validate`, `compile`, `test`, `package`), automated JUnit 5 tests, and `dev`/`prod` profiles.

---

## Day 02 Overview: Language Fundamentals + Git

A Core Java console application used to practice basic Java language concepts using the Online Auction System domain.

- **Primitive Data Types:** Demonstration of Java primitive types (`byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`) with auction examples.
- **Arrays:** 1-D arrays for daily bid totals and 2-D arrays for multi-auction bid matrices.
- **Constants:** Auction business rules stored using `public static final` constants in `AuctionConstants.java`.
- **Operators:** Arithmetic, relational, logical, assignment, and ternary operators.
- **Type Casting:** Widening (implicit) and narrowing (explicit) type conversions.
- **Overflow:** Demonstration of integer overflow and resolving it using `long`.
- **Floating-Point Precision:** Limitations of `double` precision and safe comparisons.
- **Git:** Repository setup, `.gitignore`, and Git commit workflow.

---

## Day 01 Overview: Java Platform Basics + Agile/Scrum

Java platform inspection program and Agile documentation for the Online Auction System.

- **PlatformInfo.java:** Program printing `java.version`, `os.name`, available processors, max heap, used heap, and free heap memory.
- **Compilation & Execution:** Compiling and running with standard `javac` and `java` commands.
- **Bytecode & Class Loading:** Inspecting bytecode using `javap -c` and class loading using `java -verbose:class`.
- **JVM Architecture:** Notes on JDK, JRE, JVM, ClassLoader, memory areas, and garbage collection.
- **Agile Documentation:** 8 functional requirements converted into user stories with story points, acceptance criteria, and a Definition of Done.

---

## Repository Structure

```text
Day-01/
└── day1-java-platform-info/

Day-02/
└── day2-language-fundamentals/

Day-03/
└── day3-control-flow/

Day-04/
└── day4-oop-concepts/
    ├── pom.xml
    ├── README.md
    ├── src/
    │   └── main/java/com/auction/
    │       ├── model/
    │       ├── service/
    │       └── app/
    └── docs/
        ├── OOP_NOTES.md
        └── DEBUGGING_NOTES.md

Day-05/
└── day5-inheritance-polymorphism/
    ├── pom.xml
    ├── README.md
    ├── src/
    │   ├── main/java/com/auction/
    │   │   ├── app/
    │   │   ├── learning/payment/
    │   │   ├── model/
    │   │   ├── service/
    │   │   └── strategy/
    │   └── test/java/com/auction/
    └── docs/
        └── CONFLICT_PRACTICE.md

Day-06/
└── day6-exception-handling/
    ├── pom.xml
    ├── README.md
    ├── src/
    │   ├── main/java/com/auction/
    │   │   ├── app/
    │   │   ├── exception/
    │   │   ├── learning/payment/
    │   │   ├── model/
    │   │   ├── service/
    │   │   └── strategy/
    │   └── test/java/com/auction/
    └── docs/
        ├── EXCEPTION_HANDLING_NOTES.md
        └── AI_DEBUGGING_NOTES.md

.gitignore
README.md
```

---

## How to Run

### Day 01
```bash
cd Day-01/day1-java-platform-info
javac PlatformInfo.java
java PlatformInfo
```

### Day 02
```bash
cd Day-02/day2-language-fundamentals
mvn compile
mvn exec:java
```

### Day 03
```bash
cd Day-03/day3-control-flow

# Build and package
mvn clean package

# Run console app
java -jar target/day3-control-flow-1.0-SNAPSHOT.jar

# Or run via Maven
mvn exec:java

# Run with profiles
mvn clean package -Pdev
mvn clean package -Pprod
```

### Day 04
```bash
cd Day-04/day4-oop-concepts

# Build and package
mvn clean package

# Run console app
java -jar target/day4-oop-concepts-1.0-SNAPSHOT.jar
```

### Day 05
```bash
cd Day-05/day5-inheritance-polymorphism

# Run all 36 JUnit 5 tests
mvn clean test

# Build and package
mvn clean package

# Run the console demo application
mvn exec:java
```

### Day 06
```bash
cd Day-06/day6-exception-handling

# Run all 45 JUnit 5 tests
mvn clean test

# Build and package
mvn clean package

# Run automated demo
java -jar target/day6-exception-handling-1.0-SNAPSHOT.jar --demo

# Run interactive console app
java -jar target/day6-exception-handling-1.0-SNAPSHOT.jar
```

