# Day 10 Memory Lab: Experimental Results & Analysis

This document records the commands, JVM configuration flags, actual console outputs, observations, and memory management explanations for all 5 experiments conducted on Java 27 (HotSpot 64-Bit Server VM, Windows 11).

---

## 1. Experimental Summary Table

| Experiment | Command / Configuration | Actual Result | Observation | Explanation |
|---|---|---|---|---|
| **1. StackOverflowError** | `java -Xss256k -cp target/classes com.auction.lab.StackOverflowExperiment`<br><br>`java -Xss1m -cp target/classes com.auction.lab.StackOverflowExperiment` | **-Xss256k:**<br>Max depth: **1,541** frames<br><br>**-Xss1m:**<br>Max depth: **13,959** frames | Larger thread stack size (`-Xss1m`) allowed ~9x deeper recursion before exhausting memory than `-Xss256k`. | Each recursive method call allocates a new stack frame storing local variables, parameters, and return PC. When recursion has no terminating base case, the thread stack exhausts its allocated memory limit and triggers `StackOverflowError`. |
| **2. OutOfMemoryError** | `java -Xms16m -Xmx64m -cp target/classes com.auction.lab.OutOfMemoryExperiment` | Heap exhausted after allocating **31 chunks (~31 MB retained)**.<br>Caught `OutOfMemoryError: Java heap space`. | Iterations 10, 20, 30 showed total heap growing from 24 MB to 44 MB to 64 MB until max heap ceiling was hit. | Retained references inside `ArrayList<byte[]>` kept byte arrays reachable from the thread's stack root. Because objects were strongly reachable, the Garbage Collector could not free them, exhausting the 64 MB heap limit. |
| **3. String vs StringBuilder** | `java -cp target/classes com.auction.lab.StringVsStringBuilderExperiment 15000` | **String +=:**<br>27.952 ms (27,951,500 ns)<br><br>**StringBuilder:**<br>0.803 ms (803,300 ns)<br><br>Speedup ratio: **~34.8x faster** | Both produced identical strings of 15,000 characters. `StringBuilder` executed orders of magnitude faster with zero GC thrashing. | Strings in Java are immutable. Every `+=` copies the old array into a new one and leaves intermediate strings as garbage on the young heap ($O(N^2)$ copies). `StringBuilder` uses an internal resizable buffer and appends in amortized $O(1)$. |
| **4. Garbage Collection** | `java -Xlog:gc -cp target/classes com.auction.lab.GarbageCollectionExperiment` | HotSpot G1 Collector logged:<br>`GC(0) Pause Young (Normal) 5M->1M(10M) 1.818ms`<br>`GC(8) Pause Full (System.gc()) 28M->1M(102M) 5.910ms`<br>Free heap increased: 73.73 MB -> 99.82 MB. | Young GC collected dereferenced tokens during Phase 1. `System.gc()` after clearing the 400-token list reclaimed ~27 MB immediately in a Full GC pause. | Short-lived unreferenced objects in the Eden space are cleared quickly during Minor/Young GC. Objects reachable from active roots are retained; once cleared (`list = null`), they become unreachable and eligible for reclamation. `System.gc()` requests a collection cycle but does not guarantee execution timing. |
| **5. Boxed Integer Cache** | `java -cp target/classes com.auction.lab.IntegerCacheExperiment` | **100:** `a1 == a2` -> **true**, `.equals()` -> **true** (Identity hash: `2060468723`)<br><br>**1000:** `b1 == b2` -> **false**, `.equals()` -> **true** (Identity hashes: `1118140819` vs `1975012498`)<br><br>Boundaries: `-128 == -128` (true), `127 == 127` (true), `128 == 128` (false). | `==` succeeded for 100 because both references pointed to the same cached instance, but failed for 1000 where distinct heap objects were allocated. | JLS §5.1.7 mandates that autoboxing / `Integer.valueOf(int)` caches values between -128 and 127. Outside this range, new heap objects are allocated. `==` checks reference address equality, whereas `.equals()` compares underlying numeric value. |

---

## 2. Detailed Experiment Walkthroughs

### Experiment 1: StackOverflowError Depth Exploration

#### Commands Executed
```bash
# Test with 256k stack size
java -Xss256k -cp target/classes com.auction.lab.StackOverflowExperiment

# Test with 1m stack size
java -Xss1m -cp target/classes com.auction.lab.StackOverflowExperiment
```

