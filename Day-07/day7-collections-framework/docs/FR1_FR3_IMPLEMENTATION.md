# Functional Requirements 1–3 Implementation Guide

This document describes how Functional Requirements 1 through 3 were implemented in Day 7 of the Online Auction System using Java Collections.

---

## 1. FR1 — User Authentication & Registration

### Requirement Summary
- Allow users to register with required details and assigned roles (`ADMIN`, `SELLER`, `BIDDER`).
- Store users in memory and prevent duplicate usernames.
- Authenticate users on login.
- Keep user passwords secure (never print passwords in logs or summaries).

### Collection Strategy & Justification
1. **`ArrayList<User>` (Primary User Registry):**
   - Preserves user registration order.
   - Enables sequential browsing of all registered users.
   - Provides efficient memory utilization for storing user objects.
2. **`HashSet<String>` (Unique Username Registry):**
   - Stores lowercase normalized usernames.
   - Provides $\mathcal{O}(1)$ average time complexity for checking whether a username is already taken (`registeredUsernames.contains(username)`).
   - Prevents duplicate registration instantly without having to scan the user list.

### Password Security Rule
- `User.toString()` and `User.getSummary()` explicitly omit the `password` field.
- Password verification is performed using `user.verifyPassword(rawPassword)` in-memory.

---

## 2. FR2 — Auction Listing Creation

### Requirement Summary
- Allow registered sellers to submit auction listings with product name, description, starting price, reserve price, start time, and end time.
- Validate that starting price is strictly positive and that the reserve price is at or above the starting price.
- Newly created listings must enter a **`PENDING`** state awaiting admin moderation.
- Store listings in an accessible catalogue.

### Collection Strategy & Justification
- **`ArrayList<Auction>` (Master Auction Catalogue):**
   - Stores all created listings in insertion order.
   - Enables fast indexed retrieval and clean table formatting in the console menu.
   - Retains full history of all auctions regardless of moderation status.

---

## 3. FR3 — Admin Moderation Workflow

### Requirement Summary
- Provide a moderation workflow where an admin can review pending auction listings.
- Admin can either **Approve** or **Reject** a listing with review notes.
- Approved listings become active and eligible for bidding; rejected listings are marked as rejected and cannot be treated as approved.
- A listing cannot be moderated more than once (prevent repeated moderation).
- Pending listings are tracked in a queue.

### Collection Strategy & Justification
- **`ArrayDeque<Auction>` (FIFO Pending Moderation Queue):**
   - Backed by an efficient circular array without node pointer overhead.
   - Follows First-In, First-Out (FIFO) fairness: earlier seller submissions are moderated first.
   - When an admin moderates a listing, its status in the master catalogue is updated to `APPROVED` or `REJECTED`, and it is removed from the pending queue.
   - Repeated moderation attempts check `if (auction.getStatus() != ListingStatus.PENDING)` and throw an `IllegalStateException`.

---

## 4. Console Menu Flow & Validation

The application coordinates FR1, FR2, and FR3 through an interactive console menu (`AuctionApp.java`):

1. **Option 1 (Register):** Validates inputs, creates user object, updates `ArrayList` and `HashSet`.
2. **Option 2 (Login):** Validates credentials, sets the active session user.
3. **Option 4 (Create Listing):** Accessible by sellers; puts listing in `PENDING` state and queues it.
4. **Option 5 (View Listings):** Displays master catalogue with status (`PENDING`, `APPROVED`, `REJECTED`).
5. **Option 6 (Admin Moderation):** Allows admin to process items from the FIFO queue.
6. **Graceful Error Recovery:** All exceptions (`IllegalArgumentException`, `IllegalStateException`, `NumberFormatException`) are caught cleanly, reporting user-friendly messages and returning to the menu without terminating the process.
