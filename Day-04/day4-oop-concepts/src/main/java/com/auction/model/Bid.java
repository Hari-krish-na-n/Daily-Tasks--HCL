package com.auction.model;

import java.util.Objects;

/**
 * Bid class representing an offer placed by a bidder on an auction.
 * Demonstrates:
 * - Encapsulation (validation of amount > 0)
 * - equals() and hashCode() based on bidId identity
 * - toString() representation
 */
public class Bid {

    // Private fields
    private int bidId;
    private Bidder bidder;
    private Auction auction;
    private double amount;

    // Default constructor
    public Bid() {
        this(0, null, null, 1.0);
    }

    // Parameterized constructor with validation
    public Bid(int bidId, Bidder bidder, Auction auction, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bid amount must be greater than zero.");
        }
        this.bidId = bidId;
        this.bidder = bidder;
        this.auction = auction;
        this.amount = amount;
    }

    // Getters and Setters
    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public Bidder getBidder() {
        return bidder;
    }

    public void setBidder(Bidder bidder) {
        this.bidder = bidder;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Bid amount must be greater than zero.");
        }
        this.amount = amount;
    }

    /**
     * equals() implementation comparing Bid objects using bidId as the unique identity.
     * Easy to understand for beginners.
     */
    @Override
    public boolean equals(Object obj) {
        // 1. Check if both references point to the exact same object in memory
        if (this == obj) {
            return true;
        }

        // 2. Check if the other object is null or not of the same class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // 3. Cast to Bid and compare bidId
        Bid other = (Bid) obj;
        return this.bidId == other.bidId;
    }

    /**
     * hashCode() contract: if two objects are equal according to equals(),
     * they must return the same hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(bidId);
    }

    @Override
    public String toString() {
        return "Bid [ID=" + bidId +
               ", Bidder=" + (bidder != null ? bidder.getName() : "None") +
               ", AuctionID=" + (auction != null ? auction.getAuctionId() : "None") +
               ", Amount=" + amount + "]";
    }
}
