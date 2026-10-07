# Agile and Scrum Notes
**Project:** Online Auction System  
**Training:** HCL Day 1 — Agile / Scrum Basics

---

## What is Agile?

**Agile** is a way of developing software in small, frequent steps instead of one big release at the end.

### The Old Way (Waterfall):
Plan everything → Design → Build everything → Test → Release (takes months or years)

### The Agile Way:
Plan a little → Build a little → Test a little → Release a little → Repeat

### Key Agile Principles (Simple Version):
1. **Deliver working software frequently** (every 2 weeks, not every 6 months)
2. **Welcome changing requirements** (even late in the project)
3. **Collaborate with the customer** throughout the project
4. **Respond to change** rather than following a rigid plan
5. **Keep it simple** — build only what is needed right now

### Why Agile?
- Faster feedback from customers
- Easier to fix mistakes early
- Team stays motivated with frequent wins
- Business gets value sooner

---

## What is Scrum?

**Scrum** is the most popular Agile framework. It gives structure to how Agile is practised.

Scrum uses fixed time periods called **Sprints** and three key roles:

| Role             | Responsibility                                          |
|------------------|---------------------------------------------------------|
| **Product Owner**| Decides WHAT to build — manages the Product Backlog    |
| **Scrum Master** | Ensures HOW the team works — removes blockers           |
| **Dev Team**     | Actually BUILDS the features — self-organising          |

---

## Scrum Events (Ceremonies)

### 🔹 Sprint
A **Sprint** is a fixed time box (usually **2 weeks**) during which the team builds a set of features.

- Same duration every time (consistent rhythm)
- At the end of every Sprint, working software is delivered
- No scope changes allowed during an active Sprint

**Example for our project:**
- Sprint 1 (Weeks 1-2): US-01 User Auth, US-02 Auction Listing
- Sprint 2 (Weeks 3-4): US-03 Admin Moderation, US-04 Bidding

---

### 🔹 Product Backlog
The **Product Backlog** is the complete ordered list of ALL features the product needs.

- Maintained by the **Product Owner**
- Items are ordered by priority (highest value at the top)
- Items at the top are detailed; items at the bottom are vague (refined later)
- It is NEVER "done" — it grows and changes as requirements evolve

**Our Online Auction System Product Backlog (high level):**
1. User Authentication (US-01) ← highest priority
2. Auction Listing (US-02)
3. Admin Moderation (US-03)
4. Bidding (US-04)
5. Proxy Bidding (US-05)
6. Anti-Sniping (US-06)
7. Auction Closing (US-07)
8. Settlement (US-08)

---

### 🔹 Sprint Backlog
The **Sprint Backlog** is the subset of Product Backlog items the team commits to completing in ONE Sprint.

- Selected during **Sprint Planning**
- Owned by the **Dev Team**
- Must be achievable within the Sprint's time box

**Example Sprint 1 Backlog:**

| Story | Points | Assigned To |
|-------|:------:|-------------|
| US-01 User Authentication | 3 | Dev A |
| US-02 Auction Listing | 5 | Dev B |
| US-03 Admin Moderation | 3 | Dev A |
| US-04 Bidding | 5 | Dev B |
| **Total** | **16** | |

---

### 🔹 Sprint Planning
A meeting at the **start of each Sprint** where the team:
1. Reviews the top Product Backlog items
2. Selects how many items they can complete (based on **velocity**)
3. Breaks stories into tasks and estimates

---

### 🔹 Daily Stand-up (Daily Scrum)
A **15-minute daily meeting** where each team member answers 3 questions:

1. **What did I do yesterday?**
2. **What will I do today?**
3. **Are there any blockers?**

Rules:
- Held at the same time every day (e.g., 9:30 AM)
- Maximum 15 minutes
- Everyone stands (encourages brevity)
- Detailed discussions happen AFTER the stand-up (not during)

**Example:**
> Dev A: "Yesterday I finished user registration. Today I'll start login. No blockers."  
> Dev B: "Yesterday I finished listing creation. Today I'll add input validation. Blocker: need the DB schema."

---

### 🔹 Sprint Review
A meeting at the **end of each Sprint** where the team:
- **Demonstrates** the working features to the Product Owner and stakeholders
- Gets **feedback** on what was built
- Product Owner accepts or rejects items

**Duration:** ~1 hour for a 2-week Sprint

---

### 🔹 Sprint Retrospective
A meeting at the **end of each Sprint** (after the Review) where the team reflects on HOW they worked.

The team discusses:
- **What went well?** (keep doing it)
- **What didn't go well?** (stop doing it)
- **What can we improve?** (start doing it)

**Duration:** ~45 minutes for a 2-week Sprint

**Example:**
- Went well: "Daily stand-ups were focused"
- Didn't go well: "User stories were not detailed enough before Sprint started"
- Improve: "Product Owner will refine the next Sprint's stories one week in advance"

---

## Key Scrum Concepts

### 🔹 User Story
A short description of a feature from the user's perspective.

**Format:**
> As a **\<role\>**, I want **\<feature\>**, so that **\<benefit\>**.

**Example:**
> As a **bidder**, I want to **place a bid that must exceed the current highest bid by at least INR 500**, so that **the auction is fair and competitive**.

---

### 🔹 Story Points
**Story Points** measure the **effort and complexity** of a user story — NOT time.

- They are relative estimates (compared to other stories)
- Use the **Fibonacci sequence**: 1, 2, 3, 5, 8, 13...
  - Why Fibonacci? Because the gaps between numbers grow — harder tasks are harder to estimate precisely
- A story worth 8 points is roughly twice as hard as a story worth 3-5 points

**Velocity** = the total story points the team completes in one Sprint.

| Story Points | Meaning                         |
|:------------:|---------------------------------|
| 1            | Trivial — nearly no effort      |
| 2            | Simple — straightforward        |
| 3            | Small — some logic required     |
| 5            | Medium — multiple moving parts  |
| 8            | Complex — significant effort    |
| 13           | Very large — consider splitting |

---

### 🔹 Definition of Done (DoD)
A shared checklist that defines when a user story is truly complete.

See `docs/DEFINITION_OF_DONE.md` for the full DoD for this project.

---

## Scrum Visual Summary

```
Product Backlog
(All Features)
     ↓
Sprint Planning
(Select items for Sprint)
     ↓
Sprint Backlog
(Items committed for this Sprint)
     ↓
SPRINT (2 weeks)
  ↓
  Daily Stand-up (every day, 15 min)
  ↓
  Build → Test → Review
     ↓
Working Software (Increment)
     ↓
Sprint Review   → Get feedback from stakeholders
Sprint Retro    → Improve the team's process
     ↓
Next Sprint (repeat)
```

---

## Agile vs Waterfall (Simple Comparison)

| Aspect             | Waterfall                       | Agile / Scrum                    |
|--------------------|---------------------------------|----------------------------------|
| Planning           | All upfront                     | Iterative, sprint by sprint      |
| Delivery           | One big release at the end      | Working software every 2 weeks   |
| Change handling    | Very hard to change mid-project | Welcomes change                  |
| Customer feedback  | Only at the end                 | Every Sprint                     |
| Risk               | High (late discovery of issues) | Low (issues caught early)        |
| Team communication | Siloed (department by department)| Collaborative, cross-functional  |

---

*End of Agile Notes Document*
