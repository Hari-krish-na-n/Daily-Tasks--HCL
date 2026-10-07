# Day 01 — Java Platform Basics + Agile/Scrum Basics
**Domain:** Online Auction System  
**Training:** HCL Day 1

---

## 1. Overview

Day 1 covers:
1. **Java Platform Basics** — inspecting runtime environment, JVM memory, and bytecode
2. **Agile & Scrum** — User Stories (FR1 to FR8), Story Points, Sprint Backlog, and Definition of Done

---

## 2. Contents of this Folder

```text
Day-01/
├── PlatformInfo.java         ← Standalone Java source file
├── run.bat                   ← Batch script to compile and run
├── README.md                 ← This file
└── docs/
    ├── USER_STORIES.md       ← 8 User stories with Fibonacci points and acceptance criteria
    ├── DEFINITION_OF_DONE.md ← DoD quality checklist
    └── AGILE_NOTES.md        ← Simple guide to Agile & Scrum concepts
```

---

## 3. How to Compile and Run

### Direct Command Line
```bash
# Compile
javac PlatformInfo.java

# Run
java PlatformInfo
```

### Inspect Bytecode (JVM Internals)
```bash
# Disassemble bytecode instructions
javap -c PlatformInfo

# Trace class loading by the JVM ClassLoader
java -verbose:class PlatformInfo
```

### Or simply run:
Double-click `run.bat` or run:
```cmd
run.bat
```

---

## 4. JVM Architecture & Execution Flow

```text
PlatformInfo.java
       ↓
     javac (Compiler)
       ↓
PlatformInfo.class (Bytecode)
       ↓
      JVM
       ↓
  Class Loader
       ↓
Runtime Data Areas (Method Area, Heap, Stack, PC Register, Native Stack)
       ↓
Execution Engine (Interpreter + JIT Compiler + Garbage Collector)
       ↓
  Machine Code (Executed by CPU)
```

### Core Concepts Explained
- **JDK (Java Development Kit):** Full development toolkit containing `javac`, `java`, `javap`, and JRE.
- **JRE (Java Runtime Environment):** Runtime libraries + JVM needed to run compiled Java applications.
- **JVM (Java Virtual Machine):** Software engine executing platform-independent bytecode into machine code.
- **Class Loader:** Dynamically loads, links, and initializes `.class` files into memory.
- **Heap Memory:** Shared runtime area storing all objects created with `new`.
- **Stack Memory:** Stores thread-specific call frames, local variables, and method state.
- **Method Area / Metaspace:** Stores class-level metadata, static variables, and runtime bytecode.
- **PC Register:** Holds the memory address of the next bytecode instruction to execute.
- **Execution Engine:** Translates bytecode using Interpreter (instant execution) and JIT Compiler (optimizes hot spots into native code).
- **Garbage Collector (GC):** Automatically tracks and cleans unreferenced heap memory.

---

## 5. Agile & Scrum Documentation

See the [docs/](docs/) directory:
- [docs/USER_STORIES.md](docs/USER_STORIES.md): All 8 functional requirements (FR1–FR8) converted into user stories with story points and acceptance criteria.
- [docs/DEFINITION_OF_DONE.md](docs/DEFINITION_OF_DONE.md): DoD checklist covering compilation, naming conventions, testing, and Git practices.
- [docs/AGILE_NOTES.md](docs/AGILE_NOTES.md): Beginner-friendly explanation of Agile, Scrum roles, sprints, ceremonies, and estimation.
