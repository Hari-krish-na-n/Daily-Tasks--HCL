package com.auction.model;

/**
 * Bidder extends User and adds bidding-specific fields.
 *
 * Demonstrates:
 * - Multi-level inheritance: Bidder -> User -> BaseEntity
 * - Constructor chaining across three levels
 * - @Override on getSummary() to add bidder-specific info
 *
 * Day 5 - Task A: Inheritance
 */
public class Bidder extends User {

    private double maxBudget;
    private int totalBidsPlaced;

    // Default constructor
    public Bidder() {
        this(0, "Default Bidder", "bidder@auction.com", 100000.0);
    }

    // 3-arg constructor chains to the full one
    public Bidder(int id, String name, String email) {
        this(id, name, email, 100000.0);
    }

    // Full constructor: calls super(User), which calls super(BaseEntity)
    public Bidder(int id, String name, String email, double maxBudget) {
        super(id, name, email, AuctionRole.BIDDER); // chains to User
        this.maxBudget = maxBudget;
        this.totalBidsPlaced = 0;
    }

    public double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(double maxBudget) {
        if (maxBudget >= 0) {
            this.maxBudget = maxBudget;
            markUpdated();
        }
    }

    public int getTotalBidsPlaced() {
        return totalBidsPlaced;
    }

    public void incrementBidCount() {
        totalBidsPlaced++;
        markUpdated();
    }

    /**
     * Overrides User's getSummary() to include bidder-specific details.
     * Note: super.getSummary() is called to reuse the parent's output.
     */
    @Override
    public String getSummary() {
        return super.getSummary() + ", maxBudget=" + maxBudget
                + ", bidsPlaced=" + totalBidsPlaced;
    }

    @Override
    public String toString() {
        return "Bidder[id=" + getId() + ", name=" + getName()
                + ", email=" + getEmail()
                + ", maxBudget=" + maxBudget
                + ", bidsPlaced=" + totalBidsPlaced + "]";
    }
}
