# Debugging Notes

## Bug

In `com.auction.service.AuctionService`, a deliberate bug was introduced in the bid validation logic (`validateBid` method).

The auction rule states:
- To be accepted, a new bid must be greater than or equal to the current bid plus the minimum required increment (`minimumIncrement = ₹500`).

The buggy code was written as:
```java
// BUGGY VALIDATION LOGIC:
return newBid > (currentBid - minimumIncrement);
```

Because of the minus sign (`-`) instead of addition (`+`), when:
- Current Bid = `₹50,000`
- Minimum Increment = `₹500`
- New Bid submitted = `₹49,000`

The condition evaluated to:
`49000 > (50000 - 500)` => `49000 > 49500` (or with `<=` logic, an invalid bid below current bid was mistakenly accepted!).

---

## Expected Behaviour

- **Minimum Required Bid**: `currentBid + minimumIncrement` = `₹50,000 + ₹500 = ₹50,500`.
- **Bid of ₹49,000**: Should be **rejected** because it is lower than the current bid of ₹50,000.
- **Bid of ₹50,500 or higher**: Should be **accepted**.

---

## Actual Behaviour

Before the fix, submitting a bid of `₹49,000` was incorrectly evaluated as valid and accepted by the auction system, corrupting the auction price progression.

---

## Debugging Process

The issue was diagnosed using IDE debugging capabilities without relying on ad-hoc `System.out.println` statements.

### 1. Breakpoint
- Set a regular breakpoint on line in `AuctionService.java` at the entry of the `placeBid(...)` method.
- When `AuctionApp` is executed in Debug mode (`F5` in VS Code / `Shift+F9` in IntelliJ), execution pauses automatically before the bid is validated.

### 2. Conditional Breakpoint
- To avoid pausing on valid bids and isolate only the anomalous condition, configured a **conditional breakpoint** at `validateBid(...)`:
  - **Condition**: `newBid < currentBid`
- This ensures the debugger only halts execution when an underbid occurs (e.g. `newBid == 49000.0` while `currentBid == 50000.0`).

### 3. Watch Variables
Added the following expressions to the **Watch** window to inspect evaluated states in real-time:
- `currentBid` (`50000.0`)
- `newBid` (`49000.0`)
- `minimumIncrement` (`500.0`)
- `currentBid + minimumIncrement` (`50500.0`)
- `newBid >= (currentBid + minimumIncrement)` (`false`)

### 4. Step Over (`F10`)
- Used **Step Over** to advance execution line-by-line within `placeBid` without stepping into helper standard library methods.

### 5. Step Into (`F11`)
- Used **Step Into** when reaching `validateBid(currentBid, bidAmount, minimumIncrement)` to inspect the exact internal evaluation logic.

### 6. Step Out (`Shift+F11`)
- Used **Step Out** to return back to the caller frame (`placeBid`) after inspecting the condition inside `validateBid`.

### 7. Call Stack
- Inspected the **Call Stack** panel to trace the execution frames:
  1. `AuctionService.validateBid(double, double, double)` (Top of stack)
  2. `AuctionService.placeBid(int, Bidder, double, int)`
  3. `AuctionApp.main(String[])` (Thread entry)

### 8. Logpoint
- Placed a **Logpoint** at the start of `validateBid` with the log message:
  `[LOGPOINT] Validating bid: newBid={newBid}, currentBid={currentBid}, minIncrement={minimumIncrement}`
- This logged variable state directly to the Debug Console without pausing execution or modifying source code with print statements.

### 9. Hot Code Replace (HCR)
- In supported JVM debug sessions (standard in VS Code Java Debugger / Eclipse / IntelliJ via standard JVM JPDA/JDI), method body modifications can be reloaded on the fly:
  - Replaced the erroneous formula in `validateBid` while the JVM was paused at the breakpoint.
  - Saved the file, and Hot Code Replace replaced the method bytecode in the running JVM without needing a full server restart.

---

## Fix

Corrected the validation condition in `AuctionService.java` to enforce that any new bid must be at least the current bid plus the minimum increment:

```java
public boolean validateBid(double currentBid, double newBid, double minimumIncrement) {
    double minimumRequiredBid = currentBid + minimumIncrement;
    return newBid >= minimumRequiredBid;
}
```

---

## Result

After applying the fix:

```text
Attempt 1: Bidder Krishna submits ₹49,000
Bid rejected: ₹49000.0 is invalid.
  -> Minimum required bid: ₹50500.0 (Current: ₹50000.0 + Increment: ₹500.0)

Attempt 2: Bidder Krishna submits ₹52,000
Bid accepted! Amount: ₹52000.0 by Krishna

Attempt 3: Bidder Amit submits ₹55,000
Bid accepted! Amount: ₹55000.0 by Amit
```

- Invalid bid (`₹49,000`) -> **Rejected**
- Valid bids (`₹52,000`, `₹55,000`) -> **Accepted**

---

## Recommended IDE Screenshots to Capture

Capture the following screenshots from your IDE (VS Code / IntelliJ IDEA / Eclipse) for your submission:

1. **Breakpoint & Conditional Breakpoint**:
   - Screenshot showing the red breakpoint dot on `validateBid` in `AuctionService.java` with the condition `newBid < currentBid` popup visible.
2. **Watch Variables & Variables Panel**:
   - Screenshot showing the **Variables** and **Watch** panel during debugging displaying `currentBid = 50000.0`, `newBid = 49000.0`, and `minimumIncrement = 500.0`.
3. **Call Stack Panel**:
   - Screenshot showing the active stack trace showing `AuctionApp.main` -> `AuctionService.placeBid` -> `AuctionService.validateBid`.
4. **Logpoint & Debug Console**:
   - Screenshot of the logpoint marker and the debug console showing the evaluated message without manual `println`.
5. **Console Output**:
   - Screenshot of the terminal/console showing the final successful run with the ₹49,000 bid rejected and ₹52,000 accepted.
