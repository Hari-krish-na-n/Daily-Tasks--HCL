# ArrayList vs. LinkedList Performance Timing Analysis

In this experiment, I benchmarked `java.util.ArrayList` against `java.util.LinkedList` on Java 21 to see how internal data structures affect real-world execution time.

---

## 1. Experimental Setup & Methodology

- **Java Environment:** Java 21 (JDK 27 build with `-release 21`), Oracle HotSpot 64-Bit Server VM
- **Operating System:** Windows 11 (x86_64)
- **High-Resolution Clock:** `System.nanoTime()`
- **Warmup Procedure:** Executed 5,000 preliminary insertions and traversals prior to measurement to allow HotSpot JIT tier compilation to stabilize.
- **Fair Comparison:** Identical element counts, identical data types (`Integer`), and identical operation counts executed on both list implementations.

---

## 2. Actual Measured Benchmark Results

Here are the measurements captured directly by executing `com.auction.demo.ListPerformanceBenchmark`:

| Operation | Element / Op Count | Collection | Measured Time (Nanoseconds) | Time (Milliseconds) | Speed Ratio |
|---|---|---|---|---|---|
| **Insertion at beginning (`add(0, val)`)** | 50,000 elements | `ArrayList` | 238,325,400 ns | 238.325 ms | Baseline |
| **Insertion at beginning (`add(0, val)`)** | 50,000 elements | `LinkedList` | 12,733,000 ns | 12.733 ms | **~18.7x faster** |
| **Random Indexed Access (`get(i)`)** | 20,000 lookups (over 50k items) | `ArrayList` | 3,990,500 ns | 3.991 ms | **~188.6x faster** |
| **Random Indexed Access (`get(i)`)** | 20,000 lookups (over 50k items) | `LinkedList` | 752,762,700 ns | 752.763 ms | Baseline (Slow) |

---

## 3. Analysis & Explanation of Differences

### Test 1: Insertion at the Beginning (`add(0, item)`)
- **`ArrayList` Behavior:** `ArrayList` is backed by a contiguous array in memory (`Object[] elementData`). Inserting at index 0 forces the JVM to invoke `System.arraycopy()` to shift all $N$ existing elements one position to the right. As $N$ grows up to 50,000, each insert requires an average of $N/2$ shifts, causing total insertion time to scale at $\mathcal{O}(N^2)$.
- **`LinkedList` Behavior:** `LinkedList` is a doubly linked list where each node has `prev`, `next`, and `item` references. Inserting at the beginning simply instantiates a new `Node`, links it to the former `first` node, and updates the `first` pointer. This is an $\mathcal{O}(1)$ operation per insertion with no element shifting.
- **Caveat:** Even though `LinkedList` avoids element shifting, each insert allocates a heap object (`Node`), creating garbage collection pressure and memory overhead. For small sizes ($N < 500$), `ArrayList` can sometimes match or beat `LinkedList` due to contiguous CPU cache locality and low allocation overhead.

### Test 2: Random Indexed Access (`get(index)`)
- **`ArrayList` Behavior:** `ArrayList` provides true $\mathcal{O}(1)$ random access. Because elements sit in contiguous memory, calculating the exact memory address is an instant pointer arithmetic formula:
  $$\text{Address}(i) = \text{BaseAddress} + (i \times \text{ReferenceSize})$$
  In our run, 20,000 random lookups took just 3.99 ms.
- **`LinkedList` Behavior:** `LinkedList` does not support direct index arithmetic. Calling `get(i)` requires starting at either the head (`first`) or tail (`last`) and following pointer references node by node until index $i$ is reached. For $N = 50,000$, an average lookup traverses 12,500 node pointers ($\mathcal{O}(N)$ per lookup). Doing this 20,000 times resulted in 752.76 ms—nearly 189 times slower than `ArrayList`.

---

## 4. Key Takeaways for the Online Auction System

1. **Why `ArrayList` is the right choice for the Auction catalogue & User lists:**
   The Online Auction System frequently iterates through registered users, renders tables of auctions, and looks up items by index. `ArrayList` provides fast indexed reads and cache-friendly iteration.
2. **When to consider `LinkedList` or `ArrayDeque`:**
   If we only need queue behavior (inserting at one end and removing from the other), `ArrayDeque` is actually superior to both `ArrayList` and `LinkedList` because it uses a circular array without node allocation overhead.
3. **Important Note on Measurements:**
   All execution times depend on CPU architecture, background OS tasks, heap sizing, and JIT warm-up. These numbers demonstrate algorithmic complexity differences rather than hardcoded guarantees.
