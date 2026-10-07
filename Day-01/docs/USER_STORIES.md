# User Stories — Online Auction System
**Project:** Online Auction System  
**Training:** HCL Day 1 — Agile / Scrum Task  
**Author:** Hari Krishna  
**Date:** October 2026

---

## What is a User Story?

A **user story** is a short, simple description of a feature told from the perspective of the person who wants it.

Format:
> As a **\<role\>**, I want **\<functionality\>**, so that **\<benefit\>**.

---

## Sprint Backlog Table

| ID    | User Story (Summary)                          | Story Points | Status  |
|-------|-----------------------------------------------|:------------:|---------|
| US-01 | As a user, I want to register/login by role   | 3            | To Do   |
| US-02 | As a seller, I want to create an auction listing | 5          | To Do   |
| US-03 | As an admin, I want to review auction listings | 3           | To Do   |
| US-04 | As a bidder, I want to place validated bids   | 5            | To Do   |
| US-05 | As a bidder, I want proxy/auto-bidding        | 8            | To Do   |
| US-06 | As the system, I want to prevent bid sniping  | 5            | To Do   |
| US-07 | As the system, I want to close auctions and declare winners | 5 | To Do |
| US-08 | As a buyer/seller, I want to complete settlement | 8         | To Do   |

**Total Story Points: 42**  
**Velocity Target (Sprint 1): 21 points (US-01 to US-04)**

---

## Detailed User Stories

---

### US-01 — User Authentication (FR1)

**User Story:**
> As a **user (buyer, seller, or admin)**,  
> I want to **register an account and log in using my role and credentials**,  
> so that **I can access features appropriate to my role securely**.

**Story Points:** 3

**Acceptance Criteria:**
- [ ] A new user can register by providing name, email, password, and role (Buyer / Seller / Admin)
- [ ] Registered users can log in using email and password
- [ ] The system displays role-appropriate options after login
- [ ] Invalid credentials show a clear error message
- [ ] Duplicate email registration is rejected

**Definition of Done:**
- [ ] Registration logic is implemented and tested
- [ ] Login logic validates credentials correctly
- [ ] Role-based menu/navigation is functional
- [ ] Basic exception handling added (e.g., invalid input)
- [ ] Code reviewed, compiles cleanly, and follows Java naming conventions

---

### US-02 — Auction Listing (FR2)

**User Story:**
> As a **seller**,  
> I want to **create an auction listing with product details, starting price, reserve price, start time, and end time**,  
> so that **buyers can find and bid on my items**.

**Story Points:** 5

**Acceptance Criteria:**
- [ ] Seller can enter: item name, description, starting price, reserve price, start time, end time
- [ ] Starting price must be greater than the minimum allowed (INR 100)
- [ ] Reserve price must be ≥ starting price
- [ ] End time must be after start time
- [ ] Listing is saved in "Pending Admin Approval" status
- [ ] Seller can view their active and pending listings

**Definition of Done:**
- [ ] Auction listing creation is implemented
- [ ] Input validation is applied for prices and dates
- [ ] Listing status defaults to "PENDING"
- [ ] Code compiles without errors
- [ ] Tested with valid and invalid inputs

---

### US-03 — Admin Moderation (FR3)

**User Story:**
> As an **admin**,  
> I want to **review pending auction listings and approve or reject them**,  
> so that **only legitimate auctions are visible to buyers**.

**Story Points:** 3

**Acceptance Criteria:**
- [ ] Admin can see a list of all pending auction listings
- [ ] Admin can approve a listing (status → ACTIVE)
- [ ] Admin can reject a listing with a reason (status → REJECTED)
- [ ] Seller is notified of approval/rejection (console message)
- [ ] Approved listings become visible to bidders

**Definition of Done:**
- [ ] Admin approval/rejection logic is implemented
- [ ] Status transitions (PENDING → ACTIVE / REJECTED) work correctly
- [ ] Console notification message is displayed to seller
- [ ] Tested for both approve and reject flows

---

### US-04 — Bidding (FR4)

**User Story:**
> As a **bidder**,  
> I want to **place a bid that is valid (exceeds current highest bid by at least the minimum increment)**,  
> so that **I have a fair chance of winning the auction**.

**Story Points:** 5

**Acceptance Criteria:**
- [ ] Bidder can view active auctions and current highest bid
- [ ] Bidder can enter a bid amount
- [ ] Bid is accepted only if: new bid ≥ current highest bid + MINIMUM_BID_INCREMENT (INR 500)
- [ ] Bid below the minimum is rejected with a helpful message
- [ ] Accepted bid updates the current highest bid and highest bidder
- [ ] Bidder cannot bid on their own listing

**Definition of Done:**
- [ ] Bid validation logic uses `AuctionConstants.MINIMUM_BID_INCREMENT`
- [ ] Highest bid and bidder are updated on successful bid
- [ ] Appropriate error messages shown for invalid bids
- [ ] Edge cases tested: equal bid, zero bid, seller self-bid