#### Raw Output
```text
=== Experiment 1: StackOverflowError Exploration ===
Starting recursion to measure maximum stack depth...
Caught StackOverflowError!
Max recursion depth reached: 1541
Thread name: main

=== Experiment 1: StackOverflowError Exploration ===
Starting recursion to measure maximum stack depth...
Caught StackOverflowError!
Max recursion depth reached: 13959
Thread name: main
```

#### Student Explanation
- **What is a Stack Frame?** Whenever a Java thread executes a method, the JVM pushes a stack frame onto that thread's call stack. The frame contains method parameters (like `long depth`), local variables (`dummy1`, `dummy2`), operand stack, and the return address.
- **Why did StackOverflowError happen?** Because `recursiveMethod()` called itself unconditionally without a base case. Each call pushed another frame onto the thread's stack. With `-Xss256k`, the stack ran out of room after 1,541 frames. When we allocated 1 MB (`-Xss1m`), it was able to push 13,959 frames before overflowing.

---

### Experiment 2: OutOfMemoryError (Java Heap Space)

#### Command Executed
```bash
java -Xms16m -Xmx64m -cp target/classes com.auction.lab.OutOfMemoryExperiment
```

#### Raw Output
```text
=== Experiment 2: OutOfMemoryError (Java Heap Space) ===
Max Heap (-Xmx): 67,108,864 bytes (64.00 MB)
Initial Heap (-Xms): 18,874,368 bytes (18.00 MB)
Iteration 10: Allocated 10 MB | Total Heap: 24 MB | Free: 2 MB
Iteration 20: Allocated 20 MB | Total Heap: 44 MB | Free: 2 MB
Iteration 30: Allocated 30 MB | Total Heap: 64 MB | Free: 2 MB
Successfully caught OutOfMemoryError: Java heap space
Heap exhausted after allocating 31 chunks (~31 MB retained)
Cleaned retained references. Heap recovered successfully.
```

#### Student Explanation
- **Heap vs Stack:** Stack stores active method frames and local reference variables. The Heap stores all created objects (`new byte[...]` arrays, collections, domain models).
- **Why did OOM occur?** Each 1 MB array was added to `retainedMemoryChunks` (a `List<byte[]>`). Because the list was a local variable in the active `main` method, it served as a **GC Root**. Even when free heap memory shrank down to 2 MB, the Garbage Collector could not reclaim any byte arrays because they remained reachable. At 31 MB of retained arrays (plus JVM internal structures and object headers), the 64 MB heap limit was hit and the JVM threw `OutOfMemoryError: Java heap space`.

---

### Experiment 3: String += vs StringBuilder.append()

#### Command Executed
```bash
java -cp target/classes com.auction.lab.StringVsStringBuilderExperiment 15000
```

#### Raw Output
```text
=== Experiment 3: String += vs StringBuilder.append() ===
Running benchmark with 15,000 iterations...

[String +=]          Iterations: 15,000 | Final Length: 15,000 | Time: 27.952 ms (27,951,500 ns)
[StringBuilder]      Iterations: 15,000 | Final Length: 15,000 | Time: 0.803 ms (803,300 ns)

Verification: Both strings produced identical length: true
Speedup Ratio: StringBuilder was ~34.8x faster than String += in this run.
```

#### Student Explanation
- **Immutability of String:** `java.lang.String` is immutable. When we do `strResult += "x"`, Java cannot modify the existing string in place. It must allocate a brand new `String` object, copy all previous 14,999 characters into it, append `'x'`, and discard the old string as garbage. Over 15,000 iterations, this causes quadratic character copying ($O(N^2)$) and generates thousands of short-lived objects.
- **StringBuilder Buffer:** `StringBuilder` holds an internal expandable `byte[]`/`char[]` buffer. Appending to it takes amortized $O(1)$ time without creating garbage strings on every iteration. On this run, `StringBuilder` completed in 0.803 ms vs 27.952 ms for `+=`.

---

### Experiment 4: Garbage Collection Basics and -Xlog:gc

#### Command Executed
```bash
java -Xlog:gc -cp target/classes com.auction.lab.GarbageCollectionExperiment
```

