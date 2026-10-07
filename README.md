# Daily Tasks — HCL Java Training

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Maven](https://img.shields.io/badge/Build-Maven-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Repository:** [Hari-krish-na-n/Daily-Tasks--HCL](https://github.com/Hari-krish-na-n/Daily-Tasks--HCL)  
**Author:** Hari Krishna  
**Domain:** Online Auction System  

---

## 📂 Repository Structure

```text
Daily-Tasks--HCL/
├── .gitignore
├── README.md
│
├── Day-01/
│   ├── PlatformInfo.java         ← Java platform inspection program
│   ├── run.bat                   ← Batch script to compile and run PlatformInfo
│   ├── README.md                 ← Day 1 platform basics & JVM guide
│   └── docs/
│       ├── USER_STORIES.md       ← 8 Functional requirements (FR1-FR8) as user stories
│       ├── DEFINITION_OF_DONE.md ← Team Definition of Done checklist
│       └── AGILE_NOTES.md        ← Agile & Scrum concepts explained
│
└── Day-02/
    └── online-auction-system/
        ├── pom.xml               ← Maven Java 21 build file
        ├── .gitignore            ← Ignores target/ and IDE files
        ├── README.md             ← Day 2 language fundamentals guide
        ├── run.bat               ← Batch script to compile and run demo
        └── src/
            └── main/
                └── java/
                    └── com/
                        └── auction/
                            ├── AuctionConstants.java   ← Business rule constants
                            └── AuctionDataDemo.java    ← Java language fundamentals demo
```

---

## 📅 Daily Tasks Overview

### [Day 01 — Java Platform Basics + Agile/Scrum Basics](Day-01/)
- **Java Platform Basics:**
  - `PlatformInfo.java` prints Java version, OS, processors, maximum heap, used heap, and free heap memory.
  - Bytecode inspection via `javap -c` and class loading inspection via `java -verbose:class`.
  - Detailed architecture guide of the JVM execution flow (JVM, JRE, JDK, ClassLoader, Heap, Stack, Metaspace, PC Register, JIT Compiler, GC).
- **Agile / Scrum Documentation:**
  - 8 User Stories for the Online Auction System with Fibonacci story points (`1, 2, 3, 5, 8`), acceptance criteria, and DoD.
  - Definition of Done (`docs/DEFINITION_OF_DONE.md`) covering compilation, code style, testing, and Git hygiene.
  - Agile Notes (`docs/AGILE_NOTES.md`) covering Agile principles, Scrum roles, sprints, backlog, ceremonies, and estimation.

### [Day 02 — Language Fundamentals + Git Fundamentals](Day-02/online-auction-system/)
- **Language Fundamentals (Maven Console Application):**
  - **8 Primitive Types:** `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean` with domain examples.
  - **1-D Array:** Weekly bid statistics (Total, Average, Max, Min).
  - **2-D Array:** Matrix of bids across multiple auctions.
  - **Constants:** `AuctionConstants` eliminating magic numbers.
  - **Operators:** Arithmetic, relational, logical, assignment, increment/decrement, and ternary operators.
  - **Type Casting:** Widening (implicit) vs. Narrowing (explicit truncation).
  - **Integer Overflow:** Demonstrating silent overflow and resolving with `long`.
  - **Floating-Point Precision:** Floating-point limitations (`0.1 + 0.2 != 0.3`) and safe epsilon comparison.

---

## 🚀 How to Run

### Day 01
```bash
cd Day-01
javac PlatformInfo.java
java PlatformInfo

# Or simply:
run.bat
```

### Day 02
```bash
cd Day-02/online-auction-system
mvn compile
mvn exec:java

# Or simply:
run.bat
```

---

## ✅ Deliverables Checklist

```text
[x] JDK 21 configured
[x] Day-01 folder created
[x] PlatformInfo runs using javac/java
[x] javap -c verified
[x] -verbose:class verified
[x] 8 user stories created with story points
[x] Definition of Done created
[x] Agile notes created
[x] Day-02/online-auction-system Maven project created
[x] Primitive data types demonstrated
[x] 1-D array demonstrated
[x] 2-D array demonstrated
[x] Constants created
[x] Operators demonstrated
[x] Type casting demonstrated
[x] Overflow demonstrated
[x] Floating-point precision demonstrated
[x] Maven compile successful
[x] .gitignore configured
[x] README completed
[x] Pushed to GitHub (main branch)
```
