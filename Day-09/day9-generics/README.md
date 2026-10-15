# Day 9: Generics — Generic Toolkit, AutoCloseable CSV Parser, and Generic CRUD Repository

This project forms part of the Java training tasks for **Day 9**, continuing directly from the Online Auction System developed across Days 1 through 8.

---

## 1. Overview & Objectives

Day 9 explores Java Generics, resource management, and reusable repository architecture:
1. **Generic Toolkit:**
   - `Pair<K, V>`: Storing heterogeneous key-value pairs.
   - `Box<T extends Number>`: Upper-bounded type parameter restricting values to numeric types (`Integer`, `Double`, etc.) while rejecting non-numeric types at compile time.
   - `max()` method bounded with `<T extends Comparable<T>>`.
   - Wildcards and the **PECS** rule (**Producer Extends, Consumer Super**):
     - Reading values from `List<? extends Number>` (Producer).
     - Appending values into `Collection<? super Integer>` (Consumer).
   - Type Erasure demonstration explaining runtime bytecode representation and avoiding raw types.
2. **try-with-resources and AutoCloseable:**
   - Reading `products.csv` using try-with-resources with `BufferedReader` and a custom `ResourceTracker` implementing `AutoCloseable`.
   - Verifying the exact LIFO closing order of multiple resources.
   - Graceful handling and logging for missing files and malformed CSV rows.
3. **Generic Repository for the Online Auction System:**
   - Definition of `CrudRepository<T, ID>` interface.
   - In-memory implementation `InMemoryRepository<T, ID>` backed by `LinkedHashMap`.
   - Zero raw types and unchecked warnings through a type-safe `Function<T, ID> idExtractor`.
   - Clear upsert semantics for duplicate IDs.
   - Multi-entity reuse demonstrated for `User`, `Auction`, and `Bid`.

---

## 2. Project Structure

```text
Day-09/day9-generics/
├── pom.xml
├── README.md
├── docs/
│   ├── GENERICS_GUIDE.md
│   ├── PECS_AND_WILDCARDS.md
│   └── REPOSITORY_DESIGN.md
└── src/
    ├── main/
    │   ├── resources/
    │   │   └── products.csv                     <-- Seed CSV data with intentional malformed row
    │   └── java/
    │       └── com/auction/
    │           ├── io/
    │           │   ├── ProductCsvReader.java    <-- try-with-resources CSV reader
    │           │   └── ResourceTracker.java     <-- AutoCloseable resource tracer
    │           ├── repository/
    │           │   ├── CrudRepository.java      <-- Generic CRUD interface
    │           │   └── InMemoryRepository.java  <-- Generic in-memory implementation
    │           ├── toolkit/
    │           │   ├── GenericToolkit.java      <-- Pair, Box, max, PECS helpers
    │           │   └── GenericToolkitDemo.java  <-- Executable demo application
    │           └── model/                       <-- Domain models (User, Auction, Bid, AuctionItem)
    └── test/java/com/auction/
        ├── io/ProductCsvReaderTest.java         <-- Closing order & CSV error handling tests
        ├── repository/InMemoryRepositoryTest.java<-- CRUD operations & entity reuse tests
        └── toolkit/GenericToolkitTest.java      <-- Bounded types, Pair, max, PECS tests
```

---

## 3. Generic Toolkit & PECS Explanation

### PECS: Producer Extends, Consumer Super
- **Producer (`? extends T`):** Use when you only **read** from a collection. The collection *produces* items of type `T` (or a subtype).
  ```java
  public static double sumOfNumbers(List<? extends Number> numbers) {
      double sum = 0.0;
      for (Number n : numbers) { // Reading is safe
          sum += n.doubleValue();
      }
      return sum;
  }
  ```
- **Consumer (`? super T`):** Use when you only **write** into a collection. The collection *consumes* items of type `T`.
  ```java
  public static void appendIntegers(Collection<? super Integer> consumer, int from, int to) {
      for (int i = from; i <= to; i++) {
          consumer.add(i); // Writing Integer is safe
      }
  }
  ```

### Type Erasure
Java generics provide compile-time type safety. At compilation, the compiler erases all generic type parameters and replaces them with their bounds (or `Object` if unbounded) and inserts casts where necessary. Therefore, at runtime, `List<String>` and `List<Integer>` share the exact same raw `List.class`.

---

## 4. try-with-resources & Closing Order

Resources defined in the header of a `try (...)` block are automatically closed in **reverse order of their creation** (LIFO):
```java
try (ResourceTracker r1 = new ResourceTracker("DatabaseConnection");
     ResourceTracker r2 = new ResourceTracker("NetworkSocket")) {
    r1.doWork();
    r2.doWork();
} // r2 closes FIRST, r1 closes SECOND
```

---

## 5. Generic Repository (`CrudRepository<T, ID>`)

```java
public interface CrudRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean deleteById(ID id);
}
```

- **ID Extractor:** Because entities may not share an interface with `getId()`, `InMemoryRepository` accepts `Function<T, ID> idExtractor` (e.g. `User::getId`, `Auction::getId`, `Bid::getId`).
- **Duplicate ID Behavior:** `save()` follows *upsert* semantics: if an entity with the same ID already exists, it is replaced and updated in the backing `LinkedHashMap`.

---

## 6. How to Build and Run

```bash
# Navigate to Day 9 directory
cd Day-09/day9-generics

# Compile and run unit tests (16 tests)
mvn test

# Package executable JAR
mvn package

# Run the console demo application
java -jar target/day9-generics-1.0-SNAPSHOT.jar
```
