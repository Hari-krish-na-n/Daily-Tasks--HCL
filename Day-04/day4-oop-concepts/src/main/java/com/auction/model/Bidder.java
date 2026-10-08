package com.auction.model;

/**
 * Bidder class extending User.
 * Demonstrates:
 * - Inheritance ('extends User')
 * - 'super()' call to initialize base User fields
 * - Specialized fields and methods for a Bidder
 */
public class Bidder extends User {

    private double maxBudget;

    // Default constructor
    public Bidder() {
        super(0, "Default Bidder", "bidder@auction.com", "BIDDER");
        this.maxBudget = 100000.0;
    }

    // Constructor with basic user info - demonstrates super()
    public Bidder(int userId, String name, String email) {
        super(userId, name, email, "BIDDER");
        this.maxBudget = 100000.0;
    }

    // Overloaded constructor with max budget
    public Bidder(int userId, String name, String email, double maxBudget) {
        super(userId, name, email, "BIDDER");
        this.maxBudget = maxBudget;
    }

    public double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(double maxBudget) {
        if (maxBudget >= 0) {
            this.maxBudget = maxBudget;
        }
    }

    @Override
    public String toString() {
        return "Bidder [userId=" + getUserId() + ", name=" + getName() +
               ", email=" + getEmail() + ", role=" + getRole() +
               ", maxBudget=" + maxBudget + "]";
    }
}
