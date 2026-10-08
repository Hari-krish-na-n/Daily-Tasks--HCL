package com.auction.model;

/**
 * Auction class representing an item listed for bidding.
 * Demonstrates:
 * - Encapsulation (private fields, validated getters/setters)
 * - Constructor overloading and constructor chaining using this(...)
 * - Static vs Instance members (static auctionCount counter)
 * - 'this' keyword to access current instance members
 */
public class Auction {

    // Static member: shared across all Auction instances
    private static int auctionCount = 0;

    // Instance members: unique to each Auction object
    private int auctionId;
    private String productName;
    private Seller seller;
    private double startingPrice;
    private double reservePrice;
    private double currentBid;
    private boolean active;

    // 1. Overloaded Constructor: Default (no-arg) - chains to 2-arg constructor using this(...)
    public Auction() {
        this(0, "Unnamed Product");
    }

    // 2. Overloaded Constructor: 2-arg - chains to full parameterized constructor using this(...)
    public Auction(int auctionId, String productName) {
        this(auctionId, productName, null, 0.0, 0.0);
    }

    // 3. Overloaded Constructor: Full parameterized constructor (Chain terminus)
    public Auction(int auctionId, String productName, Seller seller, double startingPrice, double reservePrice) {
        this.auctionId = auctionId;
        this.productName = productName;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.reservePrice = reservePrice;
        this.currentBid = startingPrice;
        this.active = true;

        // Increment static counter whenever any Auction object is created
        auctionCount++;
    }

    // Static method: can be called without creating an Auction instance (Auction.getAuctionCount())
    public static int getAuctionCount() {
        return auctionCount;
    }

    // Static helper to reset count if needed
    public static void resetAuctionCount() {
        auctionCount = 0;
    }

    // Instance methods: operate on specific instance state
    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName != null && !productName.trim().isEmpty()) {
            this.productName = productName;
        }
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        if (startingPrice >= 0) {
            this.startingPrice = startingPrice;
        }
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(double reservePrice) {
        if (reservePrice >= 0) {
            this.reservePrice = reservePrice;
        }
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        if (currentBid >= this.currentBid) {
            this.currentBid = currentBid;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void closeAuction() {
        this.active = false;
    }

    // Package-private method: accessible within package com.auction.model
    void updateCurrentBidInternal(double amount) {
        this.currentBid = amount;
    }

    @Override
    public String toString() {
        return "Auction [ID=" + auctionId +
               ", Product=" + productName +
               ", Seller=" + (seller != null ? seller.getName() : "None") +
               ", StartingPrice=" + startingPrice +
               ", ReservePrice=" + reservePrice +
               ", CurrentBid=" + currentBid +
               ", Active=" + active + "]";
    }
}
