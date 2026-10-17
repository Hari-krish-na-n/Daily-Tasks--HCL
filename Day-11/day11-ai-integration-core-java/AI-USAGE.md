# AI Usage Log — Day 11

**Author:** Hari Krishnan  
**Training Program:** HCL Core Java Training — Online Auction System  
**Topic:** AI-Assisted Development, Prompt Engineering, and Critical Review  

---

## AI Usage Summary Table

| Task | Prompt or suggestion | My review | Changes made | Verification |
|---|---|---|---|---|
| **1. Student POJO Generation & Prompt Engineering** | *Prompt 1 (Vague):* "Create a Student class in Java."<br><br>*Prompt 2 (Specific):* "Create a Java Student POJO with private fields for studentId, name, and email. Include appropriate constructors, getters, setters, equals(), hashCode(), and toString(). Validate required fields and avoid exposing mutable internal collections. Follow the Java version and coding conventions used in this repository." | The vague prompt generated package-private fields, no input validations, and lacked `equals()` and `hashCode()`. The specific prompt produced encapsulated fields and basic checks, but used inefficient inline regex parsing. | Accepted the specific prompt structure. Pre-compiled the regex pattern into a `private static final Pattern` constant to prevent recompilation on every setter call, added negative ID guard clauses, and standardized `IllegalArgumentException` messages. | Verified via `StudentServiceTest.testValidStudentCreation()`, `testInvalidStudentCreation()`, and `testEqualsAndHashCode()`. |
| **2. StudentService Implementation & Defect Review** | *Prompt:* "Implement StudentService with operations to add student, find student by ID, retrieve student list, update student information, and remove student using suitable collections." | AI suggested using a simple `ArrayList<Student>` with linear loop searching, returned direct internal list references, swallowed missing record errors silently, and permitted duplicate student IDs. | Rejected the raw `ArrayList` approach. Switched backing storage to `LinkedHashMap<Integer, Student>` for O(1) lookups and insertion ordering. Implemented defensive copying (`Collections.unmodifiableList`), duplicate ID detection, and returned `Optional<Student>` instead of nulls. | Verified via `StudentServiceTest.testSafeCollectionAccess()`, `testDuplicateStudentIdRejected()`, and `testUpdateStudent()`. |
| **3. Entity & Repository Extension (BidRepository & Settlement)** | *Prompt:* "Create an AuctionListing and Bid entity and custom repository for auction closing and settlement." | AI attempted to create a duplicate `Bid` and `Auction` entity alongside a duplicate bespoke repository that completely ignored the existing Day 9 `CrudRepository` interface and Day 8 domain models. | Rejected duplicate domain classes. Reused existing `Bid` and `Auction` entities. Extended `BaseEntity` with `Settlement` to model FR8 escrow. Reused `InMemoryRepository<T, ID>` from Day 9 by creating `InMemoryBidRepository extends InMemoryRepository<Bid, Integer> implements BidRepository`. | Verified via `BidRepositoryTest.testSaveAndFindById()`, `testDuplicateIdUpsert()`, and `testSpecializedQueries()`. |
| **4. FR7 & FR8 Functional Requirements Implementation** | *Prompt:* "Implement FR7 (auction closing with reserve price check) and FR8 (escrow payment, 5% platform fee deduction, and shipment tracking) in plain Core Java compatible with the Online Auction System." | AI correctly calculated the 5% platform fee, but initially allowed sellers to mark an auction as shipped before the winning buyer had submitted payment, violating the escrow invariant. | Added escrow state machine validation: `submitShipment()` now verifies `if (!this.paid)` and throws `IllegalStateException`. Integrated `closeAuction()` with automatic `Settlement` entity generation when reserve is met. | Verified via `AuctionFulfillmentServiceTest.testCloseAuctionReservePriceMet()`, `testCloseAuctionReservePriceNotMet()`, and `testCompleteSettlementWorkflow()`. |

---

## Key Learnings on AI in Core Java Development

1. **Prompt Specificity is Paramount:** Vague prompts yield code that looks superficially correct in simple tutorials but fails in enterprise environments (missing validations, broken equality contracts, mutable leaks).
2. **Never Accept Collection Returns Blindly:** AI models frequently return internal mutable collections (`return this.items;`). A developer must always apply defensive copying (`Collections.unmodifiableList` or new collection copy).
3. **Respect Established Architecture:** AI suggestions often recreate redundant classes or ignore existing abstractions unless explicitly guided by repository conventions (e.g. reusing `BaseEntity` and generic `CrudRepository`).
4. **State Machine Guards Require Human Scrutiny:** Business logic invariants—such as ensuring payment is confirmed in escrow before shipment occurs—must be verified against formal acceptance criteria.
