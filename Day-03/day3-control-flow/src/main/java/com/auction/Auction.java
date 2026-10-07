package com.auction;

import java.util.ArrayList;
import java.util.List;

/**
 * Auction.java
 * -----------------------------------------------------------
 * Domain model representing an auction listing in the system.
 */
public class Auction {
    private final int id;
    private final String productName;
    private final String sellerName;
    private final double startingPrice;
    private final double reservePrice;
    private double currentBid;
    private String highestBidder;
    private String status; // PENDING, APPROVED, REJECTED, SOLD, UNSOLD
    private final List<Bid> bids;

    public Auction(int id, String productName, String sellerName, double startingPrice, double reservePrice) {
        this.id = id;
        this.productName = productName;
        this.sellerName = sellerName;
        this.startingPrice = startingPrice;
        this.reservePrice = reservePrice;
        this.currentBid = startingPrice;
        this.highestBidder = "None";
        this.status = "APPROVED"; // Seed auctions default to APPROVED
        this.bids = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        this.bids.add(bid);
        this.currentBid = bid.getAmount();
        this.highestBidder = bid.getBidderId();
    }

    @Override
    public String toString() {
        return String.format("Auction %d - %s - ₹%,.0f [Status: %s, Seller: %s]",
                id, productName, currentBid, status, sellerName);
    }
}
