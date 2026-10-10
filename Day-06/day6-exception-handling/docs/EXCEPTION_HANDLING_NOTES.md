# Day 6: Exception Handling Notes

These are my personal study and training notes explaining how exception handling works in Java, written using real examples from our Online Auction System.

---

## 1. Checked vs Unchecked Exceptions

In Java, all exception types inherit from `java.lang.Throwable`. The two main categories we use in everyday coding are checked and unchecked exceptions.

| Concept | Checked Exception | Unchecked Exception |
|---|---|---|
| **Base Class** | Extends `java.lang.Exception` (excluding `RuntimeException`) | Extends `java.lang.RuntimeException` |
| **Compiler Check** | Enforced at compile time. The compiler demands you handle it (`try-catch`) or declare it (`throws`). | Not enforced at compile time. The compiler lets code compile without explicit handling. |
| **Typical Purpose** | Expected business rule failures or external conditions that a well-written caller can recover from. | Programming logic bugs, invalid arguments, or state violations that should not happen under normal inputs. |
| **Our Auction Project Example** | `InsufficientStockException`, `OrderProcessingException` | `InvalidQuantityException`, `InvalidBidException`, `NumberFormatException` |

### Why did we choose this design?

- **`InsufficientStockException` (Checked):** When a bidder or customer attempts to order 10 items of a camera, having only 2 in inventory is an expected business condition. The system should gracefully prompt the user to lower the quantity or pick another listing. Forcing the caller with a checked exception ensures the developer does not forget to handle this scenario.
- **`InvalidQuantityException` (Unchecked):** Passing a quantity of `0` or `-5` is an invalid input or illegal argument. Extending `RuntimeException` is standard Java practice because passing a negative count violates basic method preconditions.
- **`InvalidBidException` (Unchecked):** Bidding a negative or zero rupee amount violates the domain contract for placing a bid.

---

## 2. `throw` vs `throws`

Although their names look similar, they serve two distinct purposes in Java:

- **`throw` (Verb - An Action):** Used inside a method body to actually trigger and raise an exception object right at runtime.
  ```java
  if (requestedQty <= 0) {
      throw new InvalidQuantityException("Order quantity must be strictly greater than 0.");
  }
  ```
- **`throws` (Clause - A Declaration):** Placed in the method signature to warn callers that this method might propagate a checked exception up the call stack.
  ```java
  public int processOrder(AuctionItem item, int requestedQty) throws InsufficientStockException {
      // ...
  }
  ```

---

## 3. `try`, `catch`, and `finally` Semantics

- **`try` block:** Wraps code that could potentially throw an exception.
- **`catch` block:** Intercepts specific exception types if thrown inside the `try` block, preventing program crash and providing recovery steps.
- **`finally` block:** A block that **always executes**, whether the `try` block completed normally, threw an exception that was caught, or encountered an unhandled exception.

### When does `finally` run in our project?
We use `finally` to record an audit record in `AuditService` for every order fulfillment and bid attempt. Regardless of whether the bid or order succeeded, failed due to invalid quantity, or failed due to low stock, the `finally` block logs the outcome.

> **Rule:** Never return inside a `finally` block or put heavy business logic there, as that can accidentally swallow or suppress exceptions.

---

## 4. Multi-Catch Blocks

Introduced in Java 7, multi-catch allows a single `catch` block to handle multiple compatible exception types using the pipe (`|`) operator. This avoids duplicate error-handling code.

```java
try {
    return processOrder(item, requestedQty);
} catch (InsufficientStockException | InvalidQuantityException ex) {
    // Both checked and unchecked domain errors handled uniformly
    throw new OrderProcessingException("Order processing failed for " + item.getProductName(), ex);
}
```

*Note:* You cannot use multi-catch if the exceptions have an inheritance parent-child relationship (e.g. `catch (Exception | RuntimeException e)` is illegal because `RuntimeException` is already an `Exception`).

---

## 5. Exception Chaining & Preserving the Cause

When an application moves through multiple layers (e.g., repository -> service -> UI/API), lower-level exceptions are often wrapped in a higher-level domain exception. 

When doing this, it is critical **not** to lose the original root cause. Java provides constructor chaining on exceptions to preserve the underlying cause:

```java
// 1. Wrapping with original cause preserved
throw new OrderProcessingException("Order processing failed", ex);

// 2. Later inspecting the cause
try {
    orderProcessor.processOrderSafely(item, qty);
} catch (OrderProcessingException ope) {
    Throwable rootCause = ope.getCause(); // returns InsufficientStockException or InvalidQuantityException
    System.out.println("Underlying error: " + rootCause.getMessage());
}
```

---

## 6. Safe Error Recovery in Console Menus

In a good console app:
1. Input parsing errors (`NumberFormatException`) are caught inside the menu loop, displaying an informative message like `"Invalid menu selection: abc is not a number"`.
2. The loop then continues cleanly without exiting or terminating the JVM (`continue` or returning to loop top).
3. The state of the system remains unchanged—if a bid fails, the previous highest bid remains untouched. Stock is not partially deducted.
