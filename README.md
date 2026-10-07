# Online Auction System Training — HCL Java Program

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Maven](https://img.shields.io/badge/Build-Maven-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Repository:** [Hari-krish-na-n/Daily-Tasks--HCL](https://github.com/Hari-krish-na-n/Daily-Tasks--HCL)  
**Author:** Hari Krishna  
**Domain:** Online Auction System  

Each training day is maintained as an independent, modular folder to preserve step-by-step progress and facilitate structured code reviews.

---

## 📂 Repository Structure

```text
online-auction-training/
│
├── Day-01/
│   └── day1-java-platform-basics/
│       ├── PlatformInfo.java             ← Java platform inspection program
│       ├── run.bat                       ← Batch script to compile and run PlatformInfo
│       ├── README.md                     ← Day 1 platform basics & JVM guide
│       └── docs/
│           ├── USER_STORIES.md           ← 8 Functional requirements (FR1-FR8)
│           ├── DEFINITION_OF_DONE.md     ← Team Definition of Done checklist
│           └── AGILE_NOTES.md            ← Agile & Scrum concepts explained
│
├── Day-02/
│   └── day2-language-fundamentals/
│       ├── pom.xml                       ← Maven Java 21 build file
│       ├── .gitignore                    ← Ignores target/ and IDE files
│       ├── README.md                     ← Day 2 language fundamentals guide
│       ├── run.bat                       ← Batch script to compile and run demo
│       └── src/main/java/com/auction/
│           ├── AuctionConstants.java     ← Business rule constants
│           ├── AuctionDataDemo.java      ← Primitive types, arrays, casting demo
│           └── PlatformInfo.java         ← Platform utility
│
├── Day-03/
│   └── day3-control-flow/
│       ├── pom.xml                       ← Maven build with dev/prod profiles
│       ├── README.md                     ← Day 3 control flow & Maven guide
│       ├── .gitignore                    ← Ignores target/ and IDE files
│       ├── run.bat                       ← Build & run batch script
│       ├── src/
│       │   ├── main/java/com/auction/
│       │   │   ├── AuctionConsoleApp.java← Interactive console app (8 FRs)
│       │   │   ├── AuctionConstants.java ← Business constants
│       │   │   ├── Auction.java          ← Auction entity model
│       │   │   ├── Bid.java              ← Bid entity model
│       │   │   └── User.java             ← User authentication model
│       │   └── test/java/com/auction/
│       │       └── AuctionConsoleAppTest.java ← Automated JUnit 5 test suite
│       └── docs/
│           ├── CONTROL_FLOW_NOTES.md     ← Control flow theory & examples
│           ├── MAVEN_NOTES.md            ← Maven lifecycle & profiles guide
│           └── TEST_CASES.md             ← Comprehensive test cases & logs
│
├── .gitignore                            ← Root Git ignore configuration
└── README.md                             ← Main training repository documentation
```

---

## 📅 Daily Tasks Overview

### [Day 01 — Java Platform Basics + Agile/Scrum Basics](Day-01/day1-java-platform-basics/)
- **Java Platform Basics:** JVM runtime environment inspection, heap memory metrics, ClassLoader hierarchy, bytecode disassembly with `javap -c`, and class loading tracing via `-verbose:class`.
- **Agile / Scrum Documentation:** 8 user stories with Fibonacci points, acceptance criteria, DoD checklist, and Scrum framework notes.

### [Day 02 — Language Fundamentals + Git Fundamentals](Day-02/day2-language-fundamentals/)
- **Language Fundamentals:** 8 primitive data types, 1D and 2D arrays, business constants (`AuctionConstants`), arithmetic/relational/logical operators, type casting (widening vs narrowing), integer overflow detection, and floating-point precision comparisons.

### [Day 03 — Control Flow + Maven](Day-03/day3-control-flow/)
- **Control Flow Concepts:**
  - Branching: `if`, `if-else`, `else-if`, `switch` (8 FR menu router and admin moderation).
  - Looping: `do-while` (console menu loop), `while` (safe input validation & auto-bidding), traditional `for` (bid ledgers), enhanced `for` (auction listings & mini-statements).
  - Jump statements: `break` (3-attempt PIN security loop), `continue` (skipping rejected auctions in audit), `labelled break` (`searchAuction:` outer loop exit).
  - Robust Input Validation: Immune to crashes from strings (`abc`), negative numbers, empty input, or out-of-range options.
- **Maven Architecture:** Standard layout, POM coordinates (`com.auction:day3-control-flow:1.0-SNAPSHOT`), full build lifecycle (`validate` to `install`), JUnit 5 dependency in `test` scope, and `dev` / `prod` profile configurations.

---

## 🚀 How to Run

### Day 01
```bash
cd Day-01/day1-java-platform-basics
javac PlatformInfo.java
java PlatformInfo
# Or simply:
run.bat
```

### Day 02
```bash
cd Day-02/day2-language-fundamentals
mvn compile
mvn exec:java
# Or simply:
run.bat
```

### Day 03
```bash
cd Day-03/day3-control-flow

# Build and test
mvn clean package

# Run the console application
java -jar target/day3-control-flow-1.0-SNAPSHOT.jar

# Or run via Maven
mvn exec:java

# Or simply:
run.bat
```

---

## ✅ Deliverables Checklist

```text
[x] Day-01 preserved in day1-java-platform-basics
[x] Day-02 preserved in day2-language-fundamentals
[x] Day-03 created in day3-control-flow
[x] Java 21 configured across all modules
[x] if / else-if / else implemented
[x] switch implemented
[x] while loop implemented
[x] do-while loop implemented
[x] traditional for loop implemented
[x] enhanced for loop implemented
[x] break implemented (3 login attempts)
[x] continue implemented (audit filter)
[x] labelled break implemented (nested search)
[x] input validation implemented (never crashes on abc, -1, 999)
[x] 8 Functional Requirements menu implemented
[x] Maven standard project layout verified
[x] Maven lifecycle documented (validate -> compile -> test -> package -> install)
[x] Dev and Prod profiles configured and tested
[x] mvn clean package succeeds
[x] mvn clean package -Pdev succeeds
[x] mvn clean package -Pprod succeeds
[x] JUnit 5 automated test suite passing
[x] Clean .gitignore without build artifacts
[x] Documentation complete
```
