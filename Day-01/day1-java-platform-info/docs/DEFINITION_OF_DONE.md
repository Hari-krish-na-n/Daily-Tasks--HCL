# Definition of Done (DoD)
**Project:** Online Auction System  
**Training:** HCL Day 1 — Agile / Scrum Task

---

## What is the Definition of Done?

The **Definition of Done** is a shared agreement within the Scrum team.  
It is a checklist of conditions that EVERY user story (or task) must satisfy before it can be called **"Done"**.

It removes ambiguity. Without a DoD, one person's "done" might mean "it compiles". Another person's "done" might mean "fully tested and deployed". A DoD aligns the whole team.

---

## Project Definition of Done

A user story is considered **Done** only when ALL of the following conditions are met:

---

### Code Quality

- [ ] **Code compiles successfully** — `mvn clean compile` runs without any errors
- [ ] **No compilation errors** — zero warnings treated as errors, zero red underlines in IDE
- [ ] **Required functionality is implemented** — the feature described in the user story works as expected
- [ ] **Code follows Java naming conventions**:
  - Classes: `PascalCase` (e.g., `AuctionDataDemo`)
  - Methods & variables: `camelCase` (e.g., `startingPrice`, `calculateFee()`)
  - Constants: `UPPER_SNAKE_CASE` (e.g., `MINIMUM_BID_INCREMENT`)
  - Packages: all lowercase (e.g., `com.auction`)
- [ ] **No magic numbers** — all business rule values use constants from `AuctionConstants.java`
- [ ] **Meaningful variable and method names** — code is self-explanatory
- [ ] **Beginner-friendly comments** added to explain non-obvious logic

---

### Testing

- [ ] **Basic testing completed** — the feature has been manually tested with valid inputs
- [ ] **Edge cases tested** — tested with boundary values (e.g., bid exactly at minimum increment, reserve price exactly met)
- [ ] **Invalid input tested** — tested with wrong values to confirm error messages appear

---

### Error Handling

- [ ] **Exceptions handled where required** — no unchecked crashes (e.g., null input, invalid number format)
- [ ] **User-friendly error messages** displayed for invalid inputs

---

### Documentation

- [ ] **README.md updated** if a new feature or concept is added
- [ ] **Code comments** explain the purpose of each method and key logic block
- [ ] **User story** is updated in `docs/USER_STORIES.md` if scope changed

---

### Version Control (Git)

- [ ] **Git commit created** with a meaningful commit message (e.g., `Add bidding validation logic`)
- [ ] **Changes pushed to GitHub** — the remote repository is up to date
- [ ] **No unnecessary build artifacts committed**:
  - `target/` folder is NOT committed
  - `*.class` files are NOT committed
  - IDE files (`.idea/`, `.vscode/`, `*.iml`) are NOT committed
- [ ] **`.gitignore` is correctly configured** to exclude all of the above

---

### Project Structure

- [ ] All Java source files are in `src/main/java/com/auction/`
- [ ] Documentation files are in `docs/`
- [ ] `pom.xml` is valid and unchanged unless a dependency was intentionally added

---

## DoD Summary Checklist (Quick Reference)

```
[ ] mvn clean compile → SUCCESS
[ ] Feature works as described in the user story
[ ] Acceptance criteria verified
[ ] Java naming conventions followed
[ ] No magic numbers (use AuctionConstants)
[ ] Comments added
[ ] Manual testing done
[ ] Edge cases tested
[ ] Exceptions handled
[ ] Git commit made with meaningful message
[ ] Pushed to GitHub
[ ] target/ and *.class NOT committed
[ ] .gitignore configured
[ ] README updated if needed
```

---

*End of Definition of Done Document*
