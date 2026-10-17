package com.auction.model;

/**
 * Status of an auction item following FR7 closing and winner evaluation.
 */
public enum AuctionCloseStatus {
    SOLD("Sold to Highest Bidder"),
    UNSOLD("Reserve Price Not Met"),
    CANCELLED("Auction Cancelled Without Bids");

    private final String description;

    AuctionCloseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