#### Raw Excerpt from HotSpot G1 Collector
```text
[0.009s][info][gc] Using G1
=== Experiment 4: Garbage Collection & Reachability Demonstration ===

--- Phase 1: Allocating short-lived temporary objects ---
Allocated temporary token ID: 0
[0.050s][info][gc] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 5M->1M(10M) 1.818ms
[0.051s][info][gc] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 3M->1M(10M) 0.955ms
Allocated temporary token ID: 100
[0.052s][info][gc] GC(2) Pause Young (Normal) (G1 Evacuation Pause) 3M->1M(30M) 0.988ms
Allocated temporary token ID: 200
Allocated temporary token ID: 300
[0.060s][info][gc] GC(3) Pause Young (Normal) (G1 Evacuation Pause) 17M->1M(30M) 0.812ms
Allocated temporary token ID: 400
Phase 1 complete: 500 temporary objects created and dereferenced.

--- Phase 2: Retained objects transitioning to unreachable ---
[0.066s][info][gc] GC(4) Pause Young (Normal) (G1 Evacuation Pause) 17M->8M(30M) 3.989ms
[0.073s][info][gc] GC(5) Pause Young (Normal) (G1 Evacuation Pause) 18M->18M(34M) 6.081ms
[0.076s][info][gc] GC(6) Pause Young (Concurrent Start) (G1 Evacuation Pause) 24M->24M(102M) 2.260ms
[0.076s][info][gc] GC(7) Concurrent Mark Cycle
Retained [0.080s][info][gc] GC(7) Pause Remark 26M->26M(102M) 0.499ms
[0.082s][info][gc] GC(7) Pause Cleanup 26M->26M(102M) 0.067ms
[0.083s][info][gc] GC(7) Concurrent Mark Cycle 6.913ms
400 tokens in list. Approximate retained payload: 25,600 KB
Cleared list reference. All 400 objects are now unreachable!

--- Phase 3: Suggesting Garbage Collection via System.gc() ---
Free heap before System.gc(): 77,315,664 bytes (73.73 MB)
[0.146s][info][gc] GC(8) Pause Full (System.gc()) 28M->1M(102M) 5.910ms
Free heap after System.gc():  104,663,792 bytes (99.82 MB)
Garbage collection observation completed successfully.
```

#### Student Explanation
- **Generations (Young vs Old):** Java divides heap memory into the Young Generation (Eden space and Survivor spaces) and Old/Tenured Generation. Newly created objects enter Eden. Most objects die young (weak generational hypothesis).
- **Minor/Young GC vs Full GC:** Minor GC collects only the Young Generation; as seen above, `Pause Young (Normal) 5M->1M(10M)` took only 1.8 ms to sweep Eden. Full GC (`Pause Full (System.gc())`) inspects both Young and Old generations and took 5.91 ms, reclaiming ~27 MB after the 400 retained tokens were dereferenced (`retainedList = null`).
- **Why System.gc() is not an immediate command:** Calling `System.gc()` is only an explicit hint to the JVM. The JVM decides if and when to trigger garbage collection depending on heap pressure, GC ergonomics, and command-line flags (such as `-XX:+DisableExplicitGC`).

---

### Experiment 5: Boxed Integer Cache and Identity vs Equality

#### Command Executed
```bash
java -cp target/classes com.auction.lab.IntegerCacheExperiment
```

#### Raw Output
```text
=== Experiment 5: Boxed Integer Cache and '==' vs '.equals()' ===

--- Test A: Comparing boxed integers with value 100 (inside cache range) ---
Integer a1 = 100; Integer a2 = 100;
a1 == a2 :      true  (Identical object reference from cache)
a1.equals(a2) : true  (Values are numerically equal)
Identity hashCodes -> a1: 2060468723, a2: 2060468723

--- Test B: Comparing boxed integers with value 1000 (outside cache range) ---
Integer b1 = 1000; Integer b2 = 1000;
b1 == b2 :      false (Different heap object references!)
b1.equals(b2) : true  (Values are numerically equal)
Identity hashCodes -> b1: 1118140819, b2: 1975012498

--- Test C: Boundary values [-128, 127] ---
-128 == -128: true
127 == 127:   true
128 == 128:   false

Summary:
For wrapper classes in Java, always use .equals() or unbox to primitive before comparison.
```

#### Student Explanation
- **Reference Equality (`==`) vs Value Equality (`.equals()`):** When using `==` on object references (such as `Integer`), Java compares the memory addresses. When using `.equals()`, it compares the actual numeric value encapsulated inside the wrapper object.
- **The Integer Cache Range:** Under JLS §5.1.7, `Integer.valueOf()` caches values from `-128` to `127`. For `100`, both `a1` and `a2` receive the exact same reference (identity hash `2060468723`), making `==` evaluate to `true`. For `1000`, new heap objects are instantiated with different addresses (`1118140819` vs `1975012498`), causing `b1 == b2` to evaluate to `false` even though both represent the number one thousand.
- **Project Rule:** In our Online Auction System, whenever comparing wrapper IDs or amounts (`Integer auctionId`, `Integer bidderId`), always use `.equals()` or primitive `int` to avoid subtle reference bugs.
