package com.auction.model;

/**
 * AuctionItem represents a product listed for bidding.
 * Extends BaseEntity to inherit id and audit timestamps.
 *
 * Demonstrates:
 * - Inheritance from an abstract class
 * - Constructor chaining using super()
 * - Overriding an abstract method (getSummary)
 * - Using @Override annotation correctly
 *
 * Day 5 - Task A: Inheritance
 */
public class AuctionItem extends BaseEntity {

    private String productName;
    private double startingPrice;
    private double currentBid;
    private boolean active;

    // Default constructor - chains to parameterized via this(), which then calls super()
    public AuctionItem() {
        this(0, "Unnamed Product", 0.0);
    }

    // 2-arg constructor - sets a default starting price
    public AuctionItem(int id, String productName) {
        this(id, productName, 0.0);
    }

    // Full constructor - calls super() to set up id and audit fields
    public AuctionItem(int id, String productName, double startingPrice) {
        super(id);  // passes id up to BaseEntity
        this.productName = productName;
        this.startingPrice = startingPrice;
        this.currentBid = startingPrice;
        this.active = true;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName != null && !productName.trim().isEmpty()) {
            this.productName = productName;
            markUpdated(); // inherited from BaseEntity
        }
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double amount) {
        if (amount >= this.currentBid) {
            this.currentBid = amount;
            markUpdated();
        }
    }

    public boolean isActive() {
        return active;
    }

    public void close() {
        this.active = false;
        markUpdated();
    }

    /**
     * Overrides abstract getSummary() declared in BaseEntity.
     * Every concrete entity must provide its own summary.
     */
    @Override
    public String getSummary() {
        return "AuctionItem[id=" + getId() + ", product=" + productName
                + ", currentBid=" + currentBid + ", active=" + active + "]";
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