---

### US-05 — Proxy / Auto Bidding (FR5)

**User Story:**
> As a **bidder**,  
> I want to **set a maximum proxy bid so the system automatically bids on my behalf**,  
> so that **I can win auctions without having to manually bid every time someone outbids me**.

**Story Points:** 8

**Acceptance Criteria:**
- [ ] Bidder can enable proxy bidding and specify a maximum proxy amount
- [ ] When another bidder places a bid, the system automatically increments the proxy bidder's bid
- [ ] Auto-bid increases by exactly MINIMUM_BID_INCREMENT each step
- [ ] Auto-bidding stops when either the proxy bidder wins or their max amount is exceeded
- [ ] Bidder is notified when outbid and proxy limit is reached

**Definition of Done:**
- [ ] Proxy bidding engine logic is implemented
- [ ] Automatic bid placement uses `AuctionConstants.MINIMUM_BID_INCREMENT`
- [ ] No infinite loops in proxy bidding logic
- [ ] Tested with two competing proxy bidders
- [ ] Edge case: both bidders have the same max — first proxy bid wins

---

### US-06 — Anti-Sniping (FR6)

**User Story:**
> As the **auction system**,  
> I want to **automatically extend the auction end time when a valid bid is placed within the final 2 minutes**,  
> so that **snipers cannot win by bidding at the very last second unfairly**.

**Story Points:** 5

**Acceptance Criteria:**
- [ ] If a bid is placed when `(endTime - currentTime) ≤ ANTI_SNIPE_MINUTES (2 minutes)`, the end time is extended
- [ ] End time is extended by `ANTI_SNIPE_EXTENSION_MINUTES (5 minutes)`
- [ ] The extension can happen multiple times (if bids keep coming in the extended window)
- [ ] Bidder is notified: "Auction extended due to late bid"
- [ ] Anti-snipe logic uses `AuctionConstants.ANTI_SNIPE_MINUTES`

**Definition of Done:**
- [ ] Anti-snipe check runs after every valid bid is placed
- [ ] End time is updated correctly
- [ ] Extension uses `AuctionConstants.ANTI_SNIPE_EXTENSION_MINUTES`
- [ ] Notification message displayed
- [ ] Tested with a bid placed exactly at 2:00, 1:59, and 3:00 before end

---

### US-07 — Auction Closing and Winner Declaration (FR7)

**User Story:**
> As the **auction system**,  
> I want to **automatically close the auction at end time, determine the winner, and declare them only if the reserve price is met**,  
> so that **the auction result is fair and transparent**.

**Story Points:** 5

**Acceptance Criteria:**
- [ ] Auction status changes to CLOSED when end time is reached
- [ ] The highest bidder is identified
- [ ] If highest bid ≥ reserve price → Winner is declared, status = SOLD
- [ ] If highest bid < reserve price → No winner, status = UNSOLD (reserve not met)
- [ ] Winner and seller are both notified (console messages)
- [ ] Auction summary is displayed (winner, winning bid, reserve status)

**Definition of Done:**
- [ ] Auction close logic runs at end time
- [ ] Reserve price check is implemented
- [ ] Winner declaration or "reserve not met" message is displayed
- [ ] Auction status is updated correctly (SOLD / UNSOLD / CLOSED)
- [ ] Tested for reserve met and not met scenarios

---

### US-08 — Settlement and Shipment (FR8)

**User Story:**
> As a **winning buyer and seller**,  
> I want to **complete the payment to the platform and receive shipment information**,  
> so that **the item is delivered securely and the seller receives payment after shipment**.

**Story Points:** 8

**Acceptance Criteria:**
- [ ] Winner is prompted to pay the winning bid amount to the platform
- [ ] Platform deducts the fee (5%) and holds the net amount for the seller
- [ ] Seller is notified to provide shipment/courier information
- [ ] Seller submits shipment details (courier name, tracking number)
- [ ] Seller receives payout (winning bid − platform fee) after shipment is confirmed
- [ ] Buyer receives a shipment notification with tracking details

**Definition of Done:**
- [ ] Payment simulation is implemented (console-based)
- [ ] Platform fee calculation uses `AuctionConstants.PLATFORM_FEE_PERCENTAGE`
- [ ] Shipment details captured from seller input
- [ ] Seller payout is calculated and displayed
- [ ] End-to-end flow tested: bid → win → pay → ship → payout

---

## Fibonacci Story Point Scale Used

| Points | Meaning                              |
|:------:|--------------------------------------|
| 1      | Very simple — 1-2 lines of logic     |
| 2      | Simple — straightforward feature     |
| 3      | Small — some logic required          |
| 5      | Medium — multiple conditions/flows   |
| 8      | Complex — multiple interacting parts |

---

*End of User Stories Document*
