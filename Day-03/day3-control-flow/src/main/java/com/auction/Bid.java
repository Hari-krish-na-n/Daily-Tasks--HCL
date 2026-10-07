package com.auction;

/**
 * Bid.java
 * -----------------------------------------------------------
 * Domain model representing a bid placed on an auction listing.
 */
public class Bid {
    private final String bidderId;
    private final double amount;
    private final String timestamp;

    public Bid(String bidderId, double amount, String timestamp) {
        this.bidderId = bidderId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getBidderId() {
        return bidderId;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Bidder: %-12s | Amount: ₹%,.2f | Time: %s", bidderId, amount, timestamp);
    }
}
