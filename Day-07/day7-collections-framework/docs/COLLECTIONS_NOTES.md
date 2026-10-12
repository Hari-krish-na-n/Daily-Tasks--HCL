# Collections Framework Study Notes & Implementation Guide

This document records what I learned while studying and applying the Java Collections Framework (JCF) during Day 7 of the training program.

---

## 1. Overview of Core Interfaces

The Java Collections Framework standardizes how groups of objects are stored and manipulated.

```text
               Iterable
                  │
              Collection
         ┌────────┼────────┐
       List      Set     Queue
         │        │        │
    ArrayList  HashSet  ArrayDeque
   LinkedList  TreeSet  PriorityQueue
```

---

## 2. List Implementations: `ArrayList` vs. `LinkedList`

| Property | `ArrayList` | `LinkedList` |
|---|---|---|
| **Underlying Structure** | Resizable dynamic array | Doubly linked list of `Node` objects |
| **Indexed Access (`get(i)`)** | $\mathcal{O}(1)$ direct array index calculation | $\mathcal{O}(N)$ sequential node traversal |
| **Insertion at Tail (`add(e)`)** | Amortized $\mathcal{O}(1)$ (occasional array resize) | $\mathcal{O}(1)$ pointer link update |
| **Insertion at Head (`add(0, e)`)** | $\mathcal{O}(N)$ (shifts all elements via `System.arraycopy`) | $\mathcal{O}(1)$ (creates node & updates head pointer) |
| **Memory Overhead** | Low (contiguous array with small unused capacity) | High (each node stores item + 2 references) |
| **CPU Cache Locality** | Excellent (contiguous memory blocks) | Poor (nodes scattered across heap) |

### When to choose which?
- Choose **`ArrayList`** by default for lists, catalogues, and records where elements are read, iterated, or accessed by index frequently.
- Choose **`LinkedList`** only if you need frequent additions/removals at both ends and are not using `ArrayDeque`.

---

## 3. Set Implementations: `HashSet`, `LinkedHashSet`, and `TreeSet`

All `Set` implementations enforce uniqueness (no duplicate elements allowed). They differ in how elements are stored and ordered:

| Implementation | Internal Mechanism | Ordering Guarantee | Basic Ops Time | Best Use Case |
|---|---|---|---|---|
| **`HashSet`** | Hash table (backed by `HashMap`) | **No order** guaranteed (unpredictable) | $\mathcal{O}(1)$ average | High-speed deduplication & fast membership lookup (e.g., checking if username exists) |
| **`LinkedHashSet`** | Hash table + doubly-linked list | **Insertion order** preserved | $\mathcal{O}(1)$ average | Deduplicating elements while preserving the sequence they arrived in (e.g., recent request IDs) |
| **`TreeSet`** | Red-Black Tree (backed by `TreeMap`) | **Sorted order** (natural or Comparator) | $\mathcal{O}(\log N)$ | Maintaining unique items in sorted order (e.g., sorted price tiers or alphabetical usernames) |

---

## 4. Queue Implementations: `ArrayDeque` vs. `PriorityQueue`

Queues are designed for holding elements prior to processing.

### A. `ArrayDeque` (FIFO Queue / LIFO Stack)
- Backed by an efficient resizable circular array.
- Standard FIFO: `offer()` adds to the tail; `poll()` retrieves and removes from the head.
- Faster and uses significantly less memory than `LinkedList` because no `Node` objects are allocated.
- **Used in FR3:** Pending moderation queue where listings are moderated in the order they were submitted.

### B. `PriorityQueue` (Priority-Based Queue)
- Backed by a balanced binary min-heap.
- Elements are ordered by their natural order or an explicit `Comparator`.
- `poll()` always returns the element with the highest priority (lowest value according to comparator).
- **Important:** Elements with equal priority do **not** guarantee FIFO order!
- **Used in Library Waitlist:** Ensuring Faculty requests (priority tier 1) are served before Student requests (priority tier 2).

---

## 5. Iteration Patterns & `ConcurrentModificationException`

### Pattern 1: Enhanced `for`-loop (for-each)
- Best for clean, readable traversal when you only need to read elements:
  ```java
  for (Auction auction : auctionList) {
      System.out.println(auction.getSummary());
  }
  ```

### Pattern 2: `Iterator` with Safe `remove()`
- If an element needs to be removed while iterating, calling `list.remove(item)` inside an enhanced-for loop triggers a `ConcurrentModificationException`.
- The safe way is to call `iterator.remove()`:
  ```java
  Iterator<BorrowRequest> it = requests.iterator();
  while (it.hasNext()) {
      BorrowRequest req = it.next();
      if (req.isOverdue()) {
          it.remove(); // Safely removes element and updates modCount
      }
  }
  ```

### Pattern 3: `ListIterator`
- Exclusive to `List` implementations.
- Allows traversing backward (`hasPrevious()`, `previous()`), inspecting indices (`nextIndex()`, `previousIndex()`), and inline element updates (`set(item)`).

### What is `ConcurrentModificationException`?
- Many Java collections maintain an internal counter named `modCount` representing the number of times the list has been structurally modified.
- When an `Iterator` is created, it captures `expectedModCount = modCount`.
- On every call to `next()`, it checks `if (modCount != expectedModCount) throw new ConcurrentModificationException()`.
- If code modifies the list directly (via `list.add()` or `list.remove()`) during an iteration loop, `modCount` increments, causing the check to fail immediately (fail-fast behavior).
- **Note:** It does **not** require multiple threads; it is a safety mechanism to prevent undefined behavior when iterating over a mutating collection.
