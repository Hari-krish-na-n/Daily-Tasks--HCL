# Git Practice — Merge Conflict Demonstration
# This file is used ONLY for Day 5 Git training exercises.
# It lives in a dedicated practice branch and does NOT affect any production code.

## Auction Bidding Policy

Current rule: A bid must exceed the current bid by at least INR 500 (standard auctions)
or INR 1000 (premium auctions with a reserve floor).

# Conflict resolution note:
# branch-a proposed INR 500 minimum increment.
# branch-b proposed INR 1000 minimum increment.
# Resolution: both values are correct for different auction types.
# The strategy pattern (BidValidationStrategy) handles this distinction in code.
