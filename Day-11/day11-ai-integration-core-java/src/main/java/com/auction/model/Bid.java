package com.auction.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Bid entity in the Online Auction System.
 * Represents a bid placed by a bidder on an auction listing.
 */
public class Bid extends BaseEntity implements Comparable<Bid> {

    private int auctionId;
    private int bidderId;
    private String bidderName;
    private double amount;
    private LocalDateTime bidTime;
    private boolean isProxy;

    public Bid() {
        super(0);
        this.bidTime = LocalDateTime.now();
    }

    public Bid(int id, int auctionId, int bidderId, String bidderName, double amount, LocalDateTime bidTime) {
        this(id, auctionId, bidderId, bidderName, amount, bidTime, false);
    }

    public Bid(int id, int auctionId, int bidderId, String bidderName, double amount,
               LocalDateTime bidTime, boolean isProxy) {
        super(id);
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.bidderName = bidderName != null ? bidderName : "Unknown";
        this.amount = amount;
        this.bidTime = bidTime != null ? bidTime : LocalDateTime.now();
        this.isProxy = isProxy;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public int getBidderId() {
        return bidderId;
    }

    public void setBidderId(int bidderId) {
        this.bidderId = bidderId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public void setProxy(boolean proxy) {
        isProxy = proxy;
    }

    @Override
    public int compareTo(Bid other) {
        if (other == null) return 1;
        // Natural order: highest amount first, then earlier time
        int cmp = Double.compare(other.amount, this.amount);
        if (cmp != 0) return cmp;
        return this.bidTime.compareTo(other.bidTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return getId() == bid.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String getSummary() {
        return String.format("Bid #%d: INR %,.2f by %s on Auction #%d (%s)%s",
                getId(), amount, bidderName, auctionId, bidTime, isProxy ? " [PROXY]" : "");
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
