package com.auction.model;

/**
 * Bidder extends User and adds bidding-specific fields.
 *
 * Demonstrates:
 * - Multi-level inheritance: Bidder -> User -> BaseEntity
 * - Constructor chaining across three levels
 * - @Override on getSummary()
 */
public class Bidder extends User {

    private double maxBudget;
    private int totalBidsPlaced;

    // Default constructor
    public Bidder() {
        this(0, "Default Bidder", "bidder@auction.com", 100000.0);
    }

    // 3-arg constructor
    public Bidder(int id, String name, String email) {
        this(id, name, email, 100000.0);
    }

    // Legacy constructor
    public Bidder(int id, String name, String email, double maxBudget) {
        super(id, name, email, AuctionRole.BIDDER);
        this.maxBudget = maxBudget;
        this.totalBidsPlaced = 0;
    }

    // Full constructor including username and password
    public Bidder(int id, String username, String password, String name, String email, double maxBudget) {
        super(id, username, password, name, email, AuctionRole.BIDDER);
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

    @Override
    public String getSummary() {
        return super.getSummary() + ", maxBudget=" + maxBudget
                + ", bidsPlaced=" + totalBidsPlaced;
    }

    @Override
    public String toString() {
        return "Bidder[id=" + getId() + ", username=" + getUsername()
                + ", name=" + getName() + ", email=" + getEmail()
                + ", maxBudget=" + maxBudget + ", bidsPlaced=" + totalBidsPlaced + "]";
    }
}
