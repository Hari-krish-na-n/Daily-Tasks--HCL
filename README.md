# Daily Tasks — HCL Training

Repository to track daily learning, assessments, and tasks during the HCL training program.

## Daily Progress Tracker

| Day | Topic / Task | Project / Code | Status |
|---|---|---|---|
| Day 01 | Java Platform Basics + Agile/Scrum | Day-01/day1-java-platform-basics | Completed |
| Day 02 | Language Fundamentals + Git | Day-02/day2-language-fundamentals | Completed |
| Day 03 | Control Flow + Maven | Day-03/day3-control-flow | Completed |

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
└── day1-java-platform-basics/

Day-02/
└── day2-language-fundamentals/

Day-03/
└── day3-control-flow/

.gitignore
README.md
```

---

## How to Run

### Day 01
```bash
cd Day-01/day1-java-platform-basics
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
